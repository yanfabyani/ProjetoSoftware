package com.example.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "voluntario_pf")
@PrimaryKeyJoinColumn(name = "pessoa_id")
public class VoluntarioPF extends Pessoa {
    
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
    
    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    private String nacionalidade;
    
    @Column(name = "endereco_residencial", columnDefinition = "TEXT")
    private String enderecoResidencial;
    
    @Column(length = 100)
    private String profissao;
    
    private Integer score;
    
    public VoluntarioPF() {
        super();
        setTipo(TipoPessoa.VOLUNTARIO_PF);
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public Sexo getSexo() {
        return sexo;
    }
    
    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }
    
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public String getNacionalidade() {
        return nacionalidade;
    }
    
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }
    
    public String getEnderecoResidencial() {
        return enderecoResidencial;
    }
    
    public void setEnderecoResidencial(String enderecoResidencial) {
        this.enderecoResidencial = enderecoResidencial;
    }
    
    public String getProfissao() {
        return profissao;
    }
    
    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public enum Sexo {
        M, F
    }
}
