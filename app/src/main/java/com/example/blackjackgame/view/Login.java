package com.example.blackjackgame.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.blackjack.R;
import com.example.blackjackgame.service.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    //Animation layoutAnimation;
    Button buttonLogin, buttonRegister;
    TextInputLayout userName, userPassword;
    LinearLayout loginForm;

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        buttonRegister = findViewById(R.id.btn_new_user);
        userName = findViewById(R.id.txt_username);
        userPassword = findViewById(R.id.txt_password);
        buttonLogin = findViewById(R.id.btn_sign_in);
        loginForm = findViewById(R.id.login_form_test);


        sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String currentUserName = userDetails.get(SessionManager.KEY_USERNAME);
        if (currentUserName != null) {
            startActivity(new Intent(getApplicationContext(), GameMenu.class));
            finish();
        } else {
            loginForm.setVisibility(View.VISIBLE);
        }

        //validate user name

        //validate password
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = userName.getEditText().getText().toString().trim();
                final String password = userPassword.getEditText().getText().toString().trim();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                Query checkUser = reference.orderByChild("userName").equalTo(username);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userName.setError(null);
                            userName.setEnabled(false);
                            String passwordFromDb = snapshot.child(username).child("password").getValue(String.class);


                            assert passwordFromDb != null;
                            if (passwordFromDb.equals(password)) {
                                userPassword.setError(null);
                                userPassword.setEnabled(false);

                                //Get data from database
                                String _fullname = snapshot.child(username).child("fullName").getValue(String.class);
                                String _username = snapshot.child(username).child("userName").getValue(String.class);
                                String _useremail = snapshot.child(username).child("userEmail").getValue(String.class);
                                String _password = snapshot.child(username).child("password").getValue(String.class);

                                //Create user session
//                                 sessionManager = new SessionManager(Login.this);

                                sessionManager.createLoginSession(_fullname, _username, _password, _useremail);


                                Intent intent = new Intent(Login.this, GameMenu.class);
                                startActivity(intent);
                                finish();
                            } else {
                                userPassword.setError("Wrong Password");
                                userPassword.requestFocus();
                            }
                        } else {
                            userName.setError("No such User");
                            userName.requestFocus();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);

            }
        });



    }

}
