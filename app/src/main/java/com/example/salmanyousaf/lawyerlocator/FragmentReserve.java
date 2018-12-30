package com.example.salmanyousaf.lawyerlocator;

        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v4.view.MenuItemCompat;
        import android.support.v4.widget.SwipeRefreshLayout;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.DividerItemDecoration;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ProgressBar;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import com.example.salmanyousaf.lawyerlocator.Helper.Utils;
        import com.example.salmanyousaf.lawyerlocator.Adapter.Reserve_Adapter;

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.Unbinder;

        import static com.example.salmanyousaf.lawyerlocator.Contracts.Contracts.SENDEREMAIL;


public class FragmentReserve extends Fragment {


    @BindView(R.id.mReserveView)
    RelativeLayout mView;

    @BindView(R.id.swiperefreshReserve)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.textViewNoData)
    TextView textViewNoData;

    @BindView(R.id.textViewNoDataDetails)
    TextView textViewNoDataDetails;

    @BindView(R.id.textViewNoDataOnDB)
    TextView textViewNoDataDb;

    @BindView(R.id.reserve)
    RecyclerView recyclerView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.buttonRefresh)
    Button button;


    Reserve_Adapter adapter;
    private Utils utils;
    private String SenderEmail = "";
    private int Count;
    private TextView buttonCount;

    private Unbinder unbinder;


    public FragmentReserve() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_reserve, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                AsyncCall();
                swipeRefreshLayout.setRefreshing(false);
                buttonCount.setText(Count + "");
            }
        });
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncCall();
            }
        });

        SharedPreferences sharedPreferences = utils.GetLoginSharedPreferences();
        SenderEmail = sharedPreferences.getString(SENDEREMAIL, null);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        super.onActivityCreated(savedInstanceState);
    }


    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem item = menu.findItem(R.id.search_bar);

        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    */


    @SuppressLint("SetTextI18n")
    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.reserve, menu);

        MenuItem item = menu.findItem(R.id.notification);
        MenuItemCompat.setActionView(item, R.layout.notification_menu_layout);
        RelativeLayout notificationCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        buttonCount = notificationCount.findViewById(R.id.notification_count);
        buttonCount.setText(Count + "");
        buttonCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                intent.putExtra("count", Count);
                startActivity(intent);
            }
        });
    }




    //Helper methods
    //Async call maker function
    public void AsyncCall() {
        loadingIndicator.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.GONE);
        textViewNoDataDetails.setVisibility(View.GONE);
        textViewNoDataDb.setVisibility(View.GONE);
        button.setVisibility(View.GONE);


//        if (SenderEmail == null) {
//            getActivity().finish();
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            startActivity(intent);
//        } else {
//                                @SuppressLint("SetTextI18n")
//                                @Override
//                                public void onSuccess(final List<Reserve> reserves) {
//                                    loadingIndicator.setVisibility(View.GONE);
//
//                                    Count = 0;
//
//                                    if (reserves.size() > 0) {
//                                        //removing the reserve items that the user has to approve or deny in notifications.
//                                        for (int pos = 0; pos < reserves.size(); pos++) {
//                                            if (!reserves.get(pos).getIsReserve() && !reserves.get(pos).getResSender().equals(SenderEmail)) {
//                                                //reserves.remove(pos);
//                                                Count++;
//                                            }
//                                        }
//
//                                        try {
//                                            buttonCount.setText(Count + "");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        adapter = new Reserve_Adapter(reserves, SenderEmail, new CustomItemClickListener() {
//                                            @Override
//                                            public void onItemClick(View v, int position) {
//                                                observeUserData( reserves.get(position).getResId(),
//                                                          reserves.get(position).getResSender(), reserves.get(position).getResReciever());
//                                            }
//                                        });
//                                        recyclerView.setAdapter(adapter);
//                                    } else {
//                                        //clearing the recyclerView in case of empty reserves
//                                        adapter = null;
//                                        recyclerView.setAdapter(null);
//                                        textViewNoDataDb.setVisibility(View.VISIBLE);
//                                    }
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    loadingIndicator.setVisibility(View.GONE);
//
//                                    Snackbar.make(mView, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
//
//                                    adapter = null;
//                                    recyclerView.setAdapter(null);
//
//                                    textViewNoData.setVisibility(View.VISIBLE);
//                                    textViewNoDataDetails.setVisibility(View.VISIBLE);
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            button.setVisibility(View.VISIBLE);
//                                        }
//                                    }, 2000);
//                                }
//                            })
//            );
//        }
    }



//    private void observeUserData(int id, String sender, String reciever) {
//        loadingIndicator.setVisibility(View.VISIBLE);
//
//                            @Override
//                            public void onSuccess(User user) {
//                                loadingIndicator.setVisibility(View.GONE);
//
//                                Intent intent = new Intent(getActivity(), Detail.class);
//                                intent.putExtra("Data", user);
//
//                                if (user.getCaseType() == null || user.getCaseType().equals("null")) {
//                                    intent.putExtra("AccountType", "client");
//                                } else {
//                                    intent.putExtra("AccountType", "lawyer");
//                                }
//                                startActivity(intent);
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                loadingIndicator.setVisibility(View.GONE);
//
//                                Snackbar.make(mView, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
//                            }
//                        })
//        );
//    }


}//class ends
