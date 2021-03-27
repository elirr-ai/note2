package com.example.notes2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class SaveToSmb extends AsyncTask<String, String, String>{
	
	private static final int BUFFER_SIZE=8192;
	public static boolean pingable=false;
//	private InetAddress[] ina;
	private ProgressDialog mProgressDialog;
	SambaShareTaskInformer delegate = null;  // must be !!!!!!!!!!!!!!!
	public static Context context;
	public static String ip;
	public static String username ;
	public static String password ;
	public static String sharedFolder ;
	public static String sharedFile1 ;
	public static String sharednewDir1 ;
	
	public static String AndroidFolder, AndroidFileName;
	public static String writeText;
	Bitmap icon;
	String fileN,fileExt;
	
	String smbFile;
//	String smbPath="smb://10.100.102.125/test/"; // with cellcomm router ADB
//	String smbPath="smb://10.100.102.125/test/"; // with cellcomm router ADB
	String smbPath="smb://192.168.1.116/11/";
	
	public static String[] files;
	public static SmbFile[] smbFiles;
	public static String readText="";
	public static String error1 ;
	public static String exeption1 ;
	public static String pingParameters ="";

	
	public static int opType=0;
	public static Bitmap myBitmap=null;
	public static int displayWidth=1,displayHeight=1;
	
	final static int saveFileText=3;
	final static int readTextFromSmbFile=5;
	final static int WriteZipFiletoSMB=7;
	final static int WriteZipFoldertoSMB=8;
	final static int CopyFromSmbToAndroidFolder=9;
	final static int CopyFromAndroidToSmb=10;
	final static int UNZIPFRPMSMBTOANDROID=16;
	final static int MakeDirInSMB=20;
	final static int READ_DIR=90;
	final static int PING_COMMAND=98;
	final static int QUERYIP=99;

	ArrayList<String> al =new ArrayList<String>();
	
	public SaveToSmb(SambaShareTaskInformer mainActivity) { 
		this.delegate=mainActivity;  // must be !!!!!!!!!!!!!!!
		setProgressBar();
	}
	
	 private void setProgressBar() {
	     mProgressDialog = new ProgressDialog(context);
	     mProgressDialog.setMessage("working..."+String.valueOf(opType));
	     mProgressDialog.setIndeterminate(false);
	     mProgressDialog.setMax(100);
	     mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	     mProgressDialog.setCancelable(true);		
	}
	
	@Override
	protected void onPreExecute() {
		readText="";
		 error1="" ;
		 exeption1="" ;	
		
		mProgressDialog.setMessage("working..."+String.valueOf(opType));
		 mProgressDialog.show();
	}

	@Override
	protected void onPostExecute(String r) {
     mProgressDialog.dismiss();
//     String g="";
//     if (files!=null && files.length>0){   	 
//    	 for (int i=0;i<files.length;i++){
//    		 g+=files[i]+"\n";
//    	 }
//         Toast.makeText(context, "Files= \n"+g, Toast.LENGTH_SHORT).show();
//     }
     delegate.onTaskDone(new DelegateDataBackHolder(opType));
	}
	
	protected void onProgressUpdate(String... progress) {
		String s="";
		if (opType==UNZIPFRPMSMBTOANDROID){
			s="Unzip file from SMB to Android\n";
			s+="target: = "+"\n";
			s+="Source : = "+sharedFolder+sharedFile1+"\n";
			s+="  "+progress[1]+"/"+progress[2]+" files";
		}
		if (opType==10){
			s="copy file from Android to SMB \n";
			s+="Source: = "+AndroidFolder+" / "+AndroidFileName+"\n";
			s+="target: = "+smbPath+AndroidFileName+"\n";
			s+="  "+progress[1]+"/"+progress[2]+" bytes";
		}
		if (opType==9){
			s="copy file from SMB to Android \n";
			s+="Source: = "+smbPath+" / "+AndroidFileName+"\n";
			s+="target: = "+AndroidFolder+"/"+AndroidFileName+"\n";
			s+="  "+progress[1]+"/"+progress[2]+" bytes";
		}	
	
		if (opType==7){
			s="Write Zip File to SMB \n";
			s+="source: = "+AndroidFolder+"/"+AndroidFileName+"\n";
			s+="target: = "+smbPath+" / "+AndroidFileName+".zip\n";
			s+="  "+progress[1]+"/"+progress[2]+" bytes";
		}
		if (opType==8){
			
			s="Write Zip Folder to SMB \n";
			s+="source: = "+AndroidFileName+"\n";
			s+="target: = "+smbPath+AndroidFolder+".zip\n";
			s+="  "+progress[1]+"/"+progress[2]+" files";
		}	
		
		if (opType==QUERYIP){
			s+="  "+progress[1]+"/"+progress[2]+" files\n"+AndroidFileName+"\n";
		}	
		
		mProgressDialog.setMessage(s);
		 mProgressDialog.setProgress(Integer.parseInt(progress[0]));
	}
	
	@Override
	protected String doInBackground(String... params) {
		String z = "";
		try {
if (opType==PING_COMMAND){ 
	pingable=executeCommand1(ip);
	if (pingable) 	pingGetLatency();
//	Toast.makeText(this, "PING= "+pingable, Toast.LENGTH_SHORT).show();
}
	
	/////////////////////////////////////////////////////////////////////////////

if (opType==UNZIPFRPMSMBTOANDROID){ 
	ZipInputStream zin=null;
	ZipEntry ze=null;
	 String location  = "";
     int size;
     byte[] buffer = new byte[BUFFER_SIZE];
		String path9 = "smb://" + ip + "//" + sharedFolder +"//" + sharedFile1;
		NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
		SmbFile smbFile = new SmbFile(path9,auth2);
     int total=0,lenghtOfFile=0;
     if (smbFile.exists()){
     try {
          zin = new ZipInputStream(new BufferedInputStream(new SmbFileInputStream(smbFile), BUFFER_SIZE));
         try {
             ze = null;           
             while ((ze = zin.getNextEntry()) != null) {
            	 lenghtOfFile++;
            	 if (lenghtOfFile>0){
            	 	publishProgress(""+(int)((total*100)/lenghtOfFile),
            	 			String.valueOf(total),
            	 			String.valueOf(lenghtOfFile)  );
            	 }
             }
        	 zin.close();
        	 
             zin = new ZipInputStream(new BufferedInputStream(new SmbFileInputStream(smbFile), BUFFER_SIZE));      	 
             ze = null;
              while ((ze = zin.getNextEntry()) != null) {
                 String path = location + ze.getName();
                 File unzipFile = new File(path);

                 if (ze.isDirectory()) {
                     if(!unzipFile.isDirectory()) {
                         unzipFile.mkdirs();
                     }
                 } else {
                     // check for and create parent directories if they don't exist
                     File parentDir = unzipFile.getParentFile();
                     if ( null != parentDir ) {
                         if ( !parentDir.isDirectory() ) {
                             parentDir.mkdirs();
                         }
                     }

                     // unzip the file
                     FileOutputStream outz1 = new FileOutputStream(unzipFile, false);
                     BufferedOutputStream fout = new BufferedOutputStream(outz1, BUFFER_SIZE);
                     try {
                         while ( (size = zin.read(buffer, 0, BUFFER_SIZE)) != -1 ) {
                             fout.write(buffer, 0, size);
                         }
                         zin.closeEntry();
                     }
                     finally {
                         fout.flush();
                         fout.close();
                     }
                 }
                 
 	publishProgress(""+(int)((total*100)/lenghtOfFile),
 			String.valueOf(total++),
 			String.valueOf(lenghtOfFile)  );

                 
             }
         }
         finally {
             zin.close();
         }
     }
     catch (Exception e) {
    	 exeption1=e.getMessage().toString();     }
}
     else {
    	 exeption1="File does nor exis";
     }
     
 }



if (opType==READ_DIR){ 
try{	
//	   String ip = "192.168.1.116";//no Michse computer
//	   String username = "elirr";
//	   String password = "elirr";
//	   String sharedFolder = "try//";
	
//   String ip = "192.168.1.116";
//  String username = "len124010@gmail.com";
//  String password = "mnbMNB9988";
//  String sharedFolder = "C/try/"; /////// len computer
	
//  String ip = "192.168.1.105";
//  String username = "x_201";
//  String password = "x";
//  String sharedFolder = "C/11/";  //x_201

    String path = "smb://" + ip + "//" + sharedFolder;
    NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
    SmbFile smbFile = new SmbFile(path,auth2);
    files = smbFile.list();
    smbFiles=smbFile.listFiles();
}
catch (IOException e) {
	exeption1=e.toString();
    e.printStackTrace();
	}
}

/*
if (opType==2){ 
try{	
//	   String ip = "192.168.1.116";//no Michse computer
//	   String username = "elirr";
//	   String password = "elirr";
//	   String sharedFolder = "try//";
	
//   String ip = "192.168.1.116";
//  String username = "len124010@gmail.com";
//  String password = "mnbMNB9988";
//  String sharedFolder = "C/try/"; /////// len computer
	
//  String ip = "192.168.1.105";
//  String username = "x_201";
//  String password = "x";
//  String sharedFolder = "C/11/";  //x_201

    String path = "smb://" + ip + "//" + sharedFolder;
    NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
    SmbFile smbFile = new SmbFile(path,auth2);
    files = smbFile.list();
    smbFiles=smbFile.listFiles();
}
catch (IOException e) {
    e.printStackTrace();
    Log.e("", "", e);
}
    
//	String smbPath="smb://192.168.1.116/11/";
//	NtlmPasswordAuthentication auth1 = new NtlmPasswordAuthentication(null  , null,  null);

//	 url = smbPath+ "qqqq.txt";
//	 sfile = new SmbFile(url, auth2);
//					if (!sfile.exists()) {
//						sfile.createNewFile();
//						z = "Created the file for you!!!!";
//					} else{
//						z = "Already exists at the specified location!!!!"; }
//					
//	out = new SmbFileOutputStream(sfile);
//	out.write("1234567890".getBytes());  // for text
//	out.flush();  
//    out.close();   
}
*/








if (opType==readTextFromSmbFile){ 
    try {
		String path = "smb://" + ip + "//" + sharedFolder + sharedFile1;
		NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
		SmbFile smbFile = new SmbFile(path,auth2);
		if (!smbFile.exists()) {
			error1="500";  // file does not exit
		}
		else {
//					if (!sfile.exists()) {
//						sfile.createNewFile();
//						z = "Created the file for you!!!!";
//					} else{
//						z = "Already exists at the specified location!!!!"; }						
						SmbFileInputStream in=new SmbFileInputStream (smbFile);
						InputStreamReader isr = new InputStreamReader(in);
						char[] data=new char[256];  // data is char array with size=100
						String final_data="";
						int size;					
							try {
								while(  ( size=isr.read(data))>0 )
								{
								String read_data=String.copyValueOf(data, 0, size); 
								final_data+=read_data;	
								data = new char[256];
								readText=final_data;
								}
													
							} catch (IOException e) {
								exeption1=e.toString();
								e.printStackTrace();
								}
in.close();
		}
	} catch (Exception e) {
		exeption1=e.toString();
		e.printStackTrace();
	}
}




//////////////////////////////
if (opType==WriteZipFiletoSMB){   // write zip file
	try  { 
    String pathSmb = "smb://" + ip + "//" + sharedFolder + sharedFile1;  // smb file to hold zip 
    NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
    SmbFile smbFile = new SmbFile(pathSmb,auth2); //zip file at smb
    
	File PathAndr = new File(Environment.getExternalStorageDirectory(), AndroidFolder);
	if(!PathAndr.exists()) {
		PathAndr.mkdirs();
		}	
		 final int BUFFER=2048;
			File source = new File(PathAndr, AndroidFileName);//file is android file		
//					if (!sfile.exists()) {
//						sfile.createNewFile();
//						z = "Created the file for you!!!!";
//					} else{
//						z = "Already exists at the specified location!!!!"; }

			  BufferedInputStream origin = null; 
			  SmbFileOutputStream dest = new SmbFileOutputStream(smbFile); 
			  ZipOutputStream outp = new ZipOutputStream(new BufferedOutputStream(dest)); 
			  long lenghtOfFile=source.length();
			  byte data[] = new byte[BUFFER]; 
				  FileInputStream fi = new FileInputStream(source); 
				  origin = new BufferedInputStream(fi, BUFFER); 
				  ZipEntry entry = new ZipEntry(AndroidFileName); 
				  outp.putNextEntry(entry); 
				  int count;     long total = 0;
				  while ((count = origin.read(data, 0, BUFFER)) != -1) { 
				    	total+=count;
				    	publishProgress(""+(int)((total*100)/lenghtOfFile),String.valueOf(total),String.valueOf(lenghtOfFile)  );
					  
			      outp.write(data, 0, count); 
			    } 
			    origin.close(); 
			    outp.close(); 
			    fi.close();
			    dest.close();			    
			} catch(Exception e) { 
				exeption1=e.toString();
			  e.printStackTrace(); 
			} 
}

if (opType==WriteZipFoldertoSMB){ // write zip folder to smb
	try  { 
    String pathSmb = "smb://" + ip + "//" + sharedFolder +"//" + sharedFile1;  // smb file to hold zip 
    NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
    SmbFile smbFile = new SmbFile(pathSmb,auth2); //zip file at smb
    
	File PathAndr = new File(Environment.getExternalStorageDirectory(), AndroidFolder);
	if(!PathAndr.exists()) {
		PathAndr.mkdirs();
		}	

File[] fl=PathAndr.listFiles();
//					if (!sfile.exists()) {
////						sfile.createNewFile();
//						z = "Created the file for you!!!!";
//					} else{
//						z = "Already exists at the specified location!!!!"; }				
	 final int BUFFER=2048;

			  BufferedInputStream origin = null; 
			  SmbFileOutputStream dest = new SmbFileOutputStream(smbFile); 
			  ZipOutputStream outp = new ZipOutputStream(new BufferedOutputStream(dest)); 
			  byte data[] = new byte[BUFFER]; 
			  
for (int i=0;i<fl.length;i++){
	AndroidFileName=fl[i].toString();
			  if (!fl[i].isDirectory()){
				  FileInputStream fi = new FileInputStream(fl[i].toString()); 
				  origin = new BufferedInputStream(fi, BUFFER); 
				  ZipEntry entry = new ZipEntry(fl[i].toString()); 
				  outp.putNextEntry(entry); 
				  int count; 
				  while ((count = origin.read(data, 0, BUFFER)) != -1) { 
			      outp.write(data, 0, count); 
			    } 
				  fi.close();//???????????
			    origin.close(); 
			    }
publishProgress(""+(int)((i*100)/fl.length),String.valueOf(i),String.valueOf(fl.length)  );
			  }
			    outp.close(); 
			    dest.close();
			} catch(Exception e) { 
				exeption1=e.toString();
			  e.printStackTrace(); 
			} 	
}

/////////////////////////////////

if (opType==CopyFromSmbToAndroidFolder){   // copy file from SMB to Android
try {
    String pathSmb = "smb://" + ip + "//" + sharedFolder + sharedFile1;  // smb file
    NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
    SmbFile smbFile = new SmbFile(pathSmb,auth2);
    
	File PathAndr = new File(Environment.getExternalStorageDirectory(), AndroidFolder);
	if(!PathAndr.exists()) {
		PathAndr.mkdirs();
		}
	File file = new File(PathAndr, sharedFile1);		
	
//	File source = new File(Path, AndroidFileName);//file is android file		
//	InputStream in = new FileInputStream(source);
//    SmbFile source = new SmbFile("smb://username:password@a.b.c.d/sandbox/sambatosdcard.txt");
//    File destination = new File(Environment.DIRECTORY_DOWNLOADS, "SambaCopy.txt");	
			
	InputStream  in = smbFile.getInputStream();
    OutputStream out999 = new FileOutputStream(file);
    long lenghtOfFile=smbFile.length();
    // Copy the bits from Instream to Outstream
    byte[] buf = new byte[6*1024];
    int len;
    long total = 0;
    while ((len = in.read(buf)) > 0) {
    	total+=len;
    	publishProgress(""+(int)((total*100)/lenghtOfFile),String.valueOf(total),String.valueOf(lenghtOfFile)  );
        out999.write(buf, 0, len);
    }
    in.close();
    out999.flush();
    out999.close();

} catch (MalformedURLException e) {
	exeption1=e.toString();
    e.printStackTrace();
} catch (IOException e) {
	exeption1=e.toString();
    e.printStackTrace();
	}
catch (Exception e) {
	exeption1=e.toString();
    e.printStackTrace();
	}
}


///////////////////////////////////
if (opType==MakeDirInSMB){   // make dir in SMB 
try {
    String path = "smb://" + ip + "//" + sharedFolder + sharednewDir1;
    NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
    SmbFile smbFile = new SmbFile(path,auth2);
    smbFile.mkdirs();

} catch (MalformedURLException e) {
	exeption1=e.toString();
    e.printStackTrace();
} catch (IOException e) {
	exeption1=e.toString();
    e.printStackTrace();
	}

}
//////////////////////////////////
if (opType==QUERYIP){   // query ip 
	   try {
		   al.clear();
		   final String TAG = "nstask";
		      if (context != null) {

		        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

		        WifiInfo connectionInfo = wm.getConnectionInfo();
		        int ipAddress = connectionInfo.getIpAddress();
		        String ipString = Formatter.formatIpAddress(ipAddress);

		        String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
		        for (int i = 100; i < 120; i++) {
		          String testIp = prefix + String.valueOf(i);

		          InetAddress address = InetAddress.getByName(testIp);
		          boolean reachable = address.isReachable(550);
		          String hostName = address.getCanonicalHostName();
		          String hostName1 = address.getHostName();
		          
		          if (reachable){
		            Log.e(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
		        
		          	}
		          al.add("Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!"+
		          	"  "+String.valueOf(reachable)+" "+hostName1);
		          
		          }
		        }
		    } catch (Throwable t) {
		    	exeption1=t.toString();
//		      Log.e(TAG, "Well that's not good.", t);
		    }

}
//////////////////////////////////////
		} catch (Exception ex) {
			exeption1=ex.toString();
			z = ex.getMessage().toString();
		}
		return z;
	}


	public String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 24) & 0xFF);
    }
	
	
	
	
	
	/////////////////////////////////////////////
	 public static Bitmap decodeSampledBitmapFromFile(String path) 
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

	       if (height > displayHeight) 
	       {
	           inSampleSize = Math.round((float)height / (float)displayHeight);
	       }
	       int expectedWidth = width / inSampleSize;

	       if (expectedWidth > displayWidth) 
	       {
	           //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
	           inSampleSize = Math.round((float)width / (float)displayWidth);
	       }

	       options.inSampleSize = inSampleSize;

	       // Decode bitmap with inSampleSize set
	       options.inJustDecodeBounds = false;

	       return BitmapFactory.decodeFile(path, options);
	   }
	 
	////////////////////////
	 private boolean executeCommand1(String ipString){// query ping  op code 98
//	        System.out.println("executeCommand");
		 boolean b=false;
	        Runtime runtime = Runtime.getRuntime();
	        try
	        {
	            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 4  "+ipString);
	            int mExitValue = mIpAddrProcess.waitFor();
//	            System.out.println(" mExitValue "+mExitValue);
	            if(mExitValue==0){
	                return true;
	            }else{
	                return false;
	            }
	        }
	        catch (InterruptedException ignore)
	        {
	            ignore.printStackTrace();
	        	exeption1=ignore.toString();
//	            System.out.println(" Exception:"+ignore);
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	            exeption1=e.toString();
//	            System.out.println(" Exception:"+e);
	        }
	        return false;
	    }	

////////////////////////
	 /*
	 Returns the latency to a given server in mili-seconds by issuing a ping command.
	 system will issue NUMBER_OF_PACKTETS ICMP Echo Request packet each having size of 56 bytes
	 every second, and returns the avg latency of them.
	 Returns 0 when there is no connection
	  */
	 private double pingGetLatency(){
		 int NUMBER_OF_PACKTETS=10;
	     String pingCommand = "/system/bin/ping -c " + NUMBER_OF_PACKTETS + " " + ip;
	     String inputLine = "";
	     double avgRtt = 0;

	     try {
	         // execute the command on the environment interface
	         Process process = Runtime.getRuntime().exec(pingCommand);
	         // gets the input stream to get the output of the executed command
	         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

	         inputLine = bufferedReader.readLine();
	         while ((inputLine != null)) {
	             if (inputLine.length() > 0 && inputLine.contains("avg")) {  // when we get to the last line of executed ping command
	                 break;
	             }
//	        	 if (inputLine.length()>0) inputLine = bufferedReader.readLine();
	             inputLine = bufferedReader.readLine();
	         }
	     }
	     catch (IOException e){
	    	 pingParameters="Error in ping";
	         e.printStackTrace();
	     }

	     // Extracting the average round trip time from the inputLine string
	     String afterEqual = inputLine.substring(inputLine.indexOf("="), inputLine.length()).trim();
	     String afterFirstSlash = afterEqual.substring(afterEqual.indexOf('/') + 1, afterEqual.length()).trim();
	     String strAvgRtt = afterFirstSlash.substring(0, afterFirstSlash.indexOf('/'));
	     avgRtt = Double.valueOf(strAvgRtt);
	     pingParameters=inputLine;
	     return avgRtt;
	 }
//////////////////////////
	
}




///////////////////////////////////////
// u maust enable SMB on windows 