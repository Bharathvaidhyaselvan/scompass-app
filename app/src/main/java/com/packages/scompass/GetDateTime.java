package com.packages.scompass;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class GetDateTime {
    public static String GetTime()
    {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        return timeStamp;
    }
}
