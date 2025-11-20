package com.example.demo.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ong")
@PrimaryKeyJoinColumn(name = "pessoa_id")
public class ONG extends Pessoa {
    
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;
    
    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;
    
    @Column(columnDefinition = "TEXT")
    private String missao;
    
    @Column(name = "endereco_comercial", columnDefinition = "TEXT")
    private String enderecoComercial;
    
    @Column(name = "area_atuacao", length = 100)
    private String areaAtuacao;
    
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<Certidao> certidoes;
    
    public ONG() {
        super();
        setTipo(TipoPessoa.ONG);
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getRazaoSocial() {
        return razaoSocial;
    }
    
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
    
    public String getMissao() {
        return missao;
    }
    
    public void setMissao(String missao) {
        this.missao = missao;
    }
    
    public String getEnderecoComercial() {
        return enderecoComercial;
    }
    
    public void setEnderecoComercial(String enderecoComercial) {
        this.enderecoComercial = enderecoComercial;
    }
    
    public String getAreaAtuacao() {
        return areaAtuacao;
    }
    
    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }
    
    public List<Certidao> getCertidoes() {
        return certidoes;
    }
    
    public void setCertidoes(List<Certidao> certidoes) {
        this.certidoes = certidoes;
    }
}
