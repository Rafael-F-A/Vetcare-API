package com.vetcare.controller;

import com.vetcare.model.entity.Veterinario;
import com.vetcare.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioController {
    
    @Autowired
    private VeterinarioService veterinarioService;
    
    // GET - Listar todos
    @GetMapping
    public List<Veterinario> getAll() {
        return veterinarioService.findAll();
    }
    
    // GET - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Veterinario> getById(@PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.findById(id));
    }
    
    // GET - Buscar por CRM
    @GetMapping("/crmv/{crmv}")
    public ResponseEntity<Veterinario> getByCrmv(@PathVariable String crmv) {
        return ResponseEntity.ok(veterinarioService.findByCrmv(crmv));
    }
    
    // GET - Buscar por especialidade
    @GetMapping("/especialidade/{especialidade}")
    public List<Veterinario> getByEspecialidade(@PathVariable String especialidade) {
        return veterinarioService.findByEspecialidade(especialidade);
    }
    
    // POST - Criar veterinário
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Veterinario create(@RequestBody Veterinario veterinario) {
        return veterinarioService.create(veterinario);
    }
    
    // PUT - Atualizar veterinário
    @PutMapping("/{id}")
    public ResponseEntity<Veterinario> update(@PathVariable Long id, @RequestBody Veterinario veterinario) {
        return ResponseEntity.ok(veterinarioService.update(id, veterinario));
    }
    
    // DELETE - Remover veterinário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        veterinarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}