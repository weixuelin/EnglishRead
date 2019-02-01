package com.xin.lv.yang.utils.view;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import com.xin.lv.yang.utils.R;


/**
 * 密码输入框 默认六位密码
 */
public class PasswordEditText extends AppCompatEditText {

    private int textLength = 6;

    private int borderColor;
    private float borderWidth;
    private float borderRadius;

    private int passwordLength;
    private int passwordColor;
    private float passwordWidth;
    private float passwordRadius;

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    /**
     * 密码画笔
     */
    private Paint passwordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     *
     * 分割线画笔
     */
    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int defaultSplitLineWidth = 1;

    public PasswordEditText(Context context) {
        super(context);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        final Resources res = getResources();

        final int defaultBorderColor = res.getColor(android.R.color.darker_gray);

        final float defaultBorderWidth = res.getDimension(R.dimen.dp_1);
        final float defaultBorderRadius = res.getDimension(R.dimen.dp_8);

        final int defaultPasswordLength = textLength;

        final int defaultPasswordColor = res.getColor(android.R.color.black);
        final float defaultPasswordWidth = res.getDimension(R.dimen.dp_6);
        final float defaultPasswordRadius = res.getDimension(R.dimen.dp_6);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0);

        try {
            borderColor = a.getColor(R.styleable.PasswordInputView_pwdBorderColor, defaultBorderColor);
            borderWidth = a.getDimension(R.styleable.PasswordInputView_pwdBorderWidth, defaultBorderWidth);
            borderRadius = a.getDimension(R.styleable.PasswordInputView_pwdBorderRadius, defaultBorderRadius);
            passwordLength = a.getInt(R.styleable.PasswordInputView_pwdPasswordLength, defaultPasswordLength);
            passwordColor = a.getColor(R.styleable.PasswordInputView_pwdPasswordColor, defaultPasswordColor);
            passwordWidth = a.getDimension(R.styleable.PasswordInputView_pwdPasswordWidth, defaultPasswordWidth);
            passwordRadius = a.getDimension(R.styleable.PasswordInputView_pwdPasswordRadius, defaultPasswordRadius);
        } finally {
            a.recycle();
        }

        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);
        passwordPaint.setStrokeWidth(passwordWidth);
        passwordPaint.setStyle(Paint.Style.FILL);
        passwordPaint.setColor(passwordColor);

        setSingleLine(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();

        Log.i("result","result-------"+passwordLength);

        /// 绘制分割线

        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(defaultSplitLineWidth);

        for (int i = 1; i < passwordLength; i++) {
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        ///  绘制密码
        float cx, cy = height / 2;
        float half = width / passwordLength / 2;
        for (int i = 0; i < textLength; i++) {
            cx = width * i / passwordLength + half;
            canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
        }
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        borderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
        invalidate();
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        invalidate();
    }

    public int getPasswordColor() {
        return passwordColor;
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        passwordPaint.setColor(passwordColor);
        invalidate();
    }

    public float getPasswordWidth() {
        return passwordWidth;
    }

    public void setPasswordWidth(float passwordWidth) {
        this.passwordWidth = passwordWidth;
        passwordPaint.setStrokeWidth(passwordWidth);
        invalidate();
    }

    public float getPasswordRadius() {
        return passwordRadius;
    }

    public void setPasswordRadius(float passwordRadius) {
        this.passwordRadius = passwordRadius;
        invalidate();
    }


    public void changeLen(int len){
        setText("");
        textLength=len;
        passwordLength=len;
        invalidate();

    }

}
