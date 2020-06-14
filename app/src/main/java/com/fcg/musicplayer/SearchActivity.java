package com.fcg.musicplayer;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fcg.musicplayer.Controller.PlayerController;
import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Listener.SearchResultListener;
import com.fcg.musicplayer.Unit.SearchUnit;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements View.OnClickListener, SearchResultListener {
    private ViewPager viewPager;
    private ArrayList<MusicInfo> musicInfos;
    private Toolbar searchToolbar;
    private ToolbarOfSearchHolder toolbarOfSearchHolder;
    private AppBarLayout toolbarView;
    private ImageButton search_btn;
    private EditText search_text;
    private FragmentManager fm;
    private MyFragmentAdapter framgentAdapter;
    private FrameLayout contentView;
    private View searchView;
    private NavigationView navigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        searchView = inflater.inflate(R.layout.search_activity,null);

        contentView = findViewById(R.id.content_frame);
        contentView.addView(searchView);

        //屏蔽侧滑菜单
        DrawerLayout drawerLayout = findViewById(R.id.main_drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        viewPager = findViewById(R.id.search_viewPager);
        toolbarView = findViewById(R.id.toolbar_search_parent);
        fm = getSupportFragmentManager();

        //设置Toolbar和Toolbar GroupView
        toolbarOfSearchHolder = new ToolbarOfSearchHolder(toolbarView);
        searchToolbar = toolbarOfSearchHolder.mToolbar;
        setSupportActionBar(searchToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //设置Toolbar 其他按钮
        search_text = toolbarOfSearchHolder.search_text;
        search_btn = toolbarOfSearchHolder.search_btn;
        search_btn.setOnClickListener(this);

        SearchUnit.get().registerSearchActivityListener(this);

//        PagerAdapter fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),musicInfos,Constant.Search_Fragment);
//        viewPager.setAdapter(fragmentAdapter);
        viewPager.setClipToPadding(true);
        viewPager.setPageTransformer(true,new BookFlipPageTransformer());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_btn:
                SearchUnit.get().search("" + search_text.getText());
        }
    }

    @Override
    public void onSuccess() {
        musicInfos = SearchUnit.get().getMusicInfos();
        framgentAdapter = new MyFragmentAdapter(fm,musicInfos,Constant.Search_Fragment);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(framgentAdapter);
            }
        });
    }

    @Override
    public void onItemClick(final MusicInfo music) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PlayerController.get().play(music);
            }
        });

    }

}
