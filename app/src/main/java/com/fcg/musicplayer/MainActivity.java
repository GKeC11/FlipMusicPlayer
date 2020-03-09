package com.fcg.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.Window;
import android.widget.FrameLayout;

import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MediaService mediaService;
    private FrameLayout playBar;
    private ViewPager viewPager;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fade enterTransition = new Fade();
        enterTransition.setDuration(200);
        getWindow().setEnterTransition(enterTransition);

        setContentView(R.layout.activity_main);

        playBar = findViewById(R.id.play_bar);
        PlayBarControl playBarControl = new PlayBarControl(playBar);
        playBarControl.init(getSupportFragmentManager());
        AudioUnit.get().addListener(playBarControl);

        PermissionUnit permissionUnit = new PermissionUnit(this);
        permissionUnit.applyPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});

        viewPager = findViewById(R.id.viewPager);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                PagerAdapter pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
                viewPager.setAdapter(pagerAdapter);
                viewPager.setClipToPadding(true);
                viewPager.setPageTransformer(true, new BookFlipPageTransformer());
            }
        });

        Intent playService_intent = new Intent(this,MediaService.class);
        bindService(playService_intent,connection,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                thread.run();
            }
        },550);
    }

    @Override
    protected void onStop() {
        unbindService(connection);
        super.onStop();
    }

    public class MyFragmentAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<MusicInfo> musicInfos;

        public MyFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);

            musicInfos = getIntent().getParcelableArrayListExtra("music_list");
            AudioUnit.get().updateMusicList(musicInfos);
            int len = musicInfos.size();
            float pages_f = (float)len / 10;
            int pages = (int)Math.ceil(pages_f);
            int page_end = 0;

            for(int i = 0;i<pages;i++){
                if(i * 10 + 10 > len){
                    page_end = len;
                }else{
                    page_end = i * 10 + 10;
                }
                List<MusicInfo> temp = musicInfos.subList(i * 10,page_end);
                ArrayList<MusicInfo> temp_list = new ArrayList<>(temp);
                MyFragment fragment = MyFragment.newInstance(temp_list);
                fragments.add(fragment);
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            mediaService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
