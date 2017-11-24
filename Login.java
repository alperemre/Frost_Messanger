package com.example.hp.chatlive;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Html;
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
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import io.fabric.sdk.android.Fabric;
import ru.alexbykov.nopermission.PermissionHelper;

public class Login extends BaseActivity implements SinchService.StartFailedListener{
    LinearLayout registerUser;
    EditText username, password;
    CircularProgressButton loginButton;
    String user, pass,TAG="Permissions:";
    SharedPreferences pref;
    ImageView rswing;
    SharedPreferences.Editor editor;
    Dialog ddi;
    private ProgressDialog mSpinner;
    PermissionHelper permissionHelper;
    Firebase frostref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fabric.with(this, new Crashlytics());
        Firebase.setAndroidContext(this);
        if (Build.VERSION.SDK_INT > 21) {
            // Call some material design APIs here
            permissionHelper = new PermissionHelper(this);
            permissionHelper.check(android.Manifest.permission.INTERNET, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_SMS, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.WAKE_LOCK).onSuccess(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Success");
                }
            }).onFailure(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Failure");
                }
            }).onNeverAskAgain(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "NeverAskAgain");
                }
            }).run();
        }
        OneSignal.startInit(this).init();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        //editor = pref.edit();
        editor = pref.edit();
        String s = getIntent().getStringExtra("Logout");
        if(s!=null)
        {
            if(s.equals("yes")) {
                editor.remove("Username");
                editor.remove("Password");
                editor.apply();
            }
        }


        final String prefuser = pref.getString("Username","");
        final String prefpass = pref.getString("Password","");



        rswing = (ImageView) findViewById(R.id.tt);
        registerUser = (LinearLayout) findViewById(R.id.lsignup);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (CircularProgressButton)findViewById(R.id.loginButton);
        Animation aq= AnimationUtils.loadAnimation(this,R.anim.swing);
        rswing.startAnimation(aq);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                overridePendingTransition(R.anim.slidefromright, R.anim.slidetoleft);
            }
        });


        if(!prefuser.equals("") && !prefpass.equals(""))
        {
            String url = "https://fir-g-a2c81.firebaseio.com/users.json";
            final Dialog dialog = new Dialog(Login.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.load);
            TextView ty=(TextView)dialog.findViewById(R.id.tyo);
            ty.setText("Welcome back, "+prefuser);
            /*final ProgressDialog pd = new ProgressDialog(Login.this);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pd.setMessage("Welcome back, "+prefuser);
            pd.show();*/
            dialog.show();

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if(!obj.has(prefuser)){
                                Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                            }
                            else if (obj.getJSONObject(prefuser).getString("password").equals(prefpass)){
                                UserDetails.username = prefuser;
                                UserDetails.password = prefpass;
                                UserDetails.phone = obj.getJSONObject(prefuser).getString("phone_no");
                                UserDetails.idd = obj.getJSONObject(prefuser).getString("UID_no");
                                /*Intent newIntent = new Intent(Login.this, ResideV.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(newIntent);
                                finish();*/
                                loginClicked(prefuser);
                            }
                            else
                            {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    //pd.dismiss();
                    dialog.dismiss();
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError);

                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(Login.this);
            rQueue.add(request);
        }
        //

/*
        FirebaseUser user1 = FirebaseAuth.getCurrentUser();
        if (user1 != null) {
            String uid = user1.getUid();
            Toast.makeText(Login.this,"Yes the userd id is:"+uid,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Login.this,"Not successful",Toast.LENGTH_LONG).show();
        }
*/

        //


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else{
                    loginButton.startAnimation();
                    String url = "https://fir-g-a2c81.firebaseio.com/users.json";
                   /* final ProgressDialog pd = new ProgressDialog(Login.this);
                    pd.setMessage("Loading...");
                    pd.show();*/

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                                    }
                                    else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        UserDetails.phone = obj.getJSONObject(user).getString("phone_no");
                                        UserDetails.idd = obj.getJSONObject(user).getString("UID_no");
                                        editor.putString("Username", user);
                                        editor.putString("Password", pass);
                                        editor.apply();
                                        loginButton.revertAnimation(new OnAnimationEndListener() {
                                            @Override
                                            public void onAnimationEnd() {
                                                loginButton.setText("Welcome, "+user);
                                            }
                                        });
                                        loginClicked(user);
                                        /*Intent newIntent = new Intent(Login.this, ResideV.class);
                                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(newIntent);
                                        finish();*/
                                    }
                                    else {
                                        Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                           // pd.dismiss();

                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);

                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }

            }
        });


    }

    //////////////////////////


    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (ddi != null) {
            ddi.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (ddi != null) {
            ddi.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    private void loginClicked(String aa) {
        String userName = aa;
        OneSignal.sendTag("User_id",userName);
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openPlaceCallActivity();
        }
    }

    private void openPlaceCallActivity() {
        FrostStatus();

        //Toast.makeText(this,"Sinchservice Started",Toast.LENGTH_LONG).show();
        Intent newIntent = new Intent(Login.this, ResideV.class);
        Intent newIntent2 = new Intent(Login.this, ConnectionService.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(newIntent2);
        startActivity(newIntent);
        finish();
    }

    private void showSpinner() {
        /*mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();*/
        ddi = new Dialog(Login.this);
        ddi.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ddi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ddi.setContentView(R.layout.load);
        TextView ty=(TextView)ddi.findViewById(R.id.tyo);
        ty.setText("Logging in");
        ddi.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //////////////////////////
    public void FrostStatus()
    {
        frostref= new Firebase("https://fir-g-a2c81.firebaseio.com/FrostVoice");
        frostref.child("State").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("On"))
                {
                    UserDetails.frostvoice_flag=1;
                }
                else
                {
                    UserDetails.frostvoice_flag=0;
                }
            }
            @Override
            public void onCancelled(FirebaseError databaseError) {
            }
        });
    }
}
