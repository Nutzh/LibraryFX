USE library_db;

-- Données de test pour les livres
INSERT INTO livres (id, titre, auteur, annee_publication, isbn, disponible) VALUES
('LIV001', 'Le Petit Prince', 'Antoine de Saint-Exupéry', 1943, '978-2070612758', TRUE),
('LIV002', '1984', 'George Orwell', 1949, '978-2070368228', TRUE),
('LIV003', 'L''Étranger', 'Albert Camus', 1942, '978-2070360024', TRUE),
('LIV004', 'Le Seigneur des Anneaux', 'J.R.R. Tolkien', 1954, '978-2267023420', FALSE),
('LIV005', 'Harry Potter à l''école des sorciers', 'J.K. Rowling', 1997, '978-2070612369', TRUE),
('LIV006', 'Le Vieil Homme et la Mer', 'Ernest Hemingway', 1952, '978-2070360031', TRUE),
('LIV007', 'Orgueil et Préjugés', 'Jane Austen', 1813, '978-2070368228', FALSE),
('LIV008', 'Les Misérables', 'Victor Hugo', 1862, '978-2253001421', TRUE),
('LIV009', 'Crime et Châtiment', 'Fiodor Dostoïevski', 1866, '978-2070360048', TRUE),
('LIV010', 'Les Fleurs du Mal', 'Charles Baudelaire', 1857, '978-2253001438', TRUE);

-- Vérification
SELECT COUNT(*) as nombre_livres FROM livres;