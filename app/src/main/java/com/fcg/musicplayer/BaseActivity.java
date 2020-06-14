package com.fcg.musicplayer;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fcg.musicplayer.Controller.PlayBarController;
import com.fcg.musicplayer.Controller.PlayerController;
import com.fcg.musicplayer.Listener.PlayBarListener;

public class BaseActivity extends AppCompatActivity {

    public FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onResume() {
        super.onResume();

        frameLayout = findViewById(R.id.play_bar);
        if(!PlayBarController.get().getIsInitial()) {
            PlayBarController.get().initialView(getSupportFragmentManager(),frameLayout);
            PlayerController.get().addListener(PlayBarController.get());
        }else{
            PlayBarController.get().updateView(getSupportFragmentManager(),frameLayout);
            PlayerController.get().changeListener(PlayBarController.get());
        }
    }
}
