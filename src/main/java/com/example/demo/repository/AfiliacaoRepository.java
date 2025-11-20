package com.example.demo.repository;

import com.example.demo.domain.Afiliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AfiliacaoRepository extends JpaRepository<Afiliacao, Long> {
    
    Optional<Afiliacao> findByPessoaId(Long pessoaId);
}

