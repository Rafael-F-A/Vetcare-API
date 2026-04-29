package com.vetcare.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "veterinarios")
public class Veterinario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(unique = true, nullable = false, length = 20)
    private String crmv;
    
    @Column(length = 50)
    private String especialidade;
    
    @Column(length = 20)
    private String telefone;
    
    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    // Construtor padrão
    public Veterinario() {
    }

    // Construtor com parâmetros
    public Veterinario(Long id, String nome, String crmv, String especialidade, 
                       String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.crmv = crmv;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.email = email;
    }

    // Getters e Setters
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

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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
}