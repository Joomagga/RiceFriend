package com.example.jooma.bobchingu;

import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by walk1 on 2016-05-07.
 * The Socket Program based on BobChinGu.
 *
 * Methods:
 * public DBConnection(DBResponse): class which uses requires to implement DBResponse.
 * public void requestAllRoomList: it
 */

public class DBConnection {
    public DBResponse deligate = null;
    private String url_s = "http://joomagga.cafe24.com/";   // url for connection.
    private String getRooms = "getRooms.php";
    private String getRoomMember = "getRoomMember.php";
    private String addRoom = "addRoom.php";
    private String addMember = "addRoomMember.php";
    private String deleteRoomMember = "deleteRoomMember.php";

    public DBConnection(DBResponse context)
    {
        this.deligate = context;
    }

    public void makeRoom(RoomInfo room)
    {
        class MakeRoom extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    String query = URLEncoder.encode("master", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                    query += "&" + URLEncoder.encode("msg", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                    query += "&" + URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
                    query += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");
                    query += "&" + URLEncoder.encode("uptime", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8");

                    URL url = new URL(url_s+addRoom);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch (Exception e)
                {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        MakeRoom task = new MakeRoom();
        String time = new SimpleDateFormat("HHmm").format(new Date(System.currentTimeMillis()));
        task.execute(room.getMaster()+"", room.getMsg(), room.getLocation(), room.getTime()+"", time);
    }

    public void requestAllRoomsList() throws NoRoomException
    {
        class AccessDatabase extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    URL url = new URL(url_s+getRooms);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    /* read server response */
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line+"\n");
                        break;
                    }
                    return sb.toString().trim();
                }
                catch (IOException e)
                {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result)
            {
                ArrayList<RoomInfo> roomsList = JSON2ArrayListRoomInfo(result);
                deligate.getRoomList(roomsList);
            }
        }

        AccessDatabase thread = new AccessDatabase();
        thread.execute();
    }

    public void requestRoomMember(int room)
    {
        class AccessDatabase extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                deligate.getRoomMemberList(JSON2MemberList(s));
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    String query = URLEncoder.encode("room_id", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");

                    URL url = new URL(url_s+getRoomMember);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch (Exception e)
                {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        AccessDatabase thread = new AccessDatabase();
        thread.execute(room+"");
    }

    public void requestRoomMember(RoomInfo room)
    {
        this.requestRoomMember(room.getId());
    }

    public void addRoomMember(int room, int newMember)
    {
        class AddRoomMember extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    Log.d("debug", "Line A");
                    String query = URLEncoder.encode("room_id", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                    query += "&" + URLEncoder.encode("room_member", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                    URL url = new URL(url_s+addMember);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch (Exception e)
                {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        Log.d("debug", "Line B");
        AddRoomMember task = new AddRoomMember();
        task.execute(room+"", newMember+"");
    }

    public void addRoomMember(RoomInfo room, int newMember)
    {
        this.addRoomMember(room.getId(), newMember);
    }

    public void deleteRoomMember(int room, int member)
    {
        class AccessDatabase extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    String query = URLEncoder.encode("room_id", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                    query += "&" + URLEncoder.encode("room_member", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                    URL url = new URL(url_s+deleteRoomMember);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch (Exception e)
                {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        AccessDatabase task = new AccessDatabase();
        task.execute(room+"", member+"");
    }

    public void deleteRoomMember(RoomInfo room, int member)
    {
        this.deleteRoomMember(room.getId(), member);
    }

    private ArrayList<RoomInfo> JSON2ArrayListRoomInfo(String data)
    {
        ArrayList<RoomInfo> roomsList = new ArrayList<RoomInfo>();
        try {
            JSONObject jsonObj = new JSONObject(data);
            JSONArray rooms = jsonObj.getJSONArray("result");
            if (rooms.length() == 0)
                throw (new NoRoomException());
            for (int i = 0; i < rooms.length(); i++) {
                JSONObject c = rooms.getJSONObject(i);
                RoomInfo room = new RoomInfo(c.getString("id"), c.getString("master"), c.getString("msg"), c.getString("location"), c.getString("time"), c.getString("uptime"), c.getString("name"), c.getString("count"));
                roomsList.add(room);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return roomsList;
        }
    }

    private ArrayList<Integer> JSON2MemberList(String data)
    {
        ArrayList<Integer> memberList = null;

        try {
            JSONObject jsonObj = new JSONObject(data);
            JSONArray members = jsonObj.getJSONArray("result");

            memberList = new ArrayList<Integer>();

            for (int i = 0; i < members.length(); i++) {
                JSONObject c = members.getJSONObject(i);
                memberList.add(c.getInt("room_member"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return memberList;
        }
    }
}