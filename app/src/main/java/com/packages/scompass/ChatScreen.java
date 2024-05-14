package com.packages.scompass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatScreen extends AppCompatActivity {
    String Event_Id;
    int uploadedimages=0,uploadedvideos=0;
    public static  FrameLayout optionscard;
    public static TextView repliedmessage;
    public static CardView repliedcard;
    ImageView closereplycard;
    TextView reply,copy,cancel,close;
    public static String REPLYMSGID="-null-",REPLYMSG="-null-";
    Double IMAGEPROGRESS=0.0,VIDEOPROGRESS=0.0;
    boolean cangoback = true;
    public static String finaltext="";
    TextView loadtitle,nofphotosandvideos,uploadingmediatext;
    DatabaseReference Eventdb;
    CardView sendbutton;
    FrameLayout uploadingmedialayout;
    public static RecyclerView messagerecyclerview;
    EditText getmessageedittext;
    ImageView addimagevideobutton;
    LinearLayout hidestartchat;
    String myemailwithoutat="",myfirebasekey="",myname="",myprofileimageuri,LastMessageKEY="";
    MyAttr attr;
    double total=0.0;
    private static final int REQUEST_SELECT_IMAGES_AND_VIDEOS = 1;
    ArrayList<String> MESSAGEKEY = new ArrayList<>();
    ArrayList<String> REPLIEDMESSAGEKEY = new ArrayList<>();
    ArrayList<String> REPLIEDMESSAGE = new ArrayList<>();
    ArrayList<String> MESSAGE= new ArrayList<>();
    ArrayList<String> TIME= new ArrayList<>();
    ArrayList<String> PROFILEIMAGELINK= new ArrayList<>();
    ArrayList<String> MESSAGEFROM= new ArrayList<>();
    ArrayList<String> MESSAGENAME= new ArrayList<>();
    private ArrayList<String> IMAGES = new ArrayList<>();
    private ArrayList<String> VIDEOS = new ArrayList<>();
    private ArrayList<Boolean> TRUEFALSE  = new ArrayList<>();
    boolean  clear=false;
    private ArrayList<String> IMAGESURLS = new ArrayList<>();
    private ArrayList<String> VIDEOSURLS = new ArrayList<>();


    public static FrameLayout showimagesvideo;
    public static String eventkey="",messagekey="";
    public static Context c;
    TextView clickimagesbutton,clickvideosbutton,closeimagevideoslayout;
    public static ViewPager2 viewpager2;
    public static ProgressBar p1;
    ArrayList<String> SHOWIMAGESLINKS = new ArrayList<>();
    ArrayList<String> SHOWVIDEOSLINKS = new ArrayList<>();


    public static void copyToClipboard(Context context, String textToCopy) {
        // Get the clipboard manager
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // Create a ClipData object to store the text
        ClipData clip = ClipData.newPlainText("Copied Text", textToCopy);

        // Set the ClipData to the clipboard
        clipboard.setPrimaryClip(clip);

        // Show a toast message to indicate that the text has been copied
        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        //load keys and names
        attr = new MyAttr(ChatScreen.this);
        myemailwithoutat = attr.getMyEmail();
        myfirebasekey = attr.getMykey();
        myname = attr.getMyname();

        showimagesvideo = findViewById(R.id.showimagesvideo);
        clickimagesbutton = findViewById(R.id.clickimagesbutton);
        clickvideosbutton = findViewById(R.id.clickvideosbutton);
        closeimagevideoslayout = findViewById(R.id.closeimagevideoslayout);
        viewpager2 = findViewById(R.id.viewpager2);
        p1 = findViewById(R.id.p1);
        repliedmessage = findViewById(R.id.repliedmessage);
        repliedcard = findViewById(R.id.repliedcard);
        closereplycard = findViewById(R.id.closereplycard);


        loadtitle = findViewById(R.id.loadtitle);
        nofphotosandvideos = findViewById(R.id.nofphotosandvideos);
        optionscard = findViewById(R.id.optionscard);
        reply = findViewById(R.id.reply);
        copy = findViewById(R.id.copy);
        close = findViewById(R.id.close);
        uploadingmediatext = findViewById(R.id.uploadingmediatext);
        uploadingmedialayout = findViewById(R.id.uploadingmedialayout);
        sendbutton = findViewById(R.id.sendbutton); sendbutton.setEnabled(false);
        getmessageedittext = findViewById(R.id.getmessageedittext);
        addimagevideobutton = findViewById(R.id.addimagevideobutton);
        messagerecyclerview = findViewById(R.id.messagerecyclerview);
        hidestartchat = findViewById(R.id.hidestartchat);
        LoadDefaults();
        onlineofflline(true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionscard.setVisibility(View.GONE);
                repliedcard.setVisibility(View.GONE);
                REPLYMSGID = "-null-";
                REPLYMSG = "-null-";
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionscard.setVisibility(View.GONE);
                copyToClipboard(ChatScreen.this,REPLYMSG);
            }
        });
//        https://drive.google.com/drive/folders/1QutOH1RinAe2jic_WuyerZ_aqW3XJ8e5?usp=drive_link

        optionscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x=0;
            }
        });
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionscard.setVisibility(View.GONE);
                repliedcard.setVisibility(View.VISIBLE);
            }
        });
        closereplycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repliedcard.setVisibility(View.GONE);
                REPLYMSGID = "-null-";
                REPLYMSG = "-null-";
            }
        });
        // FF8C42
        // 85DE7F
        closeimagevideoslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickimagesbutton.setTextColor(Color.parseColor("#FF8C42"));
                clickvideosbutton.setTextColor(Color.parseColor("#888888"));
                showimagesvideo.setVisibility(View.GONE);
                viewpager2.setVisibility(View.GONE);

            }
        });
        uploadingmedialayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = 1;
            }
        });

        clickimagesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickimagesbutton.setTextColor(Color.parseColor("#FF8C42"));
                clickvideosbutton.setTextColor(Color.parseColor("#888888"));
                MessageAdapter.GetImages(eventkey,messagekey,c);
                viewpager2.setVisibility(View.GONE);
                p1.setVisibility(View.VISIBLE);

            }
        });

        clickvideosbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickvideosbutton.setTextColor(Color.parseColor("#FF8C42"));
                clickimagesbutton.setTextColor(Color.parseColor("#888888"));
                MessageAdapter.GetVideos(eventkey,messagekey,c,true);
                viewpager2.setVisibility(View.GONE);
                p1.setVisibility(View.VISIBLE);


            }
        });


        addimagevideobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImagesAndVideos();
            }
        });
        getmessageedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getmessageedittext.getText().toString().trim().isEmpty())
                {
                    sendbutton.setAlpha(0.5f);
                }
                else {
                    if (sendbutton.isEnabled())
                    {
                        sendbutton.setAlpha(1.0f);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(ChatScreen.this))
                {
                    if (!(loadtitle.getText().toString().trim().equals("Loading...")))
                    {
                    if (!getmessageedittext.getText().toString().trim().isEmpty())
                    {
                        SendMessage(getmessageedittext.getText().toString().trim());
                    }
                    else {
                        if ((!IMAGES.isEmpty()))
                        {
                            SendMessage(getmessageedittext.getText().toString().trim());
                        }
                        else if ((!VIDEOS.isEmpty()))
                        {
                            SendMessage(getmessageedittext.getText().toString().trim());
                        }
                    }
                    }

                }
                else {
                    Toast.makeText(ChatScreen.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private String getMimeType(Uri uri) {
        ContentResolver resolver = getContentResolver();
        String type = null;
        Cursor cursor = resolver.query(uri, new String[]{MediaStore.MediaColumns.MIME_TYPE}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            type = cursor.getString(0);
            cursor.close();
        }
        return type != null ? type : "";
    }
    public void updatePhotosAndVideosCount()
    {
        String imagesvideos="";
        if (IMAGES.size()>1)
        {
            imagesvideos = String.valueOf( IMAGES.size())+" Photos, ";
        }
        else {
            imagesvideos = String.valueOf( IMAGES.size())+" Photo, ";
        }
        if (VIDEOS.size()>1)
        {
            imagesvideos = imagesvideos +String.valueOf( VIDEOS.size())+" Videos";
        }
        else {
            imagesvideos = imagesvideos + String.valueOf( VIDEOS.size())+" Video";
        }

        nofphotosandvideos.setVisibility(View.VISIBLE);
        nofphotosandvideos.setText(imagesvideos);
        sendbutton.setAlpha(1.0f);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IMAGES.clear();
        VIDEOS.clear();

        if (requestCode == REQUEST_SELECT_IMAGES_AND_VIDEOS && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();

            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    String path = uri.toString();

                    Log.d("URI", "URI: " + path); // Log URI for debugging

                    if (getMimeType(uri).startsWith("image")) {
                        IMAGES.add(path); // Add image URI to IMAGES list
                    } else if (getMimeType(uri).startsWith("video")) {
                        VIDEOS.add(path); // Add video URI to VIDEOS list
                    } else {
                        Log.d("URI", "Unsupported URI: " + path); // Log unsupported URI
                    }
                }
            } else {
                Uri uri = data.getData();
                String path = uri.toString();

                Log.d("URI", "URI: " + path); // Log URI for debugging

                if (getMimeType(uri).startsWith("image")) {
                    IMAGES.add(path); // Add image URI to IMAGES list
                } else if (getMimeType(uri).startsWith("video")) {
                    VIDEOS.add(path); // Add video URI to VIDEOS list
                } else {
                    Log.d("URI", "Unsupported URI: " + path); // Log unsupported URI
                }
            }

            // Update UI with the number of selected photos and videos
            updatePhotosAndVideosCount();
        }
    }
    public void LoadAllMessages()
    {


        if (clear)
        {
            MESSAGEKEY = new ArrayList<>();
            MESSAGE= new ArrayList<>();
            TIME= new ArrayList<>();
            PROFILEIMAGELINK= new ArrayList<>();
            MESSAGEFROM= new ArrayList<>();
            REPLIEDMESSAGE = new ArrayList<>();
            REPLIEDMESSAGEKEY = new ArrayList<>();
            TRUEFALSE= new ArrayList<>();
            clear=false;

        }


        DatabaseReference Eventchat = FirebaseDatabase.getInstance()
                .getReference().child("Chats").child(Event_Id);
        Eventchat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        String messgakey = ds.getKey();
                        if (!(MESSAGEKEY.contains(messgakey)))
                        {
                            if ((messgakey.equals("Lastmessage")))
                            {
                                LoadAllMessages();
                                return;
                            }
                            DatabaseReference msg = Eventchat.child(ds.getKey()).child("Message");
                            DatabaseReference time = Eventchat.child(ds.getKey()).child("Time");
                            DatabaseReference profilelink = Eventchat.child(ds.getKey()).child("Profilelink");
                            DatabaseReference messagefrom_userkey= Eventchat.child(ds.getKey()).child("Userkey");
                            DatabaseReference repliedmessage= Eventchat.child(ds.getKey()).child("repliedmessage");
                            DatabaseReference repliedmessageorignalmessagekey= Eventchat.child(ds.getKey()).child("repliedmessageorignalmessagekey");

                            profilelink.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotprofilelink) {
                                    if (snapshotprofilelink.exists())
                                    {
                                        msg.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshotmessage) {
                                                if (snapshotmessage.exists())
                                                {
                                                    time.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshottime) {
                                                            if (snapshottime.exists())
                                                            {
                                                                messagefrom_userkey.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshotmesssagefromuserkey) {
                                                                        if (snapshotmesssagefromuserkey.exists())
                                                                        {
                                                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(snapshotmesssagefromuserkey.getValue(String.class)).child("name");
                                                                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshotname) {
                                                                                    if (snapshotname.exists())
                                                                                    {
                                                                                        repliedmessageorignalmessagekey.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshotrepliedmessagekey) {
                                                                                                if (snapshotrepliedmessagekey.exists())
                                                                                                {
                                                                                                    repliedmessage.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshotrepliedmessage) {
                                                                                                            if (snapshotrepliedmessage.exists())
                                                                                                            {
                                                                                                                if (!MESSAGEKEY.contains(messgakey))
                                                                                                                {

                                                                                                                    MESSAGEKEY.add(messgakey);
                                                                                                                    REPLIEDMESSAGEKEY.add(snapshotrepliedmessagekey.getValue(String.class));
                                                                                                                    REPLIEDMESSAGE.add(snapshotrepliedmessage.getValue(String.class));
                                                                                                                    MESSAGE.add(snapshotmessage.getValue(String.class));
                                                                                                                    TIME.add(snapshottime.getValue(String.class));
                                                                                                                    PROFILEIMAGELINK.add(snapshotprofilelink.getValue(String.class));
                                                                                                                    MESSAGEFROM.add(snapshotmesssagefromuserkey.getValue(String.class));
                                                                                                                    TRUEFALSE.add(false);
                                                                                                                    MESSAGENAME.add(snapshotname.getValue(String.class));

                                                                                                                    MessageAdapter messageAdapter = new MessageAdapter(ChatScreen.this,
                                                                                                                            Event_Id,
                                                                                                                            MESSAGEKEY,
                                                                                                                            MESSAGE,
                                                                                                                            TIME,
                                                                                                                            PROFILEIMAGELINK,
                                                                                                                            MESSAGEFROM,TRUEFALSE,REPLIEDMESSAGEKEY,REPLIEDMESSAGE,MESSAGENAME);
                                                                                                                    int lastItemIndex = messageAdapter.getItemCount() - 1;
                                                                                                                    messagerecyclerview.smoothScrollToPosition(lastItemIndex);
                                                                                                                    messagerecyclerview.setAdapter(messageAdapter);
                                                                                                                }

                                                                                                            }
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                                else {
                                                                                                    if (!MESSAGEKEY.contains(messgakey))
                                                                                                    {

                                                                                                        MESSAGEKEY.add(messgakey);
                                                                                                        REPLIEDMESSAGEKEY.add("-null-");
                                                                                                        REPLIEDMESSAGE.add("-null-");
                                                                                                        MESSAGE.add(snapshotmessage.getValue(String.class));
                                                                                                        TIME.add(snapshottime.getValue(String.class));
                                                                                                        PROFILEIMAGELINK.add(snapshotprofilelink.getValue(String.class));
                                                                                                        MESSAGEFROM.add(snapshotmesssagefromuserkey.getValue(String.class));
                                                                                                        TRUEFALSE.add(false);
                                                                                                        MESSAGENAME.add(snapshotname.getValue(String.class));


                                                                                                        MessageAdapter messageAdapter = new MessageAdapter(ChatScreen.this,
                                                                                                                Event_Id,
                                                                                                                MESSAGEKEY,
                                                                                                                MESSAGE,
                                                                                                                TIME,
                                                                                                                PROFILEIMAGELINK,
                                                                                                                MESSAGEFROM,TRUEFALSE,REPLIEDMESSAGEKEY,REPLIEDMESSAGE,MESSAGENAME);
                                                                                                        int lastItemIndex = messageAdapter.getItemCount() - 1;
                                                                                                        messagerecyclerview.smoothScrollToPosition(lastItemIndex);
                                                                                                        messagerecyclerview.setAdapter(messageAdapter);
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                    else {
                                                                                        repliedmessageorignalmessagekey.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshotrepliedmessagekey) {
                                                                                                if (snapshotrepliedmessagekey.exists())
                                                                                                {
                                                                                                    repliedmessage.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshotrepliedmessage) {
                                                                                                            if (snapshotrepliedmessage.exists())
                                                                                                            {
                                                                                                                if (!MESSAGEKEY.contains(messgakey))
                                                                                                                {

                                                                                                                    MESSAGEKEY.add(messgakey);
                                                                                                                    REPLIEDMESSAGEKEY.add(snapshotrepliedmessagekey.getValue(String.class));
                                                                                                                    REPLIEDMESSAGE.add(snapshotrepliedmessage.getValue(String.class));
                                                                                                                    MESSAGE.add(snapshotmessage.getValue(String.class));
                                                                                                                    TIME.add(snapshottime.getValue(String.class));
                                                                                                                    PROFILEIMAGELINK.add(snapshotprofilelink.getValue(String.class));
                                                                                                                    MESSAGEFROM.add(snapshotmesssagefromuserkey.getValue(String.class));
                                                                                                                    TRUEFALSE.add(false);
                                                                                                                    MESSAGENAME.add("Anonymous");


                                                                                                                    MessageAdapter messageAdapter = new MessageAdapter(ChatScreen.this,
                                                                                                                            Event_Id,
                                                                                                                            MESSAGEKEY,
                                                                                                                            MESSAGE,
                                                                                                                            TIME,
                                                                                                                            PROFILEIMAGELINK,
                                                                                                                            MESSAGEFROM,TRUEFALSE,REPLIEDMESSAGEKEY,REPLIEDMESSAGE,MESSAGENAME);
                                                                                                                    int lastItemIndex = messageAdapter.getItemCount() - 1;
                                                                                                                    messagerecyclerview.smoothScrollToPosition(lastItemIndex);
                                                                                                                    messagerecyclerview.setAdapter(messageAdapter);
                                                                                                                }

                                                                                                            }
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                                else {
                                                                                                    if (!MESSAGEKEY.contains(messgakey))
                                                                                                    {

                                                                                                        MESSAGEKEY.add(messgakey);
                                                                                                        REPLIEDMESSAGEKEY.add("-null-");
                                                                                                        REPLIEDMESSAGE.add("-null-");
                                                                                                        MESSAGE.add(snapshotmessage.getValue(String.class));
                                                                                                        TIME.add(snapshottime.getValue(String.class));
                                                                                                        PROFILEIMAGELINK.add(snapshotprofilelink.getValue(String.class));
                                                                                                        MESSAGEFROM.add(snapshotmesssagefromuserkey.getValue(String.class));
                                                                                                        TRUEFALSE.add(false);
                                                                                                        MESSAGENAME.add("Anonymous");


                                                                                                        MessageAdapter messageAdapter = new MessageAdapter(ChatScreen.this,
                                                                                                                Event_Id,
                                                                                                                MESSAGEKEY,
                                                                                                                MESSAGE,
                                                                                                                TIME,
                                                                                                                PROFILEIMAGELINK,
                                                                                                                MESSAGEFROM,TRUEFALSE,REPLIEDMESSAGEKEY,REPLIEDMESSAGE,MESSAGENAME);
                                                                                                        int lastItemIndex = messageAdapter.getItemCount() - 1;
                                                                                                        messagerecyclerview.smoothScrollToPosition(lastItemIndex);
                                                                                                        messagerecyclerview.setAdapter(messageAdapter);
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
                }
                else {
                    hidestartchat.setVisibility(View.VISIBLE);
                    messagerecyclerview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadDefaults();
    }


    private void uploadphoto(Uri imguri,String Eventkey,String messagekey,String messageToSend )
    {
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Chats").child(Eventkey).child(messagekey)
                .child("Images")
                .child(String.valueOf(System.currentTimeMillis()));
        storageRef.putFile(imguri).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                IMAGESURLS.add(imageUrl);

                // message
                DatabaseReference Eventchat = FirebaseDatabase.getInstance()
                        .getReference().child("Chats").child(Event_Id).child(messagekey);

                DatabaseReference messageNodeParent = Eventchat;
                DatabaseReference msg = messageNodeParent.child("Message");
                DatabaseReference status = messageNodeParent.child("Status");
                DatabaseReference time = messageNodeParent.child("Time");
                DatabaseReference profilelink = messageNodeParent.child("Profilelink");
                DatabaseReference images = messageNodeParent.child("Images");
                DatabaseReference videos = messageNodeParent.child("Videos");
                DatabaseReference messagefrom_userkey = messageNodeParent.child("Userkey");

                DatabaseReference repliedmessage = messageNodeParent.child("repliedmessage");
                DatabaseReference repliedmessageorignalmessagekey = messageNodeParent.child("repliedmessageorignalmessagekey");

                DatabaseReference Eventchatx = FirebaseDatabase.getInstance()
                        .getReference().child("Chats").child(Event_Id);
                DatabaseReference lastmessagekey = Eventchatx.child("Lastmessage");

                if (uploadedvideos == VIDEOS.size() && uploadedimages == IMAGES.size())
                {
                    // upload to db
                    for (String vid: VIDEOSURLS)
                    {
                        DatabaseReference pushvid = videos.push(); pushvid.setValue(vid);
                    }
                    for (String img: IMAGESURLS)
                    {
                        DatabaseReference pushimages = images.push(); pushimages.setValue(img);
                    }
                    lastmessagekey.setValue(messagekey);
                    msg.setValue(messageToSend);
                    profilelink.setValue(myprofileimageuri);
                    status.setValue("1");
                    repliedmessageorignalmessagekey.setValue(REPLYMSGID);
                    repliedmessage.setValue(REPLYMSG);
                    messagefrom_userkey.setValue(myfirebasekey);
                    LastMessageKEY = messagekey;
                    time.setValue(getCurrentTime());
                    // send noti
                    {
                        DatabaseReference eve = FirebaseDatabase.getInstance().getReference().child("Events")
                                .child(Event_Id).child("Members");
                        eve.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                {
                                    for (DataSnapshot dataSnapshot: snapshot.getChildren())
                                    {
                                        if (!dataSnapshot.getKey().toString().equals(attr.getMykey()))
                                        {
                                            DatabaseReference userdb = FirebaseDatabase.getInstance().getReference().child("users").child(dataSnapshot.getKey());
                                            //check online
                                            DatabaseReference onoffdb = userdb.child("chatonline");
                                            onoffdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshotonoff) {
                                                    if (snapshotonoff.exists() && snapshotonoff.getValue(String.class).equals("1"))
                                                    {

                                                    }
                                                    else {
                                                        // send noti1
                                                        {
                                                            {
                                                                // if i liked anyother person post
                                                                // get his token
                                                                // get token
                                                                DatabaseReference usertoken = userdb.child("token");
                                                                usertoken.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshottoken) {
                                                                        if (snapshottoken.exists())
                                                                        {
                                                                            String token = snapshottoken.getValue(String.class);
                                                                            String message = attr.getMyname().concat("\n"+messageToSend);
                                                                            String title = loadtitle.getText().toString().trim();
                                                                            NotificationSender.sendNotification(token,title,message);

                                                                            DatabaseReference userthis = userdb.child("Notifications");
                                                                            DatabaseReference notificationdot = userthis.child("Notidot");
                                                                            notificationdot.setValue("1");

                                                                            DatabaseReference notikey = userthis.push();
                                                                            DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                            DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                            DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                            DatabaseReference type = notikey.child("type"); type.setValue("chat");
                                                                            DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                            DatabaseReference chatidd = notikey.child("chatid"); chatidd.setValue(Event_Id);


                                                                        }
                                                                        else {

                                                                            String message = attr.getMyname().concat("\n"+messageToSend);
                                                                            String title = loadtitle.getText().toString().trim();
                                                                            DatabaseReference userthis = userdb.child("Notifications");
                                                                            DatabaseReference notificationdot = userthis.child("Notidot");
                                                                            notificationdot.setValue("1");

                                                                            DatabaseReference notikey = userthis.push();
                                                                            DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                            DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                            DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                            DatabaseReference type = notikey.child("type"); type.setValue("chat");
                                                                            DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                            DatabaseReference chatidd = notikey.child("chatid"); chatidd.setValue(Event_Id);

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
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    //--------------------------------
                    getmessageedittext.setText("");
                    getmessageedittext.setEnabled(true);
                    sendbutton.setEnabled(true);
                    sendbutton.setAlpha(1.0f);
                    nofphotosandvideos.setVisibility(View.GONE);
                    nofphotosandvideos.setText("");
                    uploadedimages=0;
                    uploadedvideos=0;
                    IMAGES.clear();
                    VIDEOS.clear();
                    VIDEOSURLS.clear();
                    IMAGESURLS.clear();
                    IMAGEPROGRESS=0.0;
                    VIDEOPROGRESS = 0.0;
                    REPLYMSG = "-null-";
                    REPLYMSGID = "-null-";
                    total=0.0;
                    uploadingmediatext.setText("");
                    uploadingmedialayout.setVisibility(View.GONE);
                    cangoback=true;
                    clear=true;

                    LoadAllMessages();

                }
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(ChatScreen.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            // Progress Listener for loading
            // percentage on the dialog box
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                // show the progress bar
                IMAGEPROGRESS = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                updateText();
                if (IMAGEPROGRESS==100)
                {
                    uploadedimages = uploadedimages+1;
                }


            }
        });
    }
    private void uploadvideo(Uri videouri,String Eventkey,String messagekey,String messageToSend ) {
        if (videouri != null) {
            // save the selected video in Firebase storage
            final StorageReference reference = FirebaseStorage.getInstance().getReference()
                    .child("Chats").child(Eventkey).child(messagekey)
                    .child("Videos")
                    .child(String.valueOf(System.currentTimeMillis()));

            reference.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    // get the link of video
                    String downloadUri = uriTask.getResult().toString();
                    VIDEOSURLS.add(downloadUri);
                    // uploading

                    // message
                    DatabaseReference Eventchat = FirebaseDatabase.getInstance()
                            .getReference().child("Chats").child(Event_Id).child(messagekey);

                    DatabaseReference messageNodeParent = Eventchat;
                    DatabaseReference msg = messageNodeParent.child("Message");
                    DatabaseReference status = messageNodeParent.child("Status");
                    DatabaseReference images = messageNodeParent.child("Images");
                    DatabaseReference videos = messageNodeParent.child("Videos");
                    DatabaseReference time = messageNodeParent.child("Time");
                    DatabaseReference profilelink = messageNodeParent.child("Profilelink");
                    DatabaseReference messagefrom_userkey = messageNodeParent.child("Userkey");

                    DatabaseReference repliedmessage = messageNodeParent.child("repliedmessage");
                    DatabaseReference repliedmessageorignalmessagekey = messageNodeParent.child("repliedmessageorignalmessagekey");

                    DatabaseReference Eventchatx = FirebaseDatabase.getInstance()
                            .getReference().child("Chats").child(Event_Id);
                    DatabaseReference lastmessagekey = Eventchatx.child("Lastmessage");

                    // Video uploaded successfully
                    // Dismiss dialog
                    if (uploadedvideos == VIDEOS.size() && uploadedimages == IMAGES.size())
                    {
                        // upload to db
                        for (String vid: VIDEOSURLS)
                        {
                            DatabaseReference pushvid = videos.push(); pushvid.setValue(vid);
                        }
                        for (String img: IMAGESURLS)
                        {
                            DatabaseReference pushimages = images.push(); pushimages.setValue(img);
                        }
                        // send noti
                        {
                            DatabaseReference eve = FirebaseDatabase.getInstance().getReference().child("Events")
                                    .child(Event_Id).child("Members");
                            eve.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {
                                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                                        {
                                            if (!dataSnapshot.getKey().toString().equals(attr.getMykey()))
                                            {
                                                DatabaseReference userdb = FirebaseDatabase.getInstance().getReference().child("users").child(dataSnapshot.getKey());
                                                //check online
                                                DatabaseReference onoffdb = userdb.child("chatonline");
                                                onoffdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshotonoff) {
                                                        if (snapshotonoff.exists() && snapshotonoff.getValue(String.class).equals("1"))
                                                        {

                                                        }
                                                        else {
                                                            // send noti1
                                                            {
                                                                {
                                                                    // if i liked anyother person post
                                                                    // get his token
                                                                    // get token
                                                                    DatabaseReference usertoken = userdb.child("token");
                                                                    usertoken.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshottoken) {
                                                                            if (snapshottoken.exists())
                                                                            {
                                                                                String token = snapshottoken.getValue(String.class);
                                                                                String message = attr.getMyname().concat("\n"+messageToSend);
                                                                                String title = loadtitle.getText().toString().trim();
                                                                                NotificationSender.sendNotification(token,title,message);

                                                                                DatabaseReference userthis = userdb.child("Notifications");
                                                                                DatabaseReference notificationdot = userthis.child("Notidot");
                                                                                notificationdot.setValue("1");

                                                                                DatabaseReference notikey = userthis.push();
                                                                                DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                                DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                                DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                                DatabaseReference type = notikey.child("type"); type.setValue("chat");
                                                                                DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                                DatabaseReference chatidd = notikey.child("chatid"); chatidd.setValue(Event_Id);


                                                                            }
                                                                            else {

                                                                                String message = attr.getMyname().concat("\n"+messageToSend);
                                                                                String title = loadtitle.getText().toString().trim();
                                                                                DatabaseReference userthis = userdb.child("Notifications");
                                                                                DatabaseReference notificationdot = userthis.child("Notidot");
                                                                                notificationdot.setValue("1");

                                                                                DatabaseReference notikey = userthis.push();
                                                                                DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                                DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                                DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                                DatabaseReference type = notikey.child("type"); type.setValue("chat");
                                                                                DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                                DatabaseReference chatidd = notikey.child("chatid"); chatidd.setValue(Event_Id);

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
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        lastmessagekey.setValue(messagekey);
                        msg.setValue(messageToSend);
                        profilelink.setValue(myprofileimageuri);
                        repliedmessageorignalmessagekey.setValue(REPLYMSGID);
                        repliedmessage.setValue(REPLYMSG);
                        status.setValue("1");
                        messagefrom_userkey.setValue(myfirebasekey);
                        LastMessageKEY = messagekey;
                        time.setValue(getCurrentTime());
                        //--------------------------------
                        finaltext="";
                        getmessageedittext.setText("");
                        getmessageedittext.setEnabled(true);
                        sendbutton.setEnabled(true);
                        sendbutton.setAlpha(1.0f);
                        nofphotosandvideos.setVisibility(View.GONE);
                        nofphotosandvideos.setText("");
                        uploadedimages=0;
                        uploadedvideos=0;
                        IMAGES.clear();
                        VIDEOS.clear();
                        VIDEOSURLS.clear();
                        IMAGESURLS.clear();
                        IMAGEPROGRESS=0.0;
                        VIDEOPROGRESS=0.0;
                        REPLYMSG = "-null-";
                        REPLYMSGID = "-null-";
                        total=0.0;
                        uploadingmedialayout.setVisibility(View.GONE);
                        uploadingmediatext.setText("");
                        clear=true;
                        cangoback=true;
                        LoadAllMessages();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    Toast.makeText(ChatScreen.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    // show the progress bar

                    VIDEOPROGRESS = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    updateText();
                    if (VIDEOPROGRESS==100)
                    {
                        uploadedvideos = uploadedvideos+1;
                    }

                }
            });
        }
    }
    public void updateText()
    {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);

        uploadingmediatext.setText("Uploading media..."+String.valueOf(uploadedimages+uploadedvideos)+" out of "+String.valueOf((IMAGES.size()+VIDEOS.size())));
//        total = total+IMAGEPROGRESS+VIDEOPROGRESS;
//        double totalx = total/(IMAGES.size()+VIDEOS.size());
//        totalx = Double.valueOf(df.format(totalx));
//        uploadingmediatext.setText("Uploading media..."+totalx+"%");

    }

    public void selectImagesAndVideos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");  // Accept all file types
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Enable multiple selection
        startActivityForResult(intent, REQUEST_SELECT_IMAGES_AND_VIDEOS);
    }
    public void SendMessage(String messageToSend)
    {
        hidestartchat.setVisibility(View.GONE);
        messagerecyclerview.setVisibility(View.VISIBLE);
        // Chats ----> Eventid -----> Messagekey ...
        //                   | -----> Lastmessage
        DatabaseReference Eventchat = FirebaseDatabase.getInstance()
                .getReference().child("Chats").child(Event_Id);

        DatabaseReference messageNodeParent = Eventchat.push();

        if (!IMAGES.isEmpty())
        {
            cangoback = false;
            uploadingmedialayout.setVisibility(View.VISIBLE);
            uploadingmediatext.setText("Uploading media...0"+" out of "+String.valueOf((IMAGES.size()+VIDEOS.size())));
            getmessageedittext.setText("");
            getmessageedittext.setEnabled(false);
            sendbutton.setEnabled(false);
            sendbutton.setAlpha(0.5f);
 //            nofphotosandvideos.setText("uploading...");

            if (!MESSAGEKEY.contains(messageNodeParent.getKey()))
            {
                MESSAGEKEY.add(messageNodeParent.getKey());
                MESSAGE.add(messageToSend);
                TIME.add(getCurrentTime());
                PROFILEIMAGELINK.add(myprofileimageuri);
                MESSAGEFROM.add((myfirebasekey));
                REPLIEDMESSAGEKEY.add(REPLYMSGID);
                REPLIEDMESSAGE.add(REPLYMSG);
                MESSAGENAME.add(attr.getMyname());
                TRUEFALSE.add(true);
                MessageAdapter messageAdapter = new MessageAdapter(ChatScreen.this,
                        Event_Id,
                        MESSAGEKEY,
                        MESSAGE,
                        TIME,
                        PROFILEIMAGELINK,
                        MESSAGEFROM,TRUEFALSE,REPLIEDMESSAGEKEY,REPLIEDMESSAGE,MESSAGENAME);
                int lastItemIndex = messageAdapter.getItemCount() - 1;
                messagerecyclerview.smoothScrollToPosition(lastItemIndex);
                messagerecyclerview.setAdapter(messageAdapter);
                repliedcard.setVisibility(View.GONE);
            }

            for (String imageuri: IMAGES)
           {
               uploadphoto(Uri.parse(imageuri),Event_Id,messageNodeParent.getKey(),messageToSend);
           }
            return;
        }

        if (!VIDEOS.isEmpty())
        {

            uploadingmedialayout.setVisibility(View.VISIBLE);
            cangoback = false;
            uploadingmediatext.setText("Uploading media...0"+" out of "+String.valueOf((IMAGES.size()+VIDEOS.size())));
            getmessageedittext.setText("");
            getmessageedittext.setEnabled(false);
            sendbutton.setEnabled(false);
            sendbutton.setAlpha(0.5f);
 //            nofphotosandvideos.setText("uploading...");
            if (!MESSAGEKEY.contains(messageNodeParent.getKey()))
            {
                MESSAGEKEY.add(messageNodeParent.getKey());
                MESSAGE.add(messageToSend);
                TIME.add(getCurrentTime());
                REPLIEDMESSAGEKEY.add(REPLYMSGID);
                REPLIEDMESSAGE.add(REPLYMSG);
                PROFILEIMAGELINK.add(myprofileimageuri);
                MESSAGEFROM.add((myfirebasekey));
                MESSAGENAME.add(attr.getMyname());
                TRUEFALSE.add(true);
                MessageAdapter messageAdapter = new MessageAdapter(ChatScreen.this,
                        Event_Id,
                        MESSAGEKEY,
                        MESSAGE,
                        TIME,
                        PROFILEIMAGELINK,
                        MESSAGEFROM,TRUEFALSE,REPLIEDMESSAGEKEY,REPLIEDMESSAGE,MESSAGENAME);
                int lastItemIndex = messageAdapter.getItemCount() - 1;
                messagerecyclerview.smoothScrollToPosition(lastItemIndex);
                messagerecyclerview.setAdapter(messageAdapter);
                repliedcard.setVisibility(View.GONE);
            }

            for (String videouri: VIDEOS)
            {
                uploadvideo(Uri.parse(videouri),Event_Id,messageNodeParent.getKey(),messageToSend);
            }
            return;
        }

        // message
        DatabaseReference msg = messageNodeParent.child("Message");
        msg.setValue(messageToSend);
        DatabaseReference status = messageNodeParent.child("Status");
        status.setValue("1");
        // Time
        DatabaseReference time = messageNodeParent.child("Time");
        time.setValue(getCurrentTime());
        DatabaseReference profilelink = messageNodeParent.child("Profilelink");
        profilelink.setValue(myprofileimageuri);
        DatabaseReference repliedmessage = messageNodeParent.child("repliedmessage");
        DatabaseReference repliedmessageorignalmessagekey = messageNodeParent.child("repliedmessageorignalmessagekey");
        repliedmessageorignalmessagekey.setValue(REPLYMSGID);
        repliedmessage.setValue(REPLYMSG);

        // userkey
        DatabaseReference messagefrom_userkey = messageNodeParent.child("Userkey");
        messagefrom_userkey.setValue(myfirebasekey);


        DatabaseReference lastmessagekey = Eventchat.child("Lastmessage");
        lastmessagekey.setValue(messageNodeParent.getKey());
        LastMessageKEY = messageNodeParent.getKey();


        // send noti
        {
            DatabaseReference eve = FirebaseDatabase.getInstance().getReference().child("Events")
                    .child(Event_Id).child("Members");
            eve.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            if (!dataSnapshot.getKey().toString().equals(attr.getMykey()))
                            {
                                DatabaseReference userdb = FirebaseDatabase.getInstance().getReference().child("users").child(dataSnapshot.getKey());
                                //check online
                                DatabaseReference onoffdb = userdb.child("chatonline");
                                onoffdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshotonoff) {
                                        if (snapshotonoff.exists() && snapshotonoff.getValue(String.class).equals("1"))
                                        {

                                        }
                                        else {
                                            // send noti1
                                            {
                                                {
                                                    // if i liked anyother person post
                                                    // get his token
                                                    // get token
                                                    DatabaseReference usertoken = userdb.child("token");
                                                    usertoken.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshottoken) {
                                                            if (snapshottoken.exists())
                                                            {
                                                                String token = snapshottoken.getValue(String.class);
                                                                String message = attr.getMyname().concat("\n"+messageToSend);
                                                                String title = loadtitle.getText().toString().trim();
                                                                NotificationSender.sendNotification(token,title,message);

                                                                DatabaseReference userthis = userdb.child("Notifications");
                                                                DatabaseReference notificationdot = userthis.child("Notidot");
                                                                notificationdot.setValue("1");

                                                                DatabaseReference notikey = userthis.push();
                                                                DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                DatabaseReference type = notikey.child("type"); type.setValue("chat");
                                                                DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                DatabaseReference chatidd = notikey.child("chatid"); chatidd.setValue(Event_Id);


                                                            }
                                                            else {

                                                                String message = attr.getMyname().concat("\n"+messageToSend);
                                                                String title = loadtitle.getText().toString().trim();
                                                                DatabaseReference userthis = userdb.child("Notifications");
                                                                DatabaseReference notificationdot = userthis.child("Notidot");
                                                                notificationdot.setValue("1");

                                                                DatabaseReference notikey = userthis.push();
                                                                DatabaseReference likedbyid = notikey.child("userid"); likedbyid.setValue(attr.getMykey());
                                                                DatabaseReference titledb = notikey.child("title"); titledb.setValue(title);
                                                                DatabaseReference msgdb = notikey.child("message"); msgdb.setValue(message);
                                                                DatabaseReference type = notikey.child("type"); type.setValue("chat");
                                                                DatabaseReference dt = notikey.child("datetime"); dt.setValue(GetDateTime.GetTime());
                                                                DatabaseReference chatidd = notikey.child("chatid"); chatidd.setValue(Event_Id);

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
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        //trigger message changed
        getmessageedittext.setText("");
        if (!MESSAGEKEY.contains(messageNodeParent.getKey())){
            MESSAGEKEY.add(messageNodeParent.getKey());
            MESSAGE.add(messageToSend);
            TIME.add(getCurrentTime());
            REPLIEDMESSAGEKEY.add(REPLYMSGID);
            REPLIEDMESSAGE.add(REPLYMSG);
            PROFILEIMAGELINK.add(myprofileimageuri);
            MESSAGEFROM.add((myfirebasekey));
            TRUEFALSE.add(false);
            MESSAGENAME.add(attr.getMyname());
            MessageAdapter messageAdapter = new MessageAdapter(ChatScreen.this,
                    Event_Id,
                    MESSAGEKEY,
                    MESSAGE,
                    TIME,
                    PROFILEIMAGELINK,
                    MESSAGEFROM,TRUEFALSE,REPLIEDMESSAGEKEY,REPLIEDMESSAGE,MESSAGENAME);
            int lastItemIndex = messageAdapter.getItemCount() - 1;
            messagerecyclerview.smoothScrollToPosition(lastItemIndex);
            messagerecyclerview.setAdapter(messageAdapter);
            REPLYMSG = "-null-";
            REPLYMSGID = "-null-";

            repliedcard.setVisibility(View.GONE);
        }


    }


    public void LoadDefaults()
    {
        Intent i = getIntent();
        Event_Id = i.getStringExtra("eventid");
        Eventdb = FirebaseDatabase.getInstance().getReference().child("Events")
                .child(Event_Id);

        DatabaseReference citydb = Eventdb.child("City");
        DatabaseReference countrydb = Eventdb.child("Country");
        DatabaseReference profileImageUrldb = FirebaseDatabase.getInstance().getReference()
                .child("users").child(attr.getMykey()).child("profileimage");

        profileImageUrldb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    myprofileimageuri = snapshot.getValue(String.class);
                    sendbutton.setEnabled(true);
                    LoadAllMessages();
                }
                else {
                    myprofileimageuri = "-null-";
                    sendbutton.setEnabled(true);
                    LoadAllMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                                loadtitle.setText(snapshotcity.getValue(String.class)+", "+snapshotcountry.getValue(String.class)+" Tour");
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
    public void onlineofflline(boolean setonline)
    {
        if (setonline)
        {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(attr.getMykey()).child("chatonline");
            db.setValue("1");
        }
        else {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(attr.getMykey()).child("chatonline");
            db.setValue("0");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onlineofflline(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onlineofflline(true);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        onlineofflline(true);
    }
    @Override
    protected void onPause() {
        super.onPause();
        onlineofflline(false);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (showimagesvideo.getVisibility()==View.VISIBLE)
        {
            clickimagesbutton.setTextColor(Color.parseColor("#FF8C42"));
            clickvideosbutton.setTextColor(Color.parseColor("#888888"));
            showimagesvideo.setVisibility(View.GONE);
            viewpager2.setVisibility(View.GONE);
            return;
        }
        if (cangoback)
        {
            onlineofflline(false);
            finish();
        }
        else {
            Toast.makeText(ChatScreen.this, "Please wait...", Toast.LENGTH_SHORT).show();
        }
    }
}
class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterChild>
{
    Context c;
    boolean isthisitemvideoorimageitem;
    String EventKey;
    ArrayList<String> MESSAGEKEY;
    ArrayList<String> MESSAGENAME;
    ArrayList<String> REPLIEDMESSAGEKEY;
    ArrayList<String> REPLIEDMESSAGE;
    ArrayList<String> MESSAGE;
    ArrayList<String> TIME;
    ArrayList<String> PROFILEIMAGELINK;
    ArrayList<String> MESSAGEFROM;
    ArrayList<Boolean> TRUEFALSE;

    MyAttr myAttr;

    // link1, link2, position 0
    //
    // link3
    //
    // link4, link3, link2
    public MessageAdapter(Context c,String EventKey, ArrayList<String> MESSAGEKEY, ArrayList<String> MESSAGE, ArrayList<String> TIME, ArrayList<String> PROFILEIMAGELINK, ArrayList<String> MESSAGEFROM,ArrayList<Boolean> TRUEFALSE,ArrayList<String> REPLIEDMESSAGEKEY,ArrayList<String> REPLIEDMESSAGE,ArrayList<String> MESSAGENAME)
    {
        this.c = c;
        this.isthisitemvideoorimageitem = isthisitemvideoorimageitem;
        this.MESSAGEKEY = MESSAGEKEY;
        this.REPLIEDMESSAGEKEY = REPLIEDMESSAGEKEY;
        this.REPLIEDMESSAGE = REPLIEDMESSAGE ;
        this.EventKey = EventKey;
        this.MESSAGE = MESSAGE;
        this.TRUEFALSE = TRUEFALSE;
        this.TIME = TIME;
        this.MESSAGENAME = MESSAGENAME;
        this.PROFILEIMAGELINK = PROFILEIMAGELINK;
        this.MESSAGEFROM = MESSAGEFROM;
        myAttr = new MyAttr(c);

    }

    @NonNull
    @Override
    public MessageAdapterChild onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1)
        {
            View v = LayoutInflater.from(c).inflate(R.layout.rightmymessage,parent,false);
            return new MessageAdapterChild(v);

        }else if (viewType==2)
        {
            View v = LayoutInflater.from(c).inflate(R.layout.leftusermessage,parent,false);
            return new MessageAdapterChild(v);
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {

        if (myAttr.getMykey().equals(MESSAGEFROM.get(position)))
        {
            return 1;
        }
        else {
            return 2;
        }

    }
    public static String[] splitDateTime(String dateTimeString) {
        return dateTimeString.split(" ");
    }
    public static String extractLinks(String text) {
        // Regular expression to match URLs
        String regex = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\((?:[^\\s()<>]+|(?:\\([^\\s()<>]+\\)))?\\))+(?:\\((?:[^\\s()<>]+|(?:\\([^\\s()<>]+\\)))?\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // Iterate through the matches and print the URLs
        while (matcher.find()) {
            String url = matcher.group();
            return url;
        }
        return "";
    }
    @Override
    public void onBindViewHolder(@NonNull MessageAdapterChild holder, @SuppressLint("RecyclerView") int position) {
        if (TRUEFALSE.get(position))
        {
            holder.imagevideocard.setVisibility(View.VISIBLE);
        }
        else {
            holder.imagevideocard.setVisibility(View.GONE);
        }
        holder.messagename.setText(MESSAGENAME.get(position));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatScreen.showimagesvideo.setVisibility(View.VISIBLE);
                GetImages(EventKey,MESSAGEKEY.get(position),c);
                GetVideos(EventKey,MESSAGEKEY.get(position),c,false);
            }
        });
        DatabaseReference dbimages = FirebaseDatabase.getInstance().getReference()
                .child("Chats").child(EventKey).child(MESSAGEKEY.get(position)).child("Images");
        DatabaseReference dbvideos = FirebaseDatabase.getInstance().getReference()
                .child("Chats").child(EventKey).child(MESSAGEKEY.get(position)).child("Videos");
        dbimages.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    holder.imagevideocard.setVisibility(View.VISIBLE);
                    int img=0;
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        img=img+1;
                        holder.img= String.valueOf(img);
                        holder.st=1;
                        holder.textphotos.setVisibility(View.VISIBLE);
                        holder.textphotos.setText(holder.img+" Photos, "+holder.vid+" Videos");

                        DatabaseReference imgdb = dbimages.child(ds.getKey());
                        if (img==1)
                        {
                            imgdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotx) {
                                    if (snapshotx.exists())
                                    {
                                        try {
                                            Glide.with(c.getApplicationContext()).load(snapshotx.getValue(String.class)).into(holder.image);

                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
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
                    holder.img="0";
                    holder.textphotos.setText("0 Photos, "+holder.vid+" Videos");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dbvideos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    holder.imagevideocard.setVisibility(View.VISIBLE);
                    int vid=0;
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        holder.st=1;

                        vid=vid+1;
                        holder.vid= String.valueOf(vid);
                        holder.videoicon.setVisibility(View.VISIBLE);
                        holder.textphotos.setText(holder.img+" Photos, "+holder.vid+" Videos");


                    }
                }
                else {
                    holder.vid="0";
                    holder.videoicon.setVisibility(View.GONE);
                    holder.textphotos.setText(holder.img+" Photos, 0 Videos");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.layoutcard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ChatScreen.optionscard.setVisibility(View.VISIBLE);
                if (MESSAGE.get(position).isEmpty())
                {
                    ChatScreen.REPLYMSG = holder.textphotos.getText().toString().trim();
                    ChatScreen.repliedmessage.setText(holder.textphotos.getText().toString().trim());
                    ChatScreen.REPLYMSGID = MESSAGEKEY.get(position);

                }
                else {
                    ChatScreen.repliedmessage.setText(MESSAGE.get(position));
                    ChatScreen.REPLYMSG = MESSAGE.get(position);
                    ChatScreen.REPLYMSGID = MESSAGEKEY.get(position);

                }
                return true;
            }
        });
        CheckStatus(position,EventKey,MESSAGEKEY.get(position),holder);

        String[] parts = splitDateTime(TIME.get(position));
        holder.time.setText(parts[1]);
        if (REPLIEDMESSAGEKEY.get(position).equals("-null-") && REPLIEDMESSAGE.get(position).equals("-null-") )
        {
            holder.repliedmessage.setVisibility(View.GONE);
        }
        else {
            holder.repliedmessage.setVisibility(View.VISIBLE);
            holder.repliedmessage.setText("Replied: "+REPLIEDMESSAGE.get(position));
        }
        holder.repliedmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int positionToScroll = MESSAGEKEY.indexOf(REPLIEDMESSAGEKEY.get(position));
                ChatScreen.messagerecyclerview.scrollToPosition(positionToScroll);
            }
        });
        holder.message.setText(MESSAGE.get(position));
//        Toast.makeText(c, extractLinks(MESSAGE.get(position)), Toast.LENGTH_SHORT).show();
        holder.profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(c))
                {
                    Toast.makeText(c, "Loading please wait....", Toast.LENGTH_SHORT).show();

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(MESSAGEFROM.get(position)).child("email");
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Intent i = new Intent(c, ViewerProfile.class);
                                i.putExtra("key",MESSAGEFROM.get(position));
                                i.putExtra("email",snapshot.getValue(String.class));
                                c.startActivity(i);
                            }
                            else {
                                Toast.makeText(c, "something went's wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {
                    Toast.makeText(c, "No internet connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        try {
            Glide.with(c.getApplicationContext()).load(PROFILEIMAGELINK.get(position)).into(holder.profileimage);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void GetImages(String eventkey,String messagekey,Context c)
    {
        ChatScreen.eventkey = eventkey;
        ChatScreen.messagekey = messagekey;
        ChatScreen.c = c;

        ArrayList<String> imgs = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("Chats").child(eventkey).child(messagekey).child("Images");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        DatabaseReference link = db.child(ds.getKey().toString());
                        link.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotlink) {
                                if (snapshotlink.exists())
                                {
                                    imgs.add(snapshotlink.getValue(String.class));
                                    SHOWIMAGES showimages = new SHOWIMAGES(imgs,c);
                                    ChatScreen.viewpager2.setAdapter(showimages);
                                    ChatScreen.viewpager2.setOffscreenPageLimit(1);
                                    ChatScreen.viewpager2.setVisibility(View.VISIBLE);
                                    ChatScreen.p1.setVisibility(View.GONE);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                } else {
                    ChatScreen.p1.setVisibility(View.GONE);
                    Toast.makeText(c, "No images to show!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void GetVideos(String eventkey,String messagekey,Context c,boolean permission)
    {
        ChatScreen.eventkey = eventkey;
        ChatScreen.messagekey = messagekey;
        ChatScreen.c = c;

        ArrayList<String> imgs = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference()
                .child("Chats").child(eventkey).child(messagekey).child("Videos");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        DatabaseReference link = db.child(ds.getKey().toString());
                        link.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotlink) {
                                if (snapshotlink.exists())
                                {
                                    imgs.add(snapshotlink.getValue(String.class));

                                    if (permission)
                                    {
                                        SHOWVIDEOS showvideos = new SHOWVIDEOS(imgs,c);
                                        ChatScreen.viewpager2.setAdapter(showvideos);
                                        ChatScreen.viewpager2.setOffscreenPageLimit(1);
                                        ChatScreen.viewpager2.setVisibility(View.VISIBLE);
                                        ChatScreen.p1.setVisibility(View.GONE);

                                    }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                } else {
                    if (permission)
                    {
                        ChatScreen.p1.setVisibility(View.GONE);
                        Toast.makeText(c, "No videos to show!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void CheckStatus(int position,String keyofevent,String messagekey,MessageAdapterChild holder)
    {
        DatabaseReference status = FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(keyofevent).child(messagekey).child("Status");
        status.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.getValue(String.class).equals("1"))
                    {
                        holder.sentorunsent.setBackgroundColor(Color.parseColor("#FF8C42"));
                    }
                    else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CheckStatus(position,keyofevent,messagekey,holder);

                            }
                        },1000);
                    }
                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CheckStatus(position,keyofevent,messagekey,holder);
                        }
                    },1000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return REPLIEDMESSAGEKEY.size();
    }

    static class MessageAdapterChild extends PostAdapter.ViewHolder {
        TextView message,time,textphotos,repliedmessage,messagename;
        ImageView profileimage,image,videoicon;
        CardView sentorunsent,imagevideocard;
        String img="0",vid="0";
        LinearLayout layoutcard;
        int st=0;

        public MessageAdapterChild(@NonNull View v) {
            super(v);
            messagename = v.findViewById(R.id.messagename);
            repliedmessage = v.findViewById(R.id.repliedmessage);
            image = v.findViewById(R.id.image);
            textphotos = v.findViewById(R.id.textphotos);
            videoicon = v.findViewById(R.id.videoicon);
            layoutcard = v.findViewById(R.id.layoutcard);
            imagevideocard = v.findViewById(R.id.imagevideocard);
            profileimage = v.findViewById(R.id.profilepage);
            message = v.findViewById(R.id.message);
            time = v.findViewById(R.id.time);
            sentorunsent = v.findViewById(R.id.sentorunsent);
        }
    }
}
class SHOWIMAGES extends RecyclerView.Adapter<SHOWIMAGES.SHOWIMAGESCHILD>
{
    ArrayList<String> IMAGELINKS;
    Context c;

    public SHOWIMAGES(ArrayList<String> IMAGELINKS, Context c) {
        this.IMAGELINKS = IMAGELINKS;
        this.c = c;
    }

    @NonNull
    @Override
    public SHOWIMAGESCHILD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.imagelayout,parent,false);
        return new SHOWIMAGESCHILD(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SHOWIMAGESCHILD holder, int position) {
        Glide.with(c.getApplicationContext()).load(IMAGELINKS.get(position)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return IMAGELINKS.size();
    }

    static class SHOWIMAGESCHILD extends PostAdapter.ViewHolder{
        ImageView img;
        public SHOWIMAGESCHILD(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.image);
        }
    }
}
class SHOWVIDEOS extends RecyclerView.Adapter<SHOWVIDEOS.SHOWVIDEOSCHILD>
{
    ArrayList<String> VIDEOLINKS;
    Context c;

    public SHOWVIDEOS(ArrayList<String> IMAGELINKS, Context c) {
        this.VIDEOLINKS = IMAGELINKS;
        this.c = c;
    }

    @NonNull
    @Override
    public SHOWVIDEOSCHILD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.videolayout,parent,false);
        return new SHOWVIDEOSCHILD(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SHOWVIDEOSCHILD holder, int position) {
        playVideoFromUrl(VIDEOLINKS.get(position),holder,c);
    }

    @Override
    public int getItemCount() {
        return VIDEOLINKS.size();
    }
    private void playVideoFromUrl(String videoUrl, final SHOWVIDEOSCHILD holder, final Context c) {
        // Show progress bar
        holder.progressBar.setVisibility(View.VISIBLE);

        // Create a Uri object from the video URL
        Uri uri = Uri.parse(videoUrl);

        // Set the video URI to the VideoView
        holder.videoView.setVideoURI(uri);

        // Create a MediaController to control playback
        final MediaController mediaController = new MediaController(c);
        mediaController.setAnchorView(holder.videoView);

        // Set the MediaController to the VideoView
        holder.videoView.setMediaController(mediaController);

        // Start playing the video
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // Hide progress bar when video is prepared
                holder.progressBar.setVisibility(View.GONE);
                // Start playing the video
                holder.videoView.start();
            }
        });

        // Set error listener to handle any errors
        holder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // Hide progress bar on error
                holder.progressBar.setVisibility(View.GONE);
                // Handle error, for example, display a message
                Toast.makeText(c, "Error playing video", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    static class SHOWVIDEOSCHILD extends PostAdapter.ViewHolder{
        VideoView videoView;
        ProgressBar progressBar;
        public SHOWVIDEOSCHILD(@NonNull View v) {
            super(v);
            videoView = v.findViewById(R.id.videoview);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }
}