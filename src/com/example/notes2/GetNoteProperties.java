package com.example.notes2;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;

public class GetNoteProperties {
String dir;
String fileName;

public GetNoteProperties (String d, String f){
	this.dir=d;
	this.fileName=f;


    
    
}
	
public int getNumberOfFilesInNote(){
	
	int c=0; 
	File rootPath = new File(Environment.getExternalStorageDirectory(), dir);
    if(!rootPath.exists()) {
        rootPath.mkdirs();
    }
    File[] filex = rootPath.listFiles();
    for (int i=0;i<filex.length;i++) {
     	String[] arr1=filex[i].toString().split("/");
     	String[] arr2=arr1[5].split("_");
     	if (arr2[0].length()==fileName.length() &&
     			arr2[0].equalsIgnoreCase(fileName) &&	
     					(	arr1[arr1.length-1].endsWith("JPG") ||
     						arr1[arr1.length-1].endsWith("3gp")	||
     						arr1[arr1.length-1].endsWith("mp4") ||
     						arr1[arr1.length-1].endsWith("hnd")	)
     			)
     		c++;
    }
	return ++c;
}

public long getSizeOfFilesInNote(){
	
	long c=0; 
	File rootPath = new File(Environment.getExternalStorageDirectory(), dir);
    if(!rootPath.exists()) {
        rootPath.mkdirs();
    }
    File[] filex = rootPath.listFiles();
    for (int i=0;i<filex.length;i++) {
     	String[] arr1=filex[i].toString().split("/");
     	String[] arr2=arr1[5].split("_");
     	if (arr2[0].length()==fileName.length() &&
     			arr2[0].equalsIgnoreCase(fileName) &&	
     					(	arr1[arr1.length-1].endsWith("JPG") ||
     						arr1[arr1.length-1].endsWith("3gp")	||
     						arr1[arr1.length-1].endsWith("mp4") ||
     						arr1[arr1.length-1].endsWith("hnd")	)
     			)
     		c+=filex[i].length();
    }
	return c;
}

}
