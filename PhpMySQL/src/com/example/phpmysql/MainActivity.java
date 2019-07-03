package com.example.phpmysql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	/*
	 * https://www.simplifiedcoding.net/android-json-parsing-tutorial/
	 */
	
	ListView listView;
	final String urlWebService = "https://pizzaria2.000webhostapp.com/android_connect/get_all_products.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//listView = (ListView) findViewById(R.id.listView);
		
		getJSON("https://pizzaria2.000webhostapp.com/android_connect/get_all_products.php");
		
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//this method is actually fetching the json string
    private void getJSON(final String urlWebService) {
        /*
        * As fetching the json string is a network operation
        * And we cannot perform a network operation in main thread
        * so we need an AsyncTask
        * The constrains defined here are 
        * Void -> We are not passing anything 
        * Void -> Nothing at progress update as well
        * String -> After completion it should return a string and it will be the json string
        * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait 
            //as network operation may take some time 
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {
                
                try {
                    //creating a URL
                    URL url = new URL(urlWebService);
                    
                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    
                    //StringBuilder object to read the string from the service 
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;
                    
                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        
                        //appending it to string builder
                        sb.append(json + "\n");
                    }
                    
                    String jsonWebservice = sb.toString().trim();
                    String[] parts = jsonWebservice.split("<html");
                    jsonWebservice = parts[0];
                    //finally returning the read string 
                    //return sb.toString().trim();
                    return jsonWebservice;
                } catch (Exception e) {
                    return null;
                }

            }
            
            //this method will be called after execution 
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
}
