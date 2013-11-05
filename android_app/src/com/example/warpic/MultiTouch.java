package com.example.warpic;

import java.util.ArrayList;

import processing.core.PApplet;


//@author: Brian Edmonds
//@version: 1.0
class MultiTouch{//Class holds 3 different points to keep track of the users movement on the screen.

	//One MultiTouch object corresponds to a different finger object
	Pt currentTouch, lastTouch, disk; //currentTouch= where the finger is now, lastTouch= where the finger is at the previous frame 
	//disk= the actual point being drawn
	boolean selected;
	int meIndex;
	Pt movement; 
	double downTime;
	double liftTime;
	ArrayList <Pt>history;

	MultiTouch(){
		currentTouch=new Pt();
		lastTouch= new Pt();
		disk=new Pt();
		selected=false;
		meIndex=-1;
		history=new ArrayList<Pt>();
		downTime=(long) 0.0;
		liftTime= (long) 0.0;
	}

	MultiTouch(float x,float y){
		currentTouch=new Pt();
		lastTouch= new Pt();
		disk=new Pt(x,y);
		selected=false;
		meIndex=-1;
		history=new ArrayList<Pt>();
	}
	
	void show(PApplet p){
		if(this.selected){
			p.fill(0,255,0);
			p.ellipse(this.disk.x,this.disk.y,15,15);
		}
		else{
			p.fill(255,0,0);
			p.ellipse(this.disk.x,this.disk.y,15,15); 
		}
	}

	public String toString(){
		String ret="";
		ret+= "disk: "+disk;
		ret+= " currentTouch: "+currentTouch+" lastTouch: "+lastTouch+" meIndex: "+meIndex+ "Selected: "+selected;
		return ret; 
	}
	Pt getHistoryAt(int i){ 
		return history.get(i); 
	}
	
	ArrayList<Pt> getHistory(){
		return history; 
	}

	void drawHistory(PApplet pa){
		for(Pt p: history){
			p.draw(pa); 
		}
	}
	
	void clearHistory(){
		this.history.clear(); 
	}
	
}