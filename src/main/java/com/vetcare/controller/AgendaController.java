package com.vetcare.controller;

import com.vetcare.model.entity.Agenda;
import com.vetcare.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {
    
    @Autowired
    private AgendaService agendaService;
    
    // GET - Listar todos os horários
    @GetMapping
    public List<Agenda> getAll() {
        return agendaService.findAll();
    }
    
    // GET - Buscar horário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Agenda> getById(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.findById(id));
    }
    
    // GET - Listar horários de um veterinário
    @GetMapping("/veterinario/{veterinarioId}")
    public List<Agenda> getByVeterinario(@PathVariable Long veterinarioId) {
        return agendaService.findByVeterinario(veterinarioId);
    }
    
    // GET - Listar todos os horários disponíveis
    @GetMapping("/disponiveis")
    public List<Agenda> getDisponiveis() {
        return agendaService.findDisponiveis();
    }
    
    // GET - Listar horários disponíveis de um veterinário
    @GetMapping("/disponiveis/veterinario/{veterinarioId}")
    public List<Agenda> getDisponiveisByVeterinario(@PathVariable Long veterinarioId) {
        return agendaService.findDisponiveisByVeterinario(veterinarioId);
    }
    
    // GET - Listar horários disponíveis em um período
    @GetMapping("/disponiveis/periodo")
    public List<Agenda> getDisponiveisByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return agendaService.findDisponiveisByPeriodo(inicio, fim);
    }
    
    // POST - Criar novo horário
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agenda create(@RequestBody Agenda agenda) {
        return agendaService.create(agenda);
    }
    
    // PUT - Atualizar horário completo
    @PutMapping("/{id}")
    public ResponseEntity<Agenda> update(@PathVariable Long id, @RequestBody Agenda agenda) {
        return ResponseEntity.ok(agendaService.update(id, agenda));
    }
    
    // PATCH - Alterar apenas a disponibilidade
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Agenda> alterarDisponibilidade(
            @PathVariable Long id, 
            @RequestParam Boolean disponivel) {
        return ResponseEntity.ok(agendaService.alterarDisponibilidade(id, disponivel));
    }
    
    // DELETE - Remover horário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agendaService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // DELETE - Remover todos os horários de um veterinário
    @DeleteMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<Void> deleteByVeterinario(@PathVariable Long veterinarioId) {
        agendaService.deleteByVeterinario(veterinarioId);
        return ResponseEntity.noContent().build();
    }
}