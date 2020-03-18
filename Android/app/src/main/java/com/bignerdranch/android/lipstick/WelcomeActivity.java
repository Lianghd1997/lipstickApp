package com.bignerdranch.android.lipstick;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//AS 下全屏隐藏标题栏代码
        setContentView(R.layout.activity_welcome);
        RelativeLayout laoutsplsh=findViewById(R.id.activity_splash);
        AlphaAnimation alphaAnimation =new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(2200);
        laoutsplsh.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(WelcomeActivity.this,SelectPhotoActivity.class);
                //intent.setClass(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // SelectActivity设置为栈底(防止按返回键时返回到启动欢迎界面)
                startActivity(intent);// 载入主窗口
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
