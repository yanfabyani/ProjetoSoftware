package com.example.demo.repository;

import com.example.demo.domain.Interesse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InteresseRepository extends JpaRepository<Interesse, Long> {
    
    Optional<Interesse> findByNome(String nome);
}

