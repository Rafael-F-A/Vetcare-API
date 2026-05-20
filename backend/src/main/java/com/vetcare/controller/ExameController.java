package com.vetcare.controller;

import com.vetcare.model.entity.Exame;
import com.vetcare.service.ExameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exames")
@CrossOrigin(origins = "*")
public class ExameController {

    @Autowired
    private ExameService exameService;

    @GetMapping
    public List<Exame> getAll() {
        return exameService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exame> getById(@PathVariable Long id) {
        return exameService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/consulta/{consultaId}")
    public List<Exame> getByConsultaId(@PathVariable Long consultaId) {
        return exameService.findByConsultaId(consultaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exame create(@RequestBody Exame exame) {
        return exameService.create(exame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exame> update(@PathVariable Long id, @RequestBody Exame exame) {
        try {
            return ResponseEntity.ok(exameService.update(id, exame));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}