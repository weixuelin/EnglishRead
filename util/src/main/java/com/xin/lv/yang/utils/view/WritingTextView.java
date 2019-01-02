package com.xin.lv.yang.utils.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.xin.lv.yang.utils.R;

import static com.xin.lv.yang.utils.utils.TextUtils.BACKGROUND_COLOR;


public class WritingTextView extends View {
    private Paint paint;
    private Canvas cacheCanvas;
    private Bitmap cachebBitmap;
    private Path path;
    private int mColorIndex;
    private WindowManager.LayoutParams p;


    public WritingTextView(Context context) {
        super(context);
        init(context, null);
    }


    public WritingTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WritingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attributeSet) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mColorIndex == 0 ? Color.BLACK : mColorIndex);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setStrokeWidth(2f);
        path = new Path();

        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.writingStyle, 0, 0);

        int w = a.getDimensionPixelSize(R.styleable.writingStyle_w, 100);
        int h = a.getDimensionPixelSize(R.styleable.writingStyle_h, 100);

        cachebBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        cacheCanvas.drawColor(Color.WHITE);
    }

    public Bitmap getCachebBitmap() {
        return cachebBitmap;
    }

    public void clear() {
        if (cacheCanvas != null) {
            paint.setColor(BACKGROUND_COLOR);
            cacheCanvas.drawPaint(paint);
            paint.setColor(Color.BLACK);
            cacheCanvas.drawColor(Color.WHITE);
            invalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(cachebBitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
        int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
        if (curW >= w && curH >= h) {
            return;
        }

        if (curW < w)
            curW = w;
        if (curH < h)
            curH = h;

        Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);
        if (cachebBitmap != null) {
            newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
        }
        // 赋值
        cachebBitmap = newBitmap;
        cacheCanvas = newCanvas;
    }


    private float cur_x, cur_y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                cur_x = x;
                cur_y = y;
                path.moveTo(cur_x, cur_y);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                path.quadTo(cur_x, cur_y, x, y);
                cur_x = x;
                cur_y = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
            }
        }

        // 更新画布
        invalidate();
        return true;
    }

}
