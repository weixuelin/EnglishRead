package com.xin.lv.yang.utils.pull;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;


public class PullToZoomRecyclerViewEx extends PullToZoomBase<RecyclerView> implements AbsListView.OnScrollListener {
    private static final String TAG = PullToZoomRecyclerViewEx.class.getSimpleName();
    private FrameLayout mHeaderContainer;
    private FrameLayout footContainer;
    private int mHeaderHeight;
    private int mFootH;
    private ScalingRunnable mScalingRunnable;

    public static int LINEARLAYOUT_MANAGER = 1;
    public static int GRID_MANAGER = 2;

    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float paramAnonymousFloat) {
            float f = paramAnonymousFloat - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };


    public PullToZoomRecyclerViewEx(Context context) {
        this(context, null);
    }

    public PullToZoomRecyclerViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRootView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mZoomView != null && !isHideHeader() && isPullToZoomEnabled()) {
                    float f = mHeaderHeight - mHeaderContainer.getBottom();

                    if (isParallax()) {
                        if ((f > 0.0F) && (f < mHeaderHeight)) {
                            int i = (int) (0.65D * f);
                            mHeaderContainer.scrollTo(0, -i);
                        } else if (mHeaderContainer.getScrollY() != 0) {
                            mHeaderContainer.scrollTo(0, 0);
                        }
                    }
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    /// 新加的
                    if (onRefreshLoading != null) {
                        if (type == LINEARLAYOUT_MANAGER) {
                            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            int firstVisible = manager.findFirstVisibleItemPosition();
                            if (firstVisible == 0) {
                                View view = manager.findViewByPosition(firstVisible);
                                if (view != null && view.getTop() == 0) {
                                    onRefreshLoading.onRefresh(true);
                                } else {
                                    onRefreshLoading.onRefresh(false);
                                }
                            } else {
                                //屏幕中最后一个可见子项的position
                                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                                //当前屏幕所看到的子项个数
                                int visibleItemCount = manager.getChildCount();
                                //当前RecyclerView的所有子项个数
                                int totalItemCount = manager.getItemCount();
                                //RecyclerView的滑动状态
                                int state = recyclerView.getScrollState();
                                View lastVisibleItemView = manager.findViewByPosition(lastVisibleItemPosition);
                                if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == recyclerView.getHeight()) {
                                    onRefreshLoading.onLoad(true);
                                } else {
                                    onRefreshLoading.onLoad(false);
                                }
                            }
                        } else if (type == GRID_MANAGER) {
                            GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                            int firstVisible = manager.findFirstVisibleItemPosition();
                            if (firstVisible == 0) {
                                View view = manager.findViewByPosition(firstVisible);
                                if (view != null && view.getTop() == 0) {
                                    onRefreshLoading.onRefresh(true);
                                } else {
                                    onRefreshLoading.onRefresh(false);
                                }
                            } else {
                                //屏幕中最后一个可见子项的position
                                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                                //当前屏幕所看到的子项个数
                                int visibleItemCount = manager.getChildCount();
                                //当前RecyclerView的所有子项个数
                                int totalItemCount = manager.getItemCount();
                                //RecyclerView的滑动状态
                                int state = recyclerView.getScrollState();
                                View lastVisibleItemView = manager.findViewByPosition(lastVisibleItemPosition);
                                if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == recyclerView.getHeight()) {
                                    onRefreshLoading.onLoad(true);
                                } else {
                                    onRefreshLoading.onLoad(false);
                                }
                            }
                        }
                    }
                }
            }

        });
        mScalingRunnable = new ScalingRunnable();
    }


    private OnRefreshLoading onRefreshLoading;

    /**
     * 设置刷新和加载
     *
     * @param onRefreshLoading
     */
    public void setOnRefreshLoading(OnRefreshLoading onRefreshLoading) {
        this.onRefreshLoading = onRefreshLoading;
    }


    public interface OnRefreshLoading {
        void onRefresh(boolean isRefresh);

        void onLoad(boolean isLoad);
    }


    /**
     * 是否显示headerView
     *
     * @param isHideHeader true: show false: hide
     */
    @Override
    public void setHideHeader(boolean isHideHeader) {
        if (isHideHeader != isHideHeader()) {
            super.setHideHeader(isHideHeader);
            if (isHideHeader) {
                removeHeaderView();
            } else {
                updateHeaderView();
            }
        }
    }

    @Override
    public void setHeaderView(View headerView) {
        if (headerView != null) {
            this.mHeaderView = headerView;
            updateHeaderView();
        }
    }


    public void setFootView(View footView) {
        if (footView != null) {
            this.mFootView = footView;
            updateFootView();
        }
    }


    public void setFootVisibile(boolean b) {
        if (b) {
            mFootView.setVisibility(VISIBLE);
        } else {
            mFootView.setVisibility(GONE);
        }

    }


    @Override
    public void setZoomView(View zoomView) {
        if (zoomView != null) {
            this.mZoomView = zoomView;
            updateHeaderView();
        }
    }

    /**
     * 移除HeaderView
     * 如果要兼容API 9,需要修改此处逻辑，API 11以下不支持动态添加header
     */
    private void removeHeaderView() {
        if (mHeaderContainer != null) {
            if (mRootView != null && mRootView.getAdapter() != null) {

                BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder> mAdapter = (BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder>) mRootView.getAdapter();

                if (mAdapter != null) {

                    if (mAdapter.getHeader(0) != null)
                        mAdapter.removeHeaderView(mAdapter.getHeader(0));
                }

            }
        }
    }


    public void removeFootView() {
        if (footContainer != null) {
            if (mRootView != null && mRootView.getAdapter() != null) {
                BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder> mAdapter = (BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder>) mRootView.getAdapter();

                if (mAdapter != null) {

                    if (mAdapter.getFoot(0) != null)
                        mAdapter.removeFooterView(mAdapter.getFoot(0));
                }

            }
        }
    }

    /**
     * 更新HeaderView  先移除-->再添加zoomView、HeaderView -->然后添加到RecyclerView 的head
     * 如果要兼容API 9,需要修改此处逻辑，API 11以下不支持动态添加header
     */

    private void updateHeaderView() {
        if (mHeaderContainer != null) {

            if (mRootView != null && mRootView.getAdapter() != null) {

                BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder> mAdapter = (BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder>) mRootView.getAdapter();

                if (mAdapter != null) {

                    if (mAdapter.getHeader(0) != null)
                        mAdapter.removeHeaderView(mAdapter.getHeader(0));

                    mHeaderContainer.removeAllViews();

                    if (mZoomView != null) {
                        mHeaderContainer.addView(mZoomView);
                    }

                    if (mHeaderView != null) {
                        mHeaderContainer.addView(mHeaderView);
                    }

                    mHeaderHeight = mHeaderContainer.getHeight();

                    BaseRecyclerViewHeaderAdapter.ExtraItem mExtraItem = new BaseRecyclerViewHeaderAdapter.ExtraItem(
                            BaseRecyclerViewHeaderAdapter.INT_TYPE_HEADER,
                            new RecyclerView.ViewHolder(mHeaderContainer) {
                                @Override
                                public String toString() {
                                    return super.toString();
                                }
                            });

                    mAdapter.addHeaderView(mExtraItem);
                }
            }
        }
    }



    public void updateFootView() {

        Log.i("wwwww", "===footview===" + mFootView);

        if (footContainer != null) {
            if (mRootView != null && mRootView.getAdapter() != null) {
                BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder> mAdapter =
                        (BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder>) mRootView.getAdapter();

                if (mAdapter != null) {
                    mAdapter.removeFooterView(mAdapter.getFoot(0));
                    footContainer.removeAllViews();
                    if (mFootView != null) {
                        footContainer.addView(mFootView);
                    }

                    mFootH = footContainer.getHeight();

                    BaseRecyclerViewHeaderAdapter.ExtraItem mExtraItem = new BaseRecyclerViewHeaderAdapter.ExtraItem(
                            BaseRecyclerViewHeaderAdapter.INT_TYPE_FOOTER, new RecyclerView.ViewHolder(footContainer) {
                        @Override
                        public String toString() {
                            return super.toString();
                        }
                    });

                    mAdapter.addFooterView(mExtraItem);

                }
            }
        }


    }


    int type;

    public void setAdapterAndLayoutManager(RecyclerView.Adapter adapter, RecyclerView.LayoutManager mLayoutManager, int type) {
        this.type = type;
        mRootView.setLayoutManager(mLayoutManager);
        mRootView.setAdapter(adapter);
        updateHeaderView();

        updateFootView();

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {

    }

    /**
     * 创建listView 如果要兼容API9,需要修改此处
     *
     * @param context 上下文
     * @param attrs   AttributeSet
     * @return RecyclerView
     */
    @Override
    protected RecyclerView createRootView(Context context, AttributeSet attrs) {
        RecyclerView rv = new RecyclerView(context, attrs);

        return rv;
    }

    /**
     * 重置动画，自动滑动到顶部
     */
    @Override
    protected void smoothScrollToTop() {
        Log.d(TAG, "smoothScrollToTop --> ");
        mScalingRunnable.startAnimation(200L);
    }

    /**
     * zoomView动画逻辑
     *
     * @param newScrollValue 手指Y轴移动距离值
     */
    @Override
    protected void pullHeaderToZoom(int newScrollValue) {
        Log.d(TAG, "pullHeaderToZoom --> newScrollValue = " + newScrollValue);
        Log.d(TAG, "pullHeaderToZoom --> mHeaderHeight = " + mHeaderHeight);
        if (mScalingRunnable != null && !mScalingRunnable.isFinished()) {
            mScalingRunnable.abortAnimation();
        }
        ViewGroup.LayoutParams localLayoutParams = mHeaderContainer.getLayoutParams();
        localLayoutParams.height = Math.abs(newScrollValue) + mHeaderHeight;
        mHeaderContainer.setLayoutParams(localLayoutParams);
    }


    @Override
    protected boolean isReadyForPullStart() {
        return isFirstItemVisible();
    }


    private boolean isFirstItemVisible() {
        if (mRootView != null) {
            final RecyclerView.Adapter adapter = mRootView.getAdapter();
            if (type == LINEARLAYOUT_MANAGER) {
                LinearLayoutManager mLayoutmanager = (LinearLayoutManager) mRootView.getLayoutManager();
                if (null == adapter || adapter.getItemCount() == 0) {
                    return true;
                } else {
                    int[] into = {0, 0};
                    if (mLayoutmanager != null)
                        into[0] = mLayoutmanager.findFirstVisibleItemPosition();
                    if (into.length > 0 && into.length > 0 && into[0] <= 1) {
                        final View firstVisibleChild = mRootView.getChildAt(0);
                        if (firstVisibleChild != null) {
                            return firstVisibleChild.getTop() >= mRootView.getTop();
                        }
                    }
                }
            } else if (type == GRID_MANAGER) {
                GridLayoutManager mLayoutmanager = (GridLayoutManager) mRootView.getLayoutManager();
                if (null == adapter || adapter.getItemCount() == 0) {
                    return true;
                } else {
                    int[] into = {0, 0};
                    if (mLayoutmanager != null)
                        into[0] = mLayoutmanager.findFirstVisibleItemPosition();
                    if (into.length > 0 && into.length > 0 && into[0] <= 1) {
                        final View firstVisibleChild = mRootView.getChildAt(0);
                        if (firstVisibleChild != null) {
                            return firstVisibleChild.getTop() >= mRootView.getTop();
                        }
                    }
                }
            }

        }

        return false;
    }


    @Override
    public void handleStyledAttributes(TypedArray a) {
        mHeaderContainer = new FrameLayout(getContext());
        footContainer = new FrameLayout(getContext());
        if (mFootView != null) {
            footContainer.addView(mFootView);
        }

        if (mZoomView != null) {
            mHeaderContainer.addView(mZoomView);
        }
        if (mHeaderView != null) {
            mHeaderContainer.addView(mHeaderView);
        }

        BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder> mAdapter = (BaseRecyclerViewHeaderAdapter<RecyclerView.ViewHolder>) mRootView.getAdapter();
        if (mAdapter != null) {
            BaseRecyclerViewHeaderAdapter.ExtraItem mExtraItem = new BaseRecyclerViewHeaderAdapter.ExtraItem(BaseRecyclerViewHeaderAdapter.INT_TYPE_HEADER, new RecyclerView.ViewHolder(mHeaderContainer) {
                @Override
                public String toString() {
                    return super.toString();
                }
            });

            mAdapter.addHeaderView(mExtraItem);

            BaseRecyclerViewHeaderAdapter.ExtraItem footItem = new BaseRecyclerViewHeaderAdapter.ExtraItem(BaseRecyclerViewHeaderAdapter.INT_TYPE_FOOTER, new RecyclerView.ViewHolder(footContainer) {
                @Override
                public String toString() {
                    return super.toString();
                }
            });

            mAdapter.addFooterView(footItem);
        }

    }

    /**
     * 设置HeaderView高度
     *
     * @param width  宽
     * @param height 高
     */
    public void setHeaderViewSize(int width, int height) {
        if (mHeaderContainer != null) {
            Object localObject = mHeaderContainer.getLayoutParams();
            if (localObject == null) {
                localObject = new AbsListView.LayoutParams(width, height);
            }
            ((ViewGroup.LayoutParams) localObject).width = width;
            ((ViewGroup.LayoutParams) localObject).height = height;
            mHeaderContainer.setLayoutParams((ViewGroup.LayoutParams) localObject);
            mHeaderHeight = height;
        }
    }

    public void setHeaderLayoutParams(AbsListView.LayoutParams layoutParams) {
        if (mHeaderContainer != null) {
            mHeaderContainer.setLayoutParams(layoutParams);
            mHeaderHeight = layoutParams.height;
        }
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
                            int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        Log.d(TAG, "onLayout --> ");
        if (mHeaderHeight == 0 && mHeaderContainer != null) {
            mHeaderHeight = mHeaderContainer.getHeight();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(TAG, "onScrollStateChanged --> ");
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mZoomView != null && !isHideHeader() && isPullToZoomEnabled()) {
            float f = mHeaderHeight - mHeaderContainer.getBottom();
            Log.d(TAG, "onScroll --> f = " + f);
            if (isParallax()) {
                if ((f > 0.0F) && (f < mHeaderHeight)) {
                    int i = (int) (0.65D * f);
                    mHeaderContainer.scrollTo(0, -i);
                } else if (mHeaderContainer.getScrollY() != 0) {
                    mHeaderContainer.scrollTo(0, 0);
                }
            }
        }
    }


    class ScalingRunnable implements Runnable {
        protected long mDuration;
        protected boolean mIsFinished = true;
        protected float mScale;
        protected long mStartTime;

        ScalingRunnable() {
        }

        public void abortAnimation() {
            mIsFinished = true;
        }

        public boolean isFinished() {
            return mIsFinished;
        }

        public void run() {
            if (mZoomView != null) {
                float f2;
                ViewGroup.LayoutParams localLayoutParams;
                if ((!mIsFinished) && (mScale > 1.0D)) {
                    float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime) / (float) mDuration;
                    f2 = mScale - (mScale - 1.0F) * PullToZoomRecyclerViewEx.sInterpolator.getInterpolation(f1);
                    localLayoutParams = mHeaderContainer.getLayoutParams();
                    Log.d(TAG, "ScalingRunnable --> f2 = " + f2);
                    if (f2 > 1.0F) {
                        localLayoutParams.height = ((int) (f2 * mHeaderHeight));
                        mHeaderContainer.setLayoutParams(localLayoutParams);
                        post(this);
                        return;
                    }
                    mIsFinished = true;
                }
            }
        }

        public void startAnimation(long paramLong) {
            if (mZoomView != null) {
                mStartTime = SystemClock.currentThreadTimeMillis();
                mDuration = paramLong;
                mScale = ((float) (mHeaderContainer.getBottom()) / mHeaderHeight);
                mIsFinished = false;
                post(this);
            }
        }
    }
}
