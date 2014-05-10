package com.drew.BatText;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;

public class BatTextReceiver extends BroadcastReceiver {
	
	private String tag = "BatTextReceiver"; 

	private static final int BATTEXT_ID = 1;

	@SuppressWarnings({ "unused", "deprecation" })
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Toast.makeText(context, "Successful Broadcast!", Toast.LENGTH_LONG).show();
		
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);
		
		boolean isPresent = batteryStatus.getBooleanExtra("present", false);
        String technology = batteryStatus.getStringExtra("technology");
        int plugged = batteryStatus.getIntExtra("plugged", -1);
        int scale = batteryStatus.getIntExtra("scale", -1);
        int health = batteryStatus.getIntExtra("health", 0);
        int status = batteryStatus.getIntExtra("status", 0);
        int rawlevel = batteryStatus.getIntExtra("level", -1);
        int level = (rawlevel * 100) / scale;
        
        Log.d("battext", "Level: " + level );
        
        

		// Get Saved Preferences
		

		// Send the Text
		//SmsManager sm = SmsManager.getDefault();
		//sm.sendTextMessage(number, null, message, null, null);

		
		
		// Notification
        Resources res = context.getResources();
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.ic_stat_notif)
		        .setContentTitle(res.getString(R.string.notif_contentTitle) + " " + level)
		        .setContentText(res.getString(R.string.notif_contentText))
		        .setTicker(res.getString(R.string.notif_ticker))
		        .setAutoCancel(true)
		        
		        ;
		
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, BatTextActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(BatTextActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(BATTEXT_ID, mBuilder.build());
		
	}
}