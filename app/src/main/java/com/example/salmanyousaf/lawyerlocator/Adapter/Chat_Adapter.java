package com.example.salmanyousaf.lawyerlocator.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Helper.HelperMethods;
import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;
import com.example.salmanyousaf.lawyerlocator.Model.Chat;
import com.example.salmanyousaf.lawyerlocator.R;

import java.util.List;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.MyViewHolder> /*implements Filterable*/ {

    private List<Chat> mUserdata;
    private CustomItemClickListener mListener;
    private String mSender;
    private HelperMethods helperMethods = new HelperMethods();


    public Chat_Adapter(List<Chat> userdata, String sender, CustomItemClickListener listener) {
        this.mUserdata = userdata;
        this.mListener = listener;
        this.mSender = sender;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date;

        private MyViewHolder(View view) {
            super(view);

            name =  view.findViewById(R.id.name_text_view);
            date =  view.findViewById(R.id.date_text_view);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chat, parent, false);

        final MyViewHolder mViewHolder = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });

        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos) {
        //Show the name of other person to identify the chat.
        if (mSender.equals(mUserdata.get(pos).getChatSender())) {
            holder.name.setText(mUserdata.get(pos).getChatReciever());
        } else if (mSender.equals(mUserdata.get(pos).getChatReciever())) {
            holder.name.setText(mUserdata.get(pos).getChatSender());
        }

        holder.date.setText(helperMethods.FormatDateTime(mUserdata.get(pos).getChatDateTime()));
    }


    @Override
    public int getItemCount() {
//        return mUserdata.size();
        return 10;
    }

}//class ends.





