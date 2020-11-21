package com.example.blackjackgame.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.blackjack.R;
import com.example.blackjackgame.model.History;
import com.example.blackjackgame.model.HistoryAdapter;
import com.example.blackjackgame.service.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HistoryView extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter hAdapter;
    DatabaseReference reference;
    List<History> mHistory;
    SessionManager currentUser;
    String username;
    int historyUpdater=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyview);

        recyclerView = findViewById(R.id.history_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        currentUser = new SessionManager(HistoryView.this);
        final HashMap<String, String> usersDetails = currentUser.getUserDetailFromSession();
        username = usersDetails.get(SessionManager.KEY_USERNAME);

        mHistory = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("history");
        reference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (historyUpdater == 0) {
                        for (DataSnapshot historySnapShot : snapshot.getChildren()) {
                            History history = historySnapShot.getValue(History.class);
                            mHistory.add(history);
                            Log.d("test", "onDataChange: "+ mHistory);
                        }
                        Log.d("test", "onDataChange: ayam");
                        hAdapter = new HistoryAdapter(HistoryView.this, mHistory);
                        recyclerView.setAdapter(hAdapter);
                    }



                    hAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {

                        @Override
                        public void onDeleteClick(final int position) {

                           final Query mQuery = reference.child(username).orderByChild("timestamp").equalTo(mHistory.get(position).getTimestamp());
                           mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot ds:snapshot.getChildren()){
                                        ds.getRef().removeValue();
                                        historyUpdater=1;


                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });
                           removeItem(position);
                        }

                    });


                }
                else{
                    Log.d("test", "onDataChange: huhu");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void removeItem(int p){
        mHistory.remove(p);
        hAdapter.notifyItemRemoved(p);
    }
}
