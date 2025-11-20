-- =====================================================
-- Script de Criação do Banco de Dados
-- Rede Mais Social - Projeto Software
-- =====================================================
-- Este script cria o banco de dados e todas as tabelas
-- necessárias para o sistema, seguindo normalização
-- e as especificações das entidades JPA.
-- =====================================================

-- Remove o banco de dados se existir
DROP DATABASE IF EXISTS rede_mais_social;

-- Cria o banco de dados
CREATE DATABASE IF NOT EXISTS rede_mais_social
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Seleciona o banco de dados
USE rede_mais_social;

-- =====================================================
-- TABELA BASE: candidato
-- Tabela abstrata base para herança (JOINED strategy)
-- =====================================================
CREATE TABLE candidato (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    data_cadastro DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'AGUARDANDO_VALIDACAO',
    tipo ENUM('VOLUNTARIO_PF', 'VOLUNTARIO_PJ', 'ONG') NOT NULL,
    INDEX idx_email (email),
    INDEX idx_tipo (tipo),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABELAS FILHAS: Herança JOINED
-- =====================================================

-- Tabela: ong
CREATE TABLE ong (
    pessoa_id BIGINT PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    razao_social VARCHAR(255) NOT NULL,
    missao TEXT,
    endereco_comercial TEXT,
    area_atuacao VARCHAR(100),
    FOREIGN KEY (pessoa_id) REFERENCES candidato(id) ON DELETE CASCADE,
    INDEX idx_cnpj (cnpj)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: voluntario_pf
CREATE TABLE voluntario_pf (
    pessoa_id BIGINT PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    sexo ENUM('M', 'F'),
    data_nascimento DATE,
    nacionalidade VARCHAR(100),
    endereco_residencial TEXT,
    profissao VARCHAR(100),
    score INT,
    FOREIGN KEY (pessoa_id) REFERENCES candidato(id) ON DELETE CASCADE,
    INDEX idx_cpf (cpf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: voluntario_pj
CREATE TABLE voluntario_pj (
    pessoa_id BIGINT PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    razao_social VARCHAR(255) NOT NULL,
    endereco_comercial TEXT,
    representante_legal VARCHAR(255),
    FOREIGN KEY (pessoa_id) REFERENCES candidato(id) ON DELETE CASCADE,
    INDEX idx_cnpj (cnpj)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABELAS DE RELACIONAMENTO 1:1
-- =====================================================

-- Tabela: conta_acesso
CREATE TABLE conta_acesso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pessoa_id BIGINT UNIQUE,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    data_criacao DATETIME NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (pessoa_id) REFERENCES candidato(id) ON DELETE CASCADE,
    INDEX idx_login (login),
    INDEX idx_pessoa (pessoa_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: afiliacao
CREATE TABLE afiliacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pessoa_id BIGINT NOT NULL UNIQUE,
    data_solicitacao DATETIME NOT NULL,
    data_aprovacao DATETIME,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE',
    motivo_rejeicao TEXT,
    FOREIGN KEY (pessoa_id) REFERENCES candidato(id) ON DELETE CASCADE,
    INDEX idx_status (status),
    INDEX idx_pessoa (pessoa_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: termo_compromisso
CREATE TABLE termo_compromisso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pessoa_id BIGINT UNIQUE,
    versao VARCHAR(20) NOT NULL DEFAULT '1.0',
    conteudo TEXT NOT NULL,
    data_aceite DATETIME,
    aceito BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (pessoa_id) REFERENCES candidato(id) ON DELETE CASCADE,
    INDEX idx_pessoa (pessoa_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: perfil
CREATE TABLE perfil (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pessoa_id BIGINT NOT NULL UNIQUE,
    tipo_perfil VARCHAR(50) NOT NULL,
    FOREIGN KEY (pessoa_id) REFERENCES candidato(id) ON DELETE CASCADE,
    INDEX idx_pessoa (pessoa_id),
    INDEX idx_tipo_perfil (tipo_perfil)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABELAS DE RELACIONAMENTO 1:N
-- =====================================================

-- Tabela: email_validacao
CREATE TABLE email_validacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pessoa_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    data_envio DATETIME NOT NULL,
    data_expiracao DATETIME NOT NULL,
    utilizado BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (pessoa_id) REFERENCES candidato(id) ON DELETE CASCADE,
    INDEX idx_token (token),
    INDEX idx_pessoa (pessoa_id),
    INDEX idx_expiracao (data_expiracao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: certidao
CREATE TABLE certidao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pessoa_id BIGINT NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    numero VARCHAR(100) NOT NULL,
    data_emissao DATE NOT NULL,
    data_validade DATE,
    arquivo_path VARCHAR(500),
    valido BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (pessoa_id) REFERENCES candidato(id) ON DELETE CASCADE,
    INDEX idx_pessoa (pessoa_id),
    INDEX idx_tipo (tipo),
    INDEX idx_valido (valido)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABELAS DE CATÁLOGO
-- =====================================================

-- Tabela: habilidade
CREATE TABLE habilidade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    INDEX idx_categoria (categoria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: interesse
CREATE TABLE interesse (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    area VARCHAR(50),
    INDEX idx_area (area)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABELAS DE RELACIONAMENTO N:N
-- =====================================================

-- Tabela: perfil_habilidade
CREATE TABLE perfil_habilidade (
    perfil_id BIGINT NOT NULL,
    habilidade_id BIGINT NOT NULL,
    PRIMARY KEY (perfil_id, habilidade_id),
    FOREIGN KEY (perfil_id) REFERENCES perfil(id) ON DELETE CASCADE,
    FOREIGN KEY (habilidade_id) REFERENCES habilidade(id) ON DELETE CASCADE,
    INDEX idx_perfil (perfil_id),
    INDEX idx_habilidade (habilidade_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: perfil_interesse 
CREATE TABLE perfil_interesse (
    perfil_id BIGINT NOT NULL,
    interesse_id BIGINT NOT NULL,
    PRIMARY KEY (perfil_id, interesse_id),
    FOREIGN KEY (perfil_id) REFERENCES perfil(id) ON DELETE CASCADE,
    FOREIGN KEY (interesse_id) REFERENCES interesse(id) ON DELETE CASCADE,
    INDEX idx_perfil (perfil_id),
    INDEX idx_interesse (interesse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Para executar este script:
-- mysql -u root -p < database/create_database.sql
-- =====================================================

