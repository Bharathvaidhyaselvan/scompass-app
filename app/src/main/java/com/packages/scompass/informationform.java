package com.packages.scompass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class informationform extends AppCompatActivity {
    EditText getfullname,getusername,getcity,getcountry;
    ProgressBar p1;
    TextView getdob,getgender,finishbutton;
    String GENDER = "M",DOB="";
    MyAttr attr ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informationform);
        attr = new MyAttr(informationform.this);

        getfullname = findViewById(R.id.getfullname);
        getusername = findViewById(R.id.getusername);
        getcity = findViewById(R.id.getcity);
        getcountry = findViewById(R.id.getcountry);
        p1 = findViewById(R.id.p1);
        finishbutton = findViewById(R.id.finishbutton);
        getgender = findViewById(R.id.getgender);
        getdob = findViewById(R.id.getdob);

        SharedPreferences s = getSharedPreferences("USER",MODE_PRIVATE);
        if (s.getString("profile","0").equals("1"))
        {
            startActivity(new Intent(informationform.this,MomentsActivity.class));
            finish();
        }


        getgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GENDER.equals("M"))
                {
                    GENDER = "F";
                    getgender.setText("Female (Tap to change)");
                }
                else  if (GENDER.equals("F"))
                {
                    GENDER = "M";
                    getgender.setText("Male (Tap to change)");
                }
            }
        });
        getdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                // Set maximum date to 14 years ago
                Calendar maxDate = Calendar.getInstance();
                maxDate.add(Calendar.YEAR, -14); // Subtract 14 years from the current date

                // Set minimum date to January 1, 1940
                Calendar minDate = Calendar.getInstance();
                minDate.set(1940, 0, 1); // Start from January 1, 1940

                DatePickerDialog datePickerDialog = new DatePickerDialog(informationform.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Update calendar with chosen date
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                SimpleDateFormat sdf = new SimpleDateFormat("dd - MMM - yyyy", Locale.getDefault());
                                String selectedDate = sdf.format(calendar.getTime());

                                getdob.setText(selectedDate);
                                DOB = selectedDate;
                            }
                        },
                        // Set default date to current date
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                // Set minimum date to 14 years ago
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
                // Set maximum date to 14 years ago
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getfullname.getText().toString().trim().isEmpty())
                {
                    getfullname.setError("Required field is empty!");
                    Toast.makeText(informationform.this, "Please enter full name!", Toast.LENGTH_SHORT).show();
                }
                else
                if (getusername.getText().toString().trim().isEmpty())
                {
                    getusername.setError("Required field is empty!");
                    Toast.makeText(informationform.this, "Please enter username!", Toast.LENGTH_SHORT).show();
                }
                else if (getdob.getText().toString().trim().isEmpty())
                {
                    getdob.setError("Required field is empty!");
                    Toast.makeText(informationform.this, "Please choose date of birth!", Toast.LENGTH_SHORT).show();
                }
                else if (getcity.getText().toString().trim().isEmpty())
                {
                    getcity.setError("Required field is empty!");
                    Toast.makeText(informationform.this, "Please enter city!", Toast.LENGTH_SHORT).show();
                }
                else if (getcountry.getText().toString().trim().isEmpty())
                {
                    getcountry.setError("Required field is empty!");
                    Toast.makeText(informationform.this, "Please enter country!", Toast.LENGTH_SHORT).show();
                }
                else if (NetworkUtils.isNetworkAvailable(informationform.this))
                {
                    //check username
                    CheckUsername(getusername.getText().toString().trim());
                }
            }
        });
    }
    public void CheckUsername(String username)
    {
        p1.setVisibility(View.VISIBLE);
        finishbutton.setAlpha(0.4f);
        finishbutton.setEnabled(false);
        DatabaseReference checkdb = FirebaseDatabase.getInstance().getReference().child("usernames").child(username);
        checkdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Toast.makeText(informationform.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                    getusername.setError("Username already exists!");
                    finishbutton.setEnabled(true);
                    p1.setVisibility(View.GONE);
                    finishbutton.setAlpha(1.0f);
                }
                else {



                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(informationform.this, "token not found!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    else {
                                        String token = task.getResult();
                                        if (token!=null)
                                        {


                                            DatabaseReference checkdb2 = FirebaseDatabase.getInstance().getReference().child("usernames").child(getusername.getText().toString().trim());
                                            checkdb2.setValue(null);
                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("username");
                                            db.setValue(username);
                                            DatabaseReference x = checkdb.child(username);
                                            x.setValue("1");

                                            DatabaseReference fullname = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("name");
                                            DatabaseReference tokenn = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("token");
                                            tokenn.setValue(token);
                                            fullname.setValue(getfullname.getText().toString().trim());
                                            DatabaseReference city = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("city");
                                            city.setValue(getcity.getText().toString().trim());
                                            DatabaseReference country = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("country");
                                            country.setValue(getcountry.getText().toString().trim());

                                            DatabaseReference gender = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("gender");
                                            gender.setValue(GENDER);
                                            DatabaseReference dob = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("dob");
                                            dob.setValue(DOB);
                                            DatabaseReference profile = FirebaseDatabase.getInstance().getReference().child("users").child(attr.getMykey()).child("profile");
                                            profile.setValue("1");

                                            SharedPreferences.Editor s = getSharedPreferences("USER",MODE_PRIVATE).edit();
                                            s.putString("user_name",getfullname.getText().toString().trim());
                                            s.putString("profile","1");
                                            s.apply();



                                            startActivity(new Intent(informationform.this, MomentsActivity.class));
                                            finish();

                                        }
                                        else {
                                            Toast.makeText(informationform.this, "null", Toast.LENGTH_SHORT).show();

                                        }

                                    }




                                    // Get the token

                                }
                            });

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
        finish();
    }
}