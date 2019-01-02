package com.xin.lv.yang.utils.net;

import android.os.Handler;
import android.os.Message;


public class Time extends Thread {

    private Handler handler;
    private int num;
    private int what;

    public Time(int num, int what,Handler handler) {
        this.num = num;
        this.handler = handler;
        this.what=what;
    }

    @Override
    public void run() {
        super.run();
        for (int i = num; i >= 0; i--) {
            Message message = handler.obtainMessage();
            message.arg1 = i;
            message.what=what;
            handler.sendMessage(message);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
