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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// 点击时，首先获取“sdcard”的读写权限
// 权限通过时，打开《相册》进行选择
// （系统会将选择到的图片以Uri封装，存入到intent对象中）
// 选择结束时，从intent中获取uri(intent.getData())，
// 再利用ContentResolver从uri中获取文件的绝对路径，最终显示到ImageView中

public class SelectPhotoActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CROP_PHOTO = 3;
    private static final String TAG = "SelectActivity";

    private String uploadFileName;
    private byte[] fileBuf;
    private String uploadUrl = "http://49.234.29.33:3000/upload";

    private ImageView photo;

    private Button take_photo;
    private Button select_photo;
    private Button upload_photo;
    private Button mix_show;
    private Button multiply;

    private Uri imageUri;   // 记录拍照后的照片文件的地址

    // 打开相册 选择照片
    private void openGallery() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        // 如果第二个参数大于或等于0
        // 那么当用户操作完成后会返回到本程序的onActivityResult方法
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        photo = (ImageView) findViewById(R.id.photo_view);

        // 打开相机拍照
        take_photo = (Button) findViewById(R.id.take_photo_button);
        take_photo.getBackground().setAlpha(75);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除并创建临时文件 保存拍照后的照片
                File outputImage = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 复杂的Uri创建方式
                if (Build.VERSION.SDK_INT >= 24)
                    //Android7后，更安全获取文件uri
                    imageUri = FileProvider.getUriForFile(SelectPhotoActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
                else {
                    imageUri = Uri.fromFile(outputImage);
                }
                openCamera();
            }
        });

        // 本地图片选取
        select_photo = (Button) findViewById(R.id.select_photo_button);
        select_photo.getBackground().setAlpha(75);
        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();  // 打开相册，选择照片
            }
        });

        // 文件上传
        upload_photo = (Button) findViewById(R.id.upload_photo_button);
        upload_photo.getBackground().setAlpha(75);
        upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileBuf == null){
                    Toast.makeText(SelectPhotoActivity.this,"请选择或拍摄一张照片",Toast.LENGTH_LONG).show();
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
                            // Start ReactionActivity
                            Intent intent = new Intent(SelectPhotoActivity.this, ReactionActivity.class);
                            intent.putExtra("key",response.body().string());
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        // 混合式技术
        mix_show = (Button)findViewById(R.id.show_pic);
        mix_show.getBackground().setAlpha(75);
        mix_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPhotoActivity.this,ListActivity.class);
                startActivity(intent);
            }
        });

        // 更多功能
        multiply = (Button)findViewById(R.id.multi_function);
        multiply.getBackground().setAlpha(150);
        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPhotoActivity.this,MultiActivity.class);
                startActivity(intent);
            }
        });
    }

    // 用户选择的图片显示在ImageView中
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    photo.setImageBitmap(bitmap);
                    fileBuf = convertToBytes(getContentResolver().openInputStream(imageUri));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
//            case CROP_PHOTO:
//                try {
//                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
//                    fileBuf = convertToBytes(inputStream);
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf,0,fileBuf.length);
//                    photo.setImageBitmap(bitmap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
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
            Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf,0,fileBuf.length);
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
