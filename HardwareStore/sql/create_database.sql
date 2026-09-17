-- ============================================================
-- Loja de Hardware - script de criacao do banco
-- Rode este script uma vez no MySQL Workbench (ou console) antes
-- de rodar o DBConnection.java pela primeira vez.
-- ============================================================

CREATE DATABASE IF NOT EXISTS hardware_store CHARACTER SET utf8mb4;

-- Um usuario so para a aplicacao (nao o root).
CREATE USER IF NOT EXISTS 'store_user'@'localhost' IDENTIFIED BY 'store_pw';
GRANT ALL PRIVILEGES ON hardware_store.* TO 'store_user'@'localhost';
FLUSH PRIVILEGES;

USE hardware_store;

-- ------------------------------------------------------------
-- Produtos
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    product_name    VARCHAR(80) NOT NULL,
    brand           VARCHAR(60) NOT NULL,
    product_type    VARCHAR(50) NOT NULL,
    model           VARCHAR(60) NOT NULL,
    stock_quantity  INT NOT NULL
);

-- ------------------------------------------------------------
-- Usuarios (autenticacao)
-- OBS: num sistema real, guarde um HASH da senha (ex.: BCrypt),
-- nunca texto puro. Mantido simples aqui, no mesmo espirito
-- didatico do resto do material.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS user (
    id        INT PRIMARY KEY AUTO_INCREMENT,
    username  VARCHAR(40) NOT NULL UNIQUE,
    password  VARCHAR(60) NOT NULL
);

-- Um usuario padrao para voce conseguir logar na primeira vez.
-- Usuario: admin   Senha: admin123
INSERT INTO user (username, password)
SELECT 'admin', 'admin123'
WHERE NOT EXISTS (SELECT 1 FROM user WHERE username = 'admin');
