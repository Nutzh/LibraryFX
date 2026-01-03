package com.library.app.model;

/**
 * Classe représentant un membre de la bibliothèque
 */
public class Membre extends Personne {
    private int id;
    private boolean actif;

    // Constructeur par défaut
    public Membre() {
        super();
        this.actif = true;
    }

    // Constructeur avec paramètres
    public Membre(int id, String nom, String prenom, String email, boolean actif) {
        super(nom, prenom, email);
        this.id = id;
        this.actif = actif;
    }

    // Constructeur sans id (pour création)
    public Membre(String nom, String prenom, String email, boolean actif) {
        super(nom, prenom, email);
        this.actif = actif;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    /**
     * Vérifie si le membre peut emprunter
     */
    public boolean peutEmprunter() {
        return this.actif;
    }
}
