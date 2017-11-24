package com.example.hp.chatlive;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.IBinder;
import android.preference.PreferenceManager;

import android.support.annotation.Nullable;
import android.widget.EditText;

import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.sinch.android.rtc.SinchError;

import io.fabric.sdk.android.Fabric;


public class Cservice extends StartXfeature implements SinchService.StartFailedListener{
    EditText username, password;
    String user, pass;
    SharedPreferences pref;

    private SinchService.SinchServiceInterface mSinchServiceInterface;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceInterface;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Fabric.with(this, new Crashlytics());
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String prefusert = pref.getString("Username","");
        if(!prefusert.equals(""))
        {
            loginClicked(prefusert);
        }
        return START_STICKY;
    }

    //////////////////////////


    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }


    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    private void loginClicked(String aa) {
        String userName = aa;
        getSinchServiceInterface().startClient(userName);
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
        } else {
            openPlaceCallActivity();
        }
    }

    private void openPlaceCallActivity() {
        Toast.makeText(this,"Sinchservice Started",Toast.LENGTH_LONG).show();
    }

}