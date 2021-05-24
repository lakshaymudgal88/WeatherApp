package com.androexp.weatherapp.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherClass {

    @SerializedName("main")
    private String main;

    @SerializedName("icon")
    private String icon;

    public String getMain() {
        return main;
    }

    public String getIcon() {
        return icon;
    }
}
