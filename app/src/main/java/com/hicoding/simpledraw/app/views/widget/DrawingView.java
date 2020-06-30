package com.hicoding.simpledraw.app.views.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hicoding.simpledraw.app.R;

import java.util.ArrayList;

public class DrawingView extends View implements View.OnTouchListener {
    private Canvas canvas;
    private Path path;
    private Paint paint;
    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Path> undonePath = new ArrayList<>();
    private Bitmap bitmap;


    public DrawingView(Context context) {
        super(context);
        init();
    }

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Path p : paths) {
            canvas.drawPath(p, paint);
        }
        canvas.drawPath(path, paint);
    }

    private void init(){
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnTouchListener(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(6);

        canvas = new Canvas();
        path = new Path();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touchStart(float x, float y) {
        undonePath.clear();
        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        path.lineTo(mX, mY);
        canvas.drawPath(path, paint);
        paths.add(path);
        path = new Path();
    }

    public void undo() {
        if (paths.size() > 0) {
            undonePath.add(paths.remove(paths.size() - 1));
            invalidate();
        }
    }

    public void redo() {
        if (undonePath.size() > 0) {
            paths.add(undonePath.remove(undonePath.size() - 1));
            invalidate();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }
}
