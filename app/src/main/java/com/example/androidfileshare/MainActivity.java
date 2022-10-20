package com.example.androidfileshare;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestmanageexternalstorage_Permission();
    }

    public void shareFile(View v) {
        final Uri uri;
        final File file = new File(Environment.getExternalStorageDirectory().getPath(), "/test/test.pdf");
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 24) {//若SDK大于等于24  获取uri采用共享文件模式
            uri = FileProvider.getUriForFile(this.getApplicationContext(), "com.example.androidfileshare.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }


        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("*/*");//此处可发送多种文件
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.addCategory(Intent.CATEGORY_DEFAULT);
        // 分享到qq
        share.setPackage("com.tencent.mobileqq");
        // 分享到微信
        // share.setPackage("com.tencent.mm");
        this.startActivity(share);
    }

    /**
     * 开放文件访问的权限请求
     */
    private void requestmanageexternalstorage_Permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(this, "Android VERSION  R OR ABOVE，HAVE MANAGE_EXTERNAL_STORAGE GRANTED!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Android VERSION  R OR ABOVE，NO MANAGE_EXTERNAL_STORAGE GRANTED!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                this.startActivity(intent);
            }
        }

    }
}