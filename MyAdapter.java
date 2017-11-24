package com.example.hp.chatlive;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends ArrayAdapter<CustArryL> {
    TextView status;
    String fuser;
    ArrayList<CustArryL> animalList = new ArrayList<>();

    public MyAdapter(Context context, int textViewResourceId, ArrayList<CustArryL> objects) {
        super(context, textViewResourceId, objects);
        animalList = objects;
        Firebase.setAndroidContext(context);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.program_list, null);
        fuser=animalList.get(position).getAnimalName();
        TextView textView = (TextView) v.findViewById(R.id.textViewh);
        status = (TextView) v.findViewById(R.id.textView100);
        de.hdodenhof.circleimageview.CircleImageView imgg =(de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.imageViewh);
        //ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        textView.setText(fuser);
        checkstatus(fuser);
        //imageView.setImageResource(animalList.get(position).getAnimalImage());
        Glide
                .with(getContext())
                .load(Uri.parse(animalList.get(position).getAnimalImage()))
                .centerCrop()
                .into(imgg);

        return v;

    }
    public void checkstatus(final String fusr)
    {
        String url22 = "https://fir-g-a2c81.firebaseio.com/Connectedusers.json";
        StringRequest request = new StringRequest(Request.Method.GET, url22, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);

                    String userr=fusr;
                    String qq= obj.getJSONObject(userr).getString("presence");
                    if(qq.equals("online")){
                        status.setText("online");
                    }
                    else {
                        status.setText("offline");
                    }
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

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }
}