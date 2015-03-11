package com.qieji.mobile.common;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by August on 2015/3/11.
 */
public class util {

    /**
     * dp转px
     *
     * @param dp
     * @param context
     * @return
     */
    public static int dp2px(int dp, Context context) {
        int px = -1;
        if (context == null) {
            return  px;
        }
        px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                        .getDisplayMetrics());
        return px;
    }

}
