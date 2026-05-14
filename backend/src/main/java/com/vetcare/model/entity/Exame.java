package com.vetcare.model.entity;

import com.vetcare.model.enums.TipoExame;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exames")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoExame tipo;

    private LocalDate dataSolicitacao;
    private LocalDate dataRealizacao;

    @Column(length = 1000)
    private String resultado;

    @Column(length = 500)
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    // Construtores
    public Exame() {}

    public Exame(TipoExame tipo, Consulta consulta) {
        this.tipo = tipo;
        this.consulta = consulta;
        this.dataSolicitacao = LocalDate.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoExame getTipo() { return tipo; }
    public void setTipo(TipoExame tipo) { this.tipo = tipo; }

    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDate dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public LocalDate getDataRealizacao() { return dataRealizacao; }
    public void setDataRealizacao(LocalDate dataRealizacao) { this.dataRealizacao = dataRealizacao; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }
}