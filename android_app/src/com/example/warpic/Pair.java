package com.example.warpic;
import java.io.File;
import java.io.FileWriter;

import processing.core.PApplet;
class Pair {  

  Pt A0=new Pt(-20,-20);
  Pt B0=new Pt(20,-20);
  Pt A1=new Pt(-20,20);
  Pt B1=new Pt(20,20);
  Pt At;
  Pt Bt; 
  Pt G = new Pt(0,0);
  float a=0, s=1;
  Pt _ctr;
  float roiFactor, roiSq;
  
  Pair(){}
  
  Pair(Pt LA0, Pt LB0, Pt LA1, Pt LB1){A0=LA0; B0=LB0; A1=LA1; B1=LB1;}
  
  void show0(PApplet pa) {
	 // edge(A0,B0);
	  pa.line(A0.x,A0.y,B0.x,B0.y);
	  A0.show(A0,2,pa);
  
  }
  
  void showt(PApplet pa) {
	  
	 // edge(At,Bt); 
	  pa.line(At.x,At.y,Bt.x,Bt.y);
	  At.show(At,2,pa);
	  
  }
  
  void show1(PApplet pa) {
	  //edge(A1,B1);
	  pa.line(A1.x,A1.y,B1.x,B1.y);
	  A1.show(A1,2,pa);
  }
  
  Pair showAll(PApplet pa) {
    pa.strokeWeight(8);
    pa.stroke(255,0,0);
    show0(pa);
    pa.stroke(0,255,0);
    show1(pa); 
    pa.stroke(0,0,255);
    showt(pa); 
    pa.noStroke(); 
    return this; 
  }
  
  Pair evaluate(float t,WarpicActivity helper) { 
    a =helper.spiralAngle(A0,B0,A1,B1); 
    s =helper.spiralScale(A0,B0,A1,B1);
    G = helper.spiralCenter(a, s, A0, A1); 
    At = helper.L(G,helper.R(A0,t*a,G),helper.pow(s,t));
    Bt = helper.L(G,helper.R(B0,t*a,G),helper.pow(s,t));
    return this;
    }  
   
  	Pair evaluate1(float t,WarpicActivity helper) { 
    a =helper.spiralAngle(A0,B0,A1,B1); 
    s =helper.spiralScale(A0,B0,A1,B1);
    G =helper.spiralCenter(a, s, A0, A1); 
    return this;
    }
  	
  	Pt warp(Pt Q, float t, float roi,WarpicActivity helper) {   
  		float d=helper.d(Q,ctr(helper));
  		float c=helper.sq(helper.cos(d/roi*helper.PI/2));
  		if (d>roi) c=0; 
  		return helper.L(G,helper.R(Q,c*t*a,G),helper.pow(s,c*t));
   }
  		
  	Pt warp(Pt Q, float t,WarpicActivity helper) {return helper.L(G,helper.R(Q,t*a,G),helper.pow(s,t));} 
  
  	Pt ctr(WarpicActivity helper) {return helper.average(A0,B0);}
  	
  	void writePairsToFile(File file){
	  FileWriter fWriter;
	  try{
		  fWriter = new FileWriter(file, true);
		  fWriter.write(A0.x+","+A0.y+"\n"+B0.x+","+
		    		 B0.y+"\n"+A1.x+","+A1.y+"\n"+B1.x+","+B1.y+"\n");
		  fWriter.write("-\n");
		  fWriter.flush();
		  fWriter.close();
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }  
  }
  
  String addToString(){
		 return (A0.x+","+A0.y+"\n"+B0.x+","+B0.y+"\n"+A1.x+","+A1.y+"\n"+B1.x+","+B1.y+"\n-\n");
  }
  	
  	
    // ML: update cached data, used by warpDirectly
    void prepareToWarp(float roi,WarpicActivity helper) {
      _ctr = ctr(helper); // avoids recomputing this for every vertex in the grid
      roiFactor = 1/roi*helper.PI/2; // avoids several multiplications and reciprocals per vertex
      roiSq = helper.sq(roi);
    }
    
    void warpDirectly(Pt Q, float t, WarpicActivity helper) {   
      float dSq = helper.d2(Q, _ctr);
      float c = 0, ct = 0;
      // if we're outside the region-of-influence, don't bother computing anything
      if(dSq < roiSq) { // sqrt is expensive, so we square the roi and compare that against the point distance
        // to compute the true falloff weight, we do need to take the sqrt,
        // but since most points aren't in the ROI, we avoid calculating a sqrt for them
        c = helper.sq(helper.cos(helper.sqrt(dSq)*roiFactor));
        ct = c*t;
        helper.rotateAround(Q, ct*a, G); // updates Q
        helper.lerpTo(Q, G, 1-helper.pow(s, ct)); // updates Q
      }
    }
 } // end pair
