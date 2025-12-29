CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

/* TABLE LIVRE — brahim */
CREATE TABLE IF NOT EXISTS livres (
    id VARCHAR(50) PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    annee_publication INT,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    disponible BOOLEAN DEFAULT TRUE
);

/* TABLE MEMBRE — hatim */
CREATE TABLE membre (
    id INT PRIMARY KEY AUTO_INCREMENT
);

/* TABLE EMPRUNT — hakim */
CREATE TABLE emprunt (
    id INT PRIMARY KEY AUTO_INCREMENT
);
