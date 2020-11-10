package com.example.blackjackgame.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.blackjack.R;
import com.example.blackjackgame.viewmodel.Dealer;
import com.example.blackjackgame.viewmodel.Deck;
import com.example.blackjackgame.viewmodel.Player;

public class GameActivity extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    ImageView pf1,pf2,df1,df2;
    ImageView[] arrayPf = {pf1, pf2};
    ImageView[] arrayDf = {df1, df2};
    Button b1,b2;

    Deck mdeck;
    Player mplayer;
    Dealer mdealer;
    LinearLayout playerHandView;
    LinearLayout dealerHandView;

    int maxCard=3;
    int currCard=0;
    int dealerCurrCard=0;
    int indexPlayer = 0;
    int indexDealer = 0;


    static final int BLACKJACK=0;
    static final int NOT_BLACKJACK=1;
    static final int ON_LIMIT=0;
    static final int IN_LIMIT=1;
    static final int OVER_LIMIT=2;
    static final int MAX_WIN=0;
    static final int MAX_LOSS=1;
    static final int SHOWDOWN=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        t1=(TextView) findViewById(R.id.textView2);
        t2=(TextView) findViewById(R.id.txtStatus);
        t3=(TextView) findViewById(R.id.textView3);
        t4=(TextView) findViewById(R.id.textView4);
        t5=(TextView) findViewById(R.id.textView5);

        b1=(Button) findViewById(R.id.buttonHit);
        b2=(Button) findViewById(R.id.buttonStand);

        pf1=(ImageView) findViewById(R.id.imageView);
        pf2=(ImageView) findViewById(R.id.imageView2);
        df1=(ImageView) findViewById(R.id.imageView3);
        df2=(ImageView) findViewById(R.id.imageView4);

        mdeck = new ViewModelProvider(this).get(Deck.class);
        mplayer = new ViewModelProvider(this).get(Player.class);
        mdealer = new ViewModelProvider(this).get(Dealer.class);
        playerHandView = findViewById(R.id.layout_player_hand);
        dealerHandView = findViewById(R.id.layout_dealer_hand);

        arrayPf[0]=pf1;
        arrayPf[1]=pf2;
        arrayDf[0]=df1;
        arrayDf[1]=df2;

        try{
            Intent mIntent = getIntent();
            maxCard =  mIntent.getIntExtra("MAXCARD", 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        t3.setText(String.valueOf(maxCard));
        startGame();
    }

    private void startGame() {
        b1.setEnabled(false);
        b2.setEnabled(false);
        playerDrawCard();
        playerSetCard();
        playerDrawCard();
        playerSetCard();
        dealerDrawCard();
        dealerSetCard();
        playerBlackjack(mplayer.playerBlackjack());
    }

    private void newImageViewForLayoutPlayer(LinearLayout handView) {
        ImageView cardView = (ImageView) LayoutInflater.from(handView.getContext())
                .inflate(R.layout.card_item, handView, false);

        cardView.setImageResource(mdeck.getCardImage(indexPlayer-1));
        handView.addView(cardView);
    }

    private void newImageViewForLayoutDealer(LinearLayout handView) {
        ImageView cardView = (ImageView) LayoutInflater.from(handView.getContext())
                .inflate(R.layout.card_item, handView, false);

        cardView.setImageResource(mdeck.getCardImage(indexDealer-1+25));
        handView.addView(cardView);
    }

    private void playerSetCard(){
        arrayPf[indexPlayer-1].setImageResource(mdeck.getCardImage(indexPlayer-1));
    }

    private void dealerSetCard(){
        arrayDf[indexDealer-1].setImageResource(mdeck.getCardImage(indexDealer-1+25));
    }

    private void playerDrawCard() {
        mplayer.setCardValue(indexPlayer,mdeck.getCardValue(indexPlayer));
        t1.setText(String.valueOf(mplayer.getTotalValue()));
        indexPlayer++;
        setCurrCard();
        checkPlayerMaxCard();
    }

    private void dealerDrawCard() {
        mdealer.setCardValue(indexDealer,mdeck.getCardValue(indexDealer+25));
        t5.setText(String.valueOf(mdealer.getTotalValue()));
        indexDealer++;
        setDealerCurrCard();
    }

    private void checkPlayerMaxCard() {
        if(currCard>=maxCard) {
            b1.setEnabled(false);
            b2.setEnabled(false);
            playerMaxLimit(mplayer.playerMaxCardStatus());
        }
    }

    private void checkDealerMaxCard() {
        if(currCard>=maxCard) {
            b1.setEnabled(false);
            b2.setEnabled(false);
            dealerMaxLimit(mdealer.dealerMaxCardStatus());
        }
    }

    public void hit(View view) {
        playerDrawCard();
        newImageViewForLayoutPlayer(playerHandView);
        if(currCard<maxCard){
            playerHitLoss(mplayer.playerHitLoss());
        }

    }

    public void dealerHit() {
        dealerDrawCard();
        newImageViewForLayoutDealer(dealerHandView);
        dealerHitLoss(mdealer.dealerHitLoss());
    }

    public void stand(View view) {
        b1.setEnabled(false);
        b2.setEnabled(false);
        dealerDrawCard();
        dealerSetCard();
        dealerBlackjack(mdealer.dealerBlackjack());
        while(mdealer.getTotalValue()<17){
            if (dealerCurrCard<maxCard){
                dealerHit();
            }
            else {
                checkDealerMaxCard();
                break;
            }

        }
        if(dealerCurrCard<maxCard && mdealer.getTotalValue()>=17){
            dealerHitLoss(mdealer.dealerHitLoss());
        }
        if(dealerCurrCard==maxCard && mdealer.getTotalValue()>=17){
            checkDealerMaxCard();
        }
    }

    public void playerBlackjack(int status){
        switch(status){
            case NOT_BLACKJACK:
                t2.setText("Hit");
                b1.setEnabled(true);
                b2.setEnabled(true);
                break;
            case BLACKJACK:
                t2.setText("Player Blackjack");
                b1.setEnabled(false);
                b2.setEnabled(false);
                dealerDrawCard();
                dealerSetCard();
                dealerBlackjack(mdealer.dealerBlackjack());
                break;
        }
    }

    private void dealerBlackjack(int status) {
        switch(status){
            case NOT_BLACKJACK:
                break;
            case BLACKJACK:
                t2.setText("Dealer Blackjack");
                if(mdealer.getTotalValue()==21 && mplayer.getTotalValue() == 21){
                    t2.setText("Both blackjack, but player win as rule stated");
                }
                break;
        }
    }

    public void playerHitLoss(int status){
        switch (status){
            case ON_LIMIT:
                t2.setText("On Limit");
                b1.setEnabled(false);
                b2.setEnabled(true);
                break;
            case IN_LIMIT:
                t2.setText("Hit");
                b1.setEnabled(true);
                b2.setEnabled(true);
                break;
            case OVER_LIMIT:
                t2.setText("Bust : Player loss");
                b1.setEnabled(false);
                b2.setEnabled(false);
                break;
        }
    }

    public void dealerHitLoss(int status){
        switch (status){
            case ON_LIMIT:
            case IN_LIMIT:
                t2.setText("SHOWDOWN");
                battleShowdown();
                break;
            case OVER_LIMIT:
                t2.setText("Dealer Bust: Player Win");
                break;
        }
    }

    public void playerMaxLimit(int status){
        switch (status){
            case MAX_WIN:
                t2.setText("Player reach max card: \nplayer win");
                break;
            case MAX_LOSS:
                t2.setText("Player bust and reach max card: \nplayer loss");
                break;
            case SHOWDOWN:
                t2.setText("SHOWDOWN");
                stand(null);
                break;
        }
    }

    public void dealerMaxLimit(int status){
        switch (status){
            case MAX_WIN:
                t2.setText("Dealer reach max card: \nPlayer Loss");
                break;
            case MAX_LOSS:
                t2.setText("Dealer bust reach max card: \nPlayer Win");
                break;
            case SHOWDOWN:
                t2.setText("SHOWDOWN");
                battleShowdown();
                break;
        }
    }

    private void battleShowdown() {
        if(mdealer.getTotalValue()>mplayer.getTotalValue()){
            t2.setText("Player Loss showdown");
        }
        else if(mdealer.getTotalValue()==mplayer.getTotalValue()){
            t2.setText("Draw");
        }
        else{
            t2.setText("Player win showdown");
        }
    }

    public void setCurrCard(){
        currCard++;
        t4.setText(String.valueOf(currCard));
    }

    public void setDealerCurrCard(){
        dealerCurrCard++;
        t4.setText(String.valueOf(currCard));
    }


    public void reset(View view) {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}

