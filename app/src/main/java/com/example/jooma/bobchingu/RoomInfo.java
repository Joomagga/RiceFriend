package com.example.jooma.bobchingu;

import java.util.ArrayList;

/**
 * Created by jooma on 2016-05-11.
 */
public class RoomInfo {
    private int id;
    private int master;
    private String msg;
    private String location;
    private int time;
    private int uptime;
    private ArrayList<Integer> memberList;

    public RoomInfo(int master, String msg, String location, int time, int uptime)
    {
        this.master = master;
        this.msg = msg;
        this.location = location;
        this.time = time;
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

    public RoomInfo(String id, String master, String msg, String location, String time, String uptime)
    {
        this.id = Integer.parseInt(id);
        this.master = Integer.parseInt(master);
        this.msg = msg;
        this.location = location;
        this.time = Integer.parseInt(time);
        this.uptime = Integer.parseInt(uptime);
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

    public int getId()
    {
            return id;
    }

    public int getMaster()
    {
        return master;
    }

    public String getMsg()
    {
        return msg;
    }

    public String getLocation()
    {
        return location;
    }

    public int getTime()
    {
        return time;
    }

    public int getUploadTime()
    {
        return uptime;
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
