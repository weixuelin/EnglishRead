package com.xin.lv.yang.utils.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.xin.lv.yang.utils.R;

/**
 * 自定义加载dialog
 */
public class CustomProgressDialog extends Dialog {

    Context context;

    public CustomProgressDialog(@NonNull Context context) {
        super(context, R.style.style);
        this.context = context;
        init();
    }

    private TextView textView;

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.load_layout, null);
        AVLoadingIndicatorView avl = view.findViewById(R.id.avl);
        textView = view.findViewById(R.id.tv_text);
        avl.show();
        setContentView(view);

    }

    private int getW() {
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }


    public void setText(String string) {
        textView.setText(string);
    }


    private static final String[] INDICATORS = new String[]{
            "BallPulseIndicator",
            "BallGridPulseIndicator",
            "BallClipRotateIndicator",
            "BallClipRotatePulseIndicator",
            "SquareSpinIndicator",
            "BallClipRotateMultipleIndicator",
            "BallPulseRiseIndicator",
            "BallRotateIndicator",
            "CubeTransitionIndicator",
            "BallZigZagIndicator",
            "BallZigZagDeflectIndicator",
            "BallTrianglePathIndicator",
            "BallScaleIndicator",
            "LineScaleIndicator",
            "LineScalePartyIndicator",
            "BallScaleMultipleIndicator",
            "BallPulseSyncIndicator",
            "BallBeatIndicator",
            "LineScalePulseOutIndicator",
            "LineScalePulseOutRapidIndicator",
            "BallScaleRippleIndicator",
            "BallScaleRippleMultipleIndicator",
            "BallSpinFadeLoaderIndicator",
            "LineSpinFadeLoaderIndicator",
            "TriangleSkewSpinIndicator",
            "PacmanIndicator",
            "BallGridBeatIndicator",
            "SemiCircleSpinIndicator",

    };


}
