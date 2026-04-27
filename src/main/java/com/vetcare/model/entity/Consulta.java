package com.vetcare.model.entity;

import com.vetcare.model.enums.StatusConsulta;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
public class Consulta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime dataHora;
    
    @Enumerated(EnumType.STRING)
    private StatusConsulta status;
    
    @Column(length = 500)
    private String observacoes;
    
    @Column(length = 500)
    private String diagnostico;
    
    @Column(length = 500)
    private String prescricao;
    
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
    
    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    // Construtor padrão
    public Consulta() {
    }

    // Construtor com parâmetros
    public Consulta(Long id, LocalDateTime dataHora, StatusConsulta status, 
                    String observacoes, String diagnostico, String prescricao, 
                    Pet pet, Veterinario veterinario) {
        this.id = id;
        this.dataHora = dataHora;
        this.status = status;
        this.observacoes = observacoes;
        this.diagnostico = diagnostico;
        this.prescricao = prescricao;
        this.pet = pet;
        this.veterinario = veterinario;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }
}