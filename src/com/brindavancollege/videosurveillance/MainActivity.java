package com.brindavancollege.videosurveillance;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    startVedioSurveillance() ;
	    startSettings() ;
	    logout() ;
	    help() ;
	    
	    
	   
	}

	private void help() {
		
		Button help =(Button) findViewById(R.id.button3) ;
		help.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				
				
			        Toast.makeText(MainActivity.this,"Helper",Toast.LENGTH_SHORT).show();	
					startActivity(new Intent(MainActivity.this,HelpActivity.class)) ;

			}
		           });
		
		
	}

	private void logout() {
		Button log =(Button) findViewById(R.id.button4) ;
		log.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				
				
			        Toast.makeText(MainActivity.this,"Logging out !!",Toast.LENGTH_SHORT).show();	
					startActivity(new Intent(MainActivity.this,LoginActivity.class)) ;

			}
		           });
		
		
		
	}

	private void startVedioSurveillance()  {
		Button startSurveillanceButton=(Button) findViewById(R.id.button1) ;
		startSurveillanceButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				
				
			
		 
			
				if (Utils.checkCameraHardware1(MainActivity.this)) {
					
					Toast.makeText(MainActivity.this,"Cameras supported",Toast.LENGTH_SHORT).show();	
					startActivity(new Intent(MainActivity.this,CounterActivity.class)) ;

				} else 
				
				{
					
		            Toast.makeText(MainActivity.this,"Camera not supported",Toast.LENGTH_SHORT).show();	
					Utils.getDialogBuilder(MainActivity.this,"Camera","Device doesn't supports camera",null,"OK").show();
		           
				   }
				
			           }
		           });
		           
		        }
		 
				
				
				
				

	
	private void startSettings()  {
		Button startSettingButton=(Button) findViewById(R.id.button2) ;
		startSettingButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Toast.makeText(MainActivity.this,"starting settings....",Toast.LENGTH_SHORT).show();
			startActivity(new Intent(MainActivity.this,PrefSettings.class)) ;
			
			
			}	
		});
	}
		

	
	
	}
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		 
		
		
