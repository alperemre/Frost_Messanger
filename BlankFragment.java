package com.example.hp.chatlive;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;


public class BlankFragment extends Fragment {
    private View parentView;
    TextView tvw;
    ImageView iu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragabout, container, false);
        Fabric.with(getActivity(), new Crashlytics());
        tvw=(TextView)parentView.findViewById(R.id.tvvu);
        iu=(ImageView) parentView.findViewById(R.id.tt);
        Animation aq= AnimationUtils.loadAnimation(getActivity(),R.anim.swing);
        iu.startAnimation(aq);
        //Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Dejavu.ttf");
       // tvw.setTypeface(face);
        String url = "https://fir-g-a2c81.firebaseio.com/About.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    String about=obj.getString("Details");
                    tvw.setText(about);
                    tvw.setTextSize(18);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
        return parentView;
    }
}
