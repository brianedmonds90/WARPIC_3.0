package com.example.warpic;

import processing.core.PApplet;

class Vec { float x=0,y=0,z; 
// CREATE
 Vec () {};
 Vec (float px, float py) {x = px; y = py;};
 Vec (float px, float py, float pz) {x = px; y = py; z = pz;};
 void setTo (float px, float py, float pz) {x = px; y = py; z = pz;}; 
// MODIY
 Vec setTo(float px, float py) {x = px; y = py; return this;}; 
 Vec setTo(Vec V) {x = V.x; y = V.y; return this;}; 
 Vec zero() {x=0; y=0; return this;}
 Vec scaleBy(float u, float v) {x*=u; y*=v; return this;};
 Vec scaleBy(float f) {x*=f; y*=f; return this;};
 Vec reverse() {x=-x; y=-y; return this;};
 Vec divideBy(float f) {x/=f; y/=f; return this;};
 Vec normalize() {float n=WarpicActivity.sqrt(WarpicActivity.sq(x)+WarpicActivity.sq(y)); if (n>0.000001) {x/=n; y/=n;}; return this;};
 Vec add(float u, float v) {x += u; y += v; return this;};
 Vec add(Vec V) {x += V.x; y += V.y; return this;};   
 Vec add(float s, Vec V) {x += s*V.x; y += s*V.y; return this;};   
 
 

 
 Vec rotateBy(float a) {float xx=x, yy=y; x=xx*WarpicActivity.cos(a)-yy*WarpicActivity.sin(a); y=xx*WarpicActivity.sin(a)+yy*WarpicActivity.cos(a); return this;};
 Vec left() {float m=x; x=-y; y=m; return this;};
 void mul(float m) {x *= m; y *= m; z *= m;};
  void addScaled(float m, Vec V) {x += m*V.x; y += m*V.y; z += m*V.z;};
 Vec make() {return(new Vec(x,y,z));};
 void show (Pt P,PApplet pa) {pa.line(P.x,P.y, P.z,P.x+x,P.y+y,P.z+z); }; 

 void sub(Vec V) {x -= V.x; y -= V.y; z -= V.z;};
 void div(float m) {x /= m; y /= m; z /= m;};
void makeUnit() {float n=this.norm(); if (n>0.0001) {this.div(n);};};
 void back() {x= -x; y= -y; z= -z;};
// boolean coplanar (Vec V, Vec W) {return(abs(mixed(this,V,W))<0.0001);};
// boolean cw (Vec U, Vec V, Vec W) {return(mixed(this,V,W)>0.0001);};
   public String toString(){
   return "("+x+","+y+" ,"+z+")";
 }  



 // OUTPUT VEC
 protected Vec clone() {return(new Vec(x,y));}; 

 // OUTPUT TEST MEASURE
 float norm() {return(WarpicActivity.sqrt(WarpicActivity.sq(x)+WarpicActivity.sq(y)));}
 boolean isNull() {return((WarpicActivity.abs(x)+WarpicActivity.abs(y)<0.000001));}
 float angle() {return(WarpicActivity.atan2(y,x)); }

 // DRAW, PRINT
 void write() {System.out.println("<"+x+","+y+">");};
 void showAt (Pt P,PApplet pa) {pa.line(P.x,P.y,P.x+x,P.y+y); }; 
// void showArrowAt (Pt P,PApplet pa) {pa.line(P.x,P.y,P.x+x,P.y+y); 
//     float n=min(this.norm()/10.,height/50.); 
//     Pt Q=P(P,this); 
//     Vec U = S(-n,U(this));
//     Vec W = S(.3,R(U)); 
//     beginShape(); Q.add(U).add(W).v(); Q.v(); Q.add(U).add(M(W)).v(); endShape(CLOSE); }; 
 //void label(String s, pt P) {P(P).add(0.5,this).add(3,R(U(this))).label(s); };
 } // end vec class
