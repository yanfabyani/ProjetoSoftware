package com.example.demo.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "perfil")
public class Perfil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;
    
    @Column(name = "tipo_perfil", nullable = false, length = 50)
    private String tipoPerfil;
    
    @ManyToMany
    @JoinTable(
        name = "perfil_habilidade",
        joinColumns = @JoinColumn(name = "perfil_id"),
        inverseJoinColumns = @JoinColumn(name = "habilidade_id")
    )
    private List<Habilidade> habilidades;
    
    @ManyToMany
    @JoinTable(
        name = "perfil_interesse",
        joinColumns = @JoinColumn(name = "perfil_id"),
        inverseJoinColumns = @JoinColumn(name = "interesse_id")
    )
    private List<Interesse> interesses;
    
    public Perfil() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Pessoa getPessoa() {
        return pessoa;
    }
    
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    public String getTipoPerfil() {
        return tipoPerfil;
    }
    
    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }
    
    public List<Habilidade> getHabilidades() {
        return habilidades;
    }
    
    public void setHabilidades(List<Habilidade> habilidades) {
        this.habilidades = habilidades;
    }
    
    public List<Interesse> getInteresses() {
        return interesses;
    }
    
    public void setInteresses(List<Interesse> interesses) {
        this.interesses = interesses;
    }
}
