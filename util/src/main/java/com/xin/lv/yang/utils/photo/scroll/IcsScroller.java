package com.xin.lv.yang.utils.photo.scroll;

import android.annotation.TargetApi;
import android.content.Context;

/**
 * Created by Administrator on 2017/5/15.
 */
@TargetApi(14)
public class IcsScroller extends GingerScroller {
    public IcsScroller(Context context) {
        super(context);
    }

    public boolean computeScrollOffset() {
        return this.mScroller.computeScrollOffset();
    }
}
