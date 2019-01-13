package com.example.salmanyousaf.lawyerlocator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.Adapter.MessageViewHolder;
import com.example.salmanyousaf.lawyerlocator.Helper.HelperMethods;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Chat;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.Objects;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.encodeEmail;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MESSAGES = "messages";
    @BindView(R.id.mViewChat)
    RelativeLayout mView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.textViewNoData)
    TextView textViewNoData;

    @BindView(R.id.EditTextMessage)
    TextInputEditText editTextMessage;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.chatRecycleView)
    RecyclerView recyclerView;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindAnim(R.anim.zoom_in)
    Animation recycleAnimation;

    private ActionBar actionBar;
    private FirebaseRecyclerAdapter<Chat, MessageViewHolder> adapter;
    private String key;

    private Unbinder unbinder;
    private DatabaseReference chatDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private ValueEventListener valueEventListener;

    private String senderEmail;
    private String recieverEmail;

    private HelperMethods helperMethods = new HelperMethods();
    private LinearLayoutManager mLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        unbinder = ButterKnife.bind(this);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        chatDatabaseReference = firebaseDatabase.getReference("Chat");
        userDatabaseReference = firebaseDatabase.getReference("User");

        //Admob
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(R.string.chat);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        senderEmail = intent.getStringExtra("senderEmail");
        recieverEmail = intent.getStringExtra("recieverEmail");

        //check if user is actually the sender(conflict caused by FragmentChat)
        if(!encodeEmail(Paper.book().read("email").toString()).equals(senderEmail))
        {
            recieverEmail = senderEmail;
            senderEmail = encodeEmail(Paper.book().read("email").toString());
        }

        imageView.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAnimation(recycleAnimation);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //loading chats
        getChat();
        getStatus();
    }

    @Override
    public void onClick(View v) {
        sendMessage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(valueEventListener != null)
        {
            userDatabaseReference.child(recieverEmail).child("status").removeEventListener(valueEventListener);
            valueEventListener = null;
        }
        adapter.stopListening();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }


    //Firebase methods
    private void getChat() {
        loadingIndicator.setVisibility(View.GONE);
        textViewNoData.setVisibility(View.VISIBLE);

        FirebaseRecyclerOptions<Chat> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(chatDatabaseReference.child(key).child(MESSAGES), Chat.class).build();
        adapter = new FirebaseRecyclerAdapter<Chat, MessageViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = getLayoutInflater().inflate(R.layout.list_item_messages, viewGroup, false);
                return new MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Chat model) {
                textViewNoData.setVisibility(View.GONE);

                TextView senderMessage =  holder.itemView.findViewById(R.id.tvSenderMessage);
                TextView senderDate =  holder.itemView.findViewById(R.id.tvSenderDate);
                TextView recieverMessage =  holder.itemView.findViewById(R.id.tvRecieverMessage);
                TextView recieverDate =  holder.itemView.findViewById(R.id.tvRecieverDate);

                if(senderEmail.equals(model.getMessageSender()))
                {
                    senderMessage.setText(model.getMessage());
                    senderDate.setText(helperMethods.FormatDateTime(model.getDatetime()));
                    senderMessage.setVisibility(View.VISIBLE);
                    senderDate.setVisibility(View.VISIBLE);

                    recieverMessage.setVisibility(View.GONE);
                    recieverDate.setVisibility(View.GONE);
                }
                else
                {
                    recieverMessage.setText(model.getMessage());
                    recieverDate.setText(helperMethods.FormatDateTime(model.getDatetime()));
                    recieverMessage.setVisibility(View.VISIBLE);
                    recieverDate.setVisibility(View.VISIBLE);

                    senderMessage.setVisibility(View.GONE);
                    senderDate.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Toasty.error(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        };

        //for scrolling to bottom
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mLayoutManager.smoothScrollToPosition(recyclerView, null, adapter.getItemCount());
            }
        });
        recyclerView.setAdapter(adapter);
    }


    private void getStatus() {
        valueEventListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(Objects.requireNonNull(dataSnapshot.getValue()).toString().equals("true"))
                {
                    actionBar.setSubtitle("Online");
                }
                else
                {
                    actionBar.setSubtitle("Offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(ChatActivity.this, "No message to send", Toast.LENGTH_SHORT, true).show();
            }
        };
        userDatabaseReference.child(recieverEmail).child("status").addValueEventListener(valueEventListener);
        //Todo Add listener
    }


    private void sendMessage() {
        if(editTextMessage.getText().toString().equals(""))
        {
            Toasty.error(ChatActivity.this, "No message to send", Toast.LENGTH_SHORT, true).show();
        }
        else
        {
            Chat chat = new Chat(senderEmail, recieverEmail, DateTime.now().toString(), editTextMessage.getText().toString());
            chatDatabaseReference.child(key).child(MESSAGES).push().setValue(chat);
            editTextMessage.setText("");
            //TODO add listener later (indicators for each message)
        }
    }


}//class ends.