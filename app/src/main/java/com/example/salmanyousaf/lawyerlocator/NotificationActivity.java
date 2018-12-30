package com.example.salmanyousaf.lawyerlocator;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Adapter.Notification_Adapter;
import com.example.salmanyousaf.lawyerlocator.Helper.Utils;
import com.example.salmanyousaf.lawyerlocator.Model.Reserve;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.SENDEREMAIL;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.mNotificationView)
    RelativeLayout mView;

    @BindView(R.id.swiperefreshNotification)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.textViewNoData)
    TextView textViewNoData;

    @BindView(R.id.textViewNoDataDetails)
    TextView textViewNoDataDetails;

    @BindView(R.id.textViewNoDataOnDB)
    TextView textViewNoDataDb;

    @BindView(R.id.noticationRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    private String SenderEmail;
    private Notification_Adapter adapter;
    private Utils utils= new Utils(this);

    int reservesSize;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        unbinder = ButterKnife.bind(this);


        SharedPreferences sharedPreferences = utils.GetLoginSharedPreferences();
        SenderEmail = sharedPreferences.getString(SENDEREMAIL, null);

        loadingIndicator.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        AsyncCall();
    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    private void AsyncCall() {

        textViewNoData.setVisibility(View.GONE);
        textViewNoDataDetails.setVisibility(View.GONE);
        textViewNoDataDb.setVisibility(View.GONE);

//        disposable.add(
//                apiService.getNotificationList(SenderEmail)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableSingleObserver<List<Reserve>>() {
//
//                            @Override
//                            public void onSuccess(List<Reserve> reserves) {
//                                loadingIndicator.setVisibility(View.GONE);
//
//                                if(reserves.size() > 0) {
//                                    /*only show items that the user has to approve or deny.
//                                     Important ... Eg.You have to remove items in descending order beacuse if you remove 0th item then 1th item will replace it and we will
//                                     fail to delete item 1. We have to firstly remove 1th item then 0th item.*/
//                                    reservesSize = reserves.size();
//                                    int pos = reservesSize - 1;
//                                    for(; pos >= 0; pos--)
//                                    {
//                                        if(reserves.get(pos).getIsReserve() || reserves.get(pos).getResSender().equals(SenderEmail))
//                                        {
//                                                reserves.remove(pos);
//                                        }
//                                    }
//
//                                    //After removing items checking that if items>0 then show them otherwise display no notifications message
//                                    if(reserves.size() > 0) {
//                                        adapter = new Notification_Adapter(reserves, SenderEmail, NotificationActivity.this);
//                                        recyclerView.setAdapter(adapter);
//                                    }
//                                    else
//                                    {
//                                        textViewNoData.setVisibility(View.VISIBLE);
//                                    }
//
//                                }
//                                else{
//                                    //clearing the recyclerView in case of empty reserves
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
//
//                            }
//                        })
//
//        );
    }


}//class ends



