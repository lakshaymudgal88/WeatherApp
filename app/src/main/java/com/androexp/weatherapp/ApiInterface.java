package com.androexp.weatherapp;

import com.androexp.weatherapp.forecast.BaseForcastClass;
import com.androexp.weatherapp.weather.BaseWeatherClass;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather?")
    Call<BaseWeatherClass> getWeather(@Query("lat") String lotitide,
                                      @Query("lon") String longitude,
                                      @Query("units") String units,
                                      @Query("appid") String apiKey);

    @GET("forecast?")
    Call<BaseForcastClass> getForcast(
            @Query("cnt") String cnt,
            @Query("lat") String lotitide,
            @Query("lon") String longitude,
            @Query("appid") String apiKey);
}
