package com.packages.scompass;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private FirebaseAuth mAuth;
    public static GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mDatabase;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(view -> signInWithGoogle());

        if (sharedPreferences.getBoolean("isAuthenticated", false)) {
            SharedPreferences s = getSharedPreferences("USER",MODE_PRIVATE);
            if (s.getString("profile","0").equals("1"))
            {
                startActivity(new Intent(LoginActivity.this,MomentsActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(LoginActivity.this, informationform.class));
                finish();
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.e("Error-",String.valueOf(e));
                Toast.makeText(LoginActivity.this, "oops, Sign-In failed. Try Again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {


                                String userEmail = user.getEmail();

                                if (true) {
                                    saveUserDataToFirebase(user.getUid(), userEmail);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isAuthenticated", true);
                                    editor.apply();

                                    Toast.makeText(LoginActivity.this, "Authentication Successful 😊", Toast.LENGTH_SHORT).show();
                                    DatabaseReference checkuser = FirebaseDatabase.getInstance().getReference().child("users")
                                                    .child(user.getUid()).child("profile");
                                    checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                DatabaseReference checkusername = FirebaseDatabase.getInstance().getReference().child("users")
                                                        .child(user.getUid()).child("name");
                                                checkusername.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                        if (snapshot2.exists())
                                                        {

                                                            FirebaseMessaging.getInstance().getToken()
                                                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<String> task) {
                                                                            if (!task.isSuccessful()) {
                                                                                Toast.makeText(LoginActivity.this, "token not found!", Toast.LENGTH_SHORT).show();
                                                                                return;
                                                                            }
                                                                            SharedPreferences.Editor se = getSharedPreferences("USER",MODE_PRIVATE).edit();
                                                                            se.putString("user_name",snapshot2.getValue(String.class));
                                                                            se.putString("profile","1");
                                                                            se.apply();
                                                                            startActivity(new Intent(LoginActivity.this, MomentsActivity.class));
                                                                            finish();

                                                                            String token = task.getResult();
                                                                            DatabaseReference t = FirebaseDatabase.getInstance().getReference().child("users")
                                                                                    .child(user.getUid()).child("token");
                                                                            t.setValue(token);
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
                                            else {
                                                startActivity(new Intent(LoginActivity.this, informationform.class));
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                } else {
                                    Toast.makeText(LoginActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "User is null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "oops, Authentication Failed 😥", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void saveUserDataToFirebase(String userId, String email) {


        DatabaseReference e =  mDatabase.child("users").child(userId).child("email");
        DatabaseReference img =  mDatabase.child("users").child(userId).child("profileimage");

        img.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    SharedPreferences.Editor sx = getSharedPreferences("USER",MODE_PRIVATE).edit();
                    sx.putString("profileurl",snapshot.getValue(String.class));
                    sx.apply();

                    SharedPreferences.Editor s = getSharedPreferences("USER",MODE_PRIVATE).edit();
                    s.putString("email",email);
                    s.putString("key",userId);
                    s.putString("profileurl",snapshot.getValue(String.class));
                    s.apply();

                    e.setValue(email);
                }
                else {
                    String photoUrl="";
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;
                        img.setValue(photoUrl);
                        if (photoUrl == null) {
                            photoUrl = "-1";
                            img.setValue(photoUrl);
                            SharedPreferences.Editor s = getSharedPreferences("USER",MODE_PRIVATE).edit();
                            s.putString("profileurl",photoUrl);
                            s.apply();
                        }
                    } else {
                        photoUrl = "-1";
                        img.setValue(photoUrl);
                        SharedPreferences.Editor s = getSharedPreferences("USER",MODE_PRIVATE).edit();
                        s.putString("profileurl",photoUrl);
                        s.apply();
                    }

                    SharedPreferences.Editor s = getSharedPreferences("USER",MODE_PRIVATE).edit();
                    s.putString("email",email);
                    s.putString("key",userId);
                    s.putString("profileurl",photoUrl);
                    s.apply();

                    e.setValue(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void saveProfileImageUrlToDatabase(String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("users").child(userId).child("profileImageUrl").setValue(imageUrl);
    }
}
