package com.fcg.musicplayer.Unit;

import com.fcg.musicplayer.AppCache;

import java.io.File;
import java.io.IOException;

public class FileUnit {

    public FileUnit(){

    }

    private static class SingtonHolder{
        private static final FileUnit instance = new FileUnit();
    }

    public static FileUnit getInstance(){
        return SingtonHolder.instance;
    }

    public void deleteFile(String filePath){
        File file = new File(filePath);
        if(file.exists() && file.isFile()){
            if(file.delete()){
                ToastUnit.show("Delete Success");
            }else{
                if(file.exists()){
                    ToastUnit.show("Delete Fail");
                }
            }
        }
    }
}
