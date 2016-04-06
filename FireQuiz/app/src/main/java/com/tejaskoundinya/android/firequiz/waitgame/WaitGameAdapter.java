package com.tejaskoundinya.android.firequiz.waitgame;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tejaskoundinya.android.firequiz.R;
import com.tejaskoundinya.android.firequiz.models.GameWaitModel;

import java.util.List;

/**
 * Created by Tejas Koundinya on 09-01-2016.
 */
public class WaitGameAdapter extends RecyclerView.Adapter<WaitGameAdapter.ViewHolder> {

    public List<GameWaitModel> mGameWaitModels;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mGameTextView;
        public LinearLayout mLinearLayout;
        public ViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
            this.mLinearLayout= linearLayout;
            mGameTextView = (TextView)linearLayout.findViewById(R.id.wait_game_name_text_view);
        }
    }

    public WaitGameAdapter(List<GameWaitModel> gameWaitModels) {
        mGameWaitModels = gameWaitModels;
    }

    @Override
    public WaitGameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wait_game_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mGameTextView.setText(mGameWaitModels.get(position).getGameMaster());
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if(mGameWaitModels != null) {
            itemCount = mGameWaitModels.size();
        }
        return itemCount;
    }
}
