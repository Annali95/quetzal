package com.example.quetzal.recognize;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View implements View.OnTouchListener
{
	private Canvas mCanvas;
	public Path mPath=new Path();
	public Paths path = new Paths();

	public float preX,preY;
	private int width,height;
	private Paint paint;
	private Bitmap mBitmap;

	public DrawView(Context context)
	{
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		paint=new Paint();
		setpaint(paint);
		DisplayMetrics dm=new DisplayMetrics();
		((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		width=(dm.widthPixels);
        height=(dm.heightPixels);
		mCanvas = new Canvas();
		Log.i("DrawView", "宽度："+ String.valueOf(width));

	}
	private void setpaint(Paint paint)
	{
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(10);
		paint.setDither(true);
		paint.setAntiAlias(true);
	}
	public void setcolor(int color)
	{
		paint.setColor(color);
	}
	public void setsize(int w,int h)
	{
		width=w;
		height=h;
	}
	@Override
	public void onDraw(Canvas canvas)
	{
		mBitmap= Bitmap.createBitmap(width,height, Config.ARGB_8888);
		canvas.drawBitmap(mBitmap,0,0,null);
		canvas.drawPath(mPath,paint);
		/*canvas.drawLine(0,0,width,0,paint);
		canvas.drawLine(0,0,0,height,paint);
		canvas.drawLine(width,0,width,height,paint);
		canvas.drawLine(0,height,width,height,paint);*/
	}
	public void resetPath()
	{
		mPath.reset();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		float x=event.getX(),y=event.getY();
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				this.mPath.moveTo(x,y);
				this.preX=x;this.preY=y;
				path.addPath();
				path.addPoint(x,y);
				break;
			case MotionEvent.ACTION_MOVE:
				this.mPath.quadTo(this.preX,this.preY,x,y);
				this.preX=x;this.preY=y;
				path.addPoint(x,y);
				break;
			case MotionEvent.ACTION_UP:
				break;
		}
		this.invalidate();
		return true;
	}
}