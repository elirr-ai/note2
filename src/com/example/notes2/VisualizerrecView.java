package com.example.notes2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VisualizerrecView extends View {
//    private int width; // width of this View
//    private int height; // height of this View
    private int width=16; // width of this View
    private int height=40; // height of this View
	private int LINE_WIDTH = 2; // width of visualizer lines
 //   private static final int LINE_SCALE = 30; // scales visualizer lines
    private int LINE_SCALE ; 
//    private int LINE_SCALE = ((32768/height)*2)/4; // scales visualizer lines
     float h1,w1,h2;   
    private List<Float> amplitudes = new ArrayList<Float>(width / LINE_WIDTH);; // amplitudes for line lengths
    private String timeShow1="";
    private Paint linePaint,paintEdge,linemid,paintcircColA,paintcircColB,paintcircColC,
    	paintcircColA1,paintcircColAtime; // specifies line drawing characteristics

    public int setCircle=0; // 0=invisible,1=red,2=cyan
    displ disp; 
    double d;
    
    public VisualizerrecView(Context context) {
		super(context);
	}
	// constructor
    public VisualizerrecView(Context context, AttributeSet attrs) {
        super(context, attrs); // call superclass constructor

        float tsize=0;
		d=displ.getScreenInches();
		if (d<5) tsize=getResources().getDimension(R.dimen.audiot1) ;
		if (d>5 && d<6) tsize=getResources().getDimension(R.dimen.audiot2) ;
		if (d>6 && d<7) tsize=getResources().getDimension(R.dimen.audiot3) ;
		if (d>7 && d<8) tsize=getResources().getDimension(R.dimen.audiot4) ;
		if (d>8 && d<9) tsize=getResources().getDimension(R.dimen.audiot5) ;
		if (d>9) tsize=getResources().getDimension(R.dimen.audiot6) ;

        
        linePaint = new Paint(); // create Paint for lines
        linePaint.setColor(Color.GREEN); // set color to green
        linePaint.setStrokeWidth(LINE_WIDTH); // set stroke width
    
        linemid = new Paint(); // create Paint for lines
        linemid.setColor(Color.RED); // set color to green
        linemid.setStrokeWidth(2*LINE_WIDTH); // set stroke width
        
		paintEdge = new Paint();
        paintEdge.setAntiAlias(true);
        paintEdge.setColor(Color.YELLOW); 
		paintEdge.setStyle(Paint.Style.FILL);
		
		paintcircColA = new Paint();
		paintcircColA.setAntiAlias(true);
		paintcircColA.setColor(Color.RED); 
		paintcircColA.setStyle(Paint.Style.FILL);
		
		paintcircColA1 = new Paint();
		paintcircColA1.setAntiAlias(true);
		paintcircColA1.setColor(Color.RED); 
		paintcircColA1.setStyle(Paint.Style.FILL);
		paintcircColA1.setTextSize(tsize*1.4f);
				
		paintcircColAtime = new Paint();
		paintcircColAtime.setAntiAlias(true);
		paintcircColAtime.setColor(Color.BLUE); 
		paintcircColAtime.setStyle(Paint.Style.FILL);
		paintcircColAtime.setTextSize(tsize*1.7f);
		
		paintcircColB = new Paint();
		paintcircColB.setAntiAlias(true);
		paintcircColB.setColor(Color.YELLOW); 
		paintcircColB.setStyle(Paint.Style.FILL);
			
		paintcircColC = new Paint();
		paintcircColC.setAntiAlias(true);
		paintcircColC.setColor(Color.GREEN); 
		paintcircColC.setStyle(Paint.Style.FILL);
		

    } 
   
 // called when the dimensions of the View change
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w; // new width of this View
        height = h; // new height of this View
        h1=(height*7)/100;
        h2=(height*91)/100;
        w1=(width*95)/100; 
        LINE_SCALE = ((32768/height)*20)/30; // scales visualizer lines

    } 

    // clear all amplitudes to prepare for a new visualization
    public void clear() {
        amplitudes.clear();
    } 
    // add the given amplitude to the amplitudes ArrayList
    public void addAmplitude(float amplitude) {
        amplitudes.add(amplitude); // add newest to the amplitudes ArrayList

        // if the power lines completely fill the VisualizerView
        if (amplitudes.size() * LINE_WIDTH >= width) {
            amplitudes.remove(0); // remove oldest power value
        } 
        invalidate();
    } 
    // draw the visualizer with scaled lines representing the amplitudes
    @Override
    public void onDraw(Canvas canvas) {
        int middle = height / 2; // get the middle of the View
        float curX = 0; // start curX at zero
        // for each item in the amplitudes ArrayList
        canvas.drawRect(0, 0, width, height, paintEdge);
       
        
        for (float power : amplitudes) {
            float scaledHeight = power / LINE_SCALE; // scale the power
            curX += LINE_WIDTH; // increase X by LINE_WIDTH

            // draw a line representing this item in the amplitudes ArrayList
            canvas.drawLine(curX, middle + scaledHeight / 2, curX,
            		middle - scaledHeight / 2, linePaint);
        }
        canvas.drawLine(0, middle, width, middle, linemid);
 
        if (setCircle==0) {
        	canvas.drawCircle(w1, h1, 25, paintcircColC);
        	}
        if (setCircle==1) { 
        	canvas.drawCircle(w1, h1, 25, paintcircColA);
        	canvas.drawText("REC", (w1*8)/10, h1, paintcircColA1);
        	}
        if (setCircle==2) {
        	canvas.drawCircle(w1, h1, 25, paintcircColB);
           	canvas.drawText("REC", (w1*8)/10, h1, paintcircColA1);
        	}                
        canvas.drawText(timeShow1, (w1*8)/10, h2, paintcircColAtime);
    } 
    
	public void setCircle(int h){
		this.setCircle=h;
		invalidate();
	}	
    
	public void showTime(String t){
		this.timeShow1=t;
		invalidate();
	}	
	
}
