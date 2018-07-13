package com.example.asif2.signin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.util.Objects;


public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private ImageView i1,i2,i3,i4,i5;
    private TextView t1,t2,t3;
    private EditText editTextMob, editTextEmail, editTextPass;
    private Button btnsign;
    private Toolbar toolbar;
    private GoogleApiClient googleApiClient;
    private  static  int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);



        i1= findViewById(R.id.imageGoogle);
        i2= findViewById(R.id.imageLinkedin);
        i3 = findViewById(R.id.imageView4);
        i4 = findViewById(R.id.imageView);
        i5 = findViewById(R.id.imageView5);

        t1 = findViewById(R.id.textGoogle);
        t2 = findViewById(R.id.textLinkedin);
        t3 = findViewById(R.id.loginheretext);

        editTextMob = findViewById(R.id.editTextMob);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPassword);

        btnsign = findViewById(R.id.buttonsign);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();

        computeHash();

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t3.getText().toString().equalsIgnoreCase("Login here"))
                showLoginpage();
            }


        });

        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEdittextdata();

            }
        });

        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkedinSignIn();
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkedinSignIn();
            }
        });

    }

    private void computeHash() {

        try {

            PackageInfo info =  getPackageManager().getPackageInfo("com.example.asif2.signin", PackageManager.GET_SIGNATURES);

            for(Signature signature : info.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("Keyhash :", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (Exception e){

        }
    }

    private void linkedinSignIn() {


    }

    private void googleSignIn() {
        Toast.makeText(this, "Signing In", Toast.LENGTH_SHORT).show();
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }

    }

    private void handleResult(GoogleSignInResult googleSignInResult) {

             if(googleSignInResult.isSuccess()){

                 GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                 String name = null;
                 if (account != null) {
                     name = account.getDisplayName();
                 }

                 String email = account.getEmail();
                 String fam_asif = account.getFamilyName();
                 String int_id = account.getId();
                 String gad = account.getGivenName();
                 String important = account.getIdToken();
                 editTextEmail.setText(important);

             }

    }

    private void getEdittextdata() {


        String email = editTextEmail.getText().toString();

        if(Objects.equals(editTextMob.getText().toString(), "") && Objects.equals(editTextPass.getText().toString(), "")){

            Toast.makeText(this, "Please fill all required field", Toast.LENGTH_SHORT).show();

        }
        else {

              if(Objects.equals(editTextMob.getText().toString(), "")){

                  Toast.makeText(this, "Enter Mob No", Toast.LENGTH_SHORT).show();
              }
              else if(Objects.equals(editTextPass.getText().toString(), "")){

                  Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
              }
              else {
                  String mob = editTextMob.getText().toString();
                  String password = editTextPass.getText().toString();
              }
        }


    }


    private void showLoginpage() {

        Animation animation = AnimationUtils.loadAnimation(SignIn.this, R.anim.left_move);
        i3.startAnimation(animation);
        editTextEmail.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                i3.setVisibility(View.GONE);
                editTextEmail.setVisibility(View.GONE);
                btnsign.setText(R.string.log_in);
                t3.setText(R.string.forget_pass);
                toolbar.setTitle("Log In");

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(42, 42);
                params.topMargin= 55;
                params.addRule(RelativeLayout.BELOW, i4.getId());
                params.addRule(RelativeLayout.ALIGN_START, i4.getId());
                i5.setLayoutParams(params);


                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 55);
                params2.topMargin= 130;
                params2.addRule(RelativeLayout.BELOW, editTextPass.getId());
                params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params2.addRule(RelativeLayout.ALIGN_START, i5.getId());
                params2.addRule(RelativeLayout.ALIGN_END, editTextPass.getId());
                btnsign.setLayoutParams(params2);


            }
        },1000);



    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
