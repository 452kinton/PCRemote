package com.kinton.pcremote.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.kinton.pcremote.R;

public class JoyStickView extends View {
    private int width;
    private int height;

    private int center_radius;
    private int radius;

    private Paint mPaint;

    private int cx;
    private int cy;

    private float radio = 0f;
    private double angle = 0;

    private Context context;

    public JoyStickView(Context context) {
        super(context);
    }

    public JoyStickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JoyStickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = measureDimension(75, widthMeasureSpec);
        height = measureDimension(75, heightMeasureSpec);

        radius = width>height?height/2:width/2;

        center_radius = radius/4;

        cx = width/2;
        cy = height/2;

        setMeasuredDimension(width,height);
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;   //UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.max(result, specSize);
            }

        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xffffffff);
        canvas.drawCircle(width/2,height/2,radius-4,mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(16);
        mPaint.setColor(0xffF7C1BB);
        canvas.drawCircle(width/2,height/2,radius-8,mPaint);
        mPaint.setColor(0xffDC136C);
        mPaint.setStrokeWidth(8);
        canvas.drawCircle(cx,cy,center_radius,mPaint);
    }
    private boolean isHandled = false;

    public float getRadio() {
        return radio;
    }


    public double getAngle() {
        return angle;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int _cx = (int) event.getX();
        int _cy = (int) event.getY();

        int disX = (int) (_cx-width/2f);
        int disY = (int)  (_cy-height/2f);

        int dis = (int) Math.sqrt(disX*disX+disY*disY);

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                if (isHandled){
                    //Log.i("over?",""+(dis>radius*3/4));
                    if(dis>radius*3/4){
                        cx = (int) (((float)disX/dis)*radius*3/4)+width/2;
                        cy = (int) (((float)disY/dis)*radius*3/4)+height/2;
                        radio = 1f;
                    }else{
                        cx = _cx;
                        cy= _cy;
                        radio = dis/(radius*3/4f);
                    }

                    angle = Math.acos((double) disX/dis)*360/(2*Math.PI);
                    if(disY<0)angle=-angle;

                    if(listener!=null){
                        listener.onMove(angle,radio);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if(dis<=center_radius){
                    isHandled = true;
                    if(listener!=null){
                        listener.onDown();
                    }
                }else if(dis>radius){
                    return  false;
                }
                else {
                    isHandled = false;
                    cx = width/2;
                    cy = height/2;
                }
                break;
            case MotionEvent.ACTION_UP:
                isHandled = false;
                cx = width/2;
                cy = height/2;
                if(listener != null){
                    listener.onUp();
                }
                invalidate();
                break;
        }
        return true;
    }

    JoyStickListener listener;

    public void setJoyStickListener(JoyStickListener listener){
        this.listener = listener;
    }

    public interface JoyStickListener{
        void onDown();
        void onMove(double angle,float radio);
        void onUp();
    }
}
