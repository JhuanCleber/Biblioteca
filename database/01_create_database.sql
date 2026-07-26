-- =============================================================================
-- Matemágicos - Script de criação do banco de dados
-- Banco: app_biblioteca
-- Servidor: MySQL 5.7+ / 8.0+
-- =============================================================================

-- Apaga o banco se já existir (cuidado: apaga todos os dados!)
DROP DATABASE IF EXISTS app_biblioteca;

-- Cria o banco com charset utf8mb4 (suporte completo a emojis e acentos)
CREATE DATABASE app_biblioteca
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

-- Seleciona o banco
USE app_biblioteca;

-- =============================================================================
-- As tabelas abaixo são criadas automaticamente pelo Hibernate
-- (spring.jpa.hibernate.ddl-auto=create) quando o Spring Boot iniciar.
-- Este script cria apenas o banco vazio, mas deixamos o schema documentado
-- aqui para referência.
-- =============================================================================

-- Tabela: usuario
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario      INT             NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(100)    NOT NULL,
    email           VARCHAR(150)    NOT NULL,
    senha           VARCHAR(255)    NOT NULL,           -- BCrypt hash
    idade           INT             NOT NULL,
    nivel_escolar   INT             NULL,
    total_pontos    INT             NULL DEFAULT 0,
    moedas_magicas  INT             NULL DEFAULT 0,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_usuario_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- Como executar:
--   1. Abra o MySQL Workbench
--   2. Conecte em localhost:3306 com user root / senha Refreskant123
--   3. File > Open SQL Script > selecione este arquivo
--   4. Execute (Ctrl+Shift+Enter) ou clique no raio
--
-- Ou pela linha de comando:
--   mysql -u root -pRefreskant123 < 01_create_database.sql
-- =============================================================================
