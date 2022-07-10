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
    public List<ImageBean> list;

    @Override
    public String getDes() {
        return des;
    }
}
