package com.example.hp.chatlive;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;


//

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


//

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

public class Register extends AppCompatActivity {
    EditText username, password, phoneno;
    Button registerButton, vbb;
    String user, pass, phonen;
    String countrycodeInd="+91";
    LinearLayout login;
    ImageView rswing;
     Dialog dialog;
    EditText edt;
    //
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    //
    private static final String TAG = "PhoneAuthActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Fabric.with(this, new Crashlytics());
        rswing = (ImageView) findViewById(R.id.tt);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        phoneno = (EditText)findViewById(R.id.phone);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (LinearLayout) findViewById(R.id.llogin);

        Animation aq= AnimationUtils.loadAnimation(this,R.anim.swing);
        rswing.startAnimation(aq);
        //
        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                   phoneno.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                  //  Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                        //    Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(Register.this,"Quata exceeded",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

//llllllllllllll
        dialog=new Dialog(Register.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.jabp);
        edt=(EditText)dialog.findViewById(R.id.editText);
        vbb=(Button)dialog.findViewById(R.id.vb);
        final TextView at=(TextView)dialog.findViewById(R.id.t2);
        vbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPhoneNumberWithCode(mVerificationId,edt.getText().toString());
            }
        });




        //






        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
                overridePendingTransition(R.anim.slidefromleft, R.anim.slidetoright);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                phonen = phoneno.getText().toString();

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                        password.setError("can't be blank");
                    }
                    else if(!user.matches("[A-Za-z0-9]+")){
                            username.setError("only alphabet or number allowed");
                        }
                        else if(user.length()<5){
                                username.setError("at least 5 characters long");
                            }
                            else if(pass.length()<5){
                                password.setError("at least 5 characters long");
                            }
                              else if(phonen.equals("")){
                                  phoneno.setError("can't be blank");
                                }
                                 else if(phonen.length()<10){
                                      phoneno.setError("phone no must be 10 digits long");
                                  }
                            else {
                               /* final ProgressDialog pd = new ProgressDialog(Register.this);
                                pd.setMessage("Loading...");
                                pd.show();*/

                                final String finalphoneno=countrycodeInd+phonen;
                                phoneno.setText(finalphoneno);
                                startPhoneNumberVerification(finalphoneno);
                                  dialog.show();

                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            at.setText("00:"+ (millisUntilFinished/1000));
                        }

                        @Override
                        public void onFinish() {
                            //Toast.makeText(Register.this,"Verification Failed",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }.start();
                              /*  String url = "https://fir-g-a2c81.firebaseio.com/users.json";

                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String s) {
                                        Firebase reference = new Firebase("https://fir-g-a2c81.firebaseio.com/users");

                                        if(s.equals("null")) {
                                            reference.child(user).child("password").setValue(pass);
                                            reference.child(user).child("phone_no").setValue(finalphoneno);
                                            Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Register.this,Login.class));
                                        }
                                        else {
                                            try {
                                                JSONObject obj = new JSONObject(s);

                                                if (!obj.has(user)) {
                                                    reference.child(user).child("password").setValue(pass);
                                                    reference.child(user).child("phone_no").setValue(finalphoneno);
                                                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(Register.this,Login.class));
                                                } else {
                                                    Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (JSONException e) {
                                                    e.printStackTrace();
                                            }
                                        }

                                        pd.dismiss();
                                    }

                                },new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        System.out.println("" + volleyError );
                                        pd.dismiss();
                                    }
                                });

                                RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                                rQueue.add(request);*/
                            }
            }
        });


    }
    //Newly added--------------------------
/*
    public void addBasicProfile(Firebase reference,String s,String user,String pass,String finalphoneno)
    {
        try {
            JSONObject obj = new JSONObject(s);

            if (!obj.has(user)) {
                reference.child(user).child("password").setValue(pass);
                reference.child(user).child("phone_no").setValue(finalphoneno);
                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Register.this,Login.class));
            } else {
                Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            //final FirebaseUser user1 = task.getResult().getUser();
                            //////////////////////////////



                            final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                            if (user1 != null) {

                                //Toast.makeText(Register.this,"uid:"+user1.getUid().toString(),Toast.LENGTH_LONG).show();
                                //new added
                                String url = "https://fir-g-a2c81.firebaseio.com/users.json";

                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String s) {
                                        Firebase reference = new Firebase("https://fir-g-a2c81.firebaseio.com/users");
                                        Firebase reference2 = new Firebase("https://fir-g-a2c81.firebaseio.com/proimgdetail");
                                        Firebase reference3 = new Firebase("https://fir-g-a2c81.firebaseio.com/Connectedusers");

                                        if(s.equals("null")) {
                                            reference.child(user).child("password").setValue(pass);
                                            reference.child(user).child("phone_no").setValue(phoneno.getText().toString());
                                            reference.child(user).child("UID_no").setValue(user1.getUid().toString());
                                            reference2.child(user).child("img").setValue("");
                                            reference3.child(user).child("presence").setValue("offline");
                                            Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Register.this,Login.class));
                                        }
                                        else {
                                            //asdfg
                                            //asdfg
                                            try {
                                                JSONObject obj = new JSONObject(s);

                                                if (!obj.has(user)) {
                                                    reference.child(user).child("password").setValue(pass);
                                                    reference.child(user).child("phone_no").setValue(phoneno.getText().toString());
                                                    reference.child(user).child("UID_no").setValue(user1.getUid().toString());
                                                    reference.child(user).child("profile_img").setValue("");
                                                    reference2.child(user).child("img").setValue("");
                                                    reference.child(user).child("Friends").child(user).setValue("yes");
                                                    reference3.child(user).child("presence").setValue("offline");
                                                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                    startActivity(new Intent(Register.this,Login.class));
                                                } else {
                                                    Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    }

                                },new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        System.out.println("" + volleyError );
                                    }
                                });

                                RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                                rQueue.add(request);







                                dialog.dismiss();
                                ////////////////////////////////////
                                //startActivity(new Intent(Register.this, Login.class));
                                finish();
                            }

/*
                            String url = "https://fir-g-a2c81.firebaseio.com/users.json";

                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String s) {
                                        Firebase reference = new Firebase("https://fir-g-a2c81.firebaseio.com/users");

                                        if(s.equals("null")) {
                                            reference.child(user).child("password").setValue(pass);
                                            reference.child(user).child("phone_no").setValue(phoneno.getText().toString());
                                            Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Register.this,Login.class));
                                        }
                                        else {
                                            //asdfg
                                                    //asdfg
                                            try {
                                                JSONObject obj = new JSONObject(s);

                                                if (!obj.has(user)) {
                                                    reference.child(user).child("password").setValue(pass);
                                                    reference.child(user).child("phone_no").setValue(phoneno.getText().toString());
                                                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                    startActivity(new Intent(Register.this,Login.class));
                                                } else {
                                                    Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (JSONException e) {
                                                    e.printStackTrace();
                                            }
                        }


                    }

                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError );
                    }
                });

        RequestQueue rQueue = Volley.newRequestQueue(Register.this);
        rQueue.add(request);







                            dialog.dismiss();
                             ////////////////////////////////////
                            startActivity(new Intent(Register.this, Login.class));
                            finish();*/
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                edt.setError("Invalid code.");
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    //New;y added---------------------------------
}