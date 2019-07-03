package com.example.nfcdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;
import java.nio.charset.Charset;
import android.view.Menu;

public class MainActivity extends Activity implements CreateNdefMessageCallback {
	
	/*
	 * url
	 * android.googlesource.com/platform/development/+/2f33e1eae0671cc1aa3d367f8d900b532f6ca80f/samples/AndroidBeamDemo/src/com/example/android/beam/Beam.java
	 * 
	 * manijshrestha.wordpress.com/2014/07/23/using-android-beamnfc-to-transfer-data/
	 * 
	 * github.com/manijshrestha/AndroidNFCDemo/blob/master/app/src/main/java/com/manijshrestha/androidnfcdemo/NFCActivity.java
	 * github.com/manijshrestha/AndroidNFCDemo/blob/master/app/src/main/java/com/manijshrestha/androidnfcdemo/NFCDisplayActivity.java
	 * 
	 * 
	 */
	
	NfcAdapter mNfcAdapter;
    TextView textView;
    TextView mEditText;
    String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textView = (TextView) findViewById(R.id.textView);
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            textView.setText(new String(message.getRecords()[0].getPayload()));

        } else
            textView.setText("Waiting for NDEF Message");
    	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            textView.setText(new String(message.getRecords()[0].getPayload()));

        } else
            textView.setText("Waiting for NDEF Message");
        
    }
	
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		 	//String message = mEditText.getText().toString();
			String message = "R$00,00 Pagamento efetuado com sucesso !";
	        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
	        NdefMessage ndefMessage = new NdefMessage(ndefRecord);//API 16 Ndefmessage 4.1 Jelly beam
	        return ndefMessage;
            		/**
	          * The Android Application Record (AAR) is commented out. When a device
	          * receives a push with an AAR in it, the application specified in the AAR
	          * is guaranteed to run. The AAR overrides the tag dispatch system.
	          * You can add it back in to guarantee that this
	          * activity starts when receiving a beamed message. For now, this code
	          * uses the tag dispatch system.
	          */
	          //
	}
    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        textView = (TextView) findViewById(R.id.textView);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        textView.setText(new String(msg.getRecords()[0].getPayload()));
    }
    /**
     * Creates a custom MIME type encapsulated in an NDEF record
     *
     * @param mimeType
     */
    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }

}
