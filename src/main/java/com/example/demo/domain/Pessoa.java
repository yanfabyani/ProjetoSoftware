package com.example.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidato")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;
    
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipo;
    
    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private Afiliacao afiliacao;
    
    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private Perfil perfil;
    
    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private TermoCompromisso termoCompromisso;
    
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private java.util.List<EmailValidacao> emailValidacoes;
    
    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private ContaAcesso contaAcesso;
    
    public Pessoa() {
        this.dataCadastro = LocalDateTime.now();
        this.status = "AGUARDANDO_VALIDACAO";
    }
    
    // Getters and Setters
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public TipoPessoa getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoPessoa tipo) {
        this.tipo = tipo;
    }
    
    public Afiliacao getAfiliacao() {
        return afiliacao;
    }
    
    public void setAfiliacao(Afiliacao afiliacao) {
        this.afiliacao = afiliacao;
    }
    
    public Perfil getPerfil() {
        return perfil;
    }
    
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    
    public TermoCompromisso getTermoCompromisso() {
        return termoCompromisso;
    }
    
    public void setTermoCompromisso(TermoCompromisso termoCompromisso) {
        this.termoCompromisso = termoCompromisso;
    }
    
    public java.util.List<EmailValidacao> getEmailValidacoes() {
        return emailValidacoes;
    }
    
    public void setEmailValidacoes(java.util.List<EmailValidacao> emailValidacoes) {
        this.emailValidacoes = emailValidacoes;
    }
    
    public ContaAcesso getContaAcesso() {
        return contaAcesso;
    }
    
    public void setContaAcesso(ContaAcesso contaAcesso) {
        this.contaAcesso = contaAcesso;
    }
    
    public enum TipoPessoa {
        VOLUNTARIO_PF,
        VOLUNTARIO_PJ,
        ONG
    }
}

