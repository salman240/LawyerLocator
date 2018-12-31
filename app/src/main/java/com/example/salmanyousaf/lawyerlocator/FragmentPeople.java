package com.example.salmanyousaf.lawyerlocator;

import android.content.Intent;
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
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.Adapter.PeopleViewHolder;
import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Rating;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Signup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.decodeEmail;
import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.encodeEmail;
import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.getUsers;

public class FragmentPeople extends Fragment{

    @BindView(R.id.mPeopleView)
    RelativeLayout mView;

    @BindView(R.id.swiperefreshPeople)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.textViewNoData)
    TextView textViewNoData;

    @BindView(R.id.textViewNoDataDetails)
    TextView textViewNoDataDetails;

    @BindView(R.id.textViewNoDataOnDB)
    TextView textViewNoDataDb;

    @BindView(R.id.people)
    RecyclerView recyclerView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.buttonRefresh)
    Button button;

    private FirebaseRecyclerAdapter<Signup, PeopleViewHolder> adapter;
    private Unbinder unbinder;
    private DatabaseReference userDatabaseReference;
    private String accountType;
    private ValueEventListener getRatingValueEventListener;
    private DatabaseReference ratingDatabaseReference;


    public FragmentPeople() { }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Paper.init(Objects.requireNonNull(getActivity()));
        accountType = Paper.book().read("accountType");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
        if(getRatingValueEventListener != null)
        {
            ratingDatabaseReference.removeEventListener(getRatingValueEventListener);
            getRatingValueEventListener = null;
        }
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase init
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = firebaseDatabase.getReference("User");
        ratingDatabaseReference = firebaseDatabase.getReference("Rating");

        //applying firebase recyclerview adapter to read data
        getPeoples();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.stopListening();
                getPeoples();
                adapter.startListening();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPeoples();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        Animation recycleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition);
        recyclerView.setAnimation(recycleAnimation);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem item = menu.findItem(R.id.search_bar);

        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }


            @Override
            public boolean onQueryTextChange(String query) {

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    //Helper method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getPeoples() {
        if (accountType == null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }

        //firebase adapter
        FirebaseRecyclerOptions<Signup> options = new FirebaseRecyclerOptions.Builder<Signup>()
                .setQuery(userDatabaseReference.orderByChild("accountType").equalTo(getUsers(Paper.book().read("accountType").toString()))
                        , Signup.class).build();
        adapter = new FirebaseRecyclerAdapter<Signup, PeopleViewHolder>(options) {
            @NonNull
            @Override
            public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_people, viewGroup, false);
                return new PeopleViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final PeopleViewHolder holder, int position, @NonNull final Signup model) {
                loadingIndicator.setVisibility(View.GONE);

                TextView name = holder.itemView.findViewById(R.id.name_text_view);
                TextView location = holder.itemView.findViewById(R.id.location_text_view);
                ImageView image = holder.itemView.findViewById(R.id.image_view);
                TextView caseType = holder.itemView.findViewById(R.id.caseType_text_view);
                final RatingBar rating = holder.itemView.findViewById(R.id.ratingBar);

                name.setText(model.getName());
                location.setText(model.getLocation());
                Picasso.get().load(model.getProfileImage()).placeholder(R.drawable.placeholder_image).error(R.drawable.no_image)
                        .fit().into(image);

                //clickListener
                holder.setItemClickListener(new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(getActivity(), Detail.class);
                        model.setEmail(decodeEmail(Objects.requireNonNull(adapter.getRef(position).getKey())));
                        intent.putExtra("Data", model);
                        intent.putExtra("AccountType", accountType);
                        startActivity(intent);
                    }
                });

                if (accountType.equals("lawyer"))
                {
                    caseType.setText(model.getCaseType());
                    rating.setVisibility(View.GONE);    //diabling RatingBar
                }
                else if (accountType.equals("client"))
                {
                    getRatingValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            float totalRating = 0;
                            for(DataSnapshot ratingDs : dataSnapshot.getChildren())
                            {
                                Rating rating = ratingDs.getValue(Rating.class);
                                totalRating += Objects.requireNonNull(rating).getRating();
                            }

                            totalRating = totalRating/dataSnapshot.getChildrenCount();
                            rating.setRating(totalRating);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toasty.error(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG, true).show();
                        }
                    };

                    ratingDatabaseReference.child(encodeEmail(Objects.requireNonNull(adapter.getRef(position).getKey())))
                            .addValueEventListener(getRatingValueEventListener);

                    caseType.setVisibility(View.GONE);  //disabling visibility of case_type
                }
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                super.onError(error);
                Toasty.error(getActivity(), error.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        };
        recyclerView.setAdapter(adapter);
    }

}//class ends.