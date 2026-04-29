package com.vetcare.model.entity;

import com.vetcare.model.enums.EspeciePet;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pets")
public class Pet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String nome;
    
    @Enumerated(EnumType.STRING)
    private EspeciePet especie;
    
    @Column(length = 50)
    private String raca;
    
    private LocalDate dataNascimento;
    
    @Column(length = 20)
    private String cor;
    
    private Double peso;
    
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    // Construtor padrão
    public Pet() {
    }

    // Construtor com parâmetros
    public Pet(Long id, String nome, EspeciePet especie, String raca, 
               LocalDate dataNascimento, String cor, Double peso, Tutor tutor) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.cor = cor;
        this.peso = peso;
        this.tutor = tutor;
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

    public EspeciePet getEspecie() {
        return especie;
    }

    public void setEspecie(EspeciePet especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
}