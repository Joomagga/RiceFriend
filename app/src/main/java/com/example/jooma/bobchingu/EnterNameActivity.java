package com.example.jooma.bobchingu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

public class EnterNameActivity extends AppCompatActivity {

    private final String LOG_TAG = "logd, EnterNickName";
    private Context mContext;
    private Intent Go_Join;
    private AlertDialog.Builder mAlert;


    public EnterNameActivity() {
        mContext = this;
        Log.d(LOG_TAG, "EnterNameActivity started.");
    }

    /**
     * @param nickName  사용자가 가입한 닉네임
     * @param phone_num 어플을 다운로드한 휴대폰 번호
     *                  닉네임과 휴대폰 번호를 SharedPreference에 저장한다.
     */

    private void putNickNameToSharedPreference(String nickName, String phone_num) {
        SharedPreferences setting;

        setting = getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

        editor.putString("NickName", nickName);
        editor.commit();

        Toast.makeText(getApplicationContext(), setting.getString("NickName", "default value").toString()
                + "폰번호 : " + phone_num, Toast.LENGTH_SHORT).show();
    }

    protected void ReceiveNickName() {
        mAlert = new AlertDialog.Builder(this); // NickName 입력창.

        mAlert.setTitle("닉네임을 입력해주세요.");
        mAlert.setMessage("(2~5 글자 제한)");

        // EditText 글자 수 5자로 제한
        final EditText name = new EditText(this);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(5);
        name.setFilters(FilterArray);

        mAlert.setView(name);
        mAlert.setCancelable(false); // mAlert 영역 밖 클릭에도 mAlert 유지
        mAlert.setPositiveButton("가입하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String nickName = name.getText().toString().trim();
                Log.d(LOG_TAG, "before setting tMgr");
                TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                String phone_num = tMgr.getLine1Number().substring(3);   // erase '+82'
                Log.d(LOG_TAG, "after setting tMgr");

                try {
                    if (nickName.length() >= 2 && nickName.length() <= 5) {
                        putNickNameToSharedPreference(nickName, phone_num);
                        SyncServerService service = new SyncServerService(mContext);
                        service.registerName(phone_num, nickName);
                        Log.d(LOG_TAG, "con.enterMember is running.");
                        service.sendContactsToServer();   // for debug
                        Log.d(LOG_TAG, "service.syncFriendship is running.");
                        startActivity(new Intent(mContext, TimeTableActivity.class));
                        Toast.makeText(getApplicationContext(), "닉네임이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    } else if (nickName.length() == 1) {
                        Toast.makeText(getApplicationContext(), "두 자리 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        ReceiveNickName();
                        name.setText("");
                    } else {
                        ReceiveNickName();
                        Toast.makeText(getApplicationContext(), "입력란이 비었습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mAlert.show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        Go_Join = new Intent(this, TimeTableActivity.class);

        ReceiveNickName();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);
        if (!dialogBounds.contains((int) ev.getX(), (int) ev.getY())) {
            // 영역외 터치시 닫히지 않도록
            return false;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}

