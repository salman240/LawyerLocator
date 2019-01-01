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
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.Adapter.Chat_Adapter;
import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Chats;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paperdb.Paper;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.encodeEmail;


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

    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    private Chat_Adapter adapter;
    private List<Chats> chatList;

    private Unbinder unbinder;

    public FragmentChat() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        getChats();
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(childEventListener);
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
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

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Chat");

        chatList = new ArrayList<>();

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
        final String senderEmail = encodeEmail(Paper.book().read("email").toString());
        chatList.clear();

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(loadingIndicator != null)
                    loadingIndicator.setVisibility(View.GONE);

                Chats chats = dataSnapshot.getValue(Chats.class);
                if(chats != null) {
                    if (chats.getChatReciever().equals(senderEmail) || chats.getChatSender().equals(senderEmail))
                        chatList.add(chats);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(mView, databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        };
        databaseReference.addChildEventListener(childEventListener);

        //setting adapter
        adapter = new Chat_Adapter(chatList, senderEmail, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }


}//class ends
