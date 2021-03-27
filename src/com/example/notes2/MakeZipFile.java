package com.example.notes2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class MakeZipFile extends AsyncTask<Void, Integer, Integer> {
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
		String dir;
		ArrayList<String> allFiles=new ArrayList<String>();	
		String zipFileName;
		String memoBody;
		Context context;
		int typeOfOp=0;
		ArrayList<DirectoryHolder1> af =new ArrayList<DirectoryHolder1>();
		
	public MakeZipFile(Context context1, String dir,ArrayList<String> allFiles, String zipFileName, String memoBody,int op){
		this.dir=dir;
		this.allFiles=allFiles;
		this.zipFileName=zipFileName;
		this.memoBody=memoBody;
		this.context=context1;
		this.typeOfOp=op;
		setProgressBar();
	}
	
	public MakeZipFile(Context context1, String dir, String zipFileName, int op){
		this.dir=dir;
		this.zipFileName=zipFileName;
		this.context=context1;
		this.typeOfOp=op;
		setProgressBar();
		af.clear();
		
		
	}
	
	 private void setProgressBar() {
	     mProgressDialog = new ProgressDialog(context);
	     mProgressDialog.setMessage("Zipping files...");
	     mProgressDialog.setIndeterminate(false);
	     mProgressDialog.setMax(100);
	     mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	     mProgressDialog.setCancelable(true);		
	}

	public void zipper() throws IOException  //used to process flat memo dir
	    {
//		 FileWriteString fw=new FileWriteString();
		 FileWriteString.setFileString(dir, zipFileName+"___MEMO.txt", memoBody);
		 allFiles.add(0,zipFileName+"___MEMO.txt");
		 final int BUFFER=2048;
			File rootPath = new File(Environment.getExternalStorageDirectory(), dir);/////////+"/MemZipFiles"
		    if(!rootPath.exists()) {
		        rootPath.mkdirs();
		    }
//			File zippath = new File(rootPath, zipFileName+".zip");
			
			File rootPathz = new File(Environment.getExternalStorageDirectory(), dir+"/MemZipFiles");/////////+"/MemZipFiles"
		    if(!rootPathz.exists()) {
		        rootPathz.mkdirs();
		    }
			File zippath = new File(rootPathz, zipFileName+".zip");		
			try  { 
				  BufferedInputStream origin = null; 
				  FileOutputStream dest = new FileOutputStream(zippath); 

				  ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest)); 

				  byte data[] = new byte[BUFFER]; 

				  for(int i=0; i < allFiles.size(); i++) { 

					  publishProgress(  i  );

					  
				    FileInputStream fi = new FileInputStream(rootPath.toString()+"/"+allFiles.get(i)); 
				    origin = new BufferedInputStream(fi, BUFFER); 
				    ZipEntry entry = new ZipEntry(allFiles.get(i)); 
				    out.putNextEntry(entry); 
				    int count; 
				    while ((count = origin.read(data, 0, BUFFER)) != -1) { 
				      out.write(data, 0, count); 
				    } 
				    origin.close(); 
				  } 

				  out.close(); 
				} catch(Exception e) { 
				  e.printStackTrace(); 
				} 
			
	    }

public void ZipFolder(){  // do recursive directory view 
			File rootPath = new File(Environment.getExternalStorageDirectory(), dir);
		    if(!rootPath.exists()) {
		        rootPath.mkdirs();
		    }
	    
walk(rootPath);
int y=0;
  addAllFilestoZip();  
		    
		    
	    }

public void walk(File root) {

    File[] list = root.listFiles();

    for (File f : list) {

        if (f.isDirectory()) {
        	af.add(new DirectoryHolder1(f.getAbsoluteFile(), true));
            walk(f.getAbsoluteFile());
        }
        else {
        	af.add(new DirectoryHolder1(f.getAbsoluteFile(), false));      	
        }
    }
}   

private void addAllFilestoZip(){
	 final int BUFFER=2048;
		File rootPath = new File(Environment.getExternalStorageDirectory(), dir);
	    if(!rootPath.exists()) {
	        rootPath.mkdirs();
	    }
		File zippath = new File(rootPath, zipFileName);	
		try  { 
			  BufferedInputStream origin = null; 
			  FileOutputStream dest = new FileOutputStream(zippath); 

			  ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest)); 

			  byte data[] = new byte[BUFFER]; 
			  for(int i=0; i < af.size(); i++) {
				  publishProgress(  i  );
				  
						if (!af.get(i).isDir()) { 	  
			    FileInputStream fi = new FileInputStream(af.get(i).getFile().toString()); 
			    origin = new BufferedInputStream(fi, BUFFER); 
			    ZipEntry entry = new ZipEntry(af.get(i).getFile().toString()); 
			    out.putNextEntry(entry); 
			    int count; 
			    while ((count = origin.read(data, 0, BUFFER)) != -1) { 
			      out.write(data, 0, count); 
			    } 
			    origin.close(); 
			  }
/////////			    
			  } 

			  out.close(); 
			} catch(Exception e) { 
			  e.printStackTrace(); 
			} 
		
	
}

	 @Override
	 protected void onPreExecute() {
	     super.onPreExecute();
	     mProgressDialog.show();
	 }

	 @Override
	 protected void onPostExecute(Integer unused) {
	     mProgressDialog.dismiss();
	 } 

	 protected void onProgressUpdate(Integer... progress) {
		 if (typeOfOp==1) {
int i1=(int) (((float) progress[0] / (float) allFiles.size()) * 100);
		 
mProgressDialog.setMessage("Zipping: "+allFiles.get(progress[0])+
		"    ("+String.valueOf(progress[0]+"/"+String.valueOf(allFiles.size()))+")");
	     mProgressDialog.setProgress(i1);
	     }
		 
		 if (typeOfOp==2) {
int i1=(int) (((float) progress[0] / (float) af.size()) * 100);
		 
mProgressDialog.setMessage("Zipping: "+af.get(progress[0])+
		"    ("+String.valueOf(progress[0]+"/"+String.valueOf(af.size()))+")");
	     mProgressDialog.setProgress(i1);
	     }
		 
	}	 
	 
	@Override
	protected Integer doInBackground(Void... params) {
try {
	if (typeOfOp==1)  zipper();
	if (typeOfOp==2)  ZipFolder();
} catch (IOException e) {
	e.printStackTrace();
}
return null;
	}
	 
	
}
