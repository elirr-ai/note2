package com.example.notes2;

import java.io.Serializable;

public class TextColorsHolder implements Serializable {

	private static final long serialVersionUID = -4941606351721401877L;
	static public int optypeTemp,ColorTemp;
	static public boolean colorEnabled=false;
	int type,color,start,end;
	
	
	public TextColorsHolder (int s,int e){
		this.type=optypeTemp; this.color=ColorTemp; this.start=s;this.end=e;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
}
