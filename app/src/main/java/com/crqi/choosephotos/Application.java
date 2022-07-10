package com.crqi.choosephotos;

import com.crqi.choosephotos.util.ContextUtil;

/**
 * @Author crqi
 * @Description 程序入口
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.setContext(this);
    }
}
