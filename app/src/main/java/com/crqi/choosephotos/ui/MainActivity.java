package com.crqi.choosephotos.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crqi.choosephotos.R;
import com.crqi.choosephotos.bean.Image;
import com.crqi.choosephotos.bean.ImageGroup;
import com.crqi.choosephotos.model.OnCheckChange;
import com.crqi.choosephotos.model.Presenter;
import com.crqi.choosephotos.util.PermissionUtil;
import com.crqi.choosephotos.util.ToastUtil;
import com.crqi.choosephotos.util.UIUtil;

public class MainActivity extends AppCompatActivity implements IView, OnCheckChange {


    private Presenter<MainActivity> presenter = new Presenter<>(this);
    private FrameLayout mainContent;
    RecyclerView recyclerView;
    private ImageAdapter adapter;
    private MenuItem doneView;

    /***
     * 状态切换结果预览页面
     */
    private int isResultPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainContent = findViewById(R.id.layout_content);
        initLoadingView();
        PermissionUtil.checkPermission(this, () -> presenter.loadLocalImage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        doneView = menu.findItem(R.id.action_done);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_done) {
            if (isResultPage == 0) {
                toResultView();
            } else {
                toImageListView();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 选择图片页面
     */
    private void toImageListView() {
        mainContent.removeAllViews();
        if (recyclerView == null) {
            recyclerView = new RecyclerView(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (presenter.allImage.get(position) instanceof ImageGroup) {
                        return 3;
                    } else {
                        return 1;
                    }
                }
            });
            recyclerView.setHasFixedSize(true);
            adapter = new ImageAdapter(presenter.allImage, this);
            recyclerView.setAdapter(adapter);
        }
        mainContent.addView(recyclerView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        doneView.setTitle(getString(R.string.done) + "(" + presenter.selectEd + ")");
        isResultPage = 0;
    }

    /**
     * 选择图片结果页面
     */
    private void toResultView() {
        mainContent.removeAllViews();
        RecyclerView recyclerView = new RecyclerView(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        recyclerView.setHasFixedSize(false);
        ImageAdapter adapter = new ImageAdapter(presenter.getSelectImage(), null);
        recyclerView.setAdapter(adapter);
        mainContent.addView(recyclerView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        doneView.setTitle(getString(R.string.reset));
        isResultPage = 1;
    }

    /**
     * 进度条
     */
    private void initLoadingView() {
        ProgressBar progressBar = new ProgressBar(this);
        mainContent.addView(progressBar, new FrameLayout.LayoutParams(UIUtil.dp2px(this, 100), UIUtil.dp2px(this, 100), Gravity.CENTER));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults, () -> presenter.loadLocalImage());
    }


    @Override
    public void success() {
        toImageListView();
    }

    @Override
    public void err() {
        ToastUtil.showToast("读取图片发生异常");
    }

    @Override
    public void refreshAll() {
        mainContent.post(() -> {
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void refreshRange(int start, int count) {
        mainContent.post(() -> {
            adapter.notifyItemRangeChanged(start, count);
        });
    }

    @Override
    public void refreshItem(int position) {
        mainContent.post(() -> {
            adapter.notifyItemChanged(position);
        });
    }

    @Override
    public void check(Image image) {
        presenter.selectImage(image);
        doneView.setTitle(getString(R.string.done) + "(" + presenter.selectEd + ")");
    }
}