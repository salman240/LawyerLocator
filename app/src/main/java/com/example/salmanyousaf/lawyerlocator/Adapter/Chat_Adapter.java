package com.example.salmanyousaf.lawyerlocator.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Helper.HelperMethods;
import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Chats;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Signup;
import com.example.salmanyousaf.lawyerlocator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.decodeEmail;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.MyViewHolder> /*implements Filterable*/ {

    private List<Chats> mChats;
    private CustomItemClickListener mListener;
    private String senderEmail;
    private HelperMethods helperMethods = new HelperMethods();


    public Chat_Adapter(List<Chats> userdata, String sender, CustomItemClickListener listener) {
        this.mChats = userdata;
        this.mListener = listener;
        this.senderEmail = sender;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date;
        public ImageView image_view;

        private MyViewHolder(View view) {
            super(view);

            name =  view.findViewById(R.id.name_text_view);
            date =  view.findViewById(R.id.date_text_view);
            image_view = view.findViewById(R.id.image_view);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int pos) {
        //Show the name of other person to identify the chat.
        final Chats chat = mChats.get(pos);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User");

        //getting user name and image from email
        if (senderEmail.equals(chat.getChatSender())) {
            databaseReference.child(chat.getChatReciever()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Signup signup = dataSnapshot.getValue(Signup.class);
                    if (signup != null)
                    {
                        holder.name.setText(signup.getName());
                        Picasso.get().load(signup.getProfileImage()).placeholder(R.drawable.placeholder_image).error(R.drawable.no_image)
                                .fit().into(holder.image_view);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(getClass().getSimpleName(), databaseError.getMessage());
                }
            });
        } else if (senderEmail.equals(chat.getChatReciever())) {
            databaseReference.child(chat.getChatSender()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Signup signup = dataSnapshot.getValue(Signup.class);
                    if (signup != null)
                    {
                        holder.name.setText(signup.getName());
                        Picasso.get().load(signup.getProfileImage()).placeholder(R.drawable.placeholder_image).error(R.drawable.no_image)
                                .fit().into(holder.image_view);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(getClass().getSimpleName(), databaseError.getMessage());
                }
            });
        }

        if(chat.getmDate() != null)
            holder.date.setText(helperMethods.FormatDateTime(chat.getmDate()));
    }


    @Override
    public int getItemCount() {
        return mChats.size();
    }

}//class ends.





