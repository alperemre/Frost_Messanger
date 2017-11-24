package com.example.hp.chatlive;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import io.fabric.sdk.android.Fabric;

import static android.app.Activity.RESULT_OK;


public class ProfileFrag extends Fragment {
    private View parentView;
    String imgDecodableString;
    private static int RESULT_LOAD_IMG = 111;
    private static int SELECT_FILE = 1;
    de.hdodenhof.circleimageview.CircleImageView ik;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("images");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.profilelay, container, false);
        Fabric.with(getActivity(), new Crashlytics());
        Firebase.setAndroidContext(getActivity());
        TextView txtuser = (TextView) parentView.findViewById(R.id.textView7);
        TextView txtuserno = (TextView) parentView.findViewById(R.id.textView11);
        ImageView myQr=(ImageView)parentView.findViewById(R.id.imageView6);
        ik = (de.hdodenhof.circleimageview.CircleImageView) parentView.findViewById(R.id.profile_image);


        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Dejavu.ttf");
        txtuser.setTypeface(face);
        txtuser.setText(UserDetails.username);
        txtuserno.setText(UserDetails.phone);
        checkforpropic();

        //

        final Dialog d = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        d.setContentView(R.layout.customqrd);
        d.setCanceledOnTouchOutside(true);
        d.setCancelable(true);
        //
        final ImageView iqr=(ImageView)d.findViewById(R.id.imageViewqr);
        final LinearLayout lshare=(LinearLayout)d.findViewById(R.id.shareqr);

        //
        ik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);*/

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });


        myQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap myBitmap = QRCode.from(UserDetails.username).bitmap();
                /*final Dialog d = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
                d.setContentView(R.layout.customqrd);
                d.setCanceledOnTouchOutside(true);
                d.setCancelable(true);
                //
                ImageView iqr=(ImageView)d.findViewById(R.id.imageViewqr);
                LinearLayout lshare=(LinearLayout)d.findViewById(R.id.shareqr); */
                iqr.setImageBitmap(myBitmap);
                d.show();
                //myImage.setImageBitmap(myBitmap);
            }
        });
        lshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap myBitmap = QRCode.from(UserDetails.username).bitmap();
                /*ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);


                OutputStream outstream;
                try {
                    outstream = getActivity().getContentResolver().openOutputStream(uri);
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("mimeType", "image/*");*/
                String pathofBmp = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), myBitmap,"title", null);
                Uri bmpUri = Uri.parse(pathofBmp);
                final Intent emailIntent1 = new Intent(     android.content.Intent.ACTION_SEND);
                emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
                emailIntent1.setType("image/png");
                startActivity(Intent.createChooser(emailIntent1, "Share QRcode with"));
            }
        });

        return parentView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data.getData() != null) {
            Uri selectedImage = data.getData();
            /*String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();*/

            saveimg(selectedImage);
           // ik.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
        else
        {
            Toast.makeText(getActivity(),"Can't fetch",Toast.LENGTH_LONG).show();
        }


    }

    void saveimg(Uri selectedImage)
    {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Setting profile pic");
        pd.show();
        // File or Blob
        Uri file = selectedImage;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), file);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 2, bytes);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Myprofpic", null);
            Uri uril = Uri.parse(path);
// Create the file metadata
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();

// Upload file and metadata to the path 'images/mountains.jpg'
            UploadTask uploadTask = storageRef.child("images/" + uril.getLastPathSegment()).putFile(uril, metadata);

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

                    Glide
                            .with(getContext())
                            .load(downloadUrl) // the uri you got from Firebase
                            .placeholder(R.drawable.penguin)
                            .dontAnimate()
                            .centerCrop()
                            .into(ik); //Your imageView variable
                    savetoprofilebase(downloadUrl);
                    Toast.makeText(getActivity(), "Setting new profile pic", Toast.LENGTH_LONG).show();
                }
            });
            pd.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
            pd.dismiss();
        }
    }

    public void savetoprofilebase(final Uri downloadUrl)
    {
        String urll = "https://fir-g-a2c81.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, urll, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://fir-g-a2c81.firebaseio.com/users");
                Firebase reference2 = new Firebase("https://fir-g-a2c81.firebaseio.com/proimgdetail");
                String user=UserDetails.username;
                UserDetails.imguri=downloadUrl.toString();
                reference.child(user).child("profile_img").setValue(UserDetails.imguri);
                reference2.child(user).child("img").setValue(UserDetails.imguri);
                //Toast.makeText(getActivity(),"Done haha",Toast.LENGTH_LONG).show();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
    }
    public void checkforpropic()
    {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Checking details");
        pd.show();
        String url2 = "https://fir-g-a2c81.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                    try {
                        JSONObject obj = new JSONObject(s);

                        String user=UserDetails.username;
                        UserDetails.imguri= obj.getJSONObject(user).getString("profile_img");
                        if(UserDetails.imguri.equals("")){
                            int a=0;
                        }
                        else {
                            Uri u=Uri.parse(UserDetails.imguri);
                            Glide
                                    .with(getContext())
                                    .load(u)
                                    .placeholder(R.drawable.penguin)
                                    .dontAnimate()// the uri you got from Firebase
                                    .centerCrop()
                                    .into(ik); //Your imageView variable
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

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
        pd.dismiss();
    }
}
