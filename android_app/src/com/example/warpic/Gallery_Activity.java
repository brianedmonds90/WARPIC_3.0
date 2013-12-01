package com.example.warpic;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class Gallery_Activity extends Activity{
	ListView l;
	MotionPathAdapter mp_adapter;
	ArrayList<MotionPath> motion_paths;
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		l= (ListView) findViewById(R.id.motion_gallery_list);
		//l.setClickable(true);
		motion_paths = new ArrayList<MotionPath>();
		motion_paths.add(new MotionPath("testing"));
		mp_adapter= new MotionPathAdapter(motion_paths,getApplicationContext());
		mp_adapter.addAll(motion_paths);
		
		//l.setAdapter(mp_adapter);
		//TODO: Fix the gui here
		
	   }
}
