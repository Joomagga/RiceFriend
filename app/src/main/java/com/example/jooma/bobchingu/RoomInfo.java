
package com.example.jooma.bobchingu;

import java.util.ArrayList;

/**
 * Created by jooma on 2016-05-11.
 */
public class RoomInfo {
    private int id;
    private int master;
    private String name;
    private String msg;
    private String location;
    private int time;
    private int uptime;
    private int member;
    private ArrayList<Integer> memberList;

    public RoomInfo(int master, String msg, String location, int time, int uptime)
    {
        this.master = master;
        this.msg = msg;
        this.location = location;
        this.time = time;
        this.uptime = uptime;
    }

    public RoomInfo(String master, String msg, String location, String time)
    {
        this.master = Integer.parseInt(master);
        this.msg = msg;
        this.location = location;
        this.time = Integer.parseInt(time);
    }

    public RoomInfo(int master, String msg, String location, int time)
    {
        this.master = master;
        this.msg = msg;
        this.location = location;
        this.time = time;
    }

    public RoomInfo(String id, String master, String msg, String location, String time, String uptime, String name, String member)
    {
        this.id = Integer.parseInt(id);
        this.master = Integer.parseInt(master);
        this.msg = msg;
        this.location = location;
        this.time = Integer.parseInt(time);
        this.uptime = Integer.parseInt(uptime);
        this.name = name;
        this.member = Integer.parseInt(member);
    }

    public RoomInfo(String id, String master, String msg, String location, String time, String uptime, ArrayList<Integer> members)
    {
        this.id = Integer.parseInt(id);
        this.master = Integer.parseInt(master);
        this.msg = msg;
        this.location = location;
        this.time = Integer.parseInt(time);
        this.uptime = Integer.parseInt(uptime);
        this.memberList = members;
    }

    public String getNumberOfMembers()
    {
        return Integer.toString(this.member);
    }

    public int getId()
    {
        return id;
    }

    public int getMaster()
    {
        return master;
    }

    public String getMasterName()
    {
        return name;
    }

    public String getMsg()
    {
        return msg;
    }

    public String getLocation()
    {
        return location;
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
    public String getTime()
    {
        return timeToString(time);
    }

    public String getUploadTime()
    {
        return timeToString(uptime);
    }

    public ArrayList<Integer> getMembers()
    {
        return memberList;
    }

    public void setMemberList(ArrayList<Integer> newList)
    {
        this.memberList = newList;
    }

    public void addMember(int member)
    {
        memberList.add(member);
    }
}
