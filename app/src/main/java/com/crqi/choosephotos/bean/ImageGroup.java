package com.crqi.choosephotos.bean;

import java.util.List;

/**
 * @Author crqi
 * @Description 照片分组信息
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class ImageGroup extends Image {
    /**
     * 格式化后的时间
     */
    public String des;

    /**
     * 当前分组下面的所有图片信息，用于全选与选中
     */
    public List<ImageBean> list;

    /**
     * 获取描述信息，分组时取时间
     * 图片时取位置信息
     *
     * @return
     */
    @Override
    public String getDes() {
        return des;
    }
}
