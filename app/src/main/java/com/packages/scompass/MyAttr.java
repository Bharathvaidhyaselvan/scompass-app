package com.packages.scompass;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyAttr {
    Context context;
    SharedPreferences s;
    public MyAttr(Context c) {
        this.context=c;
        this.s = c.getSharedPreferences("USER",Context.MODE_PRIVATE);
    }

    public String getMyname()
    {
        return s.getString("user_name","");

    }
    public String getMykey()
    {
        return s.getString("key","");

    }
    public String getMyEmail()
    {
        String em =  s.getString("email","");
        String[] parts = em.split("@");
        if (parts.length > 0) {
            em = parts[0];
        }
        return em;

    }
}
