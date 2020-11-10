package com.example.blackjackgame.model;

public class Card {
    Pics pic;
    
    public Card(Pics pic){
        this.pic = pic;
    }

    public int cardValue() {
        return this.pic.getValue();
    }

    public int cardPic(){
        return this.pic.getPic();
    }
}
