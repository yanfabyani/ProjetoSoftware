package com.example.demo.repository;

import com.example.demo.domain.TermoCompromisso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TermoCompromissoRepository extends JpaRepository<TermoCompromisso, Long> {
    
    Optional<TermoCompromisso> findFirstByOrderByIdDesc();
    
    Optional<TermoCompromisso> findByPessoaId(Long pessoaId);
}

