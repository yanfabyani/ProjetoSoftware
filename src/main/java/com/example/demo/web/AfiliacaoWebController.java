package com.example.demo.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.boundary.FormularioPF;
import com.example.demo.boundary.FormularioPJ;
import com.example.demo.boundary.FormularioPerfil;
import com.example.demo.controller.ControllerAfiliacao;
import com.example.demo.controller.ControllerEmail;
import com.example.demo.domain.EmailValidacao;
import com.example.demo.domain.Pessoa;
import com.example.demo.domain.TermoCompromisso;
import com.example.demo.repository.ContaAcessoRepository;
import com.example.demo.repository.EmailValidacaoRepository;
import com.example.demo.repository.PessoaRepository;
import com.example.demo.repository.VoluntarioPFRepository;
import com.example.demo.repository.VoluntarioPJRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/afiliacao")
public class AfiliacaoWebController {

    @Autowired
    private ControllerAfiliacao controllerAfiliacao;

    @Autowired
    private ControllerEmail controllerEmail;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EmailValidacaoRepository emailValidacaoRepository;

    @Autowired
    private VoluntarioPFRepository voluntarioPFRepository;

    @Autowired
    private VoluntarioPJRepository voluntarioPJRepository;

    @Autowired
    private ContaAcessoRepository contaAcessoRepository;

    // =================================================
    // TELA INICIAL
    // =================================================

    @GetMapping
    public String mostrarTelaInicial() {
        return "afiliacao/inicial";
    }

    // =================================================
    // FORMULÁRIO IDENTIFICAÇÃO
    // =================================================

    @GetMapping("/solicitar")
    public String escolherTipoAfiliacao() {
        return "afiliacao/escolher-tipo";
    }

    // =================================================
    // FORMULÁRIO PF
    // =================================================

    @GetMapping("/solicitar/pf")
    public String abrirFormularioPF(Model model) {
        if (!model.containsAttribute("formularioPF")) {
            model.addAttribute("formularioPF", new FormularioPF());
        }
        return "afiliacao/formulario-pf";
    }

    @PostMapping("/solicitar/pf")
    public String processarFormularioPF(
            @ModelAttribute("formularioPF") FormularioPF formularioPF,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!controllerAfiliacao.validarFormularioPF(formularioPF)) {
            model.addAttribute("erro", "Preencha todos os campos obrigatórios do formulário.");
            return "afiliacao/formulario-pf";
        }

        String email = formularioPF.getEmail().trim();

        String redirecionamento = verificarPessoaExistentePorEmail(
                email,
                redirectAttributes,
                "Já existe cadastro com este e-mail. Faça login para continuar.",
                "Você já possui um registro de afiliação, mas falta criar sua conta de acesso."
        );
        if (redirecionamento != null) {
            return redirecionamento;
        }

        String cpfLimpo = formularioPF.getCpf().replaceAll("[^0-9]", "");
        Optional<com.example.demo.domain.VoluntarioPF> vpfOpt =
                voluntarioPFRepository.findByCpf(cpfLimpo);
        if (vpfOpt.isPresent()) {
            return tratarPessoaExistente(
                    vpfOpt.get(),
                    redirectAttributes,
                    "Já existe cadastro com este CPF. Faça login.",
                    "Já existe cadastro com este CPF, mas falta criar sua conta."
            );
        }

        com.example.demo.domain.VoluntarioPF voluntarioPF =
                controllerAfiliacao.registrarVoluntarioPF(formularioPF);

        redirectAttributes.addFlashAttribute("pessoaId", voluntarioPF.getId());
        return "redirect:/afiliacao/perfil?pessoaId=" + voluntarioPF.getId();
    }

    // =================================================
    // FORMULÁRIO PJ
    // =================================================

    @GetMapping("/solicitar/pj")
    public String abrirFormularioPJ(Model model) {
        if (!model.containsAttribute("formularioPJ")) {
            model.addAttribute("formularioPJ", new FormularioPJ());
        }
        return "afiliacao/formulario-pj";
    }

    @PostMapping("/solicitar/pj")
    public String processarFormularioPJ(
            @ModelAttribute("formularioPJ") FormularioPJ formularioPJ,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!controllerAfiliacao.validarFormularioPJ(formularioPJ)) {
            model.addAttribute("erro", "Preencha todos os campos obrigatórios do formulário.");
            return "afiliacao/formulario-pj";
        }

        String email = formularioPJ.getEmail().trim();
        String redirecionamento = verificarPessoaExistentePorEmail(
                email,
                redirectAttributes,
                "Já existe cadastro com este e-mail. Faça login para continuar.",
                "Você já possui um registro de afiliação, mas falta criar sua conta de acesso."
        );
        if (redirecionamento != null) {
            return redirecionamento;
        }

        String cnpjLimpo = formularioPJ.getCnpj().replaceAll("[^0-9]", "");
        Optional<com.example.demo.domain.VoluntarioPJ> vpjOpt =
                voluntarioPJRepository.findByCnpj(cnpjLimpo);
        if (vpjOpt.isPresent()) {
            return tratarPessoaExistente(
                    vpjOpt.get(),
                    redirectAttributes,
                    "Já existe cadastro com este CNPJ. Faça login.",
                    "Já existe cadastro com este CNPJ, mas falta criar sua conta."
            );
        }

        com.example.demo.domain.VoluntarioPJ voluntarioPJ =
                controllerAfiliacao.registrarVoluntarioPJ(formularioPJ);

        redirectAttributes.addFlashAttribute("pessoaId", voluntarioPJ.getId());
        return "redirect:/afiliacao/certidao?pessoaId=" + voluntarioPJ.getId();
    }

    // =================================================
    // CERTIDÃO PJ
    // =================================================

    @GetMapping("/certidao")
    public String abrirTelaCertidao(
            Model model,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("pessoaId") Long pessoaIdFlash,
            @RequestParam(required = false) Long pessoaId) {

        Long pessoaIdFinal = (pessoaId != null) ? pessoaId : pessoaIdFlash;

        if (pessoaIdFinal == null) {
            redirectAttributes.addFlashAttribute("mensagem", "Informe os dados da pessoa jurídica para continuar.");
            return "redirect:/afiliacao/solicitar/pj";
        }

        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaIdFinal);
        if (pessoaOpt.isEmpty() || !(pessoaOpt.get() instanceof com.example.demo.domain.VoluntarioPJ)) {
            redirectAttributes.addFlashAttribute("mensagem", "Esta etapa é exclusiva para voluntários pessoa jurídica.");
            return "redirect:/afiliacao/solicitar/pj";
        }

        model.addAttribute("pessoaId", pessoaIdFinal);
        model.addAttribute("opcoesCertidao", obterOpcoesCertidao());
        return "afiliacao/certidao";
    }

    @PostMapping("/certidao")
    public String processarCertidao(
            @RequestParam Long pessoaId,
            @RequestParam(required = false) String certidaoSelecionada,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (certidaoSelecionada == null || certidaoSelecionada.isBlank()) {
            model.addAttribute("erro", "Selecione uma certidão para continuar.");
            model.addAttribute("pessoaId", pessoaId);
            model.addAttribute("opcoesCertidao", obterOpcoesCertidao());
            return "afiliacao/certidao";
        }

        redirectAttributes.addFlashAttribute("pessoaId", pessoaId);
        redirectAttributes.addFlashAttribute("certidaoSelecionada", certidaoSelecionada);
        return "redirect:/afiliacao/perfil?pessoaId=" + pessoaId;
    }

    // =================================================
    // FORMULÁRIO PERFIL
    // =================================================

    @GetMapping("/perfil")
    public String abrirFormularioPerfil(
            Model model,
            @ModelAttribute("pessoaId") Long pessoaIdFlash,
            @RequestParam(required = false) Long pessoaId) {

        Long pessoaIdFinal = (pessoaId != null) ? pessoaId : pessoaIdFlash;

        if (pessoaIdFinal == null) {
            return "redirect:/afiliacao/solicitar";
        }

        model.addAttribute("formulario", new FormularioPerfil());
        model.addAttribute("pessoaId", pessoaIdFinal);

        return "afiliacao/formulario-perfil";
    }

    @PostMapping("/perfil")
    public String processarFormularioPerfil(
            @RequestParam String habilidades,
            @RequestParam String interesses,
            @RequestParam Long pessoaId,
            Model model,
            RedirectAttributes redirectAttributes) {

        FormularioPerfil formulario = new FormularioPerfil();
        formulario.setHabilidades(
                java.util.Arrays.stream(habilidades.split("\n"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList()
        );

        formulario.setInteresses(
                java.util.Arrays.stream(interesses.split("\n"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList()
        );

        if (!controllerAfiliacao.validarPerfil(formulario)) {
            model.addAttribute("erro", "Informe ao menos uma habilidade e um interesse.");
            model.addAttribute("formulario", formulario);
            model.addAttribute("pessoaId", pessoaId);
            return "afiliacao/formulario-perfil";
        }

        controllerAfiliacao.salvarPerfil(pessoaId, formulario);

        redirectAttributes.addFlashAttribute("pessoaId", pessoaId);
        return "redirect:/afiliacao/termo?pessoaId=" + pessoaId;
    }

    // =================================================
    // TERMO
    // =================================================

    @GetMapping("/termo")
    public String exibirTermoCompromisso(
            Model model,
            @ModelAttribute("pessoaId") Long pessoaIdFlash,
            @RequestParam(required = false) Long pessoaId) {

        Long pessoaIdFinal = (pessoaId != null) ? pessoaId : pessoaIdFlash;

        if (pessoaIdFinal == null) {
            return "redirect:/afiliacao/solicitar";
        }

        TermoCompromisso termo = controllerAfiliacao.obterTermoAtual();

        model.addAttribute("termo", termo);
        model.addAttribute("pessoaId", pessoaIdFinal);

        return "afiliacao/termo-compromisso";
    }

    @PostMapping("/termo")
    public String processarTermoCompromisso(
            @RequestParam Long pessoaId,
            @RequestParam(required = false) Boolean aceitar,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        if (aceitar == null || !aceitar) {

            Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);

            if (pessoaOpt.isPresent()) {
                Pessoa pessoa = pessoaOpt.get();
                pessoa.setStatus("BLOQUEADO");
                pessoaRepository.save(pessoa);
            }

            model.addAttribute("mensagem",
                    "Processo de afiliação encerrado. Você precisa aceitar o termo para continuar.");
            model.addAttribute("pessoaId", pessoaId);
            model.addAttribute("termo", controllerAfiliacao.obterTermoAtual());

            return "afiliacao/termo-compromisso";
        }

        controllerAfiliacao.registrarAceite(pessoaId);

        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isPresent()) {
            Pessoa pessoa = pessoaOpt.get();
            String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
            controllerEmail.dispararEmailVerificacao(pessoa.getEmail(), baseUrl);
        }

        redirectAttributes.addFlashAttribute("pessoaId", pessoaId);
        return "redirect:/afiliacao/confirmacao-email?pessoaId=" + pessoaId;
    }

    // =================================================
    // CONFIRMAÇÃO DE EMAIL
    // =================================================

    @GetMapping("/confirmacao-email")
    public String mostrarMensagemConfirmacaoEmail(
            Model model,
            @ModelAttribute("pessoaId") Long pessoaIdFlash,
            @RequestParam(required = false) Long pessoaId) {

        Long pessoaIdFinal = (pessoaId != null) ? pessoaId : pessoaIdFlash;

        if (pessoaIdFinal == null) {
            return "redirect:/afiliacao/solicitar";
        }

        pessoaRepository.findById(pessoaIdFinal)
                .ifPresent(p -> model.addAttribute("email", p.getEmail()));

        return "afiliacao/confirmacao-email";
    }

    // =================================================
    // VALIDAR EMAIL
    // =================================================

    @GetMapping("/validar-email")
    public String validarEmail(@RequestParam String token, Model model) {

        if (controllerEmail.conferirTokenValido(token)) {

            Optional<EmailValidacao> emailValidacaoOpt =
                    emailValidacaoRepository.findByToken(token);

            if (emailValidacaoOpt.isPresent()) {
                Pessoa pessoa = emailValidacaoOpt.get().getPessoa();

                controllerEmail.marcarTokenComoUtilizado(token);

                controllerAfiliacao.atualizarStatusAfiliacao(
                        pessoa.getId(),
                        "AGUARDANDO_APROVACAO"
                );

                model.addAttribute("sucesso", true);
                model.addAttribute("mensagem",
                        "E-mail validado com sucesso! Sua afiliação está aguardando aprovação.");

            } else {
                model.addAttribute("sucesso", false);
                model.addAttribute("mensagem", "Token não encontrado.");
            }

        } else {
            model.addAttribute("sucesso", false);
            model.addAttribute("mensagem",
                    "Token inválido ou expirado. Solicite um novo link de validação.");
        }

        return "afiliacao/validacao-email";
    }

    // =================================================
    // MÉTODOS AUXILIARES
    // =================================================

    private String verificarPessoaExistentePorEmail(String email,
                                                    RedirectAttributes redirectAttributes,
                                                    String mensagemLogin,
                                                    String mensagemCadastro) {
        Optional<Pessoa> pessoaExistente = pessoaRepository.findByEmail(email);
        if (pessoaExistente.isPresent()) {
            return tratarPessoaExistente(pessoaExistente.get(), redirectAttributes, mensagemLogin, mensagemCadastro);
        }
        return null;
    }

    private String tratarPessoaExistente(Pessoa pessoa,
                                         RedirectAttributes redirectAttributes,
                                         String mensagemLogin,
                                         String mensagemCadastro) {
        boolean possuiConta = contaAcessoRepository.findByPessoaId(pessoa.getId()).isPresent();
        if (possuiConta) {
            redirectAttributes.addFlashAttribute("mensagem", mensagemLogin);
            return "redirect:/auth/login?email=" + pessoa.getEmail() + "&redirect=/auth/status";
        }
        redirectAttributes.addFlashAttribute("mensagem", mensagemCadastro);
        return "redirect:/auth/cadastro?email=" + pessoa.getEmail() + "&redirect=/auth/status";
    }

    private List<String> obterOpcoesCertidao() {
        return List.of(
                "Certidão Negativa Federal",
                "Certidão Negativa Trabalhista",
                "Certidão de Regularidade do FGTS",
                "Certidão Estadual",
                "Outra"
        );
    }
}
