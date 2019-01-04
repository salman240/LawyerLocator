package com.example.salmanyousaf.lawyerlocator.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;

public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CustomItemClickListener itemClickListener;

    public MessageViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(CustomItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onItemClick(view, getAdapterPosition());
    }
}
