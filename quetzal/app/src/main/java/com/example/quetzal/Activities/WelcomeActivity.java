package com.example.quetzal.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.example.quetzal.R;

public class WelcomeActivity extends Activity {

    public static MediaPlayer start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = MediaPlayer.create(this, R.raw.start);
        setContentView(R.layout.activity_welcome);
        Button choose = (Button) findViewById(R.id.Start);
        choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                start.start();
                Intent intent = new Intent(WelcomeActivity.this,
                        MenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
