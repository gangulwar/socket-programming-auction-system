package com.gangulwar.auction.connection;

public class User {
    String username;
    Integer currentBid;
    int points;

    public User(String username, int points) {
        this.username = username;
        this.points=points;
    }
}
