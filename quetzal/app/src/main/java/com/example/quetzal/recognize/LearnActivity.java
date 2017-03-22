package com.example.quetzal.recognize;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.quetzal.R;

public class LearnActivity extends Activity {
	private DrawView dv;
	private Paths Paths = new Paths();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FrameLayout fl=new FrameLayout(this);
		setContentView(fl);
		dv=new DrawView(this);
		dv.measure(
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		dv.layout(0, 0, dv.getMeasuredWidth(),
				dv.getMeasuredHeight());
		dv.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		fl.addView(dv);
		RelativeLayout rl=(RelativeLayout)getLayoutInflater().inflate(R.layout.learn,null);
		fl.addView(rl);

		Button
        	reset=(Button)rl.findViewById(R.id.reset),
        	purge=(Button)rl.findViewById(R.id.purge),
        	num0=(Button)rl.findViewById(R.id.num0),
        	num1=(Button)rl.findViewById(R.id.num1),
        	num2=(Button)rl.findViewById(R.id.num2),
        	num3=(Button)rl.findViewById(R.id.num3),
        	num4=(Button)rl.findViewById(R.id.num4),
        	num5=(Button)rl.findViewById(R.id.num5),
            num6=(Button)rl.findViewById(R.id.num6),
            num7=(Button)rl.findViewById(R.id.num7),
            num8=(Button)rl.findViewById(R.id.num8),
            num9=(Button)rl.findViewById(R.id.num9);
        reset.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				dv.resetPath();
				dv.path.reset();
				dv.invalidate();
			}
		});
        purge.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				new AlertDialog.Builder(LearnActivity.this)
				.setMessage(R.string.confirm_purge)
				.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						SharedPreferences sp=getSharedPreferences("data", Context.MODE_PRIVATE);
						SharedPreferences.Editor e=sp.edit();
						e.clear();
						e.commit();
						dv.resetPath();
						dv.path.reset();
						dv.invalidate();
					}
				})
				.setNegativeButton(R.string.cancel,null)
				.show();
			}
		});
        class Listener implements View.OnClickListener
        {
        	private int n;
			public Listener(int x)
			{
				n=x;
			}
			public void onClick(View v) {
				SharedPreferences sp=getSharedPreferences("data", Context.MODE_PRIVATE);
				SharedPreferences.Editor e=sp.edit();
				int num=sp.getInt("num",0);
				num++;
				e.putInt("num",num);
				String s="";
				boolean[][] b=dv.path.process();
				//
				//
				//
				//
				//



				if (b==null)
				{
					dv.resetPath();
					dv.path.reset();
					dv.invalidate();
					return;
				}
				for (int i=0;i<Const.MAXN;i++)
				{
					for (int j=0;j<Const.MAXN;j++)
						if (b[i][j]==true)
						{
							s=s+"1";
						}
						else
						{
							s=s+"0";
						}
				}
				s=s+n;
				e.putString("d"+num,s);
				e.commit();
				new AlertDialog.Builder(LearnActivity.this)
				.setMessage(R.string.learn_sample)
				.setNeutralButton(R.string.ok,null)
				.show();
				dv.resetPath();
				dv.path.reset();
				dv.invalidate();
			}
		}
        Button[] buttonArray={num0,num1,num2,num3,num4,num5,num6,num7,num8,num9};
        for (int i=0;i<=9;i++)
        	buttonArray[i].setOnClickListener(new Listener(i));
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_about:
	        	new AlertDialog.Builder(LearnActivity.this)
    			.setTitle(R.string.menu_about)
    			.setMessage(R.string.about)
    			.setNeutralButton(R.string.ok,null)
        		.create().show();
	            return true;
	        case R.id.menu_help:
	        	new AlertDialog.Builder(LearnActivity.this)
    			.setTitle(R.string.menu_help)
    			.setMessage(R.string.help)
    			.setNeutralButton(R.string.ok,null)
        		.create().show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}