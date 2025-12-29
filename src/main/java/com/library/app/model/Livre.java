package com.library.app.model;

import java.time.LocalDate;

public class Livre extends Document implements Empruntable {
    private String auteur;
    private int anneePublication;
    private String isbn;
    private boolean disponible;
    
    public Livre() {
        super();
        this.disponible = true;
    }
    
    public Livre(String id, String titre, String auteur, int anneePublication, String isbn) {
        super(id, titre);
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.isbn = isbn;
        this.disponible = true;
    }
    
    // Implémentation de l'interface Empruntable
    @Override
    public boolean peutEtreEmprunte() {
        return disponible;
    }
    
    @Override
    public void emprunter() {
        if (disponible) {
            disponible = false;
        } else {
            throw new IllegalStateException("Le livre n'est pas disponible");
        }
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
    public double calculerPenaliteRetard(LocalDate dateRetourEffective) {
        // À implémenter plus tard
        return 0.0;
    }
    
    // Getters et setters
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
    
    @Override
    public String toString() {
        return "Livre{" +
               "id='" + id + '\'' +
               ", titre='" + titre + '\'' +
               ", auteur='" + auteur + '\'' +
               ", anneePublication=" + anneePublication +
               ", isbn='" + isbn + '\'' +
               ", disponible=" + disponible +
               '}';
    }
}