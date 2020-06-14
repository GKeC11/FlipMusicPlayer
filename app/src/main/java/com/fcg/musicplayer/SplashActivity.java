package com.fcg.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GestureDetectorCompat;

import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Unit.MusicUnit;
import com.fcg.musicplayer.Unit.PermissionUnit;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private ArrayList<MusicInfo> musicInfos;
    private GestureDetectorCompat mGestureDetector;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spalsh_activity);

        PermissionUnit permissionUnit = new PermissionUnit(this);
        permissionUnit.applyPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});

        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(300);
        getWindow().setExitTransition(slideTransition);

        musicInfos = MusicUnit.queryMusic(getApplicationContext());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.putParcelableArrayListExtra("music_list",musicInfos);
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
            }
        },1000);
    }

    @Override
    protected void onPause() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("mtest", "onDestroy: splash");
        super.onDestroy();
    }
}
