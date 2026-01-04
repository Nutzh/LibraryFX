package com.library.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private StackPane contentArea;

    private void loadView(String viewName) {
        try {
            var url = getClass().getResource("/com/library/app/" + viewName);
            if (url == null) {
                throw new RuntimeException("FXML not found: " + viewName);
            }
            Parent view = FXMLLoader.load(url);
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showLivres() {
        System.out.println("Loading LivreView.fxml");
        loadView("LivreView.fxml");
    }

    @FXML
    public void showMembres() {
        loadView("MembreView.fxml");
    }

    @FXML
    public void showEmprunts() {
        System.out.println("Emprunts clicked");
        loadView("EmpruntView.fxml");
    }
}