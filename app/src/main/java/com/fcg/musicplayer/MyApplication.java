package com.fcg.musicplayer;

import android.app.Application;

import com.fcg.musicplayer.Unit.DensityUnit;
import com.fcg.musicplayer.Unit.ToastUnit;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ToastUnit.init(this);
        DensityUnit.get().init(this);
    }
}
