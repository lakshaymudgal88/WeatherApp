package com.androexp.weatherapp.weather;

import com.google.gson.annotations.SerializedName;

public class MainWeather {

    @SerializedName("temp")
    private String temperature;
    @SerializedName("humidity")
    private String humidity;
    @SerializedName("pressure")
    private String pressure;

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }
}
