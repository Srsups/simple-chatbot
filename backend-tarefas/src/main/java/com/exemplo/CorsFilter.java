package com.exemplo;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // Permite qualquer origem (para desenvolvimento)
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        
        // Permite as credenciais, se necessário
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        
        // Permite os métodos que o Svelte vai usar
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        
        // Permite os cabeçalhos que o JSON precisa
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    }
}