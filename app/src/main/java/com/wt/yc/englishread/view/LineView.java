package com.wt.yc.englishread.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.wt.yc.englishread.R;

import java.util.HashMap;
import java.util.Map;


public class LineView extends View {
    Paint paint = new Paint();
    Context context;

    public LineView(Context context) {
        super(context);
        initView(context);
    }


    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        paint.setColor(context.getResources().getColor(R.color.blue_login));
        paint.setStrokeWidth(4);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);

    }

    /**
     * 题号的坐标
     */
    public static Map<Integer, Point> startMap = new HashMap<>();


    /**
     * 答案的坐标
     */
    public static Map<Integer, Point> endMap = new HashMap<>();

    /**
     * 题号和答案位置
     */
    public static Map<Integer, Integer> positionMap = new HashMap<>();

    /**
     * 颜色集合信息
     */
    public static Map<Integer, Integer> colorMap = new HashMap<>();

    public void drawLine() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Integer i : positionMap.keySet()) {

            Point startPoint = startMap.get(i);
            Point endPoint = endMap.get(positionMap.get(i));

            if (!colorMap.isEmpty()) {

                int color = colorMap.get(i);
                if (color != 0) {
                    paint.setColor(context.getResources().getColor(color));
                }
            }

            if (startPoint != null && endPoint != null) {
                canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint);
            }
        }

    }

    /**
     * 清除线条
     */
    public void clearLine() {
        startMap.clear();
        endMap.clear();
        positionMap.clear();
        colorMap.clear();

        invalidate();

    }

    /**
     * 改变颜色
     */
    public void changeColor() {
        invalidate();
    }

    /**
     * 改变颜色为默认颜色
     */
    public void changePaintColor() {
        paint.setColor(context.getResources().getColor(R.color.blue_login));

    }
}
