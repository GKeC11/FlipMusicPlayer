package com.fcg.musicplayer.Unit;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionUnit {
    private Activity mActivity;

    public class PermissionModel{
        String permission;
        int permissionCode;
        public PermissionModel(String permission,int permissionCode){
            this.permission = permission;
            this.permissionCode = permissionCode;
        }
    }

    public PermissionUnit(Activity activity){
        mActivity = activity;
    }

    public void applyPermissions(String[] permissions){
        ArrayList<PermissionModel> permissionModels = new ArrayList<>();
        for(String permission : permissions){
            int i = 1;
            permissionModels.add(new PermissionModel(permission,i));
            i++;
        }

        for (PermissionModel model : permissionModels){
            if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mActivity,model.permission)){
                ActivityCompat.requestPermissions(mActivity,new String[]{model.permission},model.permissionCode);
                return;
            }
        }
    }
}
