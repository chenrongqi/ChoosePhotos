package com.crqi.choosephotos.bean;

import android.net.Uri;

/**
 * @Author crqi
 * @Description 本地图片数据实体
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class ImageBean extends Image {
    /**
     * 图片uri
     */
    public Uri imgUri;
    public Double duration;
    public boolean isVideo = false;
    /**
     * 照片额外信息，有文件操作
     */
    public float latitude;
    public float longitude;

    /**
     * 添加时间
     */
    public long time;
    /**
     * 格式化后的时间
     */
    public String date;

    @Override
    public String getDes() {
        String prefix = null;
        if (isVideo) {
            prefix = String.valueOf(duration/1000) +"s";
        }
        if (latitude == 0 && longitude == 0 ) {
            return prefix;
        } else if(prefix==null){
            return latitude + "\n" + longitude;
        }else{
            return prefix + "\n" + latitude + "\n" + longitude;
        }
    }
}
