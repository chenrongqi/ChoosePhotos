package com.crqi.choosephotos.bean;

/**
 * @Author crqi
 * @Description 抽象图片
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public abstract class Image {
    /**
     * 描述信息
     */
    public abstract String getDes();

    /**
     * 是否选中
     */
    public boolean selected = false;
    /**
     * 列表角标
     */
    public int position=0;

}
