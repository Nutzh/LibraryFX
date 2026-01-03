package com.library.app.model;
import java.time.LocalDate;

public class Livre extends Document implements Empruntable {
    private String auteur;
    private int anneePublication;
    private String isbn;
    private boolean disponible;
    
    public Livre() {
        this.disponible = true;
    }
    
    @Override
    public boolean peutEtreEmprunte() {
        return disponible;
    }
    
    @Override
    public void emprunter() {
        disponible = false;
    }
    
    @Override
    public void retourner() {
        disponible = true;
    }
    
    @Override
    public boolean isDisponible() {
        return disponible;
    }
    
    @Override
    public double calculerPenaliteRetard(LocalDate dateRetourPrevue,LocalDate dateRetourEffective) {
        if (dateRetourPrevue == null) return 0;
        if (dateRetourPrevue.isBefore(dateRetourEffective)) {
            long joursRetard = java.time.temporal.ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
            return joursRetard * 5.0; // 5 DH par jour de retard
        }
        return 0;
    }
    
    public String getAuteur() {
        return auteur;
    }
    
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
    
    public int getAnneePublication() {
        return anneePublication;
    }
    
    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}