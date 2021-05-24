package com.androexp.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androexp.weatherapp.register.RegisterActivity;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static TextView userName, weatherCity, weatherCondition, todayTemp,
            todayHumidy;
    @SuppressLint("StaticFieldLeak")
    public static TextView n1Day, n2Day;
    @SuppressLint("StaticFieldLeak")
    public static ImageView weatherIcon, n1DayIcon, n2DayIcon;
    private static final int RQ_CODE = 101;
    private Intent serviceIntent;
    private boolean isServiceRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {

        userName = findViewById(R.id.user_name);
        weatherCondition = findViewById(R.id.weather_condition);
        weatherCity = findViewById(R.id.weather_city_name);
        weatherIcon = findViewById(R.id.weather_icon);
        todayTemp = findViewById(R.id.today_temp);
        todayHumidy = findViewById(R.id.today_humidity);
        n1Day = findViewById(R.id.n1_day);
        n2Day = findViewById(R.id.n2_day);
        n1DayIcon = findViewById(R.id.n1_weather_icon);
        n2DayIcon = findViewById(R.id.n2_weather_icon);
        ImageView logOutBtn = findViewById(R.id.sign_out);

        logOutBtn.setOnClickListener(this::onClick);

        if(ParseUser.getCurrentUser() != null){
            userName.setText(ParseUser.getCurrentUser().getUsername());
        }

    }

    private void signOutUser() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure, you want to sign out?")
                .setPositiveButton("Yes, Sign Out", (dialogInterface, i) -> {
                    if(ParseUser.getCurrentUser() != null){
                        ParseUser.logOut();
                        dialogInterface.dismiss();
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RQ_CODE);
        }else {
            serviceIntent = new Intent(this, LocationService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            }else {
                startService(serviceIntent);
                isServiceRunning = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RQ_CODE && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "permission granted", Toast.LENGTH_SHORT).show();
        } else if (requestCode == RQ_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getApplicationContext(), "app requires location permission to continue",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        if(isServiceRunning){
            stopService(serviceIntent);
            isServiceRunning = false;
        }
        super.onStop();
    }

    private void onClick(View view) {
        signOutUser();
    }

    //    (char) 0x00B0 -- for degree
//    \u2103  -- unicde Degree celcius code
//    \u2109   -- unicode for F
}