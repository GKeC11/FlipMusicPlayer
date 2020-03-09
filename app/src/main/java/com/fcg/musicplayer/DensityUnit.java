package com.fcg.musicplayer;

import android.content.Context;

public class DensityUnit {

    private Context context;

    public static class SingletonHolder{
        private static DensityUnit instance = new DensityUnit();
    }

    public static DensityUnit get(){
        return SingletonHolder.instance;
    }

    public void init(Context context){
        this.context = context;
    }

    public int dp2px(float dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
