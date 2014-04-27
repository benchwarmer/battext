package com.drew.BatText;

import android.app.PendingIntent;
import android.app.Service;
import android.support.v4.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class BatTextService extends Service {
	public static final int ONGOING_NOTIFICATION_ID = 808;
	private static final String debugTag = "BatTextService";
	private BroadcastReceiver receiver;
	
	public BatTextService() {
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		//super.onCreate();
		Toast.makeText(this, "BatTextService.onCreate called.", Toast.LENGTH_LONG).show();
		
		// Register the battery change receiver
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        receiver = new BatTextReceiver();
        registerReceiver(receiver, filter);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		//super.onStartCommand(intent, flags, startId);
		Toast.makeText(this, "BatTextService.onStartCommand called.", Toast.LENGTH_LONG).show();
		
		// TODO: mark service as started
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean runInForeground = sharedPref.getBoolean(SettingsActivity.KEY_PREF_FOREGROUND, false);
		Log.d(debugTag, "runInForeground: " + runInForeground);
		Toast.makeText(this, "runInForeground: " + runInForeground, Toast.LENGTH_LONG).show();
		if(runInForeground) {
			Intent resultIntent = new Intent(this, BatTextActivity.class);
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack
			stackBuilder.addParentStack(BatTextActivity.class);
			// Adds the Intent to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			// Gets a PendingIntent containing the entire back stack
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			// Build Notification
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			builder.setContentIntent(resultPendingIntent);
			// Start service in foreground with notification
			startForeground(ONGOING_NOTIFICATION_ID, builder.build());
		}
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Battery Service was killed. You will need to restart the service in order for your messages to send.", Toast.LENGTH_LONG).show();
		super.onDestroy();
		this.unregisterReceiver(this.receiver);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(SettingsActivity.KEY_PREF_ENABLE_SERVICE, false);
		editor.commit();
	}
}
