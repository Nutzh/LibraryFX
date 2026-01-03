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

/* TABLE MEMBRES — hatim */
CREATE TABLE IF NOT EXISTS membres (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    actif BOOLEAN DEFAULT TRUE
);

/* INSERT de test pour les membres */
INSERT INTO membres (nom, prenom, email, actif) VALUES
('Dupont', 'Jean', 'jean.dupont@email.com', TRUE),
('Martin', 'Sophie', 'sophie.martin@email.com', TRUE),
('Bernard', 'Pierre', 'pierre.bernard@email.com', FALSE);

/* TABLE EMPRUNT — hakim */
CREATE TABLE emprunt (
    id INT PRIMARY KEY AUTO_INCREMENT
);
