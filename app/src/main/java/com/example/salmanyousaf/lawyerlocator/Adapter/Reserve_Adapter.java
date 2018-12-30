package com.example.salmanyousaf.lawyerlocator.Adapter;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Helper.HelperMethods;
import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;
import com.example.salmanyousaf.lawyerlocator.Model.Reserve;
import com.example.salmanyousaf.lawyerlocator.R;

import java.util.List;

public class Reserve_Adapter extends RecyclerView.Adapter<Reserve_Adapter.MyViewHolder> /*implements Filterable*/ {

    private List<Reserve> mUserdata;
    private CustomItemClickListener mListener;
    private String mSender;
    private HelperMethods helperMethods = new HelperMethods();



    public Reserve_Adapter(List<Reserve> userdata, String sender, CustomItemClickListener listener) {
        this.mUserdata = userdata;
        this.mListener = listener;
        this.mSender = sender;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, date, youResHim, heResYou;//resUser, resUserName;
        //public Button allow, deny;

        private MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name_text_view);
            date = view.findViewById(R.id.date_text_view);
            youResHim = view.findViewById(R.id.you_res_text_view);
            heResYou = view.findViewById(R.id.he_res_text_view);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_reserve, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int pos) {
        //Case 1 - isReserves = false and user send the reserve
        if (!(mUserdata.get(pos)).getIsReserve()) {
            if (mSender.equals(mUserdata.get(pos).getResSender())) {
                //case 1.1 - when user is reserve sender it should be given a message that reserve is sent
                holder.name.setText(mUserdata.get(pos).getResReciever());
                holder.youResHim.setText(R.string.requestSent);
                holder.youResHim.setTextColor(Color.argb(255, 0, 200, 0));

                holder.date.setText(helperMethods.FormatDateTime(mUserdata.get(pos).getResDateTime()));
                holder.heResYou.setVisibility(View.GONE);

                holder.itemView.setEnabled(false);
                return;
            }
            //Case 2 - isReserves = false and user recieves the reserve
            else if (mSender.equals(mUserdata.get(pos).getResReciever())) {
                //case 1.1 - when user is reserve sender it should be given a message that reserve is sent
                holder.name.setText(mUserdata.get(pos).getResSender());
                holder.youResHim.setText("You are requested by this user");
                holder.youResHim.setTextColor(Color.argb(255, 0, 200, 0));

                holder.date.setText(helperMethods.FormatDateTime(mUserdata.get(pos).getResDateTime()));
                holder.heResYou.setVisibility(View.GONE);

                holder.itemView.setEnabled(false);
                return;
            }
        }



        //Case 2 - isReserves = true
        if ((mUserdata.get(pos)).getIsReserve()) {
            if (mSender.equals(mUserdata.get(pos).getResSender())) {
                holder.name.setText(mUserdata.get(pos).getResReciever());
                holder.heResYou.setVisibility(View.GONE);

            } else if (mSender.equals(mUserdata.get(pos).getResReciever())) {
                holder.name.setText(mUserdata.get(pos).getResSender());
                holder.youResHim.setVisibility(View.GONE);
            }

            holder.date.setText(helperMethods.FormatDateTime(mUserdata.get(pos).getResDateTime()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, holder.getAdapterPosition());
                }
            });

        }
    }


    @Override
    public int getItemCount() {
//        return mUserdata.size();
        return 10;
    }


}