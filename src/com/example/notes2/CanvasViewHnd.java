package com.example.notes2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class CanvasViewHnd extends View {

    private Paint paint,paint1,paint2,paint4,paintEdge,paintPen;
    private Path p;
    int xoffset=50,yoffset=50,xstep=300,ystep=300;
    int penColor,penWidth;
    float radius = (float) 130;
    int circles=3;
    ArrayList<Integer> alt=new ArrayList<Integer>();
    Bitmap bitmap;
    int width,height;
    boolean f;
    boolean useBitmap=true;;
    ArrayList<PointHolderHnd> pal1 =new ArrayList<PointHolderHnd>();
    Paint pi;
    
	public CanvasViewHnd(Context context) {
		super(context);
		 init();
	}
	
	public CanvasViewHnd(Context context, AttributeSet attrs){
	    super(context, attrs);
	    init();
	}
	
	
	
    private void init() {
   	
    	useBitmap=true;
    	f=false;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        
        paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(2);
        paint1.setTextSize(35);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setColor(Color.BLACK);
        
        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(2);
        paint2.setTextSize(25);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        paint2.setColor(Color.YELLOW);

        paint4 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint4.setAntiAlias(true);
        paint4.setStrokeWidth(22);
        paint4.setTextSize(25);
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setColor(Color.CYAN);
        
        paintEdge = new Paint();
//        paintEdge.setAntiAlias(true);
//        paintEdge.setStrokeWidth(35);
//        paintEdge.setStyle(Paint.Style.STROKE);
        paintEdge.setColor(Color.parseColor("#ccc194"));
 
        paintPen= new Paint();
        paintPen.setAntiAlias(true);
        paintPen.setStyle(Paint.Style.STROKE);
        
        pi = new Paint(Paint.ANTI_ALIAS_FLAG);
        pi.setAntiAlias(true);
        pi.setStyle(Paint.Style.STROKE);
  
    }
    @Override
    protected void onDraw(Canvas canvas) {
 
//         canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paintEdge);
         if (null!=bitmap && f )	 canvas.drawBitmap(bitmap, 0, 0, null);

        	 for(int i=1;i<pal1.size();i++){
  pi.setColor(pal1.get(i-1).getColor());
  pi.setStrokeWidth(pal1.get(i-1).getStroke());

        	    	if (	pal1.get(i-1).getX()!=-1	 &&
        	    			pal1.get(i-1).getY()!=-1	 &&
        	    			pal1.get(i).getX()!=-1		 && 
        	    			pal1.get(i).getY()!=-1){

        		 canvas.drawLine(
        				 	pal1.get(i-1).getX(),
        				 	pal1.get(i-1).getY(),
        				 	pal1.get(i).getX(),
        				 	pal1.get(i).getY(),	pi
        				 	);
        	    	}
        		 
        	 }
        	 
        canvas.drawRect(19*(width/20), (float)19.65*(height/20), width, height, paintEdge);
        paintPen.setStrokeWidth(penWidth);
        paintPen.setColor(penColor);
        canvas.drawLine((float)(9.55*(width/10)), (float)(19.90*(height/20)),
        				(float)(9.95*(width/10)), (float)(19.90*(height/20)),
       		   paintPen);
        f=false;
    }

    public void updateData(float position) {
         invalidate();
    }
	
  
    public void drawpath(Path p){
    	this.p=p;
    	invalidate();
    }
    
    public void drawCircles (int cir,int rad){
    	this.circles=cir;
    	this.radius=(float) rad;
    invalidate();
    }

    public void drawCirclesFill (int cir,int rad, ArrayList<Integer> al){
    	this.circles=cir;
    	this.radius=(float) rad;
    	this.alt=al;
     invalidate();
    }
  
    public void setBitmap (Bitmap b, int width2, int height2){
    	this.bitmap=b;
    	this.width=width2;
    	this.height=height2;
    	f=true;
     	invalidate();
    }
    
    public void drawLine1 (ArrayList<PointHolderHnd> pal){
    	this.pal1=pal;
    	f=true;
    	invalidate();
    }
    
	public void save_canvas_bitmap() {  //// not working  //////////////
//		HandFileList=getListFiles.getListofFiles(dname1, fname1, ".hnd");
//		hand_field=HandFileList.get(handFileIndex);	
		
		
		File rootPath = new File(Environment.getExternalStorageDirectory(), "DILE");
	if (!rootPath.exists()) {
		rootPath.mkdirs();
	}
	
	File output = new File(rootPath, "zxc00000______zxcv.PNG");
	OutputStream os = null;
	 
	try {
	  os = new FileOutputStream(output);
	  bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//	  bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
	  os.flush();
	  os.close();

	} catch (Exception e) {
		}
//	Toast.makeText(getApplicationContext(), "Bitmap saved", Toast.LENGTH_SHORT).show();
	
	
	}
	///////////////////////////
public void refresh(){
	f=true;
invalidate();
}

public Bitmap refreshGet(){
	f=true;
invalidate();
return bitmap;
}
  
public void showBrush(int penColour, int penWidth, int[] colors){
	this.penColor=penColour;
	this.penWidth=penWidth;
	f=true;
	invalidate();
}


}

