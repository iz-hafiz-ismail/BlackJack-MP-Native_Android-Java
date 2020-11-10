package com.example.blackjackgame;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GameMenu extends AppCompatActivity {

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

                            History history = new History(timeStamp,totalGame,totalWin);
                            reference.child(username).child(String.valueOf(timeStamp)).setValue(history);
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
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();

            }
        });
    }
}
