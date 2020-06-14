package com.fcg.musicplayer.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fcg.musicplayer.Constant;
import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Listener.OnMusicClickListener;
import com.fcg.musicplayer.Listener.PlayBarListener;
import com.fcg.musicplayer.MyAdapter;
import com.fcg.musicplayer.Controller.PlayerController;
import com.fcg.musicplayer.R;
import com.fcg.musicplayer.Unit.FileUnit;
import com.fcg.musicplayer.Unit.SearchUnit;

import java.util.ArrayList;

public class MyFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    private PlayBarListener listener;
    private ArrayList<MusicInfo> musicInfos = new ArrayList<>();
    private RecyclerView recyclerView;
    private MusicInfo musicInfoOnClick;
    private MyAdapter adapter;
    private int fragmentType;

    public static MyFragment newInstance(ArrayList<MusicInfo> musicInfos,int fragmentType){
        MyFragment fragment = new MyFragment();
        Bundle arg = new Bundle();

        arg.putParcelableArrayList("music_info",musicInfos);
        arg.putInt("fragmentType",fragmentType);
        fragment.setArguments(arg);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.musicInfos = getArguments().getParcelableArrayList("music_info");
        this.fragmentType = getArguments().getInt("fragmentType");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment,container,false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        adapter = new MyAdapter(musicInfos);

        if(fragmentType == Constant.Local_Fragment){
            adapter.setItemClickListener(new OnMusicClickListener() {
                @Override
                public void onClick(int position) {
                    PlayerController.get().play(musicInfos.get(position));
                }

                @Override
                public void onLongClick(int position) {
                    musicInfoOnClick = musicInfos.get(position);
                    RecyclerView.ViewHolder item = recyclerView.findViewHolderForAdapterPosition(position);
                    PopupMenu popupMenu = new PopupMenu(getContext(),item.itemView);
                    MenuInflater menuInflater = popupMenu.getMenuInflater();
                    menuInflater.inflate(R.menu.music_item_option, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(MyFragment.this);
                    popupMenu.show();
                }
            });
        }else if(fragmentType == Constant.Search_Fragment){
            adapter.setItemClickListener(new OnMusicClickListener() {
                @Override
                public void onClick(int position) {
                    SearchUnit.get().playOnline(musicInfos.get(position));
                }

                @Override
                public void onLongClick(int position) {
                    musicInfoOnClick = musicInfos.get(position);
                    SearchUnit.get().getUrl(musicInfoOnClick);

                    RecyclerView.ViewHolder item = recyclerView.findViewHolderForAdapterPosition(position);
                    PopupMenu popupMenu = new PopupMenu(getContext(),item.itemView);
                    MenuInflater menuInflater = popupMenu.getMenuInflater();
                    menuInflater.inflate(R.menu.search_item_option, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(MyFragment.this);
                    popupMenu.show();
                }
            });
        }

        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_musicInfo:
                MusicDetailDialog musicDetailDialog = MusicDetailDialog.newInstance(musicInfoOnClick);
                musicDetailDialog.show(getFragmentManager(),null);
                break;

            case R.id.menu_delete:
                musicInfos.remove(musicInfoOnClick);
                FileUnit.getInstance().deleteFile(musicInfoOnClick.path);
                adapter.notifyDataSetChanged();

            case R.id.search_download:
                SearchUnit.get().download(musicInfoOnClick);
                break;
        }
        return false;
    }
}
