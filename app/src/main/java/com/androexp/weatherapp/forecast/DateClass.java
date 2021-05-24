package com.androexp.weatherapp.forecast;

import com.androexp.weatherapp.weather.WeatherClass;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DateClass {

    @SerializedName("dt_txt")
    private String date;

    @SerializedName("weather")
    private List<WeatherClass> weatherClassList;

    public List<WeatherClass> getWeatherClassList() {
        return weatherClassList;
    }

    public String getDate() {
        return date;
    }
}
