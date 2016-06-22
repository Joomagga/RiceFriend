package com.example.jooma.bobchingu;

//import android.support.v4.app.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

public class RoomListFragment extends Fragment {
    private static final String LOG_TAG = "logd, RoomListFragment";

    Button btnEat, btnEnroll;
    SyncServerService con;
    Intent send_to_enroll;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    SharedPreferences setting;
    AlertDialog.Builder alert;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView is executing..");
        return inflater.inflate(R.layout.fragment_room_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "RoomListFragment is created.");

        // 시간표를 입력 후 가입을 마치고 나서만 welcomeMessage method를 실행한다.
        welcomeMessage();

        con = new SyncServerService(getActivity());
        con.updateSQLiteDb();
        ArrayList<RoomInfo> roomlist = con.getRoomList();

        mAdapter = new ListViewAdapter(getActivity());
        Log.d(LOG_TAG, "before add listView");
        for (int i = 0; i < roomlist.size(); i++) {
            mAdapter.addItem(getResources().getDrawable(R.drawable.uncheck), roomlist.get(i));
        }
        Log.d(LOG_TAG, "after add listView");
        init();
    }

    private void welcomeMessage() {
        setting = getActivity().getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

        try {
            if ((setting.getString("number", "default value").toString()).equals("1111")) {
                editor.putString("number", "2222"); // welcomeMessage를 한번만 실행시키기 위한 설정
                editor.commit();

                String userNickName = setting.getString("NickName", "default value").toString();
                alert = new AlertDialog.Builder(getActivity()); // welcome 표시창
                alert.setTitle("       안녕하세요~ (" + userNickName + ")님.");
                alert.setMessage("\n밥친구에 가입하신 것을 환영합니다.\n\n(등록하기)버튼으로 게시물을 등록하세요.\n");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                alert.show();
                Log.d(LOG_TAG, "welcomeMessage() is finished.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        Log.d(LOG_TAG, "init() is started.");
        send_to_enroll = new Intent(getActivity(), RegisterRoomActivity.class);
        btnEnroll = (Button) getActivity().findViewById(R.id.Enroll);
        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(send_to_enroll);
            }
        });
        Log.d(LOG_TAG, "setting listview in init()");
        mListView = (ListView) getActivity().findViewById(R.id.mList);
        mListView.setAdapter(mAdapter);
        Log.d(LOG_TAG, "setting adapter listview in init()");
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                final int index = position;
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
                alertDlg.setTitle("게시물 삭제");

                // 'No' 버튼이 클릭되면
                alertDlg.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();  // AlertDialog를 닫는다.

                    }
                });
                // 'Yes' 버튼이 클릭되면
                alertDlg.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.mListData.remove(index);

                        // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();  // AlertDialog를 닫는다.

                        Toast.makeText(getActivity().getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                con.deleteRoomMember(mAdapter.mListData.get(position).getRoom().getId(), Integer.parseInt(con.getMyPhoneNumber()));
                alertDlg.setMessage(String.format("삭제하시겠습니까?", mAdapter.mListData.get(position)));
                alertDlg.show();

                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoomlistView mData = mAdapter.mListData.get(position);
                if (mData.getCheck()) {
                    mData.setCheck(false);
                    mData.mIcon = getResources().getDrawable(R.drawable.uncheck);
                    con.addRoomMember(mAdapter.mListData.get(position).getRoom().getId(), Integer.parseInt(con.getMyPhoneNumber()));
                    Toast.makeText(getActivity().getApplicationContext(), "퇴장하셨습니다", Toast.LENGTH_SHORT).show();
                } else {
                    mData.setCheck(true);
                    mData.mIcon = getResources().getDrawable(R.drawable.check);
                    getView().invalidate();
                    con.addRoomMember(mAdapter.mListData.get(position).getRoom().getId(), Integer.parseInt(con.getMyPhoneNumber()));
                    Toast.makeText(getActivity().getApplicationContext(), "참가하셨습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.d(LOG_TAG, "finished to add listner at listview");
        btnEat = (Button) getActivity().findViewById(R.id.Eat);
        btnEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 밥먹자 list 초기화
                try {
                    //      con.requestAllRoomsList();
                } catch (Exception e) {
                    Log.d("debug", "no rooms exists");
                }
            }
        });

        Log.d(LOG_TAG, "init() is finished.");
    }

    private class ViewHolder {

        public ImageView mIcon;
        public TextView name;
        public TextView time;
        public TextView place;
        public TextView content;
        public TextView member;
        public TextView date;
    }


    private class ListViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private ArrayList<RoomlistView> mListData = new ArrayList<RoomlistView>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public void addItem(Drawable icon, RoomInfo room) {
            RoomlistView addInfo = new RoomlistView();
            addInfo.mIcon = icon; // Box Image
            addInfo.setRoom(room);

            mListData.add(addInfo);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            Log.d(LOG_TAG, "within getView()");
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, null);

                holder.mIcon = (ImageView) convertView.findViewById(R.id.mImage);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.place = (TextView) convertView.findViewById(R.id.location);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.member = (TextView) convertView.findViewById(R.id.member);
                holder.date = (TextView) convertView.findViewById(R.id.mDate);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Log.d(LOG_TAG, "within getView() after if statements");

            RoomlistView mData = mListData.get(position);

            if (mData.mIcon != null) {
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            } else {
                holder.mIcon.setVisibility(View.GONE);
            }

            Log.d(LOG_TAG, "before setting holder within getView()");

            holder.name.setText("방장: " + mData.getRoom().getMasterName());
            holder.time.setText("시간: " + mData.getRoom().getTime().substring(0, 2) + ":" + mData.getRoom().getTime().substring(2, 4));
            holder.place.setText("장소: " + mData.getRoom().getLocation());
            holder.content.setText("내용: " + mData.getRoom().getMsg());
            holder.member.setText("인원: " + mData.getRoom().getNumberOfMembers());
            holder.date.setText(mData.getRoom().getUploadTime().substring(0, 2) + ":" + mData.getRoom().getUploadTime().substring(2, 4));

            return convertView;
        }
    }
}