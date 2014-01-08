//Brian Edmonds
static color red = color(200, 10, 10), blue = color(10, 10, 200), green = color(10, 200, 20), 
magenta = color(200, 50, 200), black = color(10, 10, 10), gray = color(205, 201, 201),
white = color(255);
boolean onStartup, cameraPreview, pictureTaken, showPicForWarping, selectFile, fingersOnScreen, 
animate, record, startTimer, showController, firstRun, ptsLoaded, showSpirals;
PImage myImage;
int numFrames;

ArrayList<pt> A, B, C, D; //Declare finger movement list
int deviceDisplayW, deviceDisplayH;
float roi, t, f;
boolean imageSelected;
static int tracking;
int displayWidth, displayHeight;
pair L, R;
int offsetW, offsetH;
int startMillis;
boolean start;
float ellapsedTime;
boolean saveAnimationURI=false;
ArrayList<String> uris;

void setup() {
  size(1280, 800,P3D); 
  displayWidth=width;
  displayHeight=height;
  frameRate(30);
  noSmooth();
  //store framerate and allow user to increase/decrease framerate
  ptsLoaded=false;
  //instantiate and locate the take photo button
  //setup the values for the grid
  offsetW=0; 
  offsetH=0;
  ww=1.0/(n-1); 
  hh=1.0/(n-1);               // set intial width and height of a cell
  w=displayWidth*ww; 
  h=displayHeight*hh;         // set intial width and height of a cell in normalized [0,1]x[0,1]
  myImage= createImage(displayWidth, displayHeight, RGB);
  firstRun=true;
  ///*************Adding history of points lists
  A=new ArrayList<pt>();
  B=new ArrayList<pt>();
  C=new ArrayList<pt>();
  D=new ArrayList<pt>();
  //****************
  fingersOnScreen=false;
  showSpirals=false;
  tracking=0;
  animate=true;
  record=false;
  startTimer=false;
  showController=true;
  imageSelected=false;
  L=new pair(new pt(100, 100), new pt(200, 200), new pt(150, 150), new pt(250, 250));
  R=new pair(new pt(260, 200), new pt(400, 400), new pt(500, 300), new pt(450, 500));
  allocVertices(); // ML: alloc all grid points before starting animation
  initArrays();
  start=true;
  
  //Motion Data is grabbed here
  //motionSelected("motion.txt");
  
}

void draw() {

 // long time= System.nanoTime();
  if(start){
    startMillis=millis();
    start=false;
    numFrames = max(max(A.size(),B.size()),max(C.size(),D.size()));
   // println("currentMillis: "+startMillis);
  }
  
  background(255);
  resetVertices(); 
  double currentT= millis()-startMillis;
  currentT=currentT/1000.0;
  if (animate) {
    animateWarping(currentT);
  }

  //End of animation loop
  
  if (showSpirals) {
    L.showAll();
    R.showAll();
  }

  if (showEdges) drawEdges();
  if(showTexture) paintImage(myImage); 

//image(myImage,0,0);
	
}//end of draw loop

  public int computeAnimationFrame(double currentT, double totalAnimationT, int numFrames){
    return (int)((currentT/totalAnimationT)*numFrames);
  }

void keyPressed() {
  if (key=='!') snapPicture(); // make a picture of the canvas
  if (key=='L') loadPts(); 
  if (key=='l') loadPts(); 
  if (key=='a') animate=!animate;
  if(key=='s') saveFrame();
}

void realTimeWarp() {
  roi=d(L.ctr(), R.ctr()); //Find the region of influence for the warping
  L.evaluate(1);
  R.evaluate(1);
  warpVertices1(L, 1, roi);//Warp the vertices
  warpVertices1(R, 1, roi);
}

void saveWarp() {//Bakes the warped image as a PImage
  noLoop();
  myImage=get(0, 0, width, height);
  // myImage=get(offsetW, offsetH, myImage.width, myImage.height);
  loop();
}

void animateWarping(double currentT) {
  tracking=computeAnimationFrame(currentT, ellapsedTime, numFrames);
  animateUpdate1(tracking);//Advance the current pairs along the user's path
  roi=d(L.ctr(), R.ctr()); //Find the region of influence for the warping
  L.evaluate(f);
  R.evaluate(f);
  warpVertices1(L, f, roi);//Warp the vertices
  warpVertices1(R, f, roi);
  tracking++; 
  f=1;

  if (tracking>numFrames) {
    tracking=0;
    start=true;
    
  }
}

void drawArrayList(ArrayList<pt> list) {
  for (pt p: list) {
    p.draw();
  }
}

void animateUpdate1(int index) {//used to traverse through the users previous finger movements on the screen
  if (A.size()>index) {
    L.A1=A.get(index);
  }
  if (B.size()>index)
    L.B1=B.get(index);
  if (C.size()>index)
    R.A1=C.get(index);
  if (D.size()>index)
    R.B1=D.get(index);
}

int pictureCounter=0;
import java.util.Scanner;
void snapPicture() {
  console.log("insde snap picture Brian Edmonds");
  //saveFrame("PICTURES/P"+nf(pictureCounter++, 3)+".jpg");
  //saveFrame();
  console.log(save("testPic.png"));
}
Scanner scan;
void loadPts() {
  selectFolder("Select a folder to process:", "folderSelected");
}
void imageSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } 
  else {
    println("User selected " + selection.getAbsolutePath());
    myImage=loadImage('/'+selection.getAbsolutePath());
    imageSelected=true;
  }
}
void initGrid(PImage img) {
  offsetW=(width-myImage.width)/2;
  offsetH=(height-myImage.height)/2;
  h=img.height*hh;
  w=img.width*hh;
}
void motionSelected(String fileName) {
  if (fileName == null) {
    println("Window was closed or the user hit cancel.");
  } 
  else {
    seperatePts(loadStrings(fileName));
    ptsSetup();
  }
}

void loadMotion(String motion){
  seperatePts(split(motion,"\n"));
  ptsSetup();
}

void fun(PImage img){
	myImage=img;
}

void loadMyImage(String url){
	loadImage(url,"",fun);
	showTexture=true;

}

void ptsSetup(){
    ptsLoaded=true;
    if (imageSelected) {
      myResize(myImage);
      initGrid(myImage);
    showEdges=false;
      showTexture=true;
    }  
}

void generate_uri(){
   saveAnimationURI=true;
   tracking=0;
}

//Takes in the finger motion lists and adds them to the user motion paths
ArrayList<pt> addPtsToList(ArrayList<String> ss) {
  ArrayList<pt> list = new ArrayList<pt>();
  int s=0; 
  int comma;   
  float x, y; 
  pt n=new pt(0, 0);  
  for (int k=0; k<ss.size(); k++) { 
    comma=ss.get(k).indexOf(',');   
    x=float(ss.get(k).substring(0, comma)); 
    y=float(ss.get(k).substring(comma+1, ss.get(k).length()));
    n=scaleForDesktop(new pt(x, y));
    list.add(n);
  }
  //scaleForDesktop(list);
  return list;
}
//Seperates a text file into seperate ArrayLists each one represents a different fingers motions
void seperatePts(String [] ss) {  
  int spaces=0;
  ArrayList<String> myList=new ArrayList();
  for (int i=0;i<ss.length;i++) {
    if (ss[i].equals("-")) {
      if (spaces==0) {

        L=L.fromStringList(myList);
      }  
      else if (spaces==1) {
        R=L.fromStringList(myList);
      }
      else if (spaces==2)
        A= addPtsToList(myList);
      else if (spaces==3)
        B= addPtsToList(myList);
      else if (spaces==4) {
        C=addPtsToList(myList);
      }
      else if (spaces==5) {
        D=addPtsToList(myList);
      }
      else {
        break;
      }
      myList.clear();
      spaces++;
    }
    else if (ss[i].equals("display_width"))
    {
      deviceDisplayW=int(ss[i+1]); 
      ++i;
    }
    else if (ss[i].equals("display_height")) {
      deviceDisplayH=int(ss[i+1]); 
      ++i;
    }
    else if(ss[i].equals("ellapsedTime")){
      ellapsedTime= float(ss[i+1]);
      ++i;
    }
    else {
      myList.add(ss[i]);
    }
  }
}
void printArrayList(ArrayList<pt> list) {
  for (int i=0;i<list.size();i++) {
  //  println(list.get(i));
  }
}
void printArray(String [] ss) {
  for (int i=0;i<ss.length;i++) {
    println(i+": "+ss[i]);
  }
}
void myResize(PImage img) {
  img.resize(1280, 0);
}
void initArrays() {

  for (int i=0;i<50;i++) {
    A.add(new pt(i+10, i+5));
    B.add(new pt(i+30, i+35));
    C.add(new pt(i+100, i+76));
    D.add(new pt(i, i));
  }
}


// pairs of edges defining a spiral

class pair {  
  pt A0 = makePt(-20, -20);
  pt B0 = makePt(20, -20);
  pt A1 = makePt(-20, 20);
  pt B1 = makePt(20, 20);
  pt At;
  pt Bt; 
  pt G = P();
  float a=0, s=1;
  
  // cached data, update by calling .prepareToWarp()
  pt _ctr;
  float roiFactor, roiSq;

  pair(pt LA0, pt LB0, pt LA1, pt LB1) {
    A0=LA0; 
    B0=LB0; 
    A1=LA1; 
    B1=LB1;
  }
  void show0() {
    edge(A0, B0); 
    showWithRadius(A0, 2);
  }
  void showt() {
    edge(At, Bt); 
    showWithRadius(At, 2);
  }
  void show1() {
    edge(A1, B1); 
    showWithRadius(A1, 2);
  }
  pair showAll() {
    strokeWeight(8);
    stroke(red);
    show0();
    stroke(blue);
    show1(); 
    stroke(green);
    // showt(); 
    noStroke(); 
    return this;
  }
  pair fromStringList(ArrayList<String> ss) {
    pair ret= new pair(new pt(0, 0), new pt(0, 0), new pt(0, 0), new pt(0, 0)); 
    //println("ss.size()"+ss.size());
    int comma;
    ArrayList <pt> myPts= new ArrayList<pt>();
    float x, y;
    pt n;
    for (int i=0;i<ss.size();i++) {
      comma=ss.get(i).indexOf(','); 
      x=float(ss.get(i).substring(0, comma)); 
      y=float(ss.get(i).substring(comma+1, ss.get(i).length()));
      n=scaleForDesktop(new pt(x, y));
      myPts.add(n);
    }
    //scaleForDesktop(myPts);
    ret.A0=myPts.get(0); 
    ret.B0=myPts.get(1);
    ret.A1=myPts.get(2);
    ret.B1=myPts.get(3);
    return ret;
  }     
  pair evaluate(float t) { 
    a =spiralAngle(A0, B0, A1, B1); 
    s =spiralScale(A0, B0, A1, B1);
    G = spiralCenter1(a, s, A0, A1); 
    At = L1(G, R1(A0, t*a, G), pow(s, t));
    Bt = L1(G, R1(B0, t*a, G), pow(s, t));
    return this;
  }  
  pair evaluate1(float t) { 
    a =spiralAngle(A0, B0, A1, B1); 
    s =spiralScale(A0, B0, A1, B1);
    G = spiralCenter1(a, s, A0, A1); 
    // At = L(G,R(A0,t*a,G),pow(s,t));
    // Bt = L(G,R(B0,t*a,G),pow(s,t));
    return this;
  }
  
  // ML: update cached data, used by warpDirectly
  void prepareToWarp(float roi) {
    _ctr = ctr(); // avoids recomputing this for every vertex in the grid
    roiFactor = 1/roi*PI/2; // avoids several multiplications and reciprocals per vertex
    roiSq = sq(roi);
  }
  
  pt warp(pt Q, float t) {   
    pt QQ = new pt(Q.x, Q.y);
    warpDirectly(QQ, t);
    return QQ;
  }
  
  void warpDirectly(pt Q, float t) {   
    float dSq = d2(Q, _ctr);
    float c = 0, ct = 0;
    // if we're outside the region-of-influence, don't bother computing anything
    if(dSq < roiSq) { // sqrt is expensive, so we square the roi and compare that against the point distance
      // to compute the true falloff weight, we do need to take the sqrt,
      // but since most points aren't in the ROI, we avoid calculating a sqrt for them
      c = sq(cos(sqrt(dSq)*roiFactor));
      ct = c*t;
      rotateAround(Q, ct*a, G); // updates Q
      lerpTo(Q, G, 1-pow(s, ct)); // updates Q
    }
  }

  pt warp1(pt Q, float t) {
    return L1(G, R1(Q, t*a, G), pow(s, t));
  }

  pt ctr() {
    return average(A0, B0);
  }
  pt briansCtr() {
    return average(At, Bt);
  }
  String toString() {
    return "A0: "+A0+"\n A1: "+A1+"\n B0: "+B0+"\n B1: "+B1;
  }
} // end pair

pt warp2(pair LPair, pair R, float f, pt Q0) {
  pt QLt = LPair.warp1(Q0, f); 
  pt QRt = R.warp1(Q0, f);
  float dL=d(Q0, LPair.ctr()), dR=d(Q0, R.ctr());
  float roi=d(LPair.ctr(), R.ctr());
  float a = dL/(dL+dR);
  float cL=sq(cos(a*PI/2)), cR=sq(sin(a*PI/2));
  return weightedAvg(cL, QLt, cR, QRt);
}



class pt {
  
  float x, y;
  
  pt(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  float distance(pt a) {
    return (float) Math.sqrt((this.x-a.x)*(this.x-a.x)+(this.y-a.y)*(this.y-a.y));
  }
  
  void draw() {
    ellipse(this.x, this.y, 10, 10);
  }
  void drawWithNum(int num) {
    fill(255, 0, 0);
    ellipse(this.x, this.y, 100, 100);
    fill(50);
    textSize(50);
    text(num, this.x-4, this.y-4);
  }
  public void move(float dx, float dy) {
    this.x+=dx;
    this.y+=dy;
  }
  
  public void move(pt delta) {
    this.x+=delta.x;
    this.y+=delta.y;
  }

  public pt subtract(pt a) {
    return new pt(this.x-a.x, this.y-a.y);
  }

  public String toString() {
    return "("+x+","+y+" ,"+z+")";
  }

  pt add1(float u, float v) {
    x += u; 
    y += v; 
    return this;
  }                       // P.add(u,v): P+=<u,v>

  pt add2(pt P) {
    x += P.x; 
    y += P.y; 
    return this;
  };                              // incorrect notation, but useful for computing weighted averages

    pt add3(float s, pt P) {
    x += s*P.x; 
    y += s*P.y; 
    return this;
  };               // adds s*P

  pt add4(vec V) {
    x += V.x; 
    y += V.y; 
    return this;
  }                              // P.add(V): P+=V

  pt add(float s, vec V) {
    x += s*V.x; 
    y += s*V.y; 
    return this;
  }                 // P.add(s,V): P+=sV

  pt v() {
    vertex(x, y); 
    return this;
  };  // used for drawing polygons between beginShape(); and endShape();

  void set(pt p) {
    this.x=p.x;
    this.y=p.y; 
  }

  pt make() {
    return(new pt(x, y));
  };

  void show(float r) { 
    pushMatrix(); 
    translate(x, y, z); 
    sphere(r); 
    popMatrix();
  }

  void showLineTo (pt P) {
    line(x, y, P.x, P.y);
  }

  void setToPoint(pt P) { 
    x = P.x; 
    y = P.y; 
  }

  void setToPt(pt P) { 
    x = P.x; 
    y = P.y;
  } 

  void setTo (float px, float py) {
    x = px; 
    y = py;
  }

  void setToMouse() { 
    x = mouseX; 
    y = mouseY;
  }

  void write() {
    println("("+x+","+y+","+z+")");
  };

  void addVec(vec V) {
    x += V.x; 
    y += V.y; 
  }

  void addScaledVec(float s, vec V) {
    x += s*V.x; 
    y += s*V.y; 
  }

  void subVec(vec V) {
    x -= V.x; 
    y -= V.y; 
  };

  void vert() {
    vertex(x, y);
  };

  void vertext(float u, float v) {
    vertex(x, y, u, v);
  };

  boolean isInWindow() {
    return(((x<0)||(x>width)||(y<0)||(y>height)));
  };

  void label(String s, vec D) {
    text(s, x+D.x, y+D.y);
  };
  
  float disTo(pt P) {
    return sqrt(sq(P.x-x)+sq(P.y-y));
  }

  void addPt(pt P) {
    x+=P.x; 
    y+=P.y; 
  }

  void subPt(pt P) {
    x-=P.x; 
    y-=P.y; 
  }

  void mul(float f) {
    x*=f; 
    y*=f; 
  };

  void pers(float d) { 
    y=d*y/(d+z); 
    x=d*x/(d+z); 
  };

  void inverserPers(float d) { 
    y=d*y/(d-z); 
    x=d*x/(d-z); 
  }
}

pt P() {
  return makePt(0, 0);
};                                                                            // make point (0,0)                                             

pt copyPt(pt P) {
  return makePt(P.x, P.y);
};                                                                    // make copy of point A

pt scalePt(float s, pt A) {
  return new pt(s*A.x, s*A.y);
};                                                  // sA

pt addVec(pt P, vec V) {
  return makePt(P.x + V.x, P.y + V.y);
}                                                 //  P+V (P transalted by vector V)

pt addScaledVec(pt P, float s, vec V) {
  return addVec(P, W(s, V));
}                                                    //  P+sV (P transalted by sV)

pt makePt(float x, float y) {
  return new pt(x, y);
};  

pt addScaledPtVec(pt A, float s, pt B) {
  return makePt(A.x+s*(B.x-A.x), A.y+s*(B.y-A.y));
}

// display 
void showWithRadius(pt P, float r) {
  ellipse(P.x, P.y, 2*r, 2*r);
};                                             // draws circle of center r around P

void show(pt P) {
  ellipse(P.x, P.y, 6, 6);
};                                                           // draws small circle around point

void edge(pt P, pt Q) {
  line(P.x, P.y, Q.x, Q.y);
};                                                      // draws edge (P,Q)

float dot(vec U, vec V) {
  return(U.x*V.x+U.y*V.y);
};

//************************************************************************
//**** SPIRAL
//************************************************************************
pt spiralPt(pt A, pt G, float s, float a) {
  return L1(G, R1(A, a, G), s);
}  

pt spiralPt1(pt A, pt G, float s, float a, float t) {
  return L1(G, R1(A, t*a, G), pow(s, t));
} 

pt spiralCenter(pt A, pt B, pt C, pt D) { // computes center of spiral that takes A to C and B to D
  float a = spiralAngle(A, B, C, D); 
  float z = spiralScale(A, B, C, D);
  return spiralCenter1(a, z, A, C);
}

float spiralAngle(pt A, pt B, pt C, pt D) {
  return angle(V(A, B), V(C, D));
}

float spiralScale(pt A, pt B, pt C, pt D) {
  return d(C, D)/d(A, B);
}

pt spiralCenter1(float a, float z, pt A, pt C) {
  float c=cos(a), s=sin(a);
  float D = sq(c*z-1)+sq(s*z);
  float ex = c*z*A.x - C.x - s*z*A.y;
  float ey = c*z*A.y - C.y + s*z*A.x;
  float x=(ex*(c*z-1) + ey*s*z) / D;
  float y=(ey*(c*z-1) - ex*s*z) / D;
  return makePt(x, y);
}

pt spiralT(pt A, pt B, pt C, float t) {
  float a =spiralAngle(A, B, B, C); 
  float s =spiralScale(A, B, B, C);
  pt G = spiralCenter1(a, s, A, B); 
  return L1(G, R1(B, t*a, G), pow(s, t));
}

pt spiral(pt A, pt B, pt C, pt D, float t, pt Q) {
  float a =spiralAngle(A, B, C, D); 
  float s =spiralScale(A, B, C, D);
  pt G = spiralCenter1(a, s, A, C); 
  return L1(G, R1(Q, t*a, G), pow(s, t));
}

pt spiralA(pt A, pt B, pt C, pt D, float t) {
  float a =spiralAngle(A, B, C, D); 
  float s =spiralScale(A, B, C, D);
  pt G = spiralCenter1(a, s, A, C); 
  return L1(G, R1(A, t*a, G), pow(s, t));
}

pt spiralB(pt A, pt B, pt C, pt D, float t) {
  float a =spiralAngle(A, B, C, D); 
  float s =spiralScale(A, B, C, D);
  pt G = spiralCenter1(a, s, A, C); 
  return L1(G, R1(B, t*a, G), pow(s, t));
}

pt onSpiral(pt A, pt B, pt C) {
  float a =spiralAngle(A, B, B, C); 
  float s =spiralScale(A, B, B, C);
  pt G = spiralCenter1(a, s, A, B); 
  return L1(G, R1(B, a/2, G), sqrt(s));
}

pt spirals(pt LA0, pt LB0, pt LA1, pt LB1, pt RA0, pt RB0, pt RA1, pt RB1, float f, pt Q0) {
  float dL=d(Q0, average(LA0, LB0)), dR=d(Q0, average(RA0, RB0));
  float roi=d(average(LA0, LB0), average(RA0, RB0));
  float cL=sq(cos(dL/roi*PI/2)), cR=sq(cos(dR/roi*PI/2));
  if (dL>roi) cL=0;  
  if (dR>roi) cR=0;
  pt QLt = spiral(LA0, LB0, LA1, LB1, f*cL, Q0); 
  pt QRt = spiral(RA0, RB0, RA1, RB1, f*cR, Q0); 
  return addScaledVec(addScaledVec(Q0, 1, V(Q0, QLt)), 1, V(Q0, QRt));
}

pt scaleForDesktop(pt r) {
  float x, y;
  x=(r.x*800.0)/1280.0;
  y=(r.y*600.0)/960.0;
  return new pt(x, y);
}

// transform 
pt R(pt Q, float a) {
  float dx=Q.x, dy=Q.y, c=cos(a), s=sin(a); 
  return new pt(c*dx+s*dy, -s*dx+c*dy);
}  // Q rotated by angle a around the origin

pt R1(pt Q, float a, pt C) {
  float dx=Q.x-C.x, dy=Q.y-C.y, c=cos(a), s=sin(a); 
  return makePt(C.x+c*dx-s*dy, C.y+s*dx+c*dy);
}  // Q rotated by angle a around point P

// ML: just like R1, but updates Q
void rotateAround(pt Q, float angle, pt center) {
  float dx = Q.x - center.x, dy = Q.y - center.y;
  float c = cos(angle), s = sin(angle); 
  Q.x = center.x + c*dx - s*dy;
  Q.y = center.y + s*dx + c*dy;
}

pt MoveByDistanceTowards(pt P, float d, pt Q) { 
  return addScaledVec(P, d, U(V(P, Q)));
}                          //  P+dU(PQ) (transLAted P by *distance* s towards Q)!!!

// average 
pt average(pt A, pt B) {
  return makePt((A.x+B.x)/2.0, (A.y+B.y)/2.0);
}                                          // (A+B)/2 (average)

pt average3(pt A, pt B, pt C) {
  return makePt((A.x+B.x+C.x)/3.0, (A.y+B.y+C.y)/3.0);
}                            // (A+B+C)/3 (average)

pt average4(pt A, pt B, pt C, pt D) {
  return average(average(A, B), average(C, D));
}                                            // (A+B+C+D)/4 (average)

// measure 
boolean isSame(pt A, pt B) {
  return (A.x==B.x)&&(A.y==B.y) ;
}                                         // A==B

boolean isSame1(pt A, pt B, float e) {
  return ((abs(A.x-B.x)<e)&&(abs(A.y-B.y)<e));
}                   // ||A-B||<e

float d(pt P, pt Q) {
  return sqrt(d2(P, Q));
};                                                       // ||AB|| (Distance)

float d2(pt P, pt Q) {
  return sq(Q.x-P.x)+sq(Q.y-P.y);
}                                             // AB*AB (Distance squared)

vec copyVec(vec V) {
  return new vec(V.x, V.y);
}                                                             // make copy of vector V

vec fromPt(pt P) {
  return new vec(P.x, P.y);
}                                                              // make vector from origin to P

vec fromFloats(float x, float y) {
  return new vec(x, y);
}                                                      // make vector (x,y)

vec V(pt P, pt Q) {
  return new vec(Q.x-P.x, Q.y-P.y);
}                                                 // PQ (make vector Q-P from P to Q

vec U(vec V) {
  float n = n(V); 
  if (n==0) return new vec(0, 0); 
  else return new vec(V.x/n, V.y/n);
}      // V/||V|| (Unit vector : normalized version of V)

vec unitPoints(pt P, pt Q) {
  return U(V(P, Q));
}                                                                // PQ/||PQ| (Unit vector : from P towards Q)

vec MouseDrag() {
  return new vec(mouseX-pmouseX, mouseY-pmouseY);
}                                      // vector representing recent mouse displacement

// Interpolation 

vec L(vec U, vec V, float s) {
  return new vec(U.x+s*(V.x-U.x), U.y+s*(V.y-U.y));
}                      // (1-s)U+sV (Linear interpolation between vectors)

vec S(vec U, vec V, float s) {
  float a = angle(U, V); 
  vec W = R(U, s*a); 
  float u = n(U); 
  float v=n(V); 
  W(pow(v/u, s), W); 
  return W;
} // steady interpolation from U to V

pt L1(pt A, pt B, float t) {
  return makePt(A.x+t*(B.x-A.x), A.y+t*(B.y-A.y));
}

// ML: just like L1, but updates A
void lerpTo(pt A, pt B, float t) {
  A.x += t*(B.x-A.x);
  A.y += t*(B.y-A.y);
}

// measure 
//float dot(vec U, vec V) {return U.x*V.x+U.y*V.y; }                                                     // dot(U,V): U*V (dot product U*V)
float det(vec U, vec V) {
  return dot(rotate90(U), V);
}                                                         // det | U V | = scalar cross UxV 

float n(vec V) {
  return sqrt(dot(V, V));
}                                                               // n(V): ||V|| (norm: length of V)

float n2(vec V) {
  return sq(V.x)+sq(V.y);
}                                                             // n2(V): V*V (norm squared)

boolean parallel (vec U, vec V) {
  return dot(U, rotate90(V))==0;
} 

float angle (vec U, vec V) {
  return atan2(det(U, V), dot(U, V));
}                                   // angle <U,V> (between -PI and PI)

float angle1(vec V) {
  return(atan2(V.y, V.x));
}                                                       // angle between <1,0> and V (between -PI and PI)

float angle2(pt A, pt B, pt C) {
  return  angle(V(B, A), V(B, C));
}                                       // angle <BA,BC>

float turnAngle(pt A, pt B, pt C) {
  return  angle(V(A, B), V(B, C));
}                                   // angle <AB,BC> (positive when right turn as seen on screen)

int toDeg(float a) {
  return int(a*180/PI);
}                                                           // convert radians to degrees

float toRad(float a) {
  return(a*PI/180);
}                                                             // convert degrees to radians 

float positive(float a) { 
  if (a<0) return a+TWO_PI; 
  else return a;
}                                   // adds 2PI to make angle positive

// weighted sum 
vec W(float s, vec V) {
  return fromFloats(s*V.x, s*V.y);
}                                                      // sV

vec W1(vec U, vec V) {
  return fromFloats(U.x+V.x, U.y+V.y);
}                                                   // U+V 

vec W2(vec U, float s, vec V) {
  return W1(U, S(s, V));
}                                                   // U+sV

vec W3(float u, vec U, float v, vec V) {
  return W1(S(u, U), S(v, V));
}                                   // uU+vV ( Linear combination)

// weighted average 
//pt P(float a, pt A) {return P(a*A.x,a*A.y);}                                                      // aA  
pt weightedAvg(float a, pt A, float b, pt B) {
  return makePt(a*A.x+b*B.x, a*A.y+b*B.y);
}                              // aA+bB, (a+b=1) 

pt weightedAvg1(float a, pt A, float b, pt B, float c, pt C) {
  return makePt(a*A.x+b*B.x+c*C.x, a*A.y+b*B.y+c*C.y);
}   // aA+bB+cC 

pt weightedAvg2(float a, pt A, float b, pt B, float c, pt C, float d, pt D) {
  return makePt(a*A.x+b*B.x+c*C.x+d*D.x, a*A.y+b*B.y+c*C.y+d*D.y);
} // aA+bB+cC+dD 

vec rotate90(vec V) {
  return new vec(-V.y, V.x);
}                                                             // V turned right 90 degrees (as seen on screen)

vec R(vec V, float a) {
  float c=cos(a), s=sin(a); 
  return(new vec(V.x*c-V.y*s, V.x*s+V.y*c));
};                                     // V rotated by a radians

vec S(float s, vec V) {
  return new vec(s*V.x, s*V.y);
};                                                  // sV

vec M(vec V) { 
  return fromFloats(-V.x, -V.y);
}                                                                  // -V

int n = 40;                                   // size of grid. Must be >2!
pt[][] G = new pt[n][n];                  // array of vertices

boolean showVertices=false, showEdges=false, showTexture=true;  // flags for rendering vertices and edges
float w, h, ww, hh;                                  // width, height of cell in absolute and normalized units

void allocVertices() { 
  for (int i=0; i<n; i++) {
    for (int j=0; j<n; j++) {
      G[i][j] = makePt(i*w, j*h);
    }
  }
} 

// builds the grid
// optimized to reuse vertices rather than allocate new ones each time
void resetVertices() {
  for (int i=0; i<n; i++) {
    for (int j=0; j<n; j++) {
      G[i][j].setTo(i*w, j*h);
    }
  }
}

// optimized to directly warp the points rather than allocate new ones
void warpVertices1(pair L, float f, float roi) {
  L.prepareToWarp(roi); // precompute some values that are the same for each call to warpDirectly
  for (int i=0; i<n; i++) {
    for (int j=0; j<n; j++) {
      L.warpDirectly(G[i][j], f);
    }
  }
}

void warpVertices2(pair L, pair R, float f) { 
  for (int i=0; i<n; i++) {
    for (int j=0; j<n; j++) {
      G[i][j] = warp2(L, R, f, G[i][j]);
    }
  }
}


void paintImage(PImage myImage) {
  noStroke(); 
  noFill(); 
  textureMode(NORMAL);       // texture parameters in [0,1]x[0,1]
  for (int i=0; i<n-1; i++) {
    beginShape(QUAD_STRIP); 
    texture(myImage); 
    for (int j=0; j<n; j++) { 
      vertex(G[i][j].x, G[i][j].y, i*ww, j*hh); 
      vertex(G[i+1][j].x, G[i+1][j].y, (i+1)*ww, j*hh);
    }
    endShape();
  }
}

void drawEdges() {
  stroke(black);
  noFill(); 
  strokeWeight(2);
  pushMatrix();
  translate(offsetW, offsetH);
  for (int i=0; i<n-1; i++) {
    beginShape(QUAD_STRIP);  
    for (int j=0; j<n; j++) { 
      vertex(G[i][j].x, G[i][j].y); 
      vertex(G[i+1][j].x, G[i+1][j].y);
    }
    endShape();
  }
  popMatrix();
}

void drawVertices() {
  noStroke(); 
  fill(red); 
  for (int i=0; i<n; i++) {
    for (int j=0; j<n; j++) {
      showWithRadius(G[i][j], 1);
    }
  }
}


class vec { float x=0,y=0,z; 
 // CREATE
  vec () {};
  
  vec (float px, float py) {x = px; y = py;};
  //vec (float px, float py, float pz) {x = px; y = py; z = pz;};
  //void setTo (float px, float py, float pz) {x = px; y = py; z = pz;}; 
 // MODIY
  vec setTlots(float px, float py) {x = px; y = py; return this;}; 
  
  vec setTo(vec V) {x = V.x; y = V.y; return this;}; 
  
  vec zero() {x=0; y=0; return this;}
  
  vec scalets(float u, float v) {x*=u; y*=v; return this;};
  
  vec scaleBy(float f) {x*=f; y*=f; return this;};
  
  vec reverse() {x=-x; y=-y; return this;};
  
  vec divideBy(float f) {x/=f; y/=f; return this;};
  
  vec normalize() {float n=sqrt(sq(x)+sq(y)); if (n>0.000001) {x/=n; y/=n;}; return this;};
  
  vec ats(float u, float v) {x += u; y += v; return this;};
  
  vec add(vec V) {x += V.x; y += V.y; return this;};   
  
  vec aec(float s, vec V) {x += s*V.x; y += s*V.y; return this;};   
  
  vec rotateBy(float a) {float xx=x, yy=y; x=xx*cos(a)-yy*sin(a); y=xx*sin(a)+yy*cos(a); return this;};
  
  vec left() {float m=x; x=-y; y=m; return this;};
  
  void mul(float m) {x *= m; y *= m; z *= m;};
  
  void addScaled(float m, vec V) {x += m*V.x; y += m*V.y; z += m*V.z;};
  
  vec make() {return(new vec(x,y));};
  
  void show (pt P) {line(P.x,P.y, P.z,P.x+x,P.y+y,P.z+z); }; 
  
  void sub(vec V) {x -= V.x; y -= V.y; z -= V.z;};
  
  void div(float m) {x /= m; y /= m; z /= m;};
  
  void makeUnit() {float n=this.norm(); if (n>0.0001) {this.div(n);};};
  
  void back() {x= -x; y= -y; z= -z;};
//  boolean coplanar (vec V, vec W) {return(abs(mixed(this,V,W))<0.0001);};
//  boolean cw (vec U, vec V, vec W) {return(mixed(this,V,W)>0.0001);};
  
  public String toString(){
    return "("+x+","+y+" ,"+z+")";
  }  
  // OUTPUT VEC
  vec clone() {return(new vec(x,y));}; 

  // OUTPUT TEST MEASURE
  float norm() {return(sqrt(sq(x)+sq(y)));}
  
  boolean isNull() {return((abs(x)+abs(y)<0.000001));}
  
  float angle() {return(atan2(y,x)); }

  // DRAW, PRINT
  void write() {println("<"+x+","+y+">");};
  
  void showAt (pt P) {line(P.x,P.y,P.x+x,P.y+y); }; 
  
  void showArrowAt (pt P) {line(P.x,P.y,P.x+x,P.y+y); 
      float n=min(this.norm()/10.,height/50.); 
      pt Q=addVec(P,this); 
      vec U = S(-n,U(this));
      vec W = S(.3,rotate90(U)); 
      beginShape(); 
        Q.add4(U).add4(W).v(); 
        Q.v(); 
        Q.add4(U).add4(M(W)).v(); 
      endShape(CLOSE); 
    }; 
  //void label(String s, pt P) {P(P).add(0.5,this).add(3,R(U(this))).label(s); };
  } // end vec class
  

