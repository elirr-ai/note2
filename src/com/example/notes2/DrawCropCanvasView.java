package com.example.notes2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawCropCanvasView extends View {

	public int width;
	public int height;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	public Context context;
	private Paint mPaint;
	private float mX, mY,startX,startY;
	private static final float TOLERANCE = 5;
	public Bitmap bitmap=null;
	public int mode=0;
	public int radius;
	public float rotateAngle=0.0f;
	
	public DrawCropCanvasView(Context c, AttributeSet attrs) {
		super(c, attrs);
		context = c;

		// we set a new Path
		mPath = new Path();

		// and we set a new Paint with the desired attributes
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeWidth(4f);
	}

	// override onSizeChanged
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// your Canvas will draw onto the defined Bitmap
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	// override onDraw
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bitmap!=null){
			canvas.drawBitmap(bitmap,0,0,null);
		}
		// draw the mPath with the mPaint on the canvas when onDraw
		if (mode==0) {
			canvas.drawPath(mPath, mPaint);
			}
		else if (mode==1) {
			canvas.drawPath(mPath, mPaint);
			}
		else if (mode==2) {
			canvas.drawPath(mPath, mPaint);
			}
		
	}

	// when ACTION_DOWN start touch according to the x,y values
	private void startTouch(float x, float y) {
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		startX=x;
		startY=y;
		invalidate();
	}
	
	private void startTouchRotate() {
		rotateAngle+=90;
		if (rotateAngle==360) rotateAngle=0;
		RotateBitmap(90);
		invalidate();
	}

	// when ACTION_MOVE move touch according to the x,y values
	private void moveTouch(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOLERANCE || dy >= TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
			invalidate();
		}
	}
	
	private void moveTouchCircle(float x, float y) {
		radius=findRadius((int)startX, (int)startY, (int)x, (int)y);
		mPath.reset();
		mPath.addCircle(startX,startY, radius, Direction.CCW);
		invalidate();
	}

	private void moveTouchRECT2(float x, float y) {
		mPath.reset();
		mPath.moveTo(startX, startY);
		if (startX<x && startY<y){
		mPath.lineTo(x,startY);
		mPath.lineTo(x,y);
		mPath.lineTo(startX, y);}
		else if (startX>x && startY<y){
			mPath.lineTo(startX,y);
			mPath.lineTo(x,y);
			mPath.lineTo(x, startY);}
		else if (startX>x && startY>y){
			mPath.lineTo(x,startY);
			mPath.lineTo(x,y);
			mPath.lineTo(startX, y);}
		else if (startX<x && startY>y){
			mPath.lineTo(startX,y);
			mPath.lineTo(x,y);
			mPath.lineTo(x, startY);}
		mPath.lineTo(startX, startY);
		invalidate();
	}	
	// when ACTION_UP stop touch
	private void upTouch() {
		mPath.lineTo(startX,startY);
   		bitmap=convertToRECTANGLE1(bitmap,mPath);
		invalidate();
	}
	private void upTouchCircle() {
//Toast.makeText(context, "Selected: " + startX+"/"+startY
//		, Toast.LENGTH_LONG).show();
		mPath.reset();
		mPath.addCircle(startX,startY, radius, Direction.CCW);
   		bitmap=convertToCircle1(bitmap,mPath);
		invalidate();
	}	
	
	private void upTouchRECT2(int x, int y) {
		//Toast.makeText(context, "Selected: " + startX+"/"+startY
//				, Toast.LENGTH_LONG).show();
		mPath.reset();
		mPath.moveTo(startX, startY);
		if (startX<x && startY<y){
		mPath.lineTo(x,startY);
		mPath.lineTo(x,y);
		mPath.lineTo(startX, y);}
		else if (startX>x && startY<y){
			mPath.lineTo(startX,y);
			mPath.lineTo(x,y);
			mPath.lineTo(x, startY);}
		else if (startX>x && startY>y){
			mPath.lineTo(x,startY);
			mPath.lineTo(x,y);
			mPath.lineTo(startX, y);}
		else if (startX<x && startY>y){
			mPath.lineTo(startX,y);
			mPath.lineTo(x,y);
			mPath.lineTo(x, startY);}
		mPath.lineTo(startX, startY);
		bitmap=convertToCircle1(bitmap,mPath);
		invalidate();
			}		
	//

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mode==0) startTouch(x, y);
			else	if (mode==1) startTouch(x, y);
			else	if (mode==2) startTouch(x, y);
			else	if (mode>=10) startTouchRotate();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode==0)	moveTouch(x, y);
			else	if (mode==1) moveTouchCircle(x,y);
			else	if (mode==2) moveTouchRECT2(x,y);

			break;
		case MotionEvent.ACTION_UP:
			if (mode==0)	upTouch();
			if (mode==1) upTouchCircle();
			if (mode==2) upTouchRECT2((int)x,(int)y);
			
			break;
		}
		return true;
	}

	public void seRotateAngel(float b) {
		rotateAngle=b;
	}
	public float getRotateAngel() {
		return rotateAngle;
	}
	
	
	
	
	
	public void setBitmap(Bitmap b) {
		bitmap=b;
		invalidate();
	}
	
	public void clearPath() {
		mPath.reset();
		invalidate();
	}
	
	   public void RotateBitmap(float angle)    {
	          Matrix matrix = new Matrix();
	          matrix.postRotate(angle);
	          Bitmap b=Bitmap.createBitmap(bitmap, 0, 0,
	        		  bitmap.getWidth(), bitmap.getHeight(),matrix, true);
	          bitmap=b;
	          invalidate();
	    }
	
	
	public void setMode(int m) {
		mode=m;
	}
	
	public int getMode() {
		return mode;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	private Bitmap convertToRECTANGLE1(Bitmap src, Path p) {
        return DrawcropBitmapUtils.getCroppedBitmap(src, p);
    }    //convertToRECTANGLE1(bm,p));  
    
	
	private Bitmap convertToCircle1(Bitmap src, Path p) {
        return DrawcropBitmapUtils.getCroppedBitmap(src, p);
    }    
	
	private int findRadius(int x0,int y0,int x1, int y1){
   		return (int)Math.sqrt(Math.abs((x1-x0)*(x1-x0)+
   			    Math.abs((y1-y0)*(y1-y0))));
	}
	
}