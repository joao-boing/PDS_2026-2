CREATE DATABASE IF NOT EXISTS hardware_store CHARACTER SET utf8mb4;

CREATE USER IF NOT EXISTS 'store_user'@'localhost' IDENTIFIED BY 'store_pw';
GRANT ALL PRIVILEGES ON hardware_store.* TO 'store_user'@'localhost';
FLUSH PRIVILEGES;

USE hardware_store;

CREATE TABLE IF NOT EXISTS product (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    product_name    VARCHAR(80) NOT NULL,
    brand           VARCHAR(60) NOT NULL,
    product_type    VARCHAR(50) NOT NULL,
    model           VARCHAR(60) NOT NULL,
    stock_quantity  INT NOT NULL,
    photo           LONGBLOB
);

CREATE TABLE IF NOT EXISTS user (
    id        INT PRIMARY KEY AUTO_INCREMENT,
    username  VARCHAR(40) NOT NULL UNIQUE,
    password  VARCHAR(60) NOT NULL
);

INSERT INTO user (username, password)
SELECT 'admin', 'admin123'
WHERE NOT EXISTS (SELECT 1 FROM user WHERE username = 'admin');
