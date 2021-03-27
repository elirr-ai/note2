package com.example.notes2;

import java.util.Comparator;

public class CompareByNumbers implements Comparator<Note>{
	boolean f;
	public CompareByNumbers(boolean b) {
		this.f=b;
	}

	@Override
	public int compare(Note n1, Note n2) {
		int a=0,b=0;
		if (f){
			a= n1.getMemoBody().length();
			b= n2.getMemoBody().length();
		}
		else {
			 b= n1.getMemoBody().length();
			 a= n2.getMemoBody().length();
		}
		
		if(a==b)  
		return 0;  
		else if(a>b)  
		return 1;  
		else  
		return -1;  
		
	}  
	
	
	
	
}
