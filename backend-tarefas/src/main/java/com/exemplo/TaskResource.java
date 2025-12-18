package com.exemplo;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/tarefas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @GET
    public List<Task> listar() {
        return Task.listAll();
    }

    @POST
    @Transactional // Necessário para operações de escrita
    public Task adicionar(Task task) {
        task.persist();
        return task;
    }
    
    @DELETE
    @Path("/{id}")
    @Transactional
    public void deletar(@PathParam("id") Long id) {
        Task.deleteById(id);
    }
}