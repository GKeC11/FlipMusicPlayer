package com.fcg.musicplayer.Unit;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.fcg.musicplayer.AppCache;
import com.fcg.musicplayer.Constant;
import com.fcg.musicplayer.Data.MusicInfo;
import com.fcg.musicplayer.Data.MusicInfoData;
import com.fcg.musicplayer.Data.MusicUrlData;
import com.fcg.musicplayer.Listener.MainActivityListener;
import com.fcg.musicplayer.Listener.NotificationListener;
import com.fcg.musicplayer.Listener.SearchResultListener;
import com.google.gson.Gson;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.UnifiedListenerManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchUnit {

    private static OkHttpClient client;
    private static String musicInfoJson;
    private static Gson gson;
    private static ArrayList<MusicInfo> musicInfos;
    private static SearchResultListener searchResultListener;
    private static MusicInfo mMusic;
    private int mId;
    private DownloadTask task;
    private Context context;
    private NotificationListener listener;
    private UnifiedListenerManager listenerManager;
    private CancelReceiver cancelReceiver;
    private MainActivityListener mainActivityListener;

    private SearchUnit(){
        client = new OkHttpClient();
        gson = new Gson();
        listenerManager = new UnifiedListenerManager();
        context = AppCache.getInstance().getContext();
    }

    private static class SingletonHolder{
        private static final SearchUnit instance = new SearchUnit();
    }

    public static SearchUnit get(){
        return SingletonHolder.instance;
    }

    public void search(String key){
        Request request = new Request.Builder()
                .url("http://139.224.28.219:3000/search?keywords=" + key)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new searchCallBack());
    }

    public void playOnline(MusicInfo musicInfo){

        mMusic = musicInfo;
        mId = musicInfo.id;

        Request request = new Request.Builder()
                .url("http://139.224.28.219:3000/song/url?id=" + mId)
                .build();

        client.newCall(request).enqueue(new ClickCallback());
    }

    public void getUrl(MusicInfo musicInfo){

        mMusic = musicInfo;
        mId = musicInfo.id;

        Request request = new Request.Builder()
                .url("http://139.224.28.219:3000/song/url?id=" + mId)
                .build();

        client.newCall(request).enqueue(new ClickCallback(1));
    }

    private static class searchCallBack implements Callback {

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d(Constant.mTag, "onFailure: " + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if(response.isSuccessful()){
                musicInfoJson = response.body().string();
                MusicInfoData musicInfoData = gson.fromJson(musicInfoJson,MusicInfoData.class);
                musicInfos = new ArrayList<>();
                for (MusicInfoData.Song song : musicInfoData.result.songs) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.id = song.id;
                    musicInfo.name = song.name;
                    musicInfo.artists = song.artists.get(0).name;
                    musicInfo.duration = song.duration;
                    musicInfos.add(musicInfo);
                }
                searchResultListener.onSuccess();
            }
        }
    }

    private static  class ClickCallback implements Callback{

        private String musicUrlJson;
        private int type = 0;
        private static final int Click = 0;
        private static final int LongClick = 1;

        public ClickCallback(){ }

        public ClickCallback(int type){
            this.type = type;
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            ToastUnit.show("Play Failed");
            Log.d(Constant.mTag, "onFailure: " + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if(response.isSuccessful()){
                musicUrlJson = response.body().string();
                MusicUrlData musicUrlData = gson.fromJson(musicUrlJson,MusicUrlData.class);
                String url = musicUrlData.data.get(0).url;
                mMusic.path = url;
                mMusic.size = musicUrlData.data.get(0).size;

                if(type==0){
                    searchResultListener.onItemClick(mMusic);
                }
            }
        }
    }

    public ArrayList<MusicInfo> getMusicInfos(){
        if(musicInfos.size() > 0){
            return musicInfos;
        }

        return null;
    }

    public void registerSearchActivityListener(SearchResultListener listener){
        this.searchResultListener = listener;
    }

    public void download(final MusicInfo musicInfo){

        String url = musicInfo.path;
        String type = url.substring(url.lastIndexOf(".") + 1);
        File dir = AppCache.getInstance().getContext().getExternalFilesDir("Music");
        String fileName = musicInfo.name + "_" + musicInfo.artists + "." + type;

        if(url == null){
            ToastUnit.show("Please try again, that's something wrong");
        }

        task = new DownloadTask.Builder(url,dir)
                .setFilename(fileName)
                .build();

        initListener();

        listenerManager.enqueueTaskWithUnifiedListener(task,listener);

        final ContentValues contentValues =  new ContentValues();
        contentValues.put(MediaStore.Audio.Media.DATA,task.getFile().getAbsolutePath());
        contentValues.put(MediaStore.Audio.Media.SIZE,musicInfo.size);
        contentValues.put(MediaStore.Audio.Media._ID,musicInfo.id);
        contentValues.put(MediaStore.Audio.Media.TITLE,musicInfo.name);
        contentValues.put(MediaStore.Audio.Media.DURATION,musicInfo.duration);
        contentValues.put(MediaStore.Audio.Media.ARTIST,musicInfo.artists);

        listener.attachTaskEndRunnable(new Runnable() {
            @Override
            public void run() {
                context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,contentValues);
                mainActivityListener.onDownloadSuccess(musicInfo);
                ToastUnit.show("Download Success");
            }
        });
    }

    public void initListener(){
        listener = new NotificationListener(context);

        final Intent intent = new Intent(CancelReceiver.ACTION);
        final PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        listener.setAction(new NotificationCompat.Action(0, "Cancel", cancelPendingIntent));
        listener.initNotification();

        IntentFilter filter = new IntentFilter(CancelReceiver.ACTION);
        cancelReceiver = new CancelReceiver(task);
        context.registerReceiver(cancelReceiver, filter);

        listenerManager.attachListener(task, listener);
        listenerManager.addAutoRemoveListenersWhenTaskEnd(task.getId());
    }

    static class CancelReceiver extends BroadcastReceiver {
        static final String ACTION = "cancelOkdownload";

        private DownloadTask task;

        CancelReceiver(@NonNull DownloadTask task) {
            this.task = task;
        }

        @Override public void onReceive(Context context, Intent intent) {
            this.task.cancel();
        }
    }

    public void registerMainActivityListener(MainActivityListener listener){
        this.mainActivityListener = listener;
    }
}
