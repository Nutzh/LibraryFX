package com.library.app.model;

/**
 * Classe abstraite représentant une personne
 */
public abstract class Personne {
    protected String nom;
    protected String prenom;
    protected String email;

    // Constructeur par défaut
    public Personne() {
    }

    // Constructeur avec paramètres
    public Personne(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}