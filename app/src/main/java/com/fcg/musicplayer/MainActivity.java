package com.fcg.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.fcg.musicplayer.Controller.PlayBarController;
import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Fragment.MyFragment;
import com.fcg.musicplayer.Controller.PlayerController;
import com.fcg.musicplayer.Service.MediaService;
import com.fcg.musicplayer.Unit.PermissionUnit;
import com.google.android.material.navigation.NavigationView;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
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

        initNav();

        PlayBarController playBarController = new PlayBarController(playBar);
        playBarController.init(getSupportFragmentManager());
        PlayerController.get().addListener(playBarController);

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

        super.onStop();
    }

    public class MyFragmentAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<MusicInfo> musicInfos;

        public MyFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);

            musicInfos = getIntent().getParcelableArrayListExtra("music_list");
            PlayerController.get().updateMusicList(musicInfos);
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
            PlayerController.get().addCallback(mediaService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        Log.d("mtest", "onDestroy: main");
        unbindService(connection);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void initNav(){
        drawerLayout = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.nav_cover);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);

        ListView listView = navigationView.findViewById(R.id.nav_list);
        List<String> objects = new ArrayList<>();
        objects.add("Test");
        NavAdapter navAdapter = new NavAdapter(this,R.layout.my_item,objects);
        listView.setAdapter(navAdapter);
    }

}
