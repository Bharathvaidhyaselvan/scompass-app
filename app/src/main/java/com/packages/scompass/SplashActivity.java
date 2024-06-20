package com.packages.scompass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        // Get the FCM token
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(SplashActivity.this, "not", Toast.LENGTH_SHORT).show();
//                             return;
//                        }
//
//
//                        // Get the token
//                        String token = task.getResult();
//                        Toast.makeText(SplashActivity.this, token, Toast.LENGTH_SHORT).show();
//
//                    }
//                });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SharedPreferences s = getSharedPreferences("USER",MODE_PRIVATE);
            if (s.getString("profile","0").equals("1"))
            {
                startActivity(new Intent(SplashActivity.this,MomentsActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        } else {
            ImageView splashLogo = findViewById(R.id.splash_logo);
            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
            splashLogo.startAnimation(rotation);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences s = getSharedPreferences("USER",MODE_PRIVATE);
                    if (s.getString("profile","0").equals("1"))
                    {
                        startActivity(new Intent(SplashActivity.this,MomentsActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }, SPLASH_DISPLAY_DURATION);
        }


    }
}

// yyyy-MM-dd HH:m
//   <androidx.viewpager2.widget.ViewPager2
//            android:layout_width="match_parent"
//            android:layout_height="match_parent"
//            android:id="@+id/viewpager2"
//            android:clipChildren="false"
//            android:clipToPadding="false"
//            android:overScrollMode="never"/>m