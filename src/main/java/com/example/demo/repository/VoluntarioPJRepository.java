package com.example.demo.repository;

import com.example.demo.domain.VoluntarioPJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoluntarioPJRepository extends JpaRepository<VoluntarioPJ, Long> {
    
    Optional<VoluntarioPJ> findByCnpj(String cnpj);
}

