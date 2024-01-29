package com.gangulwar.auction.controllers;

import com.gangulwar.auction.HelloApplication;
import com.gangulwar.auction.connection.MultiClientServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartingController {

    private Scene scene;
    private Parent root;
    private Stage stage;

    @FXML
    public Button startServerButton;

    @FXML
    public Button biddingDetailsButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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

    public void getBiddingDetailsWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("biddingDetailsWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MultiClientServer.biddingDetailsController = fxmlLoader.getController();
        stage.setTitle("Bidding Details");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

}
