package com.vetcare.controller;

import com.vetcare.model.entity.Consulta;
import com.vetcare.model.enums.StatusConsulta;
import com.vetcare.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping
    public List<Consulta> getAll() {
        return consultaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> getById(@PathVariable Long id) {
        return consultaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pet/{petId}")
    public List<Consulta> getByPetId(@PathVariable Long petId) {
        return consultaService.findByPetId(petId);
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public List<Consulta> getByVeterinarioId(@PathVariable Long veterinarioId) {
        return consultaService.findByVeterinarioId(veterinarioId);
    }

    @GetMapping("/status/{status}")
    public List<Consulta> getByStatus(@PathVariable StatusConsulta status) {
        return consultaService.findByStatus(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consulta create(@RequestBody Consulta consulta) {
        return consultaService.create(consulta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> update(@PathVariable Long id, @RequestBody Consulta consulta) {
        try {
            return ResponseEntity.ok(consultaService.update(id, consulta));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Consulta> updateStatus(@PathVariable Long id, @RequestParam StatusConsulta status) {
        try {
            return ResponseEntity.ok(consultaService.updateStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        consultaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}