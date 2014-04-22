package com.drew.BatText;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class BatTextActivity extends Activity {
	public static final String PREFS_NAME = "BatTextPrefsFile";
	Button saveButton;
	EditText txtNumber;
	EditText txtMessage;
	Switch enableBatText;
	Context context = this;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//startService(serviceIntent);
		
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_MULTI_PROCESS);
		String savedNumber = settings.getString("number", "");
		String savedMessage = settings.getString("message", "");
		Boolean serviceStarted = settings.getBoolean("batteryServiceStarted", false);

		saveButton = (Button) findViewById(R.id.saveButton);
		txtNumber = (EditText) findViewById(R.id.txtNumber);
		txtMessage = (EditText) findViewById(R.id.txtMessage);
		enableBatText = (Switch) findViewById(R.id.enableBatText); 
		
		if (savedNumber != "") {
			txtNumber.setText(savedNumber);
			txtMessage.setText(savedMessage);
		}
		
		

		// Save Button Click Handler
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String num = txtNumber.getText().toString();
				String message = txtMessage.getText().toString();
				CharSequence toastText = "Information Saved.";
				int duration = Toast.LENGTH_SHORT;
				Context context = getApplicationContext();

				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("number", num);
				editor.putString("message", message);

				// Commit the edits
				editor.commit();

				Toast toast = Toast.makeText(context, toastText, duration);
				toast.show();
			}
		});
		
		if (serviceStarted) {
			enableBatText.setChecked(true);
		} else {
			enableBatText.setChecked(false);
		}
		
		// Switch handler
		enableBatText.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Intent serviceIntent = new Intent(context, BatTextService.class);
				if (isChecked) {
					// Start the battery listening service
					startService(serviceIntent);
				} else {
					// Kill the battery listening service
					stopService(serviceIntent);
				}
			}
		});
		

	}
	
	public void onPause(){
		super.onPause();
	}
	
	public void onResume(){
		super.onResume();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_MULTI_PROCESS);
		Boolean serviceStarted = settings.getBoolean("batteryServiceStarted", false);
		
		if (serviceStarted) {
			enableBatText.setChecked(true);
		} else {
			enableBatText.setChecked(false);
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
		Intent serviceIntent = new Intent(context, BatTextService.class);
		stopService(serviceIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            intent = new Intent(this, BatTextActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.menu_settings:
	        	intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}