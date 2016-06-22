package com.example.jooma.bobchingu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimeTableActivity extends AppCompatActivity {
    String LOG_TAG = "logd, TimeTableActivity";
    TextView[] Mon = new TextView[50];
    int[] tem = new int[50]; // Check 되었는지 확인
    int global_x;
    int global_y;
    Button confirm;
    Button reset;
    Intent mIntent;
    SharedPreferences setting;

    /**
     * SharedPreference 값을 설정해서 SelectActivity에서 바로 Main_content로 이동하게 한다.
     */
    private void ChangeState() {
        setting = getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

        // 가입이 완료됬음을 표시한다.
        // 어플 실행시 EnterNickName activity로 이동하지 않게 된다.
        editor.putString("number", "1111");
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        Log.d(LOG_TAG, "onCreated.");

        int mon_id = R.id.Mon9;

        mIntent = new Intent(this, MainActivity.class);

        for (int i = 0; i < 50; i++)
            Mon[i] = (TextView) findViewById(mon_id + i);

        // 모든 tem 변수 0으로 초기화
        for (int i = 0; i < 50; i++) tem[i] = 0;

        confirm = (Button) findViewById(R.id.confirm);
        reset = (Button) findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSet();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";

                for (int i = 0; i < 50; i++)
                    result += tem[i];
                /**
                 * << DBConnection >>
                 * 시간표 : result (0 또는 1인 String / 0 : uncheck, 1 : check / 길이 : 50)
                 * 월(10), 화(10) ... 금(10)
                 */
                try {
                    ChangeState();
                    startActivity(mIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void reSet() {

        for (int i = 0; i < 50; i++) {

            Mon[i].setBackgroundResource(R.drawable.field);
            tem[i] = 0;
        }
    }

    // Initial 좌표로부터 Touch event를 적용한다.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                global_x = (int) (event.getX());
                global_y = (int) (event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                global_x = (int) (event.getX());
                global_y = (int) (event.getY());
                break;
        }

        for (int i = 0; i < 50; i++) {

            if (((110 * (1 + i / 10) <= global_x && global_x < 110 + 110 * (1 + i / 10)) &&
                    (150 + 80 * (i % 10) <= global_y && global_y < 150 + 80 * (1 + i % 10))) && tem[i] == 0) {
                Mon[i].setBackgroundResource(R.drawable.field2);

                tem[i] = 1;
            }
        }

        return true;
    }

    // Block back button.
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}