package com.example.notes2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class serializeListArray1 {
	
	
	
	public serializeListArray1(){  // constructor
		}
	
	
	public List<Note> readSerializedObject (String dname, String fname){
		boolean envOK=true;
		List<Note> objRead=new ArrayList<Note>();
		objRead=null;
		List<Note> objRead1=new ArrayList<Note>();
		objRead1=null;
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		envOK=false;	
	}
	
	File Path = new File(Environment.getExternalStorageDirectory(), dname);
	if(!Path.exists()) {
	    Path.mkdirs();
	}
	File file = new File(Path, fname);
	if (file.exists() && envOK){
		try {
			FileInputStream fis=new FileInputStream (file);
			ObjectInputStream in = new ObjectInputStream(fis);
		try {
		
			objRead=(List<Note>) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
			
			in.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	if (!file.exists()&envOK){
		objRead1=new ArrayList<Note>();
		saveSerializedObject(dname,fname,objRead1);
		
	}
	
	if (objRead==null) return objRead1;
	else return objRead;	

	}
	
	
	public int saveSerializedObject (String dname, String fname, List<Note> nt){
		int errorcode=0;
		//List<Item> obj;
		//obj = new ArrayList<Item>();
 
	//	for (int i = 0; i < nt.size(); i++) {
	//		obj.add(i, nt.get(i));
	//		}
		// serialzie the objext...
		File rootPath = new File(Environment.getExternalStorageDirectory(), dname);
	    if(!rootPath.exists()) {
	        rootPath.mkdirs();
	    }
	    File dataFile = new File(rootPath, fname);
	    if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	    	errorcode=-1;
	    }
	    else {
		try {
			
			FileOutputStream mOutput = new FileOutputStream(dataFile, false);
			ObjectOutputStream out = new ObjectOutputStream(mOutput);
			out.writeObject(nt);
		//	out.writeObject(obj);
			out.close();
			mOutput.close();

		} catch (FileNotFoundException e) {
			errorcode=-2;
			e.printStackTrace();
		} catch (IOException e) {
			errorcode=-3;
			e.printStackTrace();
		}
	}
	    return errorcode;
	}
	
	
}
