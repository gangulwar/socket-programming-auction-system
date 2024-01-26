package com.gangulwar.auction.controllers;

import com.gangulwar.auction.connection.MultiClientServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartingController {

    @FXML
    public Button startServerButton;

    public void startServer(ActionEvent e) {
        startServerButton.setDisable(true);

        new Thread(() -> {
            try {
                MultiClientServer.StartServer();
            } finally {
                Platform.runLater(() -> startServerButton.setDisable(false));
            }
        }).start();
    }
}
