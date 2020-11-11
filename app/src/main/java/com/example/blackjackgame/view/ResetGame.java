package com.example.blackjackgame.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.blackjack.R;

public class ResetGame extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;

    int csTotalGame;
    int csTotalWin;

    Button btnPlayAGain;
    Button btnMainMenu;

    TextView tvCSTotalGame,tvCSTotalWin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reset_game_view, container, false);
        btnPlayAGain = v.findViewById(R.id.btnPlayAgain);
        btnMainMenu = v.findViewById(R.id.btnMainMenu);
        tvCSTotalGame = v.findViewById(R.id.tvCSTotalGame);
        tvCSTotalWin = v.findViewById(R.id.tvCSTotalWin);

        try{
            csTotalGame = getArguments().getInt("TOTAL_GAME");
            csTotalWin = getArguments().getInt("TOTAL_WIN");
            tvCSTotalGame.setText(String.valueOf(csTotalGame));
            tvCSTotalWin.setText(String.valueOf(csTotalWin));
        } catch (Exception e){
            e.printStackTrace();
        }


        btnPlayAGain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Play again");
                dismiss();
            }
        });
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Main menu");
                dismiss();
            }
        });
        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}