package com.example.blackjackgame.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blackjack.R;
import com.example.blackjackgame.service.SessionManager;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private  List<History> mHistory;
    private OnItemClickListener mListener;
    History historyCurrent;

    public  HistoryAdapter(Context context, List<History> histories){
        this.mContext = context;
        this.mHistory = histories;
    }
    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.history_view, parent, false);
        return new HistoryViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, final int position) {

             historyCurrent = mHistory.get(position);
             holder.textTime.setText(historyCurrent.getTimestamp());
             holder.textTotalPlay.setText(String.valueOf(historyCurrent.getTotalGame()));
             holder.textPercent.setText(String.valueOf(historyCurrent.getTotalWin()));

    }


    @Override
    public int getItemCount() {
        return mHistory.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        public TextView textTime,textTotalPlay,textPercent,buttonDelete;

        LinearLayout historyLayout;

        public HistoryViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            textTime = itemView.findViewById(R.id.history_time);
            textTotalPlay = itemView.findViewById(R.id.history_total_play);
            textPercent = itemView.findViewById(R.id.history_percentage_win);
            historyLayout = itemView.findViewById(R.id.history_view);
            buttonDelete = itemView.findViewById(R.id.btn_delete);

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);

                        }
                    }
                }
            });
        }
    }

}
