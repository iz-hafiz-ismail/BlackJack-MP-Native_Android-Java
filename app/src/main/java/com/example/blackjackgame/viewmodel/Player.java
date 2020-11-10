package com.example.blackjackgame.viewmodel;

import androidx.lifecycle.ViewModel;

public class Player extends ViewModel {

    int[] cardValue = new int[52];
    int totalValue = 0;
    int totalCardDraw = 0;
    int calValue = 0;
    static final int PLAYER_BLACKJACK=0;
    static final int PLAYER_NOT_BLACKJACK=1;
    static final int PLAYER_ON_LIMIT=0;
    static final int PLAYER_IN_LIMIT=1;
    static final int PLAYER_OVER_LIMIT=2;
    static final int PLAYER_MAX_WIN=0;
    static final int PLAYER_MAX_LOSS=1;
    static final int SHOWDOWN=2;


    public void setCardValue(int index,int cValue) {
        cardValue[index] = cValue;
        totalValue = totalValue+cValue;
        calValue = totalValue;
        totalCardDraw++;
        if(totalValue>21){
            for(int i=0;i<=totalCardDraw;i++){
                if(cardValue[i]==11){
                    if (calValue > 21) {
                        calValue=calValue-10;
                    }
                }
            }
        }
    }

    public void resetGame(){
        this.totalValue=0;
        this.calValue=0;
        this.totalCardDraw=0;
    }

    public int getTotalValue() {
        return calValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public int playerBlackjack(){
        if(calValue==21){
            return PLAYER_BLACKJACK;
        }
        else{
            return PLAYER_NOT_BLACKJACK;
        }

    }
    public int playerHitLoss(){
        if(calValue==21){
            return PLAYER_ON_LIMIT;
        }
        if(calValue<21){
            return PLAYER_IN_LIMIT;
        }
        else{
            return PLAYER_OVER_LIMIT;
        }

    }

    public int playerMaxCardStatus(){
        if(calValue<17){
            return PLAYER_MAX_WIN;
        }
        else if(calValue>21){
            return PLAYER_MAX_LOSS;
        }
        else{
            return SHOWDOWN;
        }
    }
}
