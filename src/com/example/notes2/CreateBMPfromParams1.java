package com.example.notes2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class CreateBMPfromParams1 {

	
	public static Bitmap getIVBMP(Context context,int h,int w,int gridX,int gridY,String color, String colorlineX, String colorlineY) {

		Bitmap imageBitmap = null;
		Paint pgridx = new Paint();
		pgridx.setColor(Color.parseColor(colorlineX));
		pgridx.setStrokeWidth(2);
		Paint pgridy = new Paint();
		pgridy.setColor(Color.parseColor(colorlineY));
		pgridy.setStrokeWidth(2);
		
        imageBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imageBitmap);
        canvas.drawColor((Color.parseColor(color)));
           
          for (int i=0;i<gridY+1;i++){
        	  canvas.drawLine(0, i*h/gridY, w, i*h/gridY, pgridx);  // Horizontal
          }
          
          for (int i=0;i<gridX+1;i++){
        	  canvas.drawLine(i*w/gridX, 0, i*w/gridX, h, pgridy);  
          }         
           return imageBitmap;		
	}
	
		
	public static Bitmap getIVBMPm(Context context,int h,int w) {

	    String[] colors={    "#A9A9A9","#C0C0C0","#D3D3D3","#DCDCDC","#F5F5F5","#FFFFFF","#B0C4DE","#E6E6FA","#FFFAF0","#F0F8FF","#F8F8FF","#F0FFF0",
	    	    "#FFFFF0","#F0FFFF","#FFFAFA","#FFE4B5","#FFDEAD","#FFDAB9","#FFE4E1","#FFF0F5","#FAF0E6","#FDF5E6","#FFEFD5","#FFF5EE","#F5FFFA",
	    	    "#FFC0CB","#FAEBD7","#F5F5DC","#FFE4C4","#FFEBCD","#F5DEB3","#FFF8DC","#FFFACD","#FAFAD2","#FFFFE0","#00FFFF","#E0FFFF","#00CED1",
	    	    "#40E0D0","#48D1CC","#AFEEEE","#7FFFD4","#B0E0E6"};
	   
	    String[] colorLines={
	    		"#000000","#FFFFFF","#FF0000",	"#00FF00","#0000FF","#FFFF00","#00FFFF","#FF00FF","#800000","#8B0000",
	    		"#A52A2A","#B22222","#DC143C","#FF0000","#FF6347","#006400","#008000","#228B22","#00BFFF","#1E90FF",
	    		"#191970","#000080","#00008B","#0000CD","#0000FF","#4169E1","#8A2BE2","#A0522D","#D2691E" };
		
	    final String MyPREFERENCES = "MyPrefs" ;  // my pref internal folder name
	    String PAGECOLORINDEX="PAGECOLORINDEX";
	    String HORIZONTALPAGECOLORINDEX="HORIZONTALPAGECOLORINDEX";
	    String VERTICALPAGECOLORINDEX="VERTICALPAGECOLORINDEX";
	    String HORIZONTALPAGELINESINDEX="HORIZONTALPAGELINESINDEX";
	    String VERTICALPAGELINESINDEX="VERTICALPAGELINESINDEX";
	    
SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		
		if (sharedpreferences.getString(PAGECOLORINDEX, "").equals("")){
		  SharedPreferences.Editor editor = sharedpreferences.edit();			
          editor.putString(HORIZONTALPAGELINESINDEX, "2").commit();
          editor.putString(VERTICALPAGELINESINDEX, "2").commit();
          editor.putString(HORIZONTALPAGECOLORINDEX, "2").commit();
          editor.putString(VERTICALPAGECOLORINDEX, "2").commit();
          editor.putString(PAGECOLORINDEX, "2").commit();
		}
		int pageColorSelect=Integer.valueOf(sharedpreferences.getString(PAGECOLORINDEX, ""));
		int gridX=Integer.valueOf(sharedpreferences.getString(HORIZONTALPAGELINESINDEX, ""));
		int gridY=Integer.valueOf(sharedpreferences.getString(VERTICALPAGELINESINDEX, ""));
		int gridXcolor=Integer.valueOf(sharedpreferences.getString(HORIZONTALPAGECOLORINDEX, ""));
		int gridYcolor=Integer.valueOf(sharedpreferences.getString(VERTICALPAGECOLORINDEX, ""));
		
		Bitmap imageBitmap = null;
		Paint pgridx = new Paint();
		pgridx.setColor(Color.parseColor(colorLines[gridXcolor]     ));
		pgridx.setStrokeWidth(2);
		Paint pgridy = new Paint();
		pgridy.setColor(Color.parseColor(colorLines[gridYcolor] ));
		pgridy.setStrokeWidth(2);
		
        imageBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imageBitmap);
        canvas.drawColor((Color.parseColor(colors[pageColorSelect])));
           
          for (int i=0;i<gridY+1;i++){
        	  canvas.drawLine(0, i*h/gridY, w, i*h/gridY, pgridx);  // Horizontal
          }
          
          for (int i=0;i<gridX+1;i++){
        	  canvas.drawLine(i*w/gridX, 0, i*w/gridX, h, pgridy);  
          }         
           return imageBitmap;		
	}
	
	public static Drawable convertBitmaptodrawable(Context context,Bitmap bmp){
        return new BitmapDrawable(context.getResources(), bmp);
	} 
	
	
	
	
	
	
	
}
