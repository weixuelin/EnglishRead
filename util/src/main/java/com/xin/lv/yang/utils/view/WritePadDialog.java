package com.xin.lv.yang.utils.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xin.lv.yang.utils.R;


/**
 * 手写签名
 */
public class WritePadDialog extends Dialog {

    private Context context;
    private LayoutParams p;
    private DialogListener dialogListener;

    /**
     * 需要的字体颜色id.
     */
    private int mColorIndex;

    /**
     * 背景的颜色或者图片
     */
    private int bgId = 0;

    public void setBgId(int bgId) {
        this.bgId = bgId;
    }

    public interface DialogListener {

        void refreshActivity(Bitmap bitmap);

    }


    public WritePadDialog(Context context, int textColor, int bgId, DialogListener dialogListener) {
        super(context);
        this.context = context;
        this.dialogListener = dialogListener;
        this.mColorIndex = textColor;
        this.bgId = bgId;

    }

    private static final int BACKGROUND_COLOR = Color.WHITE;

    private static final int BRUSH_COLOR = Color.BLACK;

    private PaintView mView;

    private ImageView imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.write_pad);

        p = getWindow().getAttributes();           //获取对话框当前的参数值
        p.height = (int) (getH() * 0.6);           //高度设置为屏幕的0.6
        p.width = (int) (getW() * 0.9);            //宽度设置为屏幕的0.9
        getWindow().setAttributes(p);              //设置生效

        mView = new PaintView(context);
        FrameLayout frameLayout = findViewById(R.id.tablet_view);
        frameLayout.addView(mView);
        mView.requestFocus();
        Button btnClear = findViewById(R.id.tablet_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mView.clear();
            }
        });

        RelativeLayout relativeLayout = findViewById(R.id.bg_relative);
        if (bgId != 0) {
            relativeLayout.setBackgroundResource(bgId);
        }

        Button btnOk = findViewById(R.id.tablet_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    // 获取到生成好的 bitmap 图片
                    dialogListener.refreshActivity(mView.getCachebBitmap());
                    WritePadDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnCancel = findViewById(R.id.tablet_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        imageViewBack = findViewById(R.id.image_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    public int getW() {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        return width;
    }


    public int getH() {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;   // 屏幕高度（像素）
        return height;
    }

    /**
     * 签字框的自定义view
     */
    class PaintView extends View {
        private Paint paint;
        private Canvas cacheCanvas;
        private Bitmap cachebBitmap;
        private Path path;

        public Bitmap getCachebBitmap() {
            return cachebBitmap;
        }

        public PaintView(Context context) {
            super(context);
            init();
        }

        private void init() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(mColorIndex == 0 ? Color.BLACK : mColorIndex);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setStrokeWidth(6f);
            path = new Path();
            cachebBitmap = Bitmap.createBitmap(p.width, (int) (p.height * 0.8), Bitmap.Config.ARGB_8888);
            cacheCanvas = new Canvas(cachebBitmap);
            cacheCanvas.drawColor(Color.WHITE);
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
            // canvas.drawColor(BRUSH_COLOR);
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

        @SuppressLint("ClickableViewAccessibility")
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

}
