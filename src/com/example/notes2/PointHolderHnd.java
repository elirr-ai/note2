package com.example.notes2;

import java.io.Serializable;

public class PointHolderHnd implements Serializable {

int backgroundcolor;
float X,Y,stroke;
int color;

	public PointHolderHnd(float x,float y, float str,int color , int bg) {
	this.X=x;
	this.Y=y;
	this.stroke=str;
	this.color=color;
	this.backgroundcolor=bg;
	}

	public float getX (){
		return X;
	}
	public float getY (){
		return Y;
	}	
	
	public float getStroke(){
		return stroke;
	}
	public int getColor(){
		return color;
	}	
	public int getBackGroundColor(){
		return backgroundcolor;
	}		
	
	public void setBackGroundColor(int b){
		this.backgroundcolor=b;
	}
	
}
