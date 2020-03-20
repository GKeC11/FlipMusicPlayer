package com.fcg.musicplayer.Unit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.fcg.musicplayer.R;

public class NotifionUnit {

    public static final int Notify_Music_Player = 1;
    private Context context;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String createNotificationChannel(){
        String channelId = "channel_music_player";
        CharSequence channelName = "music_player";
        String channelDescription = "Notification of music player";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
        channel.setDescription(channelDescription);
        notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    public static class SingletonHolder{
        public static NotifionUnit instance = new NotifionUnit();
    }

    public static NotifionUnit get(){
        return SingletonHolder.instance;
    }

    public void init(Context context){
        this.context = context;
    }

    public void showNotification(String channelId){
        notificationManager.notify(Notify_Music_Player,mBuilder.build());
    }

    public NotificationCompat.Builder getBuilder(){
        String channelId = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
            mBuilder = new NotificationCompat.Builder(context,channelId);
        }else{
            mBuilder = new NotificationCompat.Builder(context);
        }

        mBuilder.setContentTitle("MusicPlayer")
                .setContentText("Playing")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_play_circle_filled_24dp)
                .setAutoCancel(true);

        return mBuilder;
    }
}
