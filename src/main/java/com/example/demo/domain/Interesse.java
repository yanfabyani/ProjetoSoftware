package com.example.demo.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "interesse")
public class Interesse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(length = 50)
    private String area;
    
    @ManyToMany(mappedBy = "interesses")
    private List<Perfil> perfis;
    
    public Interesse() {
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
    
    public String getArea() {
        return area;
    }
    
    public void setArea(String area) {
        this.area = area;
    }
    
    public List<Perfil> getPerfis() {
        return perfis;
    }
    
    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
    }
}

