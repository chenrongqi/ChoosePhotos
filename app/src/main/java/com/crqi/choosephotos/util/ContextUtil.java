package com.crqi.choosephotos.util;

import android.app.Application;
import android.content.Context;

/**
 * @Author crqi
 * @Description 全局上下文管理工具
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class ContextUtil {
    public static Context context;
    /**
     * 设置全局应用的上下文
     * @param application
     */
    public static void setContext(Application application){
        context = application;

    }

    /**
     * 获取全局应用的上下文
     * @return
     */
    public static Context getContext(){
        return context;
    }


}
