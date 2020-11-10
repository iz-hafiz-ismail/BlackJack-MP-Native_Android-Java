package com.example.blackjackgame.view;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.blackjack.R;

public class Setting extends AppCompatDialogFragment {
    int totalMaxCard = 5;
    boolean recordHistory;
    TextView tvMaxCardHit;
    ImageView btnPlus, btnMinus;
    editDataListener listener;
    Switch toogleSwitch;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.setting_view, null);
        tvMaxCardHit = (TextView) view.findViewById(R.id.tvMaxCardHit);
        btnPlus = (ImageView) view.findViewById(R.id.imageView6);
        btnMinus = (ImageView) view.findViewById(R.id.imageView5);
        toogleSwitch = (Switch) view.findViewById(R.id.switchHistory);

        totalMaxCard = getArguments().getInt("num");
        recordHistory = getArguments().getBoolean("boolean");
        tvMaxCardHit.setText(String.valueOf(totalMaxCard));
        toogleSwitch.setChecked(recordHistory);

        toogleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recordHistory = true;
                    Log.d("asd", String.valueOf(recordHistory));

                } else {
                    recordHistory = false;
                    Log.d("asd", String.valueOf(recordHistory));
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalMaxCard < 52) {
                    totalMaxCard = totalMaxCard + 1;
                    tvMaxCardHit.setText(String.valueOf(totalMaxCard));
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalMaxCard > 3) {
                    totalMaxCard = totalMaxCard - 1;
                    tvMaxCardHit.setText(String.valueOf(totalMaxCard));
                }
            }
        });


        builder.setView(view)
                .setTitle("GAME SETTING")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyValue(totalMaxCard, recordHistory);
                    }
                });
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (editDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement editDataListener");
        }
    }

    public interface editDataListener {
        void applyValue(int maxCardSetting, boolean recordHistorySetting);
    }
}
