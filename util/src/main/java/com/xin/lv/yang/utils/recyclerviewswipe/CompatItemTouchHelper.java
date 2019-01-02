
package com.xin.lv.yang.utils.recyclerviewswipe;

import android.support.v7.widget.helper.ItemTouchHelper;


public class CompatItemTouchHelper extends ItemTouchHelper {


    private Callback callback;

    public CompatItemTouchHelper(Callback callback) {
        super(callback);
        this.callback=callback;
    }

    /**
     * Developer callback which controls the behavior of ItemTouchHelper.
     *
     * @return {@link Callback}
     */
    public Callback getCallback() {
        return callback;
    }
}
