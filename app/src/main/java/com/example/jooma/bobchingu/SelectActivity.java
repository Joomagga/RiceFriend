package com.example.jooma.bobchingu;

/**
 * MainActivity로 지정되어 있다.
 * 어플을 처음 시작할 때 회원 등록을 위한 actvitiy로 이동한다.
 * 이미 등록을 마친 상태인 경우 게시물을 보여주는 activity로 이동한다.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SelectActivity extends AppCompatActivity {
    SharedPreferences setting;
    Intent go_Board, go_EnterNickName;

    public void usingPreferences(){
        // save data in a SharedPreferences container.
        // We need an Editor object to make preference changes.

        setting = getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

        if(setting.getString("number", "default value").equals("default value")) {
            editor.putString("number", "0000");
        }
        editor.putString("number", "0000"); // 수정할 부분
        editor.commit();

        Toast.makeText(getApplicationContext(), setting.getString("number", "default value").toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        usingPreferences();

        go_EnterNickName = new Intent(this, EnterNickName.class);
        go_Board = new Intent(this, Main_content.class);

        if(setting.getString("number", "default value").equals("0000"))
            startActivity(go_EnterNickName);
        else
            startActivity(go_Board);
    }
}


