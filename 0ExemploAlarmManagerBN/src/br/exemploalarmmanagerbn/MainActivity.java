package br.exemploalarmmanagerbn;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// https://www.thiengo.com.br/alarmmanager-no-android-sua-app-executando-em-tempos-definidos
		setContentView(R.layout.activity_main);
		
		boolean alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("ALARME_DISPARADO"), PendingIntent.FLAG_NO_CREATE) == null);
		
		if(alarmeAtivo){
			Log.i("Script", "Novo alarme");
			
			Intent intent = new Intent("ALARME_DISPARADO");
			PendingIntent p = PendingIntent.getBroadcast(this, 0, intent, 0);
			
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(System.currentTimeMillis());
			c.add(Calendar.SECOND, 3);
			
			AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarme.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 5000, p);
		}
		else{
			Log.i("Script", "Alarme já ativo");
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		
		/*Intent intent = new Intent("ALARME_DISPARADO");
		PendingIntent p = PendingIntent.getBroadcast(this, 0, intent, 0);
		
		AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarme.cancel(p);*/
	}
}
