package com.library.app.controller;
import com.library.app.model.Livre;
import com.library.app.service.BibliothequeService;
import com.library.app.exception.ValidationException;
import com.library.app.exception.LivreIndisponibleException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class LivreController {
    @FXML private TableView<Livre> livresTable;
    @FXML private TableColumn<Livre, String> titreColumn, auteurColumn, isbnColumn;
    @FXML private TableColumn<Livre, Integer> anneeColumn;
    @FXML private TableColumn<Livre, Boolean> disponibleColumn;
    @FXML private TextField titreField, auteurField, anneeField, isbnField, searchField;
    @FXML private ComboBox<String> searchCriteria;
    @FXML private Label statsLabel;
    
    private BibliothequeService service = new BibliothequeService();
    private ObservableList<Livre> livresList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        anneeColumn.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        disponibleColumn.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        disponibleColumn.setCellFactory(col -> new TableCell<Livre, Boolean>() {
            @Override protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item ? "Oui" : "Non");
            }
        });
        
        searchCriteria.getItems().addAll("Tous", "Titre", "Auteur", "ISBN");
        searchCriteria.setValue("Tous");
        livresTable.setItems(livresList);
        loadLivres();
    }
    
    private void loadLivres() {
        livresList.clear();
        livresList.addAll(service.getAllLivres());
        updateStats();
    }
    
    private void updateStats() {
        long total = livresList.size();
        long disponibles = livresList.stream().filter(Livre::isDisponible).count();
        statsLabel.setText("Total: " + total + " | Disponibles: " + disponibles);
    }
    
    @FXML
    private void handleAjouterLivre() {
        try {
            Livre livre = new Livre();
            livre.setTitre(titreField.getText());
            livre.setAuteur(auteurField.getText());
            livre.setAnneePublication(Integer.parseInt(anneeField.getText()));
            livre.setIsbn(isbnField.getText());
            service.ajouterLivre(livre);
            clearFields();
            loadLivres();
            showAlert("Succès", "Livre ajouté", Alert.AlertType.INFORMATION);
        } catch (ValidationException e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Année invalide", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleModifierLivre() {
        Livre selected = livresTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Avertissement", "Sélectionnez un livre", Alert.AlertType.WARNING);
            return;
        }
        try {
            selected.setTitre(titreField.getText());
            selected.setAuteur(auteurField.getText());
            selected.setAnneePublication(Integer.parseInt(anneeField.getText()));
            selected.setIsbn(isbnField.getText());
            service.modifierLivre(selected);
            loadLivres();
            showAlert("Succès", "Livre modifié", Alert.AlertType.INFORMATION);
        } catch (ValidationException e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleSupprimerLivre() {
        Livre selected = livresTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Avertissement", "Sélectionnez un livre", Alert.AlertType.WARNING);
            return;
        }
        try {
            service.supprimerLivre(selected.getId());
            clearFields();
            loadLivres();
            showAlert("Succès", "Livre supprimé", Alert.AlertType.INFORMATION);
        } catch (ValidationException | LivreIndisponibleException e) {
            showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleRechercher() {
        String critere = searchCriteria.getValue();
        String valeur = searchField.getText();
        livresList.clear();
        livresList.addAll(service.rechercherLivres(critere, valeur));
        updateStats();
    }
    
    @FXML
    private void handleTableSelection() {
        Livre selected = livresTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            titreField.setText(selected.getTitre());
            auteurField.setText(selected.getAuteur());
            anneeField.setText(String.valueOf(selected.getAnneePublication()));
            isbnField.setText(selected.getIsbn());
        }
    }
    
    @FXML
    private void handleClearFields() {
        clearFields();
        livresTable.getSelectionModel().clearSelection();
    }
    
    private void clearFields() {
        titreField.clear();
        auteurField.clear();
        anneeField.clear();
        isbnField.clear();
    }
    
    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}