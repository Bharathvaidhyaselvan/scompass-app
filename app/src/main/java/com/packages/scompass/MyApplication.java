package com.packages.scompass;

import android.app.Application;
import android.os.Handler;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.packages.scompass.MyWorker;

public class MyApplication extends Application {

    private Handler handler = new Handler();

//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        // Create a runnable object to execute the periodic task
//        Runnable periodicTask = new Runnable() {
//            @Override
//            public void run() {
//                // Create a one-time work request to run the worker
//                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
//                        .build();
//
//                // Enqueue the work request with WorkManager
//                WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
//
//                // Schedule the next execution after 5 seconds
//                handler.postDelayed(this, 5000);
//            }
//        };
//
//        // Start the periodic task
//        handler.post(periodicTask);
//    }
}
