package com.exemplo.bot;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import com.exemplo.bot.ai.AiBotService;

@Path("/bot")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BotResource {

    @Inject
    AiBotService aiBotService;

    // Classe interna para representar o JSON recebido
    public static class ChatRequest {
        public String sessionId;
        public String message;
    }

    // Classe interna para resposta
    public static class ChatResponse {
        public String reply;

        public ChatResponse(String reply) { this.reply = reply; }
    }

    @POST
    @Path("/ia")
    public ChatResponse conversarIA(ChatRequest request) {
        // Manda a pergunta para a IA
        String respostaIa = aiBotService.conversar(request.message);
        return new ChatResponse(respostaIa);
    }
}