package org.altbeacon.beaconreference;

import android.app.Application;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
