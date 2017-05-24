package com.example.kmj.week12;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.BoringLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by KMJ on 2017-05-18.
 */

public class MyPainter extends View {
    String OperationType="";
    Bitmap mBitmap;
    Canvas mCanvas;
    Paint mPaint = new Paint();
    Boolean StampMode=false;

    BlurMaskFilter blur = new BlurMaskFilter(100, BlurMaskFilter.Blur.INNER);

    float array[] = {
            2f, 0, 0, 0, -25f,
            0, 2f, 0, 0, -25f,
            0, 0, 2f, 0, -25f,
            0, 0, 0, 2f, 0
    };

    ColorMatrix colorMatrix = new ColorMatrix(array);
    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

    public MyPainter(Context context) {
        super(context);
        mPaint.setColor(Color.BLACK);
    }

    public MyPainter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.YELLOW);

        //drawStamp();
    }

    public void open(Bitmap img){
        double width = img.getWidth()/Math.sqrt(2);
        double height = img.getHeight()/Math.sqrt(2);
        Bitmap newImg = Bitmap.createScaledBitmap(img,(int)width,(int)height,false);
        mCanvas.drawBitmap(newImg,(getWidth()-newImg.getWidth())/2,(getHeight()-newImg.getHeight())/2,null);
        invalidate();
    }

    private void drawStamp(int x, int y){
        mCanvas.save();
        Bitmap img = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        if (OperationType.equals("rotate")){
            mCanvas.rotate(30,x,y);
            x-=img.getWidth()/2;
            y-=img.getHeight()/2;
            mCanvas.drawBitmap(img,x,y,mPaint);
            invalidate();
            mCanvas.restore();
            OperationType="";
            return;
        }
        if (OperationType.equals("move")){
            x+=100;
            y+=100;
        }
        if (OperationType.equals("scale")){
            float width = img.getWidth()*1.5f;
            float height = img.getHeight()*1.5f;
            Bitmap newImg =Bitmap.createScaledBitmap(img,(int)width,(int)height,false);
            x-=newImg.getWidth()/2;
            x-=newImg.getHeight()/2;
            mCanvas.drawBitmap(newImg,x,y,mPaint);
            invalidate();
            OperationType="";
            return;
        }
        if (OperationType.equals("skew")){
            x-=img.getWidth()/2;
            y-=img.getHeight()/2;
            mCanvas.skew(0.2f,0);
            mCanvas.drawBitmap(img,x-250*y/1245,y,mPaint);
            invalidate();
            mCanvas.restore();
            OperationType="";
            return;
        }
        mCanvas.drawBitmap(img,x,y,mPaint);
        invalidate();
    }

    void BluringMode(Boolean bool){
        if (bool) mPaint.setMaskFilter(blur);
        else mPaint.setMaskFilter(null);
    }

    void ColoringMode(Boolean bool){
        if (bool) mPaint.setColorFilter(filter);
        else mPaint.setColorFilter(null);
    }

    void PenBigMode(Boolean bool){
        if (bool) mPaint.setTextSize(5);
        else mPaint.setTextSize(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap!=null)
            canvas.drawBitmap(mBitmap,0,0,null);

        /*
        BlurMaskFilter blur = new BlurMaskFilter(100, BlurMaskFilter.Blur.INNER);
        float array[] = {
                2f, 0, 0, 0, -25f,
                0, 2f, 0, 0, -25f,
                0, 0, 2f, 0, -25f,
                0, 0, 0, 2f, 0
        };

        ColorMatrix colorMatrix = new ColorMatrix(array);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        */

    }


    int oldx=-1,oldy=-1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X=(int)event.getX();
        int Y=(int)event.getY();
        if (StampMode) {
            if (event.getAction()==MotionEvent.ACTION_DOWN)drawStamp(X,Y);
        }
        else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                oldx = X;
                oldy = Y;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (oldx != 1 && oldy != 1) {
                    mCanvas.drawLine(oldx, oldy, X, Y, mPaint);
                    invalidate();
                    oldx = X;
                    oldy = Y;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (oldx != 1 && oldy != 1) {
                    mCanvas.drawLine(oldx, oldy, X, Y, mPaint);
                    invalidate();
                }
                oldx = -1;
                oldy = -1;
            }
        }
        return true;
    }

    public void clear() {
        mBitmap.eraseColor(Color.WHITE);
        mCanvas.drawColor(Color.YELLOW);
        invalidate();
    }

    public void setOperationType(String operationType) {
        OperationType = operationType;
    }
}
