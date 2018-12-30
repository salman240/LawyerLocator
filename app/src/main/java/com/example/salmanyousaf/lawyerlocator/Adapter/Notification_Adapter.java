package com.example.salmanyousaf.lawyerlocator.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Helper.HelperMethods;
import com.example.salmanyousaf.lawyerlocator.Model.Reserve;
import com.example.salmanyousaf.lawyerlocator.R;

import java.util.List;


public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyViewHolder>/*implements Filterable*/{

    private List<Reserve> mUserdata;
    private String mSender;
    private HelperMethods helperMethods;
    private SharedPreferences sharedPreferences;
    private String IPAddress;
    private Context mContext;



    public Notification_Adapter(List<Reserve> userdata, String sender, Context context) {
        this.mUserdata = userdata;
        this.mSender = sender;
        mContext = context;

    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView resUser, resUserName;
        public Button allow, deny;

        private MyViewHolder(View view) {
            super(view);

            resUser = view.findViewById(R.id.reserveUser_text_view);
            resUserName = view.findViewById(R.id.UserName_text_view);

            allow = view.findViewById(R.id.btnAllow);
            deny = view.findViewById(R.id.btnDeny);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_notification, parent, false);


        return new MyViewHolder(itemView);
    }





    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int pos) {
        //Case - isReserves = false
        if(!(mUserdata.get(pos)).getIsReserve())
        {
            if(mSender.equals(mUserdata.get(pos).getResReciever()))
            {
                //case 1.2 - when user is reciever it should be given an option to allow or deny that reserve
                holder.resUserName.setText(mUserdata.get(pos).getResSender());

                holder.allow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        helperMethods = new HelperMethods();
//                        sharedPreferences = helperMethods.GetIpAddressSharedPreferences();
//                        IPAddress = sharedPreferences.getString(IP_ADDRESS, null);

                        authReserve(mUserdata.get(pos).getResSender(), mSender);

                        holder.allow.setVisibility(View.GONE);
                        holder.deny.setVisibility(View.GONE);
                        holder.resUser.setVisibility(View.GONE);
                        holder.resUserName.setText("User allowed successfully!");
                    }
                });

                holder.deny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        helperMethods = new HelperMethods();
//                        sharedPreferences = helperMethods.GetIpAddressSharedPreferences();
//                        IPAddress = sharedPreferences.getString(IP_ADDRESS, null);

//                        deleteReserve(mUserdata.get(pos).getResSender(), mSender);

                        holder.deny.setEnabled(false);

                        holder.allow.setVisibility(View.GONE);
                        holder.deny.setVisibility(View.GONE);
                        holder.resUser.setVisibility(View.GONE);
                        holder.resUserName.setText("User denied successfully!");
                    }
                });

            }


            holder.itemView.setEnabled(false);

        }

    }



    @Override
    public int getItemCount() {
        return mUserdata.size();
    }




    //AsyncMethod for Api calls
    //Allow user as a Reserve
    private void authReserve(String sender, String reciever) {
//        disposable.add(
//                apiService.authReseve(sender, reciever)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableCompletableObserver() {
//                            @Override
//                            public void onComplete() {
//                                Toast.makeText(mContext, "User is Reserved successfully!", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//        );
//    }


        //Delete a reserve User
//    private void deleteReserve(String sender, String reciever)
//    {
//        disposable.add(
//                apiService.deleteReserve(sender, reciever)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableCompletableObserver() {
//                            @Override
//                            public void onComplete() {
//                                Toast.makeText(mContext, "User is Deleted successfully!", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        })
//        );
//    }
    }

}//class ends

