package com.example.warpic;

import java.util.ArrayList;

public class EditRatioSlider {
	float ratio;
	WarpicActivity wp;
	Pt location, ratio_pt;
	float length,ratio_pt_radius;
	public boolean selected;
	Pt center;
	ArrayList<Float> history;
	public EditRatioSlider(WarpicActivity w, Pt loc, float len){
		wp = w;
		location = loc;
		length = len;
		center = new Pt(location.x,location.y+length/2.0f);
		ratio_pt = new Pt(location.x,location.y+length/2.0f);
		selected = false;
		ratio_pt_radius = 40;
		history = new ArrayList<Float>();
	}
	
	public void draw(){
		wp.stroke(0,255,0);
		wp.strokeWeight(10);
		wp.noFill();
		wp.line(location.x, location.y, location.x, location.y+length);
		if(selected){
			wp.stroke(255,0,0);
			wp.fill(255,0,0);
		}
		else{
			wp.stroke(0);
			wp.fill(0);
		}
		ratio_pt.show(ratio_pt, ratio_pt_radius, wp);
	}
	
	public void move(float displacement){
		if(displacement<location.y){
			ratio_pt.y = location.y;
		}
		else if(displacement > location.y+length){
			ratio_pt.y= location.y+length;
		}
		else{
			ratio_pt.y = displacement;
		}
	}

	public float getRatio(){

		return (location.disTo(ratio_pt)/(length/2.0f));
	}
	
	public boolean grabbed(Pt cTouch) {
		float disFromCenter = (cTouch.x - ratio_pt.x)*(cTouch.x - ratio_pt.x)+
				((cTouch.y - ratio_pt.y)*(cTouch.y - ratio_pt.y));
		if(disFromCenter< ratio_pt_radius*ratio_pt_radius){
			return true;
		}
		return false;
	}
	
	
	public void updateHistory(){
		history.add(getRatio());
	}
	
	public ArrayList<Float> getHistory(){
		return history;
	}
}
