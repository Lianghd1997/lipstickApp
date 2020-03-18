package com.bignerdranch.android.lipstick;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import java.io.InputStream;

public class MultiActivity extends AppCompatActivity {
    private Button compare_button;
    private Button face_database_button;
    private Button face_search_button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutli);

        compare_button = (Button)findViewById(R.id.compare_button);
        compare_button.getBackground().setAlpha(75);
        compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiActivity.this,CompareActivity.class);
                startActivity(intent);
            }
        });

        face_database_button = (Button)findViewById(R.id.face_database);
        face_database_button.getBackground().setAlpha(75);
        face_database_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiActivity.this,FaceDatabaseActivity.class);
                startActivity(intent);
            }
        });

        face_search_button = (Button)findViewById(R.id.face_search);
        face_search_button.getBackground().setAlpha(75);
        face_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiActivity.this,FaceSearchActivity.class);
                startActivity(intent);
            }
        });


    }
}




