package com.hicoding.simpledraw.app.views.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Path p : paths) {
            canvas.drawPath(p, paint);
        }
        canvas.drawPath(path, paint);
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnTouchListener(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(2);
        path = new Path();
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

    public int getDrawingColor() {
        return paint.getColor();
    }

    public void setDrawingColor(int color) {
        paint.setColor(color);
    }


    public void setLineWidth(int width) {
        paint.setStrokeWidth(width);
    }

    public int getLineWidth() {
        return (int) paint.getStrokeWidth();
    }

    public void clear() {
        paths.clear();
        undonePath.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    @SuppressLint("ShowToast")
    public String saveImage() {
        //filename to save a file
        final String name = "CoretsDraw" + System.currentTimeMillis() + ".jpg";
        //insert the image on the device
        String location = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, name, "CoretsDraw");
        String message = location != null ? "Berhasil disimpan !" : "Gagal menyimpan !";
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast.getYOffset());
        toast.show();
        return location != null ? location : "";
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
