package com.fcg.musicplayer.Controller;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Listener.ServiceCallback;
import com.fcg.musicplayer.Listener.PlayBarListener;

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
    private PlayBarController playBarController;
    private Handler handler;
    private boolean isMediaPlayerPrepare = false;

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
        playPosition ++;
        String path = music.path;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            playBarListener.onPlay(music);
            handler.post(mRunnable);
            callback.onPlay();
            isMediaPlayerPrepare = true;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void playOrPause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        else if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
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
    }

    public long getCurrentPosition(){
        if(mediaPlayer.isPlaying()){
            return mediaPlayer.getCurrentPosition();
        }else{
            return 0;
        }
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
}
