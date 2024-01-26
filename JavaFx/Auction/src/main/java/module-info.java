module com.gangulwar.auction {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gangulwar.auction to javafx.fxml;
    exports com.gangulwar.auction;
    exports com.gangulwar.auction.controllers;
}