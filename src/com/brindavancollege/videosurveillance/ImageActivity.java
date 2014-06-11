package com.brindavancollege.videosurveillance;


import java.io.File;
import java.util.ArrayList;

import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageActivity extends Activity {

    public class ImageAdapter extends BaseAdapter {
     
     private Context mContext;
     ArrayList<String> itemList = new ArrayList<String>();
     
     public ImageAdapter(Context c) {
      mContext = c; 
     }
     
     void add(String path){
      itemList.add(path); 
     }

  @Override
  public int getCount() {
   return itemList.size();
  }

  @Override
  public Object getItem(int position) {
   // TODO Auto-generated method stub
   return itemList.get(position);
  }

  @Override
  public long getItemId(int position) {
   // TODO Auto-generated method stub
   return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
   ImageView imageView;
         if (convertView == null) {  // if it's not recycled, initialize some attributes
             
        	 imageView = new ImageView(mContext);
        	 
        	
            
        	 
        	 
             imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
             imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
             imageView.setPadding(8, 8, 8, 8);
         } else {
             imageView = (ImageView) convertView;
         }

        Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 150, 150);

        imageView.setImageBitmap(bm);
         return imageView;
  }
  
  public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
   
   Bitmap bm = null;
   // First decode with inJustDecodeBounds=true to check dimensions
   final BitmapFactory.Options options = new BitmapFactory.Options();
   options.inJustDecodeBounds = true;
   BitmapFactory.decodeFile(path, options);
       
   // Calculate inSampleSize
   options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
       
   // Decode bitmap with inSampleSize set
   options.inJustDecodeBounds = false;
   bm = BitmapFactory.decodeFile(path, options); 
       
   return bm;   
  }
  
  public int calculateInSampleSize(
    
   BitmapFactory.Options options, int reqWidth, int reqHeight) {
   // Raw height and width of image
   final int height = options.outHeight;
   final int width = options.outWidth;
   int inSampleSize = 1;
   
   if (height > reqHeight || width > reqWidth) {
    if (width > height) {
     inSampleSize = Math.round((float)height / (float)reqHeight);    
    } else {
     inSampleSize = Math.round((float)width / (float)reqWidth);    
    }   
   }
   
   return inSampleSize;    
  }
  
 }
    
    ImageAdapter myImageAdapter;

 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagesxml);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        
        String ExternalStorageDirectoryPath = Environment
          .getExternalStorageDirectory()
          .getAbsolutePath();
        
        String targetPath = ExternalStorageDirectoryPath + "/VSimagegallery/";
        
        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);
        
        File[] files = targetDirector.listFiles();
        for (File file : files){
         myImageAdapter.add(file.getAbsolutePath()); 
        }
        
        gridview.setOnItemClickListener(myOnItemClickListener);
        
    }
 
 OnItemClickListener myOnItemClickListener
 = new OnItemClickListener(){

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position,
    long id) {
   String prompt = (String)parent.getItemAtPosition(position);
  // Toast.makeText(getApplicationContext(), prompt, Toast.LENGTH_LONG).show();
   Intent i = new Intent(getApplicationContext(), BigImage.class); //start new Intent to another Activity.
   i.putExtra("ClickedImagePath", prompt ); //put image link in intent.
   startActivity(i);
   
	 
  }};

}