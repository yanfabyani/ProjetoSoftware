package com.example.demo.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.ContaAcesso;
import com.example.demo.domain.Pessoa;
import com.example.demo.repository.ContaAcessoRepository;
import com.example.demo.repository.PessoaRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthWebController {

    @Autowired
    private ContaAcessoRepository contaAcessoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    // =========================
    // LOGIN
    // =========================

    @GetMapping("/login")
    public String mostrarLogin(
            Model model,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String redirect,
            @RequestParam(required = false) String mensagem) {

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", email != null ? email : "");
        }

        if (!model.containsAttribute("redirect")) {
            String destino = (redirect != null && !redirect.isEmpty())
                    ? redirect
                    : "/auth/status"; // se não vier nada, depois do login vai para status
            model.addAttribute("redirect", destino);
        }

        if (!model.containsAttribute("mensagem") && mensagem != null && !mensagem.isBlank()) {
            model.addAttribute("mensagem", mensagem);
        }

        // view em templates/auth/login.html
        return "auth/login";
    }

    @PostMapping("/login")
    public String processarLogin(
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam(required = false) String redirect,
            HttpSession session,
            Model model) {

        boolean autenticado = false;

        Optional<ContaAcesso> contaOpt = contaAcessoRepository.findByLogin(email);
        if (contaOpt.isPresent()) {
            ContaAcesso conta = contaOpt.get();

            if (conta.getSenhaHash() != null
                    && conta.getSenhaHash().equals(senha)
                    && (conta.getAtivo() == null || conta.getAtivo())) {
                autenticado = true;
            }
        }

        if (autenticado) {
            Optional<Pessoa> pessoaOpt = pessoaRepository.findByEmail(email);
            if (pessoaOpt.isPresent()) {
                Pessoa pessoa = pessoaOpt.get();

                session.setAttribute("pessoaId", pessoa.getId());
                session.setAttribute("pessoaEmail", pessoa.getEmail());
                session.setAttribute("pessoaNome", pessoa.getNome());

                // se veio redirect, respeita; senão vai para tela de status
                String destino = (redirect != null && !redirect.isEmpty())
                        ? redirect
                        : "/auth/status";

                return "redirect:" + destino;
            }
        }

        // falha de login
        model.addAttribute("erro", "e-mail ou senha incorretos");
        model.addAttribute("email", email);
        model.addAttribute("redirect",
                (redirect != null && !redirect.isEmpty())
                        ? redirect
                        : "/auth/status");
        return "auth/login";
    }

    // =========================
    // TELA DE STATUS
    // =========================

    @GetMapping("/status")
    public String mostrarStatus(Model model, HttpSession session) {

        if (!model.containsAttribute("status")) {
            model.addAttribute("status", "AGUARDANDO_APROVACAO");
        }

        Object emailSessao = session.getAttribute("pessoaEmail");
        if (emailSessao != null && !model.containsAttribute("email")) {
            model.addAttribute("email", emailSessao.toString());
        }

        return "auth/status";
    }

    // =========================
    // CADASTRO DE CONTA
    // =========================

    @GetMapping("/cadastro")
    public String mostrarCadastro(
            Model model,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String redirect,
            @RequestParam(required = false) String mensagem) {

        if (!model.containsAttribute("email")) {
            model.addAttribute("email", email != null ? email : "");
        }

        if (!model.containsAttribute("redirect")) {
            String destino = (redirect != null && !redirect.isEmpty())
                    ? redirect
                    : "/auth/status";
            model.addAttribute("redirect", destino);
        }

        if (!model.containsAttribute("mensagem") && mensagem != null && !mensagem.isBlank()) {
            model.addAttribute("mensagem", mensagem);
        }

        return "auth/cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String confirmarSenha,
            @RequestParam(required = false) String redirect,
            Model model) {

        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não coincidem");
            model.addAttribute("email", email);
            model.addAttribute("redirect",
                    (redirect != null && !redirect.isEmpty())
                            ? redirect
                            : "/auth/status");
            return "auth/cadastro";
        }

        if (senha.length() < 6) {
            model.addAttribute("erro", "A senha deve ter pelo menos 6 caracteres");
            model.addAttribute("email", email);
            model.addAttribute("redirect",
                    (redirect != null && !redirect.isEmpty())
                            ? redirect
                            : "/auth/status");
            return "auth/cadastro";
        }

        Optional<Pessoa> pessoaOpt = pessoaRepository.findByEmail(email);
        if (pessoaOpt.isEmpty()) {
            model.addAttribute("erro", "Não foi encontrado cadastro de afiliação para este e-mail.");
            model.addAttribute("email", email);
            model.addAttribute("redirect",
                    (redirect != null && !redirect.isEmpty())
                            ? redirect
                            : "/auth/status");
            return "auth/cadastro";
        }

        Pessoa pessoa = pessoaOpt.get();

        Optional<ContaAcesso> contaExistente =
                contaAcessoRepository.findByPessoaId(pessoa.getId());

        if (contaExistente.isPresent()) {
            String destino = (redirect != null && !redirect.isEmpty())
                    ? redirect
                    : "/auth/status";
            return "redirect:/auth/login?email=" + email + "&redirect=" + destino;
        }

        ContaAcesso conta = new ContaAcesso();
        conta.setPessoa(pessoa);
        conta.setLogin(email);
        conta.setSenhaHash(senha);
        conta.setAtivo(true);
        contaAcessoRepository.save(conta);

        String destino = (redirect != null && !redirect.isEmpty())
                ? redirect
                : "/auth/status";

        return "redirect:/auth/login?email=" + email + "&redirect=" + destino;
    }

    // =========================
    // VERIFICAR SE TEM CONTA APÓS O FLUXO DE AFILIAÇÃO
    // =========================

    @GetMapping("/verificar-conta")
    public String verificarContaAposAfilicao(
            @RequestParam String email) {

        // 1 — verifica se existe pessoa
        Optional<Pessoa> pessoaOpt = pessoaRepository.findByEmail(email);

        if (pessoaOpt.isEmpty()) {
            // caso impossível: pessoa deveria existir após etapa 3
            // mas se acontecer, manda criar conta mesmo assim
            return "redirect:/auth/cadastro?email=" + email;
        }

        Pessoa pessoa = pessoaOpt.get();

        // 2 — verifica se já tem conta
        Optional<ContaAcesso> contaOpt = contaAcessoRepository.findByPessoaId(pessoa.getId());

        if (contaOpt.isPresent()) {
            // já tem conta → login
            return "redirect:/auth/login?email=" + email;
        }

        // 3 — não tem conta → ir para cadastro
        return "redirect:/auth/cadastro?email=" + email;
    }


    // =========================
    // RECUPERAR SENHA (stub simples)
    // =========================

    @PostMapping("/recuperar-senha")
    @ResponseBody
    public String solicitarRecuperacaoSenha(
            @RequestParam String email,
            HttpServletRequest request) {

        return "{\"sucesso\": true, \"mensagem\": \"Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha.\"}";
    }

    // =========================
    // REDEFINIR SENHA (stub)
    // =========================

    @GetMapping("/redefinir-senha")
    public String mostrarRedefinirSenha(
            @RequestParam String token,
            Model model) {

        model.addAttribute("token", token);
        return "auth/redefinir-senha";
    }

    @PostMapping("/redefinir-senha")
    public String processarRedefinirSenha(
            @RequestParam String token,
            @RequestParam String novaSenha,
            @RequestParam String confirmarSenha,
            Model model) {

        if (!novaSenha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não coincidem");
            model.addAttribute("token", token);
            return "auth/redefinir-senha";
        }

        if (novaSenha.length() < 6) {
            model.addAttribute("erro", "A senha deve ter pelo menos 6 caracteres");
            model.addAttribute("token", token);
            return "auth/redefinir-senha";
        }

        model.addAttribute("sucesso", true);
        model.addAttribute("mensagem", "Senha redefinida (simulação).");
        return "auth/senha-redefinida";
    }

    // =========================
    // LOGOUT
    // =========================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
