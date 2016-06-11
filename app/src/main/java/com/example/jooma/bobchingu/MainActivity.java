package com.example.jooma.bobchingu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button login, join;
    Intent Go_Board, Go_Join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Go_Board = new Intent(this, Main_content.class);
        Go_Join = new Intent(this, Join.class);

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Go_Board);
            }
        });

        join = (Button)findViewById(R.id.join);
        join.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(Go_Join);
            }
        });
    }
}
