-- =============================================================================
-- Matemágicos - Script de criação do banco de dados
-- Banco: app_biblioteca
-- Servidor: MySQL 5.7+ / 8.0+
CREATE DATABASE IF NOT EXISTS app_biblioteca
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE app_biblioteca;

-- -----------------------------------------------------------------------------
-- Tabela: usuario
-- As crianças que usam o app (cadastro/login).
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario      INT             NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(100)    NOT NULL,
    email           VARCHAR(150)    NOT NULL,
    senha           VARCHAR(255)    NOT NULL,           
    idade           INT             NOT NULL,
    nivel_escolar   INT             NULL,
    total_pontos    INT             NULL DEFAULT 0,
    moedas_magicas  INT             NULL DEFAULT 0,
    email_verificado BOOLEAN        NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_usuario_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: administrador
-- Quem cria/gerencia os jogos (painel administrativo, ainda não implementado no front).
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS administrador (
    id_administrador  INT           NOT NULL AUTO_INCREMENT,
    nome              VARCHAR(255)  NULL,
    email             VARCHAR(255)  NULL,
    senha             VARCHAR(255)  NULL,
    PRIMARY KEY (id_administrador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: jogo
-- Cada fase/jogo matemático disponível (soma, subtração, etc).
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS jogo (
    id_jogo           INT           NOT NULL AUTO_INCREMENT,
    nome_fase         VARCHAR(255)  NULL,
    dificuldade       VARCHAR(255)  NULL,
    tipo_operacao     VARCHAR(255)  NULL,
    assets_url        VARCHAR(255)  NULL,
    id_administrador  INT           NULL,
    PRIMARY KEY (id_jogo),
    KEY idx_jogo_administrador (id_administrador),
    CONSTRAINT fk_jogo_administrador
        FOREIGN KEY (id_administrador) REFERENCES administrador (id_administrador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: desempenho_jogo
-- Registro de cada partida jogada (acertos, tempo gasto).
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS desempenho_jogo (
    id_desempenho     INT           NOT NULL AUTO_INCREMENT,
    acertos_partida   INT           NULL,
    tempo_gasto       INT           NULL,
    data_hora         DATETIME(6)   NULL,
    id_usuario        INT           NULL,
    id_jogo           INT           NULL,
    PRIMARY KEY (id_desempenho),
    KEY idx_desempenho_usuario (id_usuario),
    KEY idx_desempenho_jogo (id_jogo),
    CONSTRAINT fk_desempenho_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
    CONSTRAINT fk_desempenho_jogo
        FOREIGN KEY (id_jogo) REFERENCES jogo (id_jogo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: avaliacao_final
-- Nota/feedback consolidado do aluno ao concluir um jogo.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS avaliacao_final (
    id_avaliacao      INT           NOT NULL AUTO_INCREMENT,
    nota_final        DOUBLE        NULL,
    feedback_ia       VARCHAR(255)  NULL,
    data_conclusao    DATETIME(6)   NULL,
    id_usuario        INT           NULL,
    id_jogo           INT           NULL,
    PRIMARY KEY (id_avaliacao),
    KEY idx_avaliacao_usuario (id_usuario),
    KEY idx_avaliacao_jogo (id_jogo),
    CONSTRAINT fk_avaliacao_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
    CONSTRAINT fk_avaliacao_jogo
        FOREIGN KEY (id_jogo) REFERENCES jogo (id_jogo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: pontuacao_historico
-- Histórico de cada vez que o usuário ganhou pontos (e a origem: qual jogo, bônus, etc).
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pontuacao_historico (
    id_ponto          INT           NOT NULL AUTO_INCREMENT,
    valor_ganho       INT           NULL,
    origem            VARCHAR(255)  NULL,
    data_ganho        DATETIME(6)   NULL,
    id_usuario        INT           NULL,
    PRIMARY KEY (id_ponto),
    KEY idx_pontuacao_usuario (id_usuario),
    CONSTRAINT fk_pontuacao_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: refresh_tokens
-- Sessões de longa duração (renovação automática de login). Cada linha é um
-- refresh token emitido pro usuário; "revogado" vira TRUE no logout ou quando
-- o token é rotacionado (trocado por um novo, a cada uso, por segurança).
-- Adicionada junto da funcionalidade de refresh token (login não expira mais em 24h).
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id_refresh_token  BIGINT        NOT NULL AUTO_INCREMENT,
    token             VARCHAR(512)  NOT NULL,
    id_usuario        INT           NOT NULL,
    data_expiracao    DATETIME(6)   NOT NULL,
    revogado          BOOLEAN       NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id_refresh_token),
    UNIQUE KEY uk_refresh_token (token),
    KEY idx_refresh_token_usuario (id_usuario),
    CONSTRAINT fk_refresh_token_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: password_reset_tokens
-- Códigos de 6 dígitos enviados por email pra recuperação de senha.
-- Curta duração (15min, ver password-reset.expiration-ms) e uso único ("usado").
-- Adicionada junto da funcionalidade de "esqueci minha senha".
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id_token          BIGINT        NOT NULL AUTO_INCREMENT,
    codigo            VARCHAR(6)    NOT NULL,
    id_usuario        INT           NOT NULL,
    data_expiracao    DATETIME(6)   NOT NULL,
    usado             BOOLEAN       NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id_token),
    KEY idx_password_reset_usuario (id_usuario),
    CONSTRAINT fk_password_reset_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabela: email_verification_tokens
-- Códigos de 6 dígitos enviados por email pra confirmar o email no cadastro.
-- Mesma lógica do password_reset_tokens, mas com validade mais longa (24h, ver
-- email-verification.expiration-ms) — não é urgente como recuperação de senha.
-- Adicionada junto da funcionalidade de verificação de email no cadastro.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS email_verification_tokens (
    id_token          BIGINT        NOT NULL AUTO_INCREMENT,
    codigo            VARCHAR(6)    NOT NULL,
    id_usuario        INT           NOT NULL,
    data_expiracao    DATETIME(6)   NOT NULL,
    usado             BOOLEAN       NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id_token),
    KEY idx_email_verification_usuario (id_usuario),
    CONSTRAINT fk_email_verification_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;