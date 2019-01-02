package com.xin.lv.yang.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.xin.lv.yang.utils.R;


/**
 * 带进度提示条的ImageView 包括圆形 和 矩形
 */
@SuppressLint("AppCompatCustomView")
public class ProgressImageView extends ImageView {

    private Paint slipPaint = null;
    private Paint maskPaint = null;
    private Paint textPaint = null;

    private int borderColor = 0;
    private int borderWidth = 0;
    private int progress = 101;

    private Bitmap shape = null;
    private Bitmap mask = null;

    private boolean isNewMask = true;
    private boolean isCircle = true;
    private boolean isText = true;

    /**
     * 从上往下
     */
    private boolean topToDown = true;

    public void setTopToDown(boolean topToDown) {
        this.topToDown = topToDown;
    }

    public void setNewMask(boolean newMask) {
        isNewMask = newMask;
    }

    public void setIsText(boolean text) {
        isText = text;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }

    public ProgressImageView(Context context) {
        super(context);
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressImageView);
        borderColor = typedArray.getColor(R.styleable.ProgressImageView_borderColor, getResources().getColor(R.color.color));
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.ProgressImageView_borderWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getContext().getResources().getDisplayMetrics()));
        int textColor = typedArray.getColor(R.styleable.ProgressImageView_textColor, Color.WHITE);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.ProgressImageView_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        progress = typedArray.getInt(R.styleable.ProgressImageView_progress, 0);
        boolean is=typedArray.getBoolean(R.styleable.ProgressImageView_circle,false);

        isCircle = is;

        typedArray.recycle();

        slipPaint = new Paint();
        slipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        slipPaint.setFilterBitmap(false);

        maskPaint = new Paint();
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        maskPaint.setFilterBitmap(false);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);  // 设置20sp大小的文字

    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        try {
            int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            drawable.setBounds(0, 0, getWidth(), getHeight());
            drawable.draw(canvas);

//            //切割
//            if (shape == null || shape.isRecycled()) {
//                shape = getShape(getWidth(), getHeight());
//                canvas.drawBitmap(shape, 0, 0, slipPaint);
//            }

//            Paint ringPaint = new Paint();
//            ringPaint.setAntiAlias(true);
//            ringPaint.setStyle(Paint.Style.STROKE);   // 内部空心
//            ringPaint.setColor(borderColor);
//            ringPaint.setStrokeWidth(borderWidth);
//            ringPaint.setAlpha(160);

//            if (isCircle) {
//                //画圆环
//                canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() - borderWidth) / 2, ringPaint);
//
//            } else {
//
//                int w = getWidth();
//                int h = getHeight();
//                //画矩形框
//                canvas.drawRect(0, 0, w, h, ringPaint);
//
//            }

//            // 画透明格子
//            if (isNewMask) {
//                mask = getMask(getWidth(), getHeight());
//                canvas.drawBitmap(mask, 0, 0, maskPaint);
//                isNewMask = false;
//            }

            if (isText) {
                // 画文字
                String text = progress + "%";
                Rect textRect = new Rect();
                textPaint.getTextBounds(text, 0, text.length(), textRect);
                canvas.drawText(text, (getWidth() - textRect.width()) / 2, (getHeight() + textRect.height()) / 2, textPaint);
            }

            canvas.restoreToCount(layer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getShape(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF localRectF = new RectF(0, 0, width, height);
        Paint paint = new Paint();
        paint.setAntiAlias(true); //去锯齿
        canvas.drawOval(localRectF, paint);
        return bitmap;
    }


    private Bitmap getMask(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);  //去锯齿
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWidth);
        paint.setAlpha(160);

        if (this.progress <= 100) {

            if (topToDown) {
                if (progress == 0) {
                    canvas.drawRect(0, 0, width, height, paint);
                } else {
                    // 从上往下
                    canvas.drawRect(0, height / 100 * (progress), width, height, paint);
                }

            } else {
                // 从下往上
                canvas.drawRect(0, height / 100 * (100 - progress), width, height, paint);
            }

        } else {
            canvas.drawRect(0, 0, width, height, paint);

        }

        return bitmap;
    }


    /**
     * 设置提示进度
     * @param progress  进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        isNewMask = true;
        this.invalidate();  // 重新绘制
    }
}
