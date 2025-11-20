package com.example.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "certidao")
public class Certidao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;
    
    @Column(nullable = false, length = 100)
    private String tipo;
    
    @Column(nullable = false, length = 100)
    private String numero;
    
    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;
    
    @Column(name = "data_validade")
    private LocalDate dataValidade;
    
    @Column(name = "arquivo_path", length = 500)
    private String arquivoPath;
    
    @Column(nullable = false)
    private Boolean valido;
    
    public Certidao() {
        this.valido = true;
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
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public LocalDate getDataEmissao() {
        return dataEmissao;
    }
    
    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
    
    public LocalDate getDataValidade() {
        return dataValidade;
    }
    
    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }
    
    public String getArquivoPath() {
        return arquivoPath;
    }
    
    public void setArquivoPath(String arquivoPath) {
        this.arquivoPath = arquivoPath;
    }
    
    public Boolean getValido() {
        return valido;
    }
    
    public void setValido(Boolean valido) {
        this.valido = valido;
    }
}
