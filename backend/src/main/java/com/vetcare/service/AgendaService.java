package com.vetcare.service;

import com.vetcare.model.entity.Agenda;
import com.vetcare.model.entity.Veterinario;
import com.vetcare.repository.AgendaRepository;
import com.vetcare.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendaService {
    
    @Autowired
    private AgendaRepository agendaRepository;
    
    @Autowired
    private VeterinarioRepository veterinarioRepository;
    
    // Criar horário na agenda
    public Agenda create(Agenda agenda) {
        // Validar se o veterinário existe
        if (agenda.getVeterinario() == null || agenda.getVeterinario().getId() == null) {
            throw new RuntimeException("Veterinário é obrigatório");
        }
        
        Veterinario veterinario = veterinarioRepository.findById(agenda.getVeterinario().getId())
            .orElseThrow(() -> new RuntimeException("Veterinário não encontrado"));
        
        agenda.setVeterinario(veterinario);
        
        // Validar se horário já existe
        if (agendaRepository.existsByVeterinarioAndDataHoraInicio(veterinario, agenda.getDataHoraInicio())) {
            throw new RuntimeException("Já existe um horário para este veterinário neste dia/hora");
        }
        
        // Validar se horário de início é anterior ao fim
        if (agenda.getDataHoraInicio().isAfter(agenda.getDataHoraFim())) {
            throw new RuntimeException("Horário de início deve ser anterior ao horário de fim");
        }
        
        // Por padrão, horário é disponível
        if (agenda.getDisponivel() == null) {
            agenda.setDisponivel(true);
        }
        
        return agendaRepository.save(agenda);
    }
    
    // Listar todos os horários
    public List<Agenda> findAll() {
        return agendaRepository.findAll();
    }
    
    // Buscar horário por ID
    public Agenda findById(Long id) {
        return agendaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Horário não encontrado"));
    }
    
    // Listar horários de um veterinário
    public List<Agenda> findByVeterinario(Long veterinarioId) {
        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
            .orElseThrow(() -> new RuntimeException("Veterinário não encontrado"));
        return agendaRepository.findByVeterinario(veterinario);
    }
    
    // Listar todos os horários disponíveis
    public List<Agenda> findDisponiveis() {
        return agendaRepository.findByDisponivelTrue();
    }
    
    // Listar horários disponíveis de um veterinário
    public List<Agenda> findDisponiveisByVeterinario(Long veterinarioId) {
        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
            .orElseThrow(() -> new RuntimeException("Veterinário não encontrado"));
        return agendaRepository.findByVeterinarioAndDisponivelTrue(veterinario);
    }
    
    // Listar horários disponíveis em um período
    public List<Agenda> findDisponiveisByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return agendaRepository.findDisponiveisByPeriodo(inicio, fim);
    }
    
    // Atualizar horário
    public Agenda update(Long id, Agenda agendaAtualizada) {
        Agenda agenda = findById(id);
        
        agenda.setDataHoraInicio(agendaAtualizada.getDataHoraInicio());
        agenda.setDataHoraFim(agendaAtualizada.getDataHoraFim());
        agenda.setDisponivel(agendaAtualizada.getDisponivel());
        
        if (agendaAtualizada.getVeterinario() != null) {
            Veterinario veterinario = veterinarioRepository.findById(agendaAtualizada.getVeterinario().getId())
                .orElseThrow(() -> new RuntimeException("Veterinário não encontrado"));
            agenda.setVeterinario(veterinario);
        }
        
        return agendaRepository.save(agenda);
    }
    
    // Alterar disponibilidade (método específico)
    public Agenda alterarDisponibilidade(Long id, Boolean disponivel) {
        Agenda agenda = findById(id);
        agenda.setDisponivel(disponivel);
        return agendaRepository.save(agenda);
    }
    
    // Deletar horário
    public void delete(Long id) {
        Agenda agenda = findById(id);
        agendaRepository.delete(agenda);
    }
    
    // Deletar horários de um veterinário
    public void deleteByVeterinario(Long veterinarioId) {
        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
            .orElseThrow(() -> new RuntimeException("Veterinário não encontrado"));
        List<Agenda> agendas = agendaRepository.findByVeterinario(veterinario);
        agendaRepository.deleteAll(agendas);
    }
}