package com.drew.BatText;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;

public class BatTextReceiver extends BroadcastReceiver {

	private static final int BATTEXT_ID = 1;

	@SuppressWarnings("unused")
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {

		Toast.makeText(context, "Successful Broadcast!", Toast.LENGTH_LONG).show();
		
		//int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		//int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
		boolean isPresent = intent.getBooleanExtra("present", false);
        String technology = intent.getStringExtra("technology");
        int plugged = intent.getIntExtra("plugged", -1);
        int scale = intent.getIntExtra("scale", -1);
        int health = intent.getIntExtra("health", 0);
        int status = intent.getIntExtra("status", 0);
        int rawlevel = intent.getIntExtra("level", -1);
        int level = (rawlevel * 100) / scale;
        
        Log.d("battext", "Level: " + level );
        
        

		// Get Saved Preferences
		SharedPreferences settings = context.getSharedPreferences(
				BatTextActivity.PREFS_NAME, 0);
		String number = settings.getString("number", "");
		String message = settings.getString("message", "");

		// Send the Text
		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(number, null, message, null, null);

		Resources res = context.getResources();
		
		// Notification
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