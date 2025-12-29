package com.library.app.service;

import com.library.app.dao.LivreDAO;
import com.library.app.dao.impl.LivreDAOImpl;
import com.library.app.model.Livre;
import java.util.List;

public class BibliothequeService {
    private LivreDAO livreDAO;
    
    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
    }
    
    public void ajouterLivre(Livre livre) throws Exception {
        // Validation
        if (livre.getIsbn() == null || livre.getIsbn().trim().isEmpty()) {
            throw new Exception("L'ISBN est obligatoire");
        }
        
        // Vérifier si l'ISBN existe déjà
        if (livreDAO.existsByIsbn(livre.getIsbn())) {
            throw new Exception("Un livre avec cet ISBN existe déjà");
        }
        
        // Validation du titre
        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
            throw new Exception("Le titre est obligatoire");
        }
        
        // Validation de l'auteur
        if (livre.getAuteur() == null || livre.getAuteur().trim().isEmpty()) {
            throw new Exception("L'auteur est obligatoire");
        }
        
        // Générer un ID unique si non fourni
        if (livre.getId() == null || livre.getId().trim().isEmpty()) {
            livre.setId(generateLivreId());
        }
        
        livreDAO.save(livre);
    }
    
    public void modifierLivre(Livre livre) throws Exception {
        if (livre.getId() == null || livre.getId().trim().isEmpty()) {
            throw new Exception("L'ID du livre est obligatoire");
        }
        
        Livre existing = livreDAO.findById(livre.getId());
        if (existing == null) {
            throw new Exception("Livre non trouvé avec l'ID: " + livre.getId());
        }
        
        livreDAO.update(livre);
    }
    
    public void supprimerLivre(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("L'ID du livre est obligatoire");
        }
        
        Livre livre = livreDAO.findById(id);
        if (livre == null) {
            throw new Exception("Livre non trouvé avec l'ID: " + id);
        }
        
        if (!livre.isDisponible()) {
            throw new Exception("Impossible de supprimer un livre actuellement emprunté");
        }
        
        livreDAO.delete(id);
    }
    
    public List<Livre> rechercherLivres(String critere, String valeur) {
        if (critere == null || valeur == null) {
            return livreDAO.findAll();
        }
        
        switch (critere.toLowerCase()) {
            case "titre":
                return livreDAO.findByTitre(valeur);
            case "auteur":
                return livreDAO.findByAuteur(valeur);
            case "isbn":
                return livreDAO.findByIsbn(valeur);
            default:
                return livreDAO.findAll();
        }
    }
    
    public List<Livre> getLivresDisponibles() {
        return livreDAO.findDisponibles();
    }
    
    public Livre getLivreById(String id) {
        return livreDAO.findById(id);
    }
    
    public List<Livre> getAllLivres() {
        return livreDAO.findAll();
    }
    
    private String generateLivreId() {
        return "LIV" + System.currentTimeMillis();
    }
}