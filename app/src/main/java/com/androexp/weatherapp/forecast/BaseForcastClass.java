package com.androexp.weatherapp.forecast;

import com.androexp.weatherapp.weather.WeatherClass;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseForcastClass {

    @SerializedName("list")
    private List<DateClass> dateClassList;

    public List<DateClass> getDateClassList() {
        return dateClassList;
    }
}
