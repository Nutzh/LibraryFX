package com.library.app.model;

public interface Empruntable {
    boolean peutEtreEmprunte();
    void emprunter();
    void retourner();
    boolean isDisponible();
}