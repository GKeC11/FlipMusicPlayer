package com.fcg.musicplayer.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fcg.musicplayer.AppCache;
import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.R;
import com.fcg.musicplayer.Unit.DensityUnit;

import org.w3c.dom.Text;

public class MusicDetailDialog extends DialogFragment {

    private MusicInfo musicInfo;
    private TextView file_location;
    private TextView file_size;

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
        file_size = view.findViewById(R.id.file_size);
        file_size.setText(musicInfo.size + "");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        int heigh = DensityUnit.get().dp2px(550);
        int width = DensityUnit.get().dp2px(300);
        getDialog().getWindow().setLayout(width,heigh);
    }
}
