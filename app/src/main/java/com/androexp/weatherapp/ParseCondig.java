package com.androexp.weatherapp;

import android.app.Application;

import com.parse.Parse;

public class ParseCondig extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.app_id))
                .clientKey(getString(R.string.client_key))
                .server(getString(R.string.server_url))
                .build());

    }
}
