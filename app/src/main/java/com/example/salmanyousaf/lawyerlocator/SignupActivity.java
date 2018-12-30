package com.example.salmanyousaf.lawyerlocator;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salmanyousaf.lawyerlocator.Helper.Utils;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Signup;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.joda.time.DateTime;

import java.util.Objects;
import java.util.UUID;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.encodeEmail;
import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.isEmailValid;
import static com.example.salmanyousaf.lawyerlocator.Helper.Utils.isPasswordValid;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @BindView(R.id.editTextName)
    EditText editTextName;

    @BindView(R.id.editTextPhoneNo)
    EditText editTextPhone;

    @BindView(R.id.editTextCaseType)
    EditText editTextCaseType;

    @BindView(R.id.editTextExperience)
    EditText editTextExperience;

    @BindView(R.id.editTextDescription)
    EditText editTextDescription;

    @BindView(R.id.editTextCost)
    EditText editTextFee;

    @BindView(R.id.buttonMap)
    Button buttonLocation;

    @BindView(R.id.spinnerGender)
    Spinner spinnerGender;

    @BindView(R.id.buttonSignup)
    Button buttonSignup;

    @BindView(R.id.buttonCheckEmail)
    Button buttonCheckEmail;

    @BindView(R.id.buttonProfile)
    Button buttonProfile;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.signupRoot)
    ScrollView mView;

    @BindView(R.id.loading_indicator_button)
    SpinKitView loadingIndicatorButton;

    @BindView(R.id.loading_indicator_email)
    ProgressBar loadingIndicatorEmail;

    @BindView(R.id.loading_indicator_button_email)
    SpinKitView loadingIndicatorCheckEmail;

    @BindView(R.id.textViewLocation)
    TextView tvLocation;

    @BindView(R.id.textViewGender)
    TextView tvGender;

    @BindView(R.id.textViewProfile)
    TextView tvProfile;

    @BindView(R.id.passwordTogggle)
    TextInputLayout textInputLayoutPassword;

    @BindColor(R.color.redNight)
    int redNight;

    @BindColor(R.color.colorPrimary)
    int colorPrimary;

    private boolean mEditTextTouched;

    private String accountType;
    private String address = "";
    private Utils utils = new Utils(this);
    private Unbinder unbinder;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    //flags for touched edittexts
    Boolean isEmailTouched = false;
    Boolean isPasswordTouched = false;
    Boolean isNameTouched = false;
    Boolean isPhoneTouched = false;
    Boolean isCaseTouched = false;
    Boolean isExpTouched = false;
    Boolean isFeeTouched = false;
    Boolean isDescTouched = false;
    private TextView textViewLocation;

    private Uri profileImageUri;
    private ProgressDialog progressDialog;
    private String gender;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        unbinder = ButterKnife.bind(this);

        //firebase init
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");

        //progessDialog init
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);

        //checking accountType
        if(getIntent().getStringExtra("accountType") != null)
        {
            accountType = getIntent().getStringExtra("accountType");
            if(accountType.equals("client"))
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.clientSignup);
                editTextExperience.setVisibility(View.GONE);
                editTextCaseType.setOnFocusChangeListener(onFocusChangeListener);
                editTextCaseType.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(editTextCaseType.getText().toString().equals(""))
                        {
                            editTextCaseType.setError("Case type is required");
                        }
                        else
                        {
                            editTextCaseType.setError(null);
                        }
                    }
                });

            }
            else if(accountType.equals("lawyer"))
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.lawyerSignup);
                editTextCaseType.setVisibility(View.GONE);
                editTextExperience.setOnFocusChangeListener(onFocusChangeListener);
                editTextExperience.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(editTextExperience.getText().toString().equals(""))
                        {
                            editTextExperience.setError("Experience is required");
                        }
                        else
                        {
                            editTextExperience.setError(null);
                        }
                    }
                });
            }
        }


        //Applying onFocus listener
        editTextEmail.setOnFocusChangeListener(onFocusChangeListener);
        editTextPassword.setOnFocusChangeListener(onFocusChangeListener);
        editTextName.setOnFocusChangeListener(onFocusChangeListener);
        editTextPhone.setOnFocusChangeListener(onFocusChangeListener);
        editTextDescription.setOnFocusChangeListener(onFocusChangeListener);
        editTextFee.setOnFocusChangeListener(onFocusChangeListener);

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

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTextName.getText().toString().equals(""))
                {
                    editTextName.setError("Name is required");
                }
                else
                {
                    editTextName.setError(null);
                }
            }
        });

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTextPhone.getText().toString().equals(""))
                {
                    editTextPhone.setError("Phone number is required");
                }
                else
                {
                    editTextPhone.setError(null);
                }
            }
        });

        editTextFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTextFee.getText().toString().equals(""))
                {
                    editTextFee.setError("Fee is required");
                }
                else
                {
                    editTextFee.setError(null);
                }
            }
        });

        editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editTextDescription.getText().toString().equals(""))
                {
                    editTextDescription.setError("Description is required");
                }
                else
                {
                    editTextDescription.setError(null);
                }
            }
        });

        buttonSignup.setOnClickListener(this);
        buttonCheckEmail.setOnClickListener(this);

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.SelectImage();
            }
        });

        hideEditTexts();

    }//oncreate


    //Edit Text's focus listener
    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b && view.getId() == R.id.editTextEmail) {
                if (editTextEmail.getText().toString().equals("")) {
                    editTextEmail.setError("Email is required");
                }
                isEmailTouched = true;
            }
            else if (b && view.getId() == R.id.editTextPassword) {
                if (editTextPassword.getText().toString().equals("")) {
                    editTextPassword.setError("Password is required");
                }
                isPasswordTouched = true;
            } else if (b && view.getId() == R.id.editTextName) {
                if (editTextName.getText().toString().equals("")) {
                    editTextName.setError("Name is required");
                }
                isNameTouched = true;
            } else if (b && view.getId() == R.id.editTextPhoneNo) {
                if (editTextPhone.getText().toString().equals("")) {
                    editTextPhone.setError("Phone number is required");
                }
                isPhoneTouched = true;
            } else if (b && view.getId() == R.id.editTextCost) {
                if (editTextFee.getText().toString().equals("")) {
                    editTextFee.setError("Fee is required");
                }
                isFeeTouched = true;
            }
            else if (b && view.getId() == R.id.editTextDescription) {
                if (editTextDescription.getText().toString().equals("")) {
                    editTextDescription.setError("Description is required");
                }
                isDescTouched = true;
            }
            else if (b && view.getId() == R.id.editTextCaseType) {
                if (editTextCaseType.getText().toString().equals("")) {
                    editTextCaseType.setError("Case type is required");
                }
                isCaseTouched = true;
            }
            else if (b && view.getId() == R.id.editTextExperience) {
                if (editTextExperience.getText().toString().equals("")) {
                    editTextExperience.setError("Experience is required");
                }
                isExpTouched = true;
            }
            mEditTextTouched = true;
        }
    };


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonSignup) {
            //checking internet connection
            if (utils.isConnectedToInternet()) {
                gender = spinnerGender.getSelectedItem().toString();

                if (editTextEmail.getError() == null && isEmailTouched &&
                        editTextPassword.getError() == null && isPasswordTouched &&
                        editTextName.getError() == null && isNameTouched &&
                        editTextPhone.getError() == null && isPhoneTouched &&
                        editTextDescription.getError() == null && isDescTouched &&
                        editTextFee.getError() == null && isFeeTouched
                        && !address.equals("") && !gender.equals("") && profileImageUri != null) {
                    if (accountType.equals("client")) {
                        if (editTextCaseType.getError() == null && isCaseTouched) {
                            storeImageAndUser();
                        }
                    } else if (accountType.equals("lawyer")) {
                        if (editTextExperience.getError() == null && isExpTouched) {
                            storeImageAndUser();
                        }
                    }
                } else {
                    Toasty.error(this, "Please provide all required fields!", Toast.LENGTH_LONG, true).show();
                }
            } else {
                Snackbar.make(mView, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
            }
        }
        else if(view.getId() == R.id.buttonCheckEmail) {    //checking email avl
            if (isEmailTouched && editTextEmail.getError() == null) {
                buttonCheckEmail.setVisibility(View.GONE);
                loadingIndicatorCheckEmail.setVisibility(View.VISIBLE);

                databaseReference.child(encodeEmail(editTextEmail.getText().toString())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        loadingIndicatorCheckEmail.setVisibility(View.GONE);

                        if(dataSnapshot.exists())
                        {
                            hideEditTexts();
                            Toasty.error(SignupActivity.this, "This email is already in use, Please choose a different one.", Toast.LENGTH_LONG, true).show();
                            editTextEmail.requestFocus();
                            buttonCheckEmail.setVisibility(View.VISIBLE);
                        } else {
                            showEditTexts();
                            buttonCheckEmail.setVisibility(View.GONE);
                            editTextPassword.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toasty.error(SignupActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                });
            } else {
                Toasty.error(SignupActivity.this, "Please provide required field", Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            int SELECT_FILE = 0, Map_Address = 1;
            if(requestCode == SELECT_FILE)
            {
                profileImageUri = data.getData();
                imageView.setImageURI(profileImageUri);
                imageView.setVisibility(View.VISIBLE);
            }
            else if(requestCode == Map_Address)
            {
                address = data.getStringExtra("address");
                buttonLocation.setVisibility(View.INVISIBLE);
                textViewLocation = findViewById(R.id.tvMap);
                textViewLocation.setVisibility(View.VISIBLE);
                textViewLocation.setText(address);
            }
            else
            {
                Snackbar.make(mView, "Data format not correct!", Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            Snackbar.make(mView, "Operation cancelled by You!", Snackbar.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
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
        else if (id == R.id.aboutUs)
        {
            utils.About();
        }
        else if (id== R.id.login)
        {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mEditTextTouched)
        {
            utils.ChangesUnsaved();
        }
        else
        {
            utils.Quit();
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    //Helper methods    ...     store image in firebase storage
    private void storeImageAndUser(){
        buttonSignup.setVisibility(View.GONE);
        loadingIndicatorButton.setVisibility(View.VISIBLE);
        progressDialog.show();

        //storing image
        final String imageName = UUID.randomUUID().toString();
        final StorageReference imageStorageReference =  storageReference.child(imageName);
        imageStorageReference.putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.setMessage("Image Uploaded...");

                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressDialog.setMessage("Creating Account...");
                        storeUser(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.warning(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG, true).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int) (100.0 * (taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploading : " + progress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.warning(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG, true).show();
                progressDialog.dismiss();
            }
        });
    }

    //Store user data in firebase database
    public void storeUser(Uri uri) {
        Signup signup = new Signup(editTextPassword.getText().toString(),
                editTextName.getText().toString(),
                editTextPhone.getText().toString(),
                accountType.equals("client") ? editTextCaseType.getText().toString() : "Not applicable",
                editTextFee.getText().toString(),
                editTextDescription.getText().toString(),
                accountType.equals("lawyer") ? editTextExperience.getText().toString().concat(" years") : "Not applicable",
                textViewLocation.getText().toString(),
                gender,
                uri.toString(),
                "false",
                accountType,
                DateTime.now().toString());

        databaseReference.child(encodeEmail(editTextEmail.getText().toString())).setValue(signup).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(SignupActivity.this, "Account created successfully", Toast.LENGTH_SHORT, true).show();
                progressDialog.dismiss();

                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                intent.putExtra("email", editTextEmail.getText().toString());
                intent.putExtra("password", editTextPassword.getText().toString());
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG, true).show();
                progressDialog.dismiss();
            }
        });
    }

    public void Map(View view) {
        Intent intent = new Intent(SignupActivity.this, MapsActivity.class);
        startActivityForResult(intent, 1);
    }

    private void hideEditTexts() {
        textInputLayoutPassword.setVisibility(View.GONE);
        editTextName.setVisibility(View.GONE);
        editTextPhone.setVisibility(View.GONE);
        editTextFee.setVisibility(View.GONE);
        editTextDescription.setVisibility(View.GONE);
        buttonLocation.setVisibility(View.GONE);
        spinnerGender.setVisibility(View.GONE);
        buttonProfile.setVisibility(View.GONE);
        buttonSignup.setVisibility(View.GONE);
        tvGender.setVisibility(View.GONE);
        tvLocation.setVisibility(View.GONE);
        tvProfile.setVisibility(View.GONE);

        if(accountType.equals("client"))
        {
            editTextCaseType.setVisibility(View.GONE);
        }
        else if(accountType.equals("lawyer"))
        {
            editTextExperience.setVisibility(View.GONE);
        }
    }

    private void showEditTexts() {
        textInputLayoutPassword.setVisibility(View.VISIBLE);
        editTextPassword.setVisibility(View.VISIBLE);
        editTextName.setVisibility(View.VISIBLE);
        editTextPhone.setVisibility(View.VISIBLE);
        editTextCaseType.setVisibility(View.VISIBLE);
        editTextFee.setVisibility(View.VISIBLE);
        editTextDescription.setVisibility(View.VISIBLE);
        buttonLocation.setVisibility(View.VISIBLE);
        spinnerGender.setVisibility(View.VISIBLE);
        buttonProfile.setVisibility(View.VISIBLE);
        buttonSignup.setVisibility(View.VISIBLE);
        tvGender.setVisibility(View.VISIBLE);
        tvLocation.setVisibility(View.VISIBLE);
        tvProfile.setVisibility(View.VISIBLE);

        if(accountType.equals("client"))
        {
            editTextCaseType.setVisibility(View.VISIBLE);
        }
        else if(accountType.equals("lawyer"))
        {
            editTextExperience.setVisibility(View.VISIBLE);
        }
    }

}//class ends

