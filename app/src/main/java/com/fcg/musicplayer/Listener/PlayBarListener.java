package com.fcg.musicplayer.Listener;

import com.fcg.musicplayer.Data.MusicInfo;

public interface PlayBarListener {
    void onPlay(MusicInfo musicInfo);
    void onPlaying(int progress);
}
