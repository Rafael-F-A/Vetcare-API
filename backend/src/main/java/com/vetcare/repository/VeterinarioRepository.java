package com.vetcare.repository;

import java.util.List;
import com.vetcare.model.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    
    // Buscar por CRM (único)
    Optional<Veterinario> findByCrmv(String crmv);
    
    // Buscar por email (único)
    Optional<Veterinario> findByEmail(String email);
    
    // Buscar por especialidade
    List<Veterinario> findByEspecialidade(String especialidade);
    
    // Verificar se CRM já existe
    boolean existsByCrmv(String crmv);
    
    // Verificar se email já existe
    boolean existsByEmail(String email);
}