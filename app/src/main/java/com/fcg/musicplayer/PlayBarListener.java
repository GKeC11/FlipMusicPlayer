package com.fcg.musicplayer;

public interface PlayBarListener {
    void onPlay(MusicInfo musicInfo);
    void onPlaying(int progress);
}
