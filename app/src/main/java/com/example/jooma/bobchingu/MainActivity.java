package com.example.jooma.bobchingu;

/**
 * MainActivity로 지정되어 있다.
 * 어플을 처음 시작할 때 회원 등록을 위한 actvitiy로 이동한다.
 * 이미 등록을 마친 상태인 경우 게시물을 보여주는 activity로 이동한다.
 */

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SyncServerService con;

    final String LOG_TAG = "logd, MainActivity";

    final int ROOMLIST_FRAGMENT = 1;
    final int REGIST_ROOM_FRAGMENT = 2;
    final int NEW_FRIEND_FRAGMENT = 3;

    private Button btRoomlist;
    private Button btRegistRoom;
    private Button btNewFriend;

    private Context mContext;

    private android.app.FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        showFragment(ROOMLIST_FRAGMENT);
    }

    public void showFragment(int flag) {
        Fragment fragment = null;

        switch (flag) {
            case ROOMLIST_FRAGMENT:
                fragment = new RoomListFragment();
                break;
            case REGIST_ROOM_FRAGMENT:
                fragment = new RegisterRoomFragment();
                break;
        }
        Log.d(LOG_TAG, "before getFragmentManager()");
        getFragmentManager().beginTransaction().add(R.id.layout, fragment).commit();
        Log.d(LOG_TAG, "after getFragmentManager()");
        //startActivity(new Intent(mContext, RegisterRoomActivity.class));
    }

    private void init() {
        mContext = this;
        con = new SyncServerService(this);
        con.updateSQLiteDb();

        btRoomlist = (Button) findViewById(R.id.Eat);
        btNewFriend = (Button) findViewById(R.id.NewFriend);
        btRegistRoom = (Button) findViewById(R.id.Enroll);

        btRoomlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "btRoomlist clicked.");
                showFragment(ROOMLIST_FRAGMENT);
            }
        });
        btNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "btNewFriend clicked.");
                showFragment(NEW_FRIEND_FRAGMENT);
            }
        });
        btRegistRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "btRegistRoom clicked.");
                //showFragment(REGIST_ROOM_FRAGMENT);
                startActivity(new Intent(mContext, RegisterRoomActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}