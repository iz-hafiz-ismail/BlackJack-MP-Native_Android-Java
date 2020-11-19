package com.example.blackjackgame.model;

public class History {

    String timestamp;
    int totalGame;
    int totalWin;

    public History(String timestamp, int totalGame, int totalWin) {
        this.timestamp = timestamp;
        this.totalGame = totalGame;
        this.totalWin = totalWin;
    }
    public History(){

    }

    public String getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getTotalGame() {
        return totalGame;
    }

    public void setTotalGame(int totalGame) {
        this.totalGame = totalGame;
    }

    public int getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(int totalWin) {
        this.totalWin = totalWin;
    }
}
