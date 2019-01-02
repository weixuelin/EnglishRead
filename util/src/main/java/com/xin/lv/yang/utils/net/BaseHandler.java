package com.xin.lv.yang.utils.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * 基础 Handler
 */
public abstract class BaseHandler<T> extends Handler {

    protected WeakReference<T> weak;
    private static final int ERROR = 404;

    public BaseHandler(T t) {
        if (weak == null) {
            weak = new WeakReference<>(t);
        }
    }

    private static OnNetError onNetError;

    public void setOnNetError(OnNetError onNetError) {
        BaseHandler.onNetError = onNetError;
    }



}
