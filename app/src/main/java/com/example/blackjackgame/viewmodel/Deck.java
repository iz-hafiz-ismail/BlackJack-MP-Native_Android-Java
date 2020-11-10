package com.example.blackjackgame.viewmodel;


import androidx.lifecycle.ViewModel;

import com.example.blackjackgame.model.Card;
import com.example.blackjackgame.model.Pics;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ViewModel{

    ArrayList<Card> cards= new ArrayList<Card>();
    public Deck(){
        populate();
        shuffle();
    }

    public void populate(){
        for (Pics pic : Pics.values()) {
            cards.add(new Card(pic));
        }
    }

    public int getCardValue(int i){
        Card cardHolder = cards.get(i);
        int val = cardHolder.cardValue();
        return val;
    }

    public int getCardImage(int i){
        Card cardHolder = cards.get(i);
        return cardHolder.cardPic();
    }

    public Card getCard(int i){
        return cards.get(i);
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }


}
