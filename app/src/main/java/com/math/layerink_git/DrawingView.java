package com.math.layerink_git;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Math on 22/02/2017.
 */

public class DrawingView extends View {

    private int width;
    private  int height;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint bitmapPaint;
    private Paint circlePaint;
    private Paint paint;
    private Path path;
    private Path circlePath;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    Context context;

    public DrawingView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        init();

        Log.d("deb", "on est dans le constructeur de la drawing view");
    }

    private void init() {
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        path = new Path();
        //paint = p;
        circlePath = new Path();
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(ContextCompat.getColor(context, R.color.blue));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        Log.d("deb", "on est dans init");
    }

    public void setPaint(Paint p) {
        paint = p;
        Log.d("deb", "on est dans setpaint");
    }

    /*
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("deb", "on est dans onMeasure");
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;

        setMeasuredDimension(width, height);
    }
    */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        DisplayMetrics metrics = new DisplayMetrics();
        //((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;

        super.onSizeChanged(width, height, oldw, oldh);

        Log.d("deb", "on est dans on size changed");

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.drawPath(path, paint);
        canvas.drawPath(circlePath,  circlePaint);
        Log.d("deb", "on est dans onDraw");
    }

    private void touch_start(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        path.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        canvas.drawPath(path, paint);
        // kill this so we don't double draw
        path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }


    public void clean() {
        setDrawingCacheEnabled(false);

        onSizeChanged(width, height, width, height);
        invalidate();

        setDrawingCacheEnabled(true);
    }


}
