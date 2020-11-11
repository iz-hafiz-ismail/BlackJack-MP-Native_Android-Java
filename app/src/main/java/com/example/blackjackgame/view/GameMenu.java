package com.example.blackjackgame.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blackjack.R;
import com.example.blackjackgame.model.History;
import com.example.blackjackgame.service.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GameMenu extends AppCompatActivity implements Setting.editDataListener{

    static int data;
    static int maxCard=5;
    boolean recordHistory=true;

    Animation leftRight;
    Button btnPlay,btnSetting,btnViewHistory;
    TextView textUserName;

    SessionManager currentUser;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        leftRight = AnimationUtils.loadAnimation(this, R.anim.left_right);

        btnPlay = findViewById(R.id.btn_play);
        btnSetting = findViewById(R.id.btn_logout);
        btnViewHistory = findViewById(R.id.btn_view_history);
        textUserName = findViewById(R.id.txt_user_name_game);
        btnPlay.setAnimation(leftRight);
        btnSetting.setAnimation(leftRight);



        currentUser = new SessionManager(GameMenu.this);
        HashMap<String,String> usersDetails = currentUser.getUserDetailFromSession();
        final String username = usersDetails.get(SessionManager.KEY_USERNAME);
        textUserName.setText(username);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("history");
                Query checkUser = reference.orderByChild("userName").equalTo(username);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Date currentTime = Calendar.getInstance().getTime();
                            String timeStamp = currentTime.toString();
                            int totalGame = 0;
                            int totalWin = 0;

                            if(recordHistory==true){
                                History history = new History(timeStamp,totalGame,totalWin);
                                reference.child(username).child(String.valueOf(timeStamp)).setValue(history);
                            }

                            Intent intent = new Intent(GameMenu.this, GameActivity.class);
                            intent.putExtra("MAXCARD", maxCard);
                            intent.putExtra("USERNAME", username);
                            intent.putExtra("HISTORYID", timeStamp);
                            intent.putExtra("RECORDHISTORY", recordHistory);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameMenu.this, HistoryView.class);
                startActivity(intent);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.logoutUserFromSession();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            }
        });
    }

    public void openSetting(View view) {
        Setting layoutSetting = new Setting();
        Bundle args = new Bundle();
        args.putInt("num", maxCard);
        args.putBoolean("boolean",recordHistory);
        layoutSetting.setArguments(args);
        layoutSetting.show(getSupportFragmentManager(), "layout setting");
    }

    @Override
    public void applyValue(int maxCardSetting,boolean recordHistorySetting) {
        maxCard = maxCardSetting;
        recordHistory = recordHistorySetting;
    }
}
