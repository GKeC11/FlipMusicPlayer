package com.fcg.musicplayer;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.appcompat.widget.Toolbar;

import com.fcg.musicplayer.R;

public class ToolbarOfSearchHolder {
    public Toolbar mToolbar;
    public ImageButton search_btn;
    public EditText search_text;

    public ToolbarOfSearchHolder(View view){
        mToolbar = view.findViewById(R.id.toolbar_search);
        search_btn = view.findViewById(R.id.search_btn);
        search_text = view.findViewById(R.id.search_text);
    }
}
