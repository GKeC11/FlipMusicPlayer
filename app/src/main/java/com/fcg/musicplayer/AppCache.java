package com.fcg.musicplayer;

import android.app.Application;
import android.content.Context;

public class AppCache {
    private Context mContext;

    public AppCache(){

    }

    public static class SingletonHolder{
        private static AppCache instance = new AppCache();
    }

    public static AppCache get(){
        return SingletonHolder.instance;
    }

    public void init(Application application){
        mContext = application.getApplicationContext();
    }
}
