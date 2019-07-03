package com.danielthat.loudspeaker;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
 
public class Loudspeaker extends Activity {
 
	// http://danielthat.blogspot.com/2013/06/android-make-phone-call-with-speaker-on.html
	
 Button mButton;
 EditText mEdit;
 TelephonyManager manager;
 StatePhoneReceiver myPhoneStateListener;
 boolean callFromApp=false; // To control the call has been made from the application
 boolean callFromOffHook=false; // To control the change to idle state is from the app call
 
 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.layout_loudspeaker);
 
  //To be notified of changes of the phone state create an instance
  //of the TelephonyManager class and the StatePhoneReceiver class
  myPhoneStateListener = new StatePhoneReceiver(this);
  manager = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
 
  mEdit   = (EditText)findViewById(R.id.editText1);
       
  mButton = (Button) findViewById(R.id.button1);
  mButton.setOnClickListener(new View.OnClickListener() {
   
   public void onClick(View v) {
   
    String phoneNumber = mEdit.getText().toString();
    manager.listen(myPhoneStateListener,
    PhoneStateListener.LISTEN_CALL_STATE); // start listening to the phone changes
    callFromApp=true;
    Intent i = new Intent(android.content.Intent.ACTION_CALL,
                          Uri.parse("tel:+" + phoneNumber)); // Make the call       
    startActivity(i);   
   } 
  }); 
 }
 
 
 // Monitor for changes to the state of the phone
 public class StatePhoneReceiver extends PhoneStateListener {
     Context context;
     public StatePhoneReceiver(Context context) {
         this.context = context;
     }
 
     @Override
     public void onCallStateChanged(int state, String incomingNumber) {
         super.onCallStateChanged(state, incomingNumber);
         
         switch (state) {
         
         case TelephonyManager.CALL_STATE_OFFHOOK: //Call is established
          if (callFromApp) {
              callFromApp=false;
              callFromOffHook=true;
                   
              try {
                Thread.sleep(500); // Delay 0,5 seconds to handle better turning on loudspeaker
              } catch (InterruptedException e) {
              }
           
              //Activate loudspeaker
              AudioManager audioManager = (AudioManager)
                                          getSystemService(Context.AUDIO_SERVICE);
              audioManager.setMode(AudioManager.MODE_IN_CALL);
              audioManager.setSpeakerphoneOn(true);
           }
           break;
         
        case TelephonyManager.CALL_STATE_IDLE: //Call is finished
          if (callFromOffHook) {
                callFromOffHook=false;
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_NORMAL); //Deactivate loudspeaker
                manager.listen(myPhoneStateListener, // Remove listener
                      PhoneStateListener.LISTEN_NONE);
             }
          break;
         }
     }
 }
}