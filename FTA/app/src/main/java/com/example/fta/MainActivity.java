package com.example.fta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //nextActivity();
                nextAdmin();
            }
        }, 3000);
    }

    private void nextAdmin() {
        Intent i = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(i);
        finish();
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent i;
        if (user == null){
            i = new Intent(MainActivity.this, LoginActivity.class);
        }
        else {
            i = new Intent(MainActivity.this, MenuActivity.class);
        }
        startActivity(i);
        finish();
    }
}