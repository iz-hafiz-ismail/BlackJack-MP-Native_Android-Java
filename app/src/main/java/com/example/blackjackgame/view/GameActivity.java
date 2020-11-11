package com.example.blackjackgame.view;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.blackjack.R;
import com.example.blackjackgame.model.History;
import com.example.blackjackgame.model.HistoryAdapter;
import com.example.blackjackgame.viewmodel.Dealer;
import com.example.blackjackgame.viewmodel.Deck;
import com.example.blackjackgame.viewmodel.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class GameActivity extends AppCompatActivity {
    TextView playerTotalPoint,gameStatus,maxHitAllowed,dealerTotalPoint;
    ImageView playerFirstCard,playerSecondCard,dealerFirstCard,dealerSecondCard;
    ImageView[] arrayPlayerCard = {playerFirstCard, playerSecondCard};
    ImageView[] arrayDealerCard = {dealerFirstCard,dealerSecondCard};
    Button btnHit,btnStand;

    Deck mDeck;
    Player mPlayer;
    Dealer mDealer;
    LinearLayout playerHandView;
    LinearLayout dealerHandView;
    DatabaseReference reference;

    int maxCard=5;
    int currCard=0;
    int dealerCurrCard=0;
    int indexPlayer = 0;
    int indexDealer = 0;
    int totalGame =0;
    int totalWin=0;

    String username;
    String historyID;

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

        playerTotalPoint=(TextView) findViewById(R.id.tvGAPlayerPoint);
        gameStatus=(TextView) findViewById(R.id.tvStatus);
        maxHitAllowed=(TextView) findViewById(R.id.tvGAMaxCardHit);
        dealerTotalPoint=(TextView) findViewById(R.id.tvGADealerPoint);

        btnHit=(Button) findViewById(R.id.buttonHit);
        btnStand=(Button) findViewById(R.id.buttonStand);

        playerFirstCard=(ImageView) findViewById(R.id.imageView);
        playerSecondCard=(ImageView) findViewById(R.id.imageView2);
        dealerFirstCard=(ImageView) findViewById(R.id.imageView3);
        dealerSecondCard=(ImageView) findViewById(R.id.imageView4);

        mDeck = new ViewModelProvider(this).get(Deck.class);
        mPlayer = new ViewModelProvider(this).get(Player.class);
        mDealer = new ViewModelProvider(this).get(Dealer.class);
        playerHandView = findViewById(R.id.layout_player_hand);
        dealerHandView = findViewById(R.id.layout_dealer_hand);

        arrayPlayerCard[0]=playerFirstCard;
        arrayPlayerCard[1]=playerSecondCard;
        arrayDealerCard[0]=dealerFirstCard;
        arrayDealerCard[1]=dealerSecondCard;

        try{
            Intent mIntent = getIntent();
            maxCard =  mIntent.getIntExtra("MAXCARD", 0);
            username = mIntent.getStringExtra("USERNAME");
            historyID = mIntent.getStringExtra("HISTORYID");
        }catch (Exception e){
            e.printStackTrace();
        }

        maxHitAllowed.setText(String.valueOf(maxCard));
        reference = FirebaseDatabase.getInstance().getReference("history");
        Query checkUser = reference.orderByChild("userName").equalTo(username);


        reference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    totalGame = snapshot.child(String.valueOf(historyID)).child("totalGame").getValue(Integer.class);
                    totalWin = snapshot.child(String.valueOf(historyID)).child("totalWin").getValue(Integer.class);
                }
                else{
                    Log.d("testo", "onDataChange: huhu");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        startGame();
    }

    private void startGame() {
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        playerDrawCard();
        playerSetCard();
        playerDrawCard();
        playerSetCard();
        dealerDrawCard();
        dealerSetCard();
        playerBlackjack(mPlayer.playerBlackjack());
    }

    private void newImageViewForLayoutPlayer(LinearLayout handView) {
        ImageView cardView = (ImageView) LayoutInflater.from(handView.getContext())
                .inflate(R.layout.card_item, handView, false);

        cardView.setImageResource(mDeck.getCardImage(indexPlayer-1));
        handView.addView(cardView);
    }

    private void newImageViewForLayoutDealer(LinearLayout handView) {
        ImageView cardView = (ImageView) LayoutInflater.from(handView.getContext())
                .inflate(R.layout.card_item, handView, false);

        cardView.setImageResource(mDeck.getCardImage(indexDealer-1+25));
        handView.addView(cardView);
    }

    private void playerSetCard(){
        arrayPlayerCard[indexPlayer-1].setImageResource(mDeck.getCardImage(indexPlayer-1));
    }

    private void dealerSetCard(){
        arrayDealerCard[indexDealer-1].setImageResource(mDeck.getCardImage(indexDealer-1+25));
    }

    private void playerDrawCard() {
        mPlayer.setCardValue(indexPlayer,mDeck.getCardValue(indexPlayer));
        playerTotalPoint.setText(String.valueOf(mPlayer.getTotalValue()));
        indexPlayer++;
        setCurrCard();
        checkPlayerMaxCard();
    }

    private void dealerDrawCard() {
        mDealer.setCardValue(indexDealer,mDeck.getCardValue(indexDealer+25));
        dealerTotalPoint.setText(String.valueOf(mDealer.getTotalValue()));
        indexDealer++;
        setDealerCurrCard();
    }

    private void checkPlayerMaxCard() {
        if(currCard>=maxCard) {
            btnHit.setEnabled(false);
            btnStand.setEnabled(false);
            playerMaxLimit(mPlayer.playerMaxCardStatus());
        }
    }

    private void checkDealerMaxCard() {
        if(currCard>=maxCard) {
            btnHit.setEnabled(false);
            btnStand.setEnabled(false);
            dealerMaxLimit(mDealer.dealerMaxCardStatus());
        }
    }

    public void hit(View view) {
        playerDrawCard();
        newImageViewForLayoutPlayer(playerHandView);
        if(currCard<maxCard){
            playerHitLoss(mPlayer.playerHitLoss());
        }

    }

    public void dealerHit() {
        dealerDrawCard();
        newImageViewForLayoutDealer(dealerHandView);
        dealerHitLoss(mDealer.dealerHitLoss());
    }

    public void stand(View view) {
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        dealerDrawCard();
        dealerSetCard();
        dealerBlackjack(mDealer.dealerBlackjack());
        while(mDealer.getTotalValue()<17){
            if (dealerCurrCard<maxCard){
                dealerHit();
            }
            else {
                checkDealerMaxCard();
                break;
            }

        }
        if(dealerCurrCard<maxCard && mDealer.getTotalValue()>=17){
            dealerHitLoss(mDealer.dealerHitLoss());
        }
        if(dealerCurrCard==maxCard && mDealer.getTotalValue()>=17){
            checkDealerMaxCard();
        }
    }

    public void playerBlackjack(int status){
        switch(status){
            case NOT_BLACKJACK:
                gameStatus.setText("Hit");
                btnHit.setEnabled(true);
                btnStand.setEnabled(true);
                break;
            case BLACKJACK:
                gameStatus.setText("Player Blackjack");
                btnHit.setEnabled(false);
                btnStand.setEnabled(false);
                dealerDrawCard();
                dealerSetCard();
                dealerBlackjack(mDealer.dealerBlackjack());

                break;
        }
    }

    private void dealerBlackjack(int status) {
        switch(status){
            case NOT_BLACKJACK:
                break;
            case BLACKJACK:
                gameStatus.setText("Dealer Blackjack");
                if(mDealer.getTotalValue()==21 && mPlayer.getTotalValue() == 21){
                    gameStatus.setText("Both blackjack, but player win as rule stated");
                }
                playerLossGame();
                break;
        }
    }

    public void playerHitLoss(int status){
        switch (status){
            case ON_LIMIT:
                gameStatus.setText("On Limit");
                btnHit.setEnabled(false);
                btnStand.setEnabled(true);
                break;
            case IN_LIMIT:
                gameStatus.setText("Hit");
                btnHit.setEnabled(true);
                btnStand.setEnabled(true);
                break;
            case OVER_LIMIT:
                gameStatus.setText("Bust : Player loss");
                btnHit.setEnabled(false);
                btnStand.setEnabled(false);
                playerLossGame();
                break;
        }
    }

    public void dealerHitLoss(int status){
        switch (status){
            case ON_LIMIT:
            case IN_LIMIT:
                gameStatus.setText("SHOWDOWN");
                battleShowdown();
                break;
            case OVER_LIMIT:
                gameStatus.setText("Dealer Bust: Player Win");
                playerWinGame();
                break;
        }
    }

    public void playerMaxLimit(int status){
        switch (status){
            case MAX_WIN:
                gameStatus.setText("Player reach max card: \nplayer win");
                playerWinGame();
                break;
            case MAX_LOSS:
                gameStatus.setText("Player bust and reach max card: \nplayer loss");
                playerLossGame();
                break;
            case SHOWDOWN:
                gameStatus.setText("SHOWDOWN");
                stand(null);
                break;
        }
    }

    public void dealerMaxLimit(int status){
        switch (status){
            case MAX_WIN:
                gameStatus.setText("Dealer reach max card: \nPlayer Loss");
                playerLossGame();
                break;
            case MAX_LOSS:
                gameStatus.setText("Dealer bust reach max card: \nPlayer Win");
                playerWinGame();
                break;
            case SHOWDOWN:
                gameStatus.setText("SHOWDOWN");
                battleShowdown();
                break;
        }
    }

    private void battleShowdown() {
        if(mDealer.getTotalValue()>mPlayer.getTotalValue()){
            gameStatus.setText("Player Loss showdown");
            playerLossGame();
        }
        else if(mDealer.getTotalValue()==mPlayer.getTotalValue()){
            gameStatus.setText("Draw");
        }
        else{
            gameStatus.setText("Player win showdown");
            playerWinGame();
        }
    }

    public void setCurrCard(){
        currCard++;
    }

    public void setDealerCurrCard(){
        dealerCurrCard++;
    }


    public void reset(View view) {
        databaseUpdater();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    public void playerWinGame(){
        totalGame++;
        totalWin++;
        databaseUpdater();
    }

    public void playerLossGame(){
        totalGame++;
        databaseUpdater();
    }

    public void databaseUpdater(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                        History history = new History(historyID,totalGame,totalWin);
                        reference.child(username).child(String.valueOf(historyID)).setValue(history);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void refreshGame(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}

