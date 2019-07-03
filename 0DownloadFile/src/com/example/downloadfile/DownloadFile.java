package com.example.downloadfile;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DownloadFile extends Activity {
	
	
	/*
	 * https: //stackoverflow.com/questions/6119305/android-how-to-run-asynctask-from-different-class-file/9527473
	 */ 
	private static String fileName = "yo.html";
	private static String fileURL = "http://mydomain.com/tabletcms/tablets/tablet_content/000002/form/Best%20Form%20Ever/html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new DownloadFileAsync(this).execute(fileURL,fileName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
