package com.example.warpic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Gallery_Activity extends Activity{
	ListView l;
	MotionPathAdapter mp_adapter;
	ArrayList<MotionPath> motion_paths;
	ParseQuery<ParseObject> query; 
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.motion_gallery_layout);	
		l= (ListView) findViewById(R.id.motion_gallery_list);
	
		motion_paths = new ArrayList<MotionPath>();
//		motion_paths.add(new MotionPath("smile"));
		mp_adapter= new MotionPathAdapter(motion_paths,getApplicationContext());
		
		getWarps();
		
		l.setAdapter(mp_adapter);
		l.setBackgroundColor(Color.BLACK);
		l.setOnItemClickListener(new OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
        	public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
        		MotionPath mp=(MotionPath) l.getItemAtPosition(position);
			    WarpicActivity.load_warp_points(mp);
			    onBackPressed();	
			}
		});
	}
	
	private void getWarps() {
		query = ParseQuery.getQuery("warps");
		query.findInBackground(new FindCallback<ParseObject>() {
		     public void done(List<ParseObject> objects, com.parse.ParseException e) {
		    	 Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT; 
		         if (e == null) {
		             objectsWereRetrievedSuccessfully(objects);
		             Toast toast = Toast.makeText(context, "Load Successful", duration);
					 toast.show();
		         } else {
		        	 Toast toast = Toast.makeText(context, "Load Unsuccessful", duration);
					 toast.show();
					 e.printStackTrace();
		         }
		     }

			private void objectsWereRetrievedSuccessfully(List<ParseObject> objects) {
				ArrayList<MotionPath> m_paths = new ArrayList<MotionPath>();  
				MotionPath mp;
				for(ParseObject warp: objects){
				    mp= new MotionPath(warp.getString("name"));
				    String paths= warp.getString("motion");
				    mp.unparsed_string=paths;
				    m_paths.add(mp);
				}
				mp_adapter.addAll(m_paths);
				mp_adapter.notifyDataSetChanged();
			}

		 });
		
	}	
}
