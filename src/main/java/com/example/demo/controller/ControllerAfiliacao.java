package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.boundary.FormularioIdentificacao;
import com.example.demo.boundary.FormularioPF;
import com.example.demo.boundary.FormularioPJ;
import com.example.demo.boundary.FormularioPerfil;
import com.example.demo.domain.Afiliacao;
import com.example.demo.domain.Habilidade;
import com.example.demo.domain.Interesse;
import com.example.demo.domain.Perfil;
import com.example.demo.domain.Pessoa;
import com.example.demo.domain.TermoCompromisso;
import com.example.demo.domain.VoluntarioPF;
import com.example.demo.domain.VoluntarioPJ;
import com.example.demo.repository.AfiliacaoRepository;
import com.example.demo.repository.HabilidadeRepository;
import com.example.demo.repository.InteresseRepository;
import com.example.demo.repository.PerfilRepository;
import com.example.demo.repository.PessoaRepository;
import com.example.demo.repository.TermoCompromissoRepository;
import com.example.demo.repository.VoluntarioPFRepository;
import com.example.demo.repository.VoluntarioPJRepository;

@Service
public class ControllerAfiliacao {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private VoluntarioPFRepository voluntarioPFRepository;

    @Autowired
    private VoluntarioPJRepository voluntarioPJRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private TermoCompromissoRepository termoCompromissoRepository;

    @Autowired
    private AfiliacaoRepository afiliacaoRepository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    @Autowired
    private InteresseRepository interesseRepository;

    // =========================
    // VALIDAÇÕES
    // =========================

    public boolean validarIdentificacao(FormularioIdentificacao form) {
        if (form == null) return false;

        if (form.getNome() == null || form.getNome().trim().isEmpty()) {
            return false;
        }
        if (form.getEmail() == null || form.getEmail().trim().isEmpty()) {
            return false;
        }
        if (form.getCpfOuCnpj() == null || form.getCpfOuCnpj().trim().isEmpty()) {
            return false;
        }
        if (form.getDataNascimento() == null) {
            return false;
        }
        return true;
    }

    public boolean validarFormularioPF(FormularioPF form) {
        if (form == null) return false;
        if (form.getNome() == null || form.getNome().trim().isEmpty()) return false;
        if (form.getEmail() == null || form.getEmail().trim().isEmpty()) return false;
        if (form.getCpf() == null || limparNumero(form.getCpf()).length() != 11) return false;
        if (form.getDataNascimento() == null) return false;
        if (form.getEndereco() == null || form.getEndereco().trim().isEmpty()) return false;
        if (form.getProfissao() == null || form.getProfissao().trim().isEmpty()) return false;
        if (form.getNacionalidade() == null || form.getNacionalidade().trim().isEmpty()) return false;
        return true;
    }

    public boolean validarFormularioPJ(FormularioPJ form) {
        if (form == null) return false;
        if (form.getEmail() == null || form.getEmail().trim().isEmpty()) return false;
        if (form.getNome() == null || form.getNome().trim().isEmpty()) return false;
        if (form.getCnpj() == null || limparNumero(form.getCnpj()).length() != 14) return false;
        if (form.getRazaoSocial() == null || form.getRazaoSocial().trim().isEmpty()) return false;
        if (form.getRepresentanteLegal() == null || form.getRepresentanteLegal().trim().isEmpty()) return false;
        if (form.getEnderecoComercial() == null || form.getEnderecoComercial().trim().isEmpty()) return false;
        return true;
    }

    public boolean validarPerfil(FormularioPerfil form) {
        if (form == null) return false;
        if (form.getHabilidades() == null || form.getHabilidades().isEmpty()) {
            return false;
        }
        if (form.getInteresses() == null || form.getInteresses().isEmpty()) {
            return false;
        }
        return true;
    }

    // =========================
    // PESSOA / VOLUNTÁRIO
    // =========================

    public Pessoa localizarOuCriarPessoa(String email, String cpfOuCnpj, FormularioIdentificacao form) {
        Optional<Pessoa> pessoaExistente = pessoaRepository.findByEmail(email);

        if (pessoaExistente.isPresent()) {
            Pessoa pessoa = pessoaExistente.get();
            atualizarDadosPessoa(pessoa, form);
            return pessoaRepository.save(pessoa);
        } else {
            return criarNovaPessoa(email, cpfOuCnpj, form);
        }
    }

    private void atualizarDadosPessoa(Pessoa pessoa, FormularioIdentificacao form) {
        pessoa.setNome(form.getNome());
        if (pessoa instanceof VoluntarioPF) {
            VoluntarioPF vpf = (VoluntarioPF) pessoa;
            if (form.getSexo() != null) {
                try {
                    vpf.setSexo(VoluntarioPF.Sexo.valueOf(form.getSexo()));
                } catch (IllegalArgumentException e) {
                    // ignora se o valor não for válido
                }
            }
            vpf.setDataNascimento(form.getDataNascimento());
            vpf.setNacionalidade(form.getNacionalidade());
            vpf.setEnderecoResidencial(form.getEnderecoResidencial());
            vpf.setProfissao(form.getProfissao());
        }
    }

    private Pessoa criarNovaPessoa(String email, String cpfOuCnpj, FormularioIdentificacao form) {
        // remove caracteres não numéricos do CPF/CNPJ
        String cpfCnpjLimpo = cpfOuCnpj.replaceAll("[^0-9]", "");

        // determina se é PF ou PJ baseado no tamanho
        if (cpfCnpjLimpo.length() == 11) {
            VoluntarioPF voluntarioPF = new VoluntarioPF();
            voluntarioPF.setEmail(email);
            voluntarioPF.setCpf(cpfCnpjLimpo);
            voluntarioPF.setNome(form.getNome());
            if (form.getSexo() != null) {
                try {
                    voluntarioPF.setSexo(VoluntarioPF.Sexo.valueOf(form.getSexo()));
                } catch (IllegalArgumentException e) {
                    // ignora se o valor não for válido
                }
            }
            voluntarioPF.setDataNascimento(form.getDataNascimento());
            voluntarioPF.setNacionalidade(form.getNacionalidade());
            voluntarioPF.setEnderecoResidencial(form.getEnderecoResidencial());
            voluntarioPF.setProfissao(form.getProfissao());
            return voluntarioPFRepository.save(voluntarioPF);
        } else {
            VoluntarioPJ voluntarioPJ = new VoluntarioPJ();
            voluntarioPJ.setEmail(email);
            voluntarioPJ.setCnpj(cpfCnpjLimpo);
            voluntarioPJ.setNome(form.getNome());
            voluntarioPJ.setRazaoSocial(form.getNome());
            return voluntarioPJRepository.save(voluntarioPJ);
        }
    }

    public VoluntarioPF registrarVoluntarioPF(FormularioPF form) {
        String cpfLimpo = limparNumero(form.getCpf());
        Optional<VoluntarioPF> existente = voluntarioPFRepository.findByCpf(cpfLimpo);
        VoluntarioPF voluntario = existente.orElse(new VoluntarioPF());

        voluntario.setCpf(cpfLimpo);
        voluntario.setEmail(form.getEmail());
        voluntario.setNome(form.getNome());
        voluntario.setEnderecoResidencial(form.getEndereco());
        voluntario.setProfissao(form.getProfissao());
        voluntario.setNacionalidade(form.getNacionalidade());
        voluntario.setDataNascimento(form.getDataNascimento());

        if (form.getSexo() != null && !form.getSexo().isBlank()) {
            try {
                voluntario.setSexo(VoluntarioPF.Sexo.valueOf(form.getSexo()));
            } catch (IllegalArgumentException e) {
                // ignora valores inválidos
            }
        }

        return voluntarioPFRepository.save(voluntario);
    }

    public VoluntarioPJ registrarVoluntarioPJ(FormularioPJ form) {
        String cnpjLimpo = limparNumero(form.getCnpj());
        Optional<VoluntarioPJ> existente = voluntarioPJRepository.findByCnpj(cnpjLimpo);
        VoluntarioPJ voluntario = existente.orElse(new VoluntarioPJ());

        voluntario.setCnpj(cnpjLimpo);
        voluntario.setEmail(form.getEmail());
        voluntario.setNome(form.getNome());
        voluntario.setRazaoSocial(form.getRazaoSocial());
        voluntario.setRepresentanteLegal(form.getRepresentanteLegal());
        voluntario.setEnderecoComercial(form.getEnderecoComercial());

        return voluntarioPJRepository.save(voluntario);
    }

    // =========================
    // PERFIL / HABILIDADES / INTERESSES
    // =========================

    @Transactional
    public boolean salvarPerfil(Long pessoaId, FormularioPerfil formPerfil) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            return false;
        }

        Pessoa pessoa = pessoaOpt.get();
        Optional<Perfil> perfilOpt = perfilRepository.findByPessoaId(pessoaId);

        Perfil perfil;
        if (perfilOpt.isPresent()) {
            perfil = perfilOpt.get();
        } else {
            perfil = new Perfil();
            perfil.setPessoa(pessoa);
            perfil.setTipoPerfil("VOLUNTARIO");
        }

        // processa habilidades
        List<Habilidade> habilidades = new ArrayList<>();
        if (formPerfil.getHabilidades() != null) {
            for (String nomeHabilidade : formPerfil.getHabilidades()) {
                Optional<Habilidade> habilidadeOpt = habilidadeRepository.findByNome(nomeHabilidade);
                Habilidade habilidade;
                if (habilidadeOpt.isPresent()) {
                    habilidade = habilidadeOpt.get();
                } else {
                    habilidade = new Habilidade();
                    habilidade.setNome(nomeHabilidade);
                    habilidade = habilidadeRepository.save(habilidade);
                }
                habilidades.add(habilidade);
            }
        }
        perfil.setHabilidades(habilidades);

        // processa interesses
        List<Interesse> interesses = new ArrayList<>();
        if (formPerfil.getInteresses() != null) {
            for (String nomeInteresse : formPerfil.getInteresses()) {
                Optional<Interesse> interesseOpt = interesseRepository.findByNome(nomeInteresse);
                Interesse interesse;
                if (interesseOpt.isPresent()) {
                    interesse = interesseOpt.get();
                } else {
                    interesse = new Interesse();
                    interesse.setNome(nomeInteresse);
                    interesse = interesseRepository.save(interesse);
                }
                interesses.add(interesse);
            }
        }
        perfil.setInteresses(interesses);

        perfilRepository.save(perfil);
        return true;
    }

    // =========================
    // TERMO / AFILIAÇÃO
    // =========================

    public TermoCompromisso obterTermoAtual() {
        Optional<TermoCompromisso> termoOpt = termoCompromissoRepository.findFirstByOrderByIdDesc();
        if (termoOpt.isPresent()) {
            return termoOpt.get();
        }

        // termo padrão se não houver nenhum cadastrado
        TermoCompromisso termo = new TermoCompromisso();
        termo.setConteudo("TERMO DE COMPROMISSO - REDE MAIS SOCIAL\n\n" +
                "Ao aceitar este termo, o candidato se compromete a:\n" +
                "1. Respeitar os valores e diretrizes da Rede Mais Social\n" +
                "2. Atuar de forma ética e responsável\n" +
                "3. Cumprir com os compromissos assumidos\n" +
                "4. Mantêm informações atualizadas\n\n" +
                "A Rede Mais Social se reserva o direito de aprovar ou rejeitar afiliações conforme critérios estabelecidos.");
        return termo;
    }

    @Transactional
    public boolean registrarAceite(Long pessoaId) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            return false;
        }

        Pessoa pessoa = pessoaOpt.get();

        Optional<TermoCompromisso> termoOpt = termoCompromissoRepository.findByPessoaId(pessoaId);
        TermoCompromisso termo;

        if (termoOpt.isPresent()) {
            termo = termoOpt.get();
        } else {
            termo = new TermoCompromisso();
            termo.setPessoa(pessoa);
            termo.setConteudo(obterTermoAtual().getConteudo());
        }

        termo.setAceito(true);
        termo.setDataAceite(LocalDateTime.now());
        termoCompromissoRepository.save(termo);

        pessoa.setStatus("AGUARDANDO_VALIDACAO");
        pessoaRepository.save(pessoa);

        return true;
    }

    @Transactional
    public boolean atualizarStatusAfiliacao(Long pessoaId, String novoStatus) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            return false;
        }

        Pessoa pessoa = pessoaOpt.get();
        pessoa.setStatus(novoStatus);
        pessoaRepository.save(pessoa);

        Optional<Afiliacao> afiliacaoOpt = afiliacaoRepository.findByPessoaId(pessoaId);
        Afiliacao afiliacao;

        if (afiliacaoOpt.isPresent()) {
            afiliacao = afiliacaoOpt.get();
        } else {
            afiliacao = new Afiliacao();
            afiliacao.setPessoa(pessoa);
        }

        afiliacao.setStatus(novoStatus);
        if ("APROVADO".equals(novoStatus)) {
            afiliacao.setDataAprovacao(LocalDateTime.now());
        }

        afiliacaoRepository.save(afiliacao);
        return true;
    }

    private String limparNumero(String valor) {
        return valor == null ? "" : valor.replaceAll("[^0-9]", "");
    }
}
