package com.library.app.controller;

import com.library.app.model.Emprunt;
import com.library.app.model.Livre;
import com.library.app.model.Membre;
import com.library.app.service.EmpruntService;
import com.library.app.service.BibliothequeService;

import com.library.app.exception.ValidationException;
import com.library.app.exception.LivreIndisponibleException;
import com.library.app.exception.MembreInactifException;
import com.library.app.exception.LimiteEmpruntDepasseeException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class EmpruntController {

    @FXML private TextField txtLivreId;  
    @FXML private TextField txtMembreId;  
    @FXML private DatePicker dateRetourPrevue;

    @FXML private TableView<Emprunt> tableEmprunts;
    @FXML private TableColumn<Emprunt, Integer> colId;
    @FXML private TableColumn<Emprunt, String> colLivre;
    @FXML private TableColumn<Emprunt, String> colMembre;
    @FXML private TableColumn<Emprunt, String> colDateRetour;
    @FXML private TableColumn<Emprunt, String> colPenalite;

    private final EmpruntService empruntService = new EmpruntService();
    private final BibliothequeService bibliothequeService = new BibliothequeService();

    private final ObservableList<Emprunt> emprunts =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(
                        c.getValue().getId()).asObject());

        colLivre.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getLivre().getTitre()));

        colMembre.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getMembre().getNom()));

        colDateRetour.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getDateRetourPrevue().toString()));

        colPenalite.setCellValueFactory(c -> {
            double p = empruntService.calculerPenalite(c.getValue());
            return new javafx.beans.property.SimpleStringProperty(
                    String.format("%.2f DH", p));
        });

        tableEmprunts.setItems(emprunts);
        tableEmprunts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        dateRetourPrevue.setValue(LocalDate.now().plusDays(14));

        refreshTable();
    }

    @FXML
    private void handleEmprunter() {
        try {
            String livreId = txtLivreId.getText().trim();
            String membreIdText = txtMembreId.getText().trim();

            if (livreId.isEmpty())
                throw new ValidationException("Veuillez entrer l'ID du livre");

            if (membreIdText.isEmpty())
                throw new ValidationException("Veuillez entrer l'ID du membre");

            Livre livre = bibliothequeService.getAllLivres()
                    .stream()
                    .filter(l -> livreId.equals(l.getId()))
                    .findFirst()
                    .orElseThrow(() ->
                            new ValidationException("Livre introuvable"));

            if (!livre.isDisponible())
                throw new LivreIndisponibleException("Livre indisponible");

            int membreId = Integer.parseInt(membreIdText);

            Membre membre = bibliothequeService.rechercherMembres("")
                    .stream()
                    .filter(m -> m.getId() == membreId)
                    .findFirst()
                    .orElseThrow(() ->
                            new ValidationException("Membre introuvable"));
            if (!membre.isActif()) {
                throw new MembreInactifException("Membre inactif");
}
            LocalDate retour = dateRetourPrevue.getValue();
            if (retour == null) {
                retour = LocalDate.now().plusDays(14);
            }

            Date dateRetour = Date.from(
                    retour.atStartOfDay(ZoneId.systemDefault()).toInstant());

            empruntService.emprunterLivre(livre, membre, dateRetour);

            txtLivreId.clear();
            txtMembreId.clear();
            dateRetourPrevue.setValue(LocalDate.now().plusDays(14));

            refreshTable();
            showInfo("Emprunt effectué avec succès");

        } catch (ValidationException |
                 LivreIndisponibleException |
                 MembreInactifException |
                 LimiteEmpruntDepasseeException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Erreur inattendue : " + e.getMessage());
        }
    }

    @FXML
    private void handleRetourner() {
        Emprunt e = tableEmprunts.getSelectionModel().getSelectedItem();
        if (e == null) {
            showError("Veuillez sélectionner un emprunt");
            return;
        }

        empruntService.retournerLivre(e);
        refreshTable();
        showInfo("Livre retourné");
    }

    private void refreshTable() {
        List<Emprunt> list = empruntService.getEmpruntsEnCours();
        Platform.runLater(() -> {
            emprunts.setAll(list);
            tableEmprunts.refresh();
        });
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
