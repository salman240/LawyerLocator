package com.example.salmanyousaf.lawyerlocator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.Helper.Utils;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Rating;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Signup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.encodeEmail;

public class Detail extends AppCompatActivity implements RatingDialogListener, View.OnClickListener {

    @BindView(R.id.detailView)
    RelativeLayout mView;

    @BindView(R.id.imageViewDetail)
    ImageView imageView;

    @BindView(R.id.tvEmail)
    TextView textViewEmail;

    @BindView(R.id.textViewUsername)
    TextView textViewName;

    @BindView(R.id.textViewLocation)
    TextView textViewLocation;

    @BindView(R.id.textViewCaseType)
    TextView textViewCaseType;

    @BindView(R.id.ratingBarDetail)
    RatingBar ratingBar;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    @BindView(R.id.textViewContactNo)
    TextView textViewContact;

    @BindView(R.id.textViewDateTime)
    TextView textViewDate;

    @BindView(R.id.textViewExperience)
    TextView textViewExperience;

    @BindView(R.id.tvExperience)
    TextView tvExperience;

    @BindView(R.id.buttonCall)
    Button buttonCall;

    @BindView(R.id.buttonMap)
    Button buttonPathFinder;

    @BindView(R.id.buttonMessage)
    Button buttonMessage;

    @BindView(R.id.buttonChat)
    Button buttonChat;

    @BindView(R.id.switchReserve)
    Switch switchReserve;

    @BindView(R.id.loading_indicator_switch)
    ProgressBar loadingIndicatorSwitch;

    @BindView(R.id.waitingTv)
    TextView waitingTextView;

    @BindView(R.id.buttonRating)
    Button buttonRate;

    private String senderEmail;
    private String reciverEmail;
    private Utils utils = new Utils(this);
    private Unbinder unbinder;

    private DatabaseReference ratingDatabaseReference;
    private ValueEventListener getRatingValueEventListener;
    private DatabaseReference reserveDatabaseReference;
    private ValueEventListener getReserveEventValueListener;

    private Signup data;
    private float userRating;
    private float ratingToShow = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        unbinder = ButterKnife.bind(this);

        //Firebase init
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        ratingDatabaseReference = firebaseDatabase.getReference("Rating");
        reserveDatabaseReference = firebaseDatabase.getReference("Reserve");

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //getting intent from listView clicks in Search Activity.
        Intent intent = getIntent();
        String Account = intent.getStringExtra("AccountType");
        data = (Signup) intent.getSerializableExtra("Data");

        reciverEmail = encodeEmail(data.getEmail());
        senderEmail = encodeEmail(Paper.book().read("email").toString());

        if(Account.equals("lawyer"))
        {
            SetProfileImage(data.getProfileImage());
            textViewEmail.setText(data.getEmail());
            textViewName.setText(data.getName());
            textViewLocation.setText(data.getLocation());
            textViewCaseType.setText(data.getCaseType());
            textViewDescription.setText(data.getDescription());
            textViewContact.setText(data.getPhone());
            textViewDate.setText(utils.FormatDateTime(data.getDateTime()));
            tvExperience.setVisibility(View.INVISIBLE);
            textViewExperience.setVisibility(View.INVISIBLE);
            ratingBar.setVisibility(View.INVISIBLE);
        }
        else if(Account.equals("client"))
        {
            SetProfileImage(data.getProfileImage());
            textViewEmail.setText(data.getEmail());
            textViewName.setText(data.getName());
            textViewLocation.setText(data.getLocation());
            textViewDescription.setText(data.getDescription());
            textViewContact.setText(data.getPhone());
            textViewDate.setText(utils.FormatDateTime(data.getDateTime()));
            textViewExperience.setText(data.getExperience());
            getRating();
            textViewCaseType.setVisibility(View.INVISIBLE);
        }

        //apply system call and msg intents on buttons
        buttonCall.setOnClickListener(this);
        buttonMessage.setOnClickListener(this);
        buttonChat.setOnClickListener(this);
        buttonRate.setOnClickListener(this);
        buttonPathFinder.setOnClickListener(this);

        getIsReserve();
    }//onCreate

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonCall)
        {
            Toast.makeText(Detail.this, "Opening calling app!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"  + data.getPhone()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        else if(v.getId() == R.id.buttonMessage)
        {
            Toast.makeText(Detail.this, "Opening messaging app!", Toast.LENGTH_SHORT).show();
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setDataAndType(Uri.parse("sms:" + data.getPhone()), "vnd.android-dir/mms-sms");
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(sendIntent);
        }
        else if(v.getId() == R.id.buttonChat)
        {
            Toast.makeText(Detail.this, "Opening chat app!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Detail.this, Chat.class);
            intent.putExtra("reciever", reciverEmail);
            startActivity(intent);
        }
        else if(v.getId() == R.id.buttonRating)
        {
            showDialog();
        }
        else if(v.getId() == R.id.buttonMap)
        {
            String location = textViewLocation.getText().toString();
            Intent map = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + location));
            startActivity(map);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(getRatingValueEventListener != null) {
            ratingDatabaseReference.removeEventListener(getRatingValueEventListener);
            getRatingValueEventListener = null;
        }

        if(getReserveEventValueListener != null) {
            reserveDatabaseReference.removeEventListener(getReserveEventValueListener);
            getReserveEventValueListener = null;
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }


    //Firebase calls here.
    private void getRating() {
        getRatingValueEventListener = new ValueEventListener() {
             @RequiresApi(api = Build.VERSION_CODES.KITKAT)
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()) {
                     float totalRating = 0;
                     for (DataSnapshot ratingDs : dataSnapshot.getChildren()) {
                         Rating rating = ratingDs.getValue(Rating.class);
                         totalRating += Objects.requireNonNull(rating).getRating();

                         if (rating.getEmail().equals(senderEmail)) {
                             userRating = rating.getRating();
                         }
                     }

                     ratingToShow = totalRating / dataSnapshot.getChildrenCount();
                     ratingBar.setRating(ratingToShow);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 Snackbar.make(mView, "Error : " + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
             }
         };
        ratingDatabaseReference.child(reciverEmail).addValueEventListener(getRatingValueEventListener);
    }

    private void insertRating(final float rating) {
        final Rating addRating = new Rating(rating, senderEmail);

        ratingDatabaseReference.child(reciverEmail).child(senderEmail).setValue(addRating).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(Detail.this, "Rating updated", Toast.LENGTH_LONG, true).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(Detail.this, e.getLocalizedMessage(), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    private void getIsReserve() {
        setOnReserveClickListener();

        getReserveEventValueListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingIndicatorSwitch.setVisibility(View.GONE);
                switchReserve.setVisibility(View.VISIBLE);

                if(dataSnapshot.exists()) {
                    if (Objects.requireNonNull(dataSnapshot.getValue()).equals(true)) {
                        switchReserve.setChecked(true);
                        waitingTextView.setVisibility(View.GONE);
                    } else {
                        waitingTextView.setVisibility(View.VISIBLE);
                        switchReserve.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(mView, "Error : " + databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                loadingIndicatorSwitch.setVisibility(View.GONE);
            }
        };
        reserveDatabaseReference.child(reciverEmail).child(senderEmail).addValueEventListener(getReserveEventValueListener);
    }

    private void reserveUser() {
        reserveDatabaseReference.child(reciverEmail).child(senderEmail).setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(mView, "Waiting for approval!", Snackbar.LENGTH_SHORT).show();
                waitingTextView.setVisibility(View.VISIBLE);
                switchReserve.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(mView, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteReserve() {
        reserveDatabaseReference.child(reciverEmail).child(senderEmail).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(mView, "User is un-reserved Successfully!", Snackbar.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(mView, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    //Helper methods
    private void SetProfileImage(String profileData) {
        Picasso.get().load(profileData).fit().into(imageView);
    }

    private void setOnReserveClickListener() {
        switchReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking the state of switch to make further decision on it.
                final boolean isChecked = switchReserve.isChecked();
                final AlertDialog alertDialog = new AlertDialog.Builder(Detail.this).create();

                alertDialog.setTitle("Reserve");

                if(isChecked) {
                    alertDialog.setMessage("Do you want to reserve this user?");

                    alertDialog.setButton(-1,"Reserve", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                reserveUser(); //Async Call
                        }
                    });
                }
                else{
                    alertDialog.setMessage("Do you want to delete this user from Reserve?");
                    alertDialog.setButton(-1,"Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                deleteReserve();   //Async Call
                            }
                    });
                }//if-else

                alertDialog.setButton(-2,"Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        switchReserve.setChecked(!isChecked);
                    }
                });

                alertDialog.show();
            }
        });
    }

    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setDefaultRating((int) userRating == 0 ? 5 : (int) userRating)
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setTitle("Rate this application")
                .setDescription("Please give your feedback")
                .setCommentInputEnabled(true)
                .setDefaultComment("Type your comment here")
                .setStarColor(R.color.colorPrimary)
                .setNoteDescriptionTextColor(R.color.colorPrimaryDark)
                .setTitleTextColor(R.color.colorPrimaryDark)
                .setDescriptionTextColor(R.color.colorPrimaryDark)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.colorPrimary)
                .setCommentTextColor(R.color.colorPrimaryDark)
                .setCommentBackgroundColor(R.color.f1f1f1)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(Detail.this)
                .show();
    }

    //Rating Dialog
    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        insertRating(i);
    }

}//class ends.
