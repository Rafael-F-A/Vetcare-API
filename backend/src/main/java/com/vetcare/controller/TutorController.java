package com.vetcare.controller;

import com.vetcare.model.entity.Tutor;
import com.vetcare.service.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tutores")
public class TutorController {
    
    @Autowired
    private TutorService tutorService;
    
    // GET - Listar todos os tutores
    @GetMapping
    public List<Tutor> getAllTutores() {
        return tutorService.findAll();
    }
    
    // GET - Buscar tutor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tutor> getTutorById(@PathVariable Long id) {
        return tutorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST - Criar novo tutor
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tutor createTutor(@RequestBody Tutor tutor) {
        return tutorService.create(tutor);
    }
    
    // PUT - Atualizar tutor
    @PutMapping("/{id}")
    public ResponseEntity<Tutor> updateTutor(@PathVariable Long id, @RequestBody Tutor tutor) {
        try {
            Tutor updated = tutorService.update(id, tutor);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE - Remover tutor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTutor(@PathVariable Long id) {
        tutorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}