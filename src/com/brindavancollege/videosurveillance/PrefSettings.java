package com.brindavancollege.videosurveillance;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;
import android.preference.Preference.OnPreferenceClickListener;

public class PrefSettings extends PreferenceActivity {
	
	
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	addPreferencesFromResource(R.xml.prefs);
	setUpReferences();
	setGallery();
	
	
	}
	
	
	private void setUpReferences(){
		SwitchPreference smsAlertPreference =(SwitchPreference) findPreference("smsAlert");
		Boolean curValue1=smsAlertPreference.getSharedPreferences().getBoolean("smsAlert", false);
		
		smsAlertPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				// TODO Auto-generated method stub
				Boolean curValue = (Boolean)arg1;
				Toast.makeText(PrefSettings.this, "sms alert "+curValue, Toast.LENGTH_SHORT).show();
				
				
				if(curValue==true){
			
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
				long[] pattern = {0,500 } ;
				v.vibrate(pattern,-1) ;
			} 
				else
				{
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
					long[] pattern = {0,500 } ;
					v.vibrate(pattern,-1) ;	
				}
			return true;
			}
		});
		
		
		
		
		
}	

	
	
	
	private void setGallery(){
	
	Preference imagePreference = (Preference) findPreference("image");
	imagePreference.setOnPreferenceClickListener(new OnPreferenceClickListener(){
        public boolean onPreferenceClick(Preference preference){
        	Toast.makeText(PrefSettings.this,"starting settings....",Toast.LENGTH_SHORT).show();
			startActivity(new Intent(PrefSettings.this,ImageActivity.class)) ;
			return true;
        }
   });

	
	Preference videoPreference = (Preference) findPreference("video");
	videoPreference.setOnPreferenceClickListener(new OnPreferenceClickListener(){
        public boolean onPreferenceClick(Preference preference){
        	Toast.makeText(PrefSettings.this,"starting settings....",Toast.LENGTH_SHORT).show();
			startActivity(new Intent(PrefSettings.this,VideoActivity.class)) ;
			return true;
        }
   });
	
	}







}	



	
		
		
	

	
	
	


