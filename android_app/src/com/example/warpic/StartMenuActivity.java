package com.example.warpic;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
public class StartMenuActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.start_menu_layout);	
	    findViewById(R.id.warpFromCamera).setOnClickListener(mGlobal_OnClickListener);
	    findViewById(R.id.loadImage).setOnClickListener(mGlobal_OnClickListener);
	    }
	    //Global On click listener for all views
	    final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
	        public void onClick(final View v) {
	            switch(v.getId()) {
	                case R.id.warpFromCamera:
	                    //Inform the user the button1 has been clicked
	                	//Toast.makeText(this, text, duration)
	                    System.out.println("Warp camera Button Presed");  
	                    launchCameraActivity();
	                break;
	                case R.id.loadImage:
	                    //Inform the user the button2 has been clicked
	                	loadImageOption();
	   	             // When the user center presses, let them pick a contact.
	   
	                break;
	            }
	        }
	    };
	    public void launchCameraActivity(){
	    	Intent intent = new Intent(this, CameraActivity.class);
        	startActivity(intent);
	    }
	    public void loadImageOption(){
	    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	    	Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
	    	intent.setDataAndType(uri, "image/*");
	    	startActivityForResult(Intent.createChooser(intent, "Open folder"), 1);
	    }
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data){
	    	String filePath= data.getData().toString();
	    	filePath= getRealPathFromURI(data.getData());
	    	Intent intent = new Intent(this, WarpicActivity.class);
        	intent.putExtra("filePath",filePath);
        	System.out.println("Absolution Path: "+filePath);
        	startActivity(intent);
	    }
	    public String getRealPathFromURI(Uri contentUri) {
	        String[] proj = { MediaStore.Images.Media.DATA };
	        @SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }
}


