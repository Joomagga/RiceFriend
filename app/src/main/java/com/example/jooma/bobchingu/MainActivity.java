package com.example.jooma.bobchingu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DBResponse {

    DBConnection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        con = new DBConnection(this);
        // con.makeRoom(new RoomInfo(1027204170, "please work", "Gachon", 0400));

        con.deleteRoomMember(23, 1099683324);
        con.requestRoomMember(23);
    }

    public void getRoomList(ArrayList<RoomInfo> roomsList)
    {
    }

    public void getRoomMemberList(ArrayList<Integer> memberList)
    {
        TextView tv = (TextView) findViewById(R.id.tv);
        String member = "Member List \n";

        for (int i=0; i<memberList.size(); i++)
            member = member + memberList.get(i) + "\n";

        tv.setText(member);
    }
}