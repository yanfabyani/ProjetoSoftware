package com.example.demo.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "voluntario_pj")
@PrimaryKeyJoinColumn(name = "pessoa_id")
public class VoluntarioPJ extends Pessoa {
    
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;
    
    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;
    
    @Column(name = "endereco_comercial", columnDefinition = "TEXT")
    private String enderecoComercial;
    
    @Column(name = "representante_legal")
    private String representanteLegal;
    
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<Certidao> certidoes;
    
    public VoluntarioPJ() {
        super();
        setTipo(TipoPessoa.VOLUNTARIO_PJ);
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
    
    public String getEnderecoComercial() {
        return enderecoComercial;
    }
    
    public void setEnderecoComercial(String enderecoComercial) {
        this.enderecoComercial = enderecoComercial;
    }
    
    public String getRepresentanteLegal() {
        return representanteLegal;
    }
    
    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }
    
    public List<Certidao> getCertidoes() {
        return certidoes;
    }
    
    public void setCertidoes(List<Certidao> certidoes) {
        this.certidoes = certidoes;
    }
}
