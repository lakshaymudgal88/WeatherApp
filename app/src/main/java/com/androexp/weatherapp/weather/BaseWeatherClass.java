package com.androexp.weatherapp.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseWeatherClass {

    @SerializedName("main")
    private MainWeather mainWeather;

    @SerializedName("weather")
    private List<WeatherClass> weatherClasses;

    @SerializedName("name")
    private String placeName;

    public String getPlaceName() {
        return placeName;
    }

    public List<WeatherClass> getWeatherClasses() {
        return weatherClasses;
    }

    public MainWeather getMainWeather() {
        return mainWeather;
    }
}
