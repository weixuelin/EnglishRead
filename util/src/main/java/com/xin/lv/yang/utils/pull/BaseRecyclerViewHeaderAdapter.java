package com.xin.lv.yang.utils.pull;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xin.lv.yang.utils.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewHeaderAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    public static final int INT_TYPE_HEADER = 1;
    public static final int INT_TYPE_FOOTER = 2;


    public static class ExtraItem<V extends RecyclerView.ViewHolder> {
        public final int type;
        public final V view;

        public ExtraItem(int type, V view) {
            this.type = type;
            this.view = view;
        }

    }

    private View emptyView;

    private final Context context;
    private final List<ExtraItem> headers;
    private final List<ExtraItem> footers;

    public BaseRecyclerViewHeaderAdapter(Context context) {
        this.context = context;
        this.headers = new ArrayList<ExtraItem>();
        this.footers = new ArrayList<ExtraItem>();
    }

    public Context getContext() {
        return context;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        emptyView.setVisibility(getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取item的长度
     *
     * @return item的个数
     */
    public abstract int getCount();

    @Override
    public final int getItemCount() {
        int count = headers.size();
        count += getCount();
        count += footers.size();

        if (emptyView != null)
            emptyView.setVisibility(getCount() == 0 ? View.VISIBLE : View.GONE);

        return count;
    }

    public ExtraItem addHeaderView(int type, V headerView) {
        ExtraItem item = new ExtraItem(type, headerView);
        addHeaderView(item);
        return item;
    }

    public void addHeaderView(ExtraItem headerView) {
        headers.add(headerView);
        notifyItemInserted(headers.size());
    }


    public void removeHeaderView(int type) {
        List<ExtraItem> indexesToRemove = new ArrayList<ExtraItem>();
        for (int i = 0; i < headers.size(); i++) {
            ExtraItem item = headers.get(i);
            if (item.type == type)
                indexesToRemove.add(item);
        }

        for (ExtraItem item : indexesToRemove) {
            int index = headers.indexOf(item);
            headers.remove(item);
            notifyItemRemoved(index);
        }
    }

    public void removeHeaderView(ExtraItem headerView) {
        int indexToRemove = headers.indexOf(headerView);
        if (indexToRemove >= 0) {
            headers.remove(indexToRemove);
            notifyItemRemoved(indexToRemove);
        }
    }

    public ExtraItem addFooterView(int type, V footerView) {
        ExtraItem item = new ExtraItem(type, footerView);
        addFooterView(item);
        return item;
    }

    public void addFooterView(ExtraItem footerView) {
        footers.add(footerView);
        notifyItemInserted(getItemCount());
    }

    public void removeFooterView(int type) {
        List<ExtraItem> indexesToRemove = new ArrayList<ExtraItem>();
        for (int i = 0; i < footers.size(); i++) {
            ExtraItem item = footers.get(i);
            if (item.type == type)
                indexesToRemove.add(item);
        }

        for (ExtraItem item : indexesToRemove) {
            int index = footers.indexOf(item);
            footers.remove(item);
            notifyItemRemoved(headers.size() + getCount() + index);
        }
    }

    public void removeFooterView(ExtraItem footerView) {
        int indexToRemove = footers.indexOf(footerView);
        if (indexToRemove >= 0) {
            footers.remove(indexToRemove);
            notifyItemRemoved(headers.size() + getCount() + indexToRemove);
        }
    }

    public int getViewType(int position) {
        return super.getItemViewType(position);
    }


    public ExtraItem getHeader(int mIntArgHeaderPos) {

        if (headers != null && headers.size() > mIntArgHeaderPos) {
            return headers.get(mIntArgHeaderPos);
        }
        return null;
    }


    public ExtraItem getFoot(int i) {
        if (footers.size() != 0) {
            return footers.get(i);
        }
        return null;

    }


    @Override
    public final int getItemViewType(int position) {
        Log.i("wwwwww", "viewType==p==" + position);
        if (isHeaderPosition(position)) {
            return INT_TYPE_HEADER;

        } else if (isFooterPosition(position)) {
            return INT_TYPE_FOOTER;

        } else {
            return getViewType(position - headers.size());
        }

    }


    private boolean isHeaderPosition(int position) {
        return position < headers.size();
    }

    private boolean isFooterPosition(int position) {
        return position >= headers.size() + getCount();
    }


    @Override
    public final V onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("wwwwww", "====viewType====" + viewType);

        for (ExtraItem<V> item : headers) {
            if (viewType == item.type) {
                return item.view;
            }
        }

        for (ExtraItem<V> item : footers) {
            if (viewType == item.type) {
                Log.i("wwwwwww","==view==="+item.view);
                return item.view;
            }
        }

        return onCreateContentView(parent, viewType);

    }

    /**
     * 创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract V onCreateContentView(ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
            try {
                final StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                lp.setFullSpan(true);
                holder.itemView.setLayoutParams(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (isFooterPosition(position)) {

            View view = holder.itemView.findViewById(R.id.loading);
            Log.i("wwwww", "添加尾部===" + position + "===view===" + view);
        } else {

            onBindView((V) holder, position - headers.size());

        }

    }


    /**
     * 绑定视图控件
     *
     * @param view     视图
     * @param position 位置
     */
    public abstract void onBindView(V view, int position);

}