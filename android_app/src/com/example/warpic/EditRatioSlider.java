package com.example.warpic;

public class EditRatioSlider {
	float ratio;
	WarpicActivity wp;
	Pt location, ratio_pt;
	float length,ratio_pt_radius;
	public boolean selected;
	public EditRatioSlider(WarpicActivity w, Pt loc, float len){
		wp = w;
		location = loc;
		length = len;
		ratio = 1;
		ratio_pt = loc.copy(loc);
		ratio_pt.mul(1);
		selected = false;
		ratio_pt_radius = 40;
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

	public boolean grabbed(Pt cTouch) {
		float disFromCenter = (cTouch.x - ratio_pt.x)*(cTouch.x - ratio_pt.x)+
				((cTouch.y - ratio_pt.y)*(cTouch.y - ratio_pt.y));
		if(disFromCenter< ratio_pt_radius*ratio_pt_radius){
			return true;
		}
		return false;
	}
}