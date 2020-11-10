package com.example.blackjackgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blackjack.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private  List<History> mHistory;

    public  HistoryAdapter(Context context, List<History> histories){
        this.mContext = context;
        this.mHistory = histories;
    }
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.history_view, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
             final History historyCurrent = mHistory.get(position);
             holder.textTime.setText(historyCurrent.getTimestamp());
             holder.textTotalPlay.setText(String.valueOf(historyCurrent.getTotalGame()));
             holder.textPercent.setText(String.valueOf(historyCurrent.getTotalWin()));
    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        public TextView textTime,textTotalPlay,textPercent;

        LinearLayout historyLayout;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            textTime = itemView.findViewById(R.id.history_time);
            textTotalPlay = itemView.findViewById(R.id.history_total_play);
            textPercent = itemView.findViewById(R.id.history_percentage_win);
            historyLayout = itemView.findViewById(R.id.history_view);

        }
    }

}
