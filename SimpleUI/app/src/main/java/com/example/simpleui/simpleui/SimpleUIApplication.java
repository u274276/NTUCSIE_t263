package com.example.simpleui.simpleui;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;

/**
 * Created by Festiny on 2016/3/21.
 */
public class SimpleUIApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //parse.com
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
