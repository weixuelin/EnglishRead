package com.xin.lv.yang.utils.view;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xin.lv.yang.utils.R;

/**
 * 自定义通知
 */

public class CustomToast {

    private static TextView mTextView;
    private static ImageView mImageView;

    /**
     * @param context 上下文
     * @param gravity 位置
     * @param resId   显示图片的id
     * @param message 显示的文字
     */
    public static void showToast(Context context, int gravity, int resId, String message) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast, null);
        //初始化布局控件
        mTextView = toastRoot.findViewById(R.id.message);
        mImageView = toastRoot.findViewById(R.id.imageView);
        //为控件设置属性
        mTextView.setText(message);

        if(resId!=0){
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImageDrawable(ActivityCompat.getDrawable(context, resId));
        }else{
            mImageView.setVisibility(View.GONE);
        }

        // Toast的初始化
        Toast toastStart = new Toast(context);

        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            if (display != null) {
                int height = display.getHeight();
                // Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
                toastStart.setGravity(gravity, 0, height / 3);
                toastStart.setDuration(Toast.LENGTH_SHORT);
                toastStart.setView(toastRoot);
                toastStart.show();
            }
        }
    }

}
