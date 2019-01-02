package com.xin.lv.yang.utils.photo.image;

import android.content.Context;
import android.os.Build;

import com.xin.lv.yang.utils.photo.GestureDetector;
import com.xin.lv.yang.utils.photo.OnGestureListener;


/**
 * Created by Administrator on 2017/5/15.
 */

public class VersionedGestureDetector {
    public VersionedGestureDetector() {
    }

    public static GestureDetector newInstance(Context context, OnGestureListener listener) {
        int sdkVersion = Build.VERSION.SDK_INT;
        Object detector;
        if(sdkVersion < 5) {
            detector = new CupcakeGestureDetector(context);
        } else if(sdkVersion < 8) {
            detector = new EclairGestureDetector(context);
        } else {
            detector = new FroyoGestureDetector(context);
        }

        ((GestureDetector)detector).setOnGestureListener(listener);
        return (GestureDetector)detector;
    }
}
