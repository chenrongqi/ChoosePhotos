package com.crqi.choosephotos.ui;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crqi.choosephotos.R;
import com.crqi.choosephotos.bean.Image;
import com.crqi.choosephotos.bean.ImageBean;
import com.crqi.choosephotos.util.PhotosUtil;
import com.crqi.choosephotos.util.ThreadPoolUtil;

/**
 * @Author crqi
 * @Description view 缓存holder TODO 时间问题暂时耦合
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class ImageHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView desView;
    CheckBox checkBox;
    ImageView videoView;
    public ImageHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * 加载展示数据
     * @param image
     */
    public void setImage(Image image){
        if(image instanceof ImageBean){
            if(imageView==null){
                imageView =itemView.findViewById(R.id.image);
            }
            ThreadPoolUtil.getAppExecutor().execute(()->{
                Bitmap bitmap = PhotosUtil.loadBitmap((ImageBean) image);
                ThreadPoolUtil.sendMainThread(()->{
                    imageView.setImageBitmap(bitmap);
                    setDes(image.getDes());
                });
            });
            if(videoView==null){
                videoView = itemView.findViewById(R.id.play);
            }
            if(((ImageBean) image).isVideo){
                videoView.setVisibility(View.VISIBLE);
            }else{
                videoView.setVisibility(View.GONE);
            }

        }else{
            setDes(image.getDes());
        }
        setCheck(image.selected);
    }

    public void setDes(String des){
        if(desView==null){
            desView =itemView.findViewById(R.id.text);
        }
        desView.setText(des);
    }


    public void setCheck(boolean check){
        if(checkBox==null){
            checkBox = itemView.findViewById(R.id.checkbox);
        }
        checkBox.setChecked(check);
    }

}
