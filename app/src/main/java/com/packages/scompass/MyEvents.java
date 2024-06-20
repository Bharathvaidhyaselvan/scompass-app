package com.packages.scompass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyEvents extends AppCompatActivity {
    LinearLayout noeventslayout,createeventlayout;
    RecyclerView eventsrecyclerview;
    TextView title;
    CardView createnewevent;
    MyAttr attr;
    Uri selectedImageUri;

    String myemailwithoutat="",myfirebasekey="",myname="";
    EditText description,city,country;
    TextView fromdate,todate,uploadimagebutton;
    ImageView tourimage;
    CardView tourimagecard,createtour;
    TextView alleventstext,createdeventstext;

    SimpleDateFormat sdf1,sdf2;
    ProgressBar p1;
    String picked="0";
    Boolean isImageUploaded = false;
    ArrayList<String> EVENTKEY = new ArrayList<>();
    ArrayList<String> NAME = new ArrayList<>();
    ArrayList<String> EMAIL = new ArrayList<>();
    ArrayList<String> USERKEY = new ArrayList<>();
    ArrayList<String> DESCRIPTION = new ArrayList<>();
    ArrayList<String> CITY = new ArrayList<>();
    ArrayList<String> COUNTRY = new ArrayList<>();
    ArrayList<String> FROMDATE = new ArrayList<>();
    ArrayList<String> TODATE = new ArrayList<>();
    ArrayList<String> IMAGEURL = new ArrayList<>();
    ArrayList<String> CREATEDON = new ArrayList<>();
    boolean instant = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        noeventslayout = findViewById(R.id.noeventslayout);
        createdeventstext = findViewById(R.id.createdeventstext);
        alleventstext = findViewById(R.id.alleventstext);
        eventsrecyclerview = findViewById(R.id.eventsrecyclerview);
        createeventlayout = findViewById(R.id.createeventlayout);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        city = findViewById(R.id.city);
        fromdate = findViewById(R.id.fromdate);
        todate = findViewById(R.id.todate);
        tourimage = findViewById(R.id.tourimage);
        country = findViewById(R.id.country);
        tourimagecard = findViewById(R.id.tourimagecard);
        uploadimagebutton = findViewById(R.id.uploadimagebutton);
        createtour = findViewById(R.id.createtour);
        p1 = findViewById(R.id.p1);

        createnewevent = findViewById(R.id.createnewevent);
        attr = new MyAttr(MyEvents.this);


        Intent i = getIntent();
        if (i!=null)
        {
            if (i.getBooleanExtra("instant",false))
            {
                instant = true;
                createeventlayout.setVisibility(View.VISIBLE);
                noeventslayout.setVisibility(View.GONE);
                eventsrecyclerview.setVisibility(View.GONE);
                createnewevent.setVisibility(View.GONE);
                alleventstext.setVisibility(View.GONE);
                createdeventstext.setVisibility(View.GONE);
                title.setText("Create a trip");
            }  else {
                InitializeData();
            }
        }
        else {
            InitializeData();
        }

        alleventstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                alleventstext.setTextColor(Color.parseColor("#FF8C42"));
                LoadEvents("1");
            }
        });
        createdeventstext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                createdeventstext.setTextColor(Color.parseColor("#FF8C42"));
                LoadEvents("2");

            }
        });
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picked="1";
                showDateTimePicker();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picked="2";
                showDateTimePicker();
            }
        });

        createnewevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createeventlayout.setVisibility(View.VISIBLE);
                noeventslayout.setVisibility(View.GONE);
                eventsrecyclerview.setVisibility(View.GONE);
                createnewevent.setVisibility(View.GONE);
                alleventstext.setVisibility(View.GONE);
                createdeventstext.setVisibility(View.GONE);
                title.setText("Create a trip");
            }
        });
        uploadimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        createtour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (description.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(MyEvents.this, "Please enter description!", Toast.LENGTH_SHORT).show();
                    description.setError("Field required is empty!");
                }
                else if (city.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(MyEvents.this, "Please enter city name!", Toast.LENGTH_SHORT).show();
                    city.setError("Field required is empty!");
                }
                else if (country.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(MyEvents.this, "Please enter country name!", Toast.LENGTH_SHORT).show();
                    country.setError("Field required is empty!");
                }
                else if (fromdate.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(MyEvents.this, "Please pick starting date!", Toast.LENGTH_SHORT).show();
                    fromdate.setError("Field required is empty!");
                }
                else if (todate.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(MyEvents.this, "Please pick ending date!", Toast.LENGTH_SHORT).show();
                    todate.setError("Field required is empty!");
                }
                else if (isImageUploaded)
                {
                    String startTimeString = fromdate.getText().toString().trim();
                    String endTimeString = todate.getText().toString().trim();
                    boolean isStartTimeBeforeEndTime = isStartTimeBeforeEndTime(startTimeString, endTimeString, sdf1, sdf2);
                    if (isStartTimeBeforeEndTime)
                    {
                        if (NetworkUtils.isNetworkAvailable(MyEvents.this))
                        {
                            createtour.setEnabled(false);
                            createtour.setAlpha(0.3F);
                            p1.setVisibility(View.VISIBLE);
                            //upload data
                            DatabaseReference events = FirebaseDatabase.getInstance().getReference().child("Events").push();
                            String key = events.getKey();
                            DatabaseReference addecentkey = FirebaseDatabase.getInstance().getReference().child("users").child(myfirebasekey).child("Events").child(key);

                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Events").child(key);
                            storageRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();


                                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
                                    DatabaseReference namedb = events.child("Name"); namedb.setValue(myname);
                                    DatabaseReference emaildb = events.child("Email"); emaildb.setValue(myemailwithoutat);
                                    DatabaseReference userkeydb = events.child("Userkey"); userkeydb.setValue(myfirebasekey);
                                    DatabaseReference descriptiondb = events.child("Description"); descriptiondb.setValue(description.getText().toString().trim());
                                    DatabaseReference citydb = events.child("City"); citydb.setValue(city.getText().toString().trim());
                                    DatabaseReference countrydb = events.child("Country"); countrydb.setValue(country.getText().toString().trim());
                                    DatabaseReference fromdatedb = events.child("Fromdate"); fromdatedb.setValue(fromdate.getText().toString().trim());
                                    DatabaseReference todatedb = events.child("Todate"); todatedb.setValue(todate.getText().toString().trim());
                                    DatabaseReference imageuridb = events.child("Imageuri"); imageuridb.setValue(imageUrl);
                                    DatabaseReference createdon = events.child("Createdon"); createdon.setValue(timestamp);
                                    addecentkey.setValue("1");

                                    DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("Events").child(key)
                                            .child("TotalMembers");
                                    DatabaseReference db2 = FirebaseDatabase.getInstance().getReference().child("Events").child(key)
                                            .child("Members").child(myfirebasekey);
                                    db1.setValue("1");
                                    db2.setValue("1");


                                    // send noti
                                    {
                                        {
                                            // if i liked anyother person post
                                            // get his token
                                            DatabaseReference userdb = FirebaseDatabase.getInstance().getReference().child("users");
                                            userdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshotc) {
                                                    if (snapshotc.exists())
                                                    {
                                                        for (DataSnapshot dss: snapshotc.getChildren())
                                                        {
                                                            // get token
                                                            if (!dss.getKey().toString().equals(attr.getMykey()))
                                                            {
                                                                DatabaseReference usertoken = userdb.child(dss.getKey()).child("token");
                                                                usertoken.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshottoken) {
                                                                        if (snapshottoken.exists())
                                                                        {
                                                                            String token = snapshottoken.getValue(String.class);
                                                                            String message = attr.getMyname().concat(" posted a new Event");
                                                                            String title = "New Event";
                                                                            NotificationSender.sendNotification(token,title,message);

                                                                            DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                    .child(dss.getKey()).child("Notifications");
                                                                            DatabaseReference notificationdot = userthis.child("Notidot");
                                                                            notificationdot.setValue("1");

                                                                            DatabaseReference notikey = userthis.push();
                                                                            DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                            DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                            DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                            DatabaseReference type = notikey.child("type"); type.setValue("event");
                                                                            DatabaseReference eventidd = notikey.child("eventid"); eventidd.setValue(events.getKey().toString());


                                                                        }
                                                                        else {

                                                                            String message = attr.getMyname().concat(" posted a new Event");
                                                                            String title = "New Event";
                                                                            DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                    .child(dss.getKey()).child("Notifications");
                                                                            DatabaseReference notificationdot = userthis.child("Notidot");
                                                                            notificationdot.setValue("1");

                                                                            DatabaseReference notikey = userthis.push();
                                                                            DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                            DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                            DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                            DatabaseReference type = notikey.child("type"); type.setValue("event");
                                                                            DatabaseReference eventidd = notikey.child("eventid"); eventidd.setValue(events.getKey().toString());


                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                            }

                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    Toast.makeText(MyEvents.this, "Event Created Successfully...", Toast.LENGTH_SHORT).show();
                                    createeventlayout.setVisibility(View.GONE);
                                    createeventlayout.setVisibility(View.GONE);
                                    noeventslayout.setVisibility(View.GONE);
                                    eventsrecyclerview.setVisibility(View.VISIBLE);
                                    createnewevent.setVisibility(View.VISIBLE);
                                    title.setText("My Events");
                                    p1.setVisibility(View.GONE);
                                    startActivity(new Intent(MyEvents.this,MyEvents.class));
                                    finish();


                                });
                            }).addOnFailureListener(e -> {
                                createtour.setEnabled(true);
                                createtour.setAlpha(1.0F);
                                Toast.makeText(MyEvents.this, "Failed to upload image! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                        }
                        else {
                            createtour.setEnabled(true);
                            createtour.setAlpha(1.0F);
                            Toast.makeText(MyEvents.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(MyEvents.this, "Please upload an image!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    public void reset()
    {
        alleventstext.setTextColor(Color.parseColor("#888888"));
        createdeventstext.setTextColor(Color.parseColor("#888888"));
        eventsrecyclerview.setVisibility(View.GONE);
    }
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            tourimage.setImageURI(selectedImageUri);
            tourimagecard.setVisibility(View.VISIBLE);
            isImageUploaded =true;
        }
    }

    public void LoadEvents(String allcreated)
    {
        if (EVENTKEY.isEmpty())
        {
            DatabaseReference events2 = FirebaseDatabase.getInstance().getReference().child("users")
                    .child(myfirebasekey).child("Events");
            events2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        noeventslayout.setVisibility(View.GONE);
                        createeventlayout.setVisibility(View.GONE);
                        for (DataSnapshot ds: snapshot.getChildren())
                        {
                            DatabaseReference eventkey = FirebaseDatabase.getInstance().getReference().child("Events")
                                    .child(ds.getKey());
                            eventkey.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {
                                        DatabaseReference events = eventkey;

                                        DatabaseReference namedb = events.child("Name");
                                        DatabaseReference emaildb = events.child("Email");
                                        DatabaseReference userkeydb = events.child("Userkey");
                                        DatabaseReference descriptiondb = events.child("Description");
                                        DatabaseReference citydb = events.child("City");
                                        DatabaseReference countrydb = events.child("Country");
                                        DatabaseReference fromdatedb = events.child("Fromdate");
                                        DatabaseReference todatedb = events.child("Todate");
                                        DatabaseReference imageuridb = events.child("Imageuri");
                                        DatabaseReference createdon = events.child("Createdon");

                                        namedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshotname) {
                                                if (snapshotname.exists())
                                                {
                                                    emaildb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshotemail) {
                                                            if (snapshotemail.exists())
                                                            {
                                                                userkeydb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshotuserkey) {
                                                                        if (snapshotuserkey.exists())
                                                                        {
                                                                            descriptiondb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshotdescription) {
                                                                                    if (snapshotdescription.exists())
                                                                                    {
                                                                                        citydb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshotcity) {
                                                                                                if (snapshotcity.exists())
                                                                                                {
                                                                                                    countrydb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshotcountry) {
                                                                                                            if (snapshotcountry.exists())
                                                                                                            {
                                                                                                                fromdatedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onDataChange(@NonNull DataSnapshot snapshotfromdate) {
                                                                                                                        if (snapshotfromdate.exists())
                                                                                                                        {
                                                                                                                            todatedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshottodate) {
                                                                                                                                    if (snapshottodate.exists())
                                                                                                                                    {
                                                                                                                                        imageuridb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onDataChange(@NonNull DataSnapshot snapshotimageurl) {
                                                                                                                                                if (snapshotimageurl.exists())
                                                                                                                                                {
                                                                                                                                                    createdon.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshotcreatedon) {
                                                                                                                                                            if (snapshotcreatedon.exists())
                                                                                                                                                            {
                                                                                                                                                                if (allcreated.equals("1"))
                                                                                                                                                                 {
                                                                                                                                                                    eventsrecyclerview.setVisibility(View.VISIBLE);
                                                                                                                                                                    EVENTKEY.add(0,ds.getKey());
                                                                                                                                                                    NAME.add(0,snapshotname.getValue(String.class));
                                                                                                                                                                    EMAIL.add(0,snapshotemail.getValue(String.class));
                                                                                                                                                                    USERKEY.add(0,snapshotuserkey.getValue(String.class));
                                                                                                                                                                    DESCRIPTION.add(0,snapshotdescription.getValue(String.class));
                                                                                                                                                                    CITY.add(0,snapshotcity.getValue(String.class));
                                                                                                                                                                    COUNTRY.add(0,snapshotcountry.getValue(String.class));
                                                                                                                                                                    FROMDATE.add(0,snapshotfromdate.getValue(String.class));
                                                                                                                                                                    TODATE.add(0,snapshottodate.getValue(String.class));
                                                                                                                                                                    IMAGEURL.add(0,snapshotimageurl.getValue(String.class));
                                                                                                                                                                    CREATEDON.add(0,snapshotcreatedon.getValue(String.class));

                                                                                                                                                                    EVENTS_ADAPTER eventsAdapter = new EVENTS_ADAPTER(MyEvents.this,
                                                                                                                                                                            EVENTKEY,
                                                                                                                                                                            NAME,
                                                                                                                                                                            EMAIL,
                                                                                                                                                                            USERKEY,
                                                                                                                                                                            DESCRIPTION,
                                                                                                                                                                            CITY,
                                                                                                                                                                            COUNTRY,
                                                                                                                                                                            FROMDATE,
                                                                                                                                                                            TODATE,
                                                                                                                                                                            IMAGEURL,
                                                                                                                                                                            CREATEDON);
                                                                                                                                                                     alleventstext.setVisibility(View.VISIBLE);
                                                                                                                                                                     createdeventstext.setVisibility(View.VISIBLE);
                                                                                                                                                                     eventsrecyclerview.setVisibility(View.VISIBLE);

                                                                                                                                                                     eventsrecyclerview.setAdapter(eventsAdapter);
                                                                                                                                                                }
                                                                                                                                                                else if (allcreated.equals("2"))
                                                                                                                                                                {
                                                                                                                                                                    if (attr.getMykey().equals(snapshotuserkey.getValue(String.class)))
                                                                                                                                                                    {

                                                                                                                                                                        eventsrecyclerview.setVisibility(View.VISIBLE);
                                                                                                                                                                        EVENTKEY.add(0,ds.getKey());
                                                                                                                                                                        NAME.add(0,snapshotname.getValue(String.class));
                                                                                                                                                                        EMAIL.add(0,snapshotemail.getValue(String.class));
                                                                                                                                                                        USERKEY.add(0,snapshotuserkey.getValue(String.class));
                                                                                                                                                                        DESCRIPTION.add(0,snapshotdescription.getValue(String.class));
                                                                                                                                                                        CITY.add(0,snapshotcity.getValue(String.class));
                                                                                                                                                                        COUNTRY.add(0,snapshotcountry.getValue(String.class));
                                                                                                                                                                        FROMDATE.add(0,snapshotfromdate.getValue(String.class));
                                                                                                                                                                        TODATE.add(0,snapshottodate.getValue(String.class));
                                                                                                                                                                        IMAGEURL.add(0,snapshotimageurl.getValue(String.class));
                                                                                                                                                                        CREATEDON.add(0,snapshotcreatedon.getValue(String.class));

                                                                                                                                                                        EVENTS_ADAPTER eventsAdapter = new EVENTS_ADAPTER(MyEvents.this,
                                                                                                                                                                                EVENTKEY,
                                                                                                                                                                                NAME,
                                                                                                                                                                                EMAIL,
                                                                                                                                                                                USERKEY,
                                                                                                                                                                                DESCRIPTION,
                                                                                                                                                                                CITY,
                                                                                                                                                                                COUNTRY,
                                                                                                                                                                                FROMDATE,
                                                                                                                                                                                TODATE,
                                                                                                                                                                                IMAGEURL,
                                                                                                                                                                                CREATEDON);
                                                                                                                                                                        alleventstext.setVisibility(View.VISIBLE);
                                                                                                                                                                        createdeventstext.setVisibility(View.VISIBLE);
                                                                                                                                                                        eventsrecyclerview.setVisibility(View.VISIBLE);
                                                                                                                                                                        eventsrecyclerview.setAdapter(eventsAdapter);
                                                                                                                                                                    }
                                                                                                                                                                }


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
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                    else {
                                        DatabaseReference df = events2.child(ds.getKey().toString().trim());
                                        df.setValue(null);
                                        createdeventstext.setVisibility(View.GONE);
                                        alleventstext.setVisibility(View.GONE);
                                        eventsrecyclerview.setVisibility(View.GONE);
                                        noeventslayout.setVisibility(View.VISIBLE);
                                        createeventlayout.setVisibility(View.GONE);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                    else {
                        createdeventstext.setVisibility(View.GONE);
                        alleventstext.setVisibility(View.GONE);
                        eventsrecyclerview.setVisibility(View.GONE);
                        noeventslayout.setVisibility(View.VISIBLE);
                        createeventlayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            if (allcreated.equals("1"))
            {
                EVENTS_ADAPTER eventsAdapter = new EVENTS_ADAPTER(MyEvents.this,
                        EVENTKEY,
                        NAME,
                        EMAIL,
                        USERKEY,
                        DESCRIPTION,
                        CITY,
                        COUNTRY,
                        FROMDATE,
                        TODATE,
                        IMAGEURL,
                        CREATEDON);
                eventsrecyclerview.setVisibility(View.VISIBLE);
                eventsrecyclerview.setAdapter(eventsAdapter);
                createdeventstext.setVisibility(View.VISIBLE);
                alleventstext.setVisibility(View.VISIBLE);
            }
            else if (allcreated.equals("2") && USERKEY.contains(attr.getMykey()))
            {


                ArrayList<String> EVENTKEYx = new ArrayList<>();
                ArrayList<String> NAMEx = new ArrayList<>();
                ArrayList<String> EMAILx = new ArrayList<>();
                ArrayList<String> USERKEYx = new ArrayList<>();
                ArrayList<String> DESCRIPTIONx = new ArrayList<>();
                ArrayList<String> CITYx = new ArrayList<>();
                ArrayList<String> COUNTRYx = new ArrayList<>();
                ArrayList<String> FROMDATEx = new ArrayList<>();
                ArrayList<String> TODATEx = new ArrayList<>();
                ArrayList<String> IMAGEURLx = new ArrayList<>();
                ArrayList<String> CREATEDONx = new ArrayList<>();

                for (int i=0;i< EVENTKEY.size();i++)
                {
                    if (USERKEY.get(i).equals(attr.getMykey()))
                    {
                        EVENTKEYx.add(EVENTKEY.get(i));
                        NAMEx.add(NAME.get(i));
                        EMAILx.add(EMAIL.get(i));
                        USERKEYx.add(USERKEY.get(i));
                        DESCRIPTIONx.add(DESCRIPTION.get(i));
                        CITYx.add(CITY.get(i));
                        COUNTRYx.add(COUNTRY.get(i));
                        FROMDATEx.add(FROMDATE.get(i));
                        TODATEx.add(TODATE.get(i));
                        IMAGEURLx.add(IMAGEURL.get(i));
                        CREATEDONx.add(CREATEDON.get(i));

                        EVENTS_ADAPTER eventsAdapter = new EVENTS_ADAPTER(MyEvents.this,
                                EVENTKEYx,
                                NAMEx,
                                EMAILx,
                                USERKEYx,
                                DESCRIPTIONx,
                                CITYx,
                                COUNTRYx,
                                FROMDATEx,
                                TODATEx,
                                IMAGEURLx,
                                CREATEDONx);
                        eventsrecyclerview.setVisibility(View.VISIBLE);
                        eventsrecyclerview.setAdapter(eventsAdapter);
                        createdeventstext.setVisibility(View.VISIBLE);
                        alleventstext.setVisibility(View.VISIBLE);
                    }
                }


            }
            else {
                Toast.makeText(this, "No events to show!", Toast.LENGTH_SHORT).show();
            }


        }
    }
    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();

        // Show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Show TimePickerDialog after selecting a date
                        showTimePicker(calendar);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set a minimum date to the current date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    public   boolean isStartTimeBeforeEndTime(String startTimeString, String endTimeString,
                                                   SimpleDateFormat sdf1, SimpleDateFormat sdf2) {
        try {
            // Parse the start and end time strings into Date objects
            Date startTime = sdf1.parse(startTimeString);
            Date endTime = sdf2.parse(endTimeString);

            // Compare the Date objects
            assert startTime != null;
            return startTime.before(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MyEvents.this, "Error!", Toast.LENGTH_SHORT).show();
            // Handle the parsing exception as needed
            return false; // For example, you might consider invalid input as "start time is not before end time"
        }
    }

    private void showTimePicker(final Calendar calendar) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        // Check if the selected date and time are not before the current date and time
                        if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                            // Format the selected date and time
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            sdf1=sdf2=dateFormat;
                            String formattedDateTime = dateFormat.format(calendar.getTime());
                            if (picked.equals("1"))
                            {
                                fromdate.setText(formattedDateTime);
                            }
                            else if (picked.equals("2"))
                            {
                                todate.setText(formattedDateTime);

                            }
                            // Now 'formattedDateTime' contains the selected date and time as a string
                            // You can use it as needed
                            // For example, you might want to display it in a TextView:
                            // textView.setText(formattedDateTime);
                        } else {
                            // Display an error message or handle the invalid selection
                            Toast.makeText(MyEvents.this, "Invalid starting time!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24-hour format
        );

        timePickerDialog.show();
    } 
    public void InitializeData()
    {

        title.setText("My Events");
        createnewevent.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Loading events...", Toast.LENGTH_SHORT).show();
        myemailwithoutat = attr.getMyEmail();
        myfirebasekey = attr.getMykey();
        myname = attr.getMyname();


        DatabaseReference events2 = FirebaseDatabase.getInstance().getReference().child("users")
                .child(myfirebasekey).child("Events");
        events2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    noeventslayout.setVisibility(View.GONE);
                    createeventlayout.setVisibility(View.GONE);
                    reset();
                    alleventstext.setTextColor(Color.parseColor("#FF8C42"));
                    LoadEvents("1");
                }
                else {
                    eventsrecyclerview.setVisibility(View.GONE);
                    noeventslayout.setVisibility(View.VISIBLE);
                    createeventlayout.setVisibility(View.GONE);
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
        if (instant)
        {
            finish();
        }
        else {
            if (createeventlayout.getVisibility()==View.VISIBLE)
            {
                createeventlayout.setVisibility(View.GONE);
                createnewevent.setVisibility(View.VISIBLE);
                reset();
                alleventstext.setTextColor(Color.parseColor("#FF8C42"));
                LoadEvents("1");
            }
            else {
                finish();
            }
        }
    }
}
class EVENTS_ADAPTER extends  RecyclerView.Adapter<EVENTS_ADAPTER.EVENTS_ADAPTER_CHILD>
{
    Context c;
    ArrayList<String> EVENTKEY = new ArrayList<>();
    ArrayList<String> NAME = new ArrayList<>();
    ArrayList<String> EMAIL = new ArrayList<>();
    ArrayList<String> USERKEY = new ArrayList<>();
    ArrayList<String> DESCRIPTION = new ArrayList<>();
    ArrayList<String> CITY = new ArrayList<>();
    ArrayList<String> COUNTRY = new ArrayList<>();
    ArrayList<String> FROMDATE = new ArrayList<>();
    ArrayList<String> TODATE = new ArrayList<>();
    ArrayList<String> IMAGEURL = new ArrayList<>();
    ArrayList<String> CREATEDON = new ArrayList<>();
    MyAttr attr;
    public EVENTS_ADAPTER(Context c, ArrayList<String> EVENTKEY, ArrayList<String> NAME, ArrayList<String> EMAIL, ArrayList<String> USERKEY, ArrayList<String> DESCRIPTION, ArrayList<String> CITY, ArrayList<String> COUNTRY, ArrayList<String> FROMDATE, ArrayList<String> TODATE, ArrayList<String> IMAGEURL, ArrayList<String> CREATEDON) {
        this.c = c;
        this.EVENTKEY = EVENTKEY;
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.USERKEY = USERKEY;
        this.DESCRIPTION = DESCRIPTION;
        this.CITY = CITY;
        this.COUNTRY = COUNTRY;
        this.FROMDATE = FROMDATE;
        this.TODATE = TODATE;
        this.IMAGEURL = IMAGEURL;
        this.CREATEDON = CREATEDON;
        this.attr = new MyAttr(c);
    }

    @NonNull
    @Override
    public EVENTS_ADAPTER_CHILD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.eventslayout,parent,false);
        return new EVENTS_ADAPTER_CHILD(v);
    }
    public static String convertToCustomFormat(String timeStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy 'at' HH:mm", Locale.getDefault());

            // Parse the input time string
            Date date = inputFormat.parse(timeStr);

            // Format the date into the custom format
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String calculateDaysAndNights(String startDateStr, String endDateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

            // Parse start and end dates
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            // Calculate the difference in milliseconds
            long differenceInMillis = endDate.getTime() - startDate.getTime();

            // Calculate days and nights
            long days = differenceInMillis / (24 * 60 * 60 * 1000);
            long nights = (days > 0) ? days - 1 : 0; // Subtract one day for the nights

            // Format the result
            return days + " days " + nights + " nights";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onBindViewHolder(@NonNull EVENTS_ADAPTER_CHILD holder, @SuppressLint("RecyclerView") int position) {
        if (USERKEY.get(position).equals(attr.getMykey()))
        {
            holder.joinnowbutton.setText("Tap to chat");
            holder.morebutton.setVisibility(View.VISIBLE);
        }
        else {
            DatabaseReference checkevent = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("Events").child(EVENTKEY.get(position));
            checkevent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        holder.joinnowbutton.setText("Tap to chat");
                        holder.morebutton.setVisibility(View.VISIBLE);

                    }
                    else {
                        holder.joinnowbutton.setText("Join Now");
                        holder.morebutton.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        holder.morebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,holder.morebutton);
                if (USERKEY.get(position).equals(attr.getMykey()))
                {
                    popupMenu.getMenu().add("Delete Event");
                }
                else {
                    popupMenu.getMenu().add("Left Event");

                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (NetworkUtils.isNetworkAvailable(c))
                        {
                            if (item.getTitle().equals("Delete Event"))
                            {
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                                        .child("Events").child(EVENTKEY.get(position));
                                db.setValue(null);


                                EVENTKEY.remove(EVENTKEY.get(position));
                                NAME.remove(NAME.get(position));
                                EMAIL.remove(EMAIL.get(position));
                                USERKEY.remove(USERKEY.get(position));
                                DESCRIPTION.remove(DESCRIPTION.get(position));
                                CITY.remove(CITY.get(position));
                                COUNTRY.remove(COUNTRY.get(position));
                                FROMDATE.remove(FROMDATE.get(position));
                                TODATE.remove(TODATE.get(position));
                                IMAGEURL.remove(IMAGEURL.get(position));
                                CREATEDON.remove(CREATEDON.get(position));
                                notifyDataSetChanged();

                                Toast.makeText(c, "Event Deleted...", Toast.LENGTH_SHORT).show();

                            }
                            else  if (item.getTitle().equals("Left Event")){
                                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("Events").child(EVENTKEY.get(position))
                                        .child("TotalMembers");
                                db1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists())
                                        {
                                           int total = Integer.parseInt(snapshot.getValue(String.class));
                                           total=total-1;
                                            holder.membersjoined.setText("Members\n"+String.valueOf(total));

                                           db1.setValue(String.valueOf(total));

                                            DatabaseReference userevent = FirebaseDatabase.getInstance()
                                                    .getReference().child("users").child(attr.getMykey())
                                                    .child("Events").child(EVENTKEY.get(position));
                                            DatabaseReference eventuser = FirebaseDatabase.getInstance().getReference()
                                                    .child("Events").child(EVENTKEY.get(position)).child("Members")
                                                    .child(attr.getMykey());
                                            userevent.setValue(null);
                                            eventuser.setValue(null);
                                            Toast.makeText(c, "You have left the event...", Toast.LENGTH_SHORT).show();
                                            holder.joinnowbutton.setText("Join Now");
                                            holder.morebutton.setVisibility(View.GONE);

                                            EVENTKEY.remove(EVENTKEY.get(position));
                                            NAME.remove(NAME.get(position));
                                            EMAIL.remove(EMAIL.get(position));
                                            USERKEY.remove(USERKEY.get(position));
                                            DESCRIPTION.remove(DESCRIPTION.get(position));
                                            CITY.remove(CITY.get(position));
                                            COUNTRY.remove(COUNTRY.get(position));
                                            FROMDATE.remove(FROMDATE.get(position));
                                            TODATE.remove(TODATE.get(position));
                                            IMAGEURL.remove(IMAGEURL.get(position));
                                            CREATEDON.remove(CREATEDON.get(position));
                                            notifyDataSetChanged();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }
                        else {
                            Toast.makeText(c, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });
            }
        });
        holder.joinnowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.joinnowbutton.getText().equals("Join Now"))
                {
                    //increase mmbr
                    DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("Events").child(EVENTKEY.get(position))
                            .child("TotalMembers");
                    db1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                int total = Integer.parseInt(snapshot.getValue(String.class));
                                total = total+1;


                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users")
                                        .child(attr.getMykey()).child("Events").child(EVENTKEY.get(position));
                                //addevntmember
                                DatabaseReference mmbr = FirebaseDatabase.getInstance().getReference().child("Events")
                                        .child(EVENTKEY.get(position)).child("Members").child(attr.getMykey());
                                int finalTotal = total;
                                mmbr.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshotx) {
                                        if (!snapshot.exists())
                                        {
                                            holder.membersjoined.setText("Members\n"+String.valueOf(finalTotal));

                                            db1.setValue(String.valueOf(finalTotal));


                                            mmbr.setValue("1");
                                            db.setValue("1");

                                            Toast.makeText(c, "Joined Successfully...", Toast.LENGTH_SHORT).show();
                                            holder.joinnowbutton.setText("Tap to chat");
                                            holder.morebutton.setVisibility(View.VISIBLE);
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
        });

        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("Events").child(EVENTKEY.get(position))
                        .child("TotalMembers");
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    holder.membersjoined.setText("Members\n"+snapshot.getValue(String.class));
                }
                else {
                    holder.membersjoined.setText("Members\n0");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String daynight = calculateDaysAndNights(FROMDATE.get(position), TODATE.get(position) );
        holder.countrywithdayandnight.setText(CITY.get(position)+" "+daynight);
        holder.name.setText(NAME.get(position));
        holder.createdon.setText("Event Created on: "+CREATEDON.get(position));
        holder.description.setText(DESCRIPTION.get(position));
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(USERKEY.get(position))
                .child("profileimage");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Glide.with(c).load(snapshot.getValue().toString()).into(holder.profileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ChaekTime(holder,position);

        holder.joinchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(c))
                {
                    if (holder.joinnowbutton.getText().toString().trim().equals("Tap to chat"))
                    {
                            Intent i = new Intent(c,ChatScreen.class);
                            i.putExtra("eventid",EVENTKEY.get(position));
                            c.startActivity(i);

                    }
                }
                else {
                    Toast.makeText(c, "No internet connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        Glide.with(c).load(IMAGEURL.get(position)).into(holder.bgimage);
        String monthdateformatfromdate = convertToCustomFormat(FROMDATE.get(position));
        holder.namewithvisitingdate.setText("Visiting "+CITY.get(position)+", "+COUNTRY.get(position)+" on "+monthdateformatfromdate);
    }

    @Override
    public int getItemCount() {
        return EVENTKEY.size();
    }

    public void ChaekTime(EVENTS_ADAPTER_CHILD holder, int position)
    {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
               if (FROMDATE.size()>position && TODATE.size()>position)
               {
                if (isStartTimeBeforeEndTime(FROMDATE.get(position),getCurrentTime(),dateFormat,dateFormat ))
                {
                    holder.joinnowbutton.setText("Tap to chat");
                    holder.startedcardvisiblity.setVisibility(View.VISIBLE);
                }
                else {
                    holder.startedcardvisiblity.setVisibility(View.GONE);
                    ChaekTime(holder,position);
                }
               }
            }
        },1000);

    }
    public   boolean isStartTimeBeforeEndTime(String startTimeString, String endTimeString,
                                              SimpleDateFormat sdf1, SimpleDateFormat sdf2) {
        try {
            // Parse the start and end time strings into Date objects
            Date startTime = sdf1.parse(startTimeString);
            Date endTime = sdf2.parse(endTimeString);

            // Compare the Date objects
            assert startTime != null;
            return startTime.before(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(c, "Error!", Toast.LENGTH_SHORT).show();
            // Handle the parsing exception as needed
            return false; // For example, you might consider invalid input as "start time is not before end time"
        }
    }
    public String getCurrentTime()
    {
        // Get current time
        LocalDateTime currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
        }

        // Define the date time formatter
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        }
        // Format the current time using the formatter
        String formattedTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedTime = currentTime.format(formatter);
        }
        return formattedTime;
    }

    static class EVENTS_ADAPTER_CHILD extends RecyclerView.ViewHolder{
        ImageView bgimage,profileimage,morebutton;
        TextView createdon,name,countrywithdayandnight,description,membersjoined,namewithvisitingdate;
        TextView joinnowbutton;
        CardView eventcard,startedcardvisiblity;
        CardView joinchat;
        public EVENTS_ADAPTER_CHILD(@NonNull View v) {
            super(v);
            startedcardvisiblity = v.findViewById(R.id.startedcardvisiblity);
            bgimage = v.findViewById(R.id.bgimage);
            joinchat = v.findViewById(R.id.joinchat);
            createdon = v.findViewById(R.id.createdon);
            name = v.findViewById(R.id.name);
            countrywithdayandnight = v.findViewById(R.id.countrywithdayandnight);
            description = v.findViewById(R.id.description);
            membersjoined = v.findViewById(R.id.membersjoined);
            profileimage = v.findViewById(R.id.profileimage);
            joinnowbutton = v.findViewById(R.id.joinnowbutton);
            morebutton = v.findViewById(R.id.morebutton);
            namewithvisitingdate = v.findViewById(R.id.namewithvisitingdate);
        }
    }
}