package com.example.blackjackgame.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;

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

    MerlinsBeard merlinsBeard;
    Merlin merlin;

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

        merlin = new Merlin.Builder().withConnectableCallbacks().build(getApplicationContext());
        merlin.bind();
        merlinsBeard = MerlinsBeard.from(getApplicationContext());

        try{
            Intent mIntent = getIntent();
            recordHistory = mIntent.getBooleanExtra("RECORDHISTORY",true);

        }catch (Exception e){
            e.printStackTrace();
        }

        if (!(merlinsBeard.isConnected() || merlinsBeard.isConnectedToWifi())) {
            recordHistory =false;
        }

        currentUser = new SessionManager(GameMenu.this);
        HashMap<String,String> usersDetails = currentUser.getUserDetailFromSession();
        final String username = usersDetails.get(SessionManager.KEY_USERNAME);
        textUserName.setText(username);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("history");
                Query checkUser = reference.orderByChild("userName").equalTo(username);

                Date currentTime = Calendar.getInstance().getTime();
                String dateData = currentTime.toString();
                String date = dateData.substring(4,10)+" "+dateData.substring(30,34);
                String time = dateData.substring(11,20);
                final String timeStamp = time +"  ||  "+ date;

                final int totalGame = 0;
                final int totalWin = 0;

                if (merlinsBeard.isConnected() || merlinsBeard.isConnectedToWifi()) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){


                                if(recordHistory==true){
                                    History history = new History(timeStamp,totalGame,totalWin);
                                    reference.child(username).child(String.valueOf(timeStamp)).setValue(history);
                                }

                                Intent intent = new Intent(GameMenu.this, GameActivity.class);
                                intent.putExtra("MAXCARD", maxCard);
                                intent.putExtra("USERNAME", username);
                                intent.putExtra("HISTORYID", timeStamp);
                                Log.d("testo", String.valueOf(timeStamp));
                                intent.putExtra("RECORDHISTORY", recordHistory);
                                startActivity(intent);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if (!(merlinsBeard.isConnected() || merlinsBeard.isConnectedToWifi())) {
                    recordHistory =false;
                    Toast toast = Toast.makeText(getApplicationContext(), "No internet connection detected, history will not be recorded", Toast.LENGTH_LONG);
                    toast.show();
                    Intent intent = new Intent(GameMenu.this, GameActivity.class);
                    intent.putExtra("MAXCARD", maxCard);
                    intent.putExtra("USERNAME", username);
                    intent.putExtra("HISTORYID", timeStamp);
                    Log.d("testo", String.valueOf(timeStamp));
                    intent.putExtra("RECORDHISTORY", recordHistory);
                    startActivity(intent);
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        merlin.bind();
    }

    @Override
    protected void onPause() {
        merlin.unbind();
        super.onPause();
    }
}
