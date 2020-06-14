package com.fcg.musicplayer.Data;

import java.util.ArrayList;
import java.util.List;

public class MusicInfoData {
    public int code;
    public Result result;

    public class Result{
        public ArrayList<Song> songs;
        public int songCount;
    }

    public class Song{
        public int id;
        public String name;
        public List<Artist> artists;
        public int duration;
    }

    public class Artist{
        public int id;
        public String name;
    }
}
