package com.crqi.choosephotos.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.crqi.choosephotos.R;
import com.crqi.choosephotos.bean.Image;
import com.crqi.choosephotos.bean.ImageGroup;
import com.crqi.choosephotos.model.OnCheckChange;
import com.crqi.choosephotos.util.PhotosUtil;
import com.crqi.choosephotos.util.ToastUtil;

import java.util.List;

/**
 * @Author crqi
 * @Description 图片选择适配器
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {
    private static final int GROUP_TYPE = 1;
    private static final int IMAGE_TYPE = 0;

    private List<Image> date;
    private OnCheckChange listener;
    public ImageAdapter(List<Image> date, OnCheckChange listener){
        this.date = date;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == GROUP_TYPE){
            return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_group,parent,false));
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false);
            ImageView imageView = view.findViewById(R.id.image);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
            // TODO 可根据业务下发
            layoutParams.height = (int) (PhotosUtil.smallHeight-6);
            layoutParams.width = (int) (PhotosUtil.smallWidth-6);
            return new ImageHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (date.get(position) instanceof ImageGroup)?GROUP_TYPE:IMAGE_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Image image = date.get(position);
        holder.setImage(image);
        if(listener!=null){
            image.position = position;
        }else{
            //选中结果查看页面无需展示勾选
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener((view) -> {
            image.selected = !image.selected;
            if(listener!=null){
                listener.check(image);
            }else{
                ToastUtil.showToast("暂不支持预览");
            }
        });

    }

    @Override
    public int getItemCount() {
        return date.size();
    }
}
