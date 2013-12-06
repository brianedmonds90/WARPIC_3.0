package com.example.warpic;

public class Weight {
	float a,b,c;
	Weight(float _a,float _b,float _c){
		a=_a;
		b=_b;
		c=_c;
	}
	
	Pt to_global_coords(Pt v1,Pt v2, Pt v3){
		
		
		
		Pt temp1 = v1.scale(a,v1);
		Pt temp2= v2.scale(b, v2);
		Pt temp3= v3.scale(c,v3);
		Pt ret = temp1.add(temp2).add(temp3);
		 return ret;
	}
	
}
