package com.fcg.musicplayer.Data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {
    public int id;
    public String name;
    public int size;
    public int duration;
    public String path;
    public String artists;

    public MusicInfo(){

    }

    public MusicInfo(String name){
        this.name = name;
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            MusicInfo musicInfo = new MusicInfo();
            Bundle bundle;
            bundle = in.readBundle();
            musicInfo.id = bundle.getInt("id");
            musicInfo.name = bundle.getString("name");
            musicInfo.size = bundle.getInt("size");
            musicInfo.duration = bundle.getInt("duration");
            musicInfo.path = bundle.getString("path");
            musicInfo.artists = bundle.getString("artists");
            return musicInfo;
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("name",name);
        bundle.putInt("size",size);
        bundle.putInt("duration",duration);
        bundle.putString("path",path);
        bundle.putString("artists",artists);
        dest.writeBundle(bundle);
    }
}
