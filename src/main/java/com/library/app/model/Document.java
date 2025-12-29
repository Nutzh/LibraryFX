package com.library.app.model;

import java.time.LocalDate;

public abstract class Document {
    protected String id;
    protected String titre;
    
    public Document() {}
    
    public Document(String id, String titre) {
        this.id = id;
        this.titre = titre;
    }
    
    // Getters et setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    // Méthode abstraite pour calculer les pénalités
    public abstract double calculerPenaliteRetard(LocalDate dateRetourEffective);
}