package com.example.salmanyousaf.lawyerlocator;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.Adapter.ReserveViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.encodeEmail;

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

    private Unbinder unbinder;

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<String, ReserveViewHolder> adapter;

    public FragmentReserve() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        super.onStart();
//        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_reserve, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase init
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Reserve");

        getReserves();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRefresh() {
                adapter.stopListening();
                getReserves();
                adapter.startListening();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReserves();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), LinearLayoutManager.VERTICAL));
    }


    //Firebase call
    public void getReserves() {
        FirebaseRecyclerOptions<String> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(databaseReference.child(encodeEmail(Paper.book().read("email").toString())), String.class).build();
        adapter = new FirebaseRecyclerAdapter<String, ReserveViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public ReserveViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = getLayoutInflater().inflate(R.layout.list_item_reserve, viewGroup, false);
                return new ReserveViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ReserveViewHolder holder, int position, @NonNull String model) {
                loadingIndicator.setVisibility(View.GONE);

                Toast.makeText(getActivity(), model, Toast.LENGTH_SHORT).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Toasty.error(Objects.requireNonNull(getActivity()), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        };
        recyclerView.setAdapter(adapter);
    }

}//class ends
