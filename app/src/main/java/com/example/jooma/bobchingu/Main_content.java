
package com.example.jooma.bobchingu;

/**
 * Custom ListView를 사용해서 게시물 정보들을 보여주는 activity.
 * Long Click : 게시물을 임의로 삭제할 수 있다.
 * Short Click : 게시된 밥 그룹에 참여할 수 있다.
 * (등록하기)버튼을 통해 게시물을 등록할 수 있다.
 * (밥친구)버튼을 통해 게시물을 update할 수 있다.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main_content extends AppCompatActivity implements DBResponse {

    Button eat, enroll;
    DBConnection con;
    Intent send_to_enroll;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    SharedPreferences setting;
    AlertDialog.Builder alert;

    public void getRoomList(ArrayList<RoomInfo> roomsList)
    {
        mAdapter = new ListViewAdapter(this);
        for (int i=0; i<roomsList.size(); i++) {
            mAdapter.addItem(getResources().getDrawable(R.drawable.uncheck),
                    "방장: " + roomsList.get(i).getMasterName(),
                    "시간: " + roomsList.get(i).getTime().substring(0, 2) + ":" + roomsList.get(i).getTime().substring(2, 4) + "  장소: " + roomsList.get(i).getLocation(),
                    "내용: " + roomsList.get(i).getMsg(),
                    "참가자 : " + roomsList.get(i).getNumberOfMembers() + " 명",
                    roomsList.get(i).getUploadTime().substring(0,2) + ":" + roomsList.get(i).getUploadTime().substring(2, 4));
        }
        init();
    }

    public void getRoomMemberList(ArrayList<Integer> memberList)
    {/*
        ArrayList<Integer> memberList = roomsList.get(i).getMembers();
        msg += "Room Member Count: " + memberList.size() + "\n";
        msg += "Room Member: \n";*/
    }

    private void welcomeMessage(){
        setting = getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

        try{
            if ((setting.getString("number", "default value").toString()).equals("1111")) {
                editor.putString("number", "2222"); // welcomeMessage를 한번만 실행시키기 위한 설정
                editor.commit();

                String userNickName = setting.getString("NickName", "default value").toString();
                alert = new AlertDialog.Builder(this); // welcome 표시창
                alert.setTitle("       안녕하세요~ (" + userNickName + ")님.");
                alert.setMessage("\n밥친구에 가입하신 것을 환영합니다.\n\n(등록하기)버튼으로 게시물을 등록하세요.\n");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton){
                        try{

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                alert.show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        con = new DBConnection(this);

        // 시간표를 입력 후 가입을 마치고 나서만 welcomeMessage method를 실행한다.
        welcomeMessage();

        try {
            con.requestAllRoomsList();
            ArrayList<Integer> friend = new ArrayList<Integer>();
        }
        catch(Exception e)
        {
            Log.d("debug", "no rooms exists");
        }
    }

    private void init()
    {
        send_to_enroll = new Intent(this, Main_content2.class);
        enroll = (Button)findViewById(R.id.Enroll);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(send_to_enroll);
            }
        });
        mListView = (ListView) findViewById(R.id.mList);
        mListView.setAdapter(mAdapter);

        /**
         * ListView의 item을 길게 클릭했을 경우.
         * 클릭된 item을 삭제한다.
         */
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                final int index = position;
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
                alertDlg.setTitle("게시물 삭제");

                // 'No' 버튼이 클릭되면
                alertDlg.setPositiveButton( "No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        dialog.dismiss();  // AlertDialog를 닫는다.

                    }
                });
                // 'Yes' 버튼이 클릭되면
                alertDlg.setNegativeButton( "Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        mAdapter.mListData.remove(index);

                        // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();  // AlertDialog를 닫는다.

                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });


                alertDlg.setMessage( String.format( "삭제하시겠습니까?", mAdapter.mListData.get(position)) );

                alertDlg.show();

                return true;

            }
        });

        /**
         * Short Click 이벤트 발생 시 참석하게 된다.
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData mData = mAdapter.mListData.get(position);
                Toast.makeText(getApplicationContext(), mData.name, Toast.LENGTH_SHORT).show();
            }
        });

        eat = (Button)findViewById(R.id.Eat);
        eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 밥먹자 list 초기화
                try {
              //      con.requestAllRoomsList();
                }
                catch(Exception e)
                {
                    Log.d("debug", "no rooms exists");
                }
            }
        });
    }

    private class ViewHolder{

        public ImageView mIcon;
        public TextView name;
        public TextView time_place;
        public TextView content;
        public TextView member;
        public TextView date;
    }


    private class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext){
            super();
            this.mContext = mContext;
        }

        public void addItem(Drawable icon, String Name, String Time_Place, String Content, String Member, String Date){
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mIcon = icon; // Box Image
            addInfo.name = Name;
            addInfo.time_place = Time_Place;
            addInfo.content = Content;
            addInfo.member= Member;
            addInfo.date = Date;

            mListData.add(addInfo);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return mListData.size();
        }

        @Override
        public Object getItem(int position){
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            ViewHolder holder;

            if(convertView == null){
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, null);

                holder.mIcon = (ImageView)convertView.findViewById(R.id.mImage);
                holder.name = (TextView)convertView.findViewById(R.id.name);
                holder.time_place = (TextView)convertView.findViewById(R.id.time_place);
                holder.content = (TextView)convertView.findViewById(R.id.content);
                holder.member = (TextView)convertView.findViewById(R.id.member);
                holder.date= (TextView)convertView.findViewById(R.id.mDate);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            ListData mData = mListData.get(position);

            if(mData.mIcon != null){
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            }else{
                holder.mIcon.setVisibility(View.GONE);
            }

            holder.name.setText(mData.name);
            holder.time_place.setText(mData.time_place);
            holder.content.setText(mData.content);
            holder.member.setText(mData.member);
            holder.date.setText(mData.date);

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
