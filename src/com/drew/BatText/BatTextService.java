package com.drew.BatText;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

public class BatTextService extends Service {
	//private static final String ACTION = "Intent.ACTION_BATTERY_CHANGED";
	public static final String PREFS_NAME = "BatTextPrefsFile";
	private BroadcastReceiver receiver;
	
	public BatTextService() {
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return null;
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "Service onCreate Called!", Toast.LENGTH_LONG).show();
		super.onCreate();
		
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        receiver = new BatTextReceiver();
        registerReceiver(receiver, filter);
		/* final IntentFilter theFilter = new IntentFilter();
		theFilter.addAction(ACTION);
		this.registerReceiver(receiver, theFilter);
		*/
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Toast.makeText(this, "Service onStartCommand Called!", Toast.LENGTH_LONG).show();
		super.onStartCommand(intent, flags, startId);
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("batteryServiceStarted", true).commit();
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Battery Service was killed. You will need to restart the service in order for your messages to send.", Toast.LENGTH_LONG).show();
		super.onDestroy();
		this.unregisterReceiver(this.receiver);
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("batteryServiceStarted", false).commit();
	}
}
