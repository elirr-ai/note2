package com.example.notes2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import android.graphics.Bitmap;
import android.os.Environment;

public class PdfWrite2 {

	String dir,file,saveDir;
	File fFile;
	Document document;
	boolean error;
	Rectangle pagesize;
	int width,height;
	
	public PdfWrite2 (String d, String f, int w,int h){
		this.width=w;
		this.height=h;
		this.dir=d;
		this.file=f;
//		this.saveDir=d+"/MemPDFFiles";
		init();
//		addMetaData();
	}


	private void init() {
		error=false;
		File Path = new File(Environment.getExternalStorageDirectory(), dir+"/MemPDFFiles");
//		File Path = new File(Environment.getExternalStorageDirectory(), saveDir);
		
		if(!Path.exists()) {
		    Path.mkdirs();
		}
		 fFile = new File(Path, file);

		 
			pagesize = new Rectangle(width,height);
			pagesize.setBackgroundColor(BaseColor.YELLOW);
			pagesize.setBorder(15);  //  1111=15 decimal =all boards enabled
//			pagesize.enableBorderSide(0);

			pagesize.setBorderColor(BaseColor.RED);
			pagesize.setBorderWidth(10f);
			
			document = new Document(pagesize,10,10,10,10);
			
			
		    document.addTitle("Directory: "+dir);
		    document.addSubject("File: "+file);
		    document.addKeywords("Done by DirectotyShow1.apk");
		    document.addAuthor("Eli Rajchert");
		    document.addCreator("Eli Rajchert");
		    document.addCreationDate();
		    
		    
//	        document = new Document();
//	        document.setPageSize(pagesize);

	} 
	
    public void addMetaData()
    
    {
    document.addTitle("Directory: "+dir);
    document.addSubject("File: "+file);
    document.addKeywords("Done by DirectotyShow1.apk");
    document.addAuthor("Eli Rajchert");
    document.addCreator("Eli Rajchert");
    document.addCreationDate();


	}
	
	
	public void addDocument(){
        try {
            PdfWriter.getInstance(document,
                new FileOutputStream(fFile));

            document.open();
            document.add(new Paragraph("A Hello World PDF document."));
            document.close(); // no need to close PDFwriter?

        } catch (DocumentException e) {
            e.printStackTrace();
            error=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            error=true;
        }	
	}
	
	public boolean addParagraph(ArrayList<String> msgContent, ArrayList<String> al){
        try {
			int i1=Integer.valueOf(msgContent.get(0));
        	if (i1==5) pagesize.setBorderColor(BaseColor.BLUE);
        	else if (i1==4) pagesize.setBorderColor(BaseColor.YELLOW);
        	else if (i1==3) pagesize.setBorderColor(BaseColor.GREEN);
        	else if (i1==2) pagesize.setBorderColor(BaseColor.MAGENTA);
        	else if (i1==1) pagesize.setBorderColor(BaseColor.RED);
        	 
            PdfWriter.getInstance(document,
                new FileOutputStream(fFile));
            document.open();
            
        Paragraph pr=setParagraph();    
    for(int i=0; i<msgContent.size()-1; i++){
        Chunk chunk = new Chunk(
            msgContent.get(i)+"\n");
//        paragraph1.add(chunk);
        pr.add(chunk);
    }
    Paragraph pr1=setParagraphL();
//    for(int i=0; i<msgContent.size(); i++){
        Chunk chunk = new Chunk("\n"+msgContent.get(msgContent.size()-1)+"\n");
//        paragraph1.add(chunk);
        pr1.add(chunk);
//    }
    
    
//    document.add(paragraph1);
    document.add(pr);
    document.add(pr1);
    document.newPage();
  
    for (int i=0;i<al.size();i++){
    
    Image image1=null;
	try {
		
		File Path = new File(Environment.getExternalStorageDirectory(), dir);
		if(!Path.exists()) {
		    Path.mkdirs();
		}
//		 File iFile = new File(Path, al.get(i));
		 LoadBitmapFile lb=new LoadBitmapFile(width,height);
		 SaveBitmapFile sb=new SaveBitmapFile();
		 
//		 Bitmap bt=lb.loadBitMap(dir, al.get(i));
		 sb.saveBitmap(lb.loadBitMap(dir, al.get(i)), "000", al.get(i));
		 
			File PathA = new File(Environment.getExternalStorageDirectory(), "000");
			if(!PathA.exists()) {
			    PathA.mkdirs();
			}
			 File iFileXX = new File(PathA, al.get(i));		 
		
		image1 = Image.getInstance(iFileXX.toString());
//		image1.scaleToFit(pagesize);
		image1.setAbsolutePosition(10f, 10f);
		
	} catch (MalformedURLException e) {
		error=true;
		e.printStackTrace();
	} catch (IOException e) {
		error=true;
		e.printStackTrace();
	}
    document.add(image1);
    document.newPage();
    
    }
    document.close();

} catch (DocumentException e) {
	error=true;
    e.printStackTrace();
} catch (FileNotFoundException e) {
	error=true;
    e.printStackTrace();
}
        if (error && fFile.exists()){
        	fFile.delete();
        }
		return error;

}


	private Paragraph setParagraph() {
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setSpacingAfter(25);
        paragraph2.setSpacingBefore(25);
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        paragraph2.setIndentationLeft(50);
        paragraph2.setIndentationRight(50);	
	return paragraph2;
	}
	
	private Paragraph setParagraphL() {
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setSpacingAfter(25);
        paragraph2.setSpacingBefore(25);
        paragraph2.setAlignment(Element.ALIGN_LEFT);
        paragraph2.setIndentationLeft(50);
        paragraph2.setIndentationRight(50);	
	return paragraph2;
	}
	
}
