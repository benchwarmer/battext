package com.drew.BatText;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnSwipeListener;
import it.gmariotti.cardslib.library.internal.Card.OnUndoSwipeListListener;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;
import it.gmariotti.cardslib.library.view.listener.UndoBarController;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BatTextActivity extends Activity {
	Context context = this;
	private CardArrayAdapter mCardArrayAdapter;
    private UndoBarController mUndoBarController;
    private CardListView mListView;
	
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardlist_undo);

		ArrayList<Card> cards = new ArrayList<Card>();
		
		for(int i=0; i<5; i++) {
			// Create a Card
			Card card = new Card(this, R.layout.row_card);
			card.setId("" + i);
			 
			// Create a CardHeader
			CardHeader header = new CardHeader(this);
			header.setTitle("Hello world " + i);
			header.setButtonOverflowVisible(true);
			header.setPopupMenu(R.menu.cardpopup, new CardHeader.OnClickCardHeaderPopupMenuListener() {
	            @Override
	            public void onMenuItemClick(BaseCard card, MenuItem item) {
	                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
	            }
	        });
			         
			card.setTitle("Simple card demo");
			CardThumbnail thumb = new CardThumbnail(this);
			thumb.setDrawableResource(R.drawable.ic_launcher);
			         
			card.addCardThumbnail(thumb);
			         
			// Add Header to card
			card.addCardHeader(header);
			
			// Set card swipable
			card.setSwipeable(true);
		    card.setOnSwipeListener(new OnSwipeListener() {
		            @Override
		            public void onSwipe(Card card) {
		                Toast.makeText(context, "Removed card=" + card.getTitle(), Toast.LENGTH_SHORT).show();
		            }
		    });
		    
		    cards.add(card); 
		}
	    
	    mCardArrayAdapter = new CardArrayAdapter(this,cards);
	    mCardArrayAdapter.setEnableUndo(true);
	    mListView = (CardListView) findViewById(R.id.cardlist);
	    if (mListView!=null){
            mListView.setAdapter(mCardArrayAdapter);
        }
		 
		// Set card in the cardView
		//CardView cardView = (CardView) findViewById(R.id.carddemo);
		//cardView.setCard(card);
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