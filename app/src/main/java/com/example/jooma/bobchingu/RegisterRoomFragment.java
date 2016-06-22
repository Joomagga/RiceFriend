package com.example.jooma.bobchingu;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterRoomFragment extends Fragment {

    final String LOG_TAG = "logd, RegRomFrament";
    Button confirm;
    SyncServerService con;
    EditText[] time = new EditText[2]; // int형
    EditText place; // string
    EditText content; // string

    public RegisterRoomFragment() {
        // Required empty public constructor
        con = new SyncServerService(getActivity());
        con.updateSQLiteDb();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_TAG, "onCreateView()");
        return inflater.inflate(R.layout.fragment_register_room, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "onActivityCreate()");
        time[0] = (EditText) getActivity().findViewById(R.id.Time0);
        time[1] = (EditText) getActivity().findViewById(R.id.Time1);

        place = (EditText) getActivity().findViewById(R.id.Place);
        content = (EditText) getActivity().findViewById(R.id.Content);

        confirm = (Button) getActivity().findViewById(R.id.Confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TestAndConfirm();
            }
        });
    }

    private void TestAndConfirm() {

        int test1 = 0, test2 = 0; // Test the time.
        int test3 = 0; // Test the empty content in 장소
        int n1, n2;  // 시간, 분

        try {
            n1 = Integer.parseInt(time[0].getText().toString());
            n2 = Integer.parseInt(time[1].getText().toString());
            String time_re = Integer.toString(n1 * 100 + n2);

            if (0 <= n1 && n1 < 24) test1 = 1;
            if (0 <= n2 && n2 < 60) test2 = 1;
            if (!place.getText().toString().equals("")) test3 = 1;

            if (test1 * test2 * test3 == 1) {
                Toast.makeText(getActivity().getApplicationContext(), "등록되었습니다." +
                        Integer.parseInt(time_re), Toast.LENGTH_SHORT).show();
                con.registerRoom(content.getText().toString(),
                        place.getText().toString(), Integer.parseInt(time_re));
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.showFragment(mainActivity.ROOMLIST_FRAGMENT);
            } else if (test1 == 0 || test2 == 0) {
                Toast.makeText(getActivity().getApplicationContext(), "(시/분)을 확인해주세요.", Toast.LENGTH_SHORT).show();
            } else if (test3 == 0) {
                Toast.makeText(getActivity().getApplicationContext(), "장소가 비어있습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "입력란을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
