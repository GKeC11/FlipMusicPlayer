package com.fcg.musicplayer.Controller;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Fragment.MusicListDialogFragment;
import com.fcg.musicplayer.Listener.PlayBarListener;
import com.fcg.musicplayer.Controller.PlayerController;
import com.fcg.musicplayer.R;

import java.util.ArrayList;

public class PlayBarController implements View.OnClickListener, PlayBarListener {

    private FrameLayout frameLayout;
    private TextView musicName;
    private FragmentManager fm;
    private ImageView playList;
    private ProgressBar progressBar;
    private ImageView play_btn;
    private ImageView next_btn;
    private MusicInfo musicInfo;
    private Boolean isInitial = false;

    private PlayBarController(){

    }

    private static class SingletonHolder{
        private static PlayBarController instance = new PlayBarController();
    }

    public static PlayBarController get(){
        return SingletonHolder.instance;
    }

    public void initialView(FragmentManager fm,View view){
        this.fm = fm;
        frameLayout = (FrameLayout) view;
        musicName = view.findViewById(R.id.playBar_musicName);
        play_btn = view.findViewById(R.id.playBar_playBtn);
        next_btn = view.findViewById(R.id.playBar_next);
        playList = view.findViewById(R.id.playBar_musicList);
        progressBar = view.findViewById(R.id.playBar_progressBar);
        play_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        playList.setOnClickListener(this);
        isInitial = true;
    }

    public void updateView(FragmentManager fm,View view){
        this.fm = fm;
        frameLayout = (FrameLayout) view;
        musicName = view.findViewById(R.id.playBar_musicName);
        play_btn = view.findViewById(R.id.playBar_playBtn);
        next_btn = view.findViewById(R.id.playBar_next);
        playList = view.findViewById(R.id.playBar_musicList);
        progressBar = view.findViewById(R.id.playBar_progressBar);
        play_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        playList.setOnClickListener(this);

        if(PlayerController.get().getIsPlaying()){
            musicInfo = PlayerController.get().getMusic();
            musicName.setText(musicInfo.name);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playBar_playBtn:
                PlayerController.get().playOrPause();

                break;

            case R.id.playBar_next:
                PlayerController.get().playNext();
                break;

            case R.id.playBar_musicList:
                ArrayList<MusicInfo> musicPlayList = PlayerController.get().getPlayList();

                MusicListDialogFragment dialogFragment = MusicListDialogFragment.newInstance(musicPlayList);
                dialogFragment.getSource(playList);

                dialogFragment.show(fm,null);
                break;

            default:

                break;
        }
    }

    @Override
    public void onPlay(MusicInfo musicInfo) {
        play_btn.setSelected(true);
        this.musicName.setText(musicInfo.name);
        progressBar.setMax(musicInfo.duration);
        progressBar.setProgress((int) PlayerController.get().getCurrentPosition());
    }

    @Override
    public void onPlaying(int progress) {
        progressBar.setProgress(progress);
    }

    public Boolean getIsInitial(){
        return isInitial;
    }

    public void onPlayBtn(){

        if(PlayerController.get().isMediaPlayerPrepare()){
            if(play_btn.isSelected()){
                play_btn.setSelected(false);
            }else{
                play_btn.setSelected(true);
            }
        }

    }
}
