package com.crqi.choosephotos.util;

import android.os.Handler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author crqi
 * @Description 线程池工具,TODO 可使用kotlin 语法封装
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class ThreadPoolUtil {
    private static Executor appExecutor;
    private static Handler handler;

    /**
     * 获取异步线程池
     * @return
     */
    public static Executor getAppExecutor(){
        if(appExecutor==null){
            int cpuCount = Runtime.getRuntime().availableProcessors();
            if(cpuCount==0){
                cpuCount+=2;
            }

            appExecutor =Executors.newFixedThreadPool(cpuCount);
        }
        return appExecutor;
    }

    /**
     * 切换成主线程
     * @param run
     */
    public static void sendMainThread(Runnable run){
        if(handler==null){
            handler = new Handler(ContextUtil.getContext().getMainLooper());
        }
        handler.post(run);
    }

}

