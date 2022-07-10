package com.crqi.choosephotos.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @Author crqi
 * @Description 轻量级吐司提示工具，TODO 可使用自定义toast兼容解决通知权限关闭问题
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class ToastUtil {

    public static void showToast(CharSequence msg){
        showToast(ContextUtil.getContext(),msg,Toast.LENGTH_SHORT);
    }
    public static void showToast(Context context,CharSequence msg){
        showToast(context,msg,Toast.LENGTH_SHORT);
    }
    public static void showToast(Context context,CharSequence msg,int duration){
        Toast.makeText(context,msg,duration).show();
    }

}
