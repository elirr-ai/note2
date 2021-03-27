package com.example.notes2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class LoadSaveBitmapFile {
int w,h;
int imageHeight;
int imageWidth;
	public LoadSaveBitmapFile(int w, int h){
		this.w=w;
		this.h=h;
	}
	
	public Bitmap loadBitMap(String p, String f, int scale){
		Bitmap myBitmap=null;
		String path = Environment.getExternalStorageDirectory()+
		"/"+p+"/"+f; 
		File imgFile = new File(path);
		BitmapFactory.Options options = new BitmapFactory.Options();	
		 
	//	if (scale>1) scale=scale/2;
			options.inSampleSize = scale; 
		 
        if(imgFile.exists())
{

myBitmap=BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);                  
          }
return myBitmap;
   		
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
	public Bitmap getScaledBitmap (Bitmap b, int w, int h){
		return Bitmap.createScaledBitmap(b, w, h, false);
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
	
	public void saveBitmap (String p,String f, Bitmap b){
	String path = Environment.getExternalStorageDirectory().toString()+"/"+p;
	OutputStream fOut = null;
	File file = new File(path, f); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
	try {
		fOut = new FileOutputStream(file);
	} catch (FileNotFoundException e) {
		e.printStackTrace();	}

	b.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
	try {
		fOut.flush();
	} catch (IOException e) {
		e.printStackTrace();	} 
	try {
		fOut.close();
	} catch (IOException e) {
		e.printStackTrace();	} 
	}
		
}
