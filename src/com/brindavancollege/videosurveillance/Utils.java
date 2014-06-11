package com.brindavancollege.videosurveillance;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

 public class Utils   {

	 public static boolean USE_RGB = true;
	    public static boolean USE_LUMA = false;
	    public static boolean USE_STATE = false;

	    // Which photos to save
	    public static boolean SAVE_PREVIOUS = false;
	    public static boolean SAVE_ORIGINAL = true;
	    public static boolean SAVE_CHANGES = true;

	    // Time between saving photos
	    public static int PICTURE_DELAY = 10000;
	    public static boolean ISSMSSENT = false ;
	    
		static PowerManager.WakeLock mWakeLock;
	   
	    
	  
	   
	 
	/* static String getUserNameFunction(Context mContext){
		 SharedPreferences getUserName= PreferenceManager.getDefaultSharedPreferences(mContext) ;
			
				 String userNumber =getUserName.getString("username","");
				 return userNumber;
	 }
	 
	 */
	 
	public static String getUserPhoneNumberFunction1(Context mContext){
		 SharedPreferences getUserNumber= PreferenceManager.getDefaultSharedPreferences(mContext) ;
			
				 String userNumber =getUserNumber.getString("number1","");
				 return userNumber;
	 }
	 
	public static String getUserPhoneNumberFunction2(Context mContext){
		 SharedPreferences getUserNumber= PreferenceManager.getDefaultSharedPreferences(mContext) ;
			
				 String userNumber =getUserNumber.getString("number2","");
				 return userNumber;
	 }
	
	public static String getUserPhoneNumberFunction3(Context mContext){
		 SharedPreferences getUserNumber= PreferenceManager.getDefaultSharedPreferences(mContext) ;
			
				 String userNumber =getUserNumber.getString("number3","");
				 return userNumber;
	 }
	
	
	 static String CallAlertFunction(Context mContext){
		 SharedPreferences callAlert= PreferenceManager.getDefaultSharedPreferences(mContext) ;
			//	 TextView usernameText=(TextView) findViewById(R.id.textView1) ;
				 String callAlertVar =callAlert.getString("callAlert","");
				 return callAlertVar;
	 }
	 
	 static String SmsAlertFunction(Context mContext){
		 SharedPreferences SmsAlert= PreferenceManager.getDefaultSharedPreferences(mContext) ;
			//	 TextView usernameText=(TextView) findViewById(R.id.textView1) ;
				 String userNumber =SmsAlert.getString("smsAlert","");
				 return userNumber;
	 }
	 
	 static String getSceneFunction(Context mContext){
		 SharedPreferences getScene= PreferenceManager.getDefaultSharedPreferences(mContext) ;
			//	 TextView usernameText=(TextView) findViewById(R.id.textView1) ;
				 String userNumber =getScene.getString("scene","");
				 return userNumber;
	 }
	 
	 static String getVideoFunction(Context mContext){
		 SharedPreferences  getVideo= PreferenceManager.getDefaultSharedPreferences(mContext) ;
			//	 TextView usernameText=(TextView) findViewById(R.id.textView1) ;
				 String userNumber =getVideo.getString("video","");
				 return userNumber;
	 }
	 
	 
/*	static  boolean checkCameraHardware(Context mContext){
		   
		
			if(mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			
				return true;
			}
			else
			{
				return false;
			}
		}  */
 
static AlertDialog getDialogBuilder(Context mcontext,String title,String message,String negativeTitle,String positiveTitle)	 
{
	AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(  mcontext) ;
	               alertDialogBuilder.setTitle(title);
	               alertDialogBuilder.setMessage(message);
	               
	               alertDialogBuilder.setPositiveButton(positiveTitle,new DialogInterface.OnClickListener(){
	            	   public void onClick(DialogInterface dialog,int id){
	            		   dialog.cancel();
	               
	           
	            	   }
	               });
	            		   
	
if(negativeTitle!=null){
	               
	               alertDialogBuilder.setNegativeButton(positiveTitle,new DialogInterface.OnClickListener(){
	            	   public void onClick(DialogInterface dialog,int id){
	            		   dialog.cancel();
	               
	           
	            	   }
	               } );
}
					AlertDialog alertDialog=alertDialogBuilder.create();

return alertDialog;
}


 static boolean checkCameraHardware1(Context mContext) {
	 if(mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			
			return true;
		}
		else
		{
			return false;
		}
	

	 
 }

private static final AtomicBoolean phoneInMotion = new AtomicBoolean(false);

 public static boolean isPhoneInMotion() {
     return phoneInMotion.get();
 }

 public static void setPhoneInMotion(boolean bool) {
     phoneInMotion.set(bool);
 }
 
 public static void sendSMS(Context mContext){
	// String userName=getUserNameFunction(mContext) ;
	 String phoneNo1= getUserPhoneNumberFunction1(mContext) ;
	 String phoneNo2= getUserPhoneNumberFunction2(mContext) ;
	 String phoneNo3= getUserPhoneNumberFunction3(mContext) ;
	  
	 String sms = " intirusen hes okured ";

	  try {
		
		boolean isMessaging= PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("smsAlert", false);
		if(isMessaging==true){
			SmsManager smsManager = SmsManager.getDefault();
			 if(phoneNo1 != null){
				 smsManager.sendTextMessage(phoneNo1, null, sms, null, null);
				 Toast.makeText(mContext, "SMS Sent to contact 1",Toast.LENGTH_LONG).show();
			 }
			 if(phoneNo2 != null){
				 smsManager.sendTextMessage(phoneNo2, null, sms, null, null);
				 Toast.makeText(mContext, "SMS Sent to contact 2",Toast.LENGTH_LONG).show();
			 }
			 if(phoneNo3 != null){
				 smsManager.sendTextMessage(phoneNo3, null, sms, null, null);
				 Toast.makeText(mContext, "SMS Sent to contact 3",Toast.LENGTH_LONG).show();
			 }
			
	            
		}
		
		
		
		
	  } catch (Exception e) {
		Toast.makeText(mContext,"SMS failed, please try again later!",Toast.LENGTH_LONG).show();
		e.printStackTrace();
	  }
	  
	 
 }
 
 public static void makeCall(Context mContext){
	 String phoneNo= getUserPhoneNumberFunction1(mContext) ;
	 
	 boolean isCalling= PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("callAlert", false);
	if(isCalling==true){
	 Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("Tel:"+"9035674873"));
		callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
		mContext.startActivity(callIntent) ;
	}
	 
 }


 public static void aquireWakeLock(Activity activity){
		final PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		mWakeLock.acquire();
	}

	public static void releaseWakeLock(Activity activity){
		mWakeLock.release();
	}


 
 
 

	 
}
