package com.example.warpic;

import processing.core.PApplet;
class Pt { 
  float x=0, y=0, z=0;
  Pt(){
    this.x=0;
    this.y=0;
  } 
  Pt(float x,float y){
    this.x=x;
    this.y=y;
  }
  Pt(float x,float y, float z){
    this.x=x;
    this.y=y;
    this.z=z;
  }
  float distance(Pt a){
    return (float) Math.sqrt((this.x-a.x)*(this.x-a.x)+(this.y-a.y)*(this.y-a.y));  
  }
  void draw(PApplet p){
   //fill(255,0,00);
    p.ellipse(this.x,this.y,10,10);
  }
  void drawWithNum(PApplet p,int num){
    p.fill(255,0,0);
    p.ellipse(this.x,this.y,100,100);
    p.fill(50);
    p.textSize(50);
    p.text(num,this.x-4,this.y-4);
  }
  public void move(float dx, float dy) {
    this.x+=dx;
    this.y+=dy;
    
  }
  
  public Weight barycentric(Pt v1,Pt v2, Pt v3){
	  float a = v1.x - v3.x; 
	  float b = v2.x-v3.x; 
	  float c = v1.y - v3.y; 
	  float d = v2.y - v3.y; 
	  float det = a*d - b*c; 
	  float ia = d/det; 
	  float ib = -b/det; 
	  float ic = -c/det; 
	  float id = a/det;
	  float rx = this.x - v3.x; 
	  float ry = this.y - v3.y; 
	  float l1 = ia*rx+ ib*ry; 
	  float l2 = ic*rx +id*ry; 
	  return new Weight(l1,l2,1-l1-l2);
  }
  
  
  
 public void move(Pt delta){
    this.x+=delta.x;
    this.y+=delta.y;
  }

//  public Pt subtract(Pt a){
//    return new Pt(this.x-a.x,this.y-a.y);
//  }
    public Pt subtract(Pt a){
    return new Pt(this.x-a.x,this.y-a.y,this.z-a.z);
  }
  public String toString(){
    return "("+x+","+y+" ,"+z+")";
  }
//  public boolean equals(Pt a){
//    if(a.x==this.x&&a.y==this.y)
//    return true;
//    return false;  
//  }
    Pt add(float u, float v) {x += u; y += v; return this;}                       // P.add(u,v): P+=<u,v>
  Pt add(Pt P) {x += P.x; y += P.y; return this;};                              // incorrect notation, but useful for computing weighted averages
  Pt add(float s, Pt P)   {x += s*P.x; y += s*P.y; return this;};               // adds s*P
//  Pt add(Vec V) {x += V.x; y += V.y; return this;}                              // P.add(V): P+=V
//  Pt add(float s, Vec V) {x += s*V.x; y += s*V.y; return this;}                 // P.add(s,V): P+=sV
    Pt v(WarpicActivity wp) {wp.vertex(x,y); return this;};  // used for drawing polygons between beginShape(); and endShape();

  void set(Pt p){
   this.x=p.x;
   this.y=p.y; 
   this.z=p.z;
  }
  Pt make() {return(new Pt(x,y,z));};
//  void show(float r) { pushMatrix(); translate(x,y,z); sphere(r); popMatrix();}; 
//  void showLineTo (Pt P) {line(x,y,z,P.x,P.y,P.z); }; 
  void setToPoint(Pt P) { x = P.x; y = P.y; z = P.z;}; 
  void setTo(Pt P) { x = P.x; y = P.y; }//z = P.z;}; 
  void setTo (float px, float py) {x = px; y = py; }; 
////  void setToMouse() { x = mouseX; y = mouseY; }; 
////  void write() {println("("+x+","+y+","+z+")");};
////  void addVec(Vec V) {x += V.x; y += V.y; z += V.z;};
////  void addScaledVec(float s, Vec V) {x += s*V.x; y += s*V.y; z += s*V.z;};
////  void subVec(Vec V) {x -= V.x; y -= V.y; z -= V.z;};
////  void vert() {vertex(x,y,z);};
////  void vertext(float u, float v) {vertex(x,y,z,u,v);};
////  boolean isInWindow() {return(((x<0)||(x>width)||(y<0)||(y>height)));};
////  void label(String s, Vec D) {text(s, x+D.x, y+D.y, z+D.z);  };
////  Vec VecTo(Pt P) {return(new Vec(P.x-x,P.y-y,P.z-z)); };
  	  float disTo(Pt P) {return (float) (Math.sqrt((P.x-x)*(P.x-x)+(P.y-y)*(P.y-y)+(P.z-z)*(P.z-z))); };
////  Vec VecToMid(Pt P, Pt Q) {return(new Vec((P.x+Q.x)/2.0-x,(P.y+Q.y)/2.0-y,(P.z+Q.z)/2.0-z )); };
////  Vec VecToProp (Pt B, Pt D) {
////      Vec CB = this.VecTo(B); float LCB = CB.norm();
////      Vec CD = this.VecTo(D); float LCD = CD.norm();
////      Vec U = CB.make();
////      Vec V = CD.make(); V.sub(U); V.mul(LCB/(LCB+LCD));
//      U.add(V);
//      return(U);  
//      };  
  void addPt(Pt P) {x+=P.x; y+=P.y; z+=P.z;};
  void subPt(Pt P) {x-=P.x; y-=P.y; z-=P.z; };
  void mul(float f) {x*=f; y*=f; y*=f;};
  void pers(float d) { y=d*y/(d+z); x=d*x/(d+z); z=d*z/(d+z); };
  void inverserPers(float d) { y=d*y/(d-z); x=d*x/(d-z); z=d*z/(d-z); };
//  boolean coplanar (Pt A, Pt B, Pt C) {return(abs(tetVol(this,A,B,C))<0.0001);};
//  boolean cw (Pt A, Pt B, Pt C) {return(tetVol(this,A,B,C)>0.0001);};}
  Pt P() {return make(0,0); };                                                                            // make point (0,0)                                             
  Pt copy(Pt P) {return make(P.x,P.y); };                                                                    // make copy of point A
  Pt scale(float s, Pt A) {return new Pt(s*A.x,s*A.y); };                                                  // sA
//  Pt P(Pt P, Vec V) {return P(P.x + V.x, P.y + V.y); }                                                 //  P+V (P transalted by Vector V)
 // Pt P(Pt P, float s, Vec V) {return P(P,W(s,V)); }                                                    //  P+sV (P transalted by sV)
  Pt make(float x, float y) {return new Pt(x,y); };  
  Pt lerp(Pt A, float s, Pt B) {return make(A.x+s*(B.x-A.x),A.y+s*(B.y-A.y));};//,A.z+s*(B.z-A.z)); };                 // A+sAB
  // display 
  void show(Pt P, float r, PApplet pa) {pa.ellipse(P.x, P.y, 2*r, 2*r);};                                             // draws circle of center r around P
  void show(Pt P,PApplet pa) {pa.ellipse(P.x, P.y, 6,6);};                                                           // draws small circle around point
  void edge(Pt P, Pt Q,PApplet pa) {pa.line(P.x,P.y,Q.x,Q.y); };                                                      // draws edge (P,Q)
//  Vec triNormalFromPts(Pt A, Pt B, Pt C) {Vec N = cross(A.VecTo(B),A.VecTo(C));  return(N); };
//  float tetVol (Pt A, Pt B, Pt C, Pt D) { return(dot(triNormalFromPts(A,B,C),A.VecTo(D))); };
//  float dot(Vec U, Vec V) {return(U.x*V.x+U.y*V.y+U.z*V.z); };
//  Vec cross(Vec U, Vec V) {return(new Vec( U.y*V.z-U.z*V.y, U.z*V.x-U.x*V.z, U.x*V.y-U.y*V.x )); };
//  float mixed(Vec U, Vec V, Vec W) {return(dot(cross(U,V),W)); };
  Pt average (Pt A, Pt B) {return(new Pt((A.x+B.x)/2 , (A.y+B.y)/2, (A.z+B.z)/2 )); };
Pt average (Pt A, Pt B, Pt C) {return(new Pt((A.x+B.x+C.x)/3 , (A.y+B.y+C.y)/3, (A.z+B.z+C.z)/3 )); };
Pt average (Pt A, Pt B, Pt C, Pt D) {return(new Pt( (A.x+B.x+C.x+D.x)/4 , (A.y+B.y+C.y+D.y)/4, (A.z+B.z+C.z+D.z)/4 ) ); };
Pt between (Pt A, float s, Pt B) {return(new Pt((s-1)*A.x+s*B.x , (s-1)*A.y+s*B.y,(s-1)*A.z+s*B.z )); 
}
}
//Vec between (Vec A, float s, Vec B) {return(new Vec((s-1)*A.x+s*B.x , (s-1)*A.y+s*B.y,(s-1)*A.z+s*B.z )); };
//Vec dif(Pt A, Pt B) {return(new Vec( B.x-A.x , B.y-A.y , B.z-A.z)); };
//Vec dif(Vec U, Vec V) {return(new Vec(V.x-U.x,V.y-U.y,V.z-U.z)); };
//Vec sum(Vec U, Vec V) {return(new Vec(V.x+U.x,V.y+U.y,V.z+U.z)); };
//Vec average(Vec U, Vec V) {return(new Vec((U.x+V.x)/2,(U.y+V.y)/2,(U.z+V.z)/2)); };
//Vec average (Vec A, Vec B, Vec C, Vec D) {return(new Vec( (A.x+B.x+C.x+D.x)/4 , (A.y+B.y+C.y+D.y)/4, (A.z+B.z+C.z+D.z)/4 ) ); };
//

//************************************************************************
//**** SPIRAL
//************************************************************************
//Pt spiralPt(Pt A, Pt G, float s, float a) {return L(G,R(A,a,G),s);}  
//Pt spiralPt(Pt A, Pt G, float s, float a, float t) {return L(G,R(A,t*a,G),pow(s,t));} 
//Pt spiralCenter(Pt A, Pt B, Pt C, Pt D) { // computes center of spiral that takes A to C and B to D
//  float a = spiralAngle(A,B,C,D); 
//  float z = spiralScale(A,B,C,D);
//  return spiralCenter(a,z,A,C);
//  }
//float spiralAngle(Pt A, Pt B, Pt C, Pt D) {return angle(V(A,B),V(C,D));}
//float spiralScale(Pt A, Pt B, Pt C, Pt D) {return d(C,D)/d(A,B);}
//Pt spiralCenter(float a, float z, Pt A, Pt C) {
//  float c=cos(a), s=sin(a);
//  float D = sq(c*z-1)+sq(s*z);
//  float ex = c*z*A.x - C.x - s*z*A.y;
//  float ey = c*z*A.y - C.y + s*z*A.x;
//  float x=(ex*(c*z-1) + ey*s*z) / D;
//  float y=(ey*(c*z-1) - ex*s*z) / D;
//  return P(x,y);
//  }
//  
//Pt spiralT(Pt A, Pt B, Pt C, float t) {
//  float a =spiralAngle(A,B,B,C); 
//  float s =spiralScale(A,B,B,C);
//  Pt G = spiralCenter(a, s, A, B); 
//  return L(G,R(B,t*a,G),pow(s,t));
//  }
//  
//Pt spiral(Pt A, Pt B, Pt C, Pt D, float t, Pt Q) {
//  float a =spiralAngle(A,B,C,D); 
//  float s =spiralScale(A,B,C,D);
//  Pt G = spiralCenter(a, s, A, C); 
//  return L(G,R(Q,t*a,G),pow(s,t));
//  }
//  
//Pt spiralA(Pt A, Pt B, Pt C, Pt D, float t) {
//  float a =spiralAngle(A,B,C,D); 
//  float s =spiralScale(A,B,C,D);
//  Pt G = spiralCenter(a, s, A, C); 
//  return L(G,R(A,t*a,G),pow(s,t));
//  }
//  
//Pt spiralB(Pt A, Pt B, Pt C, Pt D, float t) {
//  float a =spiralAngle(A,B,C,D); 
//  float s =spiralScale(A,B,C,D);
//  Pt G = spiralCenter(a, s, A, C); 
//  return L(G,R(B,t*a,G),pow(s,t));
//  }
//  
//Pt onSpiral(Pt A, Pt B, Pt C) {
//  float a =spiralAngle(A,B,B,C); 
//  float s =spiralScale(A,B,B,C);
//  Pt G = spiralCenter(a, s, A, B); 
//  return L(G,R(B,a/2,G),sqrt(s));
//  }
//
//Pt spirals(Pt LA0, Pt LB0, Pt LA1, Pt LB1, Pt RA0, Pt RB0, Pt RA1, Pt RB1, float f, Pt Q0) {
//  float dL=d(Q0,P(LA0,LB0)), dR=d(Q0,P(RA0,RB0));
//  float roi=d(P(LA0,LB0),P(RA0,RB0));
//   float cL=sq(cos(dL/roi*PI/2)), cR=sq(cos(dR/roi*PI/2));
//  if (dL>roi) cL=0;  if (dR>roi) cR=0;
//  Pt QLt = spiral(LA0,LB0,LA1,LB1,f*cL,Q0); 
//  Pt QRt = spiral(RA0,RB0,RA1,RB1,f*cR,Q0); 
//  return P(P(Q0,1,V(Q0,QLt)),1,V(Q0,QRt));
//  }

