package com.xin.lv.yang.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.xin.lv.yang.utils.R;
import com.xin.lv.yang.utils.utils.ImageUtil;

import java.util.List;

/**
 * 图片轮播 ViewFlipper 控件
 */

public class CustomViewFlipper extends ViewFlipper {
    private OnViewFlipperListener onViewFlipperListener;

    public void setOnViewFlipperListener(OnViewFlipperListener onViewFlipperListener) {
        this.onViewFlipperListener = onViewFlipperListener;
    }

    public interface OnViewFlipperListener {
        void onShowNext(ViewFlipper viewFlipper);

        void onShowPrevious(ViewFlipper viewFlipper);
    }

    Context context;

    public CustomViewFlipper(Context context) {
        super(context);
        this.context = context;
        init();
    }


    public CustomViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Override
    public void showNext() {
        super.showNext();
        if (onViewFlipperListener != null) {
            onViewFlipperListener.onShowNext(this);
        }
    }

    @Override
    public void showPrevious() {
        super.showPrevious();
        if (onViewFlipperListener != null) {
            onViewFlipperListener.onShowPrevious(this);
        }
    }


    /**
     * 开始播放
     */
    private void init() {
        this.setFlipInterval(5000);
        this.setAutoStart(true);
        this.startFlipping();
        TranslateAnimation inAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inAnim.setDuration(1500);

        TranslateAnimation outAnim = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);

        outAnim.setDuration(1500);
        this.setInAnimation(inAnim);
        this.setOutAnimation(outAnim);
    }



    /**
     * 加入 View
     * @param viewGroup  待加入的容器
     * @param list      数据
     * @param view     加入的view
     */
    public void initView(ViewGroup viewGroup, List<String> list, View view) {
        for (String t : list) {
            ImageView imageView = new ImageView(context);
            ImageUtil.getInstance().loadImageNoTransformation(context, imageView, R.mipmap.ic_launcher, t);
            this.addView(imageView);
        }

        int len = list.size();
        for (int i = 0; i < len; i++) {
            viewGroup.addView(view);
        }

    }

}
