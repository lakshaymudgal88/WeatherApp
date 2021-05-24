package com.androexp.weatherapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.androexp.weatherapp.MainActivity;
import com.androexp.weatherapp.R;
import com.parse.ParseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler(getMainLooper()).postDelayed(() -> {
            if(ParseUser.getCurrentUser() != null){
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }else {
                startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
            }
            finish();
        }, 2500);
    }
}