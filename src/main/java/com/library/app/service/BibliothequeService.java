package com.library.app.service;
import com.library.app.dao.LivreDAO;
import com.library.app.dao.MembreDAO;
import com.library.app.dao.EmpruntDAO;
import com.library.app.dao.impl.LivreDAOImpl;
import com.library.app.dao.impl.MembreDAOImpl;
import com.library.app.dao.impl.EmpruntDAOImpl;
import com.library.app.model.Livre;
import com.library.app.model.Membre;
import com.library.app.model.Emprunt;
import com.library.app.exception.ValidationException;
import com.library.app.exception.LivreIndisponibleException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BibliothequeService {
    private LivreDAO livreDAO;
    private MembreDAO membreDAO;
    private EmpruntDAO empruntDAO;
    
    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
        this.membreDAO = new MembreDAOImpl();
        this.empruntDAO = new EmpruntDAOImpl();
    }
    
    public void ajouterLivre(Livre livre) throws ValidationException {
        if (livre.getTitre() == null || livre.getTitre().trim().isEmpty()) {
            throw new ValidationException("Titre obligatoire");
        }
        if (livre.getAuteur() == null || livre.getAuteur().trim().isEmpty()) {
            throw new ValidationException("Auteur obligatoire");
        }
        if (livre.getIsbn() == null || livre.getIsbn().trim().isEmpty()) {
            throw new ValidationException("ISBN obligatoire");
        }
        if (livreDAO.existsByIsbn(livre.getIsbn())) {
            throw new ValidationException("ISBN déjà existant");
        }
        livre.setId("LIV" + System.currentTimeMillis());
        livreDAO.save(livre);
    }
    
    public void modifierLivre(Livre livre) throws ValidationException {
        Livre existant = livreDAO.findById(livre.getId());
        if (existant == null) {
            throw new ValidationException("Livre non trouvé");
        }
        livreDAO.update(livre);
    }
    
    public void supprimerLivre(String id) throws ValidationException, LivreIndisponibleException {
        Livre livre = livreDAO.findById(id);
        if (livre == null) {
            throw new ValidationException("Livre non trouvé");
        }
        if (!livre.isDisponible()) {
            throw new LivreIndisponibleException("Livre emprunté, impossible de supprimer");
        }
        livreDAO.delete(id);
    }
    
    public List<Livre> rechercherLivres(String critere, String valeur) {
        switch (critere) {
            case "Titre": return livreDAO.findByTitre(valeur);
            case "Auteur": return livreDAO.findByAuteur(valeur);
            case "ISBN": return livreDAO.findByIsbn(valeur);
            default: return livreDAO.findAll();
        }
    }
    
    public List<Livre> getLivresDisponibles() {
        return livreDAO.findDisponibles();
    }
    
    public List<Livre> getAllLivres() {
        return livreDAO.findAll();
    }
    
    // Services pour les membres
    public void ajouterMembre(String nom, String prenom, String email) throws ValidationException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Nom obligatoire");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new ValidationException("Prénom obligatoire");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email obligatoire");
        }
        try {
            Membre membre = new Membre(nom.trim(), prenom.trim(), email.trim(), true);
            membreDAO.save(membre);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de l'ajout du membre: " + e.getMessage());
        }
    }
    
    public void modifierMembre(int id, String nom, String prenom, String email) throws ValidationException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Nom obligatoire");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new ValidationException("Prénom obligatoire");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email obligatoire");
        }
        try {
            Membre membre = membreDAO.findById(String.valueOf(id));
            if (membre == null) {
                throw new ValidationException("Membre non trouvé");
            }
            membre.setNom(nom.trim());
            membre.setPrenom(prenom.trim());
            membre.setEmail(email.trim());
            membreDAO.update(membre);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la modification du membre: " + e.getMessage());
        }
    }
    
    public void activerDesactiver(int id) throws ValidationException {
        try {
            Membre membre = membreDAO.findById(String.valueOf(id));
            if (membre == null) {
                throw new ValidationException("Membre non trouvé");
            }
            membre.setActif(!membre.isActif());
            membreDAO.update(membre);
        } catch (SQLException e) {
            throw new ValidationException("Erreur lors de la modification du statut: " + e.getMessage());
        }
    }
    
    public List<Membre> rechercherMembres(String critere) throws SQLException {
        List<Membre> tousLesMembres = membreDAO.findAll();
        
        if (critere == null || critere.trim().isEmpty()) {
            return tousLesMembres;
        }
        
        String critereLower = critere.toLowerCase();
        return tousLesMembres.stream()
            .filter(m -> 
                m.getNom().toLowerCase().contains(critereLower) ||
                m.getPrenom().toLowerCase().contains(critereLower) ||
                m.getEmail().toLowerCase().contains(critereLower)
            )
            .collect(Collectors.toList());
    }
    
    public List<Emprunt> getHistorique(int membreId) {
        return empruntDAO.findByMembre(membreId);
    }
}