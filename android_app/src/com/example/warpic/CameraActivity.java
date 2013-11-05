package com.example.warpic;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class CameraActivity extends Activity{
	static String filePath; //Communicate this path the WarpicActivity
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
		File f= getOutputMediaFile(type);
		Uri myUri = null;
		try{
			myUri=Uri.fromFile(f);
		}
		catch(Exception e){
			System.out.println("exception caught");
			e.printStackTrace();
		}
		return myUri;
	}
	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
		System.out.println("get External Storage State: "+Environment.getExternalStorageState());
	    File mediaStorageDir = new File(Environment.getExternalStorageDirectory(
	              ), "WARPIC/Pictures");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	    		
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	        
	    }
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(0));
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	    filePath=mediaStorageDir.getPath() + File.separator +
		        "IMG_"+ timeStamp + ".jpg";
	    return mediaFile;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.activity_main);
	    // create Intent to take a picture and return control to the calling application
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
	    // start the image capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	        	Intent intent = new Intent(this, WarpicActivity.class);
	        	intent.putExtra("filePath",filePath);
	        	startActivity(intent);
	        	if(data!=null){
	        		// Image captured and saved to fileUri specified in the Intent
	        		Toast.makeText(this, "Image saved to:\n" +
	                     filePath, Toast.LENGTH_LONG).show();
	        	}
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	        	System.out.println("image capture failed");
	            // Image capture failed, advise user
	        }
	    }

	    if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Video captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Video saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }
	}
}
