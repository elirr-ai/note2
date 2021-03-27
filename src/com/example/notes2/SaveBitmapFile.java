package com.example.notes2;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class SaveBitmapFile {

	public SaveBitmapFile(){}
	
	public void saveBitmap(Bitmap b,String d, String f){		
	    File filed = new File(Environment.getExternalStorageDirectory(), d);
	    if (!filed.exists()) {
	        if (!filed.mkdirs()) {
	            Log.e("TravellerLog :: ", "Problem creating Image folder");
	        }
	    }		
		String path = Environment.getExternalStorageDirectory()+"/"+d+"/"+f; 
		File imgFile = new File(path);
		   if (imgFile.exists ()) imgFile.delete (); 
		   try {
		       FileOutputStream out = new FileOutputStream(imgFile);
		       b.compress(Bitmap.CompressFormat.PNG, 100, out);
		       out.flush();
		       out.close();
		   } catch (Exception e) {
		       e.printStackTrace();
		   			}		
	}

	public void saveBitmapJPG(Bitmap b,String d, String f){		
	    File filed = new File(Environment.getExternalStorageDirectory(), d);
	    if (!filed.exists()) {
	        if (!filed.mkdirs()) {
	            Log.e("TravellerLog :: ", "Problem creating Image folder");
	        }
	    }		
		String path = Environment.getExternalStorageDirectory()+"/"+d+"/"+f; 
		File imgFile = new File(path);
		   if (imgFile.exists ()) imgFile.delete (); 
		   try {
		       FileOutputStream out = new FileOutputStream(imgFile);
		       b.compress(Bitmap.CompressFormat.JPEG, 100, out);
		       out.flush();
		       out.close();
		   } catch (Exception e) {
		       e.printStackTrace();
		   			}		
	}
	
	
	
	
}
