package com.brindavancollege.videosurveillance;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewActivity extends Activity {
	
	VideoView mVideoView;
    MediaController mc;
    String videourl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.videoviewxml) ;
		
		 String videoPath = getIntent().getStringExtra("ClickedImagePath"); //get image path from post activity
	        if (videoPath != null && !videoPath.isEmpty()) {
	            File vidFile = new File(videoPath);
	            if (vidFile.exists()) {

	            	 try {
	                     mVideoView = (VideoView) findViewById(R.id.videoView);
	                     String videourl = videoPath ;
	                     mc = new MediaController(this);
	                     mVideoView.setMediaController(mc);
	                     mVideoView.requestFocus();
	                     mVideoView.setVideoURI(Uri.parse(videourl));
	                     mc.show();
	                     mVideoView.start();
	                 } catch (Exception e) {
	                 //TODO: handle exception
	                 }  
	            }
	        }
	}
}

