package com.gangulwar.auction.connection;

public class User {
    String username;
    String currentBid;
    int points;

    public User(String username, String currentBid,int points) {
        this.username = username;
        this.currentBid = currentBid;
        this.points=points;
    }
}
