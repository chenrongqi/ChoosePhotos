package com.crqi.choosephotos.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.crqi.choosephotos.R;

/**
 * @Author crqi
 * @Description 权限校验，TODO 暂时写死权限
 * @Date 7/9/22
 * @Email chenrongqiing@163.com
 */
public class PermissionUtil {
    private static final int CODE_FOR_WRITE_PERMISSION = 10001;
    private static final String storage = Manifest.permission.READ_EXTERNAL_STORAGE;


    public static void checkPermission(Activity activity,Runnable runnable){
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(activity, storage);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            runnable.run();
        }else{
            requestPermission(activity);
        }
    }

    private static void requestPermission(Activity activity){
        //没有权限，向用户请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION,storage}, CODE_FOR_WRITE_PERMISSION);
        }else {
            ActivityCompat.requestPermissions(activity, new String[]{storage}, CODE_FOR_WRITE_PERMISSION);
        }
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults, Runnable runnable) {
        //通过requestCode来识别是否同一个请求
        if (requestCode == CODE_FOR_WRITE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               runnable.run();
            }else{
                //用户不同意，向用户展示该权限作用
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, storage)) {
                    new AlertDialog.Builder(activity)
                            .setMessage(R.string.storage_permissions_remind)
                            .setPositiveButton("OK", (dialog1, which) ->requestPermission(activity))
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }
        }
    }
}
