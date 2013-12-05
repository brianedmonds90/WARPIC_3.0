package com.example.warpic;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;

class MultiTouchController {// Used to process the android API touch events for
							// easy use by applications
	// @author: Brian Edmonds
	// @version: 1.0
	// This class holds an arrayList of MultiTouch objects and has methods for
	// building a MultiTouch User interface. The user's actions are broken down
	// into
	// touch(),lift(), and movement()
	// This class is implemented in surfaceTouchEvent(MotionEvent me)

	ArrayList<MultiTouch> mTContainer;// Container for MultiTouch objects
	Menu menu;
	MotionPath canned_motion_path;
	double initTime, liftTime, elapsedTime;
	boolean recordTime;
	MultiTouchController(int num) {
		mTContainer = new ArrayList<MultiTouch>(num);
		for (int i = 0; i < num; i++) {
			mTContainer.add(new MultiTouch());
		}
		initTime = 0;
		liftTime = 0;
	}

	MultiTouchController() {
		mTContainer = new ArrayList<MultiTouch>();
		initTime = 0;
		liftTime = 0;
		elapsedTime = 0;// Keeps track of the total time that any fingers are
						// touching the screen
		recordTime = true;
	}

	MultiTouchController(Menu myMenu) {
		this();
		menu = myMenu;
	}
	
	MultiTouchController(Menu myMenu,MotionPath mPath) {
		this();
		menu = myMenu;
		canned_motion_path= mPath;
	}

	public void init() {// Puts disk objects on the screen to be moved around
		mTContainer.add(new MultiTouch(300, 100));
		mTContainer.add(new MultiTouch(300, 400));
		mTContainer.add(new MultiTouch(600, 100));
		mTContainer.add(new MultiTouch(600, 400));
	}

	public void touch(MyMotionEvent ev) {// Method used when a touch event
										// happens
		Pt cTouch = new Pt(ev.loc.x, ev.loc.y);
		
		MultiTouch finger;
		if (recordTime) {
			initTime = ev.nanoTime / 1000000000.0;
			recordTime = false;
		}
		if (mTContainer.size() < 4) {// Adjust this number to adjust the number
										// of fingers that you want to use
			finger = new MultiTouch(cTouch.x, cTouch.y);
			finger.selected = true;
			finger.meIndex = ev.pointerId;
			finger.lastTouch = cTouch;
			finger.downTime = ev.nanoTime / 1000000000.0;
			mTContainer.add(finger);
			if (mTContainer.size() == 4) {// && !WarpicActivity.fingersOnScreen
				WarpicActivity.fingersOnScreen = true;
			}
			if (mTContainer.size() == 4 && !WarpicActivity.firstPass)
				WarpicActivity.realTimeWarp = true;
		} else {// We have four points on the screen, the user wants to move one
				// of them
			MultiTouch temp = findClosest(cTouch);
			WarpicActivity.fingersOnScreen = true;
			if (temp != null) {
				temp.selected = true;
				temp.meIndex = ev.pointerId;
				temp.downTime = ev.nanoTime / 1000000000.0;
				temp.lastTouch = cTouch; // Keep track of the touch location for
											// movement
			}
		}

	}

	public void lift(MyMotionEvent me) {// Used when a finger is lifted
		MultiTouch temp = null;
		for (int i = 0; i < mTContainer.size(); i++) {// iterate through the
														// multiTouch Container
														// object
			temp = mTContainer.get(i);
			if (temp.meIndex == me.pointerId) {
				temp.liftTime = me.nanoTime / 1000000000.0;
				temp.selected = false;
				temp.meIndex = -1;
			}
		}
		if (me.pointerCount == 1) {
			liftTime = me.nanoTime / 1000000000.0;// Log the liftTime of the
													// last finger
			recordTime = true;
			elapsedTime += (liftTime - initTime); // Add the ellapsed time to
													// our counter
			liftTime = 0;
			initTime = 0;
			if (WarpicActivity.firstPass) {
				WarpicActivity.firstPass = false;
				WarpicActivity.saveWarp = true;
				WarpicActivity.grabPoints = true;
			}
			else if(!WarpicActivity.firstPass){
				WarpicActivity.setL_R_prime=true;
			}
			WarpicActivity.fingersOnScreen = false;
		}
	}

	public MultiTouch findClosest(Pt aa) {// Returns the index of the closest
											// disk of the container to the
		float minDistance = Float.MAX_VALUE;
		MultiTouch closest = null;
		for (MultiTouch mt : mTContainer) {
			float d = mt.disk.disTo(aa);
			if (d < minDistance && !mt.selected) {
				minDistance = d;
				closest = mt;
			}
		}
		return closest;
	}

	public void motion(MyMotionEvent me) {// Used when a finger moves on the
											// screen
		// if(mTContainer.size()==4&!WarpicActivity.fingersOnScreen){
		// WarpicActivity.fingersOnScreen=true;
		// // startTimer=true;
		// }
		MultiTouch temp = null;
		// find the matching index within the mTContainer object
		int j = me.pointerId; // which finger are we looking at
		int index = indexOf(j); // what is the index of the pointer data within
								// the mTContainer list
		if (index != -1 && mTContainer.get(index).selected) {
			temp = mTContainer.get(index);
			// log the current position of the users fingers
			temp.currentTouch = new Pt(me.loc.x, me.loc.y, 0);
			// calculate the distance moved from the previous frame and move the
			// point
			temp.disk.move(temp.currentTouch.subtract(temp.lastTouch));
			temp.lastTouch.set(temp.currentTouch);
		}
	}

	void show(PApplet p) {// shows the disks
		int num = 0;
		for (int i = 0; i < mTContainer.size(); i++) {
			mTContainer.get(i).show(p);
		}
	}

	public String toString() {
		String ret = "";
		for (int i = 0; i < mTContainer.size(); i++) {
			ret += "Multitouch: " + mTContainer.get(i);
			ret += "\n";
		}
		return ret;
	}

	// Gets the index of the pointer data from the mTContainer list
	int indexOf(int pointerId) {
		for (int i = 0; i < mTContainer.size(); i++) {
			if (mTContainer.get(i).meIndex == pointerId
					&& mTContainer.get(i).selected) {
				return i;
			}
		}
		return -1;
	}

	Pt firstPt() {// Returns the first point of the MultiTouchController
		return mTContainer.get(0).disk;
	}

	int size() {
		return this.mTContainer.size();
	}

	Pt getDiskAt(int index) {
		return this.mTContainer.get(index).disk;
	}

	MultiTouch getAt(int index) {
		return mTContainer.get(index);
	}

	MultiTouch getMultiTouchAt(int i) {// Returns the individual multiTouch
										// object at a given index
		return mTContainer.get(i);
	}

	ArrayList<Pt> getHistoryOf(int i) {
		return getMultiTouchAt(i).getHistory();
	}

	void showHistory(PApplet pa) {
		for (MultiTouch m : mTContainer) {
			m.drawHistory(pa);
		}
	}

	void updateHistory() {
		for (MultiTouch m : mTContainer) {
			m.history.add(new Pt(m.disk.x, m.disk.y));
		}
	}

	void showEdges(PApplet p) {// Shows the lines inbetween the fingers
		Pt a, b, c, d;
		p.strokeWeight(10);
		p.stroke(200, 50, 200);
		a = getDiskAt(0);
		b = getDiskAt(1);
		c = getDiskAt(2);
		d = getDiskAt(3);
		p.line(a.x, a.y, b.x, b.y);
		p.line(c.x, c.y, d.x, d.y);
		p.noStroke();
	}

	void clearHistory() {// clears the history of user's movement
		for (int i = 0; i < size(); i++) {
			mTContainer.get(i).clearHistory();
		}
	}

	void handle(MyMotionEvent me) {
		if (me.action == 1) {// The user has touched the screen

			touch(me); // Register the touch event
		} else if (me.action == 0) {// The user has lifted their fingers from
									// the screen
			// Register the lift event
			lift(me);
		} else {
			motion(me);// Register the motion event
		}
	}

	public void handleButton(MyMotionEvent me) {
		if (me.action == 1 || me.action == 2) {// The user has touched the
												// screen
			Pt p = me.loc;
			menu.buttonPressed(p); // Register the touch event
		} else if (me.action == 0) {// The user has lifted their fingers from
									// the screen
			// Register the lift event
			menu.buttonLifted();
		}
	}

	void showMultiTouchTimes(PApplet pa) {
		pa.textSize(40);
		pa.fill(0, 255, 0);
		for (MultiTouch m : mTContainer) {
			pa.text("DownTime: " + m.downTime, m.history.get(0).x,
					m.history.get(0).y);
			pa.text("LiftTime: " + m.liftTime,
					m.history.get(m.history.size() - 1).x,
					m.history.get(m.history.size() - 1).y);
			pa.fill(0);

		}

		MultiTouch m = mTContainer.get(0);
		if (m.downTime != 0 && m.liftTime != 0)
			pa.text("totalTime: " + (m.liftTime - m.downTime), 100, 100);
	}

	double getElapsedTime() {
		return elapsedTime;
	}

	public void handleCannedWarpEdit(MyMotionEvent me) {
		
		if (me.action == 1) {// The user has touched the screen
			touchCanned(me); // Register the touch event
		} else if (me.action == 0) {// The user has lifted their fingers from
									// the screen
			// Register the lift event
			liftCanned(me);
		} else {
			motionCanned(me);// Register the motion event
		}
	}

	private void liftCanned(MyMotionEvent me) {
		canned_motion_path.currentTouch= new Pt();
		canned_motion_path.prevTouch= new Pt();
		
	}

	private void motionCanned(MyMotionEvent me) {
		if(me.pointerCount==1){
			canned_motion_path.currentTouch = me.loc;
			canned_motion_path.displace();
			canned_motion_path.prevTouch=canned_motion_path.currentTouch;
		}
		else if(me.pointerCount>1){
			motion(me);
		}
	
	}

	private void touchCanned(MyMotionEvent me) {
		
		if(me.pointerCount>1){
			canned_motion_path.prevTouch_1 = me.loc;
		}
		else if(me.pointerCount==1){
//			canned_motion_path.prevTouch = me.loc;
			touch(me);
		}
	}

	public void scale(MotionPath smile) {
		//TODO: write this code
		
	}

}
