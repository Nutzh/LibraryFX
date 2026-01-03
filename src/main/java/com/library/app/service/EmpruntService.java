package com.library.app.service;

import com.library.app.dao.*;
import com.library.app.dao.impl.EmpruntDAOImpl;
import com.library.app.exception.*;
import com.library.app.model.*;

import java.util.*;

public class EmpruntService {

    private final EmpruntDAO empruntDAO = new EmpruntDAOImpl();

    public Emprunt emprunterLivre(Livre livre, Membre membre)
            throws LivreIndisponibleException,
                   MembreInactifException,
                   LimiteEmpruntDepasseeException {

        if (!livre.peutEtreEmprunte())
            throw new LivreIndisponibleException("Livre indisponible");

        if (!membre.isActif())
            throw new MembreInactifException("Membre inactif");

        if (empruntDAO.countEmpruntsEnCours(membre.getId()) >= 3)
            throw new LimiteEmpruntDepasseeException("Limite atteinte");

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 14);

        Emprunt e = new Emprunt();
        e.setLivre(livre);
        e.setMembre(membre);
        e.setDateEmprunt(now);
        e.setDateRetourPrevue(cal.getTime());

        empruntDAO.save(e);
        livre.emprunter();

        return e;
    }

    public void retournerLivre(Emprunt emprunt) {
        emprunt.setDateRetourEffective(new Date());
        empruntDAO.update(emprunt);
        emprunt.getLivre().retourner();
    }

    public List<Emprunt> getEmpruntsEnRetard() {
        Date today = new Date();

        return empruntDAO.findEnCours()
                .stream()
                .filter(e -> e.getDateRetourPrevue().before(today))
                .toList();
    }
    public double calculerPenalite(Emprunt emprunt) {
        return emprunt.getLivre()
                .calculerPenaliteRetard(
                        emprunt.getDateRetourPrevue(),
                        emprunt.getDateRetourEffective()
                );
    }
}
   