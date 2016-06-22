
package com.example.jooma.bobchingu;

import java.util.ArrayList;

/**
 * Created by jooma on 2016-05-11.
 */
public class RoomInfo {
    private int mId;
    private int mMaster;
    private String mMasterName;
    private String mMsg;
    private String mLocation;
    private int mTime;
    private int mUploadTime;
    private int mNumberOfMemberlist;
    private ArrayList<Integer> mMemberList;

    // Constructor Methods
    public RoomInfo()
    {
        mId = -1;
        mMaster = -1;
        mMasterName = null;
        mMsg = null;
        mLocation = null;
        mTime = -1;
        mUploadTime = -1;
        mNumberOfMemberlist = 0;
        mMemberList = new ArrayList<Integer>();
    }

    public RoomInfo(int master, String msg, String location, int time)
    {
        this(-1, master, msg, location, time, -1);
    }

    public RoomInfo(int id, int master, String msg, String location, int time, int uptime, String masterName, int numberOfMembers) {
        this();
        mId = id;
        mMaster = master;
        mMsg = msg;
        mLocation = location;
        mTime = time;
        mUploadTime = uptime;
        mMasterName = masterName;
    }

    public RoomInfo(int id, int master, String msg, String location, int time, int uptime) {
        this();

        mId = id;
        mMaster = master;
        mMsg = msg;
        mLocation = location;
        mTime = time;
        mUploadTime = uptime;
    }

    // Setter Methods.
    public void setId(int id)
    {
        mId = id;
    }

    public void setMaster(int master)
    {
        mMaster = master;
    }

    public void setMasterName(String name)
    {
        mMasterName = name;
    }

    public void setMsg(String msg)
    {
        mMsg = msg;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public void setTime(int time) {
        mTime = time;
    }

    public void setUploadTime(int uploadTime) {
        mUploadTime = uploadTime;
    }

    public void setMemberList(ArrayList<Integer> memberList) {
        mMemberList = memberList;
    }

    // Getter Methods.
    public int getId()
    {
        return mId;
    }

    public int getMaster()
    {
        return mMaster;
    }

    public String getMasterName()
    {
        return mMasterName;
    }

    public String getMsg()
    {
        return mMsg;
    }

    public String getLocation()
    {
        return mLocation;
    }

    public String getTime() {
        return timeToString(mTime);
    }

    public String getUploadTime() {
        return timeToString(mUploadTime);
    }

    public int getNumberOfMembers() {
        return mMemberList.size();
    }

    private String timeToString(int t)
    {
        int hour = t / 100;
        String hh;
        if (hour < 10)
            hh = "0" + Integer.toString(hour);
        else
            hh = Integer.toString(hour);

        int minute = t % 100;
        String mm;
        if (minute < 10)
            mm = "0" + Integer.toString(minute);
        else
            mm = Integer.toString(minute);

        return hh + mm;
    }
}
