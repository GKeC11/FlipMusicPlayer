package com.fcg.musicplayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fcg.musicplayer.Controller.PlayerController;
import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<MusicInfo> musicInfos;

    public MyFragmentAdapter(@NonNull FragmentManager fm,ArrayList<MusicInfo> musicInfos,int fragmentType) {
        super(fm);

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
            MyFragment fragment = MyFragment.newInstance(temp_list,fragmentType);
            fragments.add(fragment);
        }

        musicInfos = null;
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
