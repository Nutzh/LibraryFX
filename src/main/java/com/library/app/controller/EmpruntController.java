package com.library.app.controller;

import com.library.app.model.Emprunt;
import com.library.app.model.Livre;
import com.library.app.model.Membre;
import com.library.app.service.EmpruntService;
import com.library.app.service.LivreService;
import com.library.app.service.MembreService;

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
    private final LivreService livreService = new LivreService();
    private final MembreService membreService = new MembreService();

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
            int livreId = Integer.parseInt(txtLivreId.getText());
            int membreId = Integer.parseInt(txtMembreId.getText());

            Livre livre = livreService.findById(livreId);
            Membre membre = membreService.findById(membreId);

            empruntService.emprunterLivre(livre, membre);

            showInfo("Emprunt effectué avec succès");
            refreshTable();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleRetourner() {
        Emprunt selected = tableEmprunts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Veuillez sélectionner un emprunt");
            return;
        }

        empruntService.retournerLivre(selected);
        showInfo("Livre retourné");
        refreshTable();
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
