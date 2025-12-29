package com.library.app.dao;

import com.library.app.model.Emprunt;
import java.util.List;

public interface EmpruntDAO {

    void save(Emprunt emprunt);
    Emprunt findById(int id);
    List<Emprunt> findAll();
    List<Emprunt> findByMembre(int membreId);
    List<Emprunt> findEnCours();
    int countEmpruntsEnCours(int membreId);
    void update(Emprunt emprunt);
}