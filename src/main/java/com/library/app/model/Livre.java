package com.library.app.model;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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
    public double calculerPenaliteRetard(Date dateRetourPrevue, Date dateRetourEffective) {
    if (dateRetourPrevue == null || dateRetourEffective == null) {
        return 0;
    }
    
    // Conversion de java.util.Date en java.time.LocalDate
    LocalDate datePrevue = dateRetourPrevue.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    
    LocalDate dateEffective = dateRetourEffective.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    
    if (dateEffective.isAfter(datePrevue)) {
        long joursRetard = ChronoUnit.DAYS.between(datePrevue, dateEffective);
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