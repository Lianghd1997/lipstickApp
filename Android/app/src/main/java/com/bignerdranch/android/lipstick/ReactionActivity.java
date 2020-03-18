package com.bignerdranch.android.lipstick;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ReactionActivity extends AppCompatActivity {
    private Button list_button;
    private String lipstick_answer;
    private TextView reactionText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);

        reactionText = findViewById(R.id.reaction_text);
        reactionText.getBackground().setAlpha(75);
        Intent intent = getIntent();
        lipstick_answer = intent.getStringExtra("key");
        parseJSONWithJSONObject(lipstick_answer);

//        list_button = (Button) findViewById(R.id.list_button);
//        list_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // // Start ListActivity
//                Intent intent = new Intent(ReactionActivity.this,ListActivity.class);
//                startActivity(intent);
//            }
//        });

    }
    private void parseJSONWithJSONObject(String jsonData){
        if (jsonData != null){
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                String resultCode = jsonObject.getString("code");
                if (resultCode.equals("0")){
                    try {
                        reactionText.setText("");
                        String brand = jsonObject.getString("brand");
                        String name = jsonObject.getString("name");
                        String id = jsonObject.getString("id");
                        String colorname = jsonObject.getString("colorname");

                        String lipstickinfo = "品牌："+brand+"\n"+"名称："+name+"\n"+"色号："+id+"\n"+"名称："+colorname;
                        reactionText.append(lipstickinfo);
//                        reactionText.setTextColor(Color.RED);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (resultCode.equals("1")){
                    try {
                        reactionText.setText("");
                        String message1 = "上传文件为空或失败";
                        reactionText.append(message1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (resultCode.equals("2")){
                    try {
                        reactionText.setText("");
                        String message2 = "没有检测到人脸";
                        reactionText.append(message2);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (resultCode.equals("3")){
                    try {
                        reactionText.setText("");
                        String message3 = "没有找到这个颜色的口红";
                        reactionText.append(message3);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
