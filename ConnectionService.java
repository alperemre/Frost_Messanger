package com.example.hp.chatlive;

/**
 * Created by HP on 28-Sep-17.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;


public class ConnectionService extends Service {
    String urle = "https://fir-g-a2c81.firebaseio.com/Connectedusers.json";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Fabric.with(this, new Crashlytics());
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Connection Service Started", Toast.LENGTH_LONG).show();
        Firebase.setAndroidContext(this);
        final Firebase referencee = new Firebase("https://fir-g-a2c81.firebaseio.com/Connectedusers");
        referencee.child(UserDetails.username).child("presence").onDisconnect().setValue("offline");
        referencee.child(UserDetails.username).child("presence").setValue("online");

        ///////////////
        /*StringRequest request = new StringRequest(Request.Method.GET, urle, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                try {
                    reference.setValue("online");
                    reference.onDisconnect().setValue("offline");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(ConnectionService.this);
        rQueue.add(request);*/
        /////////////////////
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}