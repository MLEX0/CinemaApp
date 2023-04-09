package com.kp.cinemaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scereen);

        Thread threadLoading = new Thread() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(6);
                    Intent intentLoading = new Intent(SplashScreenActivity.this,
                            RegistrationAuthorizationActivity.class);
                    startActivity(intentLoading);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };threadLoading.start();
    }
}