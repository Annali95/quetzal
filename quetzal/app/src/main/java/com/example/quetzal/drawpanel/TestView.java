package com.example.quetzal.drawpanel;
/**
 * Created by liguoying on 1/9/17.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TestView extends View{
    //----绘制轨迹----
    private float mX;
    private float mY;
    private final Paint mGesturePaint = new Paint();
    private final Path mPath = new Path();
    //------检测点是否在path内
    private boolean isDraw=false;
    Region re=new Region();

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGesturePaint.setColor(context.getResources().getColor(android.R.color.holo_green_dark));
        mGesturePaint.setStyle(Paint.Style.STROKE);
        mGesturePaint.setStrokeWidth(4.0f);
    }
    public TestView(Context context) {
        super(context);
        mGesturePaint.setColor(context.getResources().getColor(android.R.color.holo_green_dark));
        mGesturePaint.setStyle(Paint.Style.STROKE);
        mGesturePaint.setStrokeWidth(4.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mGesturePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(isDraw){
            //------关键部分 判断点是否在 一个闭合的path内--------//
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                //构造一个区域对象，左闭右开的。
                RectF r=new RectF();
                //计算控制点的边界
                mPath.computeBounds(r, true);
                //设置区域路径和剪辑描述的区域
                re.setPath(mPath, new Region((int)r.left,(int)r.top,(int)r.right,(int)r.bottom));
                //在封闭的path内返回true 不在返回false
                Log.e("","--判断点是否则范围内----"+re.contains((int)event.getX(), (int)event.getY()));

            }
            return true;
        }
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                mPath.close();
                isDraw=true;
                break;
        }
        //更新绘制
        invalidate();
        return true;
    }

    //---------------下边是划线部分----------------------------//
    //手指点下屏幕时调用
    private void touchDown(MotionEvent event)
    {
        //重置绘制路线，即隐藏之前绘制的轨迹
        mPath.reset();
        float x = event.getX();
        float y = event.getY();
        mX = x;
        mY = y;
        mPath.moveTo(x, y);
    }
    //手指在屏幕上滑动时调用
    private void touchMove(MotionEvent event)
    {
        final float x = event.getX();
        final float y = event.getY();
        final float previousX = mX;
        final float previousY = mY;
        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);
        //两点之间的距离大于等于3时，连接连接两点形成直线
        if (dx >= 3 || dy >= 3)
        {
            //两点连成直线
            mPath.lineTo(x, y);
            //第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mX = x;
            mY = y;
        }
    }
}
