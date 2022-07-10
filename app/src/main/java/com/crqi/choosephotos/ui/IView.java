package com.crqi.choosephotos.ui;

/**
 * @Author crqi
 * @Description 页面控制逻辑
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public interface  IView {
    /**
     * 加载成功
     */
    void success();

    /**
     * 加载失败
     */
    void err();

    /**
     * 全部刷新
     */
    void refreshAll();

    /**
     * 局部刷新
     * @param start
     * @param count
     */
    void refreshRange(int start,int count);

    /**
     * 单个刷新
     * @param position
     */
    void refreshItem(int position);
}
