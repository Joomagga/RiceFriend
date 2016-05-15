package com.example.jooma.bobchingu;

import java.util.ArrayList;

/**
 * Created by jooma on 2016-05-14.
 */
public interface DBResponse {
    void getRoomList(ArrayList<RoomInfo> data);
    void getRoomMemberList(ArrayList<Integer> data);
}
