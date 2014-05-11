package com.drew.BatText;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;


public class AlertTypeDialogFragment extends DialogFragment {
	public String mAlertType = "";

    public static AlertTypeDialogFragment newInstance(String alertId) {
    	AlertTypeDialogFragment frag = new AlertTypeDialogFragment();
         Bundle args = new Bundle();
         args.putString("alertId", alertId);
         frag.setArguments(args);
         return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	final String alertId = getArguments().getString("alertId");
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alerttype_dialog_title)
               .setItems(R.array.alerttype_options, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                	   Resources res = getResources();
                	   String[] alertTypes = res.getStringArray(R.array.alerttype_options);
                	   mAlertType = alertTypes[which];
                	   ((BatTextActivity)getActivity()).setAlertType(alertTypes[which], alertId);
                	   
                	   //Toast.makeText(getActivity().getBaseContext(), alertTypes[which], Toast.LENGTH_SHORT).show();
               }
        });
        return builder.create();
    }
    
    public String getAlertType() {
    	return mAlertType;
    }
}