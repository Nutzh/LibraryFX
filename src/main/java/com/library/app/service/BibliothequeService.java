package com.library.app.service;

import com.library.app.dao.LivreDAO;
import com.library.app.dao.impl.LivreDAOImpl;
import com.library.app.dao.MembreDAO;
import com.library.app.dao.impl.MembreDAOImpl;
import com.library.app.dao.EmpruntDAO;
import com.library.app.dao.impl.EmpruntDAOImpl;
import com.library.app.model.Livre;
import com.library.app.model.Membre;
import com.library.app.model.Emprunt;
import com.library.app.exception.ValidationException;
import java.sql.SQLException;
import java.util.List;

public class BibliothequeService {
    private LivreDAO livreDAO;
    private MembreDAO membreDAO;
    private EmpruntDAO empruntDAO;
    
    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
        this.membreDAO = new MembreDAOImpl();
        this.empruntDAO = new EmpruntDAOImpl();
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
    
    // ========== MÉTHODES POUR LES MEMBRES ==========
    
    /**
     * Ajoute un nouveau membre
     */
    public void ajouterMembre(String nom, String prenom, String email) throws ValidationException, SQLException {
        // Validation des champs
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Le nom est obligatoire");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new ValidationException("Le prénom est obligatoire");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("L'email est obligatoire");
        }
        
        // Vérifier le format de l'email
        if (!email.contains("@")) {
            throw new ValidationException("L'email n'est pas valide");
        }
        
        // Vérifier si l'email existe déjà
        Membre existant = membreDAO.findByEmail(email);
        if (existant != null) {
            throw new ValidationException("Un membre avec cet email existe déjà");
        }
        
        // Créer et sauvegarder le membre
        Membre membre = new Membre(nom.trim(), prenom.trim(), email.trim(), true);
        membreDAO.save(membre);
    }
    
    /**
     * Modifie un membre existant
     */
    public void modifierMembre(int id, String nom, String prenom, String email) throws ValidationException, SQLException {
        // Validation des champs
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Le nom est obligatoire");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new ValidationException("Le prénom est obligatoire");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("L'email est obligatoire");
        }
        
        // Vérifier le format de l'email
        if (!email.contains("@")) {
            throw new ValidationException("L'email n'est pas valide");
        }
        
        // Vérifier si le membre existe
        Membre membre = membreDAO.findById(String.valueOf(id));
        if (membre == null) {
            throw new ValidationException("Membre non trouvé avec l'ID: " + id);
        }
        
        // Vérifier si l'email est utilisé par un autre membre
        Membre existant = membreDAO.findByEmail(email);
        if (existant != null && existant.getId() != id) {
            throw new ValidationException("Un autre membre utilise déjà cet email");
        }
        
        // Mettre à jour le membre
        membre.setNom(nom.trim());
        membre.setPrenom(prenom.trim());
        membre.setEmail(email.trim());
        membreDAO.update(membre);
    }
    
    /**
     * Active ou désactive un membre
     */
    public void activerDesactiver(int membreId) throws SQLException {
        Membre membre = membreDAO.findById(String.valueOf(membreId));
        if (membre == null) {
            throw new SQLException("Membre non trouvé avec l'ID: " + membreId);
        }
        
        membre.setActif(!membre.isActif());
        membreDAO.update(membre);
    }
    
    /**
     * Recherche des membres selon un critère
     */
    public List<Membre> rechercherMembres(String critere) throws SQLException {
        if (critere == null || critere.trim().isEmpty()) {
            return membreDAO.findAll();
        }
        
        // Recherche simple dans nom, prénom ou email
        List<Membre> tous = membreDAO.findAll();
        String recherche = critere.toLowerCase().trim();
        
        return tous.stream()
            .filter(m -> 
                m.getNom().toLowerCase().contains(recherche) ||
                m.getPrenom().toLowerCase().contains(recherche) ||
                m.getEmail().toLowerCase().contains(recherche)
            )
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Récupère l'historique des emprunts d'un membre
     */
    public List<Emprunt> getHistorique(int membreId) throws SQLException {
        return empruntDAO.findByMembre(membreId);
    }
    
    /**
     * Supprime un membre
     */
    public void supprimerMembre(int id) throws SQLException {
        membreDAO.delete(String.valueOf(id));
    }
}