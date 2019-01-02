package com.xin.lv.yang.utils.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xin.lv.yang.utils.utils.BitmapUtil;
import com.xin.lv.yang.utils.utils.DataUtil;

import java.text.DecimalFormat;
import java.util.List;


/**
 * 使用 ButterKnife 绑定控件
 *
 * @param <T>
 */
public abstract class OnBindRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<T> list;
    public Context context;
    public LayoutInflater inflater;

    public OnBindRecyclerAdapter(Context context, List<T> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (list != null) {
            this.list = list;
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {

            onBindViewHolder(holder, position);

        } else {
            updateViewHolder(holder, position, payloads);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        showViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh = createRecyclerViewHolder(parent, viewType);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(vh.itemView, vh.getAdapterPosition());
                }
            }
        });

        vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(vh.itemView, vh.getAdapterPosition());
                }
                return false;
            }
        });

        return vh;

    }


    /**
     * 显示数据
     *
     * @param holder   viewHolder 对象
     * @param position list集合的位置
     */
    protected abstract void showViewHolder(RecyclerView.ViewHolder holder, int position);


    /**
     * 刷新数据
     *
     * @param holder   viewholder 对象
     * @param position list集合中的位置
     * @param payloads 待刷新的数据
     */
    protected abstract void updateViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads);

    /**
     * 创建 ViewHolder
     *
     * @param parent   viewGroup
     * @param viewType viewType
     * @return viewHolder 对象
     */
    protected abstract RecyclerView.ViewHolder createRecyclerViewHolder(ViewGroup parent, int viewType);


    /**
     * 刷新数据 使用异步任务
     *
     * @param newList  新集合
     * @param callBack 回调
     */
    public void update(final List<T> newList, final AdapterCallBack<T> callBack) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, DiffUtil.DiffResult> asyncTask = new AsyncTask<Object, Void, DiffUtil.DiffResult>() {
            @Override
            protected DiffUtil.DiffResult doInBackground(Object... objects) {
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(callBack, true);
                return result;
            }

            @Override
            protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                super.onPostExecute(diffResult);
                diffResult.dispatchUpdatesTo(OnBindRecyclerAdapter.this);
                setData(newList);       //  设置新的数据

            }
        };

        asyncTask.execute();

    }

    /**
     * 不使用异步任务
     *
     * @param newList  新数据集合
     * @param callBack 回调
     */
    public void updateNoAsync(List<T> newList, AdapterCallBack<T> callBack) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callBack, false);
        result.dispatchUpdatesTo(this);
        setData(newList);

    }


    public int getW() {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        return width;
    }


    public int getH() {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;   // 屏幕高度（像素）
        return height;
    }


    /**
     * 保留两位小数
     *
     * @param s
     * @return
     */
    public String floatToString(float s) {
        DecimalFormat myFormat = new DecimalFormat("0.00");
        String strFloat = myFormat.format(s);
        return strFloat;
    }



    public String stringToString(String ss, String type) {
        return DataUtil.longToTime(Long.parseLong(ss), type);

    }


    /**
     * 将新数据设置到原来的集合中
     *
     * @param newList 新数据
     */
    public void setData(List<T> newList) {
        list.clear();
        list.addAll(newList);
    }


    public void updateData(T u, int p) {
        int len = list.size();
        for (int i = 0; i < len; i++) {
            if (i == p) {
                list.remove(i);   //移除原有的
                list.add(p, u);   //添加新的数据
            }
        }
        notifyDataSetChanged();

    }
}
