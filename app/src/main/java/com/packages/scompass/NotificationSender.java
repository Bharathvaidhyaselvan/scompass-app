package com.packages.scompass;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationSender {

    public static void sendNotification(String recipientToken, String title, String body) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("stat");
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    try {
                        // Create JSON payload
                        String jsonPayload = "{\"to\": \"" + recipientToken + "\", " +
                                "\"notification\": {\"title\": \"" + title + "\", \"body\": \"" + body + "\"}}";

                        // FCM endpoint
                        URL url = new URL("https://fcm.googleapis.com/fcm/send");

                        // Open connection
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Authorization", "key=AAAAcTd5D8A:APA91bGc8GEvA7q3r6Tiz_tnk47huDXIDCug7CG36ZMVDYZjB0uAX37096tpCozHsQsiYB9uhqbwXC67VBzWgudKBq6nGO-TIb6FdOlyNEDBi8-uAPZXjpMduiVOiiD4l2domChFPgSG");
                        conn.setDoOutput(true);

                        // Send JSON payload
                        OutputStream outputStream = conn.getOutputStream();
                        outputStream.write(jsonPayload.getBytes());
                        outputStream.flush();

                        // Get response code
                        int responseCode = conn.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            // Notification sent successfully
                            System.out.println("Notification sent successfully.");
                            db.setValue("sent");
                            Log.e("Sx","Send");

                        } else {
                            // Failed to send notification
                            System.out.println("Failed to send notification. Response code: " + responseCode);
                            Log.e("Fx","Failed to send notification. Response code: " + responseCode);
                            db.setValue("error s"+ String.valueOf(responseCode));

                        }

                        // Close connection
                        outputStream.close();
                        conn.disconnect();
                    } catch (Exception e)
                    {
                        // Handle exception
                        Log.e("Fx","Failed to send notification: " + e.getMessage());
                        System.out.println("Failed to send notification: " + e.getMessage());
                        db.setValue("error "+ String.valueOf(e));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}
