package com.packages.scompass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewPost extends AppCompatActivity {
    String postid;
    ProgressBar loadingposts;
    RecyclerView recyclerView;
    static RecyclerView comments_recycler_view;
    static FrameLayout CommentLayout;
    static ImageView hidecomments;
    static TextView comment_replytext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        loadingposts  = findViewById(R.id.loadingposts);
        recyclerView  = findViewById(R.id.recyclerView);
        comments_recycler_view = findViewById(R.id.comments_recycler_view);
        CommentLayout = findViewById(R.id.CommentLayout);
        hidecomments = findViewById(R.id.hidecomments);
        comment_replytext = findViewById(R.id.comment_replytext);

        Intent i = getIntent();
        postid = i.getStringExtra("postid");
        LoadPosts();

        hidecomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentLayout.setVisibility(View.GONE);
            }
        });

    }
    public void LoadPosts()
    {

        final ArrayList<String>[] POSTID = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] POSTIMAGE = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] PROFILEPHOTO = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] CAPTION = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] USERNAME = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] USERID = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] CITY = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] STATE = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] COUNTRY = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] TIME = new ArrayList[]{new ArrayList<>()};
        final ArrayList<String>[] KEY = new ArrayList[]{new ArrayList<>()};

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(postid);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {

                        DatabaseReference captiondb = db.child("caption");
                        DatabaseReference usernamedb = db.child("username");
                        DatabaseReference userid = db.child("userid");
                        DatabaseReference dbcity = db.child("city");
                        DatabaseReference dbstate = db.child("state");
                        DatabaseReference dbcountry = db.child("country");
                        DatabaseReference dbtime = db.child("time");
                        DatabaseReference dbkey = db.child("key");

                        dbkey.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotkey) {
                                if (snapshotkey.exists())
                                {

                                    DatabaseReference profilephotodb = FirebaseDatabase.getInstance()
                                            .getReference().child("users").child(snapshotkey.getValue(String.class))
                                            .child("profileimage");
                                    dbtime.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshottime) {
                                            if (snapshottime.exists())
                                            {
                                                dbcity.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshotcity) {
                                                        if (snapshotcity.exists())
                                                        {
                                                            dbstate.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshotstate) {
                                                                    if (snapshotstate.exists())
                                                                    {
                                                                        dbcountry.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshotcountry) {
                                                                                if (snapshotcountry.exists())
                                                                                {
                                                                                    userid.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshotuserid) {
                                                                                            if (snapshotuserid.exists()){
                                                                                                profilephotodb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot snapshotprofileimg) {
                                                                                                        if (snapshotprofileimg.exists())
                                                                                                        {
                                                                                                            captiondb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshotcaption) {
                                                                                                                    if (snapshotcaption.exists())
                                                                                                                    {
                                                                                                                        usernamedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                            @Override
                                                                                                                            public void onDataChange(@NonNull DataSnapshot snapshotusername) {
                                                                                                                                if (snapshotusername.exists())
                                                                                                                                {

                                                                                                                                    POSTID[0].add( postid);
                                                                                                                                    USERID[0].add( snapshotuserid.getValue(String.class));
                                                                                                                                    POSTIMAGE[0].add("-null-");
                                                                                                                                    PROFILEPHOTO[0].add( snapshotprofileimg.getValue(String.class));
                                                                                                                                    CAPTION[0].add( snapshotcaption.getValue(String.class));
                                                                                                                                    USERNAME[0].add( snapshotusername.getValue(String.class));
                                                                                                                                    STATE[0].add(snapshotstate.getValue(String.class));
                                                                                                                                    COUNTRY[0].add( snapshotcountry.getValue(String.class));
                                                                                                                                    CITY[0].add(snapshotcity.getValue(String.class));
                                                                                                                                    TIME[0].add( snapshottime.getValue(String.class));
                                                                                                                                    KEY[0].add( snapshotkey.getValue(String.class));

                                                                                                                                    {

// Parse the TIME values into Date objects
                                                                                                                                        List<Date> timeList = new ArrayList<>();
                                                                                                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.getDefault());
                                                                                                                                        for (String timeString : TIME[0]) {
                                                                                                                                            try {
                                                                                                                                                Date timeDate = dateFormat.parse(timeString);
                                                                                                                                                timeList.add(timeDate);
                                                                                                                                            } catch (
                                                                                                                                                    ParseException e) {
                                                                                                                                                e.printStackTrace();
                                                                                                                                            }
                                                                                                                                        }

// Sort the lists based on the parsed TIME values
                                                                                                                                        Collections.sort(timeList, new Comparator<Date>() {
                                                                                                                                            @Override
                                                                                                                                            public int compare(Date date1, Date date2) {
                                                                                                                                                return date2.compareTo(date1); // Sort in descending order (newest first)
                                                                                                                                            }
                                                                                                                                        });

// Rearrange other lists based on the sorted TIME values
                                                                                                                                        ArrayList<String> sortedPOSTID = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedUSERID = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedPOSTIMAGE = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedPROFILEPHOTO = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedCAPTION = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedUSERNAME = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedSTATE = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedCOUNTRY = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedCITY = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedTIME = new ArrayList<>();
                                                                                                                                        ArrayList<String> sortedKEY = new ArrayList<>();

                                                                                                                                        for (Date timeDate : timeList) {
                                                                                                                                            String timeString = dateFormat.format(timeDate);
                                                                                                                                            int index = TIME[0].indexOf(timeString);
                                                                                                                                            sortedPOSTID.add(POSTID[0].get(index));
                                                                                                                                            sortedUSERID.add(USERID[0].get(index));
                                                                                                                                            sortedPOSTIMAGE.add(POSTIMAGE[0].get(index));
                                                                                                                                            sortedPROFILEPHOTO.add(PROFILEPHOTO[0].get(index));
                                                                                                                                            sortedCAPTION.add(CAPTION[0].get(index));
                                                                                                                                            sortedUSERNAME.add(USERNAME[0].get(index));
                                                                                                                                            sortedSTATE.add(STATE[0].get(index));
                                                                                                                                            sortedCOUNTRY.add(COUNTRY[0].get(index));
                                                                                                                                            sortedCITY.add(CITY[0].get(index));
                                                                                                                                            sortedTIME.add(TIME[0].get(index));
                                                                                                                                            sortedKEY.add(KEY[0].get(index));
                                                                                                                                        }

// Update the original lists with the sorted values
                                                                                                                                        POSTID[0] = sortedPOSTID;
                                                                                                                                        USERID[0] = sortedUSERID;
                                                                                                                                        POSTIMAGE[0] = sortedPOSTIMAGE;
                                                                                                                                        PROFILEPHOTO[0] = sortedPROFILEPHOTO;
                                                                                                                                        CAPTION[0] = sortedCAPTION;
                                                                                                                                        USERNAME[0] = sortedUSERNAME;
                                                                                                                                        STATE[0] = sortedSTATE;
                                                                                                                                        COUNTRY[0] = sortedCOUNTRY;
                                                                                                                                        CITY[0] = sortedCITY;
                                                                                                                                        TIME[0] = sortedTIME;
                                                                                                                                        KEY[0] = sortedKEY;
                                                                                                                                    }

                                                                                                                                    SetPost setPost = new SetPost(ViewPost.this,
                                                                                                                                            POSTID[0],
                                                                                                                                            POSTIMAGE[0],
                                                                                                                                            PROFILEPHOTO[0],
                                                                                                                                            CAPTION[0],
                                                                                                                                            USERNAME[0], USERID[0], CITY[0], STATE[0], COUNTRY[0], TIME[0], KEY[0],"2");
                                                                                                                                    setPost.notifyDataSetChanged();
                                                                                                                                    recyclerView.setAdapter(setPost);
                                                                                                                                    loadingposts.setVisibility(View.GONE);

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

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                }
                else {
                    Toast.makeText(ViewPost.this, "Oops, Post deleted!", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        if (CommentLayout.getVisibility() == View.VISIBLE) {
            CommentLayout.setVisibility(View.GONE);
        }
        else {
            finish();
        }
    }
}