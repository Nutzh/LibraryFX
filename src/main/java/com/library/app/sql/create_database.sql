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
('hakim', 'benatti', 'hakim@email.com', FALSE),
('youssef', 'khouya', 'youssef@email.com', FALSE);


/* TABLE EMPRUNT — hakim */
CREATE TABLE emprunts (
    id INT AUTO_INCREMENT PRIMARY KEY,

    livre_id INT NOT NULL,
    membre_id INT NOT NULL,

    date_emprunt DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,

    CONSTRAINT fk_emprunt_livre
        FOREIGN KEY (livre_id)
        REFERENCES livres(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_emprunt_membre
        FOREIGN KEY (membre_id)
        REFERENCES membres(id)
        ON DELETE CASCADE
);
/* INSERT de test pour les emprunts */
INSERT INTO `emprunts` (`id`, `livre_id`, `membre_id`, `date_emprunt`, `date_retour_prevue`, `date_retour_effective`) VALUES
(14, 'LIV001', 1, '2026-01-04', '2026-01-18', NULL),
(15, 'LIV002', 1, '2026-01-04', '2026-01-18', NULL),
(16, 'LIV003', 1, '2026-01-04', '2026-01-18', NULL),
(17, 'LIV004', 2, '2026-01-04', '2026-01-18', NULL);

