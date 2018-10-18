package com.example.mybluetoothclientside;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SecondActivity extends Activity {
	private static MediaPlayer mediaPlayer;
	Timer timerForWaitRinging;
	Timer timer;
	Intent intent;
	Handler musicHandler;
	Runnable stopPlaybackRun;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
 	 	try {
			//mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/AndRecorder/moov.3gpp");
 	 		mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/AndRecorder/moov.mp3");
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 	 	try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

	@Override
	protected void onResume(){
		super.onResume();
		/*
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		//timerForWaitRinging = new Timer();
		//timerForWaitRinging.schedule(new waitRingingTask(), 5 * 1000);
		SystemClock.sleep(22000);
		//22 seconds numbers with 9 digits VIVO mobile phone carrier in Brazil Country Sao paulo region
		//19 seconds telephone numbers with 8 digits
		mediaPlayer.start();
		/*
		musicHandler = new Handler();
		stopPlaybackRun = new Runnable() {
		    public void run(){
		    	mediaPlayer.start();
		        //mediaPlayer.stop();
		        //mediaPlayer.release();
		    }    
		};
		musicHandler.postDelayed(stopPlaybackRun, 5 * 1000);
		*/
		//new Reminder(15);
		SystemClock.sleep(12000);
		//SystemClock.sleep(14000);
		finish();
     	
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mediaPlayer.stop();
     	mediaPlayer.release();
	}
	
	public class waitRinging {
		Timer timerForWaitRinging;
		public waitRinging(int secondsToWaitRinging){
			timerForWaitRinging = new Timer();
			timerForWaitRinging.schedule(new waitRingingTask(), secondsToWaitRinging * 1000);
		}
			
	}
	
	class waitRingingTask extends TimerTask {
		
		@Override
		public void run() {
			System.out.println("waited 5 seconds for ringing");
			timerForWaitRinging = new Timer();
			timerForWaitRinging.cancel();
			//intent = new Intent();
     	   	//intent.putExtra("ACTION_PLAY", "ACTION_PLAY");
			
			runOnUiThread(new Runnable() {
		           @Override
		           public void run() {
		        	   Toast.makeText(getApplicationContext(), "over 5 sec", Toast.LENGTH_SHORT).show();
		           }
		       });

		}
		
	}

	public class MyService extends Service implements MediaPlayer.OnPreparedListener {

		
		@Override
		public void onPrepared(MediaPlayer arg0) {
			mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
            if (intent.getAction().equals("ACTION_PLAY")) {
				mediaPlayer.start();

			}
			
		}

		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	
	public class Reminder {
	    Timer timer;
    	
	    public Reminder(int seconds) {
	    	timer = new Timer();
	    	//mediaPlayer.start();
	        System.out.println("Task scheduled.");
	        timer.schedule(new RemindTask(), seconds*1000);
	    }

	    class RemindTask extends TimerTask {
	        
	    	@Override
	    	public void run() {
	    		timer = new Timer();
	    		System.out.println("Time's up!");
	            mediaPlayer.stop();
	         	mediaPlayer.release();
	         	timer.cancel(); //Terminate the timer thread
	         	finish();
	        }
	    }
	   
	}
}