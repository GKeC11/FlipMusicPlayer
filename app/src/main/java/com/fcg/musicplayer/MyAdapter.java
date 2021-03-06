package com.fcg.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Listener.OnMusicClickListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<MusicInfo> musicInfoList;
    private OnMusicClickListener mListener;

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView info_view;
        TextView artist_textview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            info_view = itemView.findViewById(R.id.info_view);
            artist_textview = itemView.findViewById(R.id.artist_textview);
        }
    }

    public MyAdapter(List<MusicInfo> musicInfos){
        this.musicInfoList = musicInfos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        MusicInfo musicInfo = musicInfoList.get(position);
        holder.info_view.setText(musicInfo.name);
        holder.artist_textview.setText(musicInfo.artists);
        if(mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mListener.onClick(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    mListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return musicInfoList.size();
    }

    public void setItemClickListener(OnMusicClickListener listener){
        mListener = listener;
    }
}
