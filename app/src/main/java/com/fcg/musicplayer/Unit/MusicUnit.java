package com.fcg.musicplayer.Unit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.fcg.musicplayer.Data.MusicInfo;

import java.util.ArrayList;

public class MusicUnit {
    public static final int FILE_SIZE = 1024 * 1024 * 1; // 1MB
    public static final int DURATION = 1 * 1000 * 60; // 1MIN

    private static String[] proj_music = new String[]{
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE};

    public static ArrayList<MusicInfo> queryMusic(Context context){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        StringBuilder select = new StringBuilder(" 1=1 and title != ''");
        select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILE_SIZE);
        select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + DURATION);

        ArrayList<MusicInfo> local_list = getMusicListFromCursor(contentResolver.query(uri,proj_music,select.toString(),null,null));

        return local_list;
    }

    public static ArrayList<MusicInfo> getMusicListFromCursor(Cursor cursor){
        if(cursor == null){
            return null;
        }

        ArrayList<MusicInfo> musicInfos = new ArrayList<>();
        while(cursor.moveToNext()){
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            musicInfo.size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            musicInfo.duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            musicInfo.path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            musicInfos.add(musicInfo);
        }
        cursor.close();
        return musicInfos;
    }
}
