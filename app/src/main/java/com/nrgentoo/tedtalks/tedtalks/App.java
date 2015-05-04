package com.nrgentoo.tedtalks.tedtalks;

import android.app.Application;

import com.nrgentoo.tedtalks.tedtalks.database.HelperFactory;

/**
 * Application class
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }

    // TODO: referring to google docs this method is not called on production devices
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}
