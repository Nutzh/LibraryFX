package com.library.app.service;
import com.library.app.dao.LivreDAO;
import com.library.app.dao.impl.LivreDAOImpl;
import com.library.app.model.Livre;
import com.library.app.exception.ValidationException;
import com.library.app.exception.LivreIndisponibleException;
import java.util.List;

public class BibliothequeService {
    private LivreDAO livreDAO;
    
    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
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
}