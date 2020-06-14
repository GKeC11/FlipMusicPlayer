package com.fcg.musicplayer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class AppCache {
    private static Context mContext;

    private AppCache(){
    }

    private static class Singleton{
        private static final AppCache instance = new AppCache();
    }

    public static AppCache getInstance(){
        return Singleton.instance;
    }

    public static void initAppCache(Context context){
        mContext = context;
    }

    public Context getContext(){
        return mContext;
    }
}
