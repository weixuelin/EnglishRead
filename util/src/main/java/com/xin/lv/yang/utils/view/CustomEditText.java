package com.xin.lv.yang.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import com.xin.lv.yang.utils.R;

public class CustomEditText extends AppCompatEditText {
    /**
     * 本控件的宽高
     */
    private int width;
    private int height;

    private OnVerifyInputCompleteListener listener;

    /**
     * 底部的线 画笔
     */
    private Paint mLinePaint = new Paint();

    /**
     * 文字的画笔
     */
    private Paint mTextPaint = new Paint();

    /**
     * 计算出的每一个输入框的宽度
     */
    private int textLineLength = 80;

    /**
     * 验证码的数量
     */
    private int totalCount = 1;

    /**
     * 每一个字符之间的距离
     */
    private int intervalLength = 10;

    public int getTextLineLength() {
        return textLineLength;
    }

    public void setTextLineLength(int textLineLength) {
        this.textLineLength = textLineLength;
    }

    /**
     * 底部线的颜色
     */
    private int lineColor = android.R.color.holo_red_light;

    /**
     * 输入文字的颜色
     */
    private int textColor = 0;

    StringBuilder sb = new StringBuilder();

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(totalCount)});

    }

    public int getIntervalLength() {
        return intervalLength;
    }

    public void setIntervalLength(int intervalLength) {
        this.intervalLength = intervalLength;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getCustomTextColor() {
        return textColor;
    }


    public void setCustomTextColor(int textColor) {
        this.textColor = textColor;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public CustomEditText(Context context) {
        super(context);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerifyEditText, defStyleAttr, 0);
        totalCount = typedArray.getInt(R.styleable.VerifyEditText_total_count, 6);
        intervalLength = typedArray.getInt(R.styleable.VerifyEditText_interval_length, 30);

        lineColor = typedArray.getColor(R.styleable.VerifyEditText_line_color, android.R.color.holo_red_light);
        textColor = typedArray.getColor(R.styleable.VerifyEditText_text_color, android.R.color.black);

        typedArray.recycle();

        initView(context);

    }


    float startX = 0;
    float startY = 0;
    float endX = 0;
    float endY = 0;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView(Context context) {
        setBackground(null);
        setMaxLines(1);
        setLines(1);
        setSingleLine(true);

        setTextColor(context.getResources().getColor(android.R.color.transparent));

        Log.i("result", "-------" + lineColor + "-------" + textColor);

        mLinePaint.setColor(context.getResources().getColor(lineColor));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3);

        mTextPaint.setTextSize(24f);

        mTextPaint.setFakeBoldText(false);

        Log.i("result", "颜色--------" + textColor);

        if (textColor != 0) {
            mTextPaint.setColor(context.getResources().getColor(textColor));
        } else {
            mTextPaint.setColor(context.getResources().getColor(android.R.color.black));
        }

        mTextPaint.setAntiAlias(true);

        Log.i("result", "padding--------");

        this.addTextChangedListener(textWatcher);

        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        startX = event.getX();
                        startY = event.getY();

                        break;

                    case MotionEvent.ACTION_UP:

                        endX = event.getX();
                        endY = event.getY();

                        Log.i("result", "startX-----" + startX + "--startY-----" + startY + "---endX----" + endX + "---endY-----" + endY);

                        if (Math.abs(startX - endX) < 5 && Math.abs(startY - endY) < 5) {

                            chooseIndex(startX);

                        }

                        break;
                }

                return false;
            }
        });

    }

    /**
     * 显示光标位置
     *
     * @param startX
     * @return
     */
    private int chooseIndex(float startX) {
        int len = choose(startX);

        Log.i("result", "-----len-----" + len);

        int paddingLeft = 0;

        if (len != -1) {

            paddingLeft = (textLineLength + intervalLength) * (len + 1) - (textLineLength + intervalLength) / 2;

        }

        setPadding(paddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());

        return paddingLeft;
    }


    private int choose(float startX) {
        int ll = lenList.size();

        for (int i = 0; i < ll; i++) {

            int saveLen = lenList.get(i);
            if (Math.abs(saveLen - startX) <= (textLineLength + intervalLength) / 2) {
                return i;
            }

        }

        return -1;
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            sb.delete(0, sb.length());

            if (!TextUtils.isEmpty(s.toString())) {

                //// TODO 只能输入指定totalCount个数的字多出的需要删除
                if (s.toString().length() > totalCount) {
                    s.delete(totalCount, s.length());
                    return;
                }

                sb.append(s);
                if (s.toString().length() == totalCount && listener != null) {
                    listener.onCompleteInput(sb.toString());
                }

            }

            setIndexFocus();

        }
    };

    /**
     * 计算验证码输入之后光标显示的位置
     * 如果已经输完验证码则光标需要显示在最后一个字符的后面而不是下一个文字输入框
     **/

    private void setIndexFocus() {
        int paddingLeft;

        int textLen = sb.length();

        if (textLen < totalCount) {
            paddingLeft = (int) ((textLineLength + intervalLength) * textLen
                    + textLineLength / 2 - getPaint().measureText(!TextUtils.isEmpty(sb.toString()) ? sb.toString() : ""));

        } else {
            paddingLeft = (int) ((textLineLength + intervalLength) * (textLen - 1)
                    + (mTextPaint.measureText(sb.substring(textLen - 2, textLen - 1)) / 2)
                    + textLineLength / 2 - getPaint().measureText(!TextUtils.isEmpty(sb.toString()) ? sb.toString() : ""));
        }

        setPadding(paddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }


    /**
     * 距离集合
     */
    SparseIntArray lenList = new SparseIntArray();


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        // 每一个输入文字的宽度
        textLineLength = 80;

        // 定位光标的初始显示位置
        setPadding(textLineLength / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (totalCount <= 0) {
            return;
        }

        int currentLength = 0;

        if (!sb.toString().equals("")) {

            for (int i = 0; i < totalCount; i++) {

                String textString = String.valueOf((sb != null && sb.length() > i) ? sb.charAt(i) : "");

                canvas.drawText(textString, currentLength + textLineLength / 2 - mTextPaint.measureText(textString) / 2, height / 2 + mTextPaint.getTextSize() / 2, mTextPaint);

                currentLength += textLineLength + intervalLength;

            }
        }


        currentLength = 0;
        lenList.clear();

        for (int i = 0; i < totalCount; i++) {

            int stopLen = textLineLength * (i + 1) + intervalLength * i;

            lenList.put(i, currentLength + ((stopLen - currentLength) / 2));

            canvas.drawLine(currentLength, height, stopLen, height, mLinePaint);

            currentLength += textLineLength + intervalLength;

        }

    }


    /**
     * 设置输入完成监听
     */
    public void setOnVerifyInputCompleteListener(OnVerifyInputCompleteListener listener) {
        this.listener = listener;
    }


    interface OnVerifyInputCompleteListener {
        void onCompleteInput(String result);
    }


}
