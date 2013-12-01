package com.example.warpic;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class MotionPathAdapter extends ArrayAdapter<MotionPath>{
	List<MotionPath> itemList;
	Context context;
	public MotionPathAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}
	public MotionPathAdapter(List<MotionPath> itemList, Context ctx) {
		super(ctx, android.R.layout.simple_list_item_1, itemList);
		this.itemList = itemList;
		this.context = ctx;		
	}

	public int getCount() {
		if (itemList != null)
			return itemList.size();
		return 0;
	}

	public MotionPath getItem(int position) {
		if (itemList != null)
			return itemList.get(position);
		return null;
	}

	public long getItemId(int position) {
		if (itemList != null)
			return itemList.get(position).hashCode();
		return 0;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		View v = convertView;
//		Exercise ex= itemList.get(position);
//		TextView tv = new TextView(context);
//		tv.setText(ex.name);
//		return v;
//	}

	public List<MotionPath> getItemList() {
		return itemList;
	}

	public void setItemList(List<MotionPath> itemList) {
		this.itemList = itemList;
	}
}
