package com.example.salmanyousaf.lawyerlocator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Adapter.ChatViewHolder;
import com.example.salmanyousaf.lawyerlocator.Adapter.Chat_Adapter;
import com.example.salmanyousaf.lawyerlocator.Adapter.PeopleViewHolder;
import com.example.salmanyousaf.lawyerlocator.Helper.Utils;
import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Chat;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Chats;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paperdb.Paper;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.decodeEmail;


public class FragmentChat extends Fragment {


    @BindView(R.id.mChatView)
    RelativeLayout mView;

    @BindView(R.id.swiperefreshChat)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.textViewNoData)
    TextView textViewNoData;

    @BindView(R.id.textViewNoDataDetails)
    TextView textViewNoDataDetails;

    @BindView(R.id.textViewNoDataOnDB)
    TextView textViewNoDataDb;

    @BindView(R.id.chat)
    RecyclerView recyclerView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Chats, ChatViewHolder> adapter;

    private Utils utils;

    private Unbinder unbinder;


    public FragmentChat() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        utils = new Utils(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
        if(adapter != null)
            adapter.stopListening();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Chat");

        getChats();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChats();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }


    //Helper methods (Firebase Call)
    public void getChats() {
        final String senderEmail = Paper.book().read("email");

        FirebaseRecyclerOptions<Chats> options = new FirebaseRecyclerOptions.Builder<Chats>()
                .setQuery(  databaseReference.orderByChild("chatSender").equalTo(senderEmail), Chats.class).build();

        adapter = new FirebaseRecyclerAdapter<Chats, ChatViewHolder>(options) {
            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_chat, viewGroup, false);
                return new ChatViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull final Chats model) {
                loadingIndicator.setVisibility(View.GONE);

                //Views init
                TextView name = holder.itemView.findViewById(R.id.name_text_view);

                //showing the 2nd persons name rather than sender
                if(senderEmail.equals(model.getChatReciever()))
                {
                    name.setText(decodeEmail(model.getChatSender()));
                }
                else
                {
                    name.setText(decodeEmail(model.getChatReciever()));
                }

                //onclick listener
                holder.setItemClickListener(new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Snackbar.make(mView, error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        };

        recyclerView.setAdapter(adapter);
    }


}//class ends
