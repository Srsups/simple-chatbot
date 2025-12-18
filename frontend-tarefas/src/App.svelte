<script>
  import { onMount } from 'svelte';

  let tarefas = [];
  let novaDescricao = "";

  // Função para buscar tarefas do Quarkus
  async function carregarTarefas() {
    const response = await fetch('http://localhost:8080/tarefas');
    tarefas = await response.json();
  }

  // Função para salvar nova tarefa
  async function adicionarTarefa() {
    if (!novaDescricao) return;

    try {
        const response = await fetch('http://localhost:8080/tarefas', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ 
            descricao: novaDescricao, 
            concluida: false 
          })
        });

        if (!response.ok) {
            console.error("Erro no servidor:", response.status);
            alert("Erro ao salvar tarefa!");
            return;
        }

        const tarefaSalva = await response.json();
        tarefas = [...tarefas, tarefaSalva]; 
        novaDescricao = "";
        
    } catch (erro) {
        console.error("Erro de conexão:", erro);
        alert("Não foi possível conectar ao servidor.");
    }
  }
  
  // Função para deletar
  async function deletarTarefa(id) {
      await fetch(`http://localhost:8080/tarefas/${id}`, {
          method: 'DELETE'
      });
      tarefas = tarefas.filter(t => t.id !== id);
  }

  // Roda assim que o componente é montado na tela
  onMount(() => {
    carregarTarefas();
  });
</script>

<main>
  <h1>Minhas Tarefas (Svelte + Quarkus)</h1>

  <div class="input-group">
    <input 
      type="text" 
      bind:value={novaDescricao} 
      placeholder="O que precisa ser feito?" 
      on:keydown={(e) => e.key === 'Enter' && adicionarTarefa()}
    />
    <button on:click={adicionarTarefa}>Adicionar</button>
  </div>

  <ul>
    {#each tarefas as tarefa (tarefa.id)}
      <li>
        <span>{tarefa.descricao}</span>
        <button class="delete-btn" on:click={() => deletarTarefa(tarefa.id)}>X</button>
      </li>
    {:else}
      <p>Nenhuma tarefa encontrada.</p>
    {/each}
  </ul>
</main>

<style>
  main { max-width: 500px; margin: 0 auto; font-family: sans-serif; text-align: center; }
  ul { list-style: none; padding: 0; }
  li { display: flex; justify-content: space-between; padding: 10px; border-bottom: 1px solid #ccc; }
  .input-group { display: flex; gap: 10px; justify-content: center; margin-bottom: 20px; }
  .delete-btn { background: red; color: white; border: none; cursor: pointer; padding: 5px 10px;}
</style>