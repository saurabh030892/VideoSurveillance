package com.brindavancollege.videosurveillance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class CounterActivity extends Activity {
	
	public TextView text ;
	public CountDownTimer countDownTimer ;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.counterlayout) ;
	
	text = (TextView) findViewById(R.id.text1) ;
	
	countDownTimer = new CountDownTimer(16000, 1000) {

	     public void onTick(long millisUntilFinished) {
	         text.setText("" + millisUntilFinished / 1000);
	     }

	     public void onFinish() {
	         text.setText("GO");
	         startActivity(new Intent(CounterActivity.this,MotionDetectionActivity.class));
	     }
	  }.start();
    

	
	
	
}



@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	finish();
}




}