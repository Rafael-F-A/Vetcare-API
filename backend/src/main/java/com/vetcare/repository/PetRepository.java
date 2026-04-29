package com.vetcare.repository;

import com.vetcare.model.entity.Pet;
import com.vetcare.model.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByTutor(Tutor tutor);
    List<Pet> findByTutorId(Long tutorId);
}