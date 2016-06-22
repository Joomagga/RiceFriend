package com.example.jooma.bobchingu;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SyncServerService extends Service {

    private final String URL = "http://joomagga.cafe24.com/";   // url for connection.
    private final String GET_DATABASE = "getUserlistLite.php";
    private final String GET_ROOMS = "getRooms.php";
    private final String GET_ROOM_MEMBERS = "getRoomMember.php";
    private final String ADD_ROOM = "addRoom.php";
    private final String ADD_MEMBER = "addRoomMember.php";
    private final String DELETE_ROOM_MEMBER = "deleteRoomMember.php";
    private final String ADD_FRIENDSHIP = "addFriendship.php";
    private final String ENTER_NEW_MEMBER = "enterMember.php";
    private final String LOG_TAG = "logd, SyncServerService";

    private final Context mContext; // context.
    private final SQLiteAdapter mDb;    // SQLite Database.

    public SyncServerService(Context context) {
        mContext = context;
        mDb = new SQLiteAdapter(mContext);
        try {
            mDb.open();
        } catch (Exception e) {
            Log.d(LOG_TAG, "SQLiteAdapter's objects' open() doesn't work.");
        }
    }

    public void sendContactsToServer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String friender = getMyPhoneNumber();
                ArrayList<Integer> friendee = getContactsFromDevice();

                for (int i = 0; i < friendee.size(); i++) {
                    sendContactToServer(friender, Integer.toString(friendee.get(i)));
                }
                Log.d(LOG_TAG, "sendContactsToServer() is finished.");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void registerName(String phone_number, String name) {
        final String param1 = phone_number;
        final String param2 = name;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String query = URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(param1, "UTF-8");
                    query += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(param2, "UTF-8");

                    URL url = new URL(URL + ENTER_NEW_MEMBER);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "registerName() has an error");
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void registerName(int phone_number, String name) {
        registerName(Integer.toString(phone_number), name);
    }

    public void registerRoom(RoomInfo room) {
        registerRoom(room.getMsg(), room.getLocation(), room.getTime());
    }

    public void registerRoom(String msg, String location, String time) {
        registerRoom(msg, location, Integer.parseInt(time));
    }

    public void registerRoom(String msg, String location, int time) {
        final String param1 = getMyPhoneNumber();
        final String param2 = msg;
        final String param3 = location;
        final String param4 = Integer.toString(time);
        final String param5 = getTime();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String query = URLEncoder.encode("master", "UTF-8") + "=" + URLEncoder.encode(param1, "UTF-8");
                    query += "&" + URLEncoder.encode("msg", "UTF-8") + "=" + URLEncoder.encode(param2, "UTF-8");
                    query += "&" + URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(param3, "UTF-8");
                    query += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(param4, "UTF-8");
                    query += "&" + URLEncoder.encode("uptime", "UTF-8") + "=" + URLEncoder.encode(param5, "UTF-8");

                    URL url = new URL(URL + ADD_ROOM);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "registerRoom() has an error.");
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void addRoomMember(int roomId, int roomMember) {
        final String param1 = Integer.toString(roomId);
        final String param2 = Integer.toString(roomMember);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String query = URLEncoder.encode("room_id", "UTF-8") + "=" + URLEncoder.encode(param1, "UTF-8");
                    query += "&" + URLEncoder.encode("room_member", "UTF-8") + "=" + URLEncoder.encode(param2, "UTF-8");

                    URL url = new URL(URL + ADD_MEMBER);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "addRoomMember() has an error.");
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void deleteRoomMember(int roomId, int roomMember) {
        final String param1 = Integer.toString(roomId);
        final String param2 = Integer.toString(roomMember);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String query = URLEncoder.encode("room_id", "UTF-8") + "=" + URLEncoder.encode(param1, "UTF-8");
                    query += "&" + URLEncoder.encode("room_member", "UTF-8") + "=" + URLEncoder.encode(param2, "UTF-8");

                    URL url = new URL(URL + DELETE_ROOM_MEMBER);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "deleteRoomMember() has an error.");
                }
            }
        };
    }

    public ArrayList<RoomInfo> getRoomList() {
        Cursor cursor = null;
        try {
            cursor = mDb.getRommListCursor();
        } catch (SQLException e) {
            Log.d(LOG_TAG, e.getMessage());
        }

        int idCol = cursor.getColumnIndex(SQLiteAdapter.ROOM_SCHEMA_ROOM_ID);
        int masterCol = cursor.getColumnIndex(SQLiteAdapter.ROOM_SCHEMA_MASTER);
        int msgCol = cursor.getColumnIndex(SQLiteAdapter.ROOM_SCHEMA_MESSAGE);
        int locationCol = cursor.getColumnIndex(SQLiteAdapter.ROOM_SCHEMA_LOCATION);
        int timeCol = cursor.getColumnIndex(SQLiteAdapter.ROOM_SCHEMA_TIME);
        int uptimeCol = cursor.getColumnIndex(SQLiteAdapter.ROOM_SCHEMA_UPDATE_TIME);
        int nameCol = cursor.getColumnIndex(SQLiteAdapter.USERLIST_SCHEMA_NAME);

        ArrayList<RoomInfo> roomlist = new ArrayList<>();
        while (cursor.moveToNext()) {
            RoomInfo room = new RoomInfo();
            int id = cursor.getInt(idCol);
            room.setId(id);
            room.setMaster(cursor.getInt(masterCol));
            room.setMasterName(cursor.getString(nameCol));
            room.setMsg(cursor.getString(msgCol));
            room.setLocation(cursor.getString(locationCol));
            room.setTime(cursor.getInt(timeCol));
            room.setUploadTime(cursor.getInt(uptimeCol));
            room.setMemberList(getMemberList(id));
            roomlist.add(room);
        }
        return roomlist;
    }

    private ArrayList<Integer> getMemberList(int id) {
        Cursor cursor = null;
        try {
            cursor = mDb.getRoomMemberListCursor(id);
        } catch (SQLException e) {
            Log.d(LOG_TAG, e.getMessage());
        }

        int memberCol = cursor.getColumnIndex(SQLiteAdapter.ROOM_MEMBER_SCHEMA_MEMBER);

        ArrayList<Integer> memberlist = new ArrayList<>();
        while (cursor.moveToNext()) {
            memberlist.add(cursor.getInt(memberCol));
            Log.d(LOG_TAG, "memberlist added");
        }
        return memberlist;
    }

    public void updateUserlist(boolean useThread) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String query = URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(getMyPhoneNumber(), "UTF-8");
                    URL url = new URL(URL + GET_DATABASE);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    mDb.deleteUserlist();

                    JSONObject jsonObj = new JSONObject(line);
                    JSONArray members = jsonObj.getJSONArray("result");

                    for (int i = 0; i < members.length(); i++) {
                        JSONObject c = members.getJSONObject(i);
                        mDb.insertUserlist(c.getInt("phone_number"), c.getString("name"));
                        String value = c.getInt("phone_number") + "," + c.getString("name");
                        Log.d(LOG_TAG, value + " inserted into SQLite");
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "updateSQLite() has an error.");
                }
            }
        };

        Thread thread = new Thread(runnable);
        if (useThread)
            thread.start();
        else
            thread.run();
    }

    public void updateRoomlist(boolean useThread) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URL + GET_ROOMS);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    /* read server response */
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        break;
                    }
                    String result = sb.toString().trim();
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray rooms = jsonObj.getJSONArray("result");

                    for (int i = 0; i < rooms.length(); i++) {
                        JSONObject c = rooms.getJSONObject(i);
                        RoomInfo room = new RoomInfo(c.getInt("id"), c.getInt("master"), c.getString("msg"), c.getString("location"), c.getInt("time"), c.getInt("uptime"));
                        room.setMasterName(c.getString("name"));
                        mDb.insertRoomlist(room);
                        updateRoomMember(room.getId());
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "updateRoomlist() has an error.");
                }
            }
        };

        Thread thread = new Thread(runnable);
        if (useThread)
            thread.start();
        else
            thread.run();
    }

    private void updateRoomMember(int roomId) {
        final String param1 = Integer.toString(roomId);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String query = URLEncoder.encode("room_id", "UTF-8") + "=" + URLEncoder.encode(param1, "UTF-8");

                    URL url = new URL(URL + GET_ROOM_MEMBERS);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    String result = sb.toString().trim();
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray rooms = jsonObj.getJSONArray("result");

                    for (int i = 0; i < rooms.length(); i++) {
                        JSONObject c = rooms.getJSONObject(i);
                        mDb.insertRoomMember(Integer.parseInt(param1), c.getInt("room_member"));
                        Log.d(LOG_TAG, "memberlist " + c.getInt("room_member"));
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "updateRoomMember() has an error.");
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.run();
    }

    public void updateSQLiteDb() {
        //mDb.dropDatabase();
        updateRoomlist(true);
        updateUserlist(true);
    }

    private void sendContactToServer(String friender, String friendee) {
        final String param1 = friender;
        final String param2 = friendee;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String query = URLEncoder.encode("friender", "UTF-8") + "=" + URLEncoder.encode(param1, "UTF-8");
                    query += "&" + URLEncoder.encode("friendee", "UTF-8") + "=" + URLEncoder.encode(param2, "UTF-8");

                    URL url = new URL(URL + ADD_FRIENDSHIP);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(query);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "sendContactsServer() has an exception.");
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private ArrayList<Integer> getContactsFromDevice() {
        ArrayList<Integer> friend = new ArrayList<Integer>();
        ContentResolver cr = mContext.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        int phoneNumber = phoneNumberCompacter(contactNumber);
                        if (phoneNumber > 0)
                            friend.add(phoneNumber);
                    }
                    pCur.close();
                }
            } while (cursor.moveToNext());
        }

        Log.d(LOG_TAG, "getContactsFromDevice is finished.");
        return friend;
    }

    private int phoneNumberCompacter(String number) {
        if (number.substring(0, 3).equals("+82"))    // if number starts with '+82'
        {
            number = number.substring(3);
            Log.d("contactD", "(+82)" + number);
        } else
            number = number.substring(1);

        number = number.replaceAll("-", "");
        Log.d("contactD", "-" + number);

        double numberD = 0;

        // first converting
        if (number.length() == 10) {
            numberD = Double.parseDouble(number);
        } else    // if phone number has more than 10 digits.
            numberD = -1;

        // second converting
        if (1100000000 <= numberD || numberD < 1000000000)// if phone number has more than 10 digits.
            numberD = -1;

        int result = (int) numberD;
        return result;
    }

    @NonNull
    public String getMyPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(LOG_TAG, "getMyPhoneNumber() is running.");
        return tMgr.getLine1Number().substring(3);   // erase '+82'
    }

    public void updatePeriodically(int time) {

    }

    private String getTime() {
        String time = new SimpleDateFormat("HHmm").format(new Date(System.currentTimeMillis()));
        return time;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
