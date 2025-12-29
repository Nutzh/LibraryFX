package com.library.app.controller;

import com.library.app.model.Livre;
import com.library.app.service.BibliothequeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class LivreController {
    
    @FXML
    private TableView<Livre> livresTable;
    
    @FXML
    private TableColumn<Livre, String> idColumn;
    
    @FXML
    private TableColumn<Livre, String> titreColumn;
    
    @FXML
    private TableColumn<Livre, String> auteurColumn;
    
    @FXML
    private TableColumn<Livre, Integer> anneeColumn;
    
    @FXML
    private TableColumn<Livre, String> isbnColumn;
    
    @FXML
    private TableColumn<Livre, Boolean> disponibleColumn;
    
    @FXML
    private TextField titreField;
    
    @FXML
    private TextField auteurField;
    
    @FXML
    private TextField anneeField;
    
    @FXML
    private TextField isbnField;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private ComboBox<String> searchCriteria;
    
    @FXML
    private Label messageLabel;
    
    private BibliothequeService bibliothequeService;
    private ObservableList<Livre> livresList;
    
    @FXML
    public void initialize() {
        bibliothequeService = new BibliothequeService();
        livresList = FXCollections.observableArrayList();
        
        configureTableView();
        
        searchCriteria.getItems().addAll("Tous", "Titre", "Auteur", "ISBN");
        searchCriteria.setValue("Tous");
        
        loadAllLivres();
    }
    
    private void configureTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        anneeColumn.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        disponibleColumn.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        
        disponibleColumn.setCellFactory(column -> new TableCell<Livre, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Oui" : "Non");
                }
            }
        });
        
        livresTable.setItems(livresList);
        livresTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    private void loadAllLivres() {
        livresList.clear();
        livresList.addAll(bibliothequeService.getAllLivres());
        messageLabel.setText("Total: " + livresList.size() + " livre(s)");
    }
    
    @FXML
    private void handleAjouterLivre() {
        try {
            if (titreField.getText().trim().isEmpty() ||
                auteurField.getText().trim().isEmpty() ||
                anneeField.getText().trim().isEmpty() ||
                isbnField.getText().trim().isEmpty()) {
                showMessage("Veuillez remplir tous les champs", Alert.AlertType.WARNING);
                return;
            }
            
            Livre livre = new Livre();
            livre.setTitre(titreField.getText().trim());
            livre.setAuteur(auteurField.getText().trim());
            
            try {
                livre.setAnneePublication(Integer.parseInt(anneeField.getText().trim()));
            } catch (NumberFormatException e) {
                showMessage("L'année doit être un nombre valide", Alert.AlertType.ERROR);
                return;
            }
            
            livre.setIsbn(isbnField.getText().trim());
            
            bibliothequeService.ajouterLivre(livre);
            loadAllLivres();
            clearFields();
            
            showMessage("Livre ajouté avec succès", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            showMessage("Erreur: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleModifierLivre() {
        Livre livreSelectionne = livresTable.getSelectionModel().getSelectedItem();
        
        if (livreSelectionne == null) {
            showMessage("Veuillez sélectionner un livre à modifier", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            livreSelectionne.setTitre(titreField.getText().trim());
            livreSelectionne.setAuteur(auteurField.getText().trim());
            
            try {
                livreSelectionne.setAnneePublication(Integer.parseInt(anneeField.getText().trim()));
            } catch (NumberFormatException e) {
                showMessage("L'année doit être un nombre valide", Alert.AlertType.ERROR);
                return;
            }
            
            livreSelectionne.setIsbn(isbnField.getText().trim());
            
            bibliothequeService.modifierLivre(livreSelectionne);
            loadAllLivres();
            
            showMessage("Livre modifié avec succès", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            showMessage("Erreur: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleSupprimerLivre() {
        Livre livreSelectionne = livresTable.getSelectionModel().getSelectedItem();
        
        if (livreSelectionne == null) {
            showMessage("Veuillez sélectionner un livre à supprimer", Alert.AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le livre");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer le livre : " + livreSelectionne.getTitre() + " ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                bibliothequeService.supprimerLivre(livreSelectionne.getId());
                loadAllLivres();
                clearFields();
                showMessage("Livre supprimé avec succès", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showMessage("Erreur: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleRechercher() {
        String critere = searchCriteria.getValue();
        String valeur = searchField.getText().trim();
        
        livresList.clear();
        
        if (critere.equals("Tous") || valeur.isEmpty()) {
            loadAllLivres();
        } else {
            livresList.addAll(bibliothequeService.rechercherLivres(critere, valeur));
            messageLabel.setText("Résultats: " + livresList.size() + " livre(s) trouvé(s)");
        }
    }
    
    @FXML
    private void handleAfficherDisponibles() {
        livresList.clear();
        livresList.addAll(bibliothequeService.getLivresDisponibles());
        messageLabel.setText("Livres disponibles: " + livresList.size());
    }
    
    @FXML
    private void handleTableSelection() {
        Livre livreSelectionne = livresTable.getSelectionModel().getSelectedItem();
        
        if (livreSelectionne != null) {
            titreField.setText(livreSelectionne.getTitre());
            auteurField.setText(livreSelectionne.getAuteur());
            anneeField.setText(String.valueOf(livreSelectionne.getAnneePublication()));
            isbnField.setText(livreSelectionne.getIsbn());
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
    
    private void showMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.toString());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}