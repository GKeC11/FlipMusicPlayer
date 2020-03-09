package com.fcg.musicplayer;

import android.app.Application;
import android.widget.Toast;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppCache.get().init(this);
        ToastUnit.init(this);
        DensityUnit.get().init(this);
    }
}
