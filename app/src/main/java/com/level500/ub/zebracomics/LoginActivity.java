package com.level500.ub.zebracomics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String finalPic;
    ProgressBar progressBar;
    private static final int RC_SIGN_IN = 123;
    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";

    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        } else {
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setLogo(R.drawable.logo_icon)      // Set logo drawable
                            .setTheme(R.style.GreenTheme)
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                assert user != null;
                final String finalUsername = user.getDisplayName();
                db.collection("users").document(Objects.requireNonNull(mAuth.getUid()))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            Toast.makeText(LoginActivity.this, "Welcome " + finalUsername, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }else{
                           registerUser();
                        }
                    }
                });


            }
            else {
                // Sign in failed
                String messageError;
                if (response == null) {
                    // User pressed back button
                    messageError = "User cancelled action";
                   Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
                    i.putExtra("messageError", messageError);
                    startActivity(i);
                    finish();
                    return;
                }

                if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    messageError = "No internet connection";
                    Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
                    i.putExtra("messageError", messageError);
                    startActivity(i);
                    finish();
                    return;
                }
                messageError = "Unknown error occured";
                Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
                i.putExtra("messageError", messageError);
                startActivity(i);
                finish();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void registerUser(){
        // Successfully signed in
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        String email = user.getEmail();
        String username = user.getDisplayName();
        String phone = user.getPhoneNumber();
        Uri pic = user.getPhotoUrl();
        if(pic == null ){
            finalPic = "https://firebasestorage.googleapis.com/v0/b/zebracomics-4aede.appspot.com/o/profile.png?alt=media&token=b5b14838-13de-4047-93dd-e57f5a395d30";
        }else {
            finalPic = pic.toString();
        }


        //check if values returned by the user is null such as email , password etc
        if(email == null){
            email = "please set an email";
        }
        if(phone == null){
            phone = "please set a phone number";
        }
        if(username == null){
            username = "Zebra user";
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", username);
        userInfo.put("state", "Please set a state");
        userInfo.put("country", "Please set a country");
        userInfo.put("phone", phone);
        userInfo.put("email", email);
        userInfo.put("firstname", username);
        userInfo.put("lastename", username);
        userInfo.put("profile_pic", finalPic);

        db.collection("users").document(Objects.requireNonNull(mAuth.getUid()))
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(!user.isEmailVerified()){
                            user.sendEmailVerification();
                        }
                        Toast.makeText(LoginActivity.this, "Account successfuilly setup" , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull final Exception e) {
                        AuthUI.getInstance()
                                .delete(LoginActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(LoginActivity.this, "Error occured " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });
    }


    public void loginScreen(View view) {

        final EditText password = (EditText) findViewById(R.id.password);
        final EditText email = (EditText) findViewById(R.id.email);

        String strEmail = email.getText().toString().trim();
        String strPassword = password.getText().toString().trim();

        if ((TextUtils.isEmpty(strEmail)) && (TextUtils.isEmpty(strPassword))) {
            Toast.makeText(LoginActivity.this, "Invalid credentials please fill in!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(strEmail)) {
            Toast.makeText(LoginActivity.this, "Please enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(LoginActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isValidEmaillId(email.getText().toString().trim())) {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Login into account...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {

                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Authentication failed." + Objects.requireNonNull(task.getException()).getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
        }


    }
    private boolean isValidEmaillId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }




}
