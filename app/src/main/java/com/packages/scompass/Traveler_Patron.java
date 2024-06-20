package com.packages.scompass;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Traveler_Patron extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelerpatron);


        // Preload Razorpay
        Checkout.preload(getApplicationContext());

        // Setup click listeners
        findViewById(R.id.button100).setOnClickListener(this::onAmountButtonClick);
        findViewById(R.id.button500).setOnClickListener(this::onAmountButtonClick);
        findViewById(R.id.button1000).setOnClickListener(this::onAmountButtonClick);
        findViewById(R.id.payButton).setOnClickListener(this::onCustomAmountButtonClick);
    }

    public void onGoBackButtonClick(View view) {
        onBackPressed(); // Or any other action you want to perform when going back
    }

    public void onAmountButtonClick(View view) {
        int amount = 0;
        if (view.getId() == R.id.button100) {
            amount = 100;
        } else if (view.getId() == R.id.button500) {
            amount = 500;
        } else if (view.getId() == R.id.button1000) {
            amount = 1000;
        }
        startPayment(amount);
    }

    public void onCustomAmountButtonClick(View view) {
        EditText customAmountEditText = findViewById(R.id.customAmount);
        String customAmountStr = customAmountEditText.getText().toString();
        if (!customAmountStr.isEmpty()) {
            int amount = Integer.parseInt(customAmountStr);
            startPayment(amount);
        } else {
            Toast.makeText(this, "Please enter a custom amount", Toast.LENGTH_SHORT).show();
        }
    }

    private void startPayment(int amount) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_MthV22vcK7OlI8");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Traveler’s Patron");
            options.put("description", "In our initial phase of enhancing the travel community, we greatly appreciate your support to help maintain this platform.");
            options.put("theme.color", "#FF8C42");
            options.put("currency", "INR");
            options.put("amount", amount * 100);
            options.put("prefill.contact", "");

            checkout.open(this, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment successful: Thanks for your Contribution", Toast.LENGTH_SHORT).show();
        // Handle successful payment here
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Oops, Payment failed, Try again. ", Toast.LENGTH_SHORT).show();
        // Handle failed payment here
    }
}
