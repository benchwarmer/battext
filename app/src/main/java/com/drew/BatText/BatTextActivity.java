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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.provider.ContactsContract;
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
    private String mCurrentCardIdStr;
    private int mCurrentCardIdInt;
    
    public String prefAlertTypePrefix = "alertCard_alertType_";
    public String prefContactNamePrefix = "alertCard_contact_name_";
    public String prefContactNumberPrefix = "alertCard_contact_number_";
    public String prefContactEmailPrefix = "alertCard_contact_email_";
    
    private static final int PICK_CONTACT = 1234;
    private static final int PICK_EMAIL = 1235;
    
    private String[] alertTypeOptions;
    private String alertTypeOptions_text;
    private String alertTypeOptions_email;
    private String alertTypeOptions_other;
    
	
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		alertTypeOptions = context.getResources().getStringArray(R.array.alerttype_options);
		alertTypeOptions_text = alertTypeOptions[0];
		alertTypeOptions_email = alertTypeOptions[1];
		alertTypeOptions_other = alertTypeOptions[2];
		
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
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	  switch (reqCode) {
	    case (PICK_CONTACT) :
	      if (resultCode == Activity.RESULT_OK) {
	        Uri contactData = data.getData();
	        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
	        if (c.moveToFirst()) {
	          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	          String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	          setContact(name, number, "", mCurrentCardIdStr, reqCode); 
	          
	        }
	      }
	      break;
	    case (PICK_EMAIL) :
		      if (resultCode == Activity.RESULT_OK) {
		        Uri contactData = data.getData();
		        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
		        if (c.moveToFirst()) {
		          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		          String email = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
		          setContact(name, "", email, mCurrentCardIdStr, reqCode); 
		          
		        }
		      }
		      break; 
	  }
	}
	
	/****** Set Alert Value Methods **************/
	public void setAlertType(String alertType, String alertId) {
		int positionFromId =  java.lang.Integer.parseInt(alertId);  
		AlertCard card = (AlertCard) mCardArrayAdapter.getItem(positionFromId);
		card.setAlertType(alertType);
	}
	
	public String getAlertType(String alertId) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(prefAlertTypePrefix + alertId, "");
	}
	
	public void setContact(String name, String number, String email, String alertId, int reqType) {
		int positionFromId =  java.lang.Integer.parseInt(alertId);  
		AlertCard card = (AlertCard) mCardArrayAdapter.getItem(positionFromId);
		card.setContact(name, number, email, reqType);
	}
	
	public String getContact(String alertId) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(prefContactNamePrefix + alertId, "");
	}
	
	
	
	
	/***** Alert Card Class ***********************/
	public class AlertCard extends Card {
		private Card thisCard = this;
		protected TextView mAlertType;
		protected TextView mContact;
		protected TextView mBatteryPercent;
		
		private String alertTypeStr;
		private String contactStr;

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
        	 
        	// Setup alertType
        	mAlertType = (TextView) parent.findViewById(R.id.main_inner_alerttype);
        	if(mAlertType!=null) {
        		alertTypeStr = PreferenceManager.getDefaultSharedPreferences(context).getString(prefAlertTypePrefix + thisCard.getId(), "");
        		if (!alertTypeStr.equals("")) {
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
        		Log.d("AlertCard", "AlertType TextView was not found.");
        	}
        	
        	// Setup contact
        	mContact = (TextView) parent.findViewById(R.id.main_inner_contact);
        	if(mContact!=null) {
        		contactStr = PreferenceManager.getDefaultSharedPreferences(context).getString(prefContactNamePrefix + thisCard.getId(), "");
        		if (!contactStr.equals("")) {
        			mContact.setText(contactStr);
        		}
        		mContact.setOnClickListener(new View.OnClickListener() {
      		      @Override
      		      public void onClick(View v) {
      		    	mCurrentCardIdStr = thisCard.getId();
      		    	Intent intent = new Intent(Intent.ACTION_PICK);
      		    	if(alertTypeStr.equals(alertTypeOptions_text)) {
	      		    	intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
	      		    	startActivityForResult(intent, PICK_CONTACT);
      		    	} else if (alertTypeStr.equals(alertTypeOptions_email)) {
      		    		intent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
	      		    	startActivityForResult(intent, PICK_EMAIL);
      		    	}
      		      }
      		    });
        	} 

        }
        
        public void setAlertType(String alertType) {
        	TextView alertTypeView = (TextView) mCardView.findViewById(R.id.main_inner_alerttype);
        	TextView contactView = (TextView) mCardView.findViewById(R.id.main_inner_contact);
        	
    		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    		
        	if(!alertTypeView.getText().equals(alertType)) {
        		alertTypeStr = alertType;
        		alertTypeView.setText(alertType);
            	contactView.setText(R.string.inner_special_text_1);
            	editor.putString(prefAlertTypePrefix + thisCard.getId(), alertType);
            	editor.putString(prefContactNumberPrefix + thisCard.getId(), getString(R.string.inner_special_text_1));
        		editor.putString(prefContactNamePrefix + thisCard.getId(), getString(R.string.inner_special_text_1));
        	}
        	editor.commit();
        }
        
        public void setContact(String name, String number, String email, int reqType) {
        	TextView contactView = (TextView) mCardView.findViewById(R.id.main_inner_contact);
        	contactView.setText(name);
        	SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        	editor.putString(prefContactNamePrefix + thisCard.getId(), name);
        	if(reqType == PICK_CONTACT) {
        		editor.putString(prefContactNumberPrefix + thisCard.getId(), number);
        	} else if (reqType == PICK_EMAIL) {
        		editor.putString(prefContactEmailPrefix + thisCard.getId(), email);
        	}
    		editor.commit();
        	
        }
    }

}