package com.library.app.dao;

import com.library.app.model.Emprunt;
import java.util.List;

public interface EmpruntDAO {

    void save(Emprunt emprunt);

    void update(Emprunt emprunt);

    Emprunt findById(int id);

    List<Emprunt> findAll();

    List<Emprunt> findEnCours();

    List<Emprunt> findByMembre(int membreId);

    int countEmpruntsEnCours(int membreId);
}
