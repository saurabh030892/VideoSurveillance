package com.brindavancollege.videosurveillance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	   private EditText  username=null;
	   private EditText  password=null;
	   private TextView attempts;
	   private Button login;
	   int counter = 3;
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.loginactivity);
	      username = (EditText) findViewById(R.id.un) ;
	      password = (EditText) findViewById(R.id.pw);
	      attempts = (TextView) findViewById(R.id.attemptsLeft) ;
	      attempts.setText(Integer.toString(counter));
	      login = (Button)findViewById(R.id.loginButton);
	   }

	   public void login(View view){
	      if(username.getText().toString().equals("ad") && 
	      password.getText().toString().equals("pw")){
	      Toast.makeText(getApplicationContext(), "Redirecting...", 
	      Toast.LENGTH_SHORT).show();
	      startActivity(new Intent(LoginActivity.this,MainActivity.class)) ;
	   }	
	   else{
	      Toast.makeText(getApplicationContext(), "Wrong Credentials",
	      Toast.LENGTH_SHORT).show();
	      attempts.setBackgroundColor(Color.RED);	
	      counter--;
	      attempts.setText(Integer.toString(counter));
	      if(counter==0){
	         login.setEnabled(false);
	      }

	   }

	}
	   
	   @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish() ;
	}
	   

	}