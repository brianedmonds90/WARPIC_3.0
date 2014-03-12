package com.example.warpic;

import java.util.ArrayList;

public class MotionPath {
	String name;
	ArrayList<Pt> A,B,C,D;
	public Pt prevTouch;
	public Pt currentTouch;
	public Pt displacement;
	public Pt prevTouch_1;
	public String unparsed_string;
	public Pt upperLeftCorner, bottomLeftCorner, bottomRightCorner,upperRightCorner;
	double ellapsed_time;
	public ArrayList<Weight> weights_a, weights_b, weights_c, weights_d, weights_bounding_box;
	public MotionPath(String n){
		name=n;
		A= new ArrayList<Pt>();
		B= new ArrayList<Pt>();
		C= new ArrayList<Pt>();
		D= new ArrayList<Pt>();
		prevTouch=new Pt(0,0);
		currentTouch= new Pt(0,0);
		displacement= new Pt(0,0);
		upperLeftCorner= new Pt();
		upperRightCorner = new Pt();
		bottomLeftCorner = new Pt();
		bottomRightCorner = new Pt();
		

		// *****************Weight lists for Barycentric coords
		weights_a = new ArrayList<Weight>();
		weights_b = new ArrayList<Weight>();
		weights_c = new ArrayList<Weight>();
		weights_d = new ArrayList<Weight>();
		weights_bounding_box = new ArrayList<Weight>();
		// *****************

		
	}
	
	public String toSting(){
		return name;
	}
	
	public void showPaths(WarpicActivity wp){
		draw(A,wp);
		draw(B,wp);
		draw(C,wp);
		draw(D,wp);
	}
	
	void draw(ArrayList<Pt> list, WarpicActivity wp) {
		    wp.beginShape(); 
		    for (int v=0; v<list.size(); v++){
		      list.get(v).v(wp);
		    }
		    wp.endShape();
	}
	
	public float computeLeftMostBound(){
		float leftMost=9999999f;
		for(Pt p: A){
			if(p.x<leftMost)
				leftMost=p.x;
		}
		for(Pt p: B){
			if(p.x<leftMost)
				leftMost=p.x;
		}
		for(Pt p: C){
			if(p.x<leftMost)
				leftMost=p.x;
		}
		for(Pt p: D){
			if(p.x<leftMost)
				leftMost=p.x;
		}
		return leftMost;
	}
	
	public float computeRightMostBound(){
		float rightMost=-9999999f;
		for(Pt p: A){
			if(p.x>rightMost)
				rightMost=p.x;
		}
		for(Pt p: B){
			if(p.x>rightMost)
				rightMost=p.x;
		}
		for(Pt p: C){
			if(p.x>rightMost)
				rightMost=p.x;
		}
		for(Pt p: D){
			if(p.x>rightMost)
				rightMost=p.x;
		}
		return rightMost;
	}
	
	/********************************************* Smoothing ***************************/
	void smooth(){
		smooth(A);
		smooth(B);
		smooth(C);
		smooth(D);
	}
	void smooth(ArrayList<Pt> l) {
		for (int k = 0; k < 4; k++) {
			tuck((float) .5, l);
			tuck((float) -.5, l);
		}
	}

	void tuck(float s, ArrayList<Pt> list) {
		Pt[] S = new Pt[list.size()]; // temporry array
		// copy of each intermediate point moved by s towards the average of its
		// neighbors
		for (int v = 1; v < list.size() - 1; v++)
			S[v] = MathUtility.P(list.get(v), s, MathUtility.average(list.get(v - 1), list.get(v + 1))); // S
																					// =
																					// G
																					// +
																					// s((P+N)/2-G)
		for (int v = 1; v < list.size() - 1; v++)
			list.set(v, S[v]); // copy back
	}
	
	
	public float computeUpperMostBound(){
		float upperMost=-9999999f;
		for(Pt p: A){
			if(p.y>upperMost)
				upperMost=p.y;
		}
		for(Pt p: B){
			if(p.y>upperMost)
				upperMost=p.y;
		}
		for(Pt p: C){
			if(p.y>upperMost)
				upperMost=p.y;
		}
		for(Pt p: D){
			if(p.y>upperMost)
				upperMost=p.y;
		}
		return upperMost;
	}
	
	public float computeLowerMostBound(){
		float lowerMost=9999999f;
		for(Pt p: A){
			if(p.y<lowerMost)
				lowerMost=p.y;
		}
		for(Pt p: B){
			if(p.y<lowerMost)
				lowerMost=p.y;
		}
		for(Pt p: C){
			if(p.y<lowerMost)
				lowerMost=p.y;
		}
		for(Pt p: D){
			if(p.y<lowerMost)
				lowerMost=p.y;
		}
		return lowerMost;
	}
	
	public void drawBoundingBox(WarpicActivity wp){
		wp.fill(255,0,0);
		wp.stroke(255,0,0);
		bottomLeftCorner.show(bottomLeftCorner, 10, wp);
		wp.noFill();
		wp.beginShape();
		wp.vertex(upperLeftCorner.x,upperLeftCorner.y);
		wp.vertex(upperRightCorner.x,upperRightCorner.y);
		wp.vertex(bottomRightCorner.x,bottomRightCorner.y);
		wp.vertex(bottomLeftCorner.x,bottomLeftCorner.y);
		wp.vertex(upperLeftCorner.x,upperLeftCorner.y);
		wp.endShape();
	}
	
	public void getBoundingBoxCoords(){
		upperLeftCorner=new Pt(computeLeftMostBound(),computeUpperMostBound());
		upperRightCorner= new Pt(computeRightMostBound(),computeUpperMostBound());
		bottomRightCorner = new Pt(computeRightMostBound(),computeLowerMostBound());
		bottomLeftCorner = new Pt(computeLeftMostBound(),computeLowerMostBound());
	}
	

	public void displace() {
		calcDisplacement();
		for(Pt p: A){
			p.add(displacement);
		}
		for(Pt p: B){
			p.add(displacement);
		}
		for(Pt p: C){
			p.add(displacement);
		}
		for(Pt p: D){
			p.add(displacement);
		}
		//displacement = new Pt(0,0);
		
	}

	public void calcDisplacement() {
		displacement= new Pt(currentTouch.x-prevTouch.x,currentTouch.y-prevTouch.y);		
	}
	
	public String toString(){
		return name;
	}

	public void setTo(MotionPath mp) {
		for(Pt p: mp.A){
			A.add(p);
		}
		for(Pt p: mp.B){
			B.add(p);
		}
		for(Pt p: mp.C){
			C.add(p);
		}
		for(Pt p: mp.D){
			D.add(p);
		}
	}

	public void setTo(MultiTouchController mController) {
		A = mController.getHistoryOf(0);
		B = mController.getHistoryOf(1);
		C = mController.getHistoryOf(2);
		D = mController.getHistoryOf(3);
		ellapsed_time = mController.getElapsedTime();
		
	}
	
	public void clearBarys() {
		weights_a.clear();
		weights_b.clear();
		weights_c.clear();
		weights_d.clear();
		weights_bounding_box.clear();
	}
	void computeBarys(MultiTouchController mController) {
		clearBarys();
		getBaryCentricCoords(mController);
		WarpicActivity.compute_bary=false;
		
	}
	
	private void displaceBoundingBox(Pt v1, Pt v2, Pt v3){
		Weight w= weights_bounding_box.get(0);
		upperLeftCorner=w.to_global_coords(v1, v2, v3);
		w=weights_bounding_box.get(1);
		upperRightCorner=w.to_global_coords(v1, v2, v3);
		w=weights_bounding_box.get(2);
		bottomRightCorner=w.to_global_coords(v1, v2, v3);
		w=weights_bounding_box.get(3);
		bottomLeftCorner=w.to_global_coords(v1, v2, v3);
	}
	
	private void editWarp(ArrayList<Pt> a2, ArrayList<Pt> b2, ArrayList<Pt> c2,
			ArrayList<Pt> d2, Pt v1, Pt v2, Pt v3, Pair L, Pair R) {
		// Clear the current loactions of the warp motion paths
		a2.clear();
		b2.clear();
		c2.clear();
		d2.clear();
		
		//TODO: Add barycentric translation of bounding box here
		
		// Recalculate their location based on the barycentric coordinates
		for (Weight w : weights_a) {
			a2.add(w.to_global_coords(v1, v2, v3));
		}
		for (Weight w : weights_b) {
			b2.add(w.to_global_coords(v1, v2, v3));
		}
		for (Weight w : weights_c) {
			c2.add(w.to_global_coords(v1, v2, v3));
		}
		for (Weight w : weights_d) {
			d2.add(w.to_global_coords(v1, v2, v3));
		}
		// Set the pairs correctly based on the new coordinates
		L.A0.setTo(a2.get(0));
		L.B0.setTo(b2.get(0));
		R.A0.setTo(c2.get(0));
		R.B0.setTo(d2.get(0));
	}
	

	void editWarp(Pair L, Pair R, MultiTouchController mController) {
		Pt v1 = mController.getDiskAt(0);
		Pt v2 = mController.getDiskAt(1);
		Pt v3 = mController.getDiskAt(2);
		displaceBoundingBox(v1, v2, v3);
		editWarp(A,B, C,D, v1, v2, v3,L,R);
		//drawBoundingBox(this);
		//mController.showTriangle(this);
	}
	
	public void getBaryCentricCoords(MultiTouchController mController) {
		// compute triangle
	
		// Compute The original Barycentric coords for the loaded effects_path
		for (Pt p : A) {
			System.out.println("point p: "+p);
			weights_a.add(p.barycentric(mController.getDiskAt(0),
					mController.getDiskAt(1), mController.getDiskAt(2)));
		}
		
		for (Pt p : B) {
			weights_b.add(p.barycentric(mController.getDiskAt(0),
					mController.getDiskAt(1), mController.getDiskAt(2)));
		}

		for (Pt p : C) {
			weights_c.add(p.barycentric(mController.getDiskAt(0),
					mController.getDiskAt(1), mController.getDiskAt(2)));
		}

		for (Pt p : D) {
			weights_d.add(p.barycentric(mController.getDiskAt(0),
					mController.getDiskAt(1), mController.getDiskAt(2)));
		}
		getBaryCentricCoordsOfBox(mController);
	}
	
	private void getBaryCentricCoordsOfBox(MultiTouchController mController) {
		weights_bounding_box.add(upperLeftCorner.barycentric(mController.getDiskAt(0),
				mController.getDiskAt(1), mController.getDiskAt(2)));
		weights_bounding_box.add(upperRightCorner.barycentric(mController.getDiskAt(0),
			mController.getDiskAt(1), mController.getDiskAt(2)));
		weights_bounding_box.add(bottomRightCorner.barycentric(mController.getDiskAt(0),
			mController.getDiskAt(1), mController.getDiskAt(2)));
		weights_bounding_box.add(bottomLeftCorner.barycentric(mController.getDiskAt(0),
			mController.getDiskAt(1), mController.getDiskAt(2)));	
	}

	
}
