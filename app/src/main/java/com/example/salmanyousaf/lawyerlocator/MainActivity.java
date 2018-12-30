package com.example.salmanyousaf.lawyerlocator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.Helper.Utils;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Signup;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.encodeEmail;
import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.isEmailValid;
import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.isPasswordValid;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    @BindView(R.id.mainRoot)
    LinearLayout mView;

    @BindView(R.id.loading_indicator)
    SpinKitView loadingIndicator;

    @BindView(R.id.rememberMe)
    com.rey.material.widget.CheckBox rememberMe;

    private boolean mEditTextChanged;
//    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;
    private Utils utils = new Utils(this);

    private boolean isEmailTouched = true;
    private boolean isPasswordTouched = true;

    private DatabaseReference userDatabaseReference;
    private ValueEventListener valueEventListener;
    private boolean isSignInSuccessfull;
    private Signup signedUser;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        Paper.init(this);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userDatabaseReference = firebaseDatabase.getReference("User");

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.login);

        editTextEmail.setOnTouchListener(mTouchListener);
        editTextPassword.setOnTouchListener(mTouchListener);

        buttonLogin.setOnClickListener(this);

        //if redirecting from Sign-Up the Auto-Load email and password
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        if(email != null && !email.equals(""))
        {
            editTextEmail.setText(email);
            editTextPassword.setText(password);
            isEmailTouched = true;
            isPasswordTouched = true;
        }

        //OnFocusChangeListener @Email
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b && editTextEmail.getText().toString().equals(""))
                {
                    editTextEmail.setError("Email is required");
                }
                isEmailTouched = true;
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b && editTextPassword.getText().toString().equals(""))
                {
                    editTextPassword.setError("Password is required");
                }
                isPasswordTouched = true;
            }
        });


        //Setting Text Watchers
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTextEmail.getText().toString().equals(""))
                {
                    editTextEmail.setError("Email address is required");
                }
                else if(!isEmailValid(editTextEmail.getText().toString())) {
                    editTextEmail.setError("Email address is invalid");
                }
                else
                {
                    editTextEmail.setError(null);
                }
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTextPassword.getText().toString().equals(""))
                {
                    editTextPassword.setError("Password is required");
                }
                else if(!isPasswordValid(editTextPassword.getText().toString())) {
                    editTextPassword.setError("Password must contain 8 or more characters");
                }
                else
                {
                    editTextPassword.setError(null);
                }
            }
        });


    }//onCreate ends here

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mEditTextChanged = true;
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        if (isEmailTouched && editTextEmail.getError() == null && isPasswordTouched && editTextPassword.getError() == null )
        {
            buttonLogin.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);

            userDatabaseReference.child(encodeEmail(editTextEmail.getText().toString())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadingIndicator.setVisibility(View.GONE);

                    if (dataSnapshot.exists()) {
                        Signup signedUser = dataSnapshot.getValue(Signup.class);

                        if(signedUser.getPassword().equals(editTextPassword.getText().toString())) {
                            Toasty.success(MainActivity.this, "Sign in Successfull", Toast.LENGTH_SHORT, true).show();

                            signedUser.setEmail(editTextEmail.getText().toString());
                            Paper.book().write("email", editTextEmail.getText().toString());
                            Paper.book().write("user", signedUser);
                            Paper.book().write("accountType", signedUser.getAccountType());

                            if (rememberMe.isChecked()) {
                                Paper.book().write("rememberMe", true);
                            } else {
                                Paper.book().write("rememberMe", false);
                            }

                            Intent intent = new Intent(MainActivity.this, DataActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toasty.warning(MainActivity.this, "No such user exists", Toast.LENGTH_SHORT, true).show();
                        buttonLogin.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    loadingIndicator.setVisibility(View.GONE);
                    buttonLogin.setVisibility(View.VISIBLE);
                    Toasty.error(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG, true).show();
                }
            });
        }
        else
        {
            Snackbar.make(mView, "Please provide required details", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.setting)
        {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
        else if(id == R.id.aboutUs)
        {
            utils.About();
        }
        else if (id == R.id.quit)
        {
            utils.Quit();
        }
        else if (id == R.id.register)
        {
            utils.Register();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!mEditTextChanged)
        {
            utils.Quit();
        }
        else
        {
            utils.ChangesUnsaved();
        }
        //super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(valueEventListener != null)
        userDatabaseReference.removeEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

}//class ends.
