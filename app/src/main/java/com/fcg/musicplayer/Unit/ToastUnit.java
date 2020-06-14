package com.fcg.musicplayer.Unit;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUnit {
    private static Context sContext;
    private static Toast sToast;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static void show(int resId) {
        show(sContext.getString(resId));
    }

    public static void show(String text) {
        if (sToast == null) {
            Looper.prepare();
            sToast = Toast.makeText(sContext, text, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(text);
        }
        sToast.show();
        Looper.loop();
    }
}