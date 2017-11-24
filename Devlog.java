package com.example.hp.chatlive;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.fabric.sdk.android.Fabric;

public class Devlog extends AppCompatActivity {
    EditText edt;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.devlog);
        Firebase.setAndroidContext(this);
        ImageView rswing = (ImageView) findViewById(R.id.tt);
        ImageView con = (ImageView) findViewById(R.id.jj);
        edt = (EditText) findViewById(R.id.password);
        Animation aq= AnimationUtils.loadAnimation(this,R.anim.swing);
        rswing.startAnimation(aq);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt.getText().toString().equals("22188"))
                {
                    getBase(Devlog.this);
                }
                else
                {
                    Toast.makeText(Devlog.this,"Invalid Dev Key",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static void getBase(final Context context) {
        String url = "https://fir-g-a2c81.firebaseio.com/.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(obj);
                    File file=new File(  Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android"+"/FROSTDATABASE.csv");
                    String csv = CDL.toString(jsonArray);
                    FileUtils.writeStringToFile(file, csv);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                    context.startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(context,"JsonExp",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }catch (IOException e) {
                    Toast.makeText(context,"IoExp",Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);

            }
        });
        Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}
