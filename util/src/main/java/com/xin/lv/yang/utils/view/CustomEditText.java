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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.xin.lv.yang.utils.R;

public class CustomEditText extends AppCompatEditText {
    /**
     * 本控件的宽高
     */
    private int width;
    private int height;
    private OnVerifyInputCompleteListener listener;

    //底部的线 画笔
    private Paint mLinePaint=new Paint();
    //文字的画笔
    private Paint mTextPaint=new Paint();

    //计算出的每一个输入框的宽度
    private int textLineLength=60;

    //验证码的数量
    private int totalCount=1;
    //每一个验证码之间的距离
    private int intervalLength=10;

    public int getTextLineLength() {
        return textLineLength;
    }

    public void setTextLineLength(int textLineLength) {
        this.textLineLength = textLineLength;
    }

    //底部线的颜色
    private int lineColor = android.R.color.holo_red_light;

    //输入文字的颜色
    private int textColor = android.R.color.black;

    StringBuilder sb=new StringBuilder();

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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

    public int getTextColor() {
        return textColor;
    }

    @Override
    public void setTextColor(int textColor) {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView(final Context context) {
        setBackground(null);
        setMaxLines(1);
        setLines(1);
        setSingleLine(true);


//        setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return true;
//            }
//        });
//
//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(CustomEditText.this, 0);
//                return true;
//            }
//        });


        setTextColor(context.getResources().getColor(android.R.color.transparent));

        Log.i("result","-------"+lineColor+"-------"+textColor);

        mLinePaint.setColor(context.getResources().getColor(lineColor));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3);

        mTextPaint.setTextSize(50f);

        mTextPaint.setFakeBoldText(true);

        if(textColor!=0){
            mTextPaint.setColor(context.getResources().getColor(textColor));
        }else{
            mTextPaint.setColor(context.getResources().getColor(android.R.color.black));
        }

        mTextPaint.setAntiAlias(true);

        Log.i("result","padding--------");

        this.addTextChangedListener(textWatcher);

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

                // TODO 只能输入指定totalCount个数的字多出的需要删除
                if (s.toString().length() > totalCount) {
                    s.delete(totalCount, s.length());
                    return;
                }
                sb.append(s);
                if (s.toString().length() == totalCount && listener != null) {
                    listener.onCompleteInput(sb.toString());
                }

            }

            int paddingLeft;

            /**
             * 计算验证码输入之后光标显示的位置
             * 如果已经输完验证码则光标需要显示在最后一个字符的后面而不是下一个文字输入框
             * */
            if (sb.length() < totalCount) {
                paddingLeft = (int) ((textLineLength + intervalLength) * sb.length()
                        + textLineLength / 2 - getPaint().measureText(!TextUtils.isEmpty(sb.toString()) ? sb.toString() : ""));

            } else {
                paddingLeft = (int) ((textLineLength + intervalLength) * (sb.length() - 1)
                        + (mTextPaint.measureText(sb.substring(sb.length() - 2, sb.length() - 1)) / 2)
                        + textLineLength / 2 - getPaint().measureText(!TextUtils.isEmpty(sb.toString()) ? sb.toString() : ""));
            }
            setPadding(paddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
    };


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

        if(!sb.toString().equals("")) {

            for (int i = 0; i < totalCount; i++) {

                String textString = String.valueOf((sb != null && sb.length() > i) ? sb.charAt(i) : "");
                canvas.drawText(textString, currentLength + textLineLength / 2 - mTextPaint.measureText(textString) / 2, height / 2 + mTextPaint.getTextSize() / 2, mTextPaint);

                currentLength += textLineLength + intervalLength;

            }
        }

        currentLength = 0;

        for (int i = 0; i < totalCount; i++) {

            canvas.drawLine(currentLength, height, textLineLength * (i + 1) + intervalLength * i, height, mLinePaint);

            currentLength += textLineLength + intervalLength;

        }

    }


    /**
     * 设置输入完成监听
     */
    public void setOnVerifyInputCompleteListener(OnVerifyInputCompleteListener listener) {
        this.listener = listener;
    }


    interface  OnVerifyInputCompleteListener{
        void onCompleteInput(String result);
    }


}
