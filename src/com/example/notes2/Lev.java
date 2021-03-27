package com.example.notes2;

import java.util.ArrayList;

public class Lev {

public static ArrayList<DictionaryDB> al =new ArrayList<DictionaryDB>();
//	public Lev(){};

		 
	    public static int distance(String a, String b) {
	        a = a.toLowerCase();
	        b = b.toLowerCase();
	        // i == 0
	        int [] costs = new int [b.length() + 1];
	        for (int j = 0; j < costs.length; j++)
	            costs[j] = j;
	        for (int i = 1; i <= a.length(); i++) {
	            // j == 0; nw = lev(i - 1, j)
	            costs[0] = i;
	            int nw = i - 1;
	            for (int j = 1; j <= b.length(); j++) {
	                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
	                nw = costs[j];
	                costs[j] = cj;
	            }
	        }
	        return costs[b.length()];
	    }
	 
		   public static int[] compareBufferDict(ArrayList<String> buffer){
			   String[] sp=null;
			   
			   int t1=999,lastIndex = 0;t1=999;//String a,b;
			   for (int i=0;i<buffer.size();i++){
//			   	a=buffer[i];
			   for (int j=0;j<al.size();j++){
//			   	b=dictionary[j];
				   sp=buffer.get(i).split(" ");
			   	int k=Lev.distance(sp[0], al.get(j).getS());
			   	if (k<t1){ 
			   		t1=k;
			   	lastIndex=j;
			   	
			   			}
			   		}	
			   }
			   int yyy=0;int t2=999;int parameter=0;
			   if (al.get(lastIndex).getNumberparams()==1 ){
				   
				   for (int i=0;i<buffer.size();i++){
					  
				   		for (int j=0;j<al.size();j++){
				   			sp=buffer.get(i).split(" ");
				   			if (sp.length==2){
				   			int k9=Lev.distance(sp[1], al.get(j).getS());
				   				if (k9<t2 && al.get(j).getNumberparams()==10){ 
				   					t2=k9;
				   					parameter=j;
					   			}
				   				
				   			}	
				   				
				   				
					   		}	
				   
				   
			   }
				   
			   }
			   
return new int[] {al.get(lastIndex).getOpCode(),al.get(parameter).getOpCode()};			   
//			   return lastIndex;
			
		   }
		   
	public static void initDB(){
		al.add(new DictionaryDB("back",0,0));
		al.add(new DictionaryDB("exit",0,0));
		al.add(new DictionaryDB("note",1,1)); //1=num params, 1=opcode
		al.add(new DictionaryDB("new",0,3));
		al.add(new DictionaryDB("set",0,7));
		al.add(new DictionaryDB("clip",0,8));
		al.add(new DictionaryDB("flash",0,9));
		
		al.add(new DictionaryDB("one",10,1));
		al.add(new DictionaryDB("two",10,2));
		al.add(new DictionaryDB("three",10,3));
		al.add(new DictionaryDB("four",10,4));
		al.add(new DictionaryDB("five",10,5));
		al.add(new DictionaryDB("six",10,6));
		al.add(new DictionaryDB("seven",10,7));
		al.add(new DictionaryDB("eight",10,8));
		al.add(new DictionaryDB("nine",10,9));
	
		al.add(new DictionaryDB("1",10,1));
		al.add(new DictionaryDB("2",10,2));
		al.add(new DictionaryDB("3",10,3));
		al.add(new DictionaryDB("4",10,4));
		al.add(new DictionaryDB("5",10,5));
		al.add(new DictionaryDB("6",10,6));
		al.add(new DictionaryDB("7",10,7));
		al.add(new DictionaryDB("8",10,8));
		al.add(new DictionaryDB("9",10,9));
		
	}	   
	    
	    
//	    public static void main(String [] args) {
//	        String [] data = { "kitten", "sitting", "saturday", "sunday", "rosettacode", "raisethysword" };
//	        for (int i = 0; i < data.length; i += 2)
//	            System.out.println("distance(" + data[i] + ", " + data[i+1] + ") = " + distance(data[i], data[i+1]));
//	    }
	}
	
	
	
	
