package com.example.demo.repository;

import com.example.demo.domain.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
    Optional<Pessoa> findByEmail(String email);
    
    Optional<Pessoa> findByEmailOrId(String email, Long id);
}

