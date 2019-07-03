package com.example.downloadfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownloadFileAsync extends AsyncTask<String, String, String> {
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

	private Context context;

	public DownloadFileAsync(Context context) 
	{
	    this.context = context;
	     mProgressDialog = new ProgressDialog(context);
	     mProgressDialog.setMessage("Downloading file..");
	     mProgressDialog.setIndeterminate(false);
	     mProgressDialog.setMax(100);
	     mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	     mProgressDialog.setCancelable(true);

	}

	@Override
	protected void onPreExecute() {
	    super.onPreExecute();
	    mProgressDialog.show();
	}

	@Override
	protected String doInBackground(String... url) {
	    return null;
	}

	protected void onProgressUpdate(String... progress) {
	     Log.d("ANDRO_ASYNC",progress[0]);
	     mProgressDialog.setProgress(Integer.parseInt(progress[0]));
	}

	@Override
	protected void onPostExecute(String unused) {
	    mProgressDialog.dismiss();
	}


}
