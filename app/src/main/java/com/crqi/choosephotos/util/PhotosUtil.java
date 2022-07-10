package com.crqi.choosephotos.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;

import androidx.annotation.RequiresApi;

import com.crqi.choosephotos.bean.Image;
import com.crqi.choosephotos.bean.ImageBean;
import com.crqi.choosephotos.bean.ImageGroup;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.graphics.Bitmap.Config.RGB_565;

/**
 * @Author crqi
 * @Description 图片管理工具
 * https://developer.android.com/training/data-storage/shared/media?hl=zh-cn#java
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class PhotosUtil {
    public static double smallWidth = 0;
    public static double smallHeight = 0;

    /**
     * 获取所有图片和视频
     * cursor.moveToPosition()
     * TODO
     * 可以优化成分页加载
     * 可以优化成多线程加载
     * <p>
     * 正式环境时使用 getColumnIndexOrThrow
     *
     * @return
     */
    public static List<Image> getAllLocalImage() {
        smallWidth = UIUtil.getScreenWidth() / 3.0;
        smallHeight = smallWidth / 0.618;

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        Map<String, ImageGroup> imageGroupMap = new HashMap<>();

        loadImage(imageGroupMap, format);
        loadVideo(format, imageGroupMap);
        List<String> keyList = new ArrayList<String>(imageGroupMap.keySet());
        Collections.sort(keyList, (o1, o2) -> o2.compareTo(o1));
        List<Image> list = new ArrayList<>();
        for (String time : keyList) {
            //TODO 是否需要排序
            ImageGroup group = imageGroupMap.get(time);
            list.add(group);
            list.addAll(group.list);
        }
        imageGroupMap.clear();
        return list;


    }

    /**
     * 加载视频
     * @param format
     * @param imageGroupMap
     */
    private static void loadVideo(SimpleDateFormat format, Map<String, ImageGroup> imageGroupMap) {
        String[] VideoProjection = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DATE_ADDED,
                MediaStore.Video.VideoColumns.ALBUM,
                MediaStore.Video.VideoColumns.LATITUDE,
                MediaStore.Video.VideoColumns.LONGITUDE,
        };
        Cursor videoCursor = ContextUtil.getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VideoProjection, null, null, MediaStore.Video.Media.DATE_ADDED);
        try {
            videoCursor.moveToLast();
            do {
                ImageBean image = new ImageBean();
                long id = videoCursor.getLong(0);
                Double duration = videoCursor.getDouble(2);
                long time = videoCursor.getLong(4);
                float latitude = videoCursor.getFloat(6);
                float longitude = videoCursor.getFloat(7);
                image.imgUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                image.latitude = latitude;
                image.longitude = longitude;
                image.time = time;
                image.duration = duration;
                image.isVideo = true;

                image.date = format.format(new Date(time * 1000));
                if (imageGroupMap.containsKey(image.date)) {
                    imageGroupMap.get(image.date).list.add(image);
                } else {
                    ImageGroup group = new ImageGroup();
                    group.des = image.date;
                    group.list = new ArrayList<ImageBean>();
                    group.list.add(image);
                    imageGroupMap.put(image.date, group);
                }
            } while (videoCursor.moveToPrevious());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            videoCursor.close();
        }
    }

    /**
     * 加载图片
     * @param imageGroupMap
     * @param format
     */
    private static void loadImage(Map<String, ImageGroup> imageGroupMap, SimpleDateFormat format) {
        String[] ImageProjection = new String[]{
                MediaStore.Images.Media._ID,  //图片完整路径
                MediaStore.Images.Media.DATA,  //图片名称
                MediaStore.Images.Media.DISPLAY_NAME,  //被添加到库中的时间
                MediaStore.Images.Media.DATE_ADDED,  //所在文件夹ID
                MediaStore.Images.Media.LATITUDE,
                MediaStore.Images.Media.LONGITUDE};
        Cursor cursor = ContextUtil.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ImageProjection,
                null, null, MediaStore.Images.Media.DATE_ADDED);
        try {
            cursor.moveToLast();
            do {
                ImageBean image = new ImageBean();
                long id = cursor.getLong(0);
                long time = cursor.getLong(3);
                float latitude = cursor.getFloat(4);
                float longitude = cursor.getFloat(5);
                image.imgUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                image.latitude = latitude;
                image.longitude = longitude;
                image.time = time;

                image.date = format.format(new Date(time * 1000));
                if (imageGroupMap.containsKey(image.date)) {
                    imageGroupMap.get(image.date).list.add(image);
                } else {
                    ImageGroup group = new ImageGroup();
                    group.des = image.date;
                    group.list = new ArrayList<ImageBean>();
                    group.list.add(image);
                    imageGroupMap.put(image.date, group);
                }
            } while (cursor.moveToPrevious());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

    }


    /**
     * 获取位置信息
     *
     * @param inputStream
     * @param bean
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static void loadImageLocationN(InputStream inputStream, ImageBean bean) throws IOException {
        ExifInterface exif = new ExifInterface(inputStream);
        float[] singleLat = new float[2];
        boolean hasPermission = exif.getLatLong(singleLat);
        if (singleLat[0] != 0) {
            bean.latitude = singleLat[0];
            bean.longitude = singleLat[1];
            return;
        }
        Log.e("PhotosUtil", "uri:" + bean.imgUri.getPath() + "\t" + "位置状态可读：" + hasPermission);
    }

    /**
     * 获取位置信息
     *
     * @param inputStream
     * @param bean
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void loadImageLocationQ(InputStream inputStream, ImageBean bean) throws IOException {
        loadImageLocationN(inputStream, bean);
        inputStream.close();
    }

    /**
     * 加载图片 TODO 可更换成第三方图片库，时间问题不做缓存优化
     * <p>
     *
     * @param bean
     * @return
     */
    public static Bitmap loadBitmap(ImageBean bean) {

        if (bean.isVideo) {
            return getVideoBitmap(bean);
        }
        try {
            Bitmap thumbnail = getThumbnailBitmap(bean);
            if (thumbnail != null) return thumbnail;
            return getCommonSmallBitmap(bean);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载图片
     * 可以抽离成传入大小的通用方法，或者用第三方图片库加载
     *
     * @param bean
     * @return
     * @throws IOException
     */
    private static Bitmap getCommonSmallBitmap(ImageBean bean) throws IOException {
        ContentResolver resolver = ContextUtil.getContext().getContentResolver();

        InputStream inputStream = resolver.openInputStream(bean.imgUri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = RGB_565;
        BufferedInputStream input = new BufferedInputStream(inputStream);
        input.mark(input.available());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && bean.getDes() == null) {
            loadImageLocationN(input, bean);
        }
        input.reset();
        BitmapFactory.decodeStream(input, null, options);
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > smallWidth || height > smallHeight) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            while (halfWidth / inSampleSize >= smallWidth && halfHeight / inSampleSize >= smallHeight) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        input.reset();
        Bitmap bitmapTrue = BitmapFactory.decodeStream(input, null, options);

        input.close();
        inputStream.close();

        return bitmapTrue;
    }

    /**
     * google 推荐加载缩略图方案
     * Bitmap thumbnail =
     * getApplicationContext().getContentResolver().loadThumbnail(
     * content-uri, new Size(640, 480), null);
     *
     * @param bean
     * @return
     */
    private static Bitmap getThumbnailBitmap(ImageBean bean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = ContextUtil.getContext().getContentResolver();
            try {
                Bitmap thumbnail =
                        resolver.loadThumbnail(
                                bean.imgUri, new Size(480, 640), null);
                if (bean.getDes() == null) {
                    loadImageLocationQ(resolver.openInputStream(MediaStore.setRequireOriginal(bean.imgUri)), bean);
                }
                if (thumbnail != null) {
                    return thumbnail;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 加载视频中的第一张
     *
     * @param bean
     * @return
     */
    private static Bitmap getVideoBitmap(ImageBean bean) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(ContextUtil.getContext(), bean.imgUri);// videoPath 本地视频的路径
        String location = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
        if (!TextUtils.isEmpty(location) && bean.longitude == 0) {
            String[] lat = location.replace("/", "").replaceFirst("\\+", "").split("\\+");
            if (lat.length == 2) {
                bean.longitude = Float.parseFloat(lat[1]);
                bean.latitude = Float.parseFloat(lat[0]);

            }
        }
        return media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    }

}
