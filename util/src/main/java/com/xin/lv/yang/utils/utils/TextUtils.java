package com.xin.lv.yang.utils.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class TextUtils {

    private static TextUtils text;

    public synchronized static TextUtils getInstance() {
        if (text == null) {
            text = new TextUtils();
        }
        return text;
    }


    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 0L ? "error" : (var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F)) + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F)) + "MB" : var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F)) + "GB")));
    }


    public static final int COLOR = 1;
    public static final int TEXT_SIZE = 2;
    public static final int BACKGROUND_COLOR = 3;
    public static final int DELETE_LINE = 4;
    public static final int XIA_LINE = 5;
    public static final int UP_BIAO = 6;
    public static final int DOWN_BIAO = 7;
    public static final int BOLD = 8;
    public static final int ITALIC = 9;
    public static final int BOLD_AND_ITALIC = 10;


    /**
     * 设置指定字的颜色
     */
    public void setColor(Context context, SpannableString sp, int startIndex, int endIndex, int colorRes, int type) {
        switch (type) {
            case COLOR:
                sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorRes)), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
            case BACKGROUND_COLOR:
                sp.setSpan(new BackgroundColorSpan(context.getResources().getColor(colorRes)), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
        }

    }


    /**
     * 设置文字大小
     */
    public void setTextSize(Context context, SpannableString sp, int startIndex, int endIndex, float size, int type) {
        switch (type) {
            case TEXT_SIZE:
                sp.setSpan(new AbsoluteSizeSpan(sp2px(context, size)), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
        }

    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 画下划线 或者删除线  上标  下标
     */
    public void setLine(Context context, SpannableString sp, int startIndex, int endIndex, int type) {
        switch (type) {
            case DELETE_LINE:
                sp.setSpan(new StrikethroughSpan(), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
            case XIA_LINE:
                sp.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
            case UP_BIAO:
                sp.setSpan(new SuperscriptSpan(), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
            case DOWN_BIAO:
                sp.setSpan(new SubscriptSpan(), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
            case BOLD:
                sp.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
            case ITALIC:
                sp.setSpan(new StyleSpan(Typeface.ITALIC), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
            case BOLD_AND_ITALIC:
                sp.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                break;
        }

    }

    /**
     * 设置指定字符 点击事件
     */
    public void setClick(Context context, SpannableString sp, int startIndex, int endIndex, ClickableSpan clickableSpan) {
        sp.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(android.R.color.holo_blue_light)), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


    }

    /**
     * 设置超链接
     */
    public void setURLSpan(Context context, SpannableString sp, int startIndex, int endIndex, URLSpan urlSpan) {
        sp.setSpan(urlSpan, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(android.R.color.holo_blue_light)), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

    }


    public String replaceText(String str, int start, int end) {
        StringBuilder b = new StringBuilder(str);
        b.replace(start, end, "****");
        return b.toString();
    }

}
