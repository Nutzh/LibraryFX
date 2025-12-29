-- Création de la base de données (si elle n'existe pas)
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- Table des livres
CREATE TABLE IF NOT EXISTS livres (
    id VARCHAR(50) PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    annee_publication INT,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    disponible BOOLEAN DEFAULT TRUE
);