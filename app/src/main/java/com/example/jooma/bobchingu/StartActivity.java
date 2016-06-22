package com.example.jooma.bobchingu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class StartActivity extends AppCompatActivity {
    final String LOG_TAG = "logd, StartActivity";
    SharedPreferences setting;
    Intent go_Board, go_EnterNickName;

    public void usingPreferences() {
        // save data in a SharedPreferences container.
        // We need an Editor object to make preference changes.

        setting = getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

        if (setting.getString("number", "default value").equals("default value")) {
            editor.putString("number", "0000");
        }
        editor.putString("number", "0000"); // 수정할 부분
        editor.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        usingPreferences();

        Log.d(LOG_TAG, "== app starts ==");
        go_EnterNickName = new Intent(this, EnterNameActivity.class);
        go_Board = new Intent(this, MainActivity.class);

        if (setting.getString("number", "default value").equals("0000")) {
            Log.d(LOG_TAG, "startActivity(go_enterNickname)");
            startActivity(go_EnterNickName);
        } else {
            Log.d(LOG_TAG, "startActivity(go_board)");
            startActivity(go_Board);
        }
    }
}