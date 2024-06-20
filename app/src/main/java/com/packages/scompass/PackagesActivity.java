package com.packages.scompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PackagesActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        Button button2 = findViewById(R.id.button2);
        Button button1 = findViewById(R.id.button1);
        Button button4 = findViewById(R.id.button4);
        Button button6 = findViewById(R.id.button6);

        button2.setOnClickListener(view -> {
            Intent intent = new Intent(PackagesActivity.this, ChennaitoKodaikanal.class);
            startActivity(intent);
        });

        button1.setOnClickListener(view -> {
            Intent intent = new Intent(PackagesActivity.this, ChennaitoOoty.class);
            startActivity(intent);
        });

        button4.setOnClickListener(view -> {
            Intent intent = new Intent(PackagesActivity.this, ChennaitoKollihills.class);
            startActivity(intent);
        });

        button6.setOnClickListener(view -> {
            Intent intent = new Intent(PackagesActivity.this, ChennaiToYelagiriActivity.class);
            startActivity(intent);
        });


    }

    private void startActivityWithSelectedMenuItem(Class<?> destinationActivity, int selectedMenuItemId) {
        Intent intent = new Intent(this, destinationActivity);
        intent.putExtra("selectedMenuItemId", selectedMenuItemId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}