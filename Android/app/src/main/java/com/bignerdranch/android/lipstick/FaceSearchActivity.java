package com.bignerdranch.android.lipstick;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FaceSearchActivity extends AppCompatActivity {

    private Button select_button;
    private  Button face_search_button;
    public static final int CHOOSE_PHOTO = 1;

    private ImageView photo;

    private String uploadFileName;
    private byte[] fileBuf;
    private String uploadUrl = "http://49.234.29.33:3000/upload2";

    // 打开相册 选择照片
    private void openGallery() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        // 如果第二个参数大于或等于0
        // 那么当用户操作完成后会返回到本程序的onActivityResult方法
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_search);

        photo = (ImageView) findViewById(R.id.photo);

        select_button = (Button) findViewById(R.id.select_photo_button);
        select_button.getBackground().setAlpha(75);
        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        face_search_button = (Button)findViewById(R.id.face_search);
        face_search_button.getBackground().setAlpha(75);
        face_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileBuf == null){
                    Toast.makeText(FaceSearchActivity.this,"请选择一张照片",Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        //上传文件域的请求体部分
                        RequestBody formBody = RequestBody
                                .create(fileBuf, MediaType.parse("image/jpeg"));
                        //整个上传的请求体部分（普通表单+文件上传域）
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("title", "Square Logo")
                                //filename:avatar,originname:abc.jpg
                                .addFormDataPart("avatar", uploadFileName, formBody)
                                .build();
                        Request request = new Request.Builder()
                                .url(uploadUrl)
                                .post(requestBody)
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
//                            Log.i("数据", response.body().string() + "....");
//                            responseData = response.body().string();
                            // Start FaceSearchReactionActivity
                            Intent intent = new Intent(FaceSearchActivity.this,FaceSearchReactionActivity.class);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
    // 用户选择的图片显示在ImageView中
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                handleSelect(data);
                break;
        }
    }

    // 选择后照片的读取工作
    private void handleSelect(Intent intent) {
        Cursor cursor = null;
        Uri uri = intent.getData();
        cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            uploadFileName = cursor.getString(columnIndex);
        }
        // 改良优化
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            fileBuf = convertToBytes(inputStream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf, 0, fileBuf.length);
            photo.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
            Glide.with(this).load(uri)
                    .fitCenter()
                    .into(photo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
    }

    private byte[] convertToBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();
        return out.toByteArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "读取相册的请求被拒绝", Toast.LENGTH_LONG).show();
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermission(View view) {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}


