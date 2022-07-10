package com.crqi.choosephotos.model;

import com.crqi.choosephotos.bean.Image;
import com.crqi.choosephotos.bean.ImageBean;
import com.crqi.choosephotos.bean.ImageGroup;
import com.crqi.choosephotos.ui.IView;
import com.crqi.choosephotos.util.PhotosUtil;
import com.crqi.choosephotos.util.ThreadPoolUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author crqi
 * @Description 逻辑控制类
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class Presenter<T extends IView> {
    private WeakReference<T> viewWeak;

    public Presenter(T view) {
        viewWeak = new WeakReference<T>(view);
    }

    private T getView() {
        return viewWeak.get();
    }

    //已经选中的图片
    public Map<String, Image> choosePhotos = new HashMap<>();
    //所有图片
    public List<Image> allImage;

    //已选择个数
    public int selectEd = 0;

    //获取选中的图片
    public List<Image> getSelectImage() {
        List<Image> list = new ArrayList<>();
        for (Image image : choosePhotos.values()) {
            if (image instanceof ImageBean) {
                list.add(image);
            }
        }
        return list;

    }

    /**
     * 读取本地图片
     */
    public void loadLocalImage() {
        ThreadPoolUtil.getAppExecutor().execute(() -> {
            allImage = PhotosUtil.getAllLocalImage();
            ThreadPoolUtil.sendMainThread(() -> {
                if (allImage == null) {
                    getView().err();
                } else {
                    getView().success();
                }
            });

        });
    }

    /**
     * 1、单选
     * 2、全选
     * 3、全选之后的取消
     *
     * @param image
     */
    public void selectImage(Image image) {
        String name = image.getDes();
        if (image instanceof ImageBean) {
            name = ((ImageBean) image).imgUri.getPath();
        }
        if (image instanceof ImageGroup) {
            List<ImageBean> childList = ((ImageGroup) image).list;
            int size = childList.size();
            for (ImageBean bean : childList) {
                bean.selected = image.selected;
                if (image.selected) {
                    //全选时批量添加组员
                    choosePhotos.put(bean.imgUri.getPath(), bean);
                    selectEd++;
                } else {
                    //全不选时批量删除组员
                    selectEd--;
                    choosePhotos.remove(bean.imgUri.getPath());
                }
            }
            //添加自己 不计数
            if (image.selected) {
                choosePhotos.put(name, image);
            } else {
                choosePhotos.remove(name);
            }
            getView().refreshRange(image.position, size + 1);
        } else {
            assert image instanceof ImageBean;
            String parentName = ((ImageBean) image).date;
            if (choosePhotos.containsKey(parentName)) {
                //全选之后的取消，取消父类中的选择
                //不计数
                ImageGroup imageGroup = (ImageGroup) choosePhotos.get(parentName);
                imageGroup.selected = image.selected;
                getView().refreshItem(imageGroup.position);
                choosePhotos.remove(parentName);
            }
            //计数
            if (image.selected) {
                selectEd++;
                choosePhotos.put(name, image);
            } else {
                selectEd--;
                choosePhotos.remove(name);
            }
            getView().refreshItem(image.position);
        }
    }


}
