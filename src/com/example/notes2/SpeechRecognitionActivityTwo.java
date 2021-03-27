package com.example.notes2;

import java.util.ArrayList;

public class SpeechRecognitionActivityTwo {

	static ArrayList<SpeechRecognitionActivityTwoDB> act1 =new ArrayList<SpeechRecognitionActivityTwoDB>();
	static ArrayList<SpeechRecognitionActivityTwoDB> act2 =new ArrayList<SpeechRecognitionActivityTwoDB>();
	static int[] acceptedValues ;

	public static void init(int i){ //  shows which activity is asking
		act1.clear();	
		act2.clear();
		if (i==2){
			acceptedValues = new int[]{101,102,203,304,302,406,405,507,602,604,508,707,807,909,900,910};
			
			// video play 604
			// video record 602
			
			// hand note 507
			
			// camera show 101
			// camera record 102
			// image browse 203
			
			// audio play 304
			// audio record 302
			// speak note 707
			
			// send mail 909
			// send WhatsApp 910
			// send SMS 900
			
			act1.add(new SpeechRecognitionActivityTwoDB("camera", 1));
			act1.add(new SpeechRecognitionActivityTwoDB("image", 2));
			act1.add(new SpeechRecognitionActivityTwoDB("audio", 3));
			act1.add(new SpeechRecognitionActivityTwoDB("note", 4));
			act1.add(new SpeechRecognitionActivityTwoDB("hand", 5));
			act1.add(new SpeechRecognitionActivityTwoDB("video", 6));
			act1.add(new SpeechRecognitionActivityTwoDB("speak", 7));
			act1.add(new SpeechRecognitionActivityTwoDB("dictate", 8));
			act1.add(new SpeechRecognitionActivityTwoDB("send", 9));

			act2.add(new SpeechRecognitionActivityTwoDB("sms", 0));
			act2.add(new SpeechRecognitionActivityTwoDB("show", 1));
			act2.add(new SpeechRecognitionActivityTwoDB("record", 2));
			act2.add(new SpeechRecognitionActivityTwoDB("browse", 3));
			act2.add(new SpeechRecognitionActivityTwoDB("play", 4));
			act2.add(new SpeechRecognitionActivityTwoDB("speak", 5));
			act2.add(new SpeechRecognitionActivityTwoDB("dictate", 6));
			act2.add(new SpeechRecognitionActivityTwoDB("note", 7));
			act2.add(new SpeechRecognitionActivityTwoDB("draw", 8));
			act2.add(new SpeechRecognitionActivityTwoDB("mail", 9));
			act2.add(new SpeechRecognitionActivityTwoDB("whatsapp", 10));

		}
	}
		
	public static int recognize(ArrayList<String> alr){
		ArrayList<Integer> res =new ArrayList<Integer>();
		res.clear();
		int lv;
		int p1=-1,p2 = -1;
		int i,j;
	for (j=0;j<act1.size();j++){	
		for (i=0;i<alr.size();i++){
			String[] parse = alr.get(i).split("\\s+");
			if (parse.length==2){
				lv=SpeechRecognitionLev.distance(parse[0], act1.get(j).OPcode);
				if (lv==0){p1=j; break;}
								}
							}	
						}
	
	for (j=0;j<act2.size();j++){	
		for (i=0;i<alr.size();i++){
			String[] parse = alr.get(i).split("\\s+");
			if (parse.length==2){
				lv=SpeechRecognitionLev.distance(parse[1], act2.get(j).OPcode);
				if (lv==0){p2=j; break;}
							}
						}	
					}
	
	if (p1<act1.size() && p1!=-1 && p2<act2.size() && p2!=-1){
		for (i=0;i<acceptedValues.length;i++){
			if (100*act1.get(p1).getOpcodeCmd()+act2.get(p2).getOpcodeCmd()==
					acceptedValues[i]){
				return 100*act1.get(p1).getOpcodeCmd()+act2.get(p2).getOpcodeCmd() ;
			}
		}
		 return -1;		
		}
	else return -1;
	}
	
	
	
	
}
