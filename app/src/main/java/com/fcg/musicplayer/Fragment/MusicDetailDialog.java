package com.fcg.musicplayer.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.R;

public class MusicDetailDialog extends DialogFragment {

    private MusicInfo musicInfo;
    private TextView file_location;

    private MusicDetailDialog(MusicInfo musicInfo){
        this.musicInfo = musicInfo;
    }

    public static MusicDetailDialog newInstance(MusicInfo musicInfo){
        MusicDetailDialog musicDetailDialog = new MusicDetailDialog(musicInfo);
        return musicDetailDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_detail,container);
        file_location = view.findViewById(R.id.file_locatiom);
        file_location.setText(musicInfo.path);
        return view;
    }
}
