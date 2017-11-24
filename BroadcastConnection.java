package com.example.hp.chatlive;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.sinch.android.rtc.SinchError;

import io.fabric.sdk.android.Fabric;

/**
 * Created by HP on 02-Oct-17.
 */

public class BroadcastConnection extends BroadcastReceiver{
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Fabric.with(context, new Crashlytics());
        Firebase.setAndroidContext(context);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        /*final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            // Do something

            Log.d("Network Available ", "Flag No 1");
        }*/
        if(isOnline(context))
        {
            String prefusert = pref.getString("Username","");
            if(!prefusert.equals(""))
            {
                final Firebase referenceet = new Firebase("https://fir-g-a2c81.firebaseio.com/Connectedusers");
                referenceet.child(prefusert).child("presence").onDisconnect().setValue("offline");
                referenceet.child(prefusert).child("presence").setValue("ideal");
                /*Intent nw = new Intent(context, SinchService.class);
                context.startService(nw);
                Intent newIntent22 = new Intent(context, Cservice.class);
                context.startService(newIntent22);*/
            }
        }
    }
    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

}