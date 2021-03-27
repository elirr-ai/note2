package com.example.notes2;

import java.util.Comparator;

public class CompareByString implements Comparator<Note> {
	boolean ascDsc; // true=ASC, false=DSC
	int type;
	public CompareByString(int i,boolean b) {
		this.ascDsc=b;
		this.type=i;
	}

	@Override
	public int compare(Note s1, Note s2) {
		if (type==0){
			if (ascDsc) {
				return s1.getMemo_header().compareToIgnoreCase(s2.getMemo_header());  
			}
		else {
			return s2.getMemo_header().compareToIgnoreCase(s1.getMemo_header());  
			}
		}  // type=0
		
		else if (type==1){
			if (ascDsc) {
			return s1.get_millisString().compareToIgnoreCase(s2.get_millisString());  
			}
			else return s2.get_millisString().compareToIgnoreCase(s1.get_millisString());  
		}// type =1
		
		
		
		else if (type==2){  // by pri
			if (ascDsc) {
			return s1.getPriority().compareToIgnoreCase(s2.getPriority());  
			}
			else return s2.getPriority().compareToIgnoreCase(s1.getPriority());  
		}// type =1
		
		
//		else if (type==3){  // by len
//			if (ascDsc) {
//			return s1.getMemBodyLength().compareToIgnoreCase(s2.getMemBodyLength());  
//			}
//			else return s2.getMemBodyLength().compareToIgnoreCase(s1.getMemBodyLength());  
//		}// type =3
//			
		
		
		
		
		else return 0;
	
	}
	

	
	
	
	
	
}
