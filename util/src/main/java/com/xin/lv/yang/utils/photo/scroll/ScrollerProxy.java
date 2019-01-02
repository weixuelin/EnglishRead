package com.xin.lv.yang.utils.photo.scroll;

import android.content.Context;
import android.os.Build;

/**
 * Created by Administrator on 2017/5/15.
 */

public abstract class ScrollerProxy  {
    public ScrollerProxy() {
    }

    public static ScrollerProxy getScroller(Context context) {
        return Build.VERSION.SDK_INT < 9?new PreGingerScroller(context):(Build.VERSION.SDK_INT < 14?new GingerScroller(context):new IcsScroller(context));
    }

    public abstract boolean computeScrollOffset();

    public abstract void fling(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);

    public abstract void forceFinished(boolean var1);

    public abstract boolean isFinished();

    public abstract int getCurrX();

    public abstract int getCurrY();
}
