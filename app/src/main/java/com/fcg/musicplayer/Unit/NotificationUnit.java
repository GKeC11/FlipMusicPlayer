package com.fcg.musicplayer.Unit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.fcg.musicplayer.AppCache;
import com.fcg.musicplayer.Controller.PlayerController;
import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Listener.NotificationListener;
import com.fcg.musicplayer.R;
import com.liulishuo.okdownload.DownloadTask;

public class NotificationUnit{

    public static final int Notify_Music_Player = 1;
    private static Context context;
    private static NotificationManager notificationManager;
    private static NotificationCompat.Builder mBuilder;
    private static RemoteViews notificationLayout;
    private MusicInfo music;

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

    private NotificationUnit(){
        context = AppCache.getInstance().getContext();

        IntentFilter filter = new IntentFilter();
        filter.addAction("PLAY");
        filter.addAction("NEXT");
        NotificationReceiver mReceiver = new NotificationReceiver();
        context.registerReceiver(mReceiver,filter);
    }

    private static class SingletonHolder{
        public static NotificationUnit instance = new NotificationUnit();
    }

    public static NotificationUnit get(){
        return SingletonHolder.instance;
    }

    public void showNotification(String channelId){
        notificationManager.notify(Notify_Music_Player,mBuilder.build());
    }

    //
    public NotificationCompat.Builder getBuilder(String packName){
        String channelId = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
            mBuilder = new NotificationCompat.Builder(context,channelId);
        }else{
            mBuilder = new NotificationCompat.Builder(context);
        }

        music = PlayerController.get().getMusic();
        notificationLayout = new RemoteViews(packName,R.layout.notification_small);
        notificationLayout.setTextViewText(R.id.notification_music_name,music.name);
        notificationLayout.setTextViewText(R.id.notification_artist,music.artists);

//        IntentFilter filter = new IntentFilter();
//        filter.addAction("PLAY");
//        filter.addAction("NEXT");
//        NotificationReceiver mReceiver = new NotificationReceiver();
//        context.registerReceiver(mReceiver,filter);

        Intent playIntent = new Intent("PLAY");
        PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(context,21,playIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        Intent nextIntent = new Intent("NEXT");
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(context,22,nextIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        notificationLayout.setOnClickPendingIntent(R.id.notification_play_btn,pendingPlayIntent);
        notificationLayout.setOnClickPendingIntent(R.id.notification_next_btn,pendingNextIntent);

        mBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomBigContentView(notificationLayout)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
                .setAutoCancel(true);

        return mBuilder;
    }

    public NotificationCompat.Builder getChangeNotification(){

//        mBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                .setCustomBigContentView(notificationLayout)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
//                .setAutoCancel(true);

        return mBuilder;
    }

    public static class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()){
                case "PLAY":
                    PlayerController.get().playOrPause();
                    break;

                case "NEXT":
                    PlayerController.get().playNext();
                    break;

                default:
                    break;
            }
        }

    }

    public static void onChange() {

        if(PlayerController.get().getIsPlaying()){
            notificationLayout.setImageViewResource(R.id.notification_play_btn,R.drawable.ic_play_circle_filled_24dp);
        }else{
            notificationLayout.setImageViewResource(R.id.notification_play_btn,R.drawable.ic_pause_black_24dp);
        }

    }

    public void onNext(MusicInfo music){
        notificationLayout.setImageViewResource(R.id.notification_play_btn,R.drawable.ic_pause_black_24dp);
        notificationLayout.setTextViewText(R.id.notification_music_name,music.name);
        notificationLayout.setTextViewText(R.id.notification_artist,music.artists);
    }

    public void onNewPlay(MusicInfo music){
        notificationLayout.setTextViewText(R.id.notification_music_name,music.name);
        notificationLayout.setTextViewText(R.id.notification_artist,music.artists);
    }
}
