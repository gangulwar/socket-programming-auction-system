package com.gangulwar.auction.controllers;

import com.gangulwar.auction.connection.MultiClientServer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class BiddingDetailsWindowController {

    @FXML
    public TextArea bidDetailsTextBox;

    @FXML
    public Button startNewBidButton;
    public void updateBid(String newBid){
        bidDetailsTextBox.setText(newBid);
    }

    public void startNewBid(){
        MultiClientServer.startNewItemBid(bidDetailsTextBox.getText());
        updateBid("New Bidding Started!!!");
    }
}
