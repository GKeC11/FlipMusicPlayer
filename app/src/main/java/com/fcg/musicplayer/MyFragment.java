package com.fcg.musicplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    private PlayBarListener listener;
    private ArrayList<MusicInfo> musicInfos = new ArrayList<>();
    private RecyclerView recyclerView;

    public static  MyFragment newInstance(ArrayList<MusicInfo> musicInfos){
        MyFragment fragment = new MyFragment();
        Bundle arg = new Bundle();

        arg.putParcelableArrayList("music_info",musicInfos);
        fragment.setArguments(arg);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.musicInfos = getArguments().getParcelableArrayList("music_info");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment,container,false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        MyAdapter adapter = new MyAdapter(musicInfos);
        adapter.setItemClickListener(new OnMusicClickListener() {
            @Override
            public void onClick(int position) {
                AudioUnit.get().play(musicInfos.get(position));
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}
