package com.fcg.musicplayer;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {
    public String name;
    public String size;
    public String duration;
    public String path;

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            MusicInfo musicInfo = new MusicInfo();
            Bundle bundle;
            bundle = in.readBundle();
            musicInfo.name = bundle.getString("name");
            musicInfo.size = bundle.getString("size");
            musicInfo.duration = bundle.getString("duration");
            musicInfo.path = bundle.getString("path");
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
        bundle.putString("name",name);
        bundle.putString("size",size);
        bundle.putString("duration",duration);
        bundle.putString("path",path);
        dest.writeBundle(bundle);
    }
}
