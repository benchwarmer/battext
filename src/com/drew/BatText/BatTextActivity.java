package com.drew.BatText;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BatTextActivity extends Activity {
	Context context = this;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Create a Card
		Card card = new Card(this, R.layout.row_card);
		 
		// Create a CardHeader
		CardHeader header = new CardHeader(this);
		header.setTitle("Hello world");
		         
		card.setTitle("Simple card demo");
		CardThumbnail thumb = new CardThumbnail(this);
		thumb.setDrawableResource(R.drawable.ic_launcher);
		         
		card.addCardThumbnail(thumb);
		         
		// Add Header to card
		card.addCardHeader(header);
		 
		// Set card in the cardView
		CardView cardView = (CardView) findViewById(R.id.carddemo);
		cardView.setCard(card);
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
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