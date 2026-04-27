package com.vetcare.service;

import com.vetcare.model.entity.Tutor;
import com.vetcare.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TutorService {
    
    @Autowired
    private TutorRepository tutorRepository;
    
    // Criar tutor
    public Tutor create(Tutor tutor) {
        tutor.setDataCadastro(LocalDate.now());
        return tutorRepository.save(tutor);
    }
    
    // Listar todos
    public List<Tutor> findAll() {
        return tutorRepository.findAll();
    }
    
    // Buscar por ID
    public Optional<Tutor> findById(Long id) {
        return tutorRepository.findById(id);
    }
    
    // Atualizar tutor
    public Tutor update(Long id, Tutor tutorAtualizado) {
        Tutor tutor = tutorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tutor não encontrado"));
        
        tutor.setNome(tutorAtualizado.getNome());
        tutor.setTelefone(tutorAtualizado.getTelefone());
        tutor.setEmail(tutorAtualizado.getEmail());
        tutor.setEndereco(tutorAtualizado.getEndereco());
        
        return tutorRepository.save(tutor);
    }
    
    // Deletar tutor
    public void delete(Long id) {
        tutorRepository.deleteById(id);
    }
}