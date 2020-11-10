package com.example.blackjackgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blackjack.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static  int SPLASH_SCREEN = 2500;

    Animation topAnim,bottomAnim;
    ImageView image;
    TextView logo,slogan;
    ConstraintLayout mainLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //animation

        topAnim = AnimationUtils.loadAnimation(this, R.anim.animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        image = findViewById(R.id.image_logo);
        logo = findViewById(R.id.text_logo);
        slogan = findViewById(R.id.text_slogan);
        mainLayout = findViewById(R.id.splash);


        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein,1000);
                finish();
            }
        },SPLASH_SCREEN);



    }
}
