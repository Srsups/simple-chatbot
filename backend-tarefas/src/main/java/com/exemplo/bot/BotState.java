package com.exemplo.bot;

public enum BotState {
    INICIO,          // O usuário acabou de chegar
    MENU_PRINCIPAL,  // O bot mostrou as opções principais
    FINANCEIRO,      // Submenu financeiro
    SUPORTE,         // Submenu suporte
    ENCERRADO        // Conversa finalizada
}