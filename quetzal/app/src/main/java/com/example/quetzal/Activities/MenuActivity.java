package com.example.quetzal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.example.quetzal.R;
import com.example.quetzal.recognize.LearnActivity;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button choose = (Button) findViewById(R.id.Menu1);
        choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
              //  WelcomeActivity.start.start();
                Intent intent = new Intent(MenuActivity.this,
                        conversation.class);
                startActivity(intent);

            }
        });
        Button learn = (Button) findViewById(R.id.learn);
        learn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
              //  WelcomeActivity.start.start();
                Intent intent = new Intent(MenuActivity.this,
                        LearnActivity.class);
                startActivity(intent);
            }
        });
    }
}
