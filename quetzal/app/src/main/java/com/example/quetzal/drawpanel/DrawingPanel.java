package com.example.quetzal.drawpanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.quetzal.Activities.FingerPaint;

import java.util.ArrayList;

/**
 * Created by liguoying on 1/13/17.
 */

public class DrawingPanel extends View implements View.OnTouchListener {

    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint, mBitmapPaint;

    private ArrayList<PathPoints> paths = new ArrayList<PathPoints>();
    private ArrayList<PathPoints> undonePaths = new ArrayList<PathPoints>();
    private Bitmap mBitmap;
    private int color;
    private int end = 0;
    private int flag = 0;
    private boolean isDraw=true;
    private int count=0;


    public DrawingPanel(Context context, int color) {
        super(context);
        this.color = color;
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        setpaint(mPaint);
        mPath = new Path();
        mCanvas = new Canvas();

    }
    public void setbitmap(Bitmap m)
    {
        mBitmap = m;
    }

    public void setpaint(Paint mPaint) {
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(30);
    }

    public void colorChanged(int color) {
        this.color = color;
        mPaint.setColor(color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int height = mBitmap.getHeight();
        int width = mBitmap.getWidth();
        mBitmap = rotateImage(mBitmap, 90, width, height);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, this.getWidth(), this.getHeight(), true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        for (PathPoints p : paths) {
            mPaint.setColor(p.getColor());
            Log.v("", "Color code : " + p.getColor());
            canvas.drawPath(p.getPath(), mPaint);

        }
    }

    private Bitmap rotateImage(Bitmap bitmap, int angle, int width, int height) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public void reverseisdraw()
    {
        isDraw = !isDraw;
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 0;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
        if(flag!= end)//add first path
        {
            paths.add(new PathPoints(mPath, color));
            flag = end;

        }
        else
        {
            if(paths.size()>0) paths.remove(paths.size() - 1);
            paths.add(new PathPoints(mPath, color));
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mPath.close();
        PathMeasure pm = new PathMeasure(mPath, false);
        Log.i(" length of Path", String.valueOf(pm.getLength()));
        if(pm.getLength()>300)
        {
           // paths.add(new PathPoints(mPath, color));
           // mCanvas.drawPath(mPath, mPaint);
            end++;
        }
        else
        {
            if(paths.size()>0){
            paths.remove(paths.size() - 1);
            end++;
            invalidate();}

        }

        mPath = new Path();

    }


    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        if(!isDraw){
            int flag = 0;
            //------关键部分 判断点是否在 一个闭合的path内--------//
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                for (PathPoints p : paths)
                {
                    Region re=new Region();
                    Path thispath =  p.getPath();
                    RectF r=new RectF();
                    thispath.computeBounds(r, true);
                    re.setPath(thispath, new Region((int)r.left,(int)r.top,(int)r.right,(int)r.bottom));
                    if(re.contains((int)event.getX(), (int)event.getY()))
                    {
                        if(p.pathcount == false)
                        {
                            FingerPaint.success.play(1,1, 1, 0, 0, 1);
                            //ChooseActivity.success.start();
                            p.pathcount=true;
                            flag = 1;
                            p.setColor(Color.parseColor("#365663"));
                            invalidate();
                        }
                        else
                        {
                           /* ChooseActivity.fail.start();
                            FingerPaint.fail.play(1,1, 1, 0, 0, 1);*/

                        }
                    }
                }
                if(flag == 0)
                {
                    FingerPaint.fail.play(1,1, 1, 0, 0, 1);
                    //ChooseActivity.fail.start();

                }

            }
            return true;
        }

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                {
                    touch_start(x, y);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                {
                    touch_move(x, y);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                {
                    touch_up();
                    invalidate();
                }
                break;
        }
        return true;
    }
    public boolean allcircle()
    {
        for (PathPoints p : paths)
        {
            if(p.pathcount ==false)
                return false;

        }
        return true;
    }
    public int getcount()
    {
        int num = 0;
        for (PathPoints p : paths)
        {
            if(p.pathcount ==true)
                num++;

        }
        return num;
    }
    public int getpath()
    {
        return paths.size();
    }
    public void resetcount()
    {
        count=0;
        for (PathPoints p : paths)
        {
            p.pathcount =false;
            p.setColor(color);
            invalidate();
        }

    }

    public void onClickUndo() {
        if (paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        } else {

        }
        // toast the user
    }

    public void onClickRedo() {
        if (undonePaths.size() > 0) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            invalidate();
        } else {

        }
        // toast the user
    }
}
