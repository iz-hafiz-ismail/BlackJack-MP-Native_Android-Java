package com.example.blackjackgame.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.blackjack.R;
import com.example.blackjackgame.model.History;
import com.example.blackjackgame.viewmodel.Dealer;
import com.example.blackjackgame.viewmodel.Deck;
import com.example.blackjackgame.viewmodel.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Thread.sleep;

public class GameActivity extends AppCompatActivity implements  ResetGame.BottomSheetListener{
    TextView playerTotalPoint,gameStatus,maxHitAllowed,dealerTotalPoint;
    ImageView playerFirstCard,playerSecondCard,dealerFirstCard,dealerSecondCard;
    ImageView[] arrayPlayerCard = {playerFirstCard, playerSecondCard};
    ImageView[] arrayDealerCard = {dealerFirstCard,dealerSecondCard};
    Button btnHit,btnStand,btnStart;

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

    boolean recordHistory;
    boolean togglePlayerBlackjack = false;

    static final int BLACKJACK=0;
    static final int NOT_BLACKJACK=1;
    static final int ON_LIMIT=0;
    static final int IN_LIMIT=1;
    static final int OVER_LIMIT=2;
    static final int MAX_WIN=0;
    static final int MAX_LOSS=1;
    static final int SHOWDOWN=2;

    View view;

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
        btnStart=(Button) findViewById(R.id.btnStartGame);

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
            recordHistory = mIntent.getBooleanExtra("RECORDHISTORY",true);

        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            Intent mIntent = getIntent();
            totalGame =  mIntent.getIntExtra("TOTALGAME", 0);
            totalWin =  mIntent.getIntExtra("TOTALWIN", 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        maxHitAllowed.setText(String.valueOf(maxCard));
        reference = FirebaseDatabase.getInstance().getReference("history");
        Query checkUser = reference.orderByChild("userName").equalTo(username);

    }

    public void Start(View view) {
        btnStart.setVisibility(view.GONE);
        startGame();
    }

    private void startGame() {
        btnHit.setVisibility(view.VISIBLE);
        btnStand.setVisibility(view.VISIBLE);
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
//        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(arrayPlayerCard[indexPlayer-1], "scaleX", 1f, 0f);
//        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(arrayPlayerCard[indexPlayer-1], "scaleX", 0f, 1f);
//        oa1.setDuration(500);
//        oa2.setDuration(500);
//        oa1.setInterpolator(new DecelerateInterpolator());
//        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
//        oa1.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                arrayPlayerCard[indexPlayer-1].setImageResource(mDeck.getCardImage(indexPlayer-1));
//                oa2.start();
//            }
//        });
//        oa1.start();
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
        if(dealerCurrCard>=maxCard) {
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
    }

    public void stand(View view) {
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        dealerDrawCard();
        dealerSetCard();
        dealerBlackjack(mDealer.dealerBlackjack());
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
                togglePlayerBlackjack = true;
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
                if(togglePlayerBlackjack == true){
                    gameStatus.setText("Player Blackjack");
                    playerWinGame();
                }
                else if(togglePlayerBlackjack == false){
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
                    else if(dealerCurrCard>=maxCard && mDealer.getTotalValue()>=17){
                        checkDealerMaxCard();
                    }
                }
                break;
            case BLACKJACK:
                if(togglePlayerBlackjack == true){
                    gameStatus.setText("Both blackjack, but player win as rule stated");
                    playerWinGame();
                }
                else  if(togglePlayerBlackjack == false){
                    gameStatus.setText("Dealer Blackjack");
                    playerLossGame();
                }
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
                battleShowdown();
                break;
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
            playerWinGame();
        }
        else if((mDealer.getTotalValue()<mPlayer.getTotalValue())){
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
        if(recordHistory==true){
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        double calcPercent = (((double)totalWin/(double)totalGame)*100);
//                        int percentageWin = (int) calcPercent;
//                        Log.d("testo", "onDataChange: "+String.valueOf(calcPercent));
                        History history = new History(historyID,totalGame,(int)calcPercent);
                        reference.child(username).child(String.valueOf(historyID)).setValue(history);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        ResetGame resetGame = new ResetGame();
        resetGame.setCancelable(false);
        Bundle args = new Bundle();
        args.putInt("TOTAL_GAME", totalGame);
        args.putInt("TOTAL_WIN",totalWin);
        resetGame.setArguments(args);
        resetGame.show(getSupportFragmentManager(), "resetGame Layout");
    }

    public void refreshGame(){
        finish();
        overridePendingTransition(0, 0);
        Intent intent = new Intent(GameActivity.this, GameActivity.class);
        intent.putExtra("MAXCARD", maxCard);
        intent.putExtra("USERNAME", username);
        intent.putExtra("HISTORYID", historyID);
        intent.putExtra("RECORDHISTORY", recordHistory);
        intent.putExtra("TOTALGAME", totalGame);
        intent.putExtra("TOTALWIN", totalWin);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent( GameActivity.this, GameMenu.class);
        intent.putExtra("RECORDHISTORY", recordHistory);
        startActivity(intent);
    }

    @Override
    public void onButtonClicked(String text) {
        if(text.equals("Main menu")){
            finish();
            Intent intent = new Intent( GameActivity.this, GameMenu.class);
            intent.putExtra("RECORDHISTORY", recordHistory);
            startActivity(intent);
        }
        else if (text.equals("Play again")){
            refreshGame();
        }
    }

}

