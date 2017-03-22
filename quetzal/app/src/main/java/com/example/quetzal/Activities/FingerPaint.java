/*******************************************************************************
 * Created by liguoying on 1/13/17.
 ******************************************************************************/


package com.example.quetzal.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quetzal.R;
import com.example.quetzal.ambilwarna.AmbilWarnaDialog;
import com.example.quetzal.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import com.example.quetzal.drawpanel.DrawingPanel;
import com.example.quetzal.recognize.Const;
import com.example.quetzal.recognize.DrawView;
import com.example.quetzal.recognize.Paths;
import com.example.quetzal.recognize.Recognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;



public class FingerPaint extends Activity implements TextToSpeech.OnInitListener{
    private TextToSpeech mTts;
    RelativeLayout mainContainer,Container,rl1;
    private DrawView dv;
    TextView textview2;
    ImageView image1;
    Bitmap mBitmap;
    Button btnColor, btnUndo, btnFinish,reset1,recog1,goback_tap,goback_cicleitem,goback_main;
    Button retap ;
    Button done ;
	DrawingPanel drawView;
	int lastColor = 0xFFFF0000;
	public static final int SUCCESS = 200;
	private final String TAG = getClass().getSimpleName();
    int count = 0;
    int h,w;

    private int num = 150;
    public static SoundPool success,fail;




    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerpaint);
        mTts = new TextToSpeech(this,this); //TextToSpeech.OnInitListener
        File photo = new File(getIntent().getStringExtra(ChooseActivity.IMAGE_PATH));
        mBitmap = BitmapFactory.decodeFile(photo.getAbsolutePath());
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        w = metric.widthPixels;     // Get the width of the screen
        h = metric.heightPixels;   //
        success= new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        success.load(this,R.raw.success,1);
        fail= new SoundPool(5, AudioManager.STREAM_SYSTEM,0);
        fail.load(this,R.raw.fail,1);
        generateViews();

    }
    //Set buttons and drawing panels
    private void generateViews() {

        //Generate drawview and containers
        mainContainer = (RelativeLayout)findViewById(R.id.layout);

        rl1 = (RelativeLayout)findViewById(R.id.layout);

        Container = new RelativeLayout(this);
        image1 = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
        layoutParams.gravity= Gravity.CENTER;
        image1.setLayoutParams(layoutParams);
        image1.setBackgroundResource(R.drawable.icon2);

        //Character recognize panel
        dv=new DrawView(this);
        dv.setcolor(Color.BLUE);

        //Circle each item
        btnFinish = new Button(this);
        btnFinish.setTextSize(28);
        btnFinish.setText("Next");
        goback_main= new Button(this);
        goback_main.setTextSize(28);
        goback_main.setText("Back");
        btnColor = new Button(this);
        btnColor.setTextSize(28);
        btnColor.setText("Color");
        btnUndo = new Button(this);
        btnUndo.setTextSize(28);
        btnUndo.setText("Undo");

        //Add View on Circle each item
        LinearLayout Table = (LinearLayout)findViewById(R.id.table1);
        Table.addView(image1);
        Table.addView(btnColor);
        Table.addView(btnUndo);
        Table.addView(goback_main);
        Table.addView(btnFinish);

        //Tap and count
        goback_cicleitem = new Button(this);
        retap = new Button(this);
        done = new Button(this);
        goback_cicleitem.setTextSize(32);
        goback_cicleitem.setText("Back");
        retap.setTextSize(32);
        retap.setText("Redo");
        done.setTextSize(32);
        done.setText("Next");

        //Recognize
        reset1 = new Button(this);
        recog1 = new Button(this);
        goback_tap = new Button(this);

        goback_tap.setTextSize(32);
        goback_tap.setText("Back");

        reset1.setTextSize(32);
        reset1.setText("Redo");

        recog1.setTextSize(32);
        recog1.setText("Next");

        //Set drawingPanel here
        drawView = new DrawingPanel(this, lastColor);
        mBitmap = rotateImage(mBitmap, 180, mBitmap.getWidth(),mBitmap.getHeight());
        drawView.setbitmap(mBitmap);
        drawView.setDrawingCacheEnabled(true);
        drawView.layout(0, 0, mBitmap.getHeight(),mBitmap.getWidth());
        drawView.buildDrawingCache(true);
        int Height =mBitmap.getHeight();
        Log.v(TAG, "height:"+Height);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mBitmap.getWidth(),mBitmap.getHeight());
        mainContainer.setLayoutParams(lp);
        mainContainer.addView(drawView);
        mTts.speak("Please draw cicles on each item!", TextToSpeech.QUEUE_FLUSH, null);

        btnColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AmbilWarnaDialog dialog = new AmbilWarnaDialog(
						FingerPaint.this, lastColor,
						new OnAmbilWarnaListener() {
							@Override
							public void onOk(AmbilWarnaDialog dialog, int color) {
								// color is the color selected by the user.
								colorChanged(color);
							}

							@Override
							public void onCancel(AmbilWarnaDialog dialog) {
								// cancel was selected by the user
							}
						});

				dialog.show();
			}
		});

		btnUndo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				drawView.onClickUndo();
			}
		});
        goback_main.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                setResult(ChooseActivity.FAILURE, intent);
                finish();
            }
        });

        retap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                drawView.resetcount();
            }
        });
        done.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(drawView.allcircle()) {
                    image1.setBackgroundResource(R.drawable.icon4);
                    mTts.speak("Please write the number of circles you tap!", TextToSpeech.QUEUE_FLUSH, null);
                    count = drawView.getcount();
                    LinearLayout Table = (LinearLayout) findViewById(R.id.table1);
                    RelativeLayout.LayoutParams layoutp = new RelativeLayout.LayoutParams(400, 400);
                    layoutp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    Container.setBackgroundResource(R.drawable.shape);
                    Container.setLayoutParams(layoutp);
                    rl1.addView(Container);
                    Container.addView(dv);
                    Table.removeAllViews();
                    Table.addView(image1);
                    Table.addView(reset1);
                    Table.addView(goback_tap);
                    Table.addView(recog1);
                }
                else
                {
                    mTts.speak("Did you tap all circles? Try again!", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        btnFinish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO AutoisDraw-generated method stub
                mTts.speak("Now, tap all the circles you draw!", TextToSpeech.QUEUE_FLUSH, null);
                LinearLayout Table = (LinearLayout)findViewById(R.id.table1);
                Table.removeAllViews();
                image1.setBackgroundResource(R.drawable.icon3);
                drawView.reverseisdraw();
                Table.addView(image1);
                Table.addView(retap);
                Table.addView(goback_cicleitem);
                Table.addView(done);
            }
        });
        //Go back to the paint panel
        goback_cicleitem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO AutoisDraw-generated method stub
                LinearLayout Table = (LinearLayout)findViewById(R.id.table1);
                Table.removeAllViews();
                image1.setBackgroundResource(R.drawable.icon2);
                Table.addView(image1);
                Table.addView(btnColor);
                Table.addView(btnUndo);
                Table.addView(goback_main);
                Table.addView(btnFinish);
                drawView.reverseisdraw();


            }
        });

        //Set buttons onclick listener
        reset1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                reset(dv);
            }
        });

        recog1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int result = reconumber(dv.path);
                int c = drawView.getcount();
                if(result == c) {
                    success.play(1,1, 1, 0, 0, 1);
                    File photo = new File(getIntent().getStringExtra(ChooseActivity.IMAGE_PATH));
                    Bitmap editedImage = Bitmap.createBitmap(drawView.getDrawingCache());
                    FileOutputStream out = null;
                    if (photo.exists()) {
                        photo.delete();
                    }
                    try {
                        out = new FileOutputStream(photo.getPath());
                        editedImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (editedImage != null) {
                        Intent intent = new Intent();
                        intent.putExtra("count",String.valueOf(c));
                        intent.putExtra("int",c);
                        setResult(SUCCESS, intent);
                        finish();
                    }

                }
                else
                {
                    String output = "The number you input is "+result + "  The circle you tap is" +drawView.getcount()+"\n";
                    mTts.speak(output, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        goback_tap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO AutoisDraw-generated method stub
                LinearLayout Table = (LinearLayout)findViewById(R.id.table1);
                Table.removeAllViews();
                Container.removeAllViews();
                rl1.removeView(Container);
                image1.setBackgroundResource(R.drawable.icon3);
                drawView.reverseisdraw();
                Table.addView(image1);
                Table.addView(retap);
                Table.addView(goback_cicleitem);
                Table.addView(done);
            }
        });
	}
    private Bitmap rotateImage(Bitmap bitmap, int angle, int width, int height) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
	public void colorChanged(int color) {
		// TODO Auto-generated method stub
		lastColor = color;
		drawView.colorChanged(lastColor);
	}
    public void reset(DrawView dv)
    {
        dv.resetPath();
        dv.path.reset();
        dv.invalidate();
    }

    public int reconumber(Paths path)
    {
        Recognizer.init();

        for (int n=1;n<=num;n++)
        {
            String s = ChooseActivity.sarray[n];
            if (s==null)
                continue;
            int number=s.charAt(s.length()-1)-'0';
            boolean[][] input=new boolean[Const.MAXN][Const.MAXN];
            for (int i=0;i<Const.MAXN;i++)
                for (int j=0;j<Const.MAXN;j++)
                    if (s.charAt(i*Const.MAXN+j)=='0')
                        input[i][j]=false;
                    else
                        input[i][j]=true;
            Recognizer.add(input,number);
        }
        Recognizer.learn();
        boolean[][] b=path.process();
        String output="";
        if (b==null)
        {
            return -1;
        }
        double[] d=Recognizer.recognize(b);
        double sum=0;
        for (int i=0;i<=Const.MAXNUM;i++)
            sum+=d[i];
        for (int i=0;i<=Const.MAXNUM;i++)
            d[i]=d[i]*1.0/sum;
        double maxp=0;
        int maxn=0;
        for (int i=0;i<=Const.MAXNUM;i++)
        {
            output=output+i+":"+d[i]+"\n";
            if (d[i]>maxp)
            {
                maxp=d[i];
                maxn=i;
            }
        }
        return maxn;
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
            mTts.speak("Please draw cicles on each item!", TextToSpeech.QUEUE_FLUSH, null);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Lanuage data is missing or the language is not supported.
                Log.e(TAG, "Language is not available.");
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }


}
