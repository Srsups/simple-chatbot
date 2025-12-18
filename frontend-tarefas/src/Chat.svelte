<script>
  import { onMount } from 'svelte';

  // Gera um ID aleatório para simular um usuário único a cada refresh
  const sessionId = "user-" + Math.floor(Math.random() * 10000);

  let mensagens = []; // Histórico do chat
  let inputUsuario = "";

  async function enviarMensagem() {
    if (!inputUsuario.trim()) return;

    // 1. Adiciona a mensagem do usuário na tela imediatamente
    const textoEnviado = inputUsuario;
    mensagens = [...mensagens, { remetente: 'voce', texto: textoEnviado }];
    inputUsuario = ""; // Limpa input

    try {
      // 2. Manda para o Quarkus
      const response = await fetch('http://localhost:8080/bot/ia', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
            sessionId: sessionId, 
            message: textoEnviado 
        })
      });

      const data = await response.json();

      // 3. Adiciona a resposta do Bot na tela
      mensagens = [...mensagens, { remetente: 'bot', texto: data.reply }];

    } catch (erro) {
      mensagens = [...mensagens, { remetente: 'bot', texto: "Erro de conexão..." }];
    }
  }

  // Manda um "olá" automático ao carregar para iniciar o bot
  onMount(() => {
    inputUsuario = "olá";
    enviarMensagem();
  });
</script>

<main>
  <h1>Chat de Atendimento</h1>
  
  <div class="chat-box">
    {#each mensagens as msg}
      <div class="mensagem {msg.remetente}">
        <strong>{msg.remetente === 'voce' ? 'Você' : 'Robô'}:</strong>
        <p>{@html msg.texto.replace(/\n/g, '<br>')}</p>
      </div>
    {/each}
  </div>

  <div class="controles">
    <input 
      type="text" 
      bind:value={inputUsuario} 
      placeholder="Digite sua resposta..." 
      on:keydown={(e) => e.key === 'Enter' && enviarMensagem()}
    />
    <button on:click={enviarMensagem}>Enviar</button>
  </div>
</main>

<style>
  .chat-box { 
      border: 1px solid #ccc; height: 300px; overflow-y: auto; 
      padding: 10px; margin-bottom: 10px; display: flex; flex-direction: column; gap: 10px;
  }
  .mensagem { padding: 8px 12px; border-radius: 8px; max-width: 80%; }
  .mensagem.voce { align-self: flex-end; background-color: #000000; text-align: right; }
  .mensagem.bot { align-self: flex-start; background-color: #000000; border: 1px solid #ddd; }
  .controles { display: flex; gap: 10px; }
  input { flex: 1; padding: 8px; }
</style>