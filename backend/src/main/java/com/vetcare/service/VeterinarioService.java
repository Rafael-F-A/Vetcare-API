package com.vetcare.service;

import com.vetcare.model.entity.Veterinario;
import com.vetcare.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VeterinarioService {
    
    @Autowired
    private VeterinarioRepository veterinarioRepository;
    
    // Criar veterinário
    public Veterinario create(Veterinario veterinario) {
        // Validar se CRM já existe
        if (veterinarioRepository.existsByCrmv(veterinario.getCrmv())) {
            throw new RuntimeException("CRM já cadastrado: " + veterinario.getCrmv());
        }
        
        // Validar se email já existe
        if (veterinario.getEmail() != null && veterinarioRepository.existsByEmail(veterinario.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + veterinario.getEmail());
        }
        
        veterinario.setDataCadastro(LocalDateTime.now());
        return veterinarioRepository.save(veterinario);
    }
    
    // Listar todos
    public List<Veterinario> findAll() {
        return veterinarioRepository.findAll();
    }
    
    // Buscar por ID
    public Veterinario findById(Long id) {
        return veterinarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Veterinário não encontrado com ID: " + id));
    }
    
    // Buscar por CRM
    public Veterinario findByCrmv(String crmv) {
        return veterinarioRepository.findByCrmv(crmv)
            .orElseThrow(() -> new RuntimeException("Veterinário não encontrado com CRM: " + crmv));
    }
    
    // Buscar por especialidade
    public List<Veterinario> findByEspecialidade(String especialidade) {
        return veterinarioRepository.findByEspecialidade(especialidade);
    }
    
    // Atualizar veterinário
    public Veterinario update(Long id, Veterinario veterinarioAtualizado) {
        Veterinario veterinario = findById(id);
        
        veterinario.setNome(veterinarioAtualizado.getNome());
        veterinario.setEspecialidade(veterinarioAtualizado.getEspecialidade());
        veterinario.setTelefone(veterinarioAtualizado.getTelefone());
        
        if (veterinarioAtualizado.getEmail() != null && 
            !veterinarioAtualizado.getEmail().equals(veterinario.getEmail())) {
            
            if (veterinarioRepository.existsByEmail(veterinarioAtualizado.getEmail())) {
                throw new RuntimeException("Email já cadastrado: " + veterinarioAtualizado.getEmail());
            }
            veterinario.setEmail(veterinarioAtualizado.getEmail());
        }
        
        return veterinarioRepository.save(veterinario);
    }
    
    // Deletar veterinário
    public void delete(Long id) {
        Veterinario veterinario = findById(id);
        veterinarioRepository.delete(veterinario);
    }
}