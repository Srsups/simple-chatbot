package com.exemplo.bot.ai;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.enterprise.inject.Produces;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CsvIngestor {

    @Produces
    @Singleton
    public EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Produces
    @ApplicationScoped
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> store, EmbeddingModel model) {
        // Aumentei maxResults para 5 para dar mais opções ao usuário
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .embeddingModel(model)
                .maxResults(15) 
                .minScore(0.5) 
                .build();
    }

    @Produces
    @ApplicationScoped
    public RetrievalAugmentor retrievalAugmentor(ContentRetriever contentRetriever) {
        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .build();
    }

    public void ingest(@Observes StartupEvent event, 
                       EmbeddingStore<TextSegment> store, 
                       EmbeddingModel model) {
        
        System.out.println("--- INICIANDO LEITURA DOS IMÓVEIS ---");
        
        // Agora lendo 'data.csv' em vez de 'dados.csv'
        try (InputStream is = getClass().getResourceAsStream("/data.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                System.out.println("ERRO: Arquivo data.csv não encontrado!");
                return;
            }

            List<TextSegment> segmentos = new ArrayList<>();
            String linha;
            boolean cabecalho = true;
            int count = 0;

            while ((linha = reader.readLine()) != null) {
                if (cabecalho) { cabecalho = false; continue; }

                // O CSV de imóveis usa vírgula como separador
                // Usamos uma regex simples para evitar quebrar vírgulas dentro de aspas (se houver)
                String[] colunas = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                // Mapeamento baseado no seu CSV:
                // 0:address, 1:district, 2:area, 3:bedrooms, 4:garage, 5:type, 6:rent, 7:total
                if (colunas.length >= 8) {
                    try {
                        String endereco = colunas[0].replace("\"", "");
                        String bairro = colunas[1].replace("\"", "");
                        String area = colunas[2];
                        String quartos = colunas[3];
                        String garagem = colunas[4];
                        String tipo = colunas[5];
                        String precoTotal = colunas[7]; // Usando o total (Aluguel + Condomínio)

                        // Montamos um "anúncio" para a IA indexar
                        String descricaoImovel = String.format(
                            "Imóvel para alugar: %s localizado em %s (%s). " +
                            "Possui %s m², %s quartos e %s vagas de garagem. " +
                            "O valor total mensal é R$ %s.",
                            tipo, bairro, endereco, area, quartos, garagem, precoTotal
                        );

                        segmentos.add(TextSegment.from(descricaoImovel));
                        count++;
                        
                        // Limitador de segurança para não estourar a memória RAM no teste (opcional)
                        // Se quiser carregar todos os 11mil, remova este if.
                        // if (count >= 2000) break; 

                    } catch (Exception e) {
                        // Ignora linhas com erro de formatação
                    }
                }
            }

            if (!segmentos.isEmpty()) {
                store.addAll(model.embedAll(segmentos).content(), segmentos);
                System.out.println("--- BASE DE IMÓVEIS CARREGADA: " + segmentos.size() + " ofertas ---");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}