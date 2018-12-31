package com.example.salmanyousaf.lawyerlocator;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;
import com.example.salmanyousaf.lawyerlocator.Model.Message;
import com.example.salmanyousaf.lawyerlocator.Adapter.Message_Adapter;
import com.example.salmanyousaf.lawyerlocator.Model.OnlineStatus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;



public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.mViewChat)
    LinearLayout mView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.textViewNoDataOnDB)
    TextView textViewNoData;

    @BindView(R.id.textViewServerProblem)
    TextView textViewServerProblem;

    @BindView(R.id.EditTextMessage)
    EditText editTextMessage;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.chatRecycleView)
    RecyclerView recyclerView;

    @BindAnim(R.anim.zoom_in)
    Animation recycleAnimation;

    //private static final String LOGCAT = "ChatActivity";
    private String SenderEmail;
    private int id;
    int itemCount;
    private String reciever;
    private ActionBar actionBar;
    private String RecieverEmail;

    private Timer timer;
    private Message_Adapter adapter;

    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        unbinder = ButterKnife.bind(this);

//        SharedPreferences sharedPreferences = helperMethods.GetLoginSharedPreferences();
//        SenderEmail = sharedPreferences.getString(SENDEREMAIL, null);


        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.chat);

        Intent intent = getIntent();
        id = intent.getIntExtra("Data", 0);
        reciever = intent.getStringExtra("reciever");
        actionBar.setSubtitle(reciever);


        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAnimation(recycleAnimation);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //Calling api network thread
        AsyncCall();

        //refreshing messages, Declaring the timer
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                AsyncCall();
            }
        }, 10000, 15000);
    }


    @Override
    public void onDestroy() {
        unbinder.unbind();
        timer.cancel();
        super.onDestroy();
    }

    //Helper methods
    public void AsyncCall()
    {
        if(id != 0)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    textViewNoData.setVisibility(View.GONE);
                    textViewServerProblem.setVisibility(View.GONE);
                }
            });

            //Get Messages ... When coming from ChatActivity Fragment
//            disposable.add(
//                 apiService.messages(id)
//                 .subscribeOn(Schedulers.io())
//                 .observeOn(AndroidSchedulers.mainThread())
//                 .subscribeWith(new DisposableSingleObserver<List<Message>>() {
//                     @Override
//                     public void onSuccess(List<Message> messages) {
//                         loadingIndicator.setVisibility(View.INVISIBLE);
//
//                         if(messages.size() == 0)
//                         {
//                             adapter = null;
//                             recyclerView.setAdapter(null);
//
//                             actionBar.setSubtitle("0 Messages");
//                             textViewNoData.setVisibility(View.VISIBLE);
//
//                         }
//                         else
//                         {
//                             //Only refresh the adapter if data is changed
//                             if(itemCount == messages.size())
//                             {
//                                 return;
//                             }
//                             else
//                             {
//                                 itemCount = messages.size();
//                             }
//
//                             Collections.reverse(messages);
//                             adapter = new Message_Adapter(messages, SenderEmail, new CustomItemClickListener() {
//                                 @Override
//                                 public void onItemClick(View v, int position) {
//
//                                 }
//                             });
//                             recyclerView.setAdapter(adapter);
//
//                             //Setting name and status on actionBar
//                             String msgReciever = messages.get(0).getMessageReciever();
//                             String msgSender = messages.get(0).getMessageSender();
//
//                             if(SenderEmail.equals(msgSender)) {
//                                 RecieverEmail = msgReciever;    //the actual reciever
//                             }
//                             else if(SenderEmail.equals(msgReciever)) {
//                                 RecieverEmail = msgSender;
//                             }
//                         }
//
//                         //getting status of user
//                         getStatus();
//
//                         //set onClickListener on imageView to enter text
//                         SendButtonOnClickListener(id, messages);
//                     }
//
//                     @Override
//                     public void onError(Throwable e) {
//                         Snackbar.make(mView, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
//                         adapter = null;
//                         recyclerView.setAdapter(null);
//
//                         textViewServerProblem.setVisibility(View.VISIBLE);
//                         loadingIndicator.setVisibility(View.INVISIBLE);
//
//                         //cancelling timer so that getPeoples is not called after 15 seconds
//                         timer.cancel();
//
//                     }
//                 })
//            );
        }
        else
        {
            //When coming from detail activity
//            disposable.add(
//                    apiService.messagesSR(SenderEmail, reciever)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribeWith(new DisposableSingleObserver<List<Message>>() {
//                                @Override
//                                public void onSuccess(List<Message> messages) {
//                                    loadingIndicator.setVisibility(View.INVISIBLE);
//
//                                    if(messages.size() == 0)
//                                    {
//                                        adapter = null;
//                                        recyclerView.setAdapter(null);
//
//                                        actionBar.setSubtitle("0 Messages");
//                                        textViewNoData.setVisibility(View.VISIBLE);
//
//                                    }
//                                    else
//                                    {
//                                        //Only refresh the adapter if data is changed
//                                        if(itemCount == messages.size())
//                                        {
//                                            return;
//                                        }
//                                        else
//                                        {
//                                            itemCount = messages.size();
//                                        }
//
//                                        Collections.reverse(messages);
//                                        adapter = new Message_Adapter(messages, SenderEmail, new CustomItemClickListener() {
//                                            @Override
//                                            public void onItemClick(View v, int position) {
//
//                                            }
//                                        });
//                                        recyclerView.setAdapter(adapter);
//
//                                        //Setting name and status on actionBar
//                                        String msgReciever = messages.get(0).getMessageReciever();
//                                        String msgSender = messages.get(0).getMessageSender();
//
//                                        if(SenderEmail.equals(msgSender)) {
//                                            RecieverEmail = msgReciever;    //the actual reciever
//                                        }
//                                        else if(SenderEmail.equals(msgReciever)) {
//                                            RecieverEmail = msgSender;
//                                        }
//
//                                    }
//
//                                    //getting status of user
//                                    getStatus();
//
//                                    editTextMessage.setVisibility(View.GONE);
//                                    imageView.setVisibility(View.GONE);
//
//                                    Toast.makeText(ChatActivity.this, "Please go to ChatActivity Tab to send messages!", Toast.LENGTH_LONG).show();
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Snackbar.make(mView, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
//                                    adapter = null;
//                                    recyclerView.setAdapter(null);
//
//                                    textViewServerProblem.setVisibility(View.VISIBLE);
//                                    loadingIndicator.setVisibility(View.INVISIBLE);
//
//                                    //cancelling timer so that getPeoples is not called after 15 seconds
//                                    timer.cancel();
//
//                                }
//                            })
//            );
        }
    }




    private void SendButtonOnClickListener(final int Id, final List<Message> data)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             String message = editTextMessage.getText().toString();
                                             String encodedMessage = null;
                                             try {
                                                 encodedMessage = URLEncoder.encode(message, "UTF-8");
                                             } catch (UnsupportedEncodingException e) {
                                                 e.printStackTrace();
                                             }
                                             if (!message.isEmpty()) {
                                                 loadingIndicator.setVisibility(View.VISIBLE);

                                                 //Sending Message
//                    disposable.add(
//                      apiService.sendMessage(SenderEmail, reciever, encodedMessage, Id)
//                      .subscribeOn(Schedulers.io())
//                      .observeOn(AndroidSchedulers.mainThread())
//                      .subscribeWith(new DisposableCompletableObserver() {
//                          @Override
//                          public void onComplete() {
//                                  Snackbar.make(mView, "Message sent!", Snackbar.LENGTH_SHORT).show();
//                                  loadingIndicator.setVisibility(View.INVISIBLE);
//                          }
//
//                          @Override
//                          public void onError(Throwable e) {
//                              Snackbar.make(mView, "Message not sent, " + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
//                              loadingIndicator.setVisibility(View.INVISIBLE);
//                          }
//                      })
//                    );
//
//
//                    Collections.reverse(data);      //to real form
//                    data.add(new Message(SenderEmail, reciever, message, Id,"Just Now"));
//                    Collections.reverse(data);      //then reverse again
//
//                    TextView textView = findViewById(R.id.textViewNoDataOnDB);
//                    textView.setVisibility(View.GONE);
//
//                    editTextMessage.setText("");
//
//                    adapter = new Message_Adapter(data, SenderEmail, null);
//                    recyclerView.setAdapter(adapter);
//                }
//                else
//                {
//                    Snackbar.make(mView, "Please write message!", Snackbar.LENGTH_SHORT).show();
//                }
//            }
//        });
                                             }
                                         }
                                     });
    }


                                         //Retrofit call
                                         public void getStatus() {
                                         }
                                         //Checking Online Status
//        disposable.add(
//                apiService.onlineStatus(reciever)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableSingleObserver<OnlineStatus>() {
//                            @Override
//                            public void onSuccess(OnlineStatus onlineStatus) {
//                                if(RecieverEmail != null)
//                                    actionBar.setTitle(RecieverEmail);
//
//                                if(onlineStatus.getOnline())
//                                    actionBar.setSubtitle("Online");
//                                else if(!onlineStatus.getOnline())
//                                {
//                                    actionBar.setSubtitle("Offline");
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Snackbar.make(mView, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
//                            }
//                        })
//        );


}
//class ends.