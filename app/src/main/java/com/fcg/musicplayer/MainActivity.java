package com.fcg.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.fcg.musicplayer.Controller.PlayBarController;
import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Fragment.MyFragment;
import com.fcg.musicplayer.Controller.PlayerController;
import com.fcg.musicplayer.Listener.MainActivityListener;
import com.fcg.musicplayer.Service.MediaService;
import com.fcg.musicplayer.Unit.PermissionUnit;
import com.fcg.musicplayer.Unit.SearchUnit;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainActivityListener {

    private ArrayList<MusicInfo> musicInfos;
    private NavigationView navigationView;
    private MediaService mediaService;
    private DrawerLayout drawerLayout;
    private FrameLayout contentFrame;
    private ViewPager viewPager;
    private Thread thread;
    private ExtendedFloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fade enterTransition = new Fade();
        enterTransition.setDuration(200);
        getWindow().setEnterTransition(enterTransition);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mainView = inflater.inflate(R.layout.activity_main,null);

//        setContentView(R.layout.activity_main);
        musicInfos = getIntent().getParcelableArrayListExtra("music_list");

        floatingActionButton = mainView.findViewById(R.id.floating_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        contentFrame = findViewById(R.id.content_frame);
        contentFrame.addView(mainView);
//        playBar = findViewById(R.id.play_bar);

        frameLayout = findViewById(R.id.play_bar);

        SearchUnit.get().registerMainActivityListener(this);

        initNav();

        viewPager = findViewById(R.id.viewPager);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final PagerAdapter pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager(),musicInfos,Constant.Local_Fragment);
                viewPager.setAdapter(pagerAdapter);
                viewPager.setClipToPadding(true);
                viewPager.setPageTransformer(true, new BookFlipPageTransformer());
                floatingActionButton.setText(1 + "/" + pagerAdapter.getCount());
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        int pagerPosition = position + 1;
                        floatingActionButton.setText(pagerPosition + "/" + pagerAdapter.getCount());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {

        super.onStop();
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
        final List<String> objects = new ArrayList<>();
        objects.add("Quit");
        objects.add("Search");
        NavAdapter navAdapter = new NavAdapter(this,R.layout.nav_item,objects);
        listView.setAdapter(navAdapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (objects.get(position)){
                    case "Quit":
                        MainActivity.this.finish();
                        break;

                    case "Search":
                        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });
    }

    @Override
    public void onDownloadSuccess(MusicInfo musicInfo) {
        musicInfos.add(musicInfo);
        MyFragmentAdapter fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),musicInfos,Constant.Local_Fragment);
        viewPager.setAdapter(fragmentAdapter);
    }
}
