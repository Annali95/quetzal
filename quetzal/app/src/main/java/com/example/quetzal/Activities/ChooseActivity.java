package com.example.quetzal.Activities;

import java.io.File;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.quetzal.R;
import com.example.quetzal.recognize.Const;
import com.example.quetzal.recognize.DrawView;
import com.example.quetzal.recognize.Paths;
import com.example.quetzal.recognize.Recognizer;

import static android.graphics.Color.WHITE;

public class ChooseActivity extends Activity  implements TextToSpeech.OnInitListener{
	private TextToSpeech mTts;

	private final String TAG = getClass().getSimpleName();

	public static final String BITMAP = "bitmap";
	public static final String IMAGE_PATH = "imagePath";
	private ImageView capturedImage,capturedImage2,capturedImage3;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	int width,height;
	public int num1=-1,num2=-1,num3=-1;
	private int operator = 1;
	private Button finish;
	private TextView hint1,hint2,hint3,oper;
    RelativeLayout rl1;
	private static final int TAKE_PICTURE = 0;
	private static final int EDIT_PICTURE = 1;
	private static final int SELECT_PICTURE = 2;
	private Uri mUri;
	private DrawView dv;
	private File photoFile;
	public static Bitmap mPhoto;
	public static int image;
	Spinner sp;
	String JPEG_FILE_PREFIX = "IMG";
	String JPEG_FILE_SUFFIX = ".jpg";
	public static MediaPlayer success,fail,start;

	private RelativeLayout Container;
	private int num = 150;
	public static String sarray[] = new String[151];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTts = new TextToSpeech(this,this); //TextToSpeech.OnInitListener
		setContentView(R.layout.activity_choose);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels;     // Get the width of the screen
		height = metric.heightPixels;   //
		readfile();
		generateViews();
	}

	private void generateViews() {
		start = MediaPlayer.create(this, R.raw.start);
		fail = MediaPlayer.create(this, R.raw.fail);
		success = MediaPlayer.create(this, R.raw.success);
		capturedImage = (ImageView) findViewById(R.id.capturedImage);
		capturedImage2 = (ImageView) findViewById(R.id.capturedImage2);
		capturedImage3 = (ImageView) findViewById(R.id.capturedImage3);

		hint1 = (TextView)findViewById(R.id.hint1);
		hint2 = (TextView)findViewById(R.id.hint2);
		hint3 = (TextView)findViewById(R.id.hint3);
		oper = (TextView)findViewById(R.id.operator);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);

		Container = new RelativeLayout(this);
		Container.setBackgroundResource(R.drawable.shape);
		rl1.addView(Container);


		dv = new DrawView(this);
		dv.setcolor(Color.BLUE);
        rl1.setBackgroundResource(R.drawable.shape);
        rl1.addView(dv);
        rl1.setVisibility(View.INVISIBLE);
        finish = (Button) findViewById(R.id.finish);
		finish.setVisibility(View.INVISIBLE);

		sp = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.operator, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);

		sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			protected Adapter initializedAdapter = null;

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if (initializedAdapter != parentView.getAdapter()) {
					initializedAdapter = parentView.getAdapter();
					return;
				}

				String selected = parentView.getItemAtPosition(position).toString();
				if (selected.equals("+")) {
					operator = 1;
					oper.setText(" +");
				} else if (selected.equals("-")) {
					operator = 2;
					oper.setText(" -");

				} else if (selected.equals("x")) {
					oper.setText(" x");
					operator = 3;
				} else if (selected.equals("/")) {
					oper.setText(" /");
					operator = 4;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		});


		//capturedImage from camera to image 1
		capturedImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//start.start();    //The start audio
				photoFile = new File(Environment.getExternalStorageDirectory(),
						"photo1.jpg");
				mUri = Uri.fromFile(photoFile);
				Intent intent = new Intent(ChooseActivity.this,
						CameraActivity.class);
				intent.putExtra(IMAGE_PATH, photoFile.getAbsolutePath());
				image = 1;
				startActivityForResult(intent, TAKE_PICTURE);
			}
		});
		//capturedImage from camera to image 2
		capturedImage2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//start.start();    //The start audio
				photoFile = new File(Environment.getExternalStorageDirectory(),
						"photo2.jpg");
				mUri = Uri.fromFile(photoFile);
				Intent intent = new Intent(ChooseActivity.this,
						CameraActivity.class);
				intent.putExtra(IMAGE_PATH, photoFile.getAbsolutePath());
				image = 2;
				startActivityForResult(intent, TAKE_PICTURE);
			}
		});
		capturedImage2.setEnabled(false);
		//capturedImage from camera to image 3
		capturedImage3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				photoFile = new File(Environment.getExternalStorageDirectory(),
						"photo3.jpg");
				mUri = Uri.fromFile(photoFile);
				Intent intent = new Intent(ChooseActivity.this,
						CameraActivity.class);
				intent.putExtra(IMAGE_PATH, photoFile.getAbsolutePath());
				image = 3;
				startActivityForResult(intent, TAKE_PICTURE);
			}
		});
		capturedImage3.setEnabled(false);

		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				num3 = reconumber(dv.path);
				judge();
			}
		});
	}



	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TAKE_PICTURE && resultCode == SUCCESS) {
			getContentResolver().notifyChange(mUri, null);
			try {
					new LoadCapturedImageTask().execute();
			} catch (Exception e) {

			}
		}

		else if (requestCode == EDIT_PICTURE
				&& resultCode == FingerPaint.SUCCESS) {
            mPhoto = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
			String a = data.getStringExtra("count");
			int num = data.getIntExtra("int", 0);
			Drawable drawable = new BitmapDrawable(getResources(), mPhoto);
			if (image == 1) {
				hint1.setVisibility(View.INVISIBLE);
				num1 = num;
				TextView text1 = (TextView) findViewById(R.id.textView1);
				text1.setText(a);
				text1.setTextSize(48);
				text1.setTextColor(WHITE);
				Log.v(TAG, "image = 1");
				capturedImage.setBackgroundDrawable(drawable);
                int Height = capturedImage.getHeight();
                Log.v(TAG, "height:"+Height);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (Height * 1.5),Height);
                capturedImage.setLayoutParams(layoutParams);
				capturedImage2.setEnabled(true);

			} else if (image == 2) {
				hint2.setVisibility(View.INVISIBLE);
				num2 = num;
				TextView text2 = (TextView) findViewById(R.id.textView2);
				text2.setText(a);
				text2.setTextSize(48);
				text2.setTextColor(WHITE);
				Log.v(TAG, "image = 2");
				capturedImage2.setBackgroundDrawable(drawable);
                int Height = capturedImage2.getHeight();
                Log.v(TAG, "height:"+Height);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (Height * 1.5),Height);
                capturedImage2.setLayoutParams(layoutParams);
				capturedImage3.setEnabled(true);

			}


		}


	}
	void judge() {
		if (num1 != -1 && num2 != -1 && num3 != -1) {
			switch (operator) {
				case 1:
					if (num3 == num1 + num2) {
						success.start();
						mTts.speak("You get the right answer!", TextToSpeech.QUEUE_FLUSH, null);
						String output = "You get the right answer!\n";
						new AlertDialog.Builder(ChooseActivity.this).setMessage(output).setNeutralButton(R.string.ok, null).show();
					} else {
						reset(dv);
						fail.start();
						int sum = num1 + num2;
						String output =  num1 + "+" + num2 + " =" + sum + "\n Please try again!";
						mTts.speak(output, TextToSpeech.QUEUE_FLUSH, null);
						new AlertDialog.Builder(ChooseActivity.this).setMessage(output).setNeutralButton(R.string.ok, null).show();
						num3 = -1;
					}
					break;
				case 2:
					if (num3 == num1 - num2) {
						success.start();
						mTts.speak("You get the right answer!", TextToSpeech.QUEUE_FLUSH, null);

						String output = "You get the right answer!\n";
						new AlertDialog.Builder(ChooseActivity.this).setMessage(output).setNeutralButton(R.string.ok, null).show();
					} else {
						reset(dv);
						fail.start();
						int sum = num1 - num2;

						String output = num1 + "-" + num2 + " =" + sum + "\n Please try again!";
						new AlertDialog.Builder(ChooseActivity.this).setMessage(output).setNeutralButton(R.string.ok, null).show();
						num3 = -1;
					}
					break;
				case 3:
					if (num3 == num1 * num2) {
						success.start();
						mTts.speak("You get the right answer!", TextToSpeech.QUEUE_FLUSH, null);
						String output = "You get the right answer!\n";
						new AlertDialog.Builder(ChooseActivity.this).setMessage(output).setNeutralButton(R.string.ok, null).show();
					} else {
						reset(dv);
						fail.start();
						int sum = num1 * num2;

						String output = num1 + "*" + num2 + " =" + sum + "\n Please try again!";
						new AlertDialog.Builder(ChooseActivity.this).setMessage(output).setNeutralButton(R.string.ok, null).show();
						num3 = -1;
					}
					break;
				case 4:
					if (num3 == num1 / num2) {
						success.start();
						mTts.speak("You get the right answer!", TextToSpeech.QUEUE_FLUSH, null);
						String output = "You get the right answer!\n";
						new AlertDialog.Builder(ChooseActivity.this).setMessage(output).setNeutralButton(R.string.ok, null).show();
					} else {
						fail.start();
						reset(dv);
						int sum = num1 / num2;
						String output = num1 + "/" + num2 + " =" + sum + "\n Please try again!";
						new AlertDialog.Builder(ChooseActivity.this).setMessage(output).setNeutralButton(R.string.ok, null).show();
						num3 = -1;
					}
					break;

			}
		}
	}

	class LoadCapturedImageTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(ChooseActivity.this, "",
					getString(R.string.msg_please_wait));
		}

		@Override
		protected Void doInBackground(Void... params) {

                return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			if(image!=3)
			{
				progress.dismiss();
                Intent intent = new Intent(ChooseActivity.this, FingerPaint.class);
                intent.putExtra(IMAGE_PATH, photoFile.getAbsolutePath());
				startActivityForResult(intent, EDIT_PICTURE);

			}
			else{
                mPhoto = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                progress.dismiss();
				Drawable drawable = new BitmapDrawable(getResources(), mPhoto);
                Log.d("camera","camera size: h: "+mPhoto.getHeight()+" w: "+mPhoto.getWidth());
                rl1.setVisibility(View.VISIBLE);
                hint3.setVisibility(View.INVISIBLE);
				finish.setVisibility(View.VISIBLE);
				capturedImage3.setBackgroundDrawable(drawable);
                int Height = capturedImage3.getHeight();
                Log.v(TAG, "height:"+Height);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (Height * 1.5),Height);
                capturedImage3.setLayoutParams(layoutParams);
			}

		}
	}


	public int reconumber(Paths path)
	{
		Recognizer.init();
		for (int n=1;n<=num;n++)
		{
			String s = sarray[n];
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
	private void readfile( ){
		int[] LOOKUP_TABLE = new int[] {
				R.string.d1, R.string.d2,R.string.d3,R.string.d4,R.string.d5,R.string.d6,R.string.d7,R.string.d8,R.string.d9,
				R.string.d10,R.string.d11,R.string.d12,R.string.d13,R.string.d14,R.string.d15,R.string.d16,R.string.d17,R.string.d18,
				R.string.d19,R.string.d20,R.string.d21,R.string.d22,R.string.d23,R.string.d24,R.string.d25,R.string.d26,
				R.string.d27,R.string.d28,R.string.d29,R.string.d30,R.string.d31,R.string.d32,R.string.d33,R.string.d34,R.string.d35,R.string.d36,
				R.string.d37,R.string.d38,R.string.d39,R.string.d40,R.string.d41,R.string.d42,R.string.d43,R.string.d44,R.string.d45,R.string.d46,R.string.d47,
				R.string.d48,R.string.d49,R.string.d50,R.string.d51,R.string.d52,R.string.d53,R.string.d54,R.string.d55,R.string.d56,R.string.d57,R.string.d58,
				R.string.d59,R.string.d60,R.string.d61,R.string.d62,R.string.d63,R.string.d64,R.string.d65,R.string.d66,R.string.d67,R.string.d68,R.string.d69,
				R.string.d70,R.string.d71,R.string.d72,R.string.d73,R.string.d74,R.string.d75,R.string.d76,R.string.d77,R.string.d78,R.string.d79,R.string.d80,
				R.string.d81,R.string.d82,R.string.d83,R.string.d84,R.string.d85,R.string.d86,R.string.d87,R.string.d88,R.string.d89,R.string.d90,R.string.d91,
				R.string.d92,R.string.d93,R.string.d94,R.string.d95,R.string.d96,R.string.d97,R.string.d98,R.string.d99,R.string.d100,R.string.d101,R.string.d102,
				R.string.d103,R.string.d104,R.string.d105,R.string.d106,R.string.d107,R.string.d108,R.string.d109,R.string.d110,R.string.d111,R.string.d112,
				R.string.d113,R.string.d114,R.string.d115,R.string.d116,R.string.d117,R.string.d118,R.string.d119,R.string.d120,R.string.d121,R.string.d122,R.string.d123,R.string.d124,R.string.d125,
				R.string.d126,R.string.d127,R.string.d128,R.string.d129,R.string.d130,R.string.d131,R.string.d132,R.string.d133,R.string.d134,R.string.d135,R.string.d136,R.string.d137,R.string.d138,
				R.string.d139,R.string.d140,R.string.d141,R.string.d142,R.string.d143,R.string.d144,R.string.d145,R.string.d146,R.string.d147,R.string.d148,R.string.d149,R.string.d150
		};
		for(int i = 1;i<=num;i++)
		{
			sarray[i] = getResources().getString(LOOKUP_TABLE[i-1]);

		}
	}
	public void reset(DrawView dv)
	{
		dv.resetPath();
		dv.path.reset();
		dv.invalidate();
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
