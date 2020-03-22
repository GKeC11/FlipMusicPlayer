package com.fcg.musicplayer;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NavAdapter extends ArrayAdapter {

    private int resourceId;

    private class ViewHolder{
        TextView textView;
    }

    public NavAdapter(Context context, int resourceId, List<String> objects){
        super(context,resourceId,objects);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String menuItem = (String) getItem(position);
        View view;
        ViewHolder viewHolder;

        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.info_view);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.getPaint().setFakeBoldText(true);
        viewHolder.textView.setText(menuItem);

        return view;
    }
}
