package com.brindavancollege.videosurveillance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.brindavancollege.motiondetection.AggregateLumaMotionDetection;
import com.brindavancollege.motiondetection.IMotionDetection;
import com.brindavancollege.motiondetection.ImageProcessing;
import com.brindavancollege.motiondetection.LumaMotionDetection;
import com.brindavancollege.motiondetection.RgbMotionDetection;


/**
 * This class extends Activity to handle a picture preview, process the frame
 * for motion, and then save the file to the SD card.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class MotionDetectionActivity extends SensorsActivity {

	private static final String TAG = "MotionDetectionActivity";

	private static SurfaceView preview = null;
	private static SurfaceHolder previewHolder = null;
	private static Camera camera = null;
	private static boolean inPreview = false;
	private static long mReferenceTime = 0;
	private static IMotionDetection detector = null;
	private static Context context=null;
	private static  MediaRecorder mMediaRecorder ;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static boolean isRecording = false;
	DetectionThread thread;
	private static String FOLDER_NAME = "VSvideogallery";
	private static String IMAGE_FOLDER_NAME = "VSimagegallery";

	private static volatile AtomicBoolean processing = new AtomicBoolean(false);

	/**
	 * {@inheritDoc}
	 */


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		camera = Camera.open();
		preview = (SurfaceView) findViewById(R.id.preview);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		if (Utils.USE_RGB) {
			detector = new RgbMotionDetection();
		} else if (Utils.USE_LUMA) {
			detector = new LumaMotionDetection();
		} else {
			// Using State based (aggregate map)
			detector = new AggregateLumaMotionDetection();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();
		Utils.releaseWakeLock(this);
		
		
		if(Utils.ISSMSSENT == true){
			releaseMediaRecorder();
			Utils.ISSMSSENT = false;
		}
		camera.setPreviewCallback(null);
		if (inPreview) camera.stopPreview();
		inPreview = false;
		camera.release();
		camera = null;

		//finish();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
		Utils.aquireWakeLock(this);
		Utils.ISSMSSENT = false;
		
		

	}

	private PreviewCallback previewCallback = new PreviewCallback() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onPreviewFrame(byte[] data, Camera cam) {
			if (data == null) return;
			Camera.Size size = cam.getParameters().getPreviewSize();
			if (size == null) return;

			if (!Utils.isPhoneInMotion()) {

				context=getApplicationContext();
				thread = new DetectionThread(data, size.width, size.height,context);
				thread.start();
			}
		}
	};

	private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			camera.setDisplayOrientation(90);
			try {
				camera.setPreviewDisplay(previewHolder); 
				camera.setPreviewCallback(previewCallback);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void surfaceChanged(SurfaceHolder holder,int format, int width, int height) {
			Camera.Parameters parameters = camera.getParameters();
			Camera.Size size = getBestPreviewSize(width, height, parameters);
			if (size != null) {
				parameters.setPreviewSize(size.width, size.height);
				Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
			}
			camera.setParameters(parameters);
			camera.startPreview();
			inPreview = true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// Ignore
		}
	};

	private static Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) result = size;
				}
			}
		}

		return result;
	}

	private static Handler handler=new Handler();

	private static final class DetectionThread extends Thread {

		private byte[] data;
		private int width;
		private int height;
		private Context context=null;
		private  static int count=0;

		DetectionThread(byte[] data, int width, int height,Context context) {
			this.data = data;
			this.width = width;
			this.height = height;
			this.context=context;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			
			if (!processing.compareAndSet(false, true)) return;
			if(Utils.ISSMSSENT == true)return;
			// Log.d(TAG, "BEGIN PROCESSING...");
			try {
				// Previous frame
				int[] pre = null;
				if (Utils.SAVE_PREVIOUS) pre = detector.getPrevious();

				// Current frame (with changes)
				// long bConversion = System.currentTimeMillis();
				int[] img = null;
				if (Utils.USE_RGB) {
					img = ImageProcessing.decodeYUV420SPtoRGB(data, width, height);
				} else {
					img = ImageProcessing.decodeYUV420SPtoLuma(data, width, height);
				}
				// long aConversion = System.currentTimeMillis();
				// Log.d(TAG, "Converstion="+(aConversion-bConversion));

				// Current frame (without changes)
				int[] org = null;
				if (Utils.SAVE_ORIGINAL && img != null) org = img.clone();

				if (img != null && detector.detect(img, width, height)) {
					Log.i(TAG, "MotionDetected");

					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							Toast.makeText(context,"CHOR !!!", Toast.LENGTH_SHORT).show() ;
							startRecording();
						 if(Utils.ISSMSSENT == false ){
							//    Intent callIntent = new Intent(Intent.ACTION_CALL);
							//	callIntent.setData(Uri.parse("Tel:"+"9035674873"));
							//	callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
							//	context.startActivity(callIntent) ;
							//	Utils.makeCall(context);
							    Utils.sendSMS(context) ;
								Utils.ISSMSSENT = true;  
							}   
						}
					});




					// The delay is necessary to avoid taking a picture while in
					// the
					// middle of taking another. This problem can causes some
					// phones
					// to reboot.
					long now = System.currentTimeMillis();
					if (now > (mReferenceTime + Utils.PICTURE_DELAY)) {
						mReferenceTime = now;

						Bitmap previous = null;
						if (Utils.SAVE_PREVIOUS && pre != null) {
							if (Utils.USE_RGB) previous = ImageProcessing.rgbToBitmap(pre, width, height);
							else previous = ImageProcessing.lumaToGreyscale(pre, width, height);
						}

						Bitmap original = null;
						if (Utils.SAVE_ORIGINAL && org != null) {
							if (Utils.USE_RGB) original = ImageProcessing.rgbToBitmap(org, width, height);
							else original = ImageProcessing.lumaToGreyscale(org, width, height);
						}

						Bitmap bitmap = null;
						if (Utils.SAVE_CHANGES) {
							if (Utils.USE_RGB) bitmap = ImageProcessing.rgbToBitmap(img, width, height);
							else bitmap = ImageProcessing.lumaToGreyscale(img, width, height);
						}

						// Log.i(TAG, "Saving.. previous=" + previous + " original=" + original + " bitmap=" + bitmap);
						Looper.prepare();
						new SavePhotoTask().execute(previous, original, bitmap);
					} else {
						Log.i(TAG, "Not taking picture because not enough time has passed since the creation of the Surface");
					}
				}
				else{//no intrusion mode
					Utils.ISSMSSENT=false; 
					//count=0;
				}


			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				processing.set(false);
			}
			// Log.d(TAG, "END PROCESSING...");

			processing.set(false);
		}
		
		public void startRecording()
		{
			if(prepareVideoRecorder())
				mMediaRecorder.start();

			
		}
		/*private void release() {
			// TODO Auto-generated method stub
			if (isRecording) {
                // stop recording and release camera
                mMediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                camera.lock();    

		}*/
	};

	private static final class SavePhotoTask extends AsyncTask<Bitmap, Integer, Integer> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Integer doInBackground(Bitmap... data) {
			for (int i = 0; i < data.length; i++) {
				Bitmap bitmap = data[i];
				String name = String.valueOf(System.currentTimeMillis());
				if (bitmap != null) save(name, bitmap);
			}
			return 1;
		}

		private void save(String name, Bitmap bitmap) {
			File videoFolder = new File(Environment.getExternalStorageDirectory()+"/"+IMAGE_FOLDER_NAME);
		    videoFolder.mkdirs();
			File photo = new File(Environment.getExternalStorageDirectory()+"/"+IMAGE_FOLDER_NAME, name + ".jpg");
			if (photo.exists()) photo.delete();

			try {
				FileOutputStream fos = new FileOutputStream(photo.getPath());
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
			} catch (java.io.IOException e) {
				Log.e("PictureDemo", "Exception in photoCallback", e);
			}
		}
	}

	private static boolean prepareVideoRecorder(){

	    Log.v("tag","prepareVideoRecorder called ");

	    mMediaRecorder = new MediaRecorder();

	    // Step 1: Unlock and set camera to MediaRecorder
	    camera.unlock();
	    mMediaRecorder.setCamera(camera);

	    // Step 2: Set sources
	    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

	    // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
	    mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
	  // mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	  //mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	 //  mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

	    // Step 4: Set output file
	    String name = String.valueOf(System.currentTimeMillis());
	    File videoFolder = new File(Environment.getExternalStorageDirectory()+"/"+FOLDER_NAME);
	    videoFolder.mkdirs();
		File videoFile = new File(Environment.getExternalStorageDirectory()+"/"+FOLDER_NAME, name + ".mp4");
	    mMediaRecorder.setOutputFile(videoFile.toString());
	    Log.v("motion","file video path "+videoFile);
	   // mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

	    // Step 5: Set the preview output
	    mMediaRecorder.setPreviewDisplay(preview.getHolder().getSurface());

	    // Step 6: Prepare configured MediaRecorder
	    try {
	        mMediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    } catch (IOException e) {
	        Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    }
	    Log.v("tag","prepareVideoRecorder retrurning true ");
	    return true;

       
       
       
       
       
	}
	
	private static void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
        	mMediaRecorder.stop();
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
           // camera.lock();           // lock camera for later use
        }
    }
	
	



}