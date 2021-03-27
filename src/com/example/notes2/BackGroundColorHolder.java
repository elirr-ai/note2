package com.example.notes2;

import java.io.Serializable;

public class BackGroundColorHolder implements Serializable {
int colorIndex,realColor;

public BackGroundColorHolder(int c){
	this.colorIndex=c;
}
public void setRealColor(int c1) {
	this.realColor = c1;
}	
public void setColorIndex(int colorIndex) {
	this.colorIndex = colorIndex;
}	
	
public int GetColorIndex(){
	return colorIndex;
}
public int GetRealColor(){
	return realColor;
}



}
