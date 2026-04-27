package com.vetcare.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tutores")
public class Tutor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(unique = true, nullable = false, length = 14)
    private String cpf;
    
    @Column(length = 20)
    private String telefone;
    
    @Column(unique = true, length = 100)
    private String email;
    
    @Column(length = 200)
    private String endereco;
    
    private LocalDate dataCadastro;

    // Construtor padrão (obrigatório para JPA)
    public Tutor() {
    }

    // Construtor com parâmetros
    public Tutor(Long id, String nome, String cpf, String telefone, 
                 String email, String endereco, LocalDate dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.dataCadastro = dataCadastro;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}