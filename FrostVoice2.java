package com.example.hp.chatlive;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;


public class FrostVoice2 extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    LinearLayout parent;
    private ViewGroup mLinearLayout;
    LinearLayout child;
    ImageView sendButton, cButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2,reference3,reference4;
    ImageView imageView;
    LinearLayout.LayoutParams p2;
    TextView textView;
    int flagd;
    Context cont=this;
    private static int RESULT_LOAD_IMG = 111;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("images");


    int[] a={Gravity.CENTER,Gravity.RIGHT,Gravity.LEFT};
    MediaPlayer insond,outsond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grouplayout);
        mLinearLayout = (ViewGroup) findViewById(R.id.layout1);
        p2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        cButton = (ImageView)findViewById(R.id.cButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference3 = new Firebase("https://fir-g-a2c81.firebaseio.com/group/"+ "group");
        reference4 = new Firebase("https://fir-g-a2c81.firebaseio.com/group/"+ "group");
        // reference1 = new Firebase("https://fir-g-a2c81.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        //  reference2 = new Firebase("https://fir-g-a2c81.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        insond=MediaPlayer.create(this,R.raw.outsound);
        outsond=MediaPlayer.create(this,R.raw.arpeggio);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference3.push().setValue(map);
                    //reference4.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });
        //////////////////////////



        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
/*
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference3.push().setValue(map);
                    //reference4.push().setValue(map);
                    messageArea.setText("");
                }

*/
                //////////////


                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });



        /////////////////////////

        reference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    // addMessageBox("You:-\n" + message, 1);
                    addMessageBox("You :\n",message, 1);
                }
                else{
                    // addMessageBox(userName + ":-\n" + message, 2);
                    addMessageBox(userName + " :\n",message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String userN,String message, int type){



        //
        String Uname=userN;

        //
        TextView textView=new TextView(FrostVoice2.this);
        TextView test=new TextView(FrostVoice2.this);
        test.setText(userN);
        test.setTextColor(Color.parseColor("#000000"));
        test.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.textS));
        test.setTypeface(null, Typeface.BOLD);
        ImageView imageView=new ImageView(FrostVoice2.this);
        ImageView ir=new ImageView(FrostVoice2.this);
        ir.setBackgroundResource(R.drawable.div);
        if(message.startsWith("https://firebasestorage.googleapis.com/v0/b/fir-g-a2c81.appspot.com/"))
        {
            Uri u=Uri.parse(message);

            imgposted(u,Uname);

        }
        else {
            textposted(message,Uname);

        }

        if(type == 1) {
            insond.start();

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        else{
            outsond.start();

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        if(flagd==1) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        else
        {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });


    }

    ///////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data.getData() != null) {
            Uri selectedImage = data.getData();
            /*String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
*/
            saveimg(selectedImage);
            // ik.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }
    void saveimg(Uri selectedImage)
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Setting profile pic");
        pd.show();
        // File or Blob
        Uri file = selectedImage;

// Create the file metadata
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

// Upload file and metadata to the path 'images/mountains.jpg'
        UploadTask uploadTask = storageRef.child("images/"+file.getLastPathSegment()).putFile(file, metadata);

// Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                pd.setMessage("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                pd.setMessage("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.setMessage("upload failed");
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                pd.setMessage("upload successful");
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
               /* ik.setImageURI(null);
                ik.setImageURI(downloadUrl);*/
                postimg(downloadUrl);
            }
        });
        pd.dismiss();
    }

    public void postimg(Uri downloadUrl)
    {
        String downloadu=downloadUrl.toString();
        if(!downloadu.equals("")){
            Map<String, String> map = new HashMap<String, String>();
            map.put("message", downloadu);
            map.put("user", UserDetails.username);
            reference3.push().setValue(map);
            //reference4.push().setValue(map);
            messageArea.setText("");
        }
    }
    public void imgposted(Uri u,String Uname)
    {

        View layout2 = LayoutInflater.from(this).inflate(R.layout.test, mLinearLayout, false);
        View layout3 = LayoutInflater.from(this).inflate(R.layout.imglay, mLinearLayout, false);
        View layout4 = LayoutInflater.from(this).inflate(R.layout.gap, mLinearLayout, false);
        TextView textView = (TextView) layout2.findViewById(R.id.textView2);
        ImageView immg=(ImageView) layout3.findViewById(R.id.ima);
        LinearLayout lo=new LinearLayout(FrostVoice2.this);
        lo.setLayoutParams(p2);
        lo.setOrientation(LinearLayout.VERTICAL);
        de.hdodenhof.circleimageview.CircleImageView ikl=(de.hdodenhof.circleimageview.CircleImageView)layout2.findViewById(R.id.prof);
        Glide
                .with(getApplicationContext())
                .load(R.drawable.penguin)
                .placeholder(R.drawable.penguin)
                .centerCrop()// the uri you got from Firebase           //.centerCrop()
                .into(ikl); //Your imageView variable
        flagd=0;
        Glide
                .with(getApplicationContext())
                .load(u)
                .placeholder(R.drawable.penguin)
                .centerCrop()// the uri you got from Firebase           //.centerCrop()
                .into(immg); //Your imageView variable
        textView.setText(Uname);
        lo.addView(layout2);
        lo.addView(layout3);
        lo.setBackgroundResource(R.drawable.backbox);
        //lo.setDividerPadding(7);
        //lo.setPadding(0,15,0,15);
        mLinearLayout.addView(lo);
        mLinearLayout.addView(layout4);
        /*mLinearLayout.addView(layout2);
        mLinearLayout.addView(layout3);*/
    }
    public void textposted(String u,String Uname)
    {

        View layout2 = LayoutInflater.from(this).inflate(R.layout.test, mLinearLayout, false);
        View layout3 = LayoutInflater.from(this).inflate(R.layout.imglay, mLinearLayout, false);
        View layout33 = LayoutInflater.from(this).inflate(R.layout.textlay, mLinearLayout, false);
        View layout4 = LayoutInflater.from(this).inflate(R.layout.gap, mLinearLayout, false);
        LinearLayout lo=new LinearLayout(FrostVoice2.this);
        lo.setLayoutParams(p2);
        lo.setOrientation(LinearLayout.VERTICAL);
        TextView textView = (TextView) layout2.findViewById(R.id.textView2);
        TextView tex = (TextView) layout33.findViewById(R.id.ti);
        //TextView newt=new TextView(FrostVoice2.this);
        ImageView immg=(ImageView) layout3.findViewById(R.id.ima);
        de.hdodenhof.circleimageview.CircleImageView ikl=(de.hdodenhof.circleimageview.CircleImageView)layout2.findViewById(R.id.prof);
        Glide
                .with(getApplicationContext())
                .load(R.drawable.penguin)
                .placeholder(R.drawable.penguin)
                .centerCrop()// the uri you got from Firebase           //.centerCrop()
                .into(ikl); //Your imageView variable
        flagd=1;
        textView.setText(Uname);
       // newt.setText(u);
        tex.setText(u);
        lo.addView(layout2);
        lo.addView(layout33);
        lo.setBackgroundResource(R.drawable.backbox);
        mLinearLayout.addView(lo);
        mLinearLayout.addView(layout4);
        //mLinearLayout.addView(layout2);
        //mLinearLayout.addView(newt);
    }

}
