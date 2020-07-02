package com.hicoding.simpledraw.app.views.widget;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import java.util.HashMap;
import java.util.Map;

public class DrawView extends View
{
	//used to determine wheter user moved a finger enought to draw again
	private static final float TOUCH_TOLERANCE = 10;
	private Bitmap bitmap;
	private Canvas canvas;
	private Paint paintScreen;
	private Paint paintLine;
	
	//maps of current Paths being drawn and points in those Paths
	private final Map<Integer, Path> pathMap = new HashMap<>();
	private final Map<Integer, Point> previousPointMap = new HashMap<>();
	
	public DrawView(Context context, AttributeSet attrs){
		super(context, attrs);
		
		paintScreen = new Paint();
		paintLine = new Paint();
		paintLine.setAntiAlias(true);
		paintLine.setColor(Color.BLACK);
		paintLine.setStyle(Paint.Style.STROKE);
		paintLine.setStrokeWidth(5);
		paintLine.setStrokeCap(Paint.Cap.ROUND);
		
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldW, int oldH){
		bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888));
		canvas = new Canvas(bitmap);
		bitmap.eraseColor(Color.WHITE);
	}
	
	//clear the painting
	public void clear(){
		pathMap.clear();
		previousPointMap.clear();
		bitmap.eraseColor(Color.WHITE);
		invalidate();
	}
	
	public void setDrawingColor(int color){
		paintLine.setColor(color);
	}
}
