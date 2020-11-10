package com.example.blackjackgame.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blackjack.R;
import com.example.blackjackgame.model.UserHelper;
import com.example.blackjackgame.other.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    TextInputLayout txtFullName, txtUserName, txtEmail, txtPassword;
    Button btnRegister, btnLogin;


    DatabaseReference reference;
    SessionManager currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtFullName = findViewById(R.id.txt_fullName);
        txtUserName = findViewById(R.id.txt_username);
        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);

        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Register.this, Login.class);
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get all values from textfield


                String username = txtUserName.getEditText().getText().toString().trim();
                reference = FirebaseDatabase.getInstance().getReference("users");

                Query checkUser = reference.orderByChild("userName").equalTo(username);

               checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           txtUserName.setError("user already exist");
                           txtUserName.requestFocus();
                       }
                       else{
                           txtUserName.setError(null);
                           txtUserName.setEnabled(false);

                           String fullname = txtFullName.getEditText().getText().toString();
                           String username = txtUserName.getEditText().getText().toString();
                           String email = txtEmail.getEditText().getText().toString();
                           String password = txtPassword.getEditText().getText().toString();
                           String history = "history";
                           currentUser = new SessionManager(Register.this);
                           currentUser.createLoginSession(fullname, username,email, password);

                           UserHelper user = new UserHelper(fullname,username,email,password,history);
                           reference.child(username).setValue(user);
                           Intent i = new Intent(Register.this, GameMenu.class);
                           startActivity(i);
                           finish();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

            }
        });

    }


}
