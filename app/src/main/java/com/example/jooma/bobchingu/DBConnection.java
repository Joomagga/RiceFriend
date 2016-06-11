
package com.example.jooma.bobchingu;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
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
import java.sql.Array;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by walk1 on 2016-05-07.
 * The Socket Program based on BobChinGu.
 *
 * Methods:
 * public DBConnection(DBResponse): class which uses requires to implement DBResponse.
 * public void requestAllRoomList: it
 */

public class DBConnection {
    public final DBResponse deligate;
    private final String url_s = "http://joomagga.cafe24.com/";   // url for connection.
    private final String getRooms = "getRooms.php";
    private final String getRoomMember = "getRoomMember.php";
    private final String addRoom = "addRoom.php";
    private final String addMember = "addRoomMember.php";
    private final String deleteRoomMember = "deleteRoomMember.php";
    private final String addFriendship = "addFriendship.php";
    private final String enterNewMember = "enterMember.php";
    private final Context context;

    public DBConnection(Object context)
    {
        this.deligate = (DBResponse)context;
        this.context = (Context)context;
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
        task.execute(room.getMaster()+"", room.getMsg(), room.getLocation(), room.getTime(), time);
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

    public void enterMember(String phone_number, String name)
    {
        class EnterMember extends AsyncTask<String, Void, String>
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
                Log.d("DBConnection", "enterMember: " + s);
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    String query = URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                    query += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                    URL url = new URL(url_s+enterNewMember);
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

        EnterMember task = new EnterMember();
        task.execute(phone_number, name);
    }

    public void syncFriendship()
    {
        ArrayList<Integer> friend = new ArrayList<Integer>();
        ContentResolver cr = context.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        friend.add(string2Integer(contactNumber));
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext()) ;
        }

        class AddFriendship extends AsyncTask<String, Void, String>
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
                Log.d("dbg", s);
            }

            @Override
            protected String doInBackground(String... params)
            {
                try
                {
                    String query = URLEncoder.encode("friender", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                    query += "&" + URLEncoder.encode("friendee", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                    URL url = new URL(url_s+addFriendship);
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

        TelephonyManager tMgr = (TelephonyManager)this.context.getSystemService(Context.TELEPHONY_SERVICE);
        String friender = tMgr.getLine1Number().substring(3);   // erase '+82'
        Log.d("dbg", friender);

        int i=0;
        while (i<friend.size())
        {
            new AddFriendship().execute(friender, friend.get(i)+"");
            i = i + 1;
        }
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

    private int string2Integer(String number)
    {
        if (number.substring(0, 3).equals("+82"))    // if number starts with '+82'
        {
            number = number.substring(3);
            Log.d("contactD", "(+82)" + number);
        }
        else
            number = number.substring(1);
        number = number.replaceAll("-", "");
        Log.d("contactD", "-" + number);
        return Integer.parseInt(number);
    }
}