package com.example.blackjackgame;

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
//        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//        cal.setTimeInMillis(timestamp * 1000);
//        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
//        return date;
//        String time = timestamp.substring(0,19);
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
