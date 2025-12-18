# Chatbot Imobiliário com IA (RAG)

Sistema fullstack para consulta de imóveis utilizando linguagem natural.

## Tecnologias
- **Backend:** Java 21, Quarkus, LangChain4j
- **Frontend:** Svelte, Vite
- **IA:** GitHub Models (GPT-4o) + Embeddings Locais (ONNX)
- **Dados:** CSV processado em memória vetorial

## Como rodar
1. Configure a variável de ambiente: `export MY_GITHUB_TOKEN="seu_token"`
2. Backend: `mvn quarkus:dev`
3. Frontend: `npm run dev`
