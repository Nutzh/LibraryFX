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

public class EmpruntController {

    @FXML private TextField txtLivreId;
    @FXML private TextField txtMembreId;

    @FXML private TableView<Emprunt> tableEmprunts;
    @FXML private TableColumn<Emprunt, Integer> colId;
    @FXML private TableColumn<Emprunt, String> colLivre;
    @FXML private TableColumn<Emprunt, String> colMembre;
    @FXML private TableColumn<Emprunt, String> colDateRetour;

    private final EmpruntService empruntService = new EmpruntService();
    private final BibliothequeService bibliothequeService = new BibliothequeService();

    private final ObservableList<Emprunt> emprunts =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getId()
                ).asObject());

        colLivre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getLivre().getTitre()
                ));

        colMembre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMembre().getNom()
                ));

        colDateRetour.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDateRetourPrevue().toString()
                ));

        refreshTable();
    }

    @FXML
    private void handleEmprunter() {
        try {
            String livreId = txtLivreId.getText().trim();
            String membreIdText = txtMembreId.getText().trim();

            if (livreId.isEmpty() || membreIdText.isEmpty()) {
                throw new ValidationException("Tous les champs sont obligatoires");
            }

            int membreId = Integer.parseInt(membreIdText);

            // Recherche du livre
            Livre livre = bibliothequeService.getAllLivres().stream()
                    .filter(l -> l.getId().equals(livreId))
                    .findFirst()
                    .orElseThrow(() ->
                            new ValidationException("Livre introuvable"));

            // Recherche du membre
            Membre membre = bibliothequeService.rechercherMembres("")
                    .stream()
                    .filter(m -> m.getId() == membreId)
                    .findFirst()
                    .orElseThrow(() ->
                            new ValidationException("Membre introuvable"));

            // Appel métier
            empruntService.emprunterLivre(livre, membre);

            showInfo("Emprunt effectué avec succès");
            refreshTable();

        } catch (ValidationException |
                 LivreIndisponibleException |
                 MembreInactifException |
                 LimiteEmpruntDepasseeException e) {

            showError(e.getMessage());

        } catch (NumberFormatException e) {
            showError("ID du membre invalide");

        } catch (Exception e) {
            showError("Erreur inattendue : " + e.getMessage());
        }
    }

    @FXML
    private void handleRetourner() {
        Emprunt selected = tableEmprunts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Veuillez sélectionner un emprunt");
            return;
        }

        try {
            empruntService.retournerLivre(selected);
            showInfo("Livre retourné");
            refreshTable();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void refreshTable() {
        emprunts.setAll(empruntService.getEmpruntsEnRetard());
        tableEmprunts.setItems(emprunts);
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
