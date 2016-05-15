package com.example.jooma.bobchingu;

/**
 * Created by jooma on 2016-05-14.
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
