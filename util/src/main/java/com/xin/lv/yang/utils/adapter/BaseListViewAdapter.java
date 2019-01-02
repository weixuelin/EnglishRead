package com.xin.lv.yang.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * 加载一个item的adapter 使用ListView
 *
 * @param <T>
 */
public abstract class BaseListViewAdapter<T> extends BaseAdapter {
    public Context context;
    public List<T> list;
    private LayoutInflater inflater;

    public BaseListViewAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}
