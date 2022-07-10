package com.crqi.choosephotos.util;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author crqi
 * @Description 线程池工具, TODO 可使用kotlin 语法封装
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class ThreadPoolUtil {
    private static Executor appExecutor;
    private static Handler handler;

    /**
     * 获取异步线程池
     *
     * @return
     */
    public static Executor getAppExecutor() {
        if (appExecutor == null) {
            int cpuCount = Runtime.getRuntime().availableProcessors();
            if (cpuCount == 0) {
                cpuCount += 2;
            }

            appExecutor = new ThreadPoolExecutor(cpuCount, cpuCount * 2,
                    3L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(7), new ThreadPoolExecutor.CallerRunsPolicy());
        }
        return appExecutor;
    }

    /**
     * 切换成主线程
     *
     * @param run
     */
    public static void sendMainThread(Runnable run) {
        if (handler == null) {
            handler = new Handler(ContextUtil.getContext().getMainLooper());
        }
        handler.post(run);
    }

}

