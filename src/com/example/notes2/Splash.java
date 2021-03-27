package com.example.notes2;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;


public class Splash extends Activity {
	ImageView image;
	int i=0; 
	MediaPlayer player;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		image= (ImageView)findViewById(R.id.imageView1);
		 image.setImageDrawable(getResources().getDrawable(R.drawable.notepad8a));
		 player = MediaPlayer.create(this, R.raw.guns);
			player.setLooping(false); // Set looping
			player.start();
		
		 final Timer timer = new Timer();
		 timer.schedule(new TimerTask() {
			      
			 @Override
		           public void run() {
		        	   i++;
		        	   if (i==2) {
		                   timer.cancel();
		                   player.stop();
		                   player.release();
		                   Splash.this.finish();
		                   }
		                  // TODO task to be done every 666 milliseconds
		                  // after a lapse time of 555 milliseconds
		           }
		 }, 1555, 1666);

	}

}
