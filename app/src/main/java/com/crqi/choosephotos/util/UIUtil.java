package com.crqi.choosephotos.util;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;

/**
 * @Author crqi
 * @Description ui 工具
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class UIUtil {
    /**
     * 此获取屏幕真实宽度
     *
     * @return width
     */
    public static int getScreenWidth() {
        return ContextUtil.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 转化像素
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}
