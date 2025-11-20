package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.EmailValidacao;
import com.example.demo.domain.Pessoa;
import com.example.demo.repository.EmailValidacaoRepository;
import com.example.demo.repository.PessoaRepository;

@Service
public class ControllerEmail {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private EmailValidacaoRepository emailValidacaoRepository;
    
    @Autowired
    private PessoaRepository pessoaRepository;
    
    @Transactional
    public boolean dispararEmailVerificacao(String email, String baseUrl) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findByEmail(email);
        if (pessoaOpt.isEmpty()) {
            return false;
        }
        
        Pessoa pessoa = pessoaOpt.get();
        
        // Invalida tokens anteriores
        Optional<EmailValidacao> emailValidacaoOpt = emailValidacaoRepository.findByPessoaId(pessoa.getId());
        if (emailValidacaoOpt.isPresent()) {
            EmailValidacao emailValidacao = emailValidacaoOpt.get();
            emailValidacao.setUtilizado(true);
            emailValidacaoRepository.save(emailValidacao);
        }
        
        // Gera novo token
        String token = UUID.randomUUID().toString();
        EmailValidacao emailValidacao = new EmailValidacao();
        emailValidacao.setPessoa(pessoa);
        emailValidacao.setToken(token);
        emailValidacao.setDataEnvio(LocalDateTime.now());
        emailValidacao.setDataExpiracao(LocalDateTime.now().plusHours(24));
        emailValidacao.setUtilizado(false);
        
        emailValidacaoRepository.save(emailValidacao);
        
        // Envia e-mail
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Validação de E-mail - Rede Mais Social");
            message.setText("Olá " + pessoa.getNome() + ",\n\n" +
                    "Para validar seu e-mail e continuar o processo de afiliação, " +
                    "clique no link abaixo:\n\n" +
                    baseUrl + "/afiliacao/validar-email?token=" + token + "\n\n" +
                    "Este link é válido por 24 horas.\n\n" +
                    "Atenciosamente,\n" +
                    "Equipe Rede Mais Social");
            
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean conferirTokenValido(String token) {
        Optional<EmailValidacao> emailValidacaoOpt = emailValidacaoRepository.findByToken(token);
        if (emailValidacaoOpt.isEmpty()) {
            return false;
        }
        
        EmailValidacao emailValidacao = emailValidacaoOpt.get();
        return emailValidacao.isValido();
    }
    
    @Transactional
    public boolean marcarTokenComoUtilizado(String token) {
        Optional<EmailValidacao> emailValidacaoOpt = emailValidacaoRepository.findByToken(token);
        if (emailValidacaoOpt.isEmpty()) {
            return false;
        }
        
        EmailValidacao emailValidacao = emailValidacaoOpt.get();
        emailValidacao.setUtilizado(true);
        emailValidacaoRepository.save(emailValidacao);
        return true;
    }
    
    @Transactional
    public boolean enviarEmailRecuperacaoSenha(String email, String token, String baseUrl, String nome) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findByEmail(email);
        if (pessoaOpt.isEmpty()) {
            return false;
        }
        
        Pessoa pessoa = pessoaOpt.get();
        
        // Invalida tokens anteriores de recuperação
        Optional<EmailValidacao> emailValidacaoOpt = emailValidacaoRepository.findByPessoaId(pessoa.getId());
        if (emailValidacaoOpt.isPresent()) {
            EmailValidacao emailValidacao = emailValidacaoOpt.get();
            emailValidacao.setUtilizado(true);
            emailValidacaoRepository.save(emailValidacao);
        }
        
        // Cria novo token de recuperação
        EmailValidacao emailValidacao = new EmailValidacao();
        emailValidacao.setPessoa(pessoa);
        emailValidacao.setToken(token);
        emailValidacao.setDataEnvio(LocalDateTime.now());
        emailValidacao.setDataExpiracao(LocalDateTime.now().plusHours(24));
        emailValidacao.setUtilizado(false);
        
        emailValidacaoRepository.save(emailValidacao);
        
        // Envia e-mail
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Recuperação de Senha - Rede Mais Social");
            message.setText("Olá " + nome + ",\n\n" +
                    "Você solicitou a recuperação de senha. " +
                    "Para redefinir sua senha, clique no link abaixo:\n\n" +
                    baseUrl + "/auth/redefinir-senha?token=" + token + "\n\n" +
                    "Este link é válido por 24 horas.\n\n" +
                    "Se você não solicitou esta recuperação, ignore este e-mail.\n\n" +
                    "Atenciosamente,\n" +
                    "Equipe Rede Mais Social");
            
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public EmailValidacaoRepository getEmailValidacaoRepository() {
        return emailValidacaoRepository;
    }
}
