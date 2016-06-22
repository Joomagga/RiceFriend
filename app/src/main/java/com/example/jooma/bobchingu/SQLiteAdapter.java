package com.example.jooma.bobchingu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by jooma on 2016-06-12.
 */
public class SQLiteAdapter {

    final String LOG_TAG = "logd, SQLiteAdapter";
    // Database Setting
    static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private DatabaseHelper helper;
    private Context context;
    private int phone_number;
    private String TAG = "debug";
    static final String DATABASE_NAME = "database";

    // UserList Table Schema
    static final String USERLIST_TABLE_NAME = "userlist";
    static final String USERLIST_SCHEMA_PHONE_NUMBER = "phone_number";
    static final String USERLIST_SCHEMA_NAME = "name";
    static final String USERLIST_SCHEMA_TIMETABLE = "time_table";
    static final String USERLIST_TABLE_CREATE = "create table " + USERLIST_TABLE_NAME
            + "(" + USERLIST_SCHEMA_PHONE_NUMBER + " decimal(11,0), " + USERLIST_SCHEMA_NAME + " char(20)" + ");";

    // Room Table Schema
    static final String ROOM_TABLE_NAME = "room";
    static final String ROOM_SCHEMA_ROOM_ID = "room_id";
    static final String ROOM_SCHEMA_MASTER = "master";
    static final String ROOM_SCHEMA_MESSAGE = "msg";
    static final String ROOM_SCHEMA_LOCATION = "location";
    static final String ROOM_SCHEMA_TIME = "time";
    static final String ROOM_SCHEMA_UPDATE_TIME = "uptime";
    static final String ROOM_TABLE_CREATE = "create table " + ROOM_TABLE_NAME
            + "(" + ROOM_SCHEMA_ROOM_ID + " double, " + ROOM_SCHEMA_MASTER + " decimal(11,0), "
            + ROOM_SCHEMA_MESSAGE + " char(50), " + ROOM_SCHEMA_LOCATION + " char(50), "
            + ROOM_SCHEMA_TIME + " decimal(4,0), " + ROOM_SCHEMA_UPDATE_TIME + " decimal(4,0)" + ");";

    // Room Member Table Schema
    static final String ROOM_MEMBER_TABLE_NAME = "room_member";
    static final String ROOM_MEMBER_SCHEMA_ROOM_ID = "room_id";
    static final String ROOM_MEMBER_SCHEMA_MEMBER = "room_member";
    static final String ROOM_MEMBER_TABLE_CREATE = "create table " + ROOM_MEMBER_TABLE_NAME
            + "(" + ROOM_MEMBER_SCHEMA_ROOM_ID + " double, " + ROOM_MEMBER_SCHEMA_MEMBER + " int(11)" + ");";

    // Constructor Methods
    public SQLiteAdapter(Context context) {
        this.context = context;
        this.helper = new DatabaseHelper(context);

        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        this.phone_number = Integer.parseInt(tMgr.getLine1Number().substring(3));   // erase '+82'
        Log.d(TAG, "SQLiteAdapter is constructed.");
    }

    // Generic Methods
    public SQLiteAdapter open() throws SQLException {
        db = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public Cursor getRommListCursor() throws SQLException {
        Cursor cursor = db.query(true, ROOM_TABLE_NAME + " join " + USERLIST_TABLE_NAME, new String[]{ROOM_SCHEMA_ROOM_ID, ROOM_SCHEMA_MASTER, ROOM_SCHEMA_MESSAGE,
                ROOM_SCHEMA_LOCATION, ROOM_SCHEMA_TIME, ROOM_SCHEMA_UPDATE_TIME, USERLIST_SCHEMA_NAME}, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getRoomMemberListCursor(int id) throws SQLException {
        Cursor cursor = db.query(true, ROOM_MEMBER_TABLE_NAME, new String[]{ROOM_MEMBER_SCHEMA_ROOM_ID, ROOM_MEMBER_SCHEMA_MEMBER}, ROOM_MEMBER_SCHEMA_ROOM_ID + "=" + id + "", null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void deleteUserlist() {
        db.execSQL("delete from " + USERLIST_TABLE_NAME + ";");
        Log.d(TAG, USERLIST_TABLE_NAME + "is deleted.");
    }

    public void insertUserlist(int number, String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(USERLIST_SCHEMA_PHONE_NUMBER, number);
        initialValues.put(USERLIST_SCHEMA_NAME, name);
        db.insert(USERLIST_TABLE_NAME, null, initialValues);
    }

    public void insertRoomlist(RoomInfo room) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROOM_SCHEMA_ROOM_ID, room.getId());
        initialValues.put(ROOM_SCHEMA_MASTER, room.getMaster());
        initialValues.put(ROOM_SCHEMA_MESSAGE, room.getMsg());
        initialValues.put(ROOM_SCHEMA_LOCATION, room.getLocation());
        initialValues.put(ROOM_SCHEMA_TIME, room.getTime());
        initialValues.put(ROOM_SCHEMA_UPDATE_TIME, room.getUploadTime());
        db.insert(ROOM_TABLE_NAME, null, initialValues);
    }

    public void insertRoomMember(int id, int member) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROOM_MEMBER_SCHEMA_ROOM_ID, id);
        initialValues.put(ROOM_MEMBER_SCHEMA_MEMBER, member);
        Log.d(LOG_TAG, member + " has entered into " + id);
        db.insert(ROOM_MEMBER_TABLE_NAME, null, initialValues);
    }

    public void dropDatabase() {
        db.execSQL("drop table if exists " + USERLIST_TABLE_NAME);
        db.execSQL("drop table if exists " + ROOM_TABLE_NAME);
        db.execSQL("drop table if exists " + ROOM_MEMBER_TABLE_NAME);
        db.execSQL(USERLIST_TABLE_CREATE);
        db.execSQL(ROOM_TABLE_CREATE);
        db.execSQL(ROOM_MEMBER_TABLE_CREATE);
    }

    // InnerClass OpenHelper
    private static class DatabaseHelper extends SQLiteOpenHelper {
        final String TAG = "debug";

        public DatabaseHelper(Context con) {
            super(con, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.d(TAG, "userlist query (" + USERLIST_TABLE_CREATE + ")");
                db.execSQL(USERLIST_TABLE_CREATE);
                db.execSQL(ROOM_TABLE_CREATE);
                db.execSQL(ROOM_MEMBER_TABLE_CREATE);
                Log.d(TAG, "created database (" + DATABASE_NAME + ")");
            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "Upgrading database from " + oldVersion + " to " + newVersion);
            db.execSQL("drop table if exists" + USERLIST_TABLE_NAME);
            db.execSQL("drop table if exists" + ROOM_TABLE_NAME);
            db.execSQL("drop table if exists" + ROOM_MEMBER_TABLE_NAME);
            onCreate(db);
        }
    }
}
