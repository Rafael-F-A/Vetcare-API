package com.vetcare.service;

import com.vetcare.model.entity.Pet;
import com.vetcare.model.entity.Tutor;
import com.vetcare.repository.PetRepository;
import com.vetcare.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    public Pet create(Pet pet) {
        // Verifica se o tutor existe
        if (pet.getTutor() != null && pet.getTutor().getId() != null) {
            Tutor tutor = tutorRepository.findById(pet.getTutor().getId())
                    .orElseThrow(() -> new RuntimeException("Tutor não encontrado"));
            pet.setTutor(tutor);
        }
        return petRepository.save(pet);
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Optional<Pet> findById(Long id) {
        return petRepository.findById(id);
    }

    public List<Pet> findByTutorId(Long tutorId) {
        return petRepository.findByTutorId(tutorId);
    }

    public Pet update(Long id, Pet petAtualizado) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));
        
        pet.setNome(petAtualizado.getNome());
        pet.setEspecie(petAtualizado.getEspecie());
        pet.setRaca(petAtualizado.getRaca());
        pet.setDataNascimento(petAtualizado.getDataNascimento());
        pet.setCor(petAtualizado.getCor());
        pet.setPeso(petAtualizado.getPeso());
        
        // Se o tutor foi alterado, valida
        if (petAtualizado.getTutor() != null && petAtualizado.getTutor().getId() != null) {
            Tutor tutor = tutorRepository.findById(petAtualizado.getTutor().getId())
                    .orElseThrow(() -> new RuntimeException("Tutor não encontrado"));
            pet.setTutor(tutor);
        }
        
        return petRepository.save(pet);
    }

    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}