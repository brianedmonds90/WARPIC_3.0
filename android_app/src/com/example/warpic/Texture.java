package com.example.warpic;

import processing.core.PImage;

public class Texture {
	
	private float ww;
	private float hh;
	private float w;
	private float h;
	int n;
	WarpicActivity wActivity;
	Pt[][] G; // array of vertices
	MathUtility mu;
	public Texture(float displayWidth, float displayHeight, WarpicActivity wA,MathUtility mu){
		n = 50; // size of grid. Must be >2!
		ww = (float) (1.0 / (n - 1));
		hh = (float) (1.0 / (n - 1)); // set intial width and height of a cell
		w = displayWidth * ww;
		h = displayHeight * hh; // set intial width and height of a cell in
								// normalized [0,1]x[0,1]
		wActivity = wA;
		G = new Pt[n][n];
		allocVertices(); // ML: alloc all grid points before starting animation
	}
	
	/************************ TEXTURE *******************************************************************/
	void resetVertices() { // resets points and laplace vectors
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				G[i][j].setTo(i * w, j * h);
	}

	void warpVertices(Pt LA0, Pt LB0, Pt LA1, Pt LB1, Pt RA0, Pt RB0, Pt RA1,
			Pt RB1, float f) {
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				G[i][j] = mu.spirals(LA0, LB0, LA1, LB1, RA0, RB0, RA1, RB1, f,
						MathUtility.P(i * w, j * h));
	}

	void warpVertices(Pair L, float f, float roi) {
		L.prepareToWarp(roi); // precompute some values that are the same
									// for each call to warpDirectly
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				// G[i][j] = L.warp(G[i][j],f,roi,this);
				L.warpDirectly(G[i][j], f);
	}

	void warpVertices(Pair L, Pair R, float f) {
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				G[i][j] = warp(L, R, f, G[i][j]);
	}

	void allocVertices() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				G[i][j] = mu.P(i * w, j * h);
			}
		}
	}

	void paintImage(PImage myImage) {
		wActivity.noStroke();
		wActivity.noFill();
		wActivity.textureMode(wActivity.NORMAL); // texture parameters in [0,1]x[0,1]
		// beginShape(QUADS);
		for (int i = 0; i < n - 1; i++) {
			wActivity.beginShape(wActivity.QUAD_STRIP);
			wActivity.texture(myImage);
			for (int j = 0; j < n; j++) {
				wActivity.vertex(G[i][j].x, G[i][j].y, i * ww, j * hh);
				wActivity.vertex(G[i + 1][j].x, G[i + 1][j].y, (i + 1) * ww, j * hh);
			}
			wActivity.endShape();
		}
	}

	void drawEdges() {
		wActivity.stroke(10, 10, 10);
		wActivity.noFill();
		wActivity.strokeWeight(2);
		// beginShape(QUADS);
		for (int i = 0; i < n - 1; i++) {
			wActivity.beginShape(wActivity.QUAD_STRIP);
			for (int j = 0; j < n; j++) {
				wActivity.vertex(G[i][j].x, G[i][j].y);
				wActivity.vertex(G[i + 1][j].x, G[i + 1][j].y);
			}
			;
			wActivity.endShape();
		}
		;
	}

	void drawVertices() {
		wActivity.noStroke();
		wActivity.fill(255, 0, 0);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				wActivity.show(G[i][j], 1);
	}

	Pt warp(Pair LPair, Pair R, float f, Pt Q0) {
		Pt QLt = LPair.warp(Q0, f);
		Pt QRt = R.warp(Q0, f);
		float dL = MathUtility.d(Q0, LPair.ctr()), dR = MathUtility.d(Q0, R.ctr());
		//float roi = d(LPair.ctr(this), R.ctr(this));
		float a = dL / (dL + dR);
		float cL = WarpicActivity.sq(WarpicActivity.cos(a * WarpicActivity.PI / 2)), 
				cR = WarpicActivity.sq(WarpicActivity.sin(a * WarpicActivity.PI / 2));
		return mu.P(cL, QLt, cR, QRt);
	}

	/************************************* END OF TEXTURE **************************/
	/*****************************************************************************/
	
}
