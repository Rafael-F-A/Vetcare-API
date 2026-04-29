package com.vetcare.repository;

import com.vetcare.model.entity.Consulta;
import com.vetcare.model.enums.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPetId(Long petId);
    List<Consulta> findByVeterinarioId(Long veterinarioId);
    List<Consulta> findByStatus(StatusConsulta status);
    List<Consulta> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}