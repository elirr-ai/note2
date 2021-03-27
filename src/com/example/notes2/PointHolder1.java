package com.example.notes2;

import java.io.Serializable;

public class PointHolder1 implements Serializable {

public static int backgroundcolor=0;
float X,Y,stroke;
int color;

	public PointHolder1(float x,float y, float str,int color) {
	this.X=x;
	this.Y=y;
	this.stroke=str;
	this.color=color;
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
	
	
}
