package com.example.warpic;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Gallery_Activity extends Activity{
	ListView l;
	MotionPathAdapter mp_adapter;
	ArrayList<MotionPath> motion_paths;
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.motion_gallery_layout);	
		l= (ListView) findViewById(R.id.motion_gallery_list);
		motion_paths = new ArrayList<MotionPath>();
		motion_paths.add(new MotionPath("smile"));
		mp_adapter= new MotionPathAdapter(motion_paths,getApplicationContext());
		l.setAdapter(mp_adapter);
		l.setBackgroundColor(Color.BLACK);
		l.setOnItemClickListener(new OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
        	public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
			    WarpicActivity.load_warp_points();
			    onBackPressed();	
			}
		});
	}
	
	public void select_warp(View v){

	}
	
}
