package com.example.hp.chatlive;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Iterator;

import io.fabric.sdk.android.Fabric;

import static android.widget.Toast.LENGTH_LONG;

public class KnownUsers2 extends Fragment {
    ListView usersList;
    TextView noUsersText;
    public ArrayList<String> al = new ArrayList<>();
    public ArrayList<String> a2 = new ArrayList<>();
    public ArrayList<CustArryL> trail = new ArrayList<>();
    int totalUsers = 0;
    int imgUsers = 0;
    ProgressDialog pd;
    Dialog ddi;
    public View parentView;
    String key = "";
    String url5 = "https://fir-g-a2c81.firebaseio.com/proimgdetail.json";
    String url4 = "https://fir-g-a2c81.firebaseio.com/users/"+UserDetails.username+"/Friends.json";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.act_knownusers2, container, false);
        //setUpViews();

        Fabric.with(getActivity(), new Crashlytics());


        //@Override
        //protected void onCreate(Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_users);

        usersList = (ListView)parentView.findViewById(R.id.usersList);
        noUsersText = (TextView)parentView.findViewById(R.id.noUsersText);

        getjobdone gbd=new getjobdone();
        gbd.execute();
        /*pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();




        StringRequest request = new StringRequest(Request.Method.GET, url4, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        StringRequest request2 = new StringRequest(Request.Method.GET, url5, new Response.Listener<String>(){
            @Override
            public void onResponse(String r) {
                doOnSuccessimg(r);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyErrorr) {
                System.out.println("" + volleyErrorr);
            }
        });
        RequestQueue rQueue2 = Volley.newRequestQueue(getActivity());
        rQueue2.add(request2);

        //



      //  checkme();
        //
        */
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                startActivity(new Intent(getActivity()/*Users.this*/, Chat.class));
            }
        });
        //Toast.makeText(getActivity(),a2.get(0),LENGTH_LONG).show();
        return parentView;
    }

    public void doOnSuccess(String s){
        try {
            final String kk=s;
        JSONObject obj = new JSONObject(kk);
        Iterator i = obj.keys();
            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {
                    al.add(key);
                    //  a2.add(obj2.getString(key));
                    //Toast.makeText(getActivity(),obj2.getString(key),Toast.LENGTH_SHORT).show();
                }

                totalUsers++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();*/
    }

    ////////////////////

    public void doOnSuccessimg(String r){
        String[] imginfo= new String[al.size()+1];
        try {
            JSONObject obj2 = new JSONObject(r);
            Iterator i = obj2.keys();
            //Toast.makeText(getActivity(),"got access",LENGTH_LONG).show();
            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {
                    if(al.contains(key))
                    {
                        int ind=al.indexOf(key);
                        imginfo[ind]=obj2.getJSONObject(key).getString("img");
                    }
                    //  a2.add(obj2.getString(key));
                    //Toast.makeText(getActivity(),obj2.getString(key),Toast.LENGTH_SHORT).show();
                }
               // imgUsers++;
            }
            int z=0;
            while (imginfo[z]!=null) {
                a2.add(imginfo[z]);
                z++;
            }
        } catch (JSONException e) {
            String w=e.toString();
            //Toast.makeText(getActivity(),w,LENGTH_LONG).show();
            e.printStackTrace();
        }
        if(a2.isEmpty())
        {
            //Toast.makeText(getActivity(),"Oops Something went wrong",LENGTH_LONG).show();
        }
        else
        {

            //Toast.makeText(getActivity(),a2.get(0),LENGTH_LONG).show();
            if(!al.isEmpty() && !a2.isEmpty())
            {
                for (int q=0;q<al.size();q++)
                {
                    trail.add(new CustArryL(al.get(q),a2.get(q)));
                }
               // Toast.makeText(getActivity(),"added to trail",LENGTH_LONG).show();
            }
        }
       usersList.setVisibility(View.VISIBLE);
        MyAdapter myAdapter=new MyAdapter(getActivity(),R.layout.program_list,trail);
        usersList.setAdapter(myAdapter);



       /* if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            MyAdapter myAdapter=new MyAdapter(getActivity(),R.layout.program_list,trail);
            usersList.setAdapter(myAdapter);
            //usersList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, al));
        }
*/
      //  pd.dismiss();
      /*  if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, al));
        }*/
    }
  /*  public void checkme()
    {
        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            MyAdapter myAdapter=new MyAdapter(getActivity(),R.layout.program_list,trail);
            usersList.setAdapter(myAdapter);
            //usersList.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }*/

  //


    private class getjobdone extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
//            pd = new ProgressDialog(getActivity()/*Users.this*/);
//            pd.setMessage("Loading...");
//            pd.show();
            ddi = new Dialog(getActivity());
            ddi.requestWindowFeature(Window.FEATURE_NO_TITLE);

            ddi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ddi.setContentView(R.layout.load);
            TextView ty=(TextView)ddi.findViewById(R.id.tyo);
            ty.setText("Loading");
            ddi.show();

        }
        @Override
        protected String doInBackground(String... params) {
            StringRequest request = new StringRequest(Request.Method.GET, url4, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    doOnSuccess(s);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError);
                }
            });
            RequestQueue rQueue = Volley.newRequestQueue(getActivity()/*Users.this*/);
            rQueue.add(request);

            StringRequest request2 = new StringRequest(Request.Method.GET, url5, new Response.Listener<String>(){
                @Override
                public void onResponse(String r) {
                    doOnSuccessimg(r);
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyErrorr) {
                    System.out.println("" + volleyErrorr);
                }
            });
            RequestQueue rQueue2 = Volley.newRequestQueue(getActivity()/*Users.this*/);
            rQueue2.add(request2);
            String kkk="done";
            return kkk;
        }
        @Override
        protected void onPostExecute(String a) {
            ddi.dismiss();
        }
    }

}