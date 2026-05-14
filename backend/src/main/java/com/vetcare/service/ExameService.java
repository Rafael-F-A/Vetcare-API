package com.vetcare.service;

import com.vetcare.model.entity.Exame;
import com.vetcare.repository.ExameRepository;
import com.vetcare.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExameService {

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public Exame create(Exame exame) {
        // Valida se a consulta existe
        var consulta = consultaRepository.findById(exame.getConsulta().getId())
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        exame.setConsulta(consulta);
        exame.setDataSolicitacao(LocalDate.now());
        return exameRepository.save(exame);
    }

    public List<Exame> findAll() {
        return exameRepository.findAll();
    }

    public Optional<Exame> findById(Long id) {
        return exameRepository.findById(id);
    }

    public List<Exame> findByConsultaId(Long consultaId) {
        return exameRepository.findByConsultaId(consultaId);
    }

    public Exame update(Long id, Exame exameAtualizado) {
        Exame exame = exameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exame não encontrado"));
        exame.setTipo(exameAtualizado.getTipo());
        exame.setDataRealizacao(exameAtualizado.getDataRealizacao());
        exame.setResultado(exameAtualizado.getResultado());
        exame.setObservacoes(exameAtualizado.getObservacoes());
        return exameRepository.save(exame);
    }

    public void delete(Long id) {
        exameRepository.deleteById(id);
    }
}