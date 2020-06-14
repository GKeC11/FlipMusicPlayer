package com.fcg.musicplayer.Controller;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Listener.ServiceCallback;
import com.fcg.musicplayer.Listener.PlayBarListener;
import com.fcg.musicplayer.Unit.NotificationUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PlayerController {

    private ServiceCallback callback;
    private PlayBarListener playBarListener;
    private MediaPlayer mediaPlayer;
    private int playPosition = -1;
    private Context context;
    private List<MusicInfo> musicPlayList = new ArrayList<>();
    private ArrayList<MusicInfo> musicInfos = new ArrayList<>();
    private Handler handler;
    private boolean isMediaPlayerPrepare = false;
    private boolean isNotificationBuild = false;

    private static class SingletonHolder{
        private static PlayerController instance = new PlayerController();
    }

    public static PlayerController get(){
        return SingletonHolder.instance;
    }

    public void init(Context context){
        this.context = context.getApplicationContext();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        handler = new Handler(Looper.getMainLooper());
    }

    public void updateMusicList(ArrayList<MusicInfo> musicInfos){
        this.musicInfos = musicInfos;
    }


    public void play(MusicInfo music){

        musicPlayList.add(music);
        playPosition = musicPlayList.size() - 1;
        String path = music.path;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            playBarListener.onPlay(music);
            handler.post(mRunnable);
            if(!isNotificationBuild){
                callback.onFirstPlay();
                isNotificationBuild = true;
            }else{
                NotificationUnit.get().onNewPlay(music);
                callback.onChange();
            }

            isMediaPlayerPrepare = true;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void onChangeListener(){
        if(musicPlayList.size() > 0)
            playBarListener.onPlay(musicPlayList.get(playPosition));
    }

    public void playOnline(String url){
//        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
//        .setUsage(AudioAttributes.USAGE_MEDIA)
//        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//        .build());

        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void playOrPause(){
        if(PlayerController.get().isMediaPlayerPrepare()){
            PlayBarController.get().onPlayBtn();
            NotificationUnit.get().onChange();
            callback.onChange();
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
            else if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }

    }

    public void playNext(){
        int size = musicPlayList.size();
        if(size == 0){
            return;
        }
        playPosition = (playPosition + 1) % size;
        MusicInfo musicInfo = musicPlayList.get(playPosition);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicInfo.path);
            mediaPlayer.prepareAsync();
            playBarListener.onPlay(musicInfo);
        }catch (IOException e){
            e.printStackTrace();
        }

        NotificationUnit.get().onNext(musicInfo);
        callback.onChange();
    }

    public long getCurrentPosition(){

        return mediaPlayer.getCurrentPosition();
//        if(mediaPlayer.isPlaying()){
//            return mediaPlayer.getCurrentPosition();
//        }else{
//            return 0;
//        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer.isPlaying()) {
                playBarListener.onPlaying(mediaPlayer.getCurrentPosition());
            }
            handler.postDelayed(this,300);
        }
    };

    public void onQuit(){
        mediaPlayer.stop();
        mediaPlayer = null;
    }

//    public void playListDialog(FragmentManager fm){
//        MusicListDialogFragment dialogFragment = MusicListDialogFragment.newInstance((ArrayList<MusicInfo>)musicPlayList);
//        dialogFragment.show(fm,null);
//    }

    public ArrayList<MusicInfo> getPlayList(){
        return (ArrayList<MusicInfo>) musicPlayList;
    }

    public void addListener(PlayBarListener listener){
        this.playBarListener = listener;
    }

    public void changeListener(PlayBarListener listener) {
        this.playBarListener = listener;
        onChangeListener();
    }

    public void addCallback(ServiceCallback callback){
        this.callback = callback;
    }

    public boolean isMediaPlayerPrepare(){
        if(isMediaPlayerPrepare){
            return true;
        }else {
            return false;
        }
    }

    public boolean getIsPlaying(){
        return mediaPlayer.isPlaying();
    }

    public MusicInfo getMusic(){
        return musicPlayList.get(playPosition);
    }
}
