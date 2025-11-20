package com.example.demo.repository;

import com.example.demo.domain.Habilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HabilidadeRepository extends JpaRepository<Habilidade, Long> {
    
    Optional<Habilidade> findByNome(String nome);
}

