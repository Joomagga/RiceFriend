package com.example.jooma.bobchingu;

/**
 * 어플을 처음 등록한 사용자만이 나타나는 Activity이다.
 * 닉네임을 받아서 등록 처리해준다. 입력 이후에 시간표 입력 Join activity로 이동한다.
 * 휴대폰 번호는 자체적으로 해결한다. (사용자 input이 요구되지 않는다.)
 **/
import android.app.Activity;
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
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EnterNickName extends AppCompatActivity implements DBResponse {

    Context context;
    Intent Go_Join;
    AlertDialog.Builder alert;


    public EnterNickName()
    {
        context = this;
    }

    /**
     * @param nickName 사용자가 가입한 닉네임
     * @param phone_num 어플을 다운로드한 휴대폰 번호
     * 닉네임과 휴대폰 번호를 SharedPreference에 저장한다.
     */

    private void putNickNameToSharedPreference(String nickName, String phone_num){
        SharedPreferences setting;

        setting = getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

        editor.putString("NickName", nickName);
        editor.commit();

        Toast.makeText(getApplicationContext(), setting.getString("NickName", "default value").toString()
                + "폰번호 : " + phone_num, Toast.LENGTH_SHORT).show();
    }

    protected  void ReceiveNickName(){
        alert = new AlertDialog.Builder(this); // NickName 입력창.

        alert.setTitle("닉네임을 입력해주세요.");
        alert.setMessage("(2~5 글자 제한)");

        // EditText 글자 수 5자로 제한
        final EditText name = new EditText(this);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(5);
        name.setFilters(FilterArray);

        alert.setView(name);
        alert.setCancelable(false); // alert 영역 밖 클릭에도 alert 유지
        alert.setPositiveButton("가입하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String nickName = name.getText().toString().trim();
                TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                String phone_num = tMgr.getLine1Number().substring(3);   // erase '+82'

                try {
                    if (nickName.length() >= 2 && nickName.length() <= 5) {
                        // 시간표 입력 activity로 이동한다.
                        putNickNameToSharedPreference(nickName, phone_num);
                        startActivity(Go_Join);

                        /**
                         * << DBConnection >>
                         * 닉네임 : nickName (String)
                         * 폰번호 : phone_num (String)
                         */
                        DBConnection con = new DBConnection(context);
                        con.enterMember(phone_num, nickName);
                        Toast.makeText(getApplicationContext(), "닉네임이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    } else if (nickName.length() == 1) {
                        Toast.makeText(getApplicationContext(), "두 자리 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        ReceiveNickName();
                        name.setText("");
                    } else {
                        ReceiveNickName();
                        Toast.makeText(getApplicationContext(), "입력란이 비었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        alert.show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_nick_name);

        Go_Join = new Intent(this, Join.class);

        ReceiveNickName();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);
        if(!dialogBounds.contains((int)ev.getX(), (int)ev.getY())){
            // 영역외 터치시 닫히지 않도록
            return false;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    public void getRoomList(ArrayList<RoomInfo> roomsList)
    {
    }

    public void getRoomMemberList(ArrayList<Integer> memberList)
    {
    }
}
