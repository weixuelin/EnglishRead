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

import java.text.DecimalFormat;
import java.util.List;


/**
 * 加载一个item的adapter,  数据集合使用泛型。
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    public List<T> list;
    public Context context;
    private LayoutInflater inflater;

    /**
     *  视图 id
     */
    private int resId;
    private OnRecyclerViewItemListener itemListener;
    private OnRecyclerViewItemLongListener itemLongListener;

    public OnRecyclerViewItemListener getItemListener() {
        return itemListener;
    }

    public void setItemListener(OnRecyclerViewItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public OnRecyclerViewItemLongListener getItemLongListener() {
        return itemLongListener;
    }

    public void setItemLongListener(OnRecyclerViewItemLongListener itemLongListener) {
        this.itemLongListener = itemLongListener;
    }

    public BaseRecyclerViewAdapter(Context context, List<T> list, int resId) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.resId = resId;
    }


    /**
     * 构造View
     *
     * @param holder   ViewHolder对象
     * @param position 显示的位置
     */
    public abstract void getViewHolder(BaseViewHolder holder, int position);


    /**
     * 刷新View
     *
     * @param holder   ViewHolder 对象
     * @param position 刷新的位置
     * @param payloads 改变的数据
     */

    public abstract void getViewHolder(BaseViewHolder holder, int position, List<Object> payloads);


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final BaseViewHolder holder = new BaseViewHolder(inflater.inflate(resId, parent, false));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    itemListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemLongListener != null) {
                    itemLongListener.onItemLongClick(holder.itemView, holder.getAdapterPosition());
                }
                return false;
            }
        });

        return holder;

    }



    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        getViewHolder(holder, position);
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            /// 完整构造一次View
            onBindViewHolder(holder, position);

        } else {
            /// 刷新view
            getViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
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


    public void updateInfo(List<T> dtList) {
        list.clear();
        list.addAll(dtList);
        notifyDataSetChanged();
    }



    public void updateInfoNoClear(List<T> arr) {
        list.addAll(arr);
        notifyDataSetChanged();

    }


    /**
     * 点击事件
     */
    public interface OnRecyclerViewItemListener {
        void onItemClick(View view, int p);

    }


    /**
     * 长按事件
     */
    public interface OnRecyclerViewItemLongListener {
        void onItemLongClick(View view, int p);
    }


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
                diffResult.dispatchUpdatesTo(BaseRecyclerViewAdapter.this);
                setData(newList);       //  设置新的数据

            }
        };

        asyncTask.execute();

    }

    /**
     * 不使用异步任务
     * @param newList  新数据集合
     * @param callBack  回调
     */
    public void updateNoAsync(List<T> newList,AdapterCallBack<T> callBack){
        DiffUtil.DiffResult result=DiffUtil.calculateDiff(callBack,false);
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


    public String floatToString(float s) {
        DecimalFormat myFormat = new DecimalFormat("0.00");
        String strFloat = myFormat.format(s);
        return strFloat;
    }

}
