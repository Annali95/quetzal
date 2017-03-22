package com.example.quetzal.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quetzal.R;

import java.util.Locale;

import static android.content.ContentValues.TAG;

public class question extends Activity implements TextToSpeech.OnInitListener{
    LinearLayout layout;
    LinearLayout l1,l2,l3,l4,l5,l6,l7,l8;
    private TextView t1,t2,t3,t4,t5,t6,t7,t8;
    private TextToSpeech mTts;



    RelativeLayout rl1;
    int count =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        mTts = new TextToSpeech(this,this); //TextToSpeech.OnInitListener

        final Button choose = (Button) findViewById(R.id.button2);
        choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(question.this,
                        ChooseActivity.class);
                startActivity(intent);
            }
        });

        choose.setVisibility(View.INVISIBLE);
        rl1 = (RelativeLayout)findViewById(R.id.activity_question);

        layout = (LinearLayout)findViewById(R.id.layout);
         l1 = (LinearLayout)findViewById(R.id.l1);
         l2 = (LinearLayout)findViewById(R.id.l2);
         l3 = (LinearLayout)findViewById(R.id.l3);
         l4 = (LinearLayout)findViewById(R.id.l4);
         l5 = (LinearLayout)findViewById(R.id.l5);
         l6 = (LinearLayout)findViewById(R.id.l6);
         l7 = (LinearLayout)findViewById(R.id.l7);
         l8 = (LinearLayout)findViewById(R.id.l8);
        t1 = (TextView) findViewById(R.id.textView1);
        t2 = (TextView) findViewById(R.id.textView2);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);
        t5 = (TextView) findViewById(R.id.textView5);
        t6 = (TextView) findViewById(R.id.textView6);
        t7 = (TextView) findViewById(R.id.textView7);
        t8 = (TextView) findViewById(R.id.text);


        final TextView textarray[]= {t1,t2,t3,t4,t5,t6,t7,t8};


        final LinearLayout linearray[]= {l1,l2,l3,l4,l5,l6,l7,l8};

        linearray[0].setVisibility(View.VISIBLE);

        for(int l=1; l<8; l++)
        {
            linearray[l].setVisibility(View.INVISIBLE);
        }

       layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(count<8)
                {
                    linearray[count].setVisibility(View.VISIBLE);
                    mTts.speak(textarray[count].getText().toString(),TextToSpeech.QUEUE_FLUSH,null);


                }
                if(count==8)
                {
                    choose.setVisibility(View.VISIBLE);

                }
                count++;
            }
        });

    }
    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Lanuage data is missing or the language is not supported.
                Log.e(TAG, "Language is not available.");
            } else {
                sayHello();
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }
    private void sayHello() {
        t1 = (TextView) findViewById(R.id.textView1);
        mTts.speak(t1.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
    }
}
