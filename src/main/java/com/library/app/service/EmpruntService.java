package com.library.app.service;

import com.library.app.dao.*;
import com.library.app.dao.impl.EmpruntDAOImpl;
import com.library.app.exception.*;
import com.library.app.model.*;

import java.util.*;

public class EmpruntService {

    private final EmpruntDAO empruntDAO = new EmpruntDAOImpl();
    private final LivreDAO livreDAO = new com.library.app.dao.impl.LivreDAOImpl();

    public Emprunt emprunterLivre(Livre livre, Membre membre)
            throws LivreIndisponibleException,
                   MembreInactifException,
                   LimiteEmpruntDepasseeException,
                   ValidationException {
        // Default to 14 days from today if no date provided
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 14);
        return emprunterLivre(livre, membre, cal.getTime());
    }

    public Emprunt emprunterLivre(Livre livre, Membre membre, Date dateRetourPrevue)
            throws LivreIndisponibleException,
                   MembreInactifException,
                   LimiteEmpruntDepasseeException,
                   ValidationException {

        if (!livre.peutEtreEmprunte())
            throw new LivreIndisponibleException("Livre indisponible");

        if (!membre.isActif())
            throw new MembreInactifException("Membre inactif");

        if (empruntDAO.countEmpruntsEnCours(membre.getId()) >= 3)
            throw new LimiteEmpruntDepasseeException("Limite atteinte");

        Date now = new Date();
        
        // Validate that return date is in the future
        if (dateRetourPrevue.before(now)) {
            throw new ValidationException("La date de retour prévue doit être dans le futur");
        }

        Emprunt e = new Emprunt();
        e.setLivre(livre);
        e.setMembre(membre);
        e.setDateEmprunt(now);
        e.setDateRetourPrevue(dateRetourPrevue);
        livre.emprunter();
        livreDAO.update(livre);
        empruntDAO.save(e);
        

        return e;
    }

    public void retournerLivre(Emprunt emprunt) {
        emprunt.setDateRetourEffective(new Date());
        Livre livre = emprunt.getLivre();
        livre.retourner();
        livreDAO.update(livre);    
        empruntDAO.update(emprunt);
    }

    public List<Emprunt> getEmpruntsEnCours() {
        return empruntDAO.findEnCours();
    }

    public List<Emprunt> getEmpruntsEnRetard() {
        Date today = new Date();

        return empruntDAO.findEnCours()
                .stream()
                .filter(e -> e.getDateRetourPrevue().before(today))
                .toList();
    }
    
    public double calculerPenalite(Emprunt emprunt) {
        if (emprunt.getDateRetourEffective() == null) {
            // Si pas encore retourné, calculer la pénalité basée sur aujourd'hui
            Date today = new Date();
            if (emprunt.getDateRetourPrevue().before(today)) {
                return emprunt.getLivre()
                        .calculerPenaliteRetard(
                                emprunt.getDateRetourPrevue(),
                                today
                        );
            }
            return 0.0;
        }
        return emprunt.getLivre()
                .calculerPenaliteRetard(
                        emprunt.getDateRetourPrevue(),
                        emprunt.getDateRetourEffective()
                );
    }
}
   
