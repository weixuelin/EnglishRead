package com.xin.lv.yang.utils.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

public class CustomSwipeRefreshView extends SwipeRefreshLayout {
    Context context;
    RecyclerView recyclerView;
    private boolean isLoading = false;
    private OnLoadListener mOnLoadListener;

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public CustomSwipeRefreshView(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public CustomSwipeRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initView() {
        setColorSchemeColors(context.getResources().getColor(android.R.color.holo_red_light),
                context.getResources().getColor(android.R.color.holo_green_light),
                context.getResources().getColor(android.R.color.holo_blue_bright),
                context.getResources().getColor(android.R.color.holo_orange_light)
        );

    }

    public void onLoadMore(RecyclerView recyclerView, OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
        recyclerView.addOnScrollListener(listener);
    }


    public void onLoadByNestScrollView(NestedScrollView nestedScrollView, OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onScrollChange(NestedScrollView view, int i, int i1, int i2, int i3) {
                ///  滑动到最底部
                if (!view.canScrollVertically(1)) {
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onLoad();
                    }
                }

            }
        });
    }


    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING) {
                isLoading = true;
            } else {
                isLoading = false;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int countItem = layoutManager.getItemCount();
                int lastItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();

                if (isLoading && countItem != lastItem && lastItem == countItem - 1) {
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onLoad();
                    }
                }
            }
        }
    };


    /**
     * 上拉加载的接口回调
     */

    public interface OnLoadListener {
        void onLoad();
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mOnLoadListener = listener;
    }
}
