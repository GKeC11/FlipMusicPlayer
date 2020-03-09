package com.fcg.musicplayer;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class PlayBarControl implements View.OnClickListener,PlayBarListener {

    private FrameLayout frameLayout;
    private TextView musicName;
    private FragmentManager fm;
    private ImageView playList;
    private ProgressBar progressBar;

    PlayBarControl(View view) {
        frameLayout = (FrameLayout) view;
        musicName = view.findViewById(R.id.playBar_musicName);
        ImageView play_btn = view.findViewById(R.id.playBar_playBtn);
        ImageView next_btn = view.findViewById(R.id.playBar_next);
        playList = view.findViewById(R.id.playBar_musicList);
        progressBar = view.findViewById(R.id.playBar_progressBar);
        play_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        playList.setOnClickListener(this);
    }

    public void init(FragmentManager fm){
        this.fm = fm;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playBar_playBtn:
                AudioUnit.get().playOrPause();
                break;

            case R.id.playBar_next:
                AudioUnit.get().playNext();
                break;

            case R.id.playBar_musicList:
                ArrayList<MusicInfo> musicPlayList = AudioUnit.get().getPlayList();

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
        this.musicName.setText(musicInfo.name);
        progressBar.setMax(Integer.parseInt(musicInfo.duration));
        progressBar.setProgress((int)AudioUnit.get().getCurrentPosition());
    }

    @Override
    public void onPlaying(int progress) {
        progressBar.setProgress(progress);
    }
}
