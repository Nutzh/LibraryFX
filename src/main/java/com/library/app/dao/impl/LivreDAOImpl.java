package com.library.app.dao.impl;

import com.library.app.dao.LivreDAO;
import com.library.app.model.Livre;
import com.library.app.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAOImpl implements LivreDAO {
    private Connection connection;
    
    public LivreDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public void save(Livre livre) {
        String sql = "INSERT INTO livres (id, titre, auteur, annee_publication, isbn, disponible) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livre.getId());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setString(5, livre.getIsbn());
            stmt.setBoolean(6, livre.isDisponible());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du livre: " + e.getMessage());
        }
    }
    
    @Override
    public Livre findById(String id) {
        String sql = "SELECT * FROM livres WHERE id = ?";
        Livre livre = null;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                livre = mapResultSetToLivre(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du livre: " + e.getMessage());
        }
        
        return livre;
    }
    
    @Override
    public List<Livre> findAll() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres ORDER BY titre";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livres: " + e.getMessage());
        }
        
        return livres;
    }
    
    @Override
    public void update(Livre livre) {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, annee_publication = ?, isbn = ?, disponible = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setInt(3, livre.getAnneePublication());
            stmt.setString(4, livre.getIsbn());
            stmt.setBoolean(5, livre.isDisponible());
            stmt.setString(6, livre.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du livre: " + e.getMessage());
        }
    }
    
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM livres WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livre: " + e.getMessage());
        }
    }
    
    @Override
    public List<Livre> findByAuteur(String auteur) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE auteur LIKE ? ORDER BY titre";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + auteur + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par auteur: " + e.getMessage());
        }
        
        return livres;
    }
    
    @Override
    public List<Livre> findByTitre(String titre) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE titre LIKE ? ORDER BY titre";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + titre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par titre: " + e.getMessage());
        }
        
        return livres;
    }
    
    @Override
    public List<Livre> findByIsbn(String isbn) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE isbn = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par ISBN: " + e.getMessage());
        }
        
        return livres;
    }
    
    @Override
    public List<Livre> findDisponibles() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE disponible = true ORDER BY titre";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des livres disponibles: " + e.getMessage());
        }
        
        return livres;
    }
    
    @Override
    public boolean existsByIsbn(String isbn) {
        String sql = "SELECT COUNT(*) FROM livres WHERE isbn = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'ISBN: " + e.getMessage());
        }
        
        return false;
    }
    
    private Livre mapResultSetToLivre(ResultSet rs) throws SQLException {
        Livre livre = new Livre();
        livre.setId(rs.getString("id"));
        livre.setTitre(rs.getString("titre"));
        livre.setAuteur(rs.getString("auteur"));
        livre.setAnneePublication(rs.getInt("annee_publication"));
        livre.setIsbn(rs.getString("isbn"));
        livre.setDisponible(rs.getBoolean("disponible"));
        return livre;
    }
}