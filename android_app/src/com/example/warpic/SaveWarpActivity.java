package com.example.warpic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class SaveWarpActivity extends Activity{
	ParseObject warps;
	EditText ed;
	
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.save_warp_activity_layout);	
		warps = new ParseObject("warps");
		ed = new EditText(getApplicationContext());
		ed = (EditText) findViewById(R.id.editText1);
	}
	
	public void save(View v){
		//TODO: CREATE A SQLLite DB to store the warped paths
		String name = ed.getText().toString();
		warps.put("name", name);
		warps.saveInBackground(new SaveCallback() {
			Context context = getApplicationContext();
			//CharSequence text = "Save successful";
			int duration = Toast.LENGTH_SHORT;
			public void done(ParseException e) {
				if (e == null) {
	

					Toast toast = Toast.makeText(context, "Save Successful", duration);
					toast.show();

				} else {
					Toast toast = Toast.makeText(context, "Save Unsuccessful", duration);
					toast.show();
				}
			}
		});
		onBackPressed();
	}
}
