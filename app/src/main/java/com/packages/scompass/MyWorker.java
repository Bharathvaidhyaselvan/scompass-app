package com.packages.scompass;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    public   void logNumbersWithPause(int limit, long pauseMillis) {
        for (int i = 1; i <= limit; i++) {
            System.out.println(i);
            try {
                Thread.sleep(pauseMillis); // Pause for the specified duration
                Log.d("MyWorker", "Worker running"+i);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @NonNull
    @Override
    public Result doWork() {
//        logNumbersWithPause(50,2000);


        Log.d("MyWorker", "Worker running");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Noti");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue(String.class).equals("1")) {
                    createNotification(getApplicationContext(),"title","message", MomentsActivity.class);
                } else {
                    Log.d("MyWorker", "No notification");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyWorker", "Database error: " + error.getMessage());
            }
        });
        return Result.success();
    }



    private static final String CHANNEL_ID = "default_channel_id";
    private static final int NOTIFICATION_ID = 123;
    @Override
    public void onStopped() {
        Log.d("MyWorker", "Worker has been stopped");

        createNotification(getApplicationContext(),"title","message", MomentsActivity.class);

    }
    public static void createNotification(Context context, String title, String message, Class<?> activityClass) {
        // Create an explicit intent for the activity
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logosplasch)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Create the notification channel (for devices running Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Default Channel";
            String description = "Notification channel for default notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
