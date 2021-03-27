package com.example.notes2;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

public class LoadBitmapFile {
int w,h;
int imageHeight;
int imageWidth;
	public LoadBitmapFile(int w, int h){
		this.w=w;
		this.h=h;
	}
	
	public Bitmap loadBitMap(String p, String f){
		Bitmap bm1=null;
//		String path = Environment.getExternalStorageDirectory()+"/"+p+"/"+f; 
		File imgFile = new File(Environment.getExternalStorageDirectory()+"/"+p+"/"+f);
//		BitmapFactory.Options options = new BitmapFactory.Options();	
		 
	//	if (scale>1) scale=scale/2;
		
//		int scale=getBitmapScale(p,f);
//		if (scale>2) scale=scale-1;
//			options.inSampleSize = scale; 
		 
        if(imgFile.exists()) {
//        	myBitmap=BitmapFactory.decodeFile(imgFile.getAbsolutePath(),null);                  
        	bm1=	getResizedBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath(),null),w,h);	
        	}
        return bm1;
	}

	
	
	
public int getBitmapScale(String p, String f){
	String path = Environment.getExternalStorageDirectory()+
			"/"+p+"/"+f; 
			File imgFile = new File(path);
    BitmapFactory.Options o = new BitmapFactory.Options(); 
   o.inJustDecodeBounds = true; 
   BitmapFactory.decodeFile(imgFile.getAbsolutePath(),o); 
   //int REQUIRED_SIZE = 640; 
 int REQUIRED_SIZE = Math.min(w, h);
   int width_tmp = o.outWidth, height_tmp = o.outHeight; 
   int scale = 1; 
   while(true) { 
       if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) break; 
       width_tmp /= 2; 
       height_tmp /= 2; 
       scale *= 2; 
   } 
	return scale;
}
	
	
	public String getImageHeight() {
		return String.valueOf(imageHeight); 
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public String getImageWidth() {
		return String.valueOf(imageWidth);
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth*0.97f, scaleHeight*0.96f);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(
	        bm, 0, 0, width, height, matrix, false);
	    bm.recycle();
	    return resizedBitmap;
	}
	
}
