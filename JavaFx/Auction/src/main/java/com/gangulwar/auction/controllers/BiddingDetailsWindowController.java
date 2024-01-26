package com.gangulwar.auction.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class BiddingDetailsWindowController {

    @FXML
    public TextArea bidDetailsTextBox;

    public void updateBid(String newBid){
        bidDetailsTextBox.setText(newBid);
    }
}
