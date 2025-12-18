package com.exemplo.bot;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class BotService {

    // Memória RAM: Guarda o estado de cada usuário (ID -> Estado)
    private Map<String, BotState> userStates = new ConcurrentHashMap<>();

    public String processarMensagem(String sessionId, String mensagemUsuario) {
        // Se não conhece o usuário, coloca no INICIO
        BotState estadoAtual = userStates.getOrDefault(sessionId, BotState.INICIO);
        
        // Lógica de transição de estados
        switch (estadoAtual) {
            case INICIO:
                userStates.put(sessionId, BotState.MENU_PRINCIPAL);
                return "Olá! Bem-vindo ao Sistema. Escolha uma opção:\n1. Financeiro\n2. Suporte Técnico";

            case MENU_PRINCIPAL:
                if (mensagemUsuario.equals("1")) {
                    userStates.put(sessionId, BotState.FINANCEIRO);
                    return "Você está no Financeiro.\nDigite 'boleto' para 2ª via ou 'voltar' para o menu.";
                } else if (mensagemUsuario.equals("2")) {
                    userStates.put(sessionId, BotState.SUPORTE);
                    return "Suporte Técnico.\nDescreva seu problema ou digite 'voltar'.";
                } else {
                    return "Opção inválida. Digite 1 ou 2.";
                }

            case FINANCEIRO:
                if (mensagemUsuario.equalsIgnoreCase("voltar")) {
                    userStates.put(sessionId, BotState.MENU_PRINCIPAL);
                    return "Voltou ao Menu.\n1. Financeiro\n2. Suporte";
                }
                if (mensagemUsuario.contains("boleto")) {
                    return "Aqui está seu boleto: www.exemplo.com/boleto.pdf (Simulação)";
                }
                return "Não entendi. Digite 'boleto' ou 'voltar'.";

            case SUPORTE:
                if (mensagemUsuario.equalsIgnoreCase("voltar")) {
                    userStates.put(sessionId, BotState.MENU_PRINCIPAL);
                    return "Voltou ao Menu.\n1. Financeiro\n2. Suporte";
                }
                // Aqui poderíamos salvar o ticket no banco
                return "Recebemos sua mensagem: '" + mensagemUsuario + "'. Um técnico entrará em contato.";

            default:
                userStates.put(sessionId, BotState.INICIO);
                return "Reiniciando conversa... Diga 'Oi'.";
        }
    }
}