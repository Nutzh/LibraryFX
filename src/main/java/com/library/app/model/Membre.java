package com.library.app.model;

public class Membre extends Personne {
    private int id;
    private boolean actif;


    public Membre() {
        this.actif=true;}

        public boolean peutEmprunter(){
            return this.actif;
        }
    }


    
