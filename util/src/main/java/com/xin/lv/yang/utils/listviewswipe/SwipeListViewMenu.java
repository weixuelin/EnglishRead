package com.xin.lv.yang.utils.listviewswipe;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public class SwipeListViewMenu {

	private Context mContext;
	private List<SwipeItem> mItems;
	private int mViewType;

	public SwipeListViewMenu(Context context) {
		mContext = context;
		mItems = new ArrayList<SwipeItem>();
	}

	public Context getContext() {
		return mContext;
	}

	public void addMenuItem(SwipeItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(SwipeItem item) {
		mItems.remove(item);
	}

	public List<SwipeItem> getMenuItems() {
		return mItems;
	}

	public SwipeItem getMenuItem(int index) {
		return mItems.get(index);
	}

	public int getViewType() {
		return mViewType;
	}

	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}

}
