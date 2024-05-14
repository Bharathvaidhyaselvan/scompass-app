package com.packages.scompass;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.inappmessaging.model.Text;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public   class  MomentsActivity extends AppCompatActivity {

public void ChangeProfile()
{
    SharedPreferences s = getSharedPreferences("USER",MODE_PRIVATE);
   String url = s.getString("profileurl","");

   new Handler().postDelayed(new Runnable() {
       @Override
       public void run() {
           if (MomentsActivity.toload)
           {
               Glide.with(MomentsActivity.this).load(url).into(profilepage);
               Glide.with(MomentsActivity.this).load(url).into(profilepageimg2);
               Glide.with(MomentsActivity.this).load(url).into(profileimage);
               ChangeProfile();
           }
       }
   },1000);
}

    @Override
    protected void onPause() {
        super.onPause();
        toload = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        toload = true;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        toload = true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toload = false;

    }

    private static final int PERMISSION_REQUEST_CODE = 1001;

        ImageView notificationbtnimage,hidenotifications,deleteallnotifications;
    CardView notificationdot;
    public static FrameLayout Notificationlayout;
   public static RecyclerView notifications_recycler_view;
    ProgressBar progressnotifications;
    public static TextView nonewnotificationstext;


    public static boolean toload=true;







    int x=0;
    //////////////////// tourlayout
    public static ViewPager2 viewpager2;
    public static TextView notours;
    public static ProgressBar loadingtours;

    String myemailwithoutat = "", myfirebasekey = "", myname = "";
    ///////////////////////////////
    String MYKEY = "";
    //////////////// PROFILE PAGE
    ImageView profileimage;
    TextView nameprofile;
    TextView share,statusprofile, seefullprofile, suggestion, logout, termsandservices, privacypolicy, myevents, usernametext;
    /////////////////////////////

    private GoogleSignInClient mGoogleSignInClient;
    /////////////// SEARCH
    EditText searchuseredittext;
    RecyclerView searchusersrecyclerview;
    ImageView searchuser;
    //////////////////////


    //////////// UPLOAD POST

    CardView imgcard;
    ImageView cross;
    private FirebaseStorage storage;
    TextView postButton;
    ImageView profilepageimg2;

    private FirebaseFirestore firestore;
    ImageView selectImageButton;
    ProgressBar p1;

    String username;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    TextView locationtext;
    ImageView getlocation;
    boolean location = false;
    private ImageView imageView;
    private EditText captionEditText;
    private Uri selectedImageUri;
    String photoUrl;
    static String idBeforeAtSymbol_username;
    String city, state, country;

    double latitude;
    double longitude;
    ///////////////////////////
    public static ImageView homebutton, compass_button, uploadpostbutton, searchbutton, profilepage;


    public static LinearLayout momentslayout, uploadpostlayout, profilelayout, searchlayout, tourlayout;

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FirebaseAuth auth;
    public static String userName;
    public static TextView remainingfilter;
    public static SetPost setPost;
    private static final int REQUEST_LOCATION_PERMISSIONs = 1;

    public static SwipeRefreshLayout swipeRefreshLayout;
    // comments
    static RecyclerView comments_recycler_view, citiesfilter;
    static FrameLayout CommentLayout;
    static ImageView hidecomments;
    static TextView comment_replytext;
    ProgressBar loadingposts;

    ArrayList<String> EMAILS = new ArrayList<>();
    ArrayList<String> IMAGES = new ArrayList<>();
    ArrayList<String> NAMES = new ArrayList<>();
    ArrayList<String> KEYS = new ArrayList<>();

    static  Context momcontext;
    MyAttr attr;
    public static void ResetColors()
    {
        homebutton.setColorFilter(Color.parseColor("#FFFFFF"));
        compass_button.setColorFilter(Color.parseColor("#FFFFFF"));
        uploadpostbutton.setColorFilter(Color.parseColor("#FFFFFF"));
        searchbutton.setColorFilter(Color.parseColor("#FFFFFF"));
    }
    public void openLinkInChrome(Context context, String url) {
        // Create an Intent with ACTION_VIEW and the URI of the webpage
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        // Set the package name to ensure the link opens in Chrome
        intent.setPackage("com.android.chrome");

        // Check if Chrome is installed on the device
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Open the link in Chrome
            context.startActivity(intent);
        } else {
            // If Chrome is not installed, open the link in the default browser
            intent.setPackage(null);
            context.startActivity(intent);
        }
    }

    private void sendNotification(String title, String messageBody) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.noti)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // Generate a unique notification ID
        int notificationId = generateUniqueNotificationId();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }
    public int generateUniqueNotificationId() {
        // You can use various methods to generate a unique ID,
        // such as using the current timestamp or a random number generator
        return (int) System.currentTimeMillis();
    }


    @SuppressLint({"MissingInflatedId", "CheckResult"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        CardView postEventCard = findViewById(R.id.postevent);
        postEventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MyEventsActivity
                Intent intent = new Intent(MomentsActivity.this, MyEvents.class);
                startActivity(intent);
            }
        });
        momcontext = MomentsActivity.this;
        attr = new MyAttr(MomentsActivity.this);
        ChangeProfile();
        getMYKEY();
        SharedPreferences s = getSharedPreferences("USER", MODE_PRIVATE);
        userName = attr.getMyname();

        viewpager2 = findViewById(R.id.viewpager2);
        notours = findViewById(R.id.notours);
        loadingtours = findViewById(R.id.loadingtours);
        momentslayout = findViewById(R.id.momentslayout);
        tourlayout = findViewById(R.id.tourlayout);
        uploadpostlayout = findViewById(R.id.uploadpostlayout);
        profilelayout = findViewById(R.id.profilelayout);
        searchlayout = findViewById(R.id.searchlayout);
        homebutton = findViewById(R.id.homebutton);
        compass_button = findViewById(R.id.compass_button);
        uploadpostbutton = findViewById(R.id.uploadpostbutton);
        profilepage = findViewById(R.id.profilepage);
        searchbutton = findViewById(R.id.searchbutton);
//        emergency = findViewById(R.id.emergency);
        comments_recycler_view = findViewById(R.id.comments_recycler_view);
        CommentLayout = findViewById(R.id.CommentLayout);
        hidecomments = findViewById(R.id.hidecomments);
        loadingposts = findViewById(R.id.loadingposts);
        remainingfilter = findViewById(R.id.remainingfilter);
        comment_replytext = findViewById(R.id.comment_replytext);
        citiesfilter = findViewById(R.id.citiesfilter);

        notificationbtnimage = findViewById(R.id.notification);
        hidenotifications = findViewById(R.id.hidenotifications);
        notificationdot = findViewById(R.id.notificationdot);
        Notificationlayout = findViewById(R.id.Notificationlayout);
        notifications_recycler_view = findViewById(R.id.notifications_recycler_view);
        progressnotifications = findViewById(R.id.progressnotifications);
        nonewnotificationstext = findViewById(R.id.nonewnotificationstext);
        deleteallnotifications = findViewById(R.id.deleteallnotifications);
      //  sendNotification("temp","body");
        // load dot
        DatabaseReference mydot = FirebaseDatabase.getInstance().getReference().child("users")
                        .child(attr.getMykey()).child("Notifications").child("Notidot");
        mydot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue(String.class).equals("1"))
                {
                    notificationdot.setVisibility(View.VISIBLE);
                }
                else {
                    notificationdot.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        deleteallnotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(MomentsActivity.this))
                {
                    if (nonewnotificationstext.getVisibility() == View.VISIBLE)
                    {
                        Toast.makeText(MomentsActivity.this, "No new notifications!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users")
                                .child(attr.getMykey()).child("Notifications");
                        db.setValue(null);
                        Toast.makeText(MomentsActivity.this, "Notifications deleted", Toast.LENGTH_SHORT).show();

                        nonewnotificationstext.setVisibility(View.VISIBLE);
                        notifications_recycler_view.setVisibility(View.GONE);
                    }

                }
                else {
                    Toast.makeText(MomentsActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        notificationbtnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadnotifications();
                // load notifications
                mydot.setValue("0");
                progressnotifications.setVisibility(View.VISIBLE);
                notifications_recycler_view.setVisibility(View.GONE);
                nonewnotificationstext.setVisibility(View.GONE);
                notificationdot.setVisibility(View.GONE);
                Notificationlayout.setVisibility(View.VISIBLE);

            }
        });
        hidenotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notificationlayout.setVisibility(View.GONE);
            }
        });


        //////////////// SEARCH
        searchuseredittext = findViewById(R.id.searchuseredittext);
        searchusersrecyclerview = findViewById(R.id.searchusersrecyclerview);
        searchuser = findViewById(R.id.searchuser);

        searchuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = MomentsActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        searchuseredittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = searchuseredittext.getText().toString().trim().toLowerCase();
                searchusersrecyclerview.setVisibility(View.VISIBLE);

                if (text.isEmpty()) {
                    searchusersrecyclerview.setVisibility(View.VISIBLE );
                }

                ArrayList<String> EMAILSadded = new ArrayList<>();
                ArrayList<String> NAMESadded = new ArrayList<>();
                ArrayList<String> KEYSadded = new ArrayList<>();
                ArrayList<String> IMAGESadded = new ArrayList<>();

                for (int i = 0; i < KEYS.size(); i++) {
                    if (NAMES.get(i).startsWith(text)) {

                        EMAILSadded.add(EMAILS.get(i));
                        NAMESadded.add(NAMES.get(i));
                        KEYSadded.add(KEYS.get(i));
                        IMAGESadded.add(IMAGES.get(i));
                        SEARCHUSER searchuser1 = new SEARCHUSER(MomentsActivity.this, EMAILSadded, NAMESadded, KEYSadded, IMAGESadded);
                        searchusersrecyclerview.setAdapter(searchuser1);
                        searchusersrecyclerview.setVisibility(View.VISIBLE);


                    } else if (EMAILS.get(i).startsWith(text)) {
                        if (!EMAILS.get(i).equals("no username")) {
                            EMAILSadded.add(EMAILS.get(i));
                            NAMESadded.add(NAMES.get(i));
                            KEYSadded.add(KEYS.get(i));
                            IMAGESadded.add(IMAGES.get(i));
                            SEARCHUSER searchuser1 = new SEARCHUSER(MomentsActivity.this, EMAILSadded, NAMESadded, KEYSadded, IMAGESadded);
                            searchusersrecyclerview.setAdapter(searchuser1);
                            searchusersrecyclerview.setVisibility(View.VISIBLE);
                        }

                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ///////////////////////

        //////////////// PROFILE PAGE IDS
        profileimage = findViewById(R.id.profileimage);
        nameprofile = findViewById(R.id.nameprofile);
        statusprofile = findViewById(R.id.statusprofile);
        share = findViewById(R.id.share);
        seefullprofile = findViewById(R.id.seefullprofile);
        suggestion = findViewById(R.id.suggestion);
        termsandservices = findViewById(R.id.termsandservices);
        privacypolicy = findViewById(R.id.privacypolicy);
        myevents = findViewById(R.id.myevents);
        usernametext = findViewById(R.id.usernametext);
        logout = findViewById(R.id.logout);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().
                        child("apk");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (NetworkUtils.isNetworkAvailable(MomentsActivity.this))
                        {
                            if (snapshot.exists())
                            {
                                String url = snapshot.getValue(String.class);
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share App");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hi! you can get the Scompass from this link..." + "\n" + url);
                                startActivity(Intent.createChooser(shareIntent, "Share via"));
                            }
                            else {
                                Toast.makeText(MomentsActivity.this, "Link not found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MomentsActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                  
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        usernametext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(MomentsActivity.this).inflate(R.layout.customalertfinput, null);
                TextView titletv, savebtn, cancelbtn;
                EditText enterinput;

                titletv = view.findViewById(R.id.title);
                savebtn = view.findViewById(R.id.savebutton);
                cancelbtn = view.findViewById(R.id.cancelbutton);
                enterinput = view.findViewById(R.id.getinputtextd);

                AlertDialog.Builder alert = new AlertDialog.Builder(MomentsActivity.this);
                alert.setView(view);
                alert.setCancelable(false);
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();
                alertDialog.show();

                titletv.setText("Set Username");
                enterinput.setHint("Enter your username (15 chars max) without spaces in small letters");
                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status = enterinput.getText().toString().trim().toLowerCase();
                        enterinput.setText(status);
                        if (!status.isEmpty()) {
                            if (status.contains(" ")) {
                                Toast.makeText(MomentsActivity.this, "Invalid username!", Toast.LENGTH_SHORT).show();
                            } else if (status.length() > 15) {
                                Toast.makeText(MomentsActivity.this, "Too long username", Toast.LENGTH_SHORT).show();
                            } else {
                                if (MYKEY.isEmpty()) {
                                    Toast.makeText(MomentsActivity.this, "Something went's wrong!", Toast.LENGTH_SHORT).show();
                                } else if (!NetworkUtils.isNetworkAvailable(MomentsActivity.this)) {
                                    Toast.makeText(MomentsActivity.this, "No internet available!", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference checkdb = FirebaseDatabase.getInstance().getReference().child("usernames").child(status);
                                    checkdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                Toast.makeText(MomentsActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                DatabaseReference checkdb2 = FirebaseDatabase.getInstance().getReference().child("usernames").child(usernametext.getText().toString().trim());
                                                checkdb2.setValue(null);

                                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(MYKEY).child("username");
                                                db.setValue(status);
                                                usernametext.setText(status);
                                                DatabaseReference x = checkdb.child(status);
                                                x.setValue("1");
                                                Toast.makeText(MomentsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }

                        } else {
                            Toast.makeText(MomentsActivity.this, "Empty username!", Toast.LENGTH_SHORT).show();
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
        });
        myevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MomentsActivity.this, MyEvents.class));
            }
        });
        termsandservices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MomentsActivity.this, Privacy_and_terms.class);
                i.putExtra("no", 1);
                startActivity(i);
            }
        });
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MomentsActivity.this, Privacy_and_terms.class);
                i.putExtra("no", 2);
                startActivity(i);
            }
        });
        seefullprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s = getSharedPreferences("USER", MODE_PRIVATE);
                Intent i = new Intent(MomentsActivity.this, ViewerProfile.class);
                i.putExtra("key", s.getString("key", ""));
                i.putExtra("email", s.getString("email", ""));
                startActivity(i);


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(MomentsActivity.this, gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();

                        // 2. Clear SharedPreferences (session data)
                        SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                        sharedPreferencesEditor.clear();
                        sharedPreferencesEditor.apply();

                        SharedPreferences.Editor userSharedPreferencesEditor = getSharedPreferences("USER", MODE_PRIVATE).edit();
                        userSharedPreferencesEditor.clear();
                        userSharedPreferencesEditor.apply();

                        // 3. Set toload flag to false
                        toload = false;

                        // 4. Redirect user to SplashActivity (login screen)
                        startActivity(new Intent(MomentsActivity.this, SplashActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MomentsActivity.this, "Unable to logout, please check your internet connection!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            });


        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"scompassqueries@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I have a suggestion:");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email Client"));
            }
        });
        nameprofile.setText(userName);


        ResetColors();
        homebutton.setColorFilter(Color.parseColor("#FF8C42"));


        //load status
        DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(MYKEY).child("status");
        loadstatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    statusprofile.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        statusprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(MomentsActivity.this).inflate(R.layout.customalertfinput, null);
                TextView titletv, savebtn, cancelbtn;
                EditText enterinput;

                titletv = view.findViewById(R.id.title);
                savebtn = view.findViewById(R.id.savebutton);
                cancelbtn = view.findViewById(R.id.cancelbutton);
                enterinput = view.findViewById(R.id.getinputtextd);

                AlertDialog.Builder alert = new AlertDialog.Builder(MomentsActivity.this);
                alert.setView(view);
                alert.setCancelable(false);
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();
                alertDialog.show();

                titletv.setText("Set Status");
                enterinput.setHint("Enter your status (25 chars max)");
                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status = enterinput.getText().toString().trim();
                        if (!status.isEmpty()) {
                            if (status.length() > 25) {
                                Toast.makeText(MomentsActivity.this, "Too long status", Toast.LENGTH_SHORT).show();
                            } else {
                                if (MYKEY.isEmpty()) {
                                    Toast.makeText(MomentsActivity.this, "Something went's wrong!", Toast.LENGTH_SHORT).show();
                                } else if (!NetworkUtils.isNetworkAvailable(MomentsActivity.this)) {
                                    Toast.makeText(MomentsActivity.this, "No internet available!", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(MYKEY).child("status");
                                    db.setValue(status);
                                    statusprofile.setText(status);
                                    Toast.makeText(MomentsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();

                                }
                            }

                        } else {
                            Toast.makeText(MomentsActivity.this, "Empty status!", Toast.LENGTH_SHORT).show();
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
        });
        /////////////////////////////


        //////////////// UPLOAD
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        locationtext = findViewById(R.id.locationtext);
        selectImageButton = findViewById(R.id.selectImageButton);
        postButton = findViewById(R.id.postButton);
        imageView = findViewById(R.id.imageView);
        profilepageimg2 = findViewById(R.id.profilepageimg2);
        captionEditText = findViewById(R.id.captionEditText);
        p1 = findViewById(R.id.p1);
        cross = findViewById(R.id.cross);
        imgcard = findViewById(R.id.imgcard);

        SharedPreferences s2 = getSharedPreferences("USER", MODE_PRIVATE);
        if (!s2.getString("profileurl", "-").equals("-")) {
            Glide.with(MomentsActivity.this).load(s2.getString("profileurl", "https://lh3.googleusercontent.com/a/ACg8ocI81chLOWL3JjVUlGnTsvQd53e08UQuRCrnoIeFo3uPfQ=s360-c-no")).into(profilepageimg2);
        }


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                String[] parts = userEmail.split("@");
                if (parts.length > 0) {
                    idBeforeAtSymbol_username = parts[0];
                }
            }
        }
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetColors();
                searchbutton.setColorFilter(Color.parseColor("#FF8C42"));
                momentslayout.setVisibility(View.GONE);
                uploadpostlayout.setVisibility(View.GONE);
                profilelayout.setVisibility(View.GONE);
                searchlayout.setVisibility(View.VISIBLE);
                tourlayout.setVisibility(View.GONE);

            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetColors();
                homebutton.setColorFilter(Color.parseColor("#FF8C42"));
                momentslayout.setVisibility(View.VISIBLE);
                uploadpostlayout.setVisibility(View.GONE);
                profilelayout.setVisibility(View.GONE);
                searchlayout.setVisibility(View.GONE);
                tourlayout.setVisibility(View.GONE);

            }
        });
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadPost();
                } catch (IOException e) {
                    Toast.makeText(MomentsActivity.this, "something went's wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Check location permission and get location
        requestPostNotificationPermission();
        requestLocationPermission();
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("user_name");
        }

        // get current user and get profile image
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;
            Glide.with(MomentsActivity.this).load(photoUrl).into(profileimage);
            Glide.with(MomentsActivity.this).load(photoUrl).into(profilepage);

            if (photoUrl == null) {
                photoUrl = "-1";
            }
        } else {

            photoUrl = "-1";
        }
        ////////////////////////
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                String[] parts = userEmail.split("@");
                if (parts.length > 0) {
                    idBeforeAtSymbol_username = parts[0];
                }

            }
        }

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetColors();
                homebutton.setColorFilter(Color.parseColor("#FF8C42"));

                momentslayout.setVisibility(View.VISIBLE);
                uploadpostlayout.setVisibility(View.GONE);
                profilelayout.setVisibility(View.GONE);
                searchlayout.setVisibility(View.GONE);
                tourlayout.setVisibility(View.GONE);


            }
        });
        compass_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetColors();
                compass_button.setColorFilter(Color.parseColor("#FF8C42"));
                momentslayout.setVisibility(View.GONE);
                uploadpostlayout.setVisibility(View.GONE);
                profilelayout.setVisibility(View.GONE);
                searchlayout.setVisibility(View.GONE);
                tourlayout.setVisibility(View.VISIBLE);
                viewpager2.setVisibility(View.GONE);
                loadingtours.setVisibility(View.VISIBLE);
                LoadEvents("-null-");

            }
        });
        uploadpostbutton.setOnClickListener(view -> {
            // Check location permission and get location
            if (ContextCompat.checkSelfPermission(MomentsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(MomentsActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            } else {
                // Request location updates
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                    return;
                }
                ResetColors();
                uploadpostbutton.setColorFilter(Color.parseColor("#FF8C42"));
                uploadpostlayout.setVisibility(View.VISIBLE);
                momentslayout.setVisibility(View.GONE);
                profilelayout.setVisibility(View.GONE);
                searchlayout.setVisibility(View.GONE);
                tourlayout.setVisibility(View.GONE);


            }

        });

        profilepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetColors();
                momentslayout.setVisibility(View.GONE);
                uploadpostlayout.setVisibility(View.GONE);
                searchlayout.setVisibility(View.GONE);
                profilelayout.setVisibility(View.VISIBLE);
                tourlayout.setVisibility(View.GONE);

            }
        });
        remainingfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CITYIES_FILTER.FILTERCITYLIST.clear();
                CITYIES_FILTER.Call(CITYIES_FILTER.h.get(CITYIES_FILTER.h.size() - 1));
                Toast.makeText(MomentsActivity.this, "Showing all posts", Toast.LENGTH_SHORT).show();
            }
        });
//        emergency.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MomentsActivity.this, EmergencyActivity.class));
//            }
//        });
        loadingposts.setVisibility(View.VISIBLE);


        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);


        hidecomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentLayout.setVisibility(View.GONE);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(this::LoadPosts);

        new Handler().postDelayed(this::LoadPosts, 1000);
    }

    public static String getCurrentDateTime() {
        // Get the current date and time
        Date now = new Date();

        // Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        // Format the date and time
        return dateFormat.format(now);
    }
public void   loadnotifications()
    {

        progressnotifications.setVisibility(View.VISIBLE);
        nonewnotificationstext.setVisibility(View.GONE);
        notifications_recycler_view.setVisibility(View.GONE);

        DatabaseReference mynoti = FirebaseDatabase.getInstance().getReference().child("users")
                .child(attr.getMykey()).child("Notifications");
        mynoti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    ArrayList<String> NOTIID = new ArrayList<>();
                    ArrayList<String> EVENTID = new ArrayList<>();
                    ArrayList<String> TYPE = new ArrayList<>();
                    ArrayList<String> TITLE = new ArrayList<>();
                    ArrayList<String> MESSAGE = new ArrayList<>();
                    ArrayList<String> USERID = new ArrayList<>();
                    ArrayList<String> POSTID = new ArrayList<>();
                    ArrayList<String> DATETIME = new ArrayList<>();
                    ArrayList<String> CHATID = new ArrayList<>();

                    for (DataSnapshot notikey: snapshot.getChildren())
                    {
                        DataSnapshot typesnapshot =  notikey.child("type");
                        if (typesnapshot.exists())
                        {
                            String type = typesnapshot.getValue(String.class);
                            if (type.equals(null))
                            {
                                progressnotifications.setVisibility(View.GONE);
                                nonewnotificationstext.setVisibility(View.VISIBLE);
                                notifications_recycler_view.setVisibility(View.GONE);
                                Toast.makeText(MomentsActivity.this, "null", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else if (type.equals("event"))
                            {
                                // for events
                                NOTIID.add(0,notikey.getKey().toString());
                                TYPE.add(0,"event");
                                TITLE.add(0,notikey.child("title").getValue(String.class));
                                MESSAGE.add(0,notikey.child("message").getValue(String.class));
                                USERID.add(0,notikey.child("userid").getValue(String.class));
                                EVENTID.add(0,notikey.child("eventid").getValue(String.class));
                                POSTID.add(0,"-null-");
                                CHATID.add(0,"-null-");
                                if (notikey.child("datetime").getValue(String.class) == null)
                                {
                                    DATETIME.add(0,"-null-");

                                }
                                else {
                                    DATETIME.add(0,notikey.child("datetime").getValue(String.class));

                                }

                                NOTIFICATIONS notifications = new NOTIFICATIONS(MomentsActivity.this
                                        ,NOTIID,
                                        EVENTID,
                                        TYPE,
                                        TITLE,
                                        MESSAGE,
                                        USERID,
                                        POSTID,
                                        DATETIME,CHATID);
                                progressnotifications.setVisibility(View.GONE);
                                nonewnotificationstext.setVisibility(View.GONE);
                                notifications_recycler_view.setVisibility(View.VISIBLE);
                                notifications_recycler_view.setAdapter(notifications);


                            }
                            else if (type.equals("like"))
                            {
                                // for like
                                NOTIID.add(0,notikey.getKey().toString());
                                TYPE.add(0,"like");
                                TITLE.add(0,notikey.child("title").getValue(String.class));
                                MESSAGE.add(0,notikey.child("message").getValue(String.class));
                                USERID.add(0,notikey.child("userid").getValue(String.class));
                                POSTID.add(0,notikey.child("postid").getValue(String.class));
                                if (notikey.child("datetime").getValue(String.class) == null)
                                {
                                    DATETIME.add(0,"-null-");

                                }
                                else {
                                    DATETIME.add(0,notikey.child("datetime").getValue(String.class));

                                }
                                CHATID.add(0,"-null-");
                                EVENTID.add(0,"-null-");
                                NOTIFICATIONS notifications = new NOTIFICATIONS(MomentsActivity.this
                                        ,NOTIID,
                                        EVENTID,
                                        TYPE,
                                        TITLE,
                                        MESSAGE,
                                        USERID,
                                        POSTID,
                                        DATETIME,CHATID);
                                progressnotifications.setVisibility(View.GONE);
                                nonewnotificationstext.setVisibility(View.GONE);
                                notifications_recycler_view.setVisibility(View.VISIBLE);
                                notifications_recycler_view.setAdapter(notifications);
                            }
                            else if (type.equals("comment"))
                            {
                                // for comment
                                NOTIID.add(0,notikey.getKey().toString());
                                TYPE.add(0,"comment");
                                TITLE.add(0,notikey.child("title").getValue(String.class));
                                MESSAGE.add(0,notikey.child("message").getValue(String.class));
                                USERID.add(0,notikey.child("userid").getValue(String.class));
                                POSTID.add(0,notikey.child("postid").getValue(String.class));
                                if (notikey.child("datetime").getValue(String.class) == null)
                                {
                                    DATETIME.add(0,"-null-");

                                }
                                else {
                                    DATETIME.add(0,notikey.child("datetime").getValue(String.class));

                                }
                                CHATID.add(0,"-null-");
                                EVENTID.add(0,"-null-");
                                NOTIFICATIONS notifications = new NOTIFICATIONS(MomentsActivity.this
                                        ,NOTIID,
                                        EVENTID,
                                        TYPE,
                                        TITLE,
                                        MESSAGE,
                                        USERID,
                                        POSTID,
                                        DATETIME,CHATID);
                                progressnotifications.setVisibility(View.GONE);
                                nonewnotificationstext.setVisibility(View.GONE);
                                notifications_recycler_view.setVisibility(View.VISIBLE);
                                notifications_recycler_view.setAdapter(notifications);

                            }
                            else if (type.equals("post"))
                            {
                                // for comment
                                NOTIID.add(0,notikey.getKey().toString());
                                TYPE.add(0,"post");
                                TITLE.add(0,notikey.child("title").getValue(String.class));
                                MESSAGE.add(0,notikey.child("message").getValue(String.class));
                                USERID.add(0,notikey.child("userid").getValue(String.class));
                                POSTID.add(0,notikey.child("postid").getValue(String.class));
                                if (notikey.child("datetime").getValue(String.class) == null)
                                {
                                    DATETIME.add(0,"-null-");

                                }
                                else {
                                    DATETIME.add(0,notikey.child("datetime").getValue(String.class));

                                }
                                CHATID.add(0,"-null-");
                                EVENTID.add(0,"-null-");
                                NOTIFICATIONS notifications = new NOTIFICATIONS(MomentsActivity.this
                                        ,NOTIID,
                                        EVENTID,
                                        TYPE,
                                        TITLE,
                                        MESSAGE,
                                        USERID,
                                        POSTID,
                                        DATETIME,CHATID);
                                progressnotifications.setVisibility(View.GONE);
                                nonewnotificationstext.setVisibility(View.GONE);
                                notifications_recycler_view.setVisibility(View.VISIBLE);
                                notifications_recycler_view.setAdapter(notifications);

                            }
                            else if (type.equals("follow"))
                            {

                                // for follow
                                NOTIID.add(0,notikey.getKey().toString());
                                TYPE.add(0,"follow");
                                TITLE.add(0,notikey.child("title").getValue(String.class));
                                MESSAGE.add(0,notikey.child("message").getValue(String.class));
                                USERID.add(0,notikey.child("userid").getValue(String.class));
                                if (notikey.child("datetime").getValue(String.class) == null)
                                {
                                    DATETIME.add(0,"-null-");

                                }
                                else {
                                    DATETIME.add(0,notikey.child("datetime").getValue(String.class));

                                }
                                POSTID.add(0,"-null-");
                                CHATID.add(0,"-null-");
                                EVENTID.add(0,"-null-");
                                NOTIFICATIONS notifications = new NOTIFICATIONS(MomentsActivity.this
                                        ,NOTIID,
                                        EVENTID,
                                        TYPE,
                                        TITLE,
                                        MESSAGE,
                                        USERID,
                                        POSTID,
                                        DATETIME,CHATID);
                                progressnotifications.setVisibility(View.GONE);
                                nonewnotificationstext.setVisibility(View.GONE);
                                notifications_recycler_view.setVisibility(View.VISIBLE);
                                notifications_recycler_view.setAdapter(notifications);

                            }
                            else if (type.equals("chat"))
                            {

                                // for follow
                                NOTIID.add(0,notikey.getKey().toString());
                                TYPE.add(0,"chat");
                                TITLE.add(0,notikey.child("title").getValue(String.class));
                                MESSAGE.add(0,notikey.child("message").getValue(String.class));
                                USERID.add(0,notikey.child("userid").getValue(String.class));
                                if (notikey.child("datetime").getValue(String.class) == null)
                                {
                                    DATETIME.add(0,"-null-");

                                }
                                else {
                                    DATETIME.add(0,notikey.child("datetime").getValue(String.class));

                                }
                                POSTID.add(0,"-null-");
                                CHATID.add(0,notikey.child("chatid").getValue(String.class));
                                EVENTID.add(0,"-null-");
                                NOTIFICATIONS notifications = new NOTIFICATIONS(MomentsActivity.this
                                        ,NOTIID,
                                        EVENTID,
                                        TYPE,
                                        TITLE,
                                        MESSAGE,
                                        USERID,
                                        POSTID,
                                        DATETIME,CHATID);
                                progressnotifications.setVisibility(View.GONE);
                                nonewnotificationstext.setVisibility(View.GONE);
                                notifications_recycler_view.setVisibility(View.VISIBLE);
                                notifications_recycler_view.setAdapter(notifications);

                            }
                        } else {
                            if (notikey.getKey().equals("Notidot") && NOTIID.isEmpty())
                            {
                                progressnotifications.setVisibility(View.GONE);
                                nonewnotificationstext.setVisibility(View.VISIBLE);
                                notifications_recycler_view.setVisibility(View.GONE);
                            }

                        }

                    }
                }
                else {
                    progressnotifications.setVisibility(View.GONE);
                    nonewnotificationstext.setVisibility(View.VISIBLE);
                    notifications_recycler_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean isStartTimeBeforeEndTime(String startTimeString, String endTimeString,
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
            Toast.makeText(MomentsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            // Handle the parsing exception as needed
            return false; // For example, you might consider invalid input as "start time is not before end time"
        }
    }

    public void deleteEvents() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        DatabaseReference event = FirebaseDatabase.getInstance().getReference().child("Events");
        event.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        DatabaseReference eventtodate = event.child(ds.getKey()).child("Todate");
                        eventtodate.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotdate) {
                                if (snapshotdate.exists()) {
                                    if (!isStartTimeBeforeEndTime(getCurrentDateTime(), snapshotdate.getValue(String.class), dateFormat, dateFormat)) {
                                        DatabaseReference delevent = event.child(ds.getKey());
                                        delevent.setValue(null);
                                        DatabaseReference chatdel = FirebaseDatabase.getInstance().getReference().child("Chats").child(ds.getKey());
                                        chatdel.setValue(null);
                                        StorageReference eventimagedel = FirebaseStorage.getInstance().getReference().child("Events").child(ds.getKey());
                                        eventimagedel.delete();
                                        StorageReference eventchatdel = FirebaseStorage.getInstance().getReference().child("Chats").child(ds.getKey());
                                        deleteContents(eventchatdel);
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

    private static void deleteContents(final StorageReference folderRef) {
        // List all items (files and sub-folders) within the folder
        folderRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // Delete each item (file or sub-folder)
                for (StorageReference item : listResult.getItems()) {
                    item.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File or sub-folder deleted successfully
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to delete file or sub-folder
                        }
                    });
                }
                // Recursively delete sub-folders
                for (StorageReference prefix : listResult.getPrefixes()) {
                    deleteContents(prefix);
                }
                // Delete the folder itself once all its contents are deleted
                folderRef.delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to list items in the folder
            }
        });
    }

    public void getMYKEY() {
        SharedPreferences sharedPreferencesc = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferencesc.getString("key", "null").equals("null")) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userEmail = currentUser.getEmail();
                if (userEmail != null) {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users");
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    DatabaseReference key = db.child(ds.getKey()).child("email");
                                    key.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                            if (snapshot2.exists() && snapshot2.getValue(String.class).equals(userEmail)) {
                                                MYKEY = ds.getKey().toString();
//                                            Toast.makeText(MomentsActivity.this, MYKEY, Toast.LENGTH_SHORT).show();
                                                SharedPreferences.Editor sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                                                sharedPreferences.putString("key", MYKEY);
                                                sharedPreferences.apply();
                                                DatabaseReference loadstatus = FirebaseDatabase.getInstance().getReference().child("users").child(MYKEY).child("status");
                                                loadstatus.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            statusprofile.setText(snapshot.getValue(String.class));
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                DatabaseReference loadusername = FirebaseDatabase.getInstance().getReference().child("users").child(MYKEY).child("username");
                                                loadusername.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            usernametext.setText(snapshot.getValue(String.class));
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
        } else {
            MYKEY = sharedPreferencesc.getString("key", "");
            DatabaseReference loadusername = FirebaseDatabase.getInstance().getReference().child("users").child(MYKEY).child("username");
            loadusername.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        usernametext.setText(snapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        // also added defaults
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                USEREMAIL.userEmail = userEmail;
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                DatabaseReference em = db.child(ds.getKey()).child("username");
                                DatabaseReference img = db.child(ds.getKey()).child("profileimage");
                                DatabaseReference name = db.child(ds.getKey()).child("name");

                                img.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshotimg) {
                                        if (snapshotimg.exists()) {
                                            em.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshotem) {
                                                    if (snapshotem.exists()) {
                                                        name.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshotname) {
                                                                if (snapshotname.exists()) {
                                                                    NAMES.add(snapshotname.getValue(String.class).toLowerCase());
                                                                    EMAILS.add(snapshotem.getValue(String.class).toLowerCase());
                                                                    KEYS.add(ds.getKey());
                                                                    IMAGES.add(snapshotimg.getValue(String.class));
                                                                    searchuseredittext.setEnabled(true);
                                                                    searchuseredittext.setHint("Search with name or username");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    } else {
                                                        name.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshotname) {
                                                                if (snapshotname.exists()) {
                                                                    NAMES.add(snapshotname.getValue(String.class).toLowerCase());
                                                                    EMAILS.add("no username");
                                                                    KEYS.add(ds.getKey());
                                                                    IMAGES.add(snapshotimg.getValue(String.class));
                                                                    searchuseredittext.setEnabled(true);
                                                                    searchuseredittext.setHint("Search with name or username");
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

        MyAttr attr = new MyAttr(MomentsActivity.this);
        myemailwithoutat = attr.getMyEmail();
        myfirebasekey = attr.getMykey();
        myname = attr.getMyname();
        deleteEvents();
        LoadEvents("-null-");

    }


    public static void LoadEvents(String eventid) {

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

//        if (EVENTKEY.isEmpty())
        if (true) {
            DatabaseReference events2 = FirebaseDatabase.getInstance().getReference().child("Events");
            events2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            DatabaseReference eventkey = events2.child(ds.getKey());
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
                                    if (snapshotname.exists()) {
                                        emaildb.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshotemail) {
                                                if (snapshotemail.exists()) {
                                                    userkeydb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshotuserkey) {
                                                            if (snapshotuserkey.exists()) {
                                                                descriptiondb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshotdescription) {
                                                                        if (snapshotdescription.exists()) {
                                                                            citydb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshotcity) {
                                                                                    if (snapshotcity.exists()) {
                                                                                        countrydb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshotcountry) {
                                                                                                if (snapshotcountry.exists()) {
                                                                                                    fromdatedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshotfromdate) {
                                                                                                            if (snapshotfromdate.exists()) {
                                                                                                                todatedb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onDataChange(@NonNull DataSnapshot snapshottodate) {
                                                                                                                        if (snapshottodate.exists()) {
                                                                                                                            imageuridb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshotimageurl) {
                                                                                                                                    if (snapshotimageurl.exists()) {
                                                                                                                                        createdon.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onDataChange(@NonNull DataSnapshot snapshotcreatedon) {
                                                                                                                                                if (snapshotcreatedon.exists()) {
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

                                                                                                                                                    EVENTS_ADAPTER2 eventsAdapter = new EVENTS_ADAPTER2(momcontext,
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
                                                                                                                                                    notours.setText("");
                                                                                                                                                    if (!eventid.equals("-null-"))
                                                                                                                                                    {
                                                                                                                                                        int indexToScrollTo = EVENTKEY.indexOf(eventid); // Change this to the desired index
                                                                                                                                                        viewpager2.setCurrentItem(indexToScrollTo, true);
                                                                                                                                                    }
                                                                                                                                                    loadingtours.setVisibility(View.GONE);
                                                                                                                                                    notours.setVisibility(View.GONE);
                                                                                                                                                    viewpager2.setVisibility(View.VISIBLE);
                                                                                                                                                    viewpager2.setAdapter(eventsAdapter);
                                                                                                                                                    viewpager2.setOffscreenPageLimit(1);



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
                    } else {
                        notours.setText("No tours to show!");
                        notours.setVisibility(View.VISIBLE);
                        loadingtours.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    //////////////////////// UPLOAD

    private void startLocationUpdates() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Register the location listener to receive updates
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Handle the new location
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getAddressFromLocation(MomentsActivity.this, latitude, longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        loadgps(locationManager, locationListener);
    }

    public void loadgps(LocationManager locationManager, LocationListener locationListener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (x==0)
                    {
                       // buildAlertMessageNoGps2();
                    }
                  loadgps(locationManager, locationListener);

                } else {
                    locationtext.setVisibility(View.VISIBLE);
                    locationtext.setText("Getting location, please move your mobile in a distance of 3m if takes more than 60sec. \nLoading...");
                    if (ActivityCompat.checkSelfPermission(MomentsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MomentsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            0,  // Minimum time interval between updates (milliseconds)
                            0,     // Minimum distance between updates (meters)
                            locationListener);
                }

            }
        },1000);
    }
    private void getAddressFromLocation(final Context context, final double latitude, final double longitude) {
        if (location)
        {
            return;
        }

        locationtext.setVisibility(View.VISIBLE);
        AsyncTask<Void, Void, Address> task = new AsyncTask<Void, Void, Address>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Address doInBackground(Void... params) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addresses != null && addresses.size() > 0) {
                        return addresses.get(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Address address) {
                // Extract city, state, and country
                if (address != null) {
                    city = address.getLocality();
                    state = address.getAdminArea();
                    country = address.getCountryName();

                    // Display the variables
                    String result = "City: " + city + "\nState: " + state + "\nCountry: " + country;
                    locationtext.setText(result);
                    location=true;
                    p1.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, "No address found", Toast.LENGTH_LONG).show();
                }
            }
        };

        task.execute();
    }
    @SuppressLint("MissingInflatedId")
    private void buildAlertMessageNoGps() {

        View view = LayoutInflater.from(MomentsActivity.this).inflate(R.layout.customalertfinput,null);
        TextView titletv,savebtn,cancelbtn,textgps;
        EditText enterinput;

        textgps = view.findViewById(R.id.gps);
        titletv = view.findViewById(R.id.title);
        savebtn = view.findViewById(R.id.savebutton);
        cancelbtn = view.findViewById(R.id.cancelbutton);
        enterinput = view.findViewById(R.id.getinputtextd);
        savebtn.setText("Enable");
        enterinput.setVisibility(View.GONE);
        textgps.setVisibility(View.VISIBLE);

        AlertDialog.Builder alert = new AlertDialog.Builder(MomentsActivity.this);
        alert.setView(view);
        alert.setCancelable(false);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

        titletv.setText("Enable GPS");
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                alertDialog.dismiss();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }



    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    public static File compressImageFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // Calculate the compression ratio based on the original file size
        int compressionRatio = calculateCompressionRatio(bitmap);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio, outputStream);

        File compressedFile = createTempFile();

        FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);
        outputStream.writeTo(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();

        return compressedFile;
    }

    private static int calculateCompressionRatio(Bitmap bitmap) {
        final long MAX_FILE_SIZE_BYTES = 1 * 1024 * 1024; // 1MB
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        long originalFileSize = outputStream.size();
        int compressionRatio = 100;
        while ((originalFileSize > MAX_FILE_SIZE_BYTES) && (compressionRatio > 0)) {
            compressionRatio -= 10;
            originalFileSize = originalFileSize / 2; // Decrease by half to estimate the new file size after compression
        }
        return compressionRatio;
    }

    private static File createTempFile() throws IOException {
        String fileName = "compressed_image_" + System.currentTimeMillis() + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, fileName);
    }
    public void uploadPost() throws IOException {
        if (!location)
        {
            Toast.makeText(this, "Please add your location!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri != null) {
            p1.setVisibility(View.VISIBLE);
            postButton.setEnabled(false);
            postButton.setAlpha(0.5f);
            selectedImageUri = Uri.fromFile(compressImageFromUri(MomentsActivity.this,selectedImageUri));

            long timestamp = System.currentTimeMillis();
            Toast.makeText(this, "Please wait, uploading post...", Toast.LENGTH_SHORT).show();

            StorageReference storageRef = storage.getReference().child("images/" + timestamp + ".jpg");
            storageRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    String caption = captionEditText.getText().toString();
                    if (caption.isEmpty())
                    {
                        caption="-null-";
                    }
                    SharedPreferences s = getSharedPreferences("USER",MODE_PRIVATE);

                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").push();
                    DatabaseReference imgurldb = db.child("imageUrl");              imgurldb.setValue(imageUrl);
                    DatabaseReference profilephotodb = db.child("profileImageUrl"); profilephotodb.setValue(photoUrl);
                    DatabaseReference captiondb = db.child("caption");              captiondb.setValue(caption);
                    DatabaseReference usernamedb = db.child("username");            usernamedb.setValue(userName);
                    DatabaseReference useriddb = db.child("userid");            useriddb.setValue(idBeforeAtSymbol_username);
                    DatabaseReference dbcity = db.child("city");            dbcity.setValue(city);
                    DatabaseReference dbstate = db.child("state");            dbstate.setValue(state);
                    DatabaseReference dbcountry = db.child("country");            dbcountry.setValue(country);
                    DatabaseReference dbtime = db.child("time");            dbtime.setValue(timeStamp);
                    DatabaseReference dbkey = db.child("key");            dbkey.setValue(s.getString("key",""));

                    Toast.makeText(this, "Post will be uploaded soon!", Toast.LENGTH_SHORT).show();
                    // send noti1
                    {

                        DatabaseReference cx = FirebaseDatabase.getInstance().getReference().child("users")
                                .child(attr.getMykey()).child("Followers");
                        cx.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotmyfollowers) {
                                if (snapshotmyfollowers.exists())
                                {
                                    for (DataSnapshot dsmyfkey: snapshotmyfollowers.getChildren())
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
                                                            DatabaseReference emdb = userdb.child(dss.getKey()).child("email");
                                                            emdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshotem) {
                                                                    if (snapshotem.exists() && snapshotem.getValue(String.class).startsWith(dsmyfkey.getKey().toString()))
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
                                                                                    String message = attr.getMyname().concat(" added a new post");
                                                                                    String title = "New Post";
                                                                                    NotificationSender.sendNotification(token,title,message);

                                                                                    DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                            .child(dss.getKey()).child("Notifications");
                                                                                    DatabaseReference notificationdot = userthis.child("Notidot");
                                                                                    notificationdot.setValue("1");

                                                                                    DatabaseReference notikey = userthis.push();
                                                                                    DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                                    DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                                    DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                                    DatabaseReference pid = notikey.child("postid"); pid.setValue(db.getKey().toString());
                                                                                    DatabaseReference type = notikey.child("type"); type.setValue("post");
                                                                                    DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                                    toload=false;
                                                                                    startActivity(new Intent(MomentsActivity.this, MomentsActivity.class));
                                                                                    finish();

                                                                                }
                                                                                else {

                                                                                    String message = attr.getMyname().concat(" added a new post");
                                                                                    String title = "New Post";
                                                                                    DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                            .child(dss.getKey()).child("Notifications");
                                                                                    DatabaseReference notificationdot = userthis.child("Notidot");
                                                                                    notificationdot.setValue("1");

                                                                                    DatabaseReference notikey = userthis.push();
                                                                                    DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                                    DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                                    DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                                    DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                                    DatabaseReference pid = notikey.child("postid"); pid.setValue(db.getKey().toString());
                                                                                    toload=false;
                                                                                    DatabaseReference type = notikey.child("type"); type.setValue("post");
                                                                                    startActivity(new Intent(MomentsActivity.this, MomentsActivity.class));
                                                                                    finish();
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
                                    toload=false;
                                    startActivity(new Intent(MomentsActivity.this, MomentsActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                });
            }).addOnFailureListener(e -> {
                postButton.setEnabled(true);
                postButton.setAlpha(1.0f);
                Toast.makeText(MomentsActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        else {
            p1.setVisibility(View.VISIBLE);
            postButton.setEnabled(false);
            postButton.setAlpha(0.5f);
            String caption = captionEditText.getText().toString();
            if (caption.isEmpty())
            {
                postButton.setEnabled(true);
                postButton.setAlpha(1.0f);
                Toast.makeText(this, "Nothing to post!", Toast.LENGTH_SHORT).show();
                return;
            }
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            SharedPreferences s = getSharedPreferences("USER",MODE_PRIVATE);

            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").push();
            DatabaseReference imgurldb = db.child("imageUrl");              imgurldb.setValue("-null-");
            DatabaseReference profilephotodb = db.child("profileImageUrl"); profilephotodb.setValue(photoUrl);
            DatabaseReference captiondb = db.child("caption");              captiondb.setValue(caption);
            DatabaseReference usernamedb = db.child("username");            usernamedb.setValue(userName);
            DatabaseReference useriddb = db.child("userid");            useriddb.setValue(idBeforeAtSymbol_username);
            DatabaseReference dbcity = db.child("city");            dbcity.setValue(city);
            DatabaseReference dbstate = db.child("state");            dbstate.setValue(state);
            DatabaseReference dbcountry = db.child("country");            dbcountry.setValue(country);
            DatabaseReference dbtime = db.child("time");            dbtime.setValue(timeStamp);
            DatabaseReference dbkey = db.child("key");            dbkey.setValue(s.getString("key",""));

            Toast.makeText(this, "Post will be uploaded soon!", Toast.LENGTH_SHORT).show();
            // send noti1
            {

                DatabaseReference cx = FirebaseDatabase.getInstance().getReference().child("users")
                        .child(attr.getMykey()).child("Followers");
                cx.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshotmyfollowers) {
                        if (snapshotmyfollowers.exists())
                        {
                            for (DataSnapshot dsmyfkey: snapshotmyfollowers.getChildren())
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
                                                    DatabaseReference emdb = userdb.child(dss.getKey()).child("email");
                                                    emdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshotem) {
                                                            if (snapshotem.exists() && snapshotem.getValue(String.class).startsWith(dsmyfkey.getKey().toString()))
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
                                                                            String message = attr.getMyname().concat(" added a new post");
                                                                            String title = "New Post";
                                                                            NotificationSender.sendNotification(token,title,message);

                                                                            DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                    .child(dss.getKey()).child("Notifications");
                                                                            DatabaseReference notificationdot = userthis.child("Notidot");
                                                                            notificationdot.setValue("1");

                                                                            DatabaseReference notikey = userthis.push();
                                                                            DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                            DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                            DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                            DatabaseReference type = notikey.child("type"); type.setValue("post");
                                                                            DatabaseReference pid = notikey.child("postid"); pid.setValue(db.getKey().toString());
                                                                            DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                            toload=false;

                                                                            startActivity(new Intent(MomentsActivity.this, MomentsActivity.class));
                                                                            finish();

                                                                        }
                                                                        else {

                                                                            String message = attr.getMyname().concat(" added a new post");
                                                                            String title = "New Post";
                                                                            DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                    .child(dss.getKey()).child("Notifications");
                                                                            DatabaseReference notificationdot = userthis.child("Notidot");
                                                                            notificationdot.setValue("1");

                                                                            DatabaseReference notikey = userthis.push();
                                                                            DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                            DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                            DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                            DatabaseReference type = notikey.child("type"); type.setValue("post");
                                                                            DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                            DatabaseReference pid = notikey.child("postid"); pid.setValue(db.getKey().toString());
                                                                            toload=false;

                                                                            startActivity(new Intent(MomentsActivity.this, MomentsActivity.class));
                                                                            finish();
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
                            toload=false;
                            startActivity(new Intent(MomentsActivity.this, MomentsActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
    }
    public void resetAll()
    {
        momentslayout.setVisibility(View.GONE);
        searchlayout.setVisibility(View.GONE);
        profilelayout.setVisibility(View.GONE);
        uploadpostlayout.setVisibility(View.GONE);
        tourlayout.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            imgcard.setVisibility(View.VISIBLE);

        }
    }
    ///////////////////////////////


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        if (CommentLayout.getVisibility() == View.VISIBLE) {
            CommentLayout.setVisibility(View.GONE);
        }
        else if (momentslayout.getVisibility()==View.GONE)
        {
            resetAll();
            ResetColors();
            homebutton.setColorFilter(Color.parseColor("#FF8C42"));
            momentslayout.setVisibility(View.VISIBLE);
        }
        else {
            toload = false;
            finish();
        }
    }
    private void buildAlertMessageNoGps2()
    {
        View view = LayoutInflater.from(MomentsActivity.this).inflate(R.layout.customalertfinput,null);
        TextView titletv,savebtn,cancelbtn,textgps;
        EditText enterinput;

        textgps = view.findViewById(R.id.gps);
        titletv = view.findViewById(R.id.title);
        savebtn = view.findViewById(R.id.savebutton);
        cancelbtn = view.findViewById(R.id.cancelbutton);
        enterinput = view.findViewById(R.id.getinputtextd);
        savebtn.setText("Enable");
        enterinput.setVisibility(View.GONE);
        textgps.setVisibility(View.VISIBLE);
        x = 1;
        AlertDialog.Builder alert = new AlertDialog.Builder(MomentsActivity.this);
        alert.setView(view);
        alert.setCancelable(false);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

        titletv.setText("Enable GPS");
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                alertDialog.dismiss();
                x=0;
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                x=0;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSIONs) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                startLocationUpdates();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable location features)
            }
        }
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
            }
            else {
                // Permission denied, handle accordingly (e.g., show a message or disable location features)
            }
        }
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
            }
        }

    }
    public  void requestPostNotificationPermission() {
        // Check if the permission is already granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android Oreo (API 26) and above, check if the app has the required permission
            if (ContextCompat.checkSelfPermission(MomentsActivity.this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(MomentsActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }
    // Method to request location permission
    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(MomentsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(MomentsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            // Permission is already granted, start location updates
            startLocationUpdates();
        }
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

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot postid: snapshot.getChildren())
                    {
                        DatabaseReference imgurldb = db.child(postid.getKey()).child("imageUrl");
                        DatabaseReference captiondb = db.child(postid.getKey()).child("caption");
                        DatabaseReference usernamedb = db.child(postid.getKey()).child("username");
                        DatabaseReference userid = db.child(postid.getKey()).child("userid");
                        DatabaseReference dbcity = db.child(postid.getKey()).child("city");
                        DatabaseReference dbstate = db.child(postid.getKey()).child("state");
                        DatabaseReference dbcountry = db.child(postid.getKey()).child("country");
                        DatabaseReference dbtime = db.child(postid.getKey()).child("time");
                        DatabaseReference dbkey = db.child(postid.getKey()).child("key");

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
                                                                                                imgurldb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot snapshotpostimg) {
                                                                                                        if (snapshotpostimg.exists())
                                                                                                        {
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
                                                                                                                                        @SuppressLint("NotifyDataSetChanged")
                                                                                                                                        @Override
                                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshotusername) {
                                                                                                                                            if (snapshotusername.exists())
                                                                                                                                            {
                                                                                                                                                POSTID[0].add( postid.getKey());
                                                                                                                                                USERID[0].add( snapshotuserid.getValue(String.class));
                                                                                                                                                POSTIMAGE[0].add( snapshotpostimg.getValue(String.class));
                                                                                                                                                PROFILEPHOTO[0].add( snapshotprofileimg.getValue(String.class));
                                                                                                                                                CAPTION[0].add( snapshotcaption.getValue(String.class));
                                                                                                                                                USERNAME[0].add( snapshotusername.getValue(String.class));
                                                                                                                                                STATE[0].add( snapshotstate.getValue(String.class));
                                                                                                                                                COUNTRY[0].add( snapshotcountry.getValue(String.class));
                                                                                                                                                CITY[0].add( snapshotcity.getValue(String.class));
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
                                                                                                                                                        } catch (ParseException e) {
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

                                                                                                                                                setPost = new SetPost(MomentsActivity.this,
                                                                                                                                                        POSTID[0],
                                                                                                                                                        POSTIMAGE[0],
                                                                                                                                                        PROFILEPHOTO[0],
                                                                                                                                                        CAPTION[0],
                                                                                                                                                        USERNAME[0], USERID[0], CITY[0], STATE[0], COUNTRY[0], TIME[0], KEY[0],"1");
                                                                                                                                                setPost.notifyDataSetChanged();
                                                                                                                                                recyclerView.setAdapter(setPost);
                                                                                                                                                loadingposts.setVisibility(View.GONE);
                                                                                                                                                swipeRefreshLayout.setRefreshing(false);

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
                }
                else {
                    Toast.makeText(MomentsActivity.this, "Oops, Currently No Post to Show!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

class EVENTS_ADAPTER2 extends  RecyclerView.Adapter<EVENTS_ADAPTER2.EVENTS_ADAPTER_CHILD2>
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

    public EVENTS_ADAPTER2(Context c, ArrayList<String> EVENTKEY, ArrayList<String> NAME, ArrayList<String> EMAIL, ArrayList<String> USERKEY, ArrayList<String> DESCRIPTION, ArrayList<String> CITY, ArrayList<String> COUNTRY, ArrayList<String> FROMDATE, ArrayList<String> TODATE, ArrayList<String> IMAGEURL, ArrayList<String> CREATEDON)
    {
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
    public EVENTS_ADAPTER_CHILD2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.vp_layout,parent,false);
        return new EVENTS_ADAPTER_CHILD2(v);
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
    public void onBindViewHolder(@NonNull EVENTS_ADAPTER_CHILD2 holder, @SuppressLint("RecyclerView")
    int position) {

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
                    holder.joinnowbutton.setText("Joining...");
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
                                holder.membersjoined.setText("Members\n"+String.valueOf(total));

                                db1.setValue(String.valueOf(total));

                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users")
                                        .child(attr.getMykey()).child("Events").child(EVENTKEY.get(position));
                                db.setValue("1");
                                //addevntmember
                                DatabaseReference mmbr = FirebaseDatabase.getInstance().getReference().child("Events")
                                        .child(EVENTKEY.get(position)).child("Members").child(attr.getMykey());
                                mmbr.setValue("1");

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
        });

        if (USERKEY.get(position).equals(attr.getMykey()))
        {
            holder.joinnowbutton.setText("Tap to chat");
            holder.morebutton.setVisibility(View.GONE);
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
        ChaekTime(holder,position);
        holder.viewallevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.startActivity(new Intent(c,MyEvents.class));
            }
        });
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
        holder.createeventinstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(c,MyEvents.class);
//                i.putExtra("instant",true);
//                c.startActivity(i);
            }
        });

        Glide.with(c).load(IMAGEURL.get(position)).into(holder.bgimage);
        String monthdateformatfromdate = convertToCustomFormat(FROMDATE.get(position));
        holder.namewithvisitingdate.setText("Visiting "+CITY.get(position)+", "+COUNTRY.get(position)+" on "+monthdateformatfromdate);
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
    public void ChaekTime(EVENTS_ADAPTER2.EVENTS_ADAPTER_CHILD2 holder, int position)
    {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseReference checkevent = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("Events").child(EVENTKEY.get(position));
                checkevent.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            if (isStartTimeBeforeEndTime(FROMDATE.get(position),getCurrentTime(),dateFormat,dateFormat ))
                            {
                                holder.joinnowbutton.setText("Tap to chat");
                                holder.startedcardvisiblity.setVisibility(View.VISIBLE);
                            }
                            else {
                                holder.startedcardvisiblity.setVisibility(View.GONE);
                                ChaekTime(holder,position);
                            }
                        }else if (USERKEY.get(position).equals(attr.getMykey()))
                        {
                            holder.joinnowbutton.setText("Tap to chat");
                            holder.morebutton.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },1000);

    }

    @Override
    public int getItemCount() {
        return EVENTKEY.size();
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

    static class EVENTS_ADAPTER_CHILD2 extends RecyclerView.ViewHolder{
        ImageView bgimage,profileimage,morebutton,createeventinstant;
        TextView createdon,name,countrywithdayandnight,description,membersjoined,namewithvisitingdate;

        TextView joinnowbutton,viewallevents;
        CardView joinchat,startedcardvisiblity;

        public EVENTS_ADAPTER_CHILD2(@NonNull View v) {
            super(v);
            createeventinstant = v.findViewById(R.id.createeventinstant);
            bgimage = v.findViewById(R.id.bgimage);
            viewallevents = v.findViewById(R.id.viewallevents);
            startedcardvisiblity = v.findViewById(R.id.startedcardvisiblity);
            createdon = v.findViewById(R.id.createdon);
            name = v.findViewById(R.id.name);
            countrywithdayandnight = v.findViewById(R.id.countrywithdayandnight);
            description = v.findViewById(R.id.description);
            membersjoined = v.findViewById(R.id.membersjoined);
            profileimage = v.findViewById(R.id.profileimage);
            namewithvisitingdate = v.findViewById(R.id.namewithvisitingdate);
            joinnowbutton = v.findViewById(R.id.joinnowbutton);
            joinchat = v.findViewById(R.id.joinchat);
            morebutton = v.findViewById(R.id.morebutton);
        }
    }
}

class SEARCHUSER extends RecyclerView.Adapter<SEARCHUSER.SEARCHUSER_CHILD>
{
    Context c;
    ArrayList<String> EMAILS = new ArrayList<>();
    ArrayList<String> NAMES = new ArrayList<>();
    ArrayList<String> KEYS = new ArrayList<>();
    ArrayList<String> IMAGES = new ArrayList<>();

    public SEARCHUSER(Context c, ArrayList<String> EMAILS, ArrayList<String> NAMES, ArrayList<String> KEYS,ArrayList<String> IMAGES) {
        this.c = c;
        this.IMAGES = IMAGES;
        this.EMAILS = EMAILS;
        this.NAMES = NAMES;
        this.KEYS = KEYS;
    }

    @NonNull
    @Override
    public SEARCHUSER_CHILD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.searchuserlayout,parent,false);
        return new SEARCHUSER_CHILD(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SEARCHUSER_CHILD holder, @SuppressLint("RecyclerView") int position) {
        if (!EMAILS.get(position).equals("-"))
        {
            holder.username.setText(EMAILS.get(position));
        }
        Glide.with(c).load(IMAGES.get(position)).into(holder.profileimage);
        holder.name.setText(NAMES.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(KEYS.get(position)).child("email");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            Intent  i = new Intent(c, ViewerProfile.class);
                            i.putExtra("key",KEYS.get(position));
                            i.putExtra("email",snapshot.getValue(String.class));
                            c.startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return KEYS.size();
    }

    static class SEARCHUSER_CHILD extends RecyclerView.ViewHolder{

        TextView name,username;
        LinearLayout layout;
        ImageView profileimage;
        public SEARCHUSER_CHILD(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.name);
            username = v.findViewById(R.id.username);
            layout = v.findViewById(R.id.layout);
            profileimage = v.findViewById(R.id.profileimage);
        }
    }
}



class CITYIES_FILTER extends RecyclerView.Adapter<CITYIES_FILTER.CITYIES_FILTER_CHILD>
{
    Context c;
    static ArrayList<String> CITY;
    public static ArrayList<String> FILTERCITYLIST;
    public static ArrayList<CITYIES_FILTER_CHILD> h;

    public CITYIES_FILTER(Context c, ArrayList<String> CITY) {
        h = new ArrayList<>();
        this.c = c;
        this.FILTERCITYLIST = new ArrayList<>();
        this.CITY = new ArrayList<>();
        this.CITY.addAll(CITY);

        // Remove duplicates using HashSet
        HashSet<String> set = new HashSet<>(CITY);
        this.CITY.clear();
        this.CITY.addAll(set);

        if (this.CITY.size()>=4)
        {
            this.CITY.add(4,"View More");
        }


    }

    @NonNull
    @Override
    public CITYIES_FILTER_CHILD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(c).inflate(R.layout.cities_layout,parent,false);
       return new CITYIES_FILTER_CHILD(v);
    }
    public static void Call(CITYIES_FILTER_CHILD holder)
    {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!FILTERCITYLIST.isEmpty())
                {
                    if (FILTERCITYLIST.size()>=2)
                    {
                        MomentsActivity.remainingfilter.setVisibility(View.VISIBLE);
                        MomentsActivity.remainingfilter.setText("Clear all "+FILTERCITYLIST.size()+" filters");
                    }
                    else {
                        MomentsActivity.remainingfilter.setVisibility(View.VISIBLE);
                        MomentsActivity.remainingfilter.setText("Clear "+FILTERCITYLIST.get(0)+" filter");

                    }
                    for (String c: CITY)
                    {
                        holder.cancelfilter.setVisibility(View.GONE);
                    }
                    for (String c: FILTERCITYLIST)
                    {
                        if (c.equals(holder.city.getText().toString().trim()))
                        {
                            holder.cancelfilter.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else {
                    for (CITYIES_FILTER_CHILD c: CITYIES_FILTER.h)
                    {
                        c.cancelfilter.setVisibility(View.GONE);
                    }
                    SetPost setPost = MomentsActivity.setPost;
                    setPost.ApplyFilter(FILTERCITYLIST);
                    MomentsActivity.remainingfilter.setVisibility(View.GONE);

                }
                CITYIES_FILTER.h.add(holder);

//            Call(holder);
            }
        },0);



    }

    @Override
    public void onBindViewHolder(@NonNull CITYIES_FILTER_CHILD holder, @SuppressLint("RecyclerView") int position) {

        Call(holder);

        holder.city.setText(CITY.get(position));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.city.getText().toString().trim().equals("View More"))
                {
                    PopupMenu popupMenu = new PopupMenu(c,holder.card);
                    for (String city: CITY)
                    {
                        if (!city.equals("View More"))
                        {
                            popupMenu.getMenu().add(city);
                        }
                    }
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            for (String city: CITY)
                            {
                               if (item.getTitle().equals(city))
                               {
                                   if (FILTERCITYLIST.isEmpty())
                                   {
                                       FILTERCITYLIST.add(city);
                                       CITY.remove(CITY.indexOf(city));
                                       CITY.add(0,city);
                                       if (CITY.size()>=4)
                                       {
                                           CITY.add(4,"View More");
                                       }
                                       notifyDataSetChanged();

                                       SetPost setPost = MomentsActivity.setPost;
                                       setPost.ApplyFilter(FILTERCITYLIST);
                                   }
                                   else {
                                       if (!FILTERCITYLIST.contains(city))
                                       {
                                           FILTERCITYLIST.add(city);
                                           CITY.remove(CITY.indexOf(city));
                                           CITY.add(0,city);
                                           if (CITY.size()>=4)
                                           {
                                               CITY.add(4,"View More");
                                           }
                                           notifyDataSetChanged();
                                           SetPost setPost = MomentsActivity.setPost;
                                           setPost.ApplyFilter(FILTERCITYLIST);
                                       }
                                       else {
                                           Toast.makeText(c, "Filter already added...", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                                   break;

                               }

                            }
                            return true;
                        }
                    });

                }
                else {
                    if (FILTERCITYLIST.isEmpty())
                    {
                        FILTERCITYLIST.add(CITY.get(position));
                        holder.cancelfilter.setVisibility(View.VISIBLE);
                        SetPost setPost = MomentsActivity.setPost;
                        setPost.ApplyFilter(FILTERCITYLIST);
                    }
                    else {
                        if (!FILTERCITYLIST.contains(CITY.get(position)))
                        {
                            FILTERCITYLIST.add(CITY.get(position));
                            holder.cancelfilter.setVisibility(View.VISIBLE);
                            SetPost setPost = MomentsActivity.setPost;
                            setPost.ApplyFilter(FILTERCITYLIST);

                        }
                    }
                }

                Call(holder);

            }
        });
        holder.cancelfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call(holder);
                FILTERCITYLIST.remove(CITY.get(position));
                holder.cancelfilter.setVisibility(View.GONE);
                SetPost setPost = MomentsActivity.setPost;
                setPost.ApplyFilter(FILTERCITYLIST);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (CITY.size()>=5)
        {
            return 5;
        }
        else {
            return CITY.size();
        }
    }

    static class CITYIES_FILTER_CHILD extends PostAdapter.ViewHolder{
        TextView city;
        ImageView cancelfilter;
        LinearLayout card;
        boolean selected = false;
        public CITYIES_FILTER_CHILD(@NonNull View v) {
            super(v);
            city = v.findViewById(R.id.city);
            card = v.findViewById(R.id.card);
            cancelfilter = v.findViewById(R.id.cancelfilter);
        }
    }
}
class SetPost extends RecyclerView.Adapter<SetPost.SetPostChild>
{
    Context context;
    String activity;
    ArrayList<String> POSTID = new ArrayList<>();
    ArrayList<String> POSTIMAGE = new ArrayList<>();
    ArrayList<String> PROFILEPHOTO = new ArrayList<>();
    ArrayList<String> CAPTION = new ArrayList<>();
    ArrayList<String> USERNAME = new ArrayList<>();
    ArrayList<String> USERID = new ArrayList<>();
    ArrayList<String> CITY = new ArrayList<>();
    ArrayList<String> STATE = new ArrayList<>();
    ArrayList<String> COUNTRY = new ArrayList<>();
    ArrayList<String> TIME = new ArrayList<>();
    ArrayList<String> KEY = new ArrayList<>();

    ArrayList<String> POSTIDCOPY = new ArrayList<>();
    ArrayList<String> POSTIMAGECOPY = new ArrayList<>();
    ArrayList<String> PROFILEPHOTOCOPY = new ArrayList<>();
    ArrayList<String> CAPTIONCOPY = new ArrayList<>();
    ArrayList<String> USERNAMECOPY = new ArrayList<>();
    ArrayList<String> USERIDCOPY = new ArrayList<>();
    ArrayList<String> CITYCOPY = new ArrayList<>();
    ArrayList<String> STATECOPY = new ArrayList<>();
    ArrayList<String> COUNTRYCOPY = new ArrayList<>();
    ArrayList<String> TIMECOPY = new ArrayList<>();
    ArrayList<String> KEYCOPY = new ArrayList<>();
    MyAttr attr;

    public SetPost( Context context, ArrayList<String> POSTID, ArrayList<String> POSTIMAGE, ArrayList<String> PROFILEPHOTO, ArrayList<String> CAPTION, ArrayList<String> USERNAME, ArrayList<String> USERID, ArrayList<String> CITY, ArrayList<String> STATE, ArrayList<String> COUNTRY,ArrayList<String> TIME,ArrayList<String> KEY,String activity) {
        this.context = context;
        this.POSTID.addAll(POSTID);
        this.POSTIMAGE.addAll(POSTIMAGE);
        this.PROFILEPHOTO.addAll(PROFILEPHOTO);
        this.CAPTION.addAll(CAPTION);
        this.USERNAME.addAll(USERNAME);
        this.USERID.addAll(USERID);
        this.CITY.addAll(CITY);
        this.STATE.addAll(STATE);
        this.COUNTRY.addAll(COUNTRY);
        this.TIME.addAll(TIME);
        this.KEY.addAll(KEY);
        this.activity = activity;
        this.POSTIDCOPY.addAll(POSTID);
        this.POSTIMAGECOPY.addAll(POSTIMAGE);
        this.PROFILEPHOTOCOPY .addAll(PROFILEPHOTO);
        this.CAPTIONCOPY.addAll(CAPTION);
        this.USERNAMECOPY.addAll(USERNAME);
        this.USERIDCOPY .addAll(USERID);
        this.CITYCOPY .addAll(CITY);
        this.STATECOPY .addAll(STATE);
        this.COUNTRYCOPY .addAll(COUNTRY);
        this.TIMECOPY.addAll(TIME);
        this.KEYCOPY.addAll(KEY);
        attr = new MyAttr(context);
        CITYIES_FILTER cityiesFilter = new CITYIES_FILTER(context,CITY);
        MomentsActivity.citiesfilter.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        MomentsActivity.citiesfilter.setAdapter(cityiesFilter);

     }

    private void showAlertDialog(Context context,int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // Set up the layout for the AlertDialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alert_dialog_layout, null);
        alertDialogBuilder.setView(view);
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Get a reference to the EditText in the layout
        final EditText editText = view.findViewById(R.id.edittext);
        final TextView reportbtn = view.findViewById(R.id.reportbtn);
        final TextView cancelbtn = view.findViewById(R.id.cancelbtn);

        reportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(context))
                {
                    if (!editText.getText().toString().trim().isEmpty())
                    {
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("REPORTS").push();
                        DatabaseReference reportedby = db.child("ReportedBy"); reportedby.setValue(MomentsActivity.idBeforeAtSymbol_username);
                        DatabaseReference postid = db.child("PostId"); postid.setValue(POSTID.get(position));
                        DatabaseReference description = db.child("Description"); description.setValue(editText.getText().toString().trim());
                        alertDialog.dismiss();
                        Toast.makeText(context, "Post Reported...", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Create and show the AlertDialog
        alertDialog.show();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the cancel action if needed
                dialog.dismiss();
            }
        });

    }
     @SuppressLint("NotifyDataSetChanged")
     public void ApplyFilter(ArrayList<String> FILTER_LIST)
     {
         try {
             {
                 POSTID.clear();
                 POSTIMAGE.clear();
                 PROFILEPHOTO.clear();
                 CAPTION.clear();
                 USERNAME.clear();
                 USERID.clear();
                 TIME.clear();
                 KEY.clear();
//                 CITY.clear();
//                 STATE.clear();
//                 COUNTRY.clear();
                 notifyDataSetChanged();
                 if (FILTER_LIST.isEmpty())
                 {
                     POSTID.addAll(POSTIDCOPY);
                     POSTIMAGE.addAll(POSTIMAGECOPY);
                     PROFILEPHOTO.addAll(PROFILEPHOTOCOPY);
                     CAPTION.addAll(CAPTIONCOPY);
                     USERNAME.addAll(USERNAMECOPY);
                     USERID.addAll(USERIDCOPY);
                     TIME.addAll(TIMECOPY);
                     KEY.addAll(KEYCOPY);
//                     COUNTRY.addAll(COUNTRYCOPY);
//                     CITY.addAll(CITYCOPY);
//                     STATE.addAll(STATECOPY);
                     return;
                 }

                 for (int i=0;i<POSTIDCOPY.size();i++) {

                     for (String filter: FILTER_LIST)
                     {
                         if (CITYCOPY.get(i).equals(filter))
                         {
                             POSTID.add(POSTIDCOPY.get(i));
                             POSTIMAGE.add(POSTIMAGECOPY.get(i));
                             PROFILEPHOTO.add(PROFILEPHOTOCOPY.get(i));
                             CAPTION.add(CAPTIONCOPY.get(i));
                             USERNAME.add(USERNAMECOPY.get(i));
                             USERID.add(USERIDCOPY.get(i));
                             TIME.add(TIMECOPY.get(i));
                             KEY.add(KEYCOPY.get(i));
//                             CITY.add(CITYCOPY.get(i));
//                             COUNTRY.add(COUNTRYCOPY.get(i));
//                             STATE.add(STATECOPY.get(i));
                             notifyDataSetChanged();
                             break;
                         }
                     }
                 }

             }
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
     }

    @NonNull
    @Override
    public SetPostChild onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_post, parent, false);
        return new SetPostChild(view);
    }

    @Override
    public void onViewRecycled(@NonNull SetPostChild holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.postImageView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SetPostChild holder, @SuppressLint("RecyclerView") int position) {
        if (!POSTIMAGE.get(position).trim().equals("-null-"))
        {
            holder.postImageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(POSTIMAGE.get(position)).fitCenter().into(holder.postImageView);
        }
//        else {
//            holder.postImageView.setVisibility(View.GONE);
//        }
        // check follow and following
        SharedPreferences sx90 = context.getSharedPreferences("USER", MODE_PRIVATE);
        DatabaseReference my = FirebaseDatabase.getInstance().getReference().child("users")
                .child(sx90.getString("key","")).child("Following")
                .child(USERID.get(position));
        my.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    // you are following this current person
                    holder.follow.setVisibility(View.GONE);
                    holder.following.setVisibility(View.VISIBLE);
                    if (KEY.size()>position){
                        if (KEY.get(position).equals(sx90.getString("key","")))
                        {
                            holder.following.setVisibility(View.GONE);
                            holder.follow.setVisibility(View.GONE);
                        }
                    }


                }
                else {
                    holder.follow.setVisibility(View.VISIBLE);
                    holder.following.setVisibility(View.GONE);
                    if (KEY.size()>position)
                    {
                        if (KEY.get(position).equals(sx90.getString("key","")))
                        {
                            holder.following.setVisibility(View.GONE);
                            holder.follow.setVisibility(View.GONE);
                        }
                    }

                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        // follow
        holder.textfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(context))
                {
                    // add this in my following
                    DatabaseReference myfollowing = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(sx90.getString("key","")).child("Following")
                            .child(USERID.get(position));
                    myfollowing.setValue("1");
                    // add me in his followers
                    // split my email
                    String myemail="";
                    String[] parts = sx90.getString("email","").split("@");
                    if (parts.length > 0) {
                        myemail = parts[0];
                    }

                    DatabaseReference hisfollower = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(KEY.get(position)).child("Followers")
                            .child(myemail);
                    hisfollower.setValue("1");
                    holder.follow.setVisibility(View.GONE);
                    holder.following.setVisibility(View.VISIBLE);

                    // send noti1
                    {
                        if (!attr.getMyEmail().equals(USERID.get(position)))
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
                                            DatabaseReference emdb = userdb.child(dss.getKey()).child("email");
                                            emdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshotem) {
                                                    if (snapshotem.exists() && snapshotem.getValue(String.class).startsWith(USERID.get(position)))
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
                    Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
                }
             
            }
        });
        holder.textfollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(context))
                {
                    // add this in my following
                    DatabaseReference myfollowing = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(sx90.getString("key","")).child("Following")
                            .child(USERID.get(position));
                    myfollowing.setValue(null);
                    // add me in his followers
                    // split my email
                    String myemail="";
                    String[] parts = sx90.getString("email","").split("@");
                    if (parts.length > 0) {
                        myemail = parts[0];
                    }

                    DatabaseReference hisfollower = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(KEY.get(position)).child("Followers")
                            .child(myemail);
                    hisfollower.setValue(null);
                    holder.follow.setVisibility(View.VISIBLE);
                    holder.following.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ViewerProfile.class);
                i.putExtra("key",KEY.get(position));
                i.putExtra("email",USERID.get(position)+"@gmail.com");
                context.startActivity(i);
            }
        });

        String timeString = TIME.get(position);
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.getDefault());
            Date date = inputFormat.parse(timeString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm", Locale.getDefault());
            String formattedTime = outputFormat.format(date);
            holder.posttime.setText("Posted on: "+formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.LIKES = new ArrayList<>();

        // get comments number
        {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
            DatabaseReference commments = db.child("comments");

            commments.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        // likes exists
                        int com=0;
                        for (DataSnapshot commentid: snapshot.getChildren())
                        {
                            com++;
                            holder.commentsCountTextView.setText(String.valueOf(com));
                        }
                    }
                    else {
                        // no likes
                        holder.commentsCountTextView.setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        //get likes number
        {
            DatabaseReference likesnodb = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position)).child("likes");
            likesnodb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        int total=0;
                        for (DataSnapshot likeid: snapshot.getChildren())
                        {
                            total++;
                            holder.likesCountTextView.setText(String.valueOf(total));
                            holder.LIKES.add(likeid.getKey());
                        }
                    }
                    else {
                        holder.likesCountTextView.setText(String.valueOf("0"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            DatabaseReference likesdb = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position)).child("likes").child(MomentsActivity.idBeforeAtSymbol_username);
            likesdb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                            holder.likeButton.setImageResource(R.drawable.like_button_solid);
                            holder.likeButton.setColorFilter(Color.parseColor("#FF8C42"));
                    }
                    else {
                        holder.likeButton.setImageResource(R.drawable.like_button);
                        holder.likeButton.setColorFilter(Color.parseColor("#FFFFFF"));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        if (!CAPTION.get(position).trim().equals("-null-"))
        {
            holder.captionTextView.setVisibility(View.VISIBLE);
        }
        else {
            holder.captionTextView.setVisibility(View.GONE);
        }
        holder.captionTextView.setText(CAPTION.get(position));
        holder.usernameTextView.setText(USERNAME.get(position));

        holder.deletepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.deletepost);

                if (USERID.get(position).equals(MomentsActivity.idBeforeAtSymbol_username))
                {
                    // my post
                    popupMenu.getMenu().add("Delete Post");
                }
                popupMenu.getMenu().add("Report");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        CharSequence title = item.getTitle();
                        if (title.equals("Report")) {
                            // report user
                            if (NetworkUtils.isNetworkAvailable(context))
                            {
                                showAlertDialog(context,position);

                            }
                            else {
                                Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (title.equals("Delete Post")) {
                            if (NetworkUtils.isNetworkAvailable(context))
                            {
                                // delete post
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
                                db.setValue(null);
                                Toast.makeText(context, "Post deleted...", Toast.LENGTH_SHORT).show();
                                POSTID.remove(position);
                                POSTIMAGE.remove(position);
                                PROFILEPHOTO.remove(position);
                                CAPTION.remove(position);
                                USERID.remove(position);
                                USERNAME.remove(position);

                                notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String userEmail = currentUser.getEmail();

                    if (userEmail != null) {
                        String[] parts = userEmail.split("@");

                        if (parts.length > 0) {
                            String idBeforeAtSymbol = parts[0];
                            if (holder.LIKES.size()!=0)
                            {
                                if (holder.LIKES.contains(idBeforeAtSymbol))
                                {
                                    Toast.makeText(context, "Post disliked", Toast.LENGTH_SHORT).show();
                                    holder.likeButton.setImageResource(R.drawable.like_button);
                                    holder.likeButton.setColorFilter(Color.parseColor("#FFFFFF"));

                                    int lik = Integer.parseInt(holder.likesCountTextView.getText().toString().trim())-1;
                                    if (lik>-1)
                                    {
                                        holder.likesCountTextView.setText(String.valueOf(lik));

                                    }
                                    else {
                                        holder.likesCountTextView.setText(String.valueOf(0));

                                    }
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
                                    DatabaseReference likes = db.child("likes").child(idBeforeAtSymbol);
                                    holder.LIKES.remove(idBeforeAtSymbol);
                                    likes.setValue(null);
                                }
                                else {
                                    Toast.makeText(context, "Post liked", Toast.LENGTH_SHORT).show();
                                    holder.likeButton.setImageResource(R.drawable.like_button_solid);
                                    holder.likeButton.setColorFilter(Color.parseColor("#FF8C42"));

                                    holder.likesCountTextView.setText(String.valueOf(Integer.parseInt(holder.likesCountTextView.getText().toString().trim())+1));
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
                                    DatabaseReference likes = db.child("likes").child(idBeforeAtSymbol);
                                    holder.LIKES.add(idBeforeAtSymbol);
                                    likes.setValue("1");

                                    // send noti1
                                    {
                                        if (!attr.getMyEmail().equals(USERID.get(position)))
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
                                                            DatabaseReference emdb = userdb.child(dss.getKey()).child("email");
                                                            emdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshotem) {
                                                                    if (snapshotem.exists() && snapshotem.getValue(String.class).startsWith(USERID.get(position)))
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
                                                                                    String message = attr.getMyname().concat(" liked your post");
                                                                                    String title = "New Like";
                                                                                    NotificationSender.sendNotification(token,title,message);

                                                                                    DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                            .child(dss.getKey()).child("Notifications");
                                                                                    DatabaseReference notificationdot = userthis.child("Notidot");
                                                                                    notificationdot.setValue("1");

                                                                                    DatabaseReference notikey = userthis.push();
                                                                                    DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                                    DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                                    DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                                    DatabaseReference positid = notikey.child("postid"); positid.setValue(POSTID.get(position));
                                                                                    DatabaseReference type = notikey.child("type"); type.setValue("like");
                                                                                    DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());


                                                                                }
                                                                                else {

                                                                                    String message = attr.getMyname().concat(" liked your post");
                                                                                    String title = "New Like";
                                                                                    DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                            .child(dss.getKey()).child("Notifications");
                                                                                    DatabaseReference notificationdot = userthis.child("Notidot");
                                                                                    notificationdot.setValue("1");

                                                                                    DatabaseReference notikey = userthis.push();
                                                                                    DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                                    DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                                    DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                                    DatabaseReference positid = notikey.child("postid"); positid.setValue(POSTID.get(position));
                                                                                    DatabaseReference type = notikey.child("type"); type.setValue("like");
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
                            }
                            else {
                                Toast.makeText(context, "Post liked", Toast.LENGTH_SHORT).show();
                                holder.likeButton.setImageResource(R.drawable.like_button_solid);
                                holder.likeButton.setColorFilter(Color.parseColor("#FF8C42"));

                                holder.likesCountTextView.setText(String.valueOf(Integer.parseInt(holder.likesCountTextView.getText().toString().trim())+1));
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
                                DatabaseReference likes = db.child("likes").child(idBeforeAtSymbol);
                                holder.LIKES.add(idBeforeAtSymbol);
                                likes.setValue("1");

                                // send noti1
                                {
                                    if (!attr.getMyEmail().equals(USERID.get(position)))
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
                                                        DatabaseReference emdb = userdb.child(dss.getKey()).child("email");
                                                        emdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshotem) {
                                                                if (snapshotem.exists() && snapshotem.getValue(String.class).startsWith(USERID.get(position)))
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
                                                                                String message = attr.getMyname().concat(" liked your post");
                                                                                String title = "New Like";
                                                                                NotificationSender.sendNotification(token,title,message);

                                                                                DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                        .child(dss.getKey()).child("Notifications");
                                                                                DatabaseReference notificationdot = userthis.child("Notidot");
                                                                                notificationdot.setValue("1");

                                                                                DatabaseReference notikey = userthis.push();
                                                                                DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                                DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                                DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                                DatabaseReference positid = notikey.child("postid"); positid.setValue(POSTID.get(position));
                                                                                DatabaseReference type = notikey.child("type"); type.setValue("like");
                                                                                DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());


                                                                            }
                                                                            else {

                                                                                String message = attr.getMyname().concat(" liked your post");
                                                                                String title = "New Like";
                                                                                DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                        .child(dss.getKey()).child("Notifications");
                                                                                DatabaseReference notificationdot = userthis.child("Notidot");
                                                                                notificationdot.setValue("1");

                                                                                DatabaseReference notikey = userthis.push();
                                                                                DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                                DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                                DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                                DatabaseReference positid = notikey.child("postid"); positid.setValue(POSTID.get(position));
                                                                                DatabaseReference type = notikey.child("type"); type.setValue("like");
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

                        }
                    } else {
                        Toast.makeText(context, "Something went's wrong!", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(context, "Something went's wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.equals("1")){
                    MomentsActivity.comments_recycler_view.setVisibility(View.GONE);
                    GetLikeandComments(position,holder,context);

                     if (holder.commentsCountTextView.getText().toString().trim().equals("0"))
                     {

                         Toast.makeText(context, "No comments yet!", Toast.LENGTH_SHORT).show();
                     }
                     else {
                         MomentsActivity.CommentLayout.setVisibility(View.VISIBLE);

                     }
                     
                }
                else   if (activity.equals("2")){
                    ViewPost.comments_recycler_view.setVisibility(View.GONE);
                    GetLikeandComments(position,holder,context);

                    if (holder.commentsCountTextView.getText().toString().trim().equals("0"))
                    {

                        Toast.makeText(context, "No comments yet!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ViewPost.CommentLayout.setVisibility(View.VISIBLE);

                    }

                }
            }
        });
        holder.sendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.addcomment.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(context, "Field required is empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position)).child("comments").push();
                    DatabaseReference value = db.child("value"); value.setValue(holder.addcomment.getText().toString().trim());
                    DatabaseReference usernamecomment = db.child("username"); usernamecomment.setValue(MomentsActivity.userName);
                    DatabaseReference userid = db.child("userid"); userid.setValue(MomentsActivity.idBeforeAtSymbol_username);
                    holder.addcomment.setText("");
                    holder.commentsCountTextView.setText(Html.fromHtml("<i>posting...</i>"));
                    GetLikeandComments(position,holder,context);


                    // send noti1
                    {
                        if (!attr.getMyEmail().equals(USERID.get(position)))
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
                                            DatabaseReference emdb = userdb.child(dss.getKey()).child("email");
                                            emdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshotem) {
                                                    if (snapshotem.exists() && snapshotem.getValue(String.class).startsWith(USERID.get(position)))
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
                                                                    String message = attr.getMyname().concat(" commented on your post");
                                                                    String title = "New Comment";
                                                                    NotificationSender.sendNotification(token,title,message);

                                                                    DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                            .child(dss.getKey()).child("Notifications");
                                                                    DatabaseReference notificationdot = userthis.child("Notidot");
                                                                    notificationdot.setValue("1");

                                                                    DatabaseReference notikey = userthis.push();
                                                                    DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                    DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                    DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                    DatabaseReference positid = notikey.child("postid"); positid.setValue(POSTID.get(position));
                                                                    DatabaseReference type = notikey.child("type"); type.setValue("comment");
                                                                    DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());


                                                                }
                                                                else {

                                                                    String message = attr.getMyname().concat(" commented on your post");
                                                                    String title = "New Comment";
                                                                    DatabaseReference userthis = FirebaseDatabase.getInstance().getReference().child("users")
                                                                            .child(dss.getKey()).child("Notifications");
                                                                    DatabaseReference notificationdot = userthis.child("Notidot");
                                                                    notificationdot.setValue("1");

                                                                    DatabaseReference notikey = userthis.push();
                                                                    DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                    DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                    DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                    DatabaseReference positid = notikey.child("postid"); positid.setValue(POSTID.get(position));
                                                                    DatabaseReference type = notikey.child("type"); type.setValue("comment");
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

            }
        });

        if (KEY.get(position).equals(attr.getMykey()))
        {
            updatephoto(context,holder,position);
        }
        else {
            Glide.with(context)
                    .load(PROFILEPHOTO.get(position))
                    .into(holder.postProfileImageView);
        }
    }
    public void updatephoto(Context c,SetPostChild holder,int position)
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MomentsActivity.toload)
                {
                    if (KEY.size()>position)
                    {
                        if (KEY.get(position).equals(attr.getMykey()))
                        {
                            SharedPreferences s = c.getSharedPreferences("USER",MODE_PRIVATE);
                            String u = s.getString("profileurl","");
                            Glide.with(c.getApplicationContext())
                                    .load(u)
                                    .into(holder.postProfileImageView);
                            updatephoto(c,holder,position);
                        }
                    }


                }

            }
        },1000);
    }
    public void GetLikeandComments(int position, SetPostChild holder,Context c)

    {

        try {
            ArrayList<String> COMMENTID = new ArrayList<>();
            ArrayList<String> USERID = new ArrayList<>();
            ArrayList<String> VALUE = new ArrayList<>();
            ArrayList<String> COMMENTUSERNAME = new ArrayList<>();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("POSTS").child(POSTID.get(position));
            DatabaseReference commments = db.child("comments");

            commments.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        // likes exists
                        int com=0;
                        for (DataSnapshot commentid: snapshot.getChildren())
                        {
                            com++;
                            DatabaseReference value = commments.child(commentid.getKey()).child("value");
                            DatabaseReference uid = commments.child(commentid.getKey()).child("userid");
                            DatabaseReference username = commments.child(commentid.getKey()).child("username");
                            int finalCom = com;
                            uid.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotuid) {
                                    if (snapshotuid.exists())
                                    {
                                        value.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshotvalue) {
                                                if (snapshotvalue.exists())
                                                {
                                                    username.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshotusername) {
                                                            if (snapshotusername.exists())
                                                            {
                                                                holder.commentsCountTextView.setText(String.valueOf(finalCom));
                                                                COMMENTID.add(commentid.getKey());
                                                                USERID.add(snapshotuid.getValue(String.class));
                                                                VALUE.add(snapshotvalue.getValue(String.class));
                                                                COMMENTUSERNAME.add(snapshotusername.getValue(String.class));
                                                                SetComments setComments = new SetComments(c,COMMENTID,VALUE,COMMENTUSERNAME,POSTID.get(position),USERID);

                                                                if (activity.equals("1"))
                                                                {

                                                                    MomentsActivity.comments_recycler_view.setAdapter(setComments);
                                                                    MomentsActivity.comments_recycler_view.setVisibility(View.VISIBLE);
                                                                }
                                                                else if (activity.equals("2"))
                                                                {

                                                                    ViewPost.comments_recycler_view.setAdapter(setComments);
                                                                    ViewPost.comments_recycler_view.setVisibility(View.VISIBLE);
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
                    else {
                        // no likes
                        holder.commentsCountTextView.setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return POSTID.size();
    }

    static class SetPostChild extends PostAdapter.ViewHolder{
        ArrayList<String> LIKES;
        ImageView deletepost;
        EditText addcomment;
        TextView sendcomment,locationtext,posttime;
        ImageView postImageView;
        // declare variable for profile name and image
        ImageView postProfileImageView;
        TextView usernameTextView;
        TextView captionTextView;
        TextView likesCountTextView;
        TextView commentsCountTextView;
        ImageView likeButton,commentButton;
        CardView follow,following;
        TextView textfollow,textfollowing;
        public SetPostChild(@NonNull View itemView) {
            super(itemView);
            sendcomment = itemView.findViewById(R.id.sendcomment);
            postImageView = itemView.findViewById(R.id.post_image);
            addcomment = itemView.findViewById(R.id.addcomment);
            postProfileImageView = itemView.findViewById(R.id.profile_post_image);
            captionTextView = itemView.findViewById(R.id.post_caption);
            usernameTextView = itemView.findViewById(R.id.post_username);
            likesCountTextView = itemView.findViewById(R.id.post_likes);
            commentsCountTextView = itemView.findViewById(R.id.post_comments);
            likeButton = itemView.findViewById(R.id.like_button);
            locationtext = itemView.findViewById(R.id.locationtext);
            follow = itemView.findViewById(R.id.follow);
            following = itemView.findViewById(R.id.following);
            textfollowing = itemView.findViewById(R.id.textfollowing);
            textfollow = itemView.findViewById(R.id.textfollow);
            commentButton = itemView.findViewById(R.id.comment_button);
            deletepost = itemView.findViewById(R.id.deletepost);
            posttime = itemView.findViewById(R.id.posttime);
        }
    }
}
class  SetComments extends RecyclerView.Adapter<SetComments.SetCommentsChild>
{

    Context c;
    ArrayList<String> COMMENTID = new ArrayList<>();
    ArrayList<String> VALUE = new ArrayList<>();
    ArrayList<String> USERID = new ArrayList<>();
    ArrayList<String> COMMENTUSERNAME = new ArrayList<>();
    String POSTID;
    public SetComments(Context c, ArrayList<String> COMMENTID, ArrayList<String> VALUE, ArrayList<String> COMMENTUSERNAME,String POSTID,ArrayList<String> USERID) {
        this.c = c;
        this.POSTID = POSTID;
        this.COMMENTID = COMMENTID;
        this.USERID = USERID;
        this.VALUE = VALUE;
        this.COMMENTUSERNAME = COMMENTUSERNAME;
    }

    @NonNull
    @Override
    public SetCommentsChild onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new SetCommentsChild(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SetCommentsChild holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(COMMENTUSERNAME.get(position));
        holder.value.setText(VALUE.get(position));

        if (MomentsActivity.idBeforeAtSymbol_username.equals(USERID.get(position)))
        {
            holder.moreforcomment.setVisibility(View.VISIBLE);
        }

        holder.moreforcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,holder.moreforcomment);
                popupMenu.getMenu().add("Delete comment");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (USERID.get(position).equals(MomentsActivity.idBeforeAtSymbol_username))
                            if (NetworkUtils.isNetworkAvailable(c))
                            {
                                DatabaseReference uid = FirebaseDatabase.getInstance().getReference().child("POSTS").
                                        child(POSTID).child("comments").child(COMMENTID.get(position));
                                uid.setValue(null);

                                COMMENTID.remove(position);
                                VALUE.remove(position);
                                USERID.remove(position);
                                COMMENTUSERNAME.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(c, "Comment deleted!", Toast.LENGTH_SHORT).show();


                            }


                        return  true;
                    }
                });
                popupMenu.show();

            }
        });

        holder.reply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.is_repply_visible)
                {
                    holder.is_repply_visible=true;
                    holder.addreply.setVisibility(View.VISIBLE);
                }
                else {
                    holder.is_repply_visible=false;
                    holder.addreply.setVisibility(View.GONE);
                    Toast.makeText(c, "uploading...", Toast.LENGTH_SHORT).show();

                    if (NetworkUtils.isNetworkAvailable(c))
                    {
                        String Replytext = holder.addreply.getText().toString().trim();
                        if (!Replytext.isEmpty())
                        {
                            DatabaseReference replydb = FirebaseDatabase.getInstance().getReference().child("POSTS").
                                    child(POSTID).child("comments").child(COMMENTID.get(position)).child("replies").push();
                            DatabaseReference name = replydb.child("username"); name.setValue(MomentsActivity.userName);
                            DatabaseReference value = replydb.child("reply"); value.setValue(Replytext);
                            DatabaseReference uid = replydb.child("userid"); uid.setValue(MomentsActivity.idBeforeAtSymbol_username);
                            holder.addreply.setText("");
                            Toast.makeText(c, "Uploaded...", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(c, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        holder.viewallreplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.replies_visibility==0)
                {
                    holder.recyclerview_replies.setVisibility(View.GONE);
                    holder.replies_visibility=1;
                    holder.viewallreplies.setText("Loading...");
                    // egt all replies
                    ArrayList<String> REPLYID = new ArrayList<>();
                    ArrayList<String> REPLYNAME = new ArrayList<>();
                    ArrayList<String> REPLYVALUE = new ArrayList<>();
                    ArrayList<String> USERID = new ArrayList<>();
                    DatabaseReference replydb = FirebaseDatabase.getInstance().getReference().child("POSTS").
                            child(POSTID).child("comments").child(COMMENTID.get(position)).child("replies");

                    replydb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotreply) {
                            if (snapshotreply.exists())
                            {
                                for (DataSnapshot replykey: snapshotreply.getChildren())
                                {
                                    DatabaseReference getusername = replydb.child(replykey.getKey()).child("username");
                                    DatabaseReference getrply = replydb.child(replykey.getKey()).child("reply");
                                    DatabaseReference uid = replydb.child(replykey.getKey()).child("userid");

                                    uid.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshotuid) {
                                            if (snapshotuid.exists())
                                            {
                                                getusername.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshotname) {
                                                        if (snapshotname.exists())
                                                        {
                                                            getrply.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshotreply) {
                                                                    if (snapshotreply.exists()){
                                                                        REPLYID.add(replykey.getKey());
                                                                        REPLYVALUE.add(snapshotreply.getValue().toString());
                                                                        REPLYNAME.add(snapshotname.getValue().toString());
                                                                        USERID.add(snapshotuid.getValue().toString());

                                                                        Replies replies = new Replies(c,REPLYID,REPLYNAME,REPLYVALUE,USERID,POSTID,COMMENTID.get(position));
                                                                        holder.recyclerview_replies.setAdapter(replies);
                                                                        holder.recyclerview_replies.setVisibility(View.VISIBLE);
                                                                        holder.viewallreplies.setText("Hide all replies");

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
                            else {
                                Toast.makeText(c, "No replies to show!", Toast.LENGTH_SHORT).show();
                                holder.replies_visibility=0;
                                holder.viewallreplies.setText("View all replies");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {

                    holder.replies_visibility=0;
                    holder.recyclerview_replies.setVisibility(View.GONE);
                    holder.viewallreplies.setText("View all replies");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return COMMENTID.size();
    }

    static class SetCommentsChild extends PostAdapter.ViewHolder{
        TextView name,value,reply_btn,viewallreplies;
        EditText addreply;
        boolean is_repply_visible = false;
        RecyclerView recyclerview_replies;
        ImageView moreforcomment;
        int replies_visibility=0;

        public SetCommentsChild(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.username);
            value = v.findViewById(R.id.commentTextTextView);
            addreply = v.findViewById(R.id.addreply);
            reply_btn = v.findViewById(R.id.replybutton);
            viewallreplies = v.findViewById(R.id.viewallreplies);
            recyclerview_replies = v.findViewById(R.id.recyclerview_replies);
            moreforcomment = v.findViewById(R.id.moreforcomment);
        }
    }
}

class Replies extends RecyclerView.Adapter<Replies.Replies_Child>
{
    Context c;
    ArrayList<String> USER_ID = new ArrayList<>();
    ArrayList<String> REPLY_ID = new ArrayList<>();
    ArrayList<String> REPLY_NAME = new ArrayList<>();
    ArrayList<String> REPLY_VALUE = new ArrayList<>();
    String POSTID,COMMENTID;

    public Replies(Context c, ArrayList<String> REPLY_ID, ArrayList<String> REPLY_NAME, ArrayList<String> REPLY_VALUE, ArrayList<String> USER_ID,String POSTID,String COMMENTID) {
        this.c = c;
        this.COMMENTID = COMMENTID;
        this.REPLY_ID = REPLY_ID;
        this.REPLY_NAME = REPLY_NAME;
        this.REPLY_VALUE = REPLY_VALUE;
        this.USER_ID = USER_ID;
        this.POSTID = POSTID;
    }

    @NonNull
    @Override
    public Replies_Child onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(c).inflate(R.layout.reply_item,parent,false);
       return new Replies_Child(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Replies_Child holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(REPLY_NAME.get(position));
        holder.value.setText(REPLY_VALUE.get(position));
        if (USER_ID.get(position).equals(MomentsActivity.idBeforeAtSymbol_username))
        {
            holder.moreforreply.setVisibility(View.VISIBLE);
        }

        holder.moreforreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c,holder.moreforreply);
                popupMenu.getMenu().add("Delete reply");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                       if (USER_ID.get(position).equals(MomentsActivity.idBeforeAtSymbol_username))
                            if (NetworkUtils.isNetworkAvailable(c))
                            {
                                DatabaseReference uid = FirebaseDatabase.getInstance().getReference().child("POSTS").
                                        child(POSTID).child("comments").child(COMMENTID).child("replies").child(REPLY_ID.get(position));
                                uid.setValue(null);
                                USER_ID.remove(position);
                                REPLY_NAME.remove(position);
                                REPLY_ID.remove(position);
                                REPLY_VALUE.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(c, "Reply deleted!", Toast.LENGTH_SHORT).show();


                            }


                        return  true;
                    }
                });
                popupMenu.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return REPLY_ID.size();
    }

    static class Replies_Child extends PostAdapter.ViewHolder{
        TextView name,value;
        ImageView moreforreply;
        public Replies_Child(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.username);
            value = v.findViewById(R.id.reply);
            moreforreply = v.findViewById(R.id.moreforreply);
        }
    }
}

class NOTIFICATIONS extends RecyclerView.Adapter<NOTIFICATIONS.NOTIFICATIONS_CHILD>
{
    Context c;

    MyAttr attr;
    ArrayList<String> NOTIID = new ArrayList<>();
    ArrayList<String> CHATID = new ArrayList<>();
    ArrayList<String> EVENTID = new ArrayList<>();
    ArrayList<String> TYPE = new ArrayList<>();
    ArrayList<String> TITLE = new ArrayList<>();
    ArrayList<String> MESSAGE = new ArrayList<>();
    ArrayList<String> USERID = new ArrayList<>();
    ArrayList<String> POSTID = new ArrayList<>();
    ArrayList<String> DATETIME = new ArrayList<>();

    public NOTIFICATIONS(Context c, ArrayList<String> NOTIID, ArrayList<String> EVENTID, ArrayList<String> TYPE, ArrayList<String> TITLE, ArrayList<String> MESSAGE, ArrayList<String> USERID, ArrayList<String> POSTID, ArrayList<String> DATETIME,ArrayList<String> CHATID) {
        this.c = c;
        this.CHATID = CHATID;
        this.NOTIID = NOTIID;
        this.EVENTID = EVENTID;
        this.TYPE = TYPE;
        this.TITLE = TITLE;
        this.MESSAGE = MESSAGE;
        this.USERID = USERID;
        this.POSTID = POSTID;
        this.DATETIME = DATETIME;
        attr = new MyAttr(c);
    }

    @NonNull
    @Override
    public NOTIFICATIONS_CHILD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.notification_layout,parent,false);
        return new NOTIFICATIONS_CHILD(v);
    }

    public static String getRandomDateTime() {
        // Get current date
        Calendar calendar = Calendar.getInstance();

        // Subtract 1 month from the current date
        calendar.add(Calendar.MONTH, -1);

        // Get the minimum date (1 month ago)
        Date minDate = calendar.getTime();

        // Get the current date
        Date currentDate = new Date();

        // Ensure the year is not less than 2024
        calendar.set(Calendar.YEAR, 2024);
        Date minYearDate = calendar.getTime();

        // Generate a random date between 1 month ago and the current date
        Random random = new Random();
        long randomTime = minDate.getTime() + (long) (random.nextDouble() * (currentDate.getTime() - minDate.getTime()));

        // Set the random time
        calendar.setTimeInMillis(randomTime);

        // Ensure the year is not less than 2024
        if (calendar.getTime().before(minYearDate)) {
            calendar.setTime(minYearDate);
        }

        // Format the random date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm");
        String formattedDateTime = dateFormat.format(calendar.getTime());

        return formattedDateTime;
    }
    @Override
    public void onBindViewHolder(@NonNull NOTIFICATIONS_CHILD holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(TITLE.get(position));
        holder.message.setText(MESSAGE.get(position));
        String timeString = DATETIME.get(position);
        if (timeString.equals("-null-"))
        {
            holder.date.setText(getRandomDateTime());
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.getDefault());
            Date date = inputFormat.parse(timeString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm", Locale.getDefault());
            String formattedTime = outputFormat.format(date);
            holder.date.setText(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.deletenotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(c))
                {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(attr.getMykey()).child("Notifications").child(NOTIID.get(position));
                    db.setValue(null);

                    NOTIID.remove(NOTIID.get(position));
                    EVENTID.remove(EVENTID.get(position));
                    TYPE.remove(TYPE.get(position));
                    TITLE.remove(TITLE.get(position));
                    MESSAGE.remove(MESSAGE.get(position));
                    USERID.remove(USERID.get(position));
                    POSTID.remove(POSTID.get(position));
                    DATETIME.remove(DATETIME.get(position));
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(c, "No internet connection!", Toast.LENGTH_SHORT).show();
                }






            }
        });
        holder.cardlayoutnoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE.get(position).equals("like") || TYPE.get(position).equals("comment") || TYPE.get(position).equals("post"))
                {
                    Intent i = new Intent(c,ViewPost.class);
                    i.putExtra("postid", POSTID.get(position));
                    c.startActivity(i);
                }
                else if (TYPE.get(position).equals("follow"))
                {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(USERID.get(position)).child("email");
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                Intent  i = new Intent(c, ViewerProfile.class);
                                i.putExtra("key",USERID.get(position));
                                i.putExtra("email",snapshot.getValue(String.class));
                                c.startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (TYPE.get(position).equals("event"))
                {
                    MomentsActivity.ResetColors();
                    MomentsActivity.compass_button.setColorFilter(Color.parseColor("#FF8C42"));
                    MomentsActivity.momentslayout.setVisibility(View.GONE);
                    MomentsActivity.uploadpostlayout.setVisibility(View.GONE);
                    MomentsActivity.profilelayout.setVisibility(View.GONE);
                    MomentsActivity.searchlayout.setVisibility(View.GONE);
                    MomentsActivity.tourlayout.setVisibility(View.VISIBLE);
                    MomentsActivity.viewpager2.setVisibility(View.GONE);
                    MomentsActivity.loadingtours.setVisibility(View.VISIBLE);
                    MomentsActivity.LoadEvents(EVENTID.get(position));
                    MomentsActivity.Notificationlayout.setVisibility(View.GONE);
                }
                else if (TYPE.get(position).equals("chat"))
                {
                    Intent i = new Intent(c,ChatScreen.class);
                    i.putExtra("eventid",CHATID.get(position));
                    c.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return NOTIID.size();
    }

    static  class NOTIFICATIONS_CHILD extends RecyclerView.ViewHolder{
        TextView title,message,date, openproperty;
        ImageView deletenotification;
        CardView cardlayoutnoti;
        public NOTIFICATIONS_CHILD(@NonNull View v) {
            super(v);
            cardlayoutnoti  = v.findViewById(R.id.cardlayoutnoti);
            title = v.findViewById(R.id.titleofnoti);
            message = v.findViewById(R.id.messageofnoti);
            date = v.findViewById(R.id.dateofnoti);
            openproperty = v.findViewById(R.id.openproperty);
            deletenotification = v.findViewById(R.id.deletenoti);
        }
    }
}