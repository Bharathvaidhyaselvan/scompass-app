package com.packages.scompass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.internal.disposables.ArrayCompositeDisposable;

public class ViewerProfile extends AppCompatActivity {
    LinearLayout layoutaboutme,fellowmateslayout,postslayout;
    LinearLayout followingbox;
    TextView numberfollowing;
    ProgressBar px;
    TextView aboutbutton,postsbutton,userstatus,username;
    View aboutline,postsline,fellowmatesline;
    static TextView fellowmatesbutton;
    ImageView youtube,instagram,linkedin,profilepic,malefemalepic,hidefollowinglayout;
    TextView abouttext,userlocation;
    String key,email;
    FrameLayout followinglayout;
    int totalfollowers=0;
    ProgressBar progressfollowing;
    RecyclerView fellowmatessrecyclerview,postsrecyclerview,following_recycler_view;
    CardView follow,following;
    TextView textfollow,textfollowing,userage;
    String mykey = "";
    int x=0;
    MyAttr attr ;
    Uri urix;
    private Uri selectedImageUri;

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            px.setVisibility(View.VISIBLE);
            Toast.makeText(this, "please wait, uploading....", Toast.LENGTH_SHORT).show();
            selectedImageUri = data.getData();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("images")
                            .child(mykey).child("profilephoto");

            reference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            urix = uri;
                            Glide.with(ViewerProfile.this).load(uri).into(profilepic);
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(mykey).child("profileimage");
                            db.setValue(String.valueOf(uri));
                            SharedPreferences.Editor s = getSharedPreferences("USER",MODE_PRIVATE).edit();
                            s.putString("profileurl", String.valueOf(uri));
                            s.apply();

                            x=1;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ViewerProfile.this, "unable to upload photo!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewerProfile.this, "unable to upload photo!", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    // show the progress bar
                    double IMAGEPROGRESS = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    if (IMAGEPROGRESS==100)
                    {
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users")
                                .child(mykey).child("profileimage");
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                {

                                    SharedPreferences.Editor s = getSharedPreferences("USER",MODE_PRIVATE).edit();
                                    s.putString("profileurl", String.valueOf(urix));
                                    s.apply();

                                    x=1;
                                    px.setVisibility(View.GONE);
                                    Toast.makeText(ViewerProfile.this, "profile photo updated...", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            });

        }
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_profile);
        postslayout = findViewById(R.id.postslayout);
        progressfollowing = findViewById(R.id.progressfollowing);
        px = findViewById(R.id.px);
        followinglayout = findViewById(R.id.followinglayout);
        hidefollowinglayout = findViewById(R.id.hidefollowinglayout);
        following_recycler_view = findViewById(R.id.following_recycler_view);
        followingbox = findViewById(R.id.followingbox);
        numberfollowing = findViewById(R.id.numberfollowing);
        fellowmateslayout = findViewById(R.id.fellowmateslayout);
        layoutaboutme = findViewById(R.id.layoutaboutme);
        aboutbutton = findViewById(R.id.aboutbutton);
        postsbutton = findViewById(R.id.postsbutton);
        fellowmatesbutton = findViewById(R.id.fellowmatesbutton);
        aboutline = findViewById(R.id.aboutline);
        postsline = findViewById(R.id.postsline);
        fellowmatesline = findViewById(R.id.fellowmatesline);
        youtube = findViewById(R.id.youtube);
        instagram = findViewById(R.id.instagram);
        follow = findViewById(R.id.follow);
        following = findViewById(R.id.following);
        textfollowing = findViewById(R.id.textfollowing);
        textfollow = findViewById(R.id.textfollow);
        linkedin = findViewById(R.id.linkedin);
        profilepic = findViewById(R.id.profilepic);
        malefemalepic = findViewById(R.id.malefemalepic);
        userage = findViewById(R.id.userage);
        userstatus = findViewById(R.id.userstatus);
        userlocation = findViewById(R.id.userlocation);
        username = findViewById(R.id.username);
        fellowmatessrecyclerview = findViewById(R.id.fellowmatessrecyclerview);
        postsrecyclerview = findViewById(R.id.postsrecyclerview);
        abouttext = findViewById(R.id.abouttext);
        attr = new MyAttr(ViewerProfile.this);
        mykey = attr.getMykey();
        Intent i = getIntent();
        key = i.getStringExtra("key");
        email = i.getStringExtra("email");

        Load();
        ResetAll();
        loadPosts();
        LoadFollowing();
        ResetAll();


        followingbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (NetworkUtils.isNetworkAvailable(ViewerProfile.this))
//                {
//                    if (numberfollowing.getText().toString().trim().equals("...") || numberfollowing.getText().toString().trim().equals("0"))
//                    {
//                        if (key.equals(mykey))
//                        {
//                            Toast.makeText(ViewerProfile.this, "You're not following anyone!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else {
//                            followinglayout.setVisibility(View.VISIBLE);
//                            following_recycler_view.setVisibility(View.GONE);
//                            progressfollowing.setVisibility(View.VISIBLE);
//                            LoadFollowing();
//                    }
//                }
//                else {
//                    Toast.makeText(ViewerProfile.this, "No internet connection!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        hidefollowinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followinglayout.setVisibility(View.GONE);
            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.equals(mykey))
                {
                    if (NetworkUtils.isNetworkAvailable(ViewerProfile.this))
                    {
                        selectImage();

                    }
                    else
                    {
                        Toast.makeText(ViewerProfile.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    View view = LayoutInflater.from(ViewerProfile.this).inflate(R.layout.customalertfinput,null);
                    TextView titletv,savebtn,cancelbtn,textgps;
                    EditText enterinput;

                    textgps = view.findViewById(R.id.gps);
                    titletv = view.findViewById(R.id.title);
                    savebtn = view.findViewById(R.id.savebutton);
                    cancelbtn = view.findViewById(R.id.cancelbutton);
                    enterinput = view.findViewById(R.id.getinputtextd);
                    savebtn.setText("Open");
                    cancelbtn.setText("Change");
                    enterinput.setVisibility(View.GONE);
                    textgps.setVisibility(View.VISIBLE);
                    textgps.setText("Loading link...");
                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewerProfile.this);
                    alert.setView(view);
                    alert.setCancelable(false);
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    //load link
                    final int[] x = {0};
                    DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Youtube");
                    loadstatus.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                x[0] =1;
                                textgps.setText(snapshot.getValue(String.class));
                            }
                            else {
                                x[0] =1;
                                textgps.setText("No link to show!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    titletv.setText("Youtube");
                    savebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (x[0]==1)
                            {
                                if (textgps.getText().toString().trim().equals("No link to show!"))
                                {
                                    Toast.makeText(ViewerProfile.this, "No link to open!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    alertDialog.dismiss();
                                    DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Youtube");
                                    loadstatus.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                openLinkInChrome(ViewerProfile.this,snapshot.getValue(String.class));
                                            }
                                            else {
                                                Toast.makeText(ViewerProfile.this, "not available!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }


                        }
                    });
                    cancelbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (x[0]==1)
                            {
                                alertDialog.dismiss();
                                if (key.equals(mykey))
                                {
                                    View view = LayoutInflater.from(ViewerProfile.this).inflate(R.layout.customalertfinput,null);
                                    TextView titletv,savebtn,cancelbtn;
                                    EditText enterinput;

                                    titletv = view.findViewById(R.id.title);
                                    savebtn = view.findViewById(R.id.savebutton);
                                    cancelbtn = view.findViewById(R.id.cancelbutton);
                                    enterinput = view.findViewById(R.id.getinputtextd);

                                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewerProfile.this);
                                    alert.setView(view);
                                    alert.setCancelable(false);
                                    final AlertDialog alertDialog = alert.create();
                                    alertDialog.show();
                                    alertDialog.show();

                                    titletv.setText("Set Youtube Link");
                                    enterinput.setHint("Enter your link...");
                                    savebtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String status = enterinput.getText().toString().trim();
                                            if (!status.isEmpty())
                                            {
                                                if (NetworkUtils.isNetworkAvailable(ViewerProfile.this))
                                                {

                                                    DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Youtube");
                                                    loadstatus.setValue(status);
                                                    alertDialog.dismiss();

                                                }
                                                else {
                                                    Toast.makeText(ViewerProfile.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                Toast.makeText(ViewerProfile.this, "Empty link!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    cancelbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(ViewerProfile.this, "You can't change anyone's link!", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    });
                }

            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    View view = LayoutInflater.from(ViewerProfile.this).inflate(R.layout.customalertfinput,null);
                    TextView titletv,savebtn,cancelbtn,textgps;
                    EditText enterinput;

                    textgps = view.findViewById(R.id.gps);
                    titletv = view.findViewById(R.id.title);
                    savebtn = view.findViewById(R.id.savebutton);
                    cancelbtn = view.findViewById(R.id.cancelbutton);
                    enterinput = view.findViewById(R.id.getinputtextd);
                    savebtn.setText("Open");
                    cancelbtn.setText("Change");
                    enterinput.setVisibility(View.GONE);
                    textgps.setVisibility(View.VISIBLE);
                    textgps.setText("Loading link...");
                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewerProfile.this);
                    alert.setView(view);
                    alert.setCancelable(false);
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    //load link
                    final int[] x = {0};
                    DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Instagram");
                    loadstatus.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                x[0] =1;
                                textgps.setText(snapshot.getValue(String.class));
                            }
                            else {
                                x[0] =1;
                                textgps.setText("No link to show!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    titletv.setText("Instagram");
                    savebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (x[0]==1)
                            {
                                if (textgps.getText().toString().trim().equals("No link to show!"))
                                {
                                    Toast.makeText(ViewerProfile.this, "No link to open!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    alertDialog.dismiss();

                                    DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Instagram");
                                    loadstatus.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                openLinkInChrome(ViewerProfile.this,snapshot.getValue(String.class));
                                            }
                                            else {
                                                Toast.makeText(ViewerProfile.this, "not available!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }


                        }
                    });
                    cancelbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (x[0]==1)
                            {
                                alertDialog.dismiss();
                                if (key.equals(mykey))
                                {
                                    View view = LayoutInflater.from(ViewerProfile.this).inflate(R.layout.customalertfinput,null);
                                    TextView titletv,savebtn,cancelbtn;
                                    EditText enterinput;

                                    titletv = view.findViewById(R.id.title);
                                    savebtn = view.findViewById(R.id.savebutton);
                                    cancelbtn = view.findViewById(R.id.cancelbutton);
                                    enterinput = view.findViewById(R.id.getinputtextd);

                                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewerProfile.this);
                                    alert.setView(view);
                                    alert.setCancelable(false);
                                    final AlertDialog alertDialog2 = alert.create();
                                    alertDialog2.show();
                                    alertDialog2.show();

                                    titletv.setText("Set Instagram Link");
                                    enterinput.setHint("Enter your link...");
                                    savebtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String status = enterinput.getText().toString().trim();
                                            if (!status.isEmpty())
                                            {
                                                if (NetworkUtils.isNetworkAvailable(ViewerProfile.this))
                                                {

                                                    DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Instagram");
                                                    loadstatus.setValue(status);
                                                    alertDialog2.dismiss();

                                                }
                                                else {
                                                    Toast.makeText(ViewerProfile.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                Toast.makeText(ViewerProfile.this, "Empty link!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    cancelbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog2.dismiss();
                                        }
                                    });
                                }
                                 else {
                                Toast.makeText(ViewerProfile.this, "You can't change anyone's link!", Toast.LENGTH_SHORT).show();
                            }
                            }

                        }
                    });
                }


            }
        });
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                {
                    View view = LayoutInflater.from(ViewerProfile.this).inflate(R.layout.customalertfinput,null);
                    TextView titletv,savebtn,cancelbtn,textgps;
                    EditText enterinput;

                    textgps = view.findViewById(R.id.gps);
                    titletv = view.findViewById(R.id.title);
                    savebtn = view.findViewById(R.id.savebutton);
                    cancelbtn = view.findViewById(R.id.cancelbutton);
                    enterinput = view.findViewById(R.id.getinputtextd);
                    savebtn.setText("Open");
                    cancelbtn.setText("Change");
                    enterinput.setVisibility(View.GONE);
                    textgps.setVisibility(View.VISIBLE);
                    textgps.setText("Loading link...");
                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewerProfile.this);
                    alert.setView(view);
                    alert.setCancelable(false);
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    //load link
                    final int[] x = {0};
                    DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Linkedin");
                    loadstatus.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                x[0] =1;
                                textgps.setText(snapshot.getValue(String.class));
                            }
                            else {
                                x[0] =1;
                                textgps.setText("No link to show!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    titletv.setText("Twitter");
                    savebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (x[0]==1)
                            {
                                if (textgps.getText().toString().trim().equals("No link to show!"))
                                {
                                    Toast.makeText(ViewerProfile.this, "No link to open!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    alertDialog.dismiss();
                                    DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Linkedin");
                                    loadstatus.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                openLinkInChrome(ViewerProfile.this,snapshot.getValue(String.class));
                                            }
                                            else {
                                                Toast.makeText(ViewerProfile.this, "not available!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }


                        }
                    });
                    cancelbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (x[0]==1)
                            {
                                alertDialog.dismiss();
                                if (key.equals(mykey))
                                {

                                    View view = LayoutInflater.from(ViewerProfile.this).inflate(R.layout.customalertfinput, null);
                                    TextView titletv, savebtn, cancelbtn;
                                    EditText enterinput;

                                    titletv = view.findViewById(R.id.title);
                                    savebtn = view.findViewById(R.id.savebutton);
                                    cancelbtn = view.findViewById(R.id.cancelbutton);
                                    enterinput = view.findViewById(R.id.getinputtextd);

                                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewerProfile.this);
                                    alert.setView(view);
                                    alert.setCancelable(false);
                                    final AlertDialog alertDialog2 = alert.create();
                                    alertDialog2.show();
                                    alertDialog2.show();

                                    titletv.setText("Set Twitter Link");
                                    enterinput.setHint("Enter your link...");
                                    savebtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String status = enterinput.getText().toString().trim();
                                            if (!status.isEmpty()) {
                                                if (NetworkUtils.isNetworkAvailable(ViewerProfile.this)) {

                                                    DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Linkedin");
                                                    loadstatus.setValue(status);
                                                    alertDialog2.dismiss();

                                                } else {
                                                    Toast.makeText(ViewerProfile.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(ViewerProfile.this, "Empty link!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    cancelbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog2.dismiss();
                                        }
                                    });
                                }
                                 else {
                                Toast.makeText(ViewerProfile.this, "You can't change anyone's link!", Toast.LENGTH_SHORT).show();
                            }
                            }

                        }
                    });
                }



            }
        });

        aboutbutton.setTextColor(Color.parseColor("#FF8C42"));
        aboutline.setBackgroundColor(Color.parseColor("#FF8C42"));
        layoutaboutme.setVisibility(View.VISIBLE);
        aboutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetAll();
                aboutbutton.setTextColor(Color.parseColor("#FF8C42"));
                aboutline.setBackgroundColor(Color.parseColor("#FF8C42"));
                layoutaboutme.setVisibility(View.VISIBLE);

            }
        });
        postsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetAll();
                postsbutton.setTextColor(Color.parseColor("#FF8C42"));
                postsline.setBackgroundColor(Color.parseColor("#FF8C42"));
                postslayout.setVisibility(View.VISIBLE);
            }
        });
        abouttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s = getSharedPreferences("USER", MODE_PRIVATE);

                if (key.equals(s.getString("key", ""))) {
                    {
                        View view = LayoutInflater.from(ViewerProfile.this).inflate(R.layout.customalertfinput,null);
                        TextView titletv,savebtn,cancelbtn;
                        EditText enterinput;

                        titletv = view.findViewById(R.id.title);
                        savebtn = view.findViewById(R.id.savebutton);
                        cancelbtn = view.findViewById(R.id.cancelbutton);
                        enterinput = view.findViewById(R.id.getinputtextd);

                        AlertDialog.Builder alert = new AlertDialog.Builder(ViewerProfile.this);
                        alert.setView(view);
                        alert.setCancelable(false);
                        final AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                        alertDialog.show();

                        titletv.setText("Set About");
                        enterinput.setHint("Enter here...");
                        savebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String status = enterinput.getText().toString().trim();
                                if (!status.isEmpty())
                                {
                                    if (NetworkUtils.isNetworkAvailable(ViewerProfile.this))
                                    {

                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("about");
                                        db.setValue(status);
                                        abouttext.setText(status);
                                        abouttext.setTextColor(Color.parseColor("#FFFFFF"));
                                        Toast.makeText(ViewerProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();

                                    }
                                    else {
                                        Toast.makeText(ViewerProfile.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(ViewerProfile.this, "Empty about!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        cancelbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });

        fellowmatesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ResetAll();
                fellowmatesbutton.setTextColor(Color.parseColor("#FF8C42"));
                fellowmatesline.setBackgroundColor(Color.parseColor("#FF8C42"));
                fellowmateslayout.setVisibility(View.VISIBLE);
            }
        });
        // follow
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sx90 =  getSharedPreferences("USER",MODE_PRIVATE);

                if (NetworkUtils.isNetworkAvailable(ViewerProfile.this))
                {
                    // add this in my following
                    String hisemail="";
                    String[] parts2 = email.split("@");
                    if (parts2.length > 0) {
                        hisemail = parts2[0];
                    }


                    DatabaseReference myfollowing = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(sx90.getString("key","")).child("Following")
                            .child(hisemail);
                    myfollowing.setValue("1");
                    // add me in his followers
                    // split my email
                    String myemail="";
                    String[] parts = sx90.getString("email","").split("@");
                    if (parts.length > 0) {
                        myemail = parts[0];
                    }

                    DatabaseReference hisfollower = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(key).child("Followers")
                            .child(myemail);
                    hisfollower.setValue("1");
                   follow.setVisibility(View.GONE);
                    following.setVisibility(View.VISIBLE);

                    // send noti
                    {
                        if (!attr.getMyEmail().equals(hisemail))
                        {
                            // if i liked anyother person post
                            // get his token
                            DatabaseReference userdb = FirebaseDatabase.getInstance().getReference().child("users");
                            String finalHisemail = hisemail;
                            userdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotc) {
                                    if (snapshotc.exists())
                                    {
                                        for (DataSnapshot dss: snapshotc.getChildren())
                                        {
                                            DatabaseReference emdb = userdb.child(dss.getKey()).child("email");
                                            emdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshotem) {
                                                    if (snapshotem.exists() && snapshotem.getValue(String.class).startsWith(finalHisemail))
                                                    {
                                                        String useridunique = dss.getKey();
                                                        // get token
                                                        DatabaseReference usertoken = userdb.child(useridunique).child("token");
                                                        usertoken.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshottoken) {
                                                                if (snapshottoken.exists())
                                                                {
                                                                    String token = snapshottoken.getValue(String.class);
                                                                    String message = attr.getMyname().concat(" started following you");
                                                                    String title = "New Follower";
                                                                    NotificationSender.sendNotification(token,title,message);

                                                                    DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                            .child(dss.getKey()).child("Notifications");
                                                                    DatabaseReference notificationdot = userthis.child("Notidot");
                                                                    notificationdot.setValue("1");

                                                                    DatabaseReference notikey = userthis.push();
                                                                    DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                    DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                    DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                    DatabaseReference type = notikey.child("type"); type.setValue("follow");
                                                                    DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());


                                                                }
                                                                else {

                                                                    String message = attr.getMyname().concat(" started following you");
                                                                    String title = "New Follower";
                                                                    DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                            .child(dss.getKey()).child("Notifications");
                                                                    DatabaseReference notificationdot = userthis.child("Notidot");
                                                                    notificationdot.setValue("1");

                                                                    DatabaseReference notikey = userthis.push();
                                                                    DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                    DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                    DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                    DatabaseReference type = notikey.child("type"); type.setValue("follow");
                                                                    DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
                else {
                    Toast.makeText(ViewerProfile.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
         following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sx90 =  getSharedPreferences("USER",MODE_PRIVATE);

                if (NetworkUtils.isNetworkAvailable(ViewerProfile.this))
                {
                    // add this in my following
                    String hisemail="";
                    String[] parts2 = email.split("@");
                    if (parts2.length > 0) {
                        hisemail = parts2[0];
                    }


                    DatabaseReference myfollowing = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(sx90.getString("key","")).child("Following")
                            .child(hisemail);
                    myfollowing.setValue(null);
                    // add me in his followers
                    // split my email
                    String myemail="";
                    String[] parts = sx90.getString("email","").split("@");
                    if (parts.length > 0) {
                        myemail = parts[0];
                    }

                    DatabaseReference hisfollower = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(key).child("Followers")
                            .child(myemail);
                    hisfollower.setValue(null);
                    follow.setVisibility(View.VISIBLE);
                    following.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(ViewerProfile.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static void openLinkInChrome(Context context, String url) {
         Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage("com.android.chrome");
        context.startActivity(intent);
    }

    public void loadPosts()
    {
        ArrayList<String> IMAGESURLS = new ArrayList<>();
        ArrayList<String> POSTIDS = new ArrayList<>();
        ArrayList<String> CAPTIONS = new ArrayList<>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        DatabaseReference useriddb = db.child(ds.getKey()).child("userid");


                        DatabaseReference imageref = db.child(ds.getKey()).child("imageUrl");
                        DatabaseReference caption = db.child(ds.getKey()).child("caption");


                        useriddb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotuserid) {
                                if (snapshotuserid.exists())
                                {
                                    String dbemail = snapshotuserid.getValue(String.class)+"@gmail.com";

                                    if (dbemail.equals(email))
                                    {
                                        // this post is current user's post
                                        // get caption
                                        imageref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshotimg) {
                                                if (snapshotimg.exists())
                                                {
                                                    caption.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshotcaption) {
                                                           if (snapshotcaption.exists())
                                                           {
                                                               IMAGESURLS.add(snapshotimg.getValue(String.class));
                                                               CAPTIONS.add(snapshotcaption.getValue(String.class));
                                                               POSTIDS.add(ds.getKey());

                                                               POSTLAYOUT postlayout = new POSTLAYOUT(ViewerProfile.this,IMAGESURLS,POSTIDS,CAPTIONS);
                                                               postsrecyclerview.setLayoutManager(new GridLayoutManager(ViewerProfile.this, 3));
                                                               postsrecyclerview.setAdapter(postlayout);

                                                           }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ResetAll()
    {
        aboutbutton.setTextColor(Color.parseColor("#FFFFFF"));
        aboutline.setBackgroundColor(Color.parseColor("#FFFFFF"));

        postsbutton.setTextColor(Color.parseColor("#FFFFFF"));
        postsline.setBackgroundColor(Color.parseColor("#FFFFFF"));

        fellowmatesbutton.setTextColor(Color.parseColor("#FFFFFF"));
        fellowmatesline.setBackgroundColor(Color.parseColor("#FFFFFF"));

        layoutaboutme.setVisibility(View.GONE);
        fellowmateslayout.setVisibility(View.GONE);
        postslayout.setVisibility(View.GONE);
    }
    public static int calculateAge(String dob) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calDOB = Calendar.getInstance();
        Calendar calToday = Calendar.getInstance();

        try {
            Date dateOfBirth = sdf.parse(dob);
            calDOB.setTime(dateOfBirth);

            int age = calToday.get(Calendar.YEAR) - calDOB.get(Calendar.YEAR);
            if (calToday.get(Calendar.DAY_OF_YEAR) < calDOB.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            // Return -1 or throw an exception to handle invalid date format
            return -1;
        }
    }
    public void LoadFollowing()
    {


        ArrayList<String> EMAILS = new ArrayList<>();
        ArrayList<String> IMAGES = new ArrayList<>();
        ArrayList<String> NAMES = new ArrayList<>();
        ArrayList<String> KEYS = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Following");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users");
                        user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                if (snapshot2.exists())
                                {
                                    for (DataSnapshot ds2: snapshot2.getChildren())
                                    {
                                        DatabaseReference emal = user.child(ds2.getKey()).child("email");
                                        emal.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshotemail) {
                                                if (snapshotemail.exists() && snapshotemail.getValue(String.class).startsWith(ds.getKey()))
                                                {
                                                    // he's follower
                                                    DatabaseReference imagedb = user.child(ds2.getKey()).child("profileimage");
                                                    DatabaseReference namedb = user.child(ds2.getKey()).child("name");
                                                    DatabaseReference usernamedb = user.child(ds2.getKey()).child("username");
                                                    imagedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshotimage) {
                                                            if (snapshotimage.exists())
                                                            {
                                                                namedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshotname) {
                                                                        if (snapshotname.exists())
                                                                        {
                                                                            usernamedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshotusername) {
                                                                                    if (snapshotusername.exists())
                                                                                    {

                                                                                        EMAILS.add(snapshotusername.getValue(String.class));
                                                                                        IMAGES.add(snapshotimage.getValue(String.class));
                                                                                        NAMES.add(snapshotname.getValue(String.class));
                                                                                        KEYS.add(ds2.getKey().toString());
                                                                                        numberfollowing.setText(String.valueOf(KEYS.size()));


                                                                                        SEARCHUSER searchuser1 = new SEARCHUSER(ViewerProfile.this,EMAILS,NAMES,KEYS,IMAGES);
                                                                                        following_recycler_view.setAdapter(searchuser1);
                                                                                        following_recycler_view.setVisibility(View.VISIBLE);
                                                                                        progressfollowing.setVisibility(View.GONE);

                                                                                    }else {
                                                                                        EMAILS.add("no username");
                                                                                        IMAGES.add(snapshotimage.getValue(String.class));
                                                                                        NAMES.add(snapshotname.getValue(String.class));
                                                                                        KEYS.add(ds2.getKey().toString());
                                                                                        numberfollowing.setText(String.valueOf(KEYS.size()));

                                                                                        SEARCHUSER searchuser1 = new SEARCHUSER(ViewerProfile.this,EMAILS,NAMES,KEYS,IMAGES);
                                                                                        following_recycler_view.setAdapter(searchuser1);
                                                                                        following_recycler_view.setVisibility(View.VISIBLE);
                                                                                        progressfollowing.setVisibility(View.GONE);

                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else {
                    numberfollowing.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Load(){
        LoadFollowers();
        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(key);
        DatabaseReference name = user.child("name");
        DatabaseReference status = user.child("status");
        DatabaseReference profileimg = user.child("profileimage");
        DatabaseReference about = user.child("about");
        DatabaseReference city = user.child("city");
        DatabaseReference country = user.child("country");
        DatabaseReference gender = user.child("gender");
        DatabaseReference dob = user.child("dob");

        dob.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    int age = calculateAge(snapshot.getValue(String.class));
                    userage.setText("("+String.valueOf(age)+" years old)");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        city.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotcity) {
                if (snapshotcity.exists())
                {
                    country.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotcountry) {
                            if (snapshotcountry.exists())
                            {
                                userlocation.setText("from "+snapshotcity.getValue(String.class)+", "+snapshotcountry.getValue(String.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        gender.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.getValue(String.class).equals("M"))
                    {
                        malefemalepic.setImageResource(R.drawable.male);
                    }
                    else if (snapshot.getValue(String.class).equals("F"))
                    {
                        malefemalepic.setImageResource(R.drawable.female);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        about.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotabout) {
                if (snapshotabout.exists())
                {
                    abouttext.setText(snapshotabout.getValue(String.class));
                    abouttext.setTextColor(Color.parseColor("#FFFFFF"));

                }
                else {
                    SharedPreferences s = getSharedPreferences("USER",MODE_PRIVATE);

                    if (key.equals(s.getString("key","")))
                    {
                        abouttext.setText("Nothing to show here...(Tap to enter)");

                    }
                    else {
                        abouttext.setText("Nothing to show here...");

                    }
                    abouttext.setTextColor(Color.parseColor("#2F2E2E"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        name.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotname) {
                if (snapshotname.exists())
                {
                   username.setText(snapshotname.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        status.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotstatus) {
                if (snapshotstatus.exists())
                {
                    userstatus.setText(snapshotstatus.getValue(String.class));
                }
                else
                {
                    userstatus.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profileimg.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotimg) {
                if (snapshotimg.exists())
                {
                    Glide.with(ViewerProfile.this).load(snapshotimg.getValue(String.class)).into(profilepic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // check follow and following
        String hisemail="";
        String[] parts = email.split("@");
        if (parts.length > 0) {
            hisemail = parts[0];
        }

        SharedPreferences sx90 =  getSharedPreferences("USER",MODE_PRIVATE);
        DatabaseReference my = FirebaseDatabase.getInstance().getReference().child("users")
                .child(sx90.getString("key","")).child("Following")
                .child(hisemail);
        my.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    // you are following this current person
                    follow.setVisibility(View.GONE);
                    following.setVisibility(View.VISIBLE);

                        if (key.equals(sx90.getString("key","")))
                        {
                             following.setVisibility(View.GONE);
                             follow.setVisibility(View.GONE);
                        }



                }
                else {
                    follow.setVisibility(View.VISIBLE);
                    following.setVisibility(View.GONE);
                    if (key.equals(sx90.getString("key","")))
                    {
                        following.setVisibility(View.GONE);
                        follow.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
    public void LoadFollowers()
    {
        ArrayList<String> EMAILS = new ArrayList<>();
        ArrayList<String> IMAGES = new ArrayList<>();
        ArrayList<String> NAMES = new ArrayList<>();
        ArrayList<String> KEYS = new ArrayList<>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(key).child("Followers");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users");
                        user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                if (snapshot2.exists())
                                {
                                    for (DataSnapshot ds2: snapshot2.getChildren())
                                    {
                                        DatabaseReference emal = user.child(ds2.getKey()).child("email");
                                        emal.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshotemail) {
                                                if (snapshotemail.exists() && snapshotemail.getValue(String.class).startsWith(ds.getKey()))
                                                {
                                                    // he's follower
                                                    DatabaseReference imagedb = user.child(ds2.getKey()).child("profileimage");
                                                    DatabaseReference namedb = user.child(ds2.getKey()).child("name");
                                                    DatabaseReference usernamedb = user.child(ds2.getKey()).child("username");
                                                    imagedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshotimage) {
                                                            if (snapshotimage.exists())
                                                            {
                                                                namedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshotname) {
                                                                        if (snapshotname.exists())
                                                                        {
                                                                            usernamedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshotusername) {
                                                                                    if (snapshotusername.exists())
                                                                                    {

                                                                                        EMAILS.add(snapshotusername.getValue(String.class));
                                                                                        IMAGES.add(snapshotimage.getValue(String.class));
                                                                                        NAMES.add(snapshotname.getValue(String.class));
                                                                                        KEYS.add(ds2.getKey().toString());
                                                                                        fellowmatesbutton.setText("Fellow mates "+String.valueOf(NAMES.size()));


                                                                                        SEARCHUSER searchuser1 = new SEARCHUSER(ViewerProfile.this,EMAILS,NAMES,KEYS,IMAGES);
                                                                                        fellowmatessrecyclerview.setAdapter(searchuser1);
                                                                                        fellowmatessrecyclerview.setVisibility(View.VISIBLE);

                                                                                    }else {
                                                                                        EMAILS.add("no username");
                                                                                        IMAGES.add(snapshotimage.getValue(String.class));
                                                                                        NAMES.add(snapshotname.getValue(String.class));
                                                                                        KEYS.add(ds2.getKey().toString());
                                                                                        fellowmatesbutton.setText("Fellow mates "+String.valueOf(NAMES.size()));

                                                                                        SEARCHUSER searchuser1 = new SEARCHUSER(ViewerProfile.this,EMAILS,NAMES,KEYS,IMAGES);
                                                                                        fellowmatessrecyclerview.setAdapter(searchuser1);
                                                                                        fellowmatessrecyclerview.setVisibility(View.VISIBLE);
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });
                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else {
                    fellowmatesbutton.setText("Fellow mates 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

class POSTLAYOUT extends RecyclerView.Adapter<POSTLAYOUT.POSTLAYOUTCHILD>
{
    Context c;
    ArrayList<String> IMAGESURLS = new ArrayList<>();
    ArrayList<String> POSTIDS = new ArrayList<>();
    ArrayList<String> CAPTIONS = new ArrayList<>();

    public POSTLAYOUT(Context c, ArrayList<String> IMAGESURLS, ArrayList<String> POSTIDS, ArrayList<String> CAPTIONS) {
        this.c = c;
        this.IMAGESURLS = IMAGESURLS;
        this.POSTIDS = POSTIDS;
        this.CAPTIONS = CAPTIONS;
    }

    @NonNull
    @Override
    public POSTLAYOUTCHILD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.postlayoutprofile,parent,false);
        return new POSTLAYOUTCHILD(v);
    }

    @Override
    public void onBindViewHolder(@NonNull POSTLAYOUTCHILD holder, @SuppressLint("RecyclerView") int position) {
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,ViewPost.class);
                i.putExtra("postid", POSTIDS.get(position));
                c.startActivity(i);
            }
        });
        if (!IMAGESURLS.get(position).equals("-null-"))
        {
            holder.img.setVisibility(View.VISIBLE);
            Glide.with(c).load(IMAGESURLS.get(position)).into(holder.img);
        }
        else {
            holder.img.setVisibility(View.GONE);
        }
        if (!CAPTIONS.get(position).equals("-null-"))
        {
            holder.caption.setVisibility(View.VISIBLE);
            holder.caption.setText(CAPTIONS.get(position));
        }
        else {
            holder.caption.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return POSTIDS.size();
    }

    static class POSTLAYOUTCHILD extends RecyclerView.ViewHolder{
        ImageView img;
        TextView caption;
        FrameLayout layout;
        public POSTLAYOUTCHILD(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.img);
            caption = v.findViewById(R.id.caption);
            layout = v.findViewById(R.id.layout);
        }
    }
}