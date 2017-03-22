package com.example.quetzal.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quetzal.R;

import java.util.Locale;

import static android.content.ContentValues.TAG;

public class conversation extends Activity implements TextToSpeech.OnInitListener{
    private TextView text1;
    private TextToSpeech mTts;
    private int count = 0;
    private ImageView boy,girl;

    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mTts = new TextToSpeech(this,this); //TextToSpeech.OnInitListener
        text1 = (TextView)findViewById(R.id.text1);
        girl = (ImageView)findViewById(R.id.girl);
        boy = (ImageView)findViewById(R.id.boy);

        layout = (RelativeLayout)findViewById(R.id.activity_conversation);
        Resources res = getResources();
        final String[] array = res.getStringArray(R.array.conversation);
        final Button choose = (Button) findViewById(R.id.button);
        boy.setVisibility(View.INVISIBLE);
        choose.setVisibility(View.INVISIBLE);
        choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(conversation.this,
                        ChooseActivity.class);
                startActivity(intent);
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(count<8) {
                    text1.setTextSize(25);
                    text1.setText(array[count]);
                    if (count % 2 == 0) {
                        girl.setVisibility(View.VISIBLE);
                        boy.setVisibility(View.INVISIBLE);

                        mTts.speak(text1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else {
                        boy.setVisibility(View.VISIBLE);
                        girl.setVisibility(View.INVISIBLE);

                        mTts.speak(text1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                if(count == 8)
                {
                    boy.setVisibility(View.VISIBLE);
                    girl.setVisibility(View.VISIBLE);
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
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }

}
