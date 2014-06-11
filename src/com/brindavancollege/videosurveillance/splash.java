package com.brindavancollege.videosurveillance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class splash extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashx) ;
	
		 Thread timer = new Thread(){
			 public void run() {
				 try{
				    sleep(2000) ; 
					 Intent firstActivity = new Intent(splash.this,LoginActivity.class) ;
					 startActivity(firstActivity) ;
				 }
				 catch(InterruptedException e){
					 e.printStackTrace() ; 
				 }
				 finally{
					 finish() ;
				 }
			 }
			
			
		 };
		 
		 timer.start();
	
	}
}