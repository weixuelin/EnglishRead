package com.xin.lv.yang.utils.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;


public class BaseViewHolder extends RecyclerView.ViewHolder {

    /**
     *   view的id + view视图
     */
    private SparseArray<View> arr;

    public BaseViewHolder(View itemView) {
        super(itemView);
        arr = new SparseArray<>();
    }


    public View getViewById(int resId) {
        View view = arr.get(resId);
        if (view == null) {
            view = itemView.findViewById(resId);
            arr.append(resId, view);
        }
        return view;
    }
}
