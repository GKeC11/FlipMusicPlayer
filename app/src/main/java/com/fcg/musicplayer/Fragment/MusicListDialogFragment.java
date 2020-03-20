package com.fcg.musicplayer.Fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Unit.DensityUnit;
import com.fcg.musicplayer.MyAdapter;
import com.fcg.musicplayer.R;

import java.util.ArrayList;

public class MusicListDialogFragment extends DialogFragment {

    private View view;
    private TextView text;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<MusicInfo> musicInfos;
    private WindowManager.LayoutParams lp;
    private View locateView;

    public static MusicListDialogFragment newInstance(ArrayList<MusicInfo> musicInfoArrayList){
        MusicListDialogFragment dialogFragment = new MusicListDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("musicInfos",musicInfoArrayList);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        musicInfos = bundle.getParcelableArrayList("musicInfos");
        adapter = new MyAdapter(musicInfos);
    }

    @Override
    public void onResume() {
        super.onResume();

        int width = DensityUnit.get().dp2px(250);
        int height = DensityUnit.get().dp2px(350);

        getDialog().getWindow().setLayout(width,height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment,container);

        int[] location = new int[2];
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP|Gravity.LEFT);
        lp = window.getAttributes();
        locateView.getLocationOnScreen(location);
        lp.x = (location[0] - DensityUnit.get().dp2px(200));
        lp.y = (location[1] - DensityUnit.get().dp2px(350));
        window.setAttributes(lp);

        recyclerView = view.findViewById(R.id.dialog_musicList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void getSource(View source){
        locateView = source;
    }
}
