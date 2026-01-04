package com.library.app.dao;

import com.library.app.model.Membre;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour la gestion des membres
 */
public interface MembreDAO {
    /**
     * Sauvegarde un nouveau membre
     */
    void save(Membre membre) throws SQLException;

    /**
     * Trouve un membre par son ID
     */
    Membre findById(String id) throws SQLException;

    /**
     * Récupère tous les membres
     */
    List<Membre> findAll() throws SQLException;

    /**
     * Met à jour un membre existant
     */
    void update(Membre membre) throws SQLException;

    /**
     * Supprime un membre par son ID
     */
    void delete(String id) throws SQLException;

    /**
     * Trouve un membre par son email
     */
    Membre findByEmail(String email) throws SQLException;

    /**
     * Récupère tous les membres actifs
     */
    List<Membre> findActifs() throws SQLException;
}
