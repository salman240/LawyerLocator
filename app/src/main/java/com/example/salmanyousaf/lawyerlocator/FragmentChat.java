package com.example.salmanyousaf.lawyerlocator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Adapter.Chat_Adapter;
import com.example.salmanyousaf.lawyerlocator.Helper.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.SENDEREMAIL;


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

    @BindView(R.id.buttonRefresh)
    Button button;


    Chat_Adapter adapter;
    private Utils utils;
    private String SenderEmail;

    private Unbinder unbinder;


    public FragmentChat() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        utils = new Utils(getActivity());

    }


    @Override
    public void onStart() {
        super.onStart();
        loadingIndicator.setVisibility(View.VISIBLE);
        AsyncCall();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = utils.GetLoginSharedPreferences();
        SenderEmail = sharedPreferences.getString(SENDEREMAIL, null);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncCall();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        super.onActivityCreated(savedInstanceState);
    }




    //Helper methods (Async call maker function)
    public void AsyncCall() {
        loadingIndicator.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        textViewNoDataDetails.setVisibility(View.GONE);
        textViewNoDataDb.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
//
//        if (SenderEmail == null) {
//            getActivity().finish();
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            startActivity(intent);
//        }
//
//
//                            @Override
//                            public void onSuccess(final List<com.example.salmanyousaf.lawyerlocator.Model.Chat> chats) {
//                                loadingIndicator.setVisibility(View.GONE);
//
//                                if (chats.size() > 0) {
//                                    adapter = new Chat_Adapter(chats, SenderEmail, new CustomItemClickListener() {
//                                        @Override
//                                        public void onItemClick(View v, int position) {
//                                            int id =  chats.get(position).getChatId();
//                                            Intent intent = new Intent(getActivity(), Chat.class);
//                                            intent.putExtra("Data", id);
//
//                                            if(chats.get(position).getChatSender().equals(SenderEmail))
//                                            {
//                                                intent.putExtra("reciever", chats.get(position).getChatReciever());
//
//                                            }
//                                            else if(chats.get(position).getChatReciever().equals(SenderEmail))
//                                            {
//                                                intent.putExtra("reciever", chats.get(position).getChatSender());
//                                            }
//
//                                            startActivity(intent);
//                                        }
//                                    });
//                                    recyclerView.setAdapter(adapter);
//                                }
//                                else
//                                {
//                                    adapter = null;
//                                    recyclerView.setAdapter(null);
//                                    textViewNoDataDb.setVisibility(View.VISIBLE);
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                loadingIndicator.setVisibility(View.GONE);
//
//                                Snackbar.make(mView, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
//
//                                adapter = null;
//                                recyclerView.setAdapter(null);
//
//                                textViewNoData.setVisibility(View.VISIBLE);
//                                textViewNoDataDetails.setVisibility(View.VISIBLE);
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        button.setVisibility(View.VISIBLE);
//                                    }
//                                }, 2000);
//                            }
//                    })
//        );

    }


}//class ends
