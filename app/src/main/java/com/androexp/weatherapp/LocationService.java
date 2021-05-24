package com.androexp.weatherapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.androexp.weatherapp.forecast.BaseForcastClass;
import com.androexp.weatherapp.weather.BaseWeatherClass;
import com.androexp.weatherapp.weather.WeatherClass;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.androexp.weatherapp.MainActivity.n1Day;
import static com.androexp.weatherapp.MainActivity.n1DayIcon;
import static com.androexp.weatherapp.MainActivity.n2Day;
import static com.androexp.weatherapp.MainActivity.n2DayIcon;
import static com.androexp.weatherapp.MainActivity.todayHumidy;
import static com.androexp.weatherapp.MainActivity.todayTemp;
import static com.androexp.weatherapp.MainActivity.weatherCity;
import static com.androexp.weatherapp.MainActivity.weatherCondition;
import static com.androexp.weatherapp.MainActivity.weatherIcon;

public class LocationService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final String API_KEY = "c17ad77f66e8dffc3182ae77fa0a3a3b";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/";
    private static final int MIN_TIME = 5000;
    private static final int MIN_DIS = 1000;
    private Retrofit retrofit = null;
    private boolean isLocation = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fetchLocation();

        return START_STICKY;
    }

    private void fetchLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                isLocation = true;
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }

                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                getData(apiInterface, latitude, longitude);
                getForcast(apiInterface, latitude, longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                Snackbar.make(n1Day, "GPS ON fetching the location...", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Snackbar.make(n1Day, "GPS OFF, please ON to fetch the location", Snackbar.LENGTH_SHORT).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_TIME, MIN_DIS, locationListener);
    }

    private void getData(ApiInterface apiInterface, String latitude, String longitude) {

        Call<BaseWeatherClass> weatherCall = apiInterface.getWeather(latitude, longitude,
                "metric", API_KEY);
        weatherCall.enqueue(new Callback<BaseWeatherClass>() {
            @Override
            public void onResponse(@NotNull Call<BaseWeatherClass> call, @NotNull Response<BaseWeatherClass> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "error: " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    BaseWeatherClass weatherClass = response.body();
                    if (weatherClass != null) {
                        todayTemp.setText(weatherClass.getMainWeather().getTemperature() + (char) 0x00B0);
                        todayHumidy.setText("H " + weatherClass.getMainWeather().getHumidity());
                        weatherCity.setText(weatherClass.getPlaceName());
                        List<WeatherClass> weatherClassList = weatherClass.getWeatherClasses();
                        for (int i = 0; i < weatherClassList.size(); i++) {
                            weatherCondition.setText(weatherClassList.get(i).getMain());
                            String icon = weatherClassList.get(i).getIcon();
                            String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
                            Picasso.get().load(iconUrl).fit().into(weatherIcon);
                            Picasso.get().load(iconUrl).fit().into(n1DayIcon);
                            Picasso.get().load(iconUrl).fit().into(n2DayIcon);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseWeatherClass> call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(), "err: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getForcast(ApiInterface apiInterface, String latitude, String longitude) {
        Call<BaseForcastClass> forcastClassCall = apiInterface.getForcast("3",
                latitude,
                longitude,
                API_KEY);

        forcastClassCall.enqueue(new Callback<BaseForcastClass>() {
            @Override
            public void onResponse(@NotNull Call<BaseForcastClass> call, @NotNull Response<BaseForcastClass> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "res: " + response.message(), Toast.LENGTH_SHORT).show();
                } else {
                    BaseForcastClass forcastClass = response.body();
                    if (forcastClass != null) {
                        for (int i = 0; i < forcastClass.getDateClassList().size(); i++) {
                            String date = forcastClass.getDateClassList().get(i).getDate();
                            String[] strings = date.split(" ");
                            strings[1] = strings[1].trim();
                            if (i == 0) {
                                n1Day.setText(strings[1]);
                            }
                            if (i == 1) {
                                n2Day.setText(strings[1]);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseForcastClass> call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(), "forcast: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        if (isLocation) {
            locationManager.removeUpdates(locationListener);
            isLocation = false;
        }
        super.onDestroy();
    }
}
