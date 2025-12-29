package com.library.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentArea;

    private void loadView(String viewName) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/com/library/app/" + viewName));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Menu actions 
    @FXML
    public void showLivres() {
        loadView("livre_view.fxml");     // Brahim
    }

    @FXML
    public void showMembres() {
        loadView("membre_view.fxml");    // Hatim
    }

    @FXML
    public void showEmprunts() {
        loadView("emprunt_view.fxml");   // Hakim
    }
}
