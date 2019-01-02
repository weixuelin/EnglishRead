package com.xin.lv.yang.utils.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自定义可移动的悬浮按钮
 */

public class SuspendActionButton extends FloatingActionButton implements View.OnTouchListener {
    int lastX, lastY;
    int originX, originY;

    int screenWidth;
    int screenHeight;
    int buttonH;

    List<FloatingActionButton> list;

    ///注册归属的FloatingActionButton
    public void registerButton(FloatingActionButton floatingActionButton) {
        list.add(floatingActionButton);
    }

    //当被拖拽后其所属的FloatingActionButton 也要改变位置
    private void slideButton(int l, int t, int r, int b) {
        for (FloatingActionButton floatingActionButton : list) {
            floatingActionButton.layout(l, t, r, b);
        }
    }


    public List<FloatingActionButton> getButtons() {
        return list;
    }

    public SuspendActionButton(Context context) {
        super(context);
        init(context);
    }

    public SuspendActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SuspendActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        list = new ArrayList<>();
        screenWidth = getScreenW(context);
        screenHeight = getScreenH(context);
        buttonH = getBottomStatusHeight(context);
        Log.i("wwwwwww", "==www==" + screenWidth + "==hhhh==" + screenHeight + "==button===" + buttonH);
        setOnTouchListener(this);

    }


    /**
     * 获取屏幕高
     */
    private int getScreenH(Context context) {
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;     // 得到高度
    }


    /**
     * 获取屏幕宽
     */
    private int getScreenW(Context context) {
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;     //得到宽度

    }

    int l;
    int b;
    int r;
    int t;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isDraftable()) {
            return false;
        }
        int ea = event.getAction();
        switch (ea) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();   // 获取触摸事件触摸位置的原始X坐标
                lastY = (int) event.getRawY();
                originX = lastX;
                originY = lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getRawX() - lastX);
                int dy = (int) (event.getRawY() - lastY);
                l = v.getLeft() + dx;
                b = v.getBottom() + dy;
                r = v.getRight() + dx;
                t = v.getTop() + dy;
                // 下面判断移动是否超出屏幕
                if (l < 0) {
                    l = 0;
                    r = l + v.getWidth();
                }
                if (t < 0) {
                    t = 0;
                    b = t + v.getHeight();
                }
                if (r > screenWidth) {
                    r = screenWidth;
                    l = r - v.getWidth();
                }

                if (b > screenHeight - 2 * buttonH) {
                    b = screenHeight - 2 * buttonH;
                    t = b - v.getHeight();
                }

                v.layout(l, t, r, b);

                slideButton(l, t, r, b);

                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                v.postInvalidate();

                break;

            case MotionEvent.ACTION_UP:
                double l1 = event.getRawX() - originX;
                double l2 = event.getRawY() - originY;

                int distance = (int) Math.sqrt(l1 * l1 + l2 * l2);
                Log.i("wwwwwww", "=======" + distance);
                v.layout(l, t, r, b);

                if (distance < 20) {
                    // 当变化太小的时候什么都不做 OnClick执行
                    Log.i("wwwww", "执行到20=====");

                } else {
                    return true;
                }
                break;

        }

        return false;

    }


    /// 获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }


    public int getBottomStatusHeight(Context context) {
        return getDpi(context) - getScreenH(context);
    }


    public static void slideview(final View view, final float p1, final float p2, final float p3, final float p4,
                                 long durationMillis, long delayMillis,
                                 final boolean startVisible, final boolean endVisible) {

        ///如果处在动画阶段则不允许再次运行动画
        if (view.getTag() != null && "-1".equals(view.getTag().toString())) {
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(p1, p2, p3, p4);
        animation.setDuration(durationMillis);
        animation.setStartOffset(delayMillis);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (startVisible) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
                view.setTag("-1");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                int left = view.getLeft() + (int) (p2 - p1);
                int top = view.getTop() + (int) (p4 - p3);
                int width = view.getWidth();
                int height = view.getHeight();
                view.layout(left, top, left + width, top + height);
                //重新设置位置
                if (endVisible) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
                view.setTag("1");

            }
        });
        if (endVisible) {
            view.startAnimation(animation);
        } else {
            //如果关闭则加渐变效果
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setDuration(durationMillis);
            animationSet.setStartOffset(delayMillis);
            animationSet.addAnimation(animation);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            animationSet.addAnimation(alphaAnimation);
            view.startAnimation(animationSet);
        }

    }

    public int getButtonSize() {
        return list.size();
    }

    public boolean isDraftable() {
        for (FloatingActionButton btn : list) {
            if (btn.getVisibility() == View.VISIBLE) {
                return false;
            }
        }
        return true;
    }


    public static void slideButtons(Context context, SuspendActionButton button) {
        int size = button.getButtonSize();
        if (size == 0) {
            return;
        }
        int buttonLeft = button.getLeft();
        int screenWidth = button.getScreenW(context);
        int buttonRight = screenWidth - button.getRight();
        int buttonTop = button.getTop();
        int buttonBottom = button.getScreenH(context) - button.getBottom();
        int buttonWidth = button.getWidth();
        int radius = 7 * buttonWidth / 4;
        int gap = 5 * buttonWidth / 4;
        List<FloatingActionButton> buttons = button.getButtons();

        if (button.isDraftable()) {
            // 可拖拽展开Buttons
            showRotateAnimation(button, 0, 225);
            // 可围成圆形
            if (buttonLeft >= radius && buttonRight >= radius && buttonTop >= radius && buttonBottom >= radius) {
                double angle = 360.0 / size;
                int randomDegree = new Random().nextInt(180);
                for (int i = 0; i < size; i++) {
                    FloatingActionButton faButton = buttons.get(i);
                    slideview(faButton, 0, radius * (float) Math.cos(Math.toRadians(randomDegree + angle * i)), 0, radius * (float) Math.sin(Math.toRadians(randomDegree + angle * i)), 500, 0, true, true);
                }

            } else if (size * gap < screenWidth && (buttonTop > 3 * buttonBottom || buttonBottom > 3 * buttonTop)) {
                // 如果太靠上边或下边
                int leftNumber = buttonLeft / gap;
                int rightNumber = buttonRight / gap;
                if (buttonTop >= radius && buttonBottom >= radius) {
                    if (buttonTop > buttonBottom) {
                        FloatingActionButton faButton = buttons.get(0);
                        slideview(faButton, 0, 0, 0, -radius, 500, 0, true, true);
                        for (int i = 1; i <= leftNumber && i < size; i++) {
                            faButton = buttons.get(i);
                            slideview(faButton, 0, -gap * i, 0, -radius, 500, 0, true, true);
                        }
                        for (int i = 1; i <= rightNumber && i < size - leftNumber; i++) {
                            faButton = buttons.get(i + leftNumber);
                            slideview(faButton, 0, gap * i, 0, -radius, 500, 0, true, true);
                        }
                    } else {
                        FloatingActionButton faButton = buttons.get(0);
                        slideview(faButton, 0, 0, 0, radius, 500, 0, true, true);
                        for (int i = 1; i <= leftNumber && i < size; i++) {
                            faButton = buttons.get(i);
                            slideview(faButton, 0, -gap * i, 0, radius, 500, 0, true, true);
                        }
                        for (int i = 1; i <= rightNumber && i < size - leftNumber; i++) {
                            faButton = buttons.get(i + leftNumber);
                            slideview(faButton, 0, gap * i, 0, radius, 500, 0, true, true);
                        }
                    }
                } else if (buttonTop >= radius) {
                    FloatingActionButton faButton = buttons.get(0);
                    slideview(faButton, 0, 0, 0, -radius, 500, 0, true, true);
                    for (int i = 1; i <= leftNumber && i < size; i++) {
                        faButton = buttons.get(i);
                        slideview(faButton, 0, -gap * i, 0, -radius, 500, 0, true, true);
                    }
                    for (int i = 1; i <= rightNumber && i < size - leftNumber; i++) {
                        faButton = buttons.get(i + leftNumber);
                        slideview(faButton, 0, gap * i, 0, -radius, 500, 0, true, true);
                    }
                } else if (buttonBottom >= radius) {
                    FloatingActionButton faButton = buttons.get(0);
                    slideview(faButton, 0, 0, 0, radius, 500, 0, true, true);
                    for (int i = 1; i <= leftNumber && i < size; i++) {
                        faButton = buttons.get(i);
                        slideview(faButton, 0, -gap * i, 0, radius, 500, 0, true, true);
                    }
                    for (int i = 1; i <= rightNumber && i < size - leftNumber; i++) {
                        faButton = buttons.get(i + leftNumber);
                        slideview(faButton, 0, gap * i, 0, radius, 500, 0, true, true);
                    }
                }
            } else {
                int upNumber = buttonTop / gap;
                int belowNumber = buttonBottom / gap;
                if ((upNumber + belowNumber + 1) >= size) {
                    upNumber = upNumber * (size - 1) / (upNumber + belowNumber);
                    belowNumber = size - 1 - upNumber;
                    if (buttonLeft >= radius) {
                        FloatingActionButton faButton = buttons.get(0);
                        slideview(faButton, 0, -radius, 0, 0, 500, 0, true, true);
                        for (int i = 1; i <= upNumber && i < size; i++) {
                            faButton = buttons.get(i);
                            slideview(faButton, 0, -radius, 0, -gap * i, 500, 0, true, true);
                        }
                        for (int i = 1; i <= belowNumber && i < size - upNumber; i++) {
                            faButton = buttons.get(i + upNumber);
                            slideview(faButton, 0, -radius, 0, gap * i, 500, 0, true, true);
                        }
                    } else if (buttonRight >= radius) {
                        FloatingActionButton faButton = buttons.get(0);
                        slideview(faButton, 0, radius, 0, 0, 500, 0, true, true);
                        for (int i = 1; i <= upNumber && i < size; i++) {
                            faButton = buttons.get(i);
                            slideview(faButton, 0, radius, 0, -gap * i, 500, 0, true, true);
                        }
                        for (int i = 1; i <= belowNumber && i < size - upNumber; i++) {
                            faButton = buttons.get(i + upNumber);
                            slideview(faButton, 0, radius, 0, gap * i, 500, 0, true, true);
                        }
                    }
                }

            }

        } else {
            //  关闭Buttons
            showRotateAnimation(button, 225, 0);
            for (FloatingActionButton faButton : buttons) {
                int faButtonLeft = faButton.getLeft();
                int faButtonTop = faButton.getTop();
                slideview(faButton, 0, buttonLeft - faButtonLeft, 0, buttonTop - faButtonTop, 500, 0, true, false);
            }
        }

    }


    /**
     * 旋转的动画
     *
     * @param mView        需要选择的View
     * @param startDegress 初始的角度【从这个角度开始】
     * @param degrees      当前需要旋转的角度【转到这个角度来】
     */
    public static void showRotateAnimation(View mView, int startDegress, int degrees) {
        mView.clearAnimation();
        float centerX = mView.getWidth() / 2.0f;
        float centerY = mView.getHeight() / 2.0f;
        //这个是设置需要旋转的角度（也是初始化），我设置的是当前需要旋转的角度
        RotateAnimation rotateAnimation = new RotateAnimation(startDegress, degrees, centerX, centerY);  //centerX和centerY是旋转View时候的锚点
        //这个是设置动画时间的
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new AccelerateInterpolator());
        //动画执行完毕后是否停在结束时的角度上
        rotateAnimation.setFillAfter(true);
        //启动动画
        mView.startAnimation(rotateAnimation);
    }


}


