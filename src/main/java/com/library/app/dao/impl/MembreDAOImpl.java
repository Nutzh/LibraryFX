package com.library.app.dao.impl;

import com.library.app.dao.MembreDAO;
import com.library.app.model.Membre;
import com.library.app.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de MembreDAO
 */
public class MembreDAOImpl implements MembreDAO {

    @Override
    public void save(Membre membre) throws SQLException {
        String sql = "INSERT INTO membres (nom, prenom, email, actif) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setBoolean(4, membre.isActif());
            
            stmt.executeUpdate();
            
            // Récupérer l'ID généré
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    membre.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public Membre findById(String id) throws SQLException {
        String sql = "SELECT * FROM membres WHERE id = ?";
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMembre(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Membre> findAll() throws SQLException {
        String sql = "SELECT * FROM membres ORDER BY nom, prenom";
        List<Membre> membres = new ArrayList<>();
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }
        }
        return membres;
    }

    @Override
    public void update(Membre membre) throws SQLException {
        String sql = "UPDATE membres SET nom = ?, prenom = ?, email = ?, actif = ? WHERE id = ?";
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setBoolean(4, membre.isActif());
            stmt.setInt(5, membre.getId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM membres WHERE id = ?";
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        }
    }

    @Override
    public Membre findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM membres WHERE email = ?";
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMembre(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Membre> findActifs() throws SQLException {
        String sql = "SELECT * FROM membres WHERE actif = TRUE ORDER BY nom, prenom";
        List<Membre> membres = new ArrayList<>();
        
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }
        }
        return membres;
    }

    /**
     * Mappe un ResultSet vers un objet Membre
     */
    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        Membre membre = new Membre();
        membre.setId(rs.getInt("id"));
        membre.setNom(rs.getString("nom"));
        membre.setPrenom(rs.getString("prenom"));
        membre.setEmail(rs.getString("email"));
        membre.setActif(rs.getBoolean("actif"));
        return membre;
    }
}

