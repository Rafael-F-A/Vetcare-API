package com.vetcare.service;

import com.vetcare.model.entity.Consulta;
import com.vetcare.model.enums.StatusConsulta;
import com.vetcare.repository.ConsultaRepository;
import com.vetcare.repository.PetRepository;
import com.vetcare.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    public Consulta create(Consulta consulta) {
        if (consulta.getPet() != null && consulta.getPet().getId() != null) {
            var pet = petRepository.findById(consulta.getPet().getId())
                    .orElseThrow(() -> new RuntimeException("Pet não encontrado"));
            consulta.setPet(pet);
        }

        if (consulta.getVeterinario() != null && consulta.getVeterinario().getId() != null) {
            var veterinario = veterinarioRepository.findById(consulta.getVeterinario().getId())
                    .orElseThrow(() -> new RuntimeException("Veterinário não encontrado"));
            consulta.setVeterinario(veterinario);
        }

        if (consulta.getStatus() == null) {
            consulta.setStatus(StatusConsulta.AGENDADA);
        }

        return consultaRepository.save(consulta);
    }

    public List<Consulta> findAll() {
        return consultaRepository.findAll();
    }

    public Optional<Consulta> findById(Long id) {
        return consultaRepository.findById(id);
    }

    public List<Consulta> findByPetId(Long petId) {
        return consultaRepository.findByPetId(petId);
    }

    public List<Consulta> findByVeterinarioId(Long veterinarioId) {
        return consultaRepository.findByVeterinarioId(veterinarioId);
    }

    public List<Consulta> findByStatus(StatusConsulta status) {
        return consultaRepository.findByStatus(status);
    }

    public Consulta update(Long id, Consulta consultaAtualizada) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        consulta.setDataHora(consultaAtualizada.getDataHora());
        consulta.setStatus(consultaAtualizada.getStatus());
        consulta.setObservacoes(consultaAtualizada.getObservacoes());
        consulta.setDiagnostico(consultaAtualizada.getDiagnostico());
        consulta.setPrescricao(consultaAtualizada.getPrescricao());

        return consultaRepository.save(consulta);
    }

    public Consulta updateStatus(Long id, StatusConsulta novoStatus) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        consulta.setStatus(novoStatus);
        return consultaRepository.save(consulta);
    }

    public void delete(Long id) {
        consultaRepository.deleteById(id);
    }
}