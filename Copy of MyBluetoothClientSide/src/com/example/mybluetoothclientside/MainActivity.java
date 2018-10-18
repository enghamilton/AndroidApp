package com.example.mybluetoothclientside;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.example.mybluetoothclientside.MyService;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
 
 private static final int REQUEST_ENABLE_BT = 1;
 public static String play_audio = "play";
 private static final String TAG = "ServicesDemo";
 
 BluetoothAdapter bluetoothAdapter;
 
 private UUID myUUID;
 private String myName;
 LinearLayout inputPane;
 EditText inputField;
 Button btnSend;

 TextView textInfo, textStatus;
 
 ThreadBeConnected myThreadBeConnected;
 ThreadConnected myThreadConnected;
 
 static MediaPlayer player;
 static final String url = "http://programmerguru.com/android-tutorial/wp-content/uploads/2013/04/hosannatelugu.mp3";
 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  textInfo = (TextView)findViewById(R.id.info);
  textStatus = (TextView)findViewById(R.id.status);
  
  inputPane = (LinearLayout)findViewById(R.id.inputpane);
  inputField = (EditText)findViewById(R.id.input);  
  
  btnSend = (Button)findViewById(R.id.send);
  btnSend.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View v) {
	    if(myThreadConnected!=null){
	     byte[] bytesToSend = ("play "+inputField.getText().toString()).getBytes();
	     myThreadConnected.write(bytesToSend);
	    }
   }
   
  });
  
  if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
   Toast.makeText(this, 
    "FEATURE_BLUETOOTH NOT support", 
    Toast.LENGTH_LONG).show();
            finish();
            return;
  }

  //generate UUID on web: http://www.famkruithof.net/uuid/uuidgen
  //have to match the UUID on the another device of the BT connection
  myUUID = UUID.fromString("ec79da00-853f-11e4-b4a9-0800200c9a66");
  myName = myUUID.toString();
  
  bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  if (bluetoothAdapter == null) {
   Toast.makeText(this, 
    "Bluetooth is not supported on this hardware platform", 
    Toast.LENGTH_LONG).show();
            finish();
            return;
  }
  
  String stInfo = bluetoothAdapter.getName() + "\n" +
      bluetoothAdapter.getAddress();
  textInfo.setText(stInfo);
 }
 
 @Override
 protected void onStart() {
  super.onStart();
  
  //Turn ON BlueTooth if it is OFF
  if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
  
  setup();
 }
 
 @Override
 public void onResume() {
     super.onResume();  // Always call the superclass method first
     /*
    player = new MediaPlayer();
 	player.setAudioStreamType(AudioManager.STREAM_MUSIC);
 	try {
 		//player.setDataSource(url);
 		//player.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/AndRecorder/moov.3gpp");
 		player.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/AndRecorder/moov.mp3");
 	} catch (IllegalArgumentException e) {
 		Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
 	} catch (SecurityException e) {
 		Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
 	} catch (IllegalStateException e) {
 		Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
 	} catch (IOException e) {
 		e.printStackTrace();
 	}
 	try {
 		player.prepare();
 	} catch (IllegalStateException e) {
 		Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
 	} catch (IOException e) {
 		Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
 	}
 	player.start();
  	
  	try {
 			Thread.sleep(6000);
 		} catch (InterruptedException e) {
 			e.printStackTrace();
 		}
  	
  	player.stop();
  	player.release();
      
     */
     }

 
 private void setup() {
  textStatus.setText("setup()");
  myThreadBeConnected = new ThreadBeConnected();
  myThreadBeConnected.start();
 }
 
 @Override
 protected void onDestroy() {
  super.onDestroy();
  
  if(myThreadBeConnected!=null){
   myThreadBeConnected.cancel();
  }
  
  if (player != null) {
		player.release();
		player = null;
	}
  
 }

 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  
  if(requestCode==REQUEST_ENABLE_BT){
   if(resultCode == Activity.RESULT_OK){
    setup();
   }else{
    Toast.makeText(this, 
     "BlueTooth NOT enabled", 
     Toast.LENGTH_SHORT).show();
             finish();
   }
  } 
 }


 private class ThreadBeConnected extends Thread {
  
  private BluetoothServerSocket bluetoothServerSocket = null;
  
  public ThreadBeConnected() {
   try {
    bluetoothServerSocket = 
      bluetoothAdapter.listenUsingRfcommWithServiceRecord(myName, myUUID);
    
    textStatus.setText("Waiting\n" 
     + "bluetoothServerSocket :\n"
     + bluetoothServerSocket);
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
  }

  @Override
  public void run() {
   BluetoothSocket bluetoothSocket = null;
   
   if(bluetoothServerSocket!=null){
    try {
     bluetoothSocket = bluetoothServerSocket.accept();
     
     BluetoothDevice remoteDevice = bluetoothSocket.getRemoteDevice();
     
     final String strConnected = "Connected:\n" +
       remoteDevice.getName() + "\n" +
       remoteDevice.getAddress();
     
     //connected
     runOnUiThread(new Runnable(){

      @Override
      public void run() {
       textStatus.setText(strConnected);
       inputPane.setVisibility(View.VISIBLE);
      }});
     
     startThreadConnected(bluetoothSocket);
     
    } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     
     final String eMessage = e.getMessage();
     runOnUiThread(new Runnable(){

      @Override
      public void run() {
       textStatus.setText("something wrong: \n" + eMessage);
      }});
    }
   }else{
    runOnUiThread(new Runnable(){

     @Override
     public void run() {
      textStatus.setText("bluetoothServerSocket == null");
     }});
   }
  }
  
  public void cancel() {
   
   Toast.makeText(getApplicationContext(), 
    "close bluetoothServerSocket", 
    Toast.LENGTH_LONG).show();
   
   try {
    bluetoothServerSocket.close();
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
        }
 }
 
private void startThreadConnected(BluetoothSocket socket){
  
  myThreadConnected = new ThreadConnected(socket);
  myThreadConnected.start();
 }
 
 private class ThreadConnected extends Thread {
  private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;
  
  public ThreadConnected(BluetoothSocket socket) {
   connectedBluetoothSocket = socket;
   InputStream in = null;
            OutputStream out = null;
            
            try {
    in = socket.getInputStream();
    out = socket.getOutputStream();
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
            
            connectedInputStream = in;
            connectedOutputStream = out;
  }

  @Override
  public void run() {
   byte[] buffer = new byte[1024];
            int bytes;
            
            while (true) {
             try {
     bytes = connectedInputStream.read(buffer);
     
     final String strReceived = new String(buffer, 0, bytes);
     final String msgReceived = String.valueOf(bytes) + 
       " bytes received:\n" 
       + strReceived;
     
     
    
     runOnUiThread(new Runnable(){

      @Override
      public void run() {
       textStatus.setText(msgReceived);
       String inputStreamed = textStatus.getText().toString();
       if(inputStreamed != null){
    	   //Intent audioPlay = new Intent(getApplicationContext(), SecondActivity.class);
    	   //startActivity(audioPlay);
    	   
    	   /*
    	    * instead of using startActivity(audioPlay)
    	    * for a second Activity use a service to play audio
    	    * such as ServiceDemo.apk https://www.protechtraining.com/blog/post/73
    	    * 
    	    * and use speechRecognition to hear 
    	    * setSpeakerPhoneOn(true) from the MyBluetoothServerSide
    	    */
    	   Log.d(TAG, "inputStreamed: starting service");
    	   Intent i= new Intent(getBaseContext(), MyService.class);
    	   startService(i);
    	   
			Integer timeInMills = 13000;
			//SystemClock.sleep(timeInMills);
			try {
				Thread.sleep(timeInMills);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			stopService(i);
       }
      }
      });
     
     //if(String.valueOf(bytes) == "4"){
     //if(1 == 1){
 	 	//AssetFileDescriptor afd = getApplicationContext().getAssets().openFd("teste.mp3");
     	//player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
 	 	//player = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier("teste","raw",getPackageName()));
 	 	
 	 	//Intent audioPlay = new Intent(getApplicationContext(), SecondActivity.class);
     	//startActivity(audioPlay);
 	 	
 	 	/*
 	 	player.setAudioStreamType(AudioManager.STREAM_MUSIC);
 	 	player.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/AndRecorder/moov.3gpp");
 	 	player.prepare();
 		player.start();
     	try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
     	player.stop();
     	player.release();
     	*/
     	
     //}
     
    } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     
     final String msgConnectionLost = "Connection lost:\n"
       + e.getMessage();
     runOnUiThread(new Runnable(){

      @Override
      public void run() {
       textStatus.setText(msgConnectionLost);
      }});
    }
            }
  }
  
  public void write(byte[] buffer) {
	   try {
	    connectedOutputStream.write(buffer);
	    connectedOutputStream.flush();
	   } catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   }
   }
  
  public void cancel() {
   try {
    connectedBluetoothSocket.close();
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
  }

 }
}