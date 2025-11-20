package com.example.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "termo_compromisso")
public class TermoCompromisso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "pessoa_id", unique = true)
    private Pessoa pessoa;
    
    @Column(nullable = false, length = 20)
    private String versao;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;
    
    @Column(name = "data_aceite")
    private LocalDateTime dataAceite;
    
    @Column(nullable = false)
    private Boolean aceito;
    
    public TermoCompromisso() {
        this.aceito = false;
        this.versao = "1.0";
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
    
    public String getVersao() {
        return versao;
    }
    
    public void setVersao(String versao) {
        this.versao = versao;
    }
    
    public String getConteudo() {
        return conteudo;
    }
    
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
    
    public LocalDateTime getDataAceite() {
        return dataAceite;
    }
    
    public void setDataAceite(LocalDateTime dataAceite) {
        this.dataAceite = dataAceite;
    }
    
    public Boolean getAceito() {
        return aceito;
    }
    
    public void setAceito(Boolean aceito) {
        this.aceito = aceito;
    }
}
