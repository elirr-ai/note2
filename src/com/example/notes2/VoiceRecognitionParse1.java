package com.example.notes2;

import java.util.ArrayList;
import java.util.HashMap;

public class VoiceRecognitionParse1 {

	HashMap<String, String> hm = new HashMap<String, String>();
	
	public VoiceRecognitionParse1(){
		
		hm.put("back", "0");  // back return code 0,0 params
		hm.put("beck", "0");
		hm.put("bac", "0");
		hm.put("bak", "0");
		hm.put("Back", "0");
		hm.put("Beck", "0");
		hm.put("Bac", "0");
		hm.put("Bak", "0");		
		
		hm.put("note", "1");  // Note return code 1,1 params
		hm.put("Note", "1");	

		hm.put("1", "1");  // back return code 0,0 params
		hm.put("2", "2");
		hm.put("3", "3");
		hm.put("4", "4");
		hm.put("5", "5");
		hm.put("6", "6");
		hm.put("7", "7");
		hm.put("8", "8");				
		
	}
	
	public String getSetVoiceregogniedArray (ArrayList<String> matches){
		String g="",r="";
		String t[] = null;
			for (int i=0;i<matches.size();i++){
				t=matches.get(i).split(" ");	
				if (hm.containsKey(t[0]) ){
					r=(String) hm.get(t[0]);break;
							}
						}
if (t.length==2){
	for (int i=0;i<matches.size();i++){
		if (hm.containsKey(t[1]) ){
			r+=",";
			r+=(String) hm.get(t[1]);break;
					}
				}
}
		
		return r;
		
	} 
	
}
