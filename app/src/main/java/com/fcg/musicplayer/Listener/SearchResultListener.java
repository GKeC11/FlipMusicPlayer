package com.fcg.musicplayer.Listener;

import com.fcg.musicplayer.Data.MusicInfo;

public interface SearchResultListener {
    void onSuccess();
    void onItemClick(MusicInfo music);
}
