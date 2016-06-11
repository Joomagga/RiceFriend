package com.example.jooma.bobchingu;

import android.graphics.drawable.Drawable;
import java.text.Collator;
import java.util.Comparator;

/**
 * Created by Hyunwoo on 2016-05-09.
 */
public class ListData {

    // 아이콘
    public Drawable mIcon;

    // 이름
    public String name;

    // 시간_장소
    public String time_place;

    // 내용
    public String content;

    // 참가자
    public String member;

    // 업로드 날짜
    public String date;

    // 알파벳 이름으로 정렬
    public static final Comparator<ListData>  ALPHA_COMPARATOR = new Comparator<ListData>() {

        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ListData mListDate_1, ListData mListDate_2) {
            return sCollator.compare(mListDate_1.name, mListDate_2.name);
        }
    };
}
