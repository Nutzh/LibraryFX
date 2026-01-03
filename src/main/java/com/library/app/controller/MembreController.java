package com.library.app.controller;

import com.library.app.model.Membre;
import com.library.app.model.Emprunt;
import com.library.app.dao.MembreDAO;
import com.library.app.dao.EmpruntDAO;
import com.library.app.dao.impl.MembreDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la gestion des membres
 */
public class MembreController {
    
    @FXML
    private TableView<Membre> membresTable;
    
    @FXML
    private TableColumn<Membre, Integer> idColumn;
    
    @FXML
    private TableColumn<Membre, String> nomColumn;
    
    @FXML
    private TableColumn<Membre, String> prenomColumn;
    
    @FXML
    private TableColumn<Membre, String> emailColumn;
    
    @FXML
    private TableColumn<Membre, Boolean> statutColumn;
    
    @FXML
    private TableColumn<Membre, Void> actionsColumn;
    
    @FXML
    private TableView<Emprunt> historiqueTable;
    
    @FXML
    private TableColumn<Emprunt, Integer> empruntIdColumn;
    
    @FXML
    private TableColumn<Emprunt, String> livreColumn;
    
    @FXML
    private TableColumn<Emprunt, String> dateEmpruntColumn;
    
    @FXML
    private TableColumn<Emprunt, String> dateRetourColumn;
    
    @FXML
    private TextField nomField;
    
    @FXML
    private TextField prenomField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button ajouterButton;
    
    @FXML
    private Button modifierButton;
    
    @FXML
    private Button supprimerButton;
    
    @FXML
    private Label messageLabel;
    
    private MembreDAO membreDAO;
    private EmpruntDAO empruntDAO;
    private ObservableList<Membre> membresList;
    private ObservableList<Emprunt> historiqueList;
    private Membre membreSelectionne;
    
    @FXML
    public void initialize() {
        membreDAO = new MembreDAOImpl();
        // Implémentation basique de EmpruntDAO jusqu'à ce que EmpruntDAOImpl soit complété
        empruntDAO = new EmpruntDAO() {
            @Override
            public void save(Emprunt emprunt) {
                // À implémenter dans EmpruntDAOImpl
            }
            
            @Override
            public Emprunt findById(int id) {
                // À implémenter dans EmpruntDAOImpl
                return null;
            }
            
            @Override
            public List<Emprunt> findAll() {
                // À implémenter dans EmpruntDAOImpl
                return new java.util.ArrayList<>();
            }
            
            @Override
            public List<Emprunt> findByMembre(int membreId) {
                // À implémenter dans EmpruntDAOImpl
                return new java.util.ArrayList<>();
            }
            
            @Override
            public List<Emprunt> findEnCours() {
                // À implémenter dans EmpruntDAOImpl
                return new java.util.ArrayList<>();
            }
            
            @Override
            public int countEmpruntsEnCours(int membreId) {
                // À implémenter dans EmpruntDAOImpl
                return 0;
            }
            
            @Override
            public void update(Emprunt emprunt) {
                // À implémenter dans EmpruntDAOImpl
            }
        };
        membresList = FXCollections.observableArrayList();
        historiqueList = FXCollections.observableArrayList();
        
        configureTableView();
        loadAllMembres();
    }
    
    /**
     * Configure les TableView
     */
    private void configureTableView() {
        // Configuration de la table des membres
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("actif"));
        
        // Formater la colonne statut
        statutColumn.setCellFactory(column -> new TableCell<Membre, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Actif" : "Inactif");
                }
            }
        });
        
        // Colonne Actions avec bouton Activer/Désactiver
        actionsColumn.setCellFactory(param -> new TableCell<Membre, Void>() {
            private final Button actionButton = new Button();
            
            {
                actionButton.setOnAction(event -> {
                    Membre membre = getTableView().getItems().get(getIndex());
                    handleActiverDesactiver(membre);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Membre membre = getTableView().getItems().get(getIndex());
                    actionButton.setText(membre.isActif() ? "Désactiver" : "Activer");
                    setGraphic(actionButton);
                }
            }
        });
        
        membresTable.setItems(membresList);
        membresTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Configuration de la table historique
        empruntIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        livreColumn.setCellValueFactory(cellData -> {
            Emprunt emprunt = cellData.getValue();
            if (emprunt != null && emprunt.getLivre() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    emprunt.getLivre().getTitre()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        dateEmpruntColumn.setCellValueFactory(cellData -> {
            Emprunt emprunt = cellData.getValue();
            if (emprunt != null && emprunt.getDateEmprunt() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    emprunt.getDateEmprunt().toString()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        dateRetourColumn.setCellValueFactory(cellData -> {
            Emprunt emprunt = cellData.getValue();
            if (emprunt != null && emprunt.getDateRetourEffective() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    emprunt.getDateRetourEffective().toString()
                );
            } else if (emprunt != null && emprunt.getDateRetourPrevue() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    "Prévu: " + emprunt.getDateRetourPrevue().toString()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        historiqueTable.setItems(historiqueList);
        
        // Écouter la sélection dans la table des membres
        membresTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    membreSelectionne = newSelection;
                    loadHistorique(newSelection.getId());
                    fillFormFields(newSelection);
                }
            }
        );
    }
    
    /**
     * Charge tous les membres
     */
    private void loadAllMembres() {
        try {
            membresList.clear();
            membresList.addAll(membreDAO.findAll());
            messageLabel.setText("Total: " + membresList.size() + " membre(s)");
        } catch (SQLException e) {
            showMessage("Erreur lors du chargement: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Charge l'historique des emprunts d'un membre
     */
    private void loadHistorique(int membreId) {
        try {
            historiqueList.clear();
            List<Emprunt> emprunts = empruntDAO.findByMembre(membreId);
            if (emprunts != null) {
                historiqueList.addAll(emprunts);
            }
        } catch (Exception e) {
            showMessage("Erreur lors du chargement de l'historique: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Remplit les champs du formulaire avec les données du membre sélectionné
     */
    private void fillFormFields(Membre membre) {
        nomField.setText(membre.getNom());
        prenomField.setText(membre.getPrenom());
        emailField.setText(membre.getEmail());
        modifierButton.setDisable(false);
        supprimerButton.setDisable(false);
    }
    
    /**
     * Gère l'ajout d'un nouveau membre
     */
    @FXML
    private void handleAjouterMembre() {
        try {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String email = emailField.getText().trim();
            
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                showMessage("Veuillez remplir tous les champs", Alert.AlertType.WARNING);
                return;
            }
            
            Membre nouveauMembre = new Membre(nom, prenom, email, true);
            membreDAO.save(nouveauMembre);
            loadAllMembres();
            clearFields();
            showMessage("Membre ajouté avec succès", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            showMessage("Erreur: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Gère la modification d'un membre
     */
    @FXML
    private void handleModifierMembre() {
        if (membreSelectionne == null) {
            showMessage("Veuillez sélectionner un membre à modifier", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String email = emailField.getText().trim();
            
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                showMessage("Veuillez remplir tous les champs", Alert.AlertType.WARNING);
                return;
            }
            
            membreSelectionne.setNom(nom);
            membreSelectionne.setPrenom(prenom);
            membreSelectionne.setEmail(email);
            membreDAO.update(membreSelectionne);
            loadAllMembres();
            clearFields();
            membreSelectionne = null;
            showMessage("Membre modifié avec succès", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            showMessage("Erreur: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Gère la suppression d'un membre
     */
    @FXML
    private void handleSupprimerMembre() {
        if (membreSelectionne == null) {
            showMessage("Veuillez sélectionner un membre à supprimer", Alert.AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le membre");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer le membre : " + 
            membreSelectionne.getNom() + " " + membreSelectionne.getPrenom() + " ?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                membreDAO.delete(String.valueOf(membreSelectionne.getId()));
                loadAllMembres();
                clearFields();
                membreSelectionne = null;
                historiqueList.clear();
                showMessage("Membre supprimé avec succès", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showMessage("Erreur: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    /**
     * Gère l'activation/désactivation d'un membre
     */
    private void handleActiverDesactiver(Membre membre) {
        try {
            membre.setActif(!membre.isActif());
            membreDAO.update(membre);
            loadAllMembres();
            showMessage("Statut du membre modifié avec succès", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showMessage("Erreur: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Gère la recherche de membres
     */
    @FXML
    private void handleRechercher() {
        String critere = searchField.getText().trim();
        
        try {
            membresList.clear();
            List<Membre> tousLesMembres = membreDAO.findAll();
            
            if (critere.isEmpty()) {
                membresList.addAll(tousLesMembres);
            } else {
                String critereLower = critere.toLowerCase();
                List<Membre> resultats = tousLesMembres.stream()
                    .filter(m -> 
                        m.getNom().toLowerCase().contains(critereLower) ||
                        m.getPrenom().toLowerCase().contains(critereLower) ||
                        m.getEmail().toLowerCase().contains(critereLower)
                    )
                    .collect(Collectors.toList());
                membresList.addAll(resultats);
            }
            messageLabel.setText("Résultats: " + membresList.size() + " membre(s) trouvé(s)");
        } catch (SQLException e) {
            showMessage("Erreur lors de la recherche: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Vide les champs du formulaire
     */
    @FXML
    private void handleClearFields() {
        clearFields();
        membresTable.getSelectionModel().clearSelection();
        membreSelectionne = null;
        historiqueList.clear();
    }
    
    /**
     * Vide les champs du formulaire
     */
    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        modifierButton.setDisable(true);
        supprimerButton.setDisable(true);
    }
    
    /**
     * Affiche un message dans une alerte
     */
    private void showMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.toString());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

