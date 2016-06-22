package com.example.jooma.bobchingu;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by jooma on 2016-06-20.
 */
public class RoomlistView {
    // 아이콘
    public Drawable mIcon;
    private boolean mChecked = false;

    private RoomInfo mRoom;

    // 알파벳 이름으로 정렬
    public static final Comparator<RoomlistView> ALPHA_COMPARATOR = new Comparator<RoomlistView>() {

        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(RoomlistView mListDate_1, RoomlistView mListDate_2) {
            return sCollator.compare(mListDate_1.getRoom().getMasterName(), mListDate_2.getRoom().getMasterName());
        }
    };

    public void setRoom(RoomInfo room) {
        mRoom = room;
    }

    public RoomInfo getRoom() {
        return mRoom;
    }

    public void setCheck(boolean check) {
        mChecked = check;
    }

    public boolean getCheck() {
        return mChecked;
    }
}
