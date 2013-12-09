package com.example.warpic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class SaveWarpActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.save_warp_activity_layout);	
	}
	
	public void save(View v){
		//TODO: CREATE A SQLLite DB to store the warped paths
		onBackPressed();
	}
}
