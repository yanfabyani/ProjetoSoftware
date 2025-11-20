package com.example.demo.repository;

import com.example.demo.domain.ContaAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContaAcessoRepository extends JpaRepository<ContaAcesso, Long> {
    
    Optional<ContaAcesso> findByPessoaId(Long pessoaId);
    
    Optional<ContaAcesso> findByLogin(String login);
}

