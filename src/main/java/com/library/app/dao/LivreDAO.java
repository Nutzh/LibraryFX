package com.library.app.dao;

import com.library.app.model.Livre;
import java.util.List;

public interface LivreDAO {
    void save(Livre livre);
    Livre findById(String id);
    List<Livre> findAll();
    void update(Livre livre);
    void delete(String id);
    
    List<Livre> findByAuteur(String auteur);
    List<Livre> findByTitre(String titre);
    List<Livre> findByIsbn(String isbn);
    List<Livre> findDisponibles();
    
    boolean existsByIsbn(String isbn);
}