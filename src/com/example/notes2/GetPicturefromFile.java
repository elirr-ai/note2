package com.example.notes2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Environment;

public class GetPicturefromFile {
	
public GetPicturefromFile(){

};

public Bitmap getPicture (int rotate_degs2,String dir, String file, int w, int h){
	Matrix matrix = new Matrix();
    matrix.postRotate(rotate_degs2); 
    File Path = new File(Environment.getExternalStorageDirectory(), dir);
    File file1 = new File(Path, file);
 Bitmap bitmap = decodeSampledBitmapFromFile(file1.getAbsolutePath(), w,h);
   	return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
}

public static Bitmap getPicture3 (int rotate_degs2,String file, int w, int h){
//	Matrix matrix = new Matrix();
//	matrix.postScale(0.8f, 0.8f); 
//    matrix.postRotate(rotate_degs2); 
//    File file1 = ;
    Bitmap bitmap = decodeSampledBitmapFromFile(new File( file).getAbsolutePath(), w,h);
//    Bitmap bitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
//   if (bitmap.getWidth()>bitmap.getHeight())  matrix.postRotate(90);//force portrait
 
   
//   return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//  return Bitmap.createBitmap(bitmap,0,0,w,h,matrix,true);

   return Bitmap.createScaledBitmap(bitmap,w,h,true);	
}

public Point getWHofBMP(String dir, String file){
    File Path = new File(Environment.getExternalStorageDirectory(), dir);
    File file1 = new File(Path, file);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(file1.getAbsolutePath());
    Bitmap bitmapmm = BitmapFactory.decodeFile(file1.getAbsolutePath());
int ww=bitmapmm.getWidth();
int hh=bitmapmm.getHeight();
    String imageType = options.outMimeType;
    return new Point(ww,hh);
}

public Bitmap getPicture1 (int rotate_degs2,String fullPath, int w, int h){
	BitmapFactory.Options options = new BitmapFactory.Options();
	options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	return BitmapFactory.decodeFile(fullPath, options);

	
//	Matrix matrix = new Matrix();
//    matrix.postRotate(rotate_degs2); 
//    File file1 = new File(fullPath);
//    Bitmap bitmap = decodeSampledBitmapFromFile(file1.getAbsolutePath(), w,h);
//   	return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
	
}

	
public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) 
{ // BEST QUALITY MATCH
    
    //First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);

    // Calculate inSampleSize, Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    int inSampleSize = 1;

    if (height > reqHeight) 
    {
        inSampleSize = Math.round((float)height / (float)reqHeight);
    }
    int expectedWidth = width / inSampleSize;

    if (expectedWidth > reqWidth) 
    {
        //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
        inSampleSize = Math.round((float)width / (float)reqWidth);
    }

    options.inSampleSize = inSampleSize;

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;

    return BitmapFactory.decodeFile(path, options);
}
	
public Bitmap drawStringonBitmap(Bitmap src, String string, Point location, int color, int 
		alpha, int size, boolean underline,int width ,int height) {
		    Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		    Canvas canvas = new Canvas(result);
		    canvas.drawBitmap(src, 0, 0, null);
		    Paint paint = new Paint();
		    paint.setColor(color);
		    paint.setAlpha(alpha);
		    paint.setTextSize(size);
		    paint.setAntiAlias(true);
		    paint.setUnderlineText(underline);
		    canvas.drawText(string, location.x, location.y, paint);
		    return result;
		}	
	
	
public void writeBMPtoFile(Bitmap b,String fullPath, int w, int h) {
	File dest=new File (fullPath);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    b.compress(Bitmap.CompressFormat.PNG, 100, bytes);
//    File f = new File(Environment.getExternalStorageDirectory()
//            + File.separator + "testimage.jpg");
    try {
		dest.createNewFile();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    FileOutputStream fo = null;
	try {
		fo = new FileOutputStream(dest);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    try {
		fo.write(bytes.toByteArray());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    try {
		fo.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//try {
//  FileOutputStream out = new FileOutputStream(dest);
//  b.compress(Bitmap.CompressFormat.PNG, 100, out);
//  out.flush();
//  out.close();
//} catch (Exception e) {
//  e.printStackTrace();
//}
}
//String filename = "pippo.png";
//File sd = Environment.getExternalStorageDirectory();
//File dest = new File(sd, filename);
//
//Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//try {
//     FileOutputStream out = new FileOutputStream(dest);
//     bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
//     out.flush();
//     out.close();
//} catch (Exception e) {
//     e.printStackTrace();
//}








public void writeBMPtoFile1(Bitmap b,String dir, String file) {
	File Path = new File(Environment.getExternalStorageDirectory(), dir);
    File dest = new File(Path, file);
//	File dest=new File (fullPath);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    b.compress(Bitmap.CompressFormat.PNG, 100, bytes);
//    File f = new File(Environment.getExternalStorageDirectory()
//            + File.separator + "testimage.jpg");
    try {
		dest.createNewFile();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
    FileOutputStream fo = null;
	try {
		fo = new FileOutputStream(dest);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
    try {
		fo.write(bytes.toByteArray());
	} catch (IOException e) {
		e.printStackTrace();
	}
    try {
		fo.close();
	} catch (IOException e) {
		e.printStackTrace();
	}

}




public ArrayList<String> getBitmapProperties (Bitmap b){
	ArrayList<String> al=new ArrayList<String>();
	al.clear();
	Config c1=b.getConfig();
	al.add("G-Name= "+String.valueOf(c1.name()));
	al.add("G-ordinal= "+String.valueOf(c1.ordinal()));
	al.add("Width= "+String.valueOf(b.getWidth()));
	al.add("Height= "+String.valueOf(b.getHeight()));
	al.add("Byte count= "+String.valueOf(b.getByteCount()));
	al.add("Byte Allocated count= "+String.valueOf(b.getAllocationByteCount()));
	al.add("Density= "+String.valueOf(b.getDensity()));
	al.add("Genarated ID= "+String.valueOf(b.getGenerationId()));

	return al;
	
	
	
}








	
}
