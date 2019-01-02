package com.xin.lv.yang.utils.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xin.lv.yang.utils.R;

/**
 * 自定义toolbar
 */

public class CustomToolBar extends Toolbar {

    private TextView toolbar_title;
    private EditText toolbar_searchview;
    private ImageView toolbar_leftButton;
    private ImageView toolbar_rightButton;
    private View mChildView;
    private boolean showSearchView;
    private Drawable left_button_icon;
    private Drawable right_button_icon;
    private String title;

    public CustomToolBar(Context context) {
        this(context, null, 0);

    }


    public CustomToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //通过代码得到布局文件当中一些属性的值
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.custom_toolbar, defStyleAttr, 0);

        showSearchView = a.getBoolean(R.styleable.custom_toolbar_showSearchView, false);
        left_button_icon = a.getDrawable(R.styleable.custom_toolbar_leftButtonIcon);
        right_button_icon = a.getDrawable(R.styleable.custom_toolbar_rightButtonIcon);
        title = a.getString(R.styleable.custom_toolbar_myTitle);
        a.recycle();
        //初始界面
        initView();
        //初始监听器
        initListener();

    }


    /**
     * 初始化布局
     */
    private void initView() {
        if (mChildView == null) {
            mChildView = View.inflate(getContext(), R.layout.toolbar_layout, null);
            toolbar_title = mChildView.findViewById(R.id.toolbar_title);
            toolbar_searchview = mChildView.findViewById(R.id.toolbar_searchview);
            toolbar_leftButton = mChildView.findViewById(R.id.toolbar_leftButton);
            toolbar_rightButton = mChildView.findViewById(R.id.toolbar_rightButton);

            //添加自定义的布局到Toolbar
            addView(mChildView);

            //设置标题、搜索框、左右按钮是否显示，并且设置按钮的图标
            if (showSearchView) {
                showSearchview();
                hideTitle();
            } else {
                hideSearchview();
                showTitle();
                if (title != null) {
                    toolbar_title.setText(title);
                }
            }

            if (left_button_icon != null) {
                toolbar_leftButton.setImageDrawable(left_button_icon);
            }

            if (right_button_icon != null) {
                toolbar_rightButton.setImageDrawable(right_button_icon);
            }
        }

    }

    public void setLeftButton(int id) {
        left_button_icon = getResources().getDrawable(id);
        toolbar_leftButton.setVisibility(VISIBLE);
    }

    public void setRightButton(int id) {
        right_button_icon = getResources().getDrawable(id);
        toolbar_rightButton.setVisibility(VISIBLE);
    }

    /**
     * 重写设置标题的方法
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        toolbar_title.setText(title);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        toolbar_title.setText(resId);
    }

    /**
     * 设置左右按钮的图标
     *
     * @param d
     */
    public void setLeftButtonIconDrawable(Drawable d) {
        toolbar_leftButton.setImageDrawable(d);
    }

    public void setRightButtonIconDrawable(Drawable d) {
        toolbar_rightButton.setImageDrawable(d);
    }

    /**
     * 标题与搜索框的切换
     */
    public void setShowSearchView(int background, int logo,String hint) {
        hideTitle();
        showSearchview();
        if(background!=0){
            toolbar_searchview.setBackgroundResource(background);
        }
        if(logo!=0){
            toolbar_searchview.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(logo), null, null, null);
        }

        if(hint.equals("")){
            toolbar_searchview.setHint(hint);
        }


    }

    public void setShowTitleView(String title) {
        hideSearchview();
        showTitle();
        toolbar_title.setText(title);
    }

    /**
     * 左右按钮的监听
     */
    private void initListener() {
        toolbar_leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftButtonClickListener != null) {
                    onLeftButtonClickListener.onClick();
                }
            }
        });

        toolbar_rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightButtonClickListener != null) {
                    onRightButtonClickListener.onClick();
                }
            }
        });
    }

    public interface OnLeftButtonClickListener {
        void onClick();
    }

    public interface OnRightButtonClickListener {
        void onClick();

    }

    private OnLeftButtonClickListener onLeftButtonClickListener;
    private OnRightButtonClickListener onRightButtonClickListener;

    public void setOnLeftButtonClickListener(OnLeftButtonClickListener listener) {
        onLeftButtonClickListener = listener;
    }

    public void setOnRightButtonClickListener(OnRightButtonClickListener listener) {
        onRightButtonClickListener = listener;
    }

    /**
     * 设置标题或者搜索框是否显示
     */
    private void showTitle() {
        toolbar_title.setVisibility(View.VISIBLE);
    }

    private void hideTitle() {
        toolbar_title.setVisibility(View.GONE);
    }

    private void showSearchview() {
        toolbar_searchview.setVisibility(View.VISIBLE);

    }


    private void hideSearchview() {
        toolbar_searchview.setVisibility(View.GONE);
    }


}
