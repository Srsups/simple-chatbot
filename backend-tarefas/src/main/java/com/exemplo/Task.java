package com.exemplo;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Task extends PanacheEntity {
    public String descricao;
    public boolean concluida;
    
    // O Panache já fornece ID, getters, setters e métodos como persist(), listAll(), etc.
}