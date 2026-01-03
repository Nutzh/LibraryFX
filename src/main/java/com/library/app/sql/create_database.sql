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
-- Données de test pour les livres
INSERT INTO livres (id, titre, auteur, annee_publication, isbn, disponible) VALUES
('LIV001', 'Le Petit Prince', 'Antoine de Saint-Exupéry', 1943, '978-2070612758', TRUE),
('LIV002', '1984', 'George Orwell', 1949, '978-2070368228', TRUE),
('LIV003', 'L\'Étranger', 'Albert Camus', 1942, '978-2070360024', FALSE),
('LIV004', 'Harry Potter à l\'école des sorciers', 'J.K. Rowling', 1997, '978-2070643029', TRUE);

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
('hatim', 'ouayed', 'hatim@email.com', TRUE),
('brahim', 'mekaoui', 'brahim@email.com', TRUE),
('hakim', 'benatti', 'hakim@email.com', FALSE);
('youssef', 'khouya', 'youssef@email.com', FALSE);


/* TABLE EMPRUNT — hakim */
CREATE TABLE emprunt (
    id INT PRIMARY KEY AUTO_INCREMENT
);
