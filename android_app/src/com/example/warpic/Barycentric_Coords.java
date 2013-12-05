package com.example.warpic;

public class Barycentric_Coords {
	Pt a,b,c;
	Barycentric_Coords(Pt _a,Pt _b,Pt _c){
		a=_a;
		b=_b;
		c=_c;
	}
	
	
	void show(WarpicActivity wp){
		wp.beginShape();
		a.v(wp);b.v(wp);c.v(wp);a.v(wp);
		wp.endShape();
		
//		wp.show(a, 10);
//		wp.show(b, 10);
//		wp.show(c,10);
	}
	
	
}
