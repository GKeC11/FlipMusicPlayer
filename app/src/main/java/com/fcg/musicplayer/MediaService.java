package com.fcg.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MediaService extends Service {

    private MediaBinder binder = new MediaBinder();

    public class MediaBinder extends Binder {
        public MediaService getService(){
            return MediaService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        AudioUnit.get().init(this);
        return binder;
    }
}
