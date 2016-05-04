package com.example.root.mapnav;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by root on 5/4/16.
 */
public class SurfaceViewExample extends Activity implements View.OnTouchListener {
    OurView v;
    Bitmap ball;
    float x, y;
    Bitmap scaled;

    String X = "X coordinate";
    String Y = "Y coordinate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = new OurView(this);
        v.setOnTouchListener(this);
        ball = BitmapFactory.decodeResource(getResources(),R.drawable.marker);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wsn);
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();
        scaled = Bitmap.createScaledBitmap(bitmap, h, w, true);


        x = y = 0;
        setContentView(v);
    }

    @Override
    protected void onPause(){
        super.onPause();
        v.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        v.resume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        x = me.getX();
        y = me.getY();

        Log.d(X,String.valueOf(x));
        Log.d(Y, String.valueOf(y));
        return true;

    }


    public class OurView extends SurfaceView implements Runnable {


        Thread t = null;
        SurfaceHolder holder;
        boolean isItOK = false;

        public OurView(Context context) {
            super(context);
            holder = getHolder();
        }

        public void run(){

            while(isItOK == true){
                // perform canvas drawing

                if(!holder.getSurface().isValid()){
                    continue;
                }


                y = y-1;


                //Resources res = getResources();
                //Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.wsn);
                //int h = bitmap.getHeight();
                //int w = bitmap.getWidth();
                //Bitmap scaled = Bitmap.createScaledBitmap(bitmap, h, w, true);


                Canvas c = holder.lockCanvas();
                //c.drawARGB(255,150,150,10); // paint background
                c.drawBitmap(scaled,null,new Rect(0,0,c.getWidth(),c.getHeight()),null);
                c.drawBitmap(ball,x,y,null);
                holder.unlockCanvasAndPost(c);


            }

        }

        public void pause(){

            isItOK = false;
            while(true){

                try{
                    t.join();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            }

            t = null;

        }

        public void resume(){

            isItOK = true;
            t = new Thread(this); // this id for run method
            t.start();

        }

    }
}
