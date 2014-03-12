package com.example.warpic;

public class MathUtility {
	public MathUtility(){
		
	}
	/******************************** Spiral Math *********************************/
	static Pt spirals(Pt LA0, Pt LB0, Pt LA1, Pt LB1, Pt RA0, Pt RB0, Pt RA1, Pt RB1,
			float f, Pt Q0) {
		float dL = d(Q0, average(LA0, LB0)), dR = d(Q0, average(RA0, RB0));
		float roi = d(average(LA0, LB0), average(RA0, RB0));
		float cL = WarpicActivity.sq(WarpicActivity.cos(dL / roi * WarpicActivity.PI / 2)), cR = WarpicActivity.sq(WarpicActivity.cos(dR / roi * WarpicActivity.PI / 2));
		if (dL > roi)
			cL = 0;
		if (dR > roi)
			cR = 0;
		Pt QLt = spiral(LA0, LB0, LA1, LB1, f * cL, Q0);
		Pt QRt = spiral(RA0, RB0, RA1, RB1, f * cR, Q0);
		return P(P(Q0, 1, V(Q0, QLt)), 1, V(Q0, QRt));
	}

	static Pt spiral(Pt A, Pt B, Pt C, Pt D, float t, Pt Q) {
		float a = spiralAngle(A, B, C, D);
		float s = spiralScale(A, B, C, D);
		Pt G = spiralCenter(a, s, A, C);
		return L(G, R(Q, t * a, G), WarpicActivity.pow(s, t));
	}

	static float spiralAngle(Pt A, Pt B, Pt C, Pt D) {
		return angle(V(A, B), V(C, D));
	}

	static float spiralScale(Pt A, Pt B, Pt C, Pt D) {
		return d(C, D) / d(A, B);
	}

	static Pt spiralCenter(float a, float z, Pt A, Pt C) {
		float c = WarpicActivity.cos(a), s = WarpicActivity.sin(a);
		float D = WarpicActivity.sq(c * z - 1) + WarpicActivity.sq(s * z);
		float ex = c * z * A.x - C.x - s * z * A.y;
		float ey = c * z * A.y - C.y + s * z * A.x;
		float x = (ex * (c * z - 1) + ey * s * z) / D;
		float y = (ey * (c * z - 1) - ex * s * z) / D;
		return P(x, y);
	}

	/************************************ End of Spiral Math ***************************************/
	/*********************************************************************************************/

	/************************************ Math Utilities *******************************************/
	// transform
	Pt R(Pt Q, float a) {
		float dx = Q.x, dy = Q.y, c = WarpicActivity.cos(a), s = WarpicActivity.sin(a);
		return new Pt(c * dx + s * dy, -s * dx + c * dy);
	}; // Q rotated by angle a around the origin

	static Pt R(Pt Q, float a, Pt C) {
		float dx = Q.x - C.x, dy = Q.y - C.y, c = WarpicActivity.cos(a), s = WarpicActivity.sin(a);
		return P(C.x + c * dx - s * dy, C.y + s * dx + c * dy);
	}; // Q rotated by angle a around point P

	Pt MoveByDistanceTowards(Pt P, float d, Pt Q) {
		return P(P, d, U(V(P, Q)));
	}; // P+dU(PQ) (transLAted P by *distance* s towards Q)!!!

	// average
	static Pt average(Pt A, Pt B) { // (A+B)/2 (average)
		return MathUtility.P((A.x + B.x) / 2.0, (A.y + B.y) / 2.0);
	}

	Pt P(Pt A, Pt B, Pt C) {
		return P((A.x + B.x + C.x) / 3.0, (A.y + B.y + C.y) / 3.0);
	} // (A+B+C)/3 (average)

	Pt P(Pt A, Pt B, Pt C, Pt D) {
		return average(average(A, B), average(C, D));
	}; // (A+B+C+D)/4 (average)

	Pt P() {
		return P(0, 0);
	}; // make point (0,0)

	Pt P(Pt P) {
		return P(P.x, P.y);
	}; // make copy of point A

	Pt P(float s, Pt A) {
		return new Pt(s * A.x, s * A.y);
	}; // sA

	static Pt P(Pt P, Vec V) {
		return P(P.x + V.x, P.y + V.y);
	} // P+V (P transalted by vector V)

	static Pt P(Pt P, float s, Vec V) {
		return P(P, W(s, V));
	} // P+sV (P transalted by sV)

	static Pt P(float x, float y) {
		return new Pt(x, y);
	};

	static Pt P(double x, double y) {
		return new Pt((float) x, (float) y);
	};

	static Pt P(Pt A, float s, Pt B) {
		return P(A.x + s * (B.x - A.x), A.y + s * (B.y - A.y));
	};// ,A.z+s*(B.z-A.z)); }; // A+sAB

	// measure
	boolean isSame(Pt A, Pt B) {
		return (A.x == B.x) && (A.y == B.y);
	} // A==B

	boolean isSame(Pt A, Pt B, float e) {
		return ((WarpicActivity.abs(A.x - B.x) < e) && (WarpicActivity.abs(A.y - B.y) < e));
	} // ||A-B||<e

	static float d(Pt P, Pt Q) {
		return WarpicActivity.sqrt(d2(P, Q));
	}; // ||AB|| (Distance)

	static float d2(Pt P, Pt Q) {
		return WarpicActivity.sq(Q.x - P.x) + WarpicActivity.sq(Q.y - P.y);
	}; // AB*AB (Distance squared)

	Vec V(Vec V) {
		return new Vec(V.x, V.y);
	}; // make copy of vector V

	Vec V(Pt P) {
		return new Vec(P.x, P.y);
	}; // make Vector from origin to P

	static Vec V(float x, float y) {
		return new Vec(x, y);
	}; // make Vector (x,y)

	static Vec V(Pt P, Pt Q) {
		return new Vec(Q.x - P.x, Q.y - P.y);
	}; // PQ (make Vector Q-P from P to Q

	Vec U(Vec V) {
		float n = n(V);
		if (n == 0)
			return new Vec(0, 0);
		else
			return new Vec(V.x / n, V.y / n);
	}; // V/||V|| (Unit vector : normalized version of V)

	Vec U(Pt P, Pt Q) {
		return U(V(P, Q));
	}; // PQ/||PQ| (Unit vector : from P towards Q)

//	Vec MouseDrag() {
//		return new Vec(mouseX - pmouseX, mouseY - pmouseY);
//	}; // vector representing recent mouse displacement

	// Interpolation
	Vec L(Vec U, Vec V, float s) {
		return new Vec(U.x + s * (V.x - U.x), U.y + s * (V.y - U.y));
	}; // (1-s)U+sV (Linear interpolation between vectors)

	Vec S(Vec U, Vec V, float s) {
		float a = angle(U, V);
		Vec W = R(U, s * a);
		float u = n(U);
		float v = n(V);
		W(WarpicActivity.pow(v / u, s), W);
		return W;
	} // steady interpolation from U to V

	static Pt L(Pt A, Pt B, float t) {
		return P(A.x + t * (B.x - A.x), A.y + t * (B.y - A.y));
	}

	// measure

	static float det(Vec U, Vec V) {
		return dot(R(U), V);
	} // det | U V | = scalar cross UxV

	float n(Vec V) {
		return WarpicActivity.sqrt(dot(V, V));
	}; // n(V): ||V|| (norm: length of V)

	float n2(Vec V) {
		return WarpicActivity.sq(V.x) + WarpicActivity.sq(V.y);
	}; // n2(V): V*V (norm squared)

	boolean parallel(Vec U, Vec V) {
		return dot(U, R(V)) == 0;
	};

	static float angle(Vec U, Vec V) {
		return WarpicActivity.atan2(det(U, V), dot(U, V));
	}; // angle <U,V> (between -PI and PI)

	float angle(Vec V) {
		return (WarpicActivity.atan2(V.y, V.x));
	}; // angle between <1,0> and V (between -PI and PI)

	float angle(Pt A, Pt B, Pt C) {
		return angle(V(B, A), V(B, C));
	} // angle <BA,BC>

	float turnAngle(Pt A, Pt B, Pt C) {
		return angle(V(A, B), V(B, C));
	} // angle <AB,BC> (positive when right turn as seen on screen)

	float toRad(float a) {
		return (a * WarpicActivity.PI / 180);
	} // convert degrees to radians

	float positive(float a) {
		if (a < 0)
			return a + WarpicActivity.TWO_PI;
		else
			return a;
	} // adds 2PI to make angle positive

	static Vec W(float s, Vec V) {
		return V(s * V.x, s * V.y);
	} // sV

	Vec W(Vec U, Vec V) {
		return V(U.x + V.x, U.y + V.y);
	} // U+V

	Vec W(Vec U, float s, Vec V) {
		return W(U, S(s, V));
	} // U+sV

	Vec W(float u, Vec U, float v, Vec V) {
		return W(S(u, U), S(v, V));
	} // uU+vV ( Linear combination)

	static Pt P(float a, Pt A, float b, Pt B) {
		return P(a * A.x + b * B.x, a * A.y + b * B.y);
	} // aA+bB, (a+b=1)

	Pt P(float a, Pt A, float b, Pt B, float c, Pt C) {
		return P(a * A.x + b * B.x + c * C.x, a * A.y + b * B.y + c * C.y);
	} // aA+bB+cC

	Pt P(float a, Pt A, float b, Pt B, float c, Pt C, float d, Pt D) {
		return P(a * A.x + b * B.x + c * C.x + d * D.x, a * A.y + b * B.y + c
				* C.y + d * D.y);
	} // aA+bB+cC+dD

	static float dot(Vec U, Vec V) {
		return (U.x * V.x + U.y * V.y + U.z * V.z);
	};

	static Vec R(Vec V) {
		return new Vec(-V.y, V.x);
	}; // V turned right 90 degrees (as seen on screen)

	Vec R(Vec V, float a) {
		float c = WarpicActivity.cos(a), s = WarpicActivity.sin(a);
		return (new Vec(V.x * c - V.y * s, V.x * s + V.y * c));
	}; // V rotated by a radians

	Vec S(float s, Vec V) {
		return new Vec(s * V.x, s * V.y);
	}; // sV

	Vec M(Vec V) {
		return V(-V.x, -V.y);
	}

	// ML: just like L1, but updates A
	static void lerpTo(Pt A, Pt B, float t) {
		A.x += t * (B.x - A.x);
		A.y += t * (B.y - A.y);
	}

	static void rotateAround(Pt Q, float angle, Pt center) {
		float dx = Q.x - center.x, dy = Q.y - center.y;
		float c = WarpicActivity.cos(angle), s = WarpicActivity.sin(angle);
		Q.x = center.x + c * dx - s * dy;
		Q.y = center.y + s * dx + c * dy;
	}

	
	static float findFurthestFinger(Pair l2, Pair r2,Pt ctr_of_roi) {
		float maxDistance = -9999;
		float currMax= MathUtility.d(l2.A0,ctr_of_roi);
		if(currMax>maxDistance)
			maxDistance=currMax;
		currMax=MathUtility.d(l2.B0,ctr_of_roi);
		if(currMax>maxDistance)
			maxDistance=currMax;
		currMax=MathUtility.d(r2.A0,ctr_of_roi);
		if(currMax>maxDistance)
			maxDistance=currMax;
		currMax=MathUtility.d(r2.B0,ctr_of_roi);
		if(currMax>maxDistance)
			maxDistance=currMax;
		
		return maxDistance;
	}
	static Pt findCtr(Pair l2, Pair r2) {
		Pt ret = new Pt();
		ret= MathUtility.average(r2.ctr(),l2.ctr());
	
		return ret;
	}
	
	public static void proxy_pairs(Pair l2, Pair r2,float ratio,Pt ctr) {
		proxy_pair(l2,ratio, new Pt(ctr.x,ctr.y));
		proxy_pair(r2,ratio, new Pt(ctr.x,ctr.y));
		
	}
	
	public static void proxy_pair(Pair l2, float ratio, Pt pt) {
		l2.A0 = perform_proxy_calculations(l2.A0,ratio, pt);
		l2.B0 = perform_proxy_calculations(l2.B0,ratio, pt);
		l2.A1 = perform_proxy_calculations(l2.A1,ratio, pt);
		l2.B1 = perform_proxy_calculations(l2.B1,ratio, pt);
		
	}
	
	public static Pt perform_proxy_calculations(Pt a0, float ratio, Pt pt) {
		Pt temp= new Pt();
		temp = temp.copy(a0.subtract(pt));
		temp.mul(ratio);
		temp.add(pt);
		return temp; 
	}
	
	
	/*
	 * ******************************************End of Math
	 * Utilities***************************************************
	 */
}
