package com.example.demo.repository;

import com.example.demo.domain.VoluntarioPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoluntarioPFRepository extends JpaRepository<VoluntarioPF, Long> {
    
    Optional<VoluntarioPF> findByCpf(String cpf);
}

