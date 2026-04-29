package com.vetcare.repository;

import com.vetcare.model.entity.Agenda;
import com.vetcare.model.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    
    // Buscar horários por veterinário
    List<Agenda> findByVeterinario(Veterinario veterinario);
    
    // Buscar horários disponíveis (disponivel = true)
    List<Agenda> findByDisponivelTrue();
    
    // Buscar horários disponíveis por veterinário
    List<Agenda> findByVeterinarioAndDisponivelTrue(Veterinario veterinario);
    
    // Buscar horários em um período
    List<Agenda> findByDataHoraInicioBetween(LocalDateTime inicio, LocalDateTime fim);
    
    // Buscar horários disponíveis em um período
    @Query("SELECT a FROM Agenda a WHERE a.disponivel = true AND a.dataHoraInicio BETWEEN :inicio AND :fim")
    List<Agenda> findDisponiveisByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    // Verificar se veterinário já tem horário no mesmo dia/hora
    boolean existsByVeterinarioAndDataHoraInicio(Veterinario veterinario, LocalDateTime dataHoraInicio);
}