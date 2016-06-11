package com.example.jooma.bobchingu;

/**
 * Created by Hyunwoo on 2016-05-16.
 */
public class NoRoomException extends Exception {
    private String message = "NoRoomException: There is no room";

    public NoRoomException()
    {

    }

    public NoRoomException(String msg)
    {
        this.message = msg;
    }

    public String getMessage()
    {
        return this.message;
    }
}