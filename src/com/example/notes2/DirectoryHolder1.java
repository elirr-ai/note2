package com.example.notes2;

import java.io.File;

public class DirectoryHolder1 {

	File file;
	boolean isDir;
	
	public DirectoryHolder1 (File f, boolean b){
	this.file=f;
	this.isDir=b;	
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}
	
	
	
	
}
