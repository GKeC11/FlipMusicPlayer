package com.fcg.musicplayer.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Listener.ServiceCallback;
import com.fcg.musicplayer.Unit.NotificationUnit;
import com.fcg.musicplayer.Controller.PlayerController;

public class MediaService extends Service implements ServiceCallback {

    private MediaBinder binder = new MediaBinder();
    private String channelId;

    public class MediaBinder extends Binder {
        public MediaService getService(){
            return MediaService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        PlayerController.get().init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUnit.get().createNotificationChannel();
        }

        return binder;
    }

    @Override
    public void onFirstPlay() {
        NotificationCompat.Builder builder = NotificationUnit.get().getBuilder(getPackageName());
        Notification notification = builder.build();
        startForeground(110,notification);
    }

    @Override
    public void onChange() {
        Notification notification = NotificationUnit.get().getChangeNotification().build();
        startForeground(110,notification);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        PlayerController.get().onQuit();
        return super.onUnbind(intent);
    }
}
