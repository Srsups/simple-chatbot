package com.exemplo.bot.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface AiBotService {

    @SystemMessage("Você é um consultor imobiliário especializado. " +
                   "Você tem acesso a uma lista de imóveis. " +
                   "INSTRUÇÕES CRÍTICAS: " +
                   "1. Use APENAS os imóveis fornecidos no contexto. " +
                   "2. Se o usuário pedir para ver opções (ex: 'todos do bairro X'), liste a maioria das opções que foram recuperadas no contexto. Não faça resumos do tipo 'aqui estão alguns exemplos'. Mostre entre 10 a 15 exemplos. " +
                   "3. Para cada imóvel, mostre: Tipo, Bairro, Quartos e Preço Total. " +
                   "4. Se não houver imóveis no contexto exato, avise que não encontrou nessa busca específica.")
    String conversar(@UserMessage String pergunta);
}