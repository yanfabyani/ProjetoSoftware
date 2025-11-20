package com.example.demo.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "habilidade")
public class Habilidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(length = 50)
    private String categoria;
    
    @ManyToMany(mappedBy = "habilidades")
    private List<Perfil> perfis;
    
    public Habilidade() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public List<Perfil> getPerfis() {
        return perfis;
    }
    
    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
    }
}

