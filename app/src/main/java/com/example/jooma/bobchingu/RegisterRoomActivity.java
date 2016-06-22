package com.example.jooma.bobchingu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterRoomActivity extends AppCompatActivity {

    Button confirm;
    SyncServerService con;
    EditText[] time = new EditText[2]; // int형
    EditText place; // string
    EditText content; // string

    RegisterRoomActivity() {
        con = new SyncServerService(this);
    }

    //
    private String getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String phone_num = tMgr.getLine1Number().substring(3);   // erase '+82'

        return phone_num;
    }

    private void TestAndConfirm() {

        int test1 = 0, test2 = 0; // Test the time.
        int test3 = 0; // Test the empty content in 장소
        int n1, n2;  // 시간, 분

        try {
            n1 = Integer.parseInt(time[0].getText().toString());
            n2 = Integer.parseInt(time[1].getText().toString());
            String time_re = time[0].getText().toString() + time[1].getText().toString();

            if (0 <= n1 && n1 < 24) test1 = 1;
            if (0 <= n2 && n2 < 60) test2 = 1;
            if (!place.getText().toString().equals("")) test3 = 1;

            if (test1 * test2 * test3 == 1) {
                Toast.makeText(getApplicationContext(), "등록되었습니다." +
                        Integer.parseInt(time_re), Toast.LENGTH_SHORT).show();
                con.registerRoom(new RoomInfo(Integer.parseInt(getPhoneNumber()), content.getText().toString(),
                        place.getText().toString(), Integer.parseInt(time_re)));
                startActivity(new Intent(this, MainActivity.class));
            } else if (test1 == 0 || test2 == 0) {
                Toast.makeText(getApplicationContext(), "(시/분)을 확인해주세요.", Toast.LENGTH_SHORT).show();
            } else if (test3 == 0) {
                Toast.makeText(getApplicationContext(), "장소가 비어있습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "입력란을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_room);

        time[0] = (EditText) findViewById(R.id.Time0);
        time[1] = (EditText) findViewById(R.id.Time1);

        place = (EditText) findViewById(R.id.Place);
        content = (EditText) findViewById(R.id.Content);

        confirm = (Button) findViewById(R.id.Confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TestAndConfirm();
            }
        });
    }
}