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
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
		setContentView(R.layout.list_alertcard_undo);

		ArrayList<Card> cards = new ArrayList<Card>();
		for(int i=0;i<3;i++) {
			// Create a Card
			Card card = new AlertCard(this);
			card.setId("" + i);
		    
		    cards.add(card);
		}
	    
	    mCardArrayAdapter = new CardArrayAdapter(this,cards);
	    mCardArrayAdapter.setEnableUndo(true);
	    mListView = (CardListView) findViewById(R.id.cardlist);
	    if (mListView!=null){
            mListView.setAdapter(mCardArrayAdapter);
        }
	    

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
	
	/****** Set Alert Value Methods **************/
	public void setAlertType(String alertType, String alertId) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString("alertType_" + alertId, alertType);
		editor.commit();
		int positionFromId =  java.lang.Integer.parseInt(alertId);  
		AlertCard card = (AlertCard) mCardArrayAdapter.getItem(positionFromId);
		card.setAlertType(alertType, card);
	}
	
	public String getAlertType(String alertType, String alertId) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString("alertType_" + alertId, "");
	}
	
	
	
	
	/***** Alert Card Class ***********************/
	public class AlertCard extends Card {
		private Card thisCard = this;
		protected TextView mAlertType;
		protected TextView mBatteryPercent;
		
		

        /**
         * Constructor with a custom inner layout
         * @param context
         */
        public AlertCard(Context context) {
            this(context, R.layout.alertcard_inner_content);
        }

        /**
         *
         * @param context
         * @param innerLayout
         */
        public AlertCard(Context context, int innerLayout) {
            super(context, innerLayout);
            init();
        }
        
        /**
         * Init
         */
        private void init(){

        	CardHeader header = new CardHeader(mContext);
			header.setTitle("Alert");
			header.setButtonOverflowVisible(true);
			header.setPopupMenu(R.menu.cardpopup, new CardHeader.OnClickCardHeaderPopupMenuListener() {
	            @Override
	            public void onMenuItemClick(BaseCard card, MenuItem item) {
	                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
	            }
	        });
			addCardHeader(header);
			setSwipeable(true);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
        	 int mId = java.lang.Integer.parseInt(this.getId());
//            //Retrieve elements
//            mTitle = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_title);
//            mSecondaryTitle = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_secondaryTitle);
//            mRatingBar = (RatingBar) parent.findViewById(R.id.carddemo_myapps_main_inner_ratingBar);
//
//
//            if (mTitle!=null)
//                mTitle.setText(R.string.demo_custom_card_google_maps);
//
//            if (mSecondaryTitle!=null)
//                mSecondaryTitle.setText(R.string.demo_custom_card_googleinc);
//
//            if (mRatingBar!=null)
//                mRatingBar.setNumStars(5);
//                mRatingBar.setMax(5);
//                mRatingBar.setRating(4.7f);
        	
        	mAlertType = (TextView) parent.findViewById(R.id.main_inner_alerttype);
        	if(mAlertType!=null) {
        		String alertTypeStr = PreferenceManager.getDefaultSharedPreferences(context).getString("alertType_" + thisCard.getId(), "");
        		if (alertTypeStr != "") {
        			mAlertType.setText(alertTypeStr);
        		}
        		mAlertType.setOnClickListener(new View.OnClickListener() {
      		      @Override
      		      public void onClick(View v) {
      		    	AlertTypeDialogFragment newFragment = AlertTypeDialogFragment.newInstance(thisCard.getId());
					newFragment.show(getFragmentManager(), "alertTypeDialog");
					
      		      }
      	
      		    });
        	} else {
        		Log.d("AlertCard", "TextView was not found.");
        	}

        }
        
        public void setAlertType(String alertType, AlertCard card) {
        	//int mId = java.lang.Integer.parseInt(this.getId());
        	//mAlertType = (TextView) .findViewById(R.id.main_inner_alerttype);
        	TextView alertTypeView = (TextView) mCardView.findViewById(R.id.main_inner_alerttype);
        	alertTypeView.setText(alertType);
        	//mCardView.refreshCard(card);
        	
        }
    }

}