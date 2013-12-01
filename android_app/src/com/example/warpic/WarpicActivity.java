package com.example.warpic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class WarpicActivity extends PApplet { // PApplet in fact extends
												// android.app.Activity

	/****************************** INSTANCE VARIABLES ********************************/
	static String motionString;
	int n = 50; // size of grid. Must be >2!
	Pair initL, initR;
	Pt[][] G = new Pt[n][n]; // array of vertices
	boolean showVertices = false, showEdges = true, showTexture = false; // flags
																		// for
																		// rendering
																		// vertices
																		// and
																		// edges
	float w, h, ww, hh; // width, height of cell in absolute and normalized
						// units
	MultiTouchController mController;
	ArrayList<MyMotionEvent> l;
	String imageFilePath;
	boolean showSpirals, showFingerPaths;
	PImage myImage;// Image to be warped
	Menu menu;
	static boolean showController, fingersOnScreen, animate, firstPass,
			saveWarp;
	ArrayList<Pt> A, B, C, D;// Declare finger movement list
	Pair L, R; // Declare warping pairs
	float roi, t, f;
	static int tracking;
	public static boolean grabPoints, writePaths;
	SurfaceView mySurfaceView;
	File motionFile, gifFile;;
	int counter;
	String displayInfo;
	boolean firstAnimation = true;
	boolean showMenu;
	private boolean createNewController;
	private boolean firstFrame;
	private double firstFrameT;
	static Button myButton;
	public static boolean drawButton;
	public static boolean realTimeWarp;
	ParseObject testObject;
	ParseFile imageParseFile;
	StringBuilder motionStrBuilder;

	MotionPath smile;
	private boolean cannedWarpEdit = false;

	/****************************** END OF INSTANCE VARIABLES ****************************/

	public void setup() {

		Parse.initialize(this, "BgpuXDtTXuLyXOyv5A6sEs6ehKqcaXakOC7Mg13N",
				"A8pFBNRcLjGrhclpFFvJFNXsMaZyOfZypjiKlPjD");
		testObject = new ParseObject("TestObject");
		motionStrBuilder = new StringBuilder();
		motionString = "";
		motionFile = new File(Environment.getExternalStorageDirectory(),
				"WARPIC/motion.txt");
		motionFile.delete();
		motionFile = new File(Environment.getExternalStorageDirectory(),
				"WARPIC/motion.txt");
		Intent intent = getIntent();
		// TODO: UNCOMMENT THIS
		imageFilePath = intent.getStringExtra("filePath");
		myImage = goGetImage(imageFilePath);
		imageMode(PORTRAIT);
		fingersOnScreen = false;
		firstPass = true;
		animate = false;
		saveWarp = false;
		grabPoints = false;
		// /*************Adding history of points lists
		A = new ArrayList<Pt>();
		B = new ArrayList<Pt>();
		C = new ArrayList<Pt>();
		D = new ArrayList<Pt>();
		// ******************************
		showMenu = false;
		tracking = 0;
		counter = 0;
		showSpirals = false;
		ww = (float) (1.0 / (n - 1));
		hh = (float) (1.0 / (n - 1)); // set intial width and height of a cell
		w = displayWidth * ww;
		h = displayHeight * hh; // set intial width and height of a cell in
								// normalized [0,1]x[0,1]
		menu = new Menu(this);

		l = new ArrayList<MyMotionEvent>();
		showController = true;
		fingersOnScreen = false;
		writePaths = true;
		displayInfo = "display_width\n" + displayWidth + "\ndisplay_height"
				+ "\n" + displayHeight;
		createNewController = false;
		firstFrame = true;
		realTimeWarp = false;
		allocVertices(); // ML: alloc all grid points before starting animation
		smile = new MotionPath("smile");
		smile.initSmile();
		mController = new MultiTouchController(menu, smile);
	}

	public void draw() {
		background(255);
		synchronized (l) {// handle the previously queued motion events
			if (!showMenu && !cannedWarpEdit) {
				for (MyMotionEvent me : l) {
					mController.handle(me);
				}
			} else if (cannedWarpEdit && !showMenu) {
				for (MyMotionEvent me : l) {
					mController.handleCannedWarpEdit(me);
				}
			} else {
				for (MyMotionEvent me : l)
					// Register the button input
					mController.handleButton(me);
			}
			l.clear(); // clear the array list
		}// end of Synchronized
		if (createNewController) {
			mController = new MultiTouchController(menu);
			createNewController = false;
		}

		if (cannedWarpEdit) {

			mController.scale(smile);
		}

		stroke(0, 0, 255);
		show(smile.currentTouch);
		stroke(0, 255, 0);
		show(smile.prevTouch);

		fill(0);
		textSize(50);
		// text("totalTime: " + mController.getElapsedTime(), 100, 100);
		// text("fingersOnScreen: "+fingersOnScreen,100,150);
		if (fingersOnScreen)
			mController.updateHistory();// Record the position once per frame
		if (grabPoints) {
			L.writePairsToFile(motionFile);
			R.writePairsToFile(motionFile);
			motionString += L.addToString();
			motionString += R.addToString();
			grabPoints = false;

		}
		if (!saveWarp)
			resetVertices();
		if (fingersOnScreen || realTimeWarp) {// The user has put their fingers
												// on the screen to begin
												// warping
			realTimeWarp();

		}// end of fingers on screen

		if (animate) {
			if (firstFrame) {
				firstFrameT = System.nanoTime() / 1000000000.0;
				firstFrame = false;
			}
			if (writePaths) {
				writePathsFile(motionFile, makePtsString());
				writePathsFile(motionFile, displayInfo);
				// motionString+=makePtsString();
				// motionString+=displayInfo;
				writePaths = false;
			}
			animateWarping();
		}

		if (showEdges)
			drawEdges();

		if (showTexture)
			paintImage(myImage);

		if (showSpirals) {
			try {
				R.showAll(this);
				L.showAll(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (saveWarp) {
			saveWarp();
			saveWarp = false;
		}

		try {
			if (mController != null && mController.size() == 4
					&& showController) {
				mController.showEdges(this);
				mController.show(this);
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		if (showFingerPaths) {
			if (mController.size() > 0)
				mController.showHistory(this);
			drawFingerPaths();
		}

		if (showMenu)
			menu.draw(this);

		// this.stroke(255,0,0);
		// smile.showPaths(this);
		//
	}// End of draw

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private PImage goGetImage(String string) {
		Bitmap bMap = null;
		PImage img = null;
		try {
			bMap = BitmapFactory.decodeFile(string); // Get the jpeg image as a
														// Bitmap from the
														// filesystem
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			img = createImage(bMap.getWidth(), bMap.getHeight(), RGB);
			bMap.getPixels(img.pixels, 0, img.width, 0, 0, img.width,
					img.height);// Store the bMap's pixels into a pixel array
			img.updatePixels();
			myImage = rotateCameraImage(myImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	public String sketchRenderer() {
		return OPENGL;
	}

	private void animateWarping() {
		int numFrames = max(max(A.size(), B.size()), max(C.size(), D.size()));
		double currentT = (System.nanoTime() / 1000000000.0) - firstFrameT;
		tracking = computeAnimationFrame(currentT,
				mController.getElapsedTime(), numFrames);

		textSize(50);
		fill(0);
		animateUpdate1(tracking);// Advance the current pairs along the user's
									// path
		roi = d(L.ctr(this), R.ctr(this)); // Find the region of influence for
											// the warping
		L.evaluate(f, this);
		R.evaluate(f, this);
		warpVertices(L, f, roi);// Warp the vertices
		warpVertices(R, f, roi);
		// tracking++;
		f = 1;

		if (tracking > numFrames) {
			tracking = 0;
			firstFrame = true;
		}
	}

	private void realTimeWarp() {
		try {
			setPairsDev();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			animateUpdate1(tracking);
		} catch (Exception e) {
			e.printStackTrace();
		}
		roi = d(L.ctr(this), R.ctr(this)); // Find the region of influence for
											// the warping
		L.evaluate(1, this);
		R.evaluate(1, this);
		warpVertices(L, 1, roi);// Warp the vertices
		warpVertices(R, 1, roi);
	}

	private void animateUpdate1(int index) {
		if (A.size() > index) {
			L.A1 = A.get(index);
		}
		if (B.size() > index)
			L.B1 = B.get(index);
		if (C.size() > index)
			R.A1 = C.get(index);
		if (D.size() > index)
			R.B1 = D.get(index);
	}

	private void setPairsDev() {
		if (mController.getHistoryOf(0).size() > 0
				&& mController.getHistoryOf(1).size() > 0
				&& mController.getHistoryOf(2).size() > 0
				&& mController.getHistoryOf(3).size() > 0) {
			ArrayList<Pt> listA, listB, listC, listD;
			listA = mController.getHistoryOf(0);
			listB = mController.getHistoryOf(1);
			listC = mController.getHistoryOf(2);
			listD = mController.getHistoryOf(3);
			L = new Pair(listA.get(0), listB.get(0),
					listA.get(listA.size() - 1), listB.get(listB.size() - 1));
			R = new Pair(listC.get(0), listD.get(0),
					listC.get(listC.size() - 1), listD.get(listD.size() - 1));
		}
	}

	/********************************************************************************************/
	// Override android touch events
	/*******************************************************************************************/
	public boolean surfaceTouchEvent(MotionEvent me) {// Overwrite this android
														// touch method to
														// process touch data
		int action = whichAction(me);
		int finger = whichFinger(me);
		int pointerCount = me.getPointerCount();
		Pt loc = null;
		if (action == 1 && finger < pointerCount) {
			float px = me.getX(finger);
			float py = me.getY(finger);
			loc = new Pt(px, py);
			synchronized (l) {
				l.add(new MyMotionEvent(action, finger, loc, pointerCount,
						System.nanoTime())); // queue all the data recieved from
												// the motion events
			}
		} else if (action == 0) {
			if (pointerCount == 1) {
				if (fingersOnScreen && firstPass)
					createNewController = true;
			}
			synchronized (l) {
				l.add(new MyMotionEvent(action, finger, loc, pointerCount,
						System.nanoTime())); // queue all the data recieved from
												// the motion events
			}
		} else if (action == 2) {
			synchronized (l) {
				for (int i = 0; i < me.getPointerCount(); i++) {
					float px = me.getX(i);
					float py = me.getY(i);
					loc = new Pt(px, py);
					l.add(new MyMotionEvent(action, me.getPointerId(i), loc,
							pointerCount, System.nanoTime())); // queue all the
																// data recieved
																// from the
																// motion events
				}
			}
		}
		return super.surfaceTouchEvent(me);
	}

	void saveWarp() {// Bakes the warped image as a PImage
		noLoop();
		myImage = get(0, 0, displayWidth, displayHeight);
		loop();
	}

	void getReadyToAnimate() {
		A = mController.getHistoryOf(0);
		B = mController.getHistoryOf(1);
		C = mController.getHistoryOf(2);
		D = mController.getHistoryOf(3);
	}

	int whichAction(MotionEvent me) { // 1=press, 0=release, 2=drag
		int action = me.getAction();
		int aaction = action & MotionEvent.ACTION_MASK;
		int what = 0;
		if (aaction == MotionEvent.ACTION_POINTER_UP
				|| aaction == MotionEvent.ACTION_UP)
			what = 0;
		if (aaction == MotionEvent.ACTION_DOWN
				|| aaction == MotionEvent.ACTION_POINTER_DOWN)
			what = 1;
		if (aaction == MotionEvent.ACTION_MOVE)
			what = 2;
		return what;
	}

	int whichFinger(MotionEvent me) {// gets the index of the motion Event
		int pointerIndex = (me.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		int pointerId = me.getPointerId(pointerIndex);
		return pointerId;
	}

	/****** End of Surface Touch Event override */
	/********************** MENU BUTTON OVERRIDE **********/
	public void keyPressed() {// Display menu
		if (key == CODED) {
			if (keyCode == KeyEvent.KEYCODE_MENU) {// When the menu button is
													// pressed the animation
													// begins
				//showController = false;
				showMenu = !showMenu;
			}
		}
	}

	/****************** End of Menu Button OVERRIDE **********/

	PImage rotateCameraImage(PImage img) {
		PImage result = createImage(img.height, img.width, RGB);
		for (int y = 0; y < result.height; y++) {
			for (int x = 0; x < result.width; x++) {
				result.set(x, y, img.get(y, result.width - x));
			}
		}
		return result;
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
				G[i][j] = spirals(LA0, LB0, LA1, LB1, RA0, RB0, RA1, RB1, f,
						P(i * w, j * h));
	}

	void warpVertices(Pair L, float f, float roi) {
		L.prepareToWarp(roi, this); // precompute some values that are the same
									// for each call to warpDirectly
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				// G[i][j] = L.warp(G[i][j],f,roi,this);
				L.warpDirectly(G[i][j], f, this);
	}

	void warpVertices(Pair L, Pair R, float f) {
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				G[i][j] = warp(L, R, f, G[i][j]);
	}

	void allocVertices() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				G[i][j] = P(i * w, j * h);
			}
		}
	}

	void paintImage(PImage myImage) {
		noStroke();
		noFill();
		textureMode(NORMAL); // texture parameters in [0,1]x[0,1]
		// beginShape(QUADS);
		for (int i = 0; i < n - 1; i++) {
			beginShape(QUAD_STRIP);
			texture(myImage);
			for (int j = 0; j < n; j++) {
				vertex(G[i][j].x, G[i][j].y, i * ww, j * hh);
				vertex(G[i + 1][j].x, G[i + 1][j].y, (i + 1) * ww, j * hh);
			}
			endShape();
		}
	}

	void drawEdges() {
		stroke(10, 10, 10);
		noFill();
		strokeWeight(2);
		// beginShape(QUADS);
		for (int i = 0; i < n - 1; i++) {
			beginShape(QUAD_STRIP);
			for (int j = 0; j < n; j++) {
				vertex(G[i][j].x, G[i][j].y);
				vertex(G[i + 1][j].x, G[i + 1][j].y);
			}
			;
			endShape();
		}
		;
	}

	void drawVertices() {
		noStroke();
		fill(255, 0, 0);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				show(G[i][j], 1);
	}

	Pt warp(Pair LPair, Pair R, float f, Pt Q0) {
		Pt QLt = LPair.warp(Q0, f, this);
		Pt QRt = R.warp(Q0, f, this);
		float dL = d(Q0, LPair.ctr(this)), dR = d(Q0, R.ctr(this));
		float roi = d(LPair.ctr(this), R.ctr(this));
		float a = dL / (dL + dR);
		float cL = sq(cos(a * PI / 2)), cR = sq(sin(a * PI / 2));
		return P(cL, QLt, cR, QRt);
	}

	/************************************* END OF TEXTURE **************************/
	/*****************************************************************************/

	/******************************** Spiral Math *********************************/
	Pt spirals(Pt LA0, Pt LB0, Pt LA1, Pt LB1, Pt RA0, Pt RB0, Pt RA1, Pt RB1,
			float f, Pt Q0) {
		float dL = d(Q0, average(LA0, LB0)), dR = d(Q0, average(RA0, RB0));
		float roi = d(average(LA0, LB0), average(RA0, RB0));
		float cL = sq(cos(dL / roi * PI / 2)), cR = sq(cos(dR / roi * PI / 2));
		if (dL > roi)
			cL = 0;
		if (dR > roi)
			cR = 0;
		Pt QLt = spiral(LA0, LB0, LA1, LB1, f * cL, Q0);
		Pt QRt = spiral(RA0, RB0, RA1, RB1, f * cR, Q0);
		return P(P(Q0, 1, V(Q0, QLt)), 1, V(Q0, QRt));
	}

	Pt spiral(Pt A, Pt B, Pt C, Pt D, float t, Pt Q) {
		float a = spiralAngle(A, B, C, D);
		float s = spiralScale(A, B, C, D);
		Pt G = spiralCenter(a, s, A, C);
		return L(G, R(Q, t * a, G), pow(s, t));
	}

	float spiralAngle(Pt A, Pt B, Pt C, Pt D) {
		return angle(V(A, B), V(C, D));
	}

	float spiralScale(Pt A, Pt B, Pt C, Pt D) {
		return d(C, D) / d(A, B);
	}

	Pt spiralCenter(float a, float z, Pt A, Pt C) {
		float c = cos(a), s = sin(a);
		float D = sq(c * z - 1) + sq(s * z);
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
		float dx = Q.x, dy = Q.y, c = cos(a), s = sin(a);
		return new Pt(c * dx + s * dy, -s * dx + c * dy);
	}; // Q rotated by angle a around the origin

	Pt R(Pt Q, float a, Pt C) {
		float dx = Q.x - C.x, dy = Q.y - C.y, c = cos(a), s = sin(a);
		return P(C.x + c * dx - s * dy, C.y + s * dx + c * dy);
	}; // Q rotated by angle a around point P

	Pt MoveByDistanceTowards(Pt P, float d, Pt Q) {
		return P(P, d, U(V(P, Q)));
	}; // P+dU(PQ) (transLAted P by *distance* s towards Q)!!!

	// average
	Pt average(Pt A, Pt B) { // (A+B)/2 (average)
		return P((A.x + B.x) / 2.0, (A.y + B.y) / 2.0);
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

	Pt P(Pt P, Vec V) {
		return P(P.x + V.x, P.y + V.y);
	} // P+V (P transalted by vector V)

	Pt P(Pt P, float s, Vec V) {
		return P(P, W(s, V));
	} // P+sV (P transalted by sV)

	Pt P(float x, float y) {
		return new Pt(x, y);
	};

	Pt P(double x, double y) {
		return new Pt((float) x, (float) y);
	};

	Pt P(Pt A, float s, Pt B) {
		return P(A.x + s * (B.x - A.x), A.y + s * (B.y - A.y));
	};// ,A.z+s*(B.z-A.z)); }; // A+sAB

	// measure
	boolean isSame(Pt A, Pt B) {
		return (A.x == B.x) && (A.y == B.y);
	} // A==B

	boolean isSame(Pt A, Pt B, float e) {
		return ((abs(A.x - B.x) < e) && (abs(A.y - B.y) < e));
	} // ||A-B||<e

	float d(Pt P, Pt Q) {
		return sqrt(d2(P, Q));
	}; // ||AB|| (Distance)

	float d2(Pt P, Pt Q) {
		return sq(Q.x - P.x) + sq(Q.y - P.y);
	}; // AB*AB (Distance squared)

	Vec V(Vec V) {
		return new Vec(V.x, V.y);
	}; // make copy of vector V

	Vec V(Pt P) {
		return new Vec(P.x, P.y);
	}; // make Vector from origin to P

	Vec V(float x, float y) {
		return new Vec(x, y);
	}; // make Vector (x,y)

	Vec V(Pt P, Pt Q) {
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

	Vec MouseDrag() {
		return new Vec(mouseX - pmouseX, mouseY - pmouseY);
	}; // vector representing recent mouse displacement

	// Interpolation
	Vec L(Vec U, Vec V, float s) {
		return new Vec(U.x + s * (V.x - U.x), U.y + s * (V.y - U.y));
	}; // (1-s)U+sV (Linear interpolation between vectors)

	Vec S(Vec U, Vec V, float s) {
		float a = angle(U, V);
		Vec W = R(U, s * a);
		float u = n(U);
		float v = n(V);
		W(pow(v / u, s), W);
		return W;
	} // steady interpolation from U to V

	Pt L(Pt A, Pt B, float t) {
		return P(A.x + t * (B.x - A.x), A.y + t * (B.y - A.y));
	}

	// measure

	float det(Vec U, Vec V) {
		return dot(R(U), V);
	} // det | U V | = scalar cross UxV

	float n(Vec V) {
		return sqrt(dot(V, V));
	}; // n(V): ||V|| (norm: length of V)

	float n2(Vec V) {
		return sq(V.x) + sq(V.y);
	}; // n2(V): V*V (norm squared)

	boolean parallel(Vec U, Vec V) {
		return dot(U, R(V)) == 0;
	};

	float angle(Vec U, Vec V) {
		return atan2(det(U, V), dot(U, V));
	}; // angle <U,V> (between -PI and PI)

	float angle(Vec V) {
		return (atan2(V.y, V.x));
	}; // angle between <1,0> and V (between -PI and PI)

	float angle(Pt A, Pt B, Pt C) {
		return angle(V(B, A), V(B, C));
	} // angle <BA,BC>

	float turnAngle(Pt A, Pt B, Pt C) {
		return angle(V(A, B), V(B, C));
	} // angle <AB,BC> (positive when right turn as seen on screen)

	float toRad(float a) {
		return (a * PI / 180);
	} // convert degrees to radians

	float positive(float a) {
		if (a < 0)
			return a + TWO_PI;
		else
			return a;
	} // adds 2PI to make angle positive

	Vec W(float s, Vec V) {
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

	Pt P(float a, Pt A, float b, Pt B) {
		return P(a * A.x + b * B.x, a * A.y + b * B.y);
	} // aA+bB, (a+b=1)

	Pt P(float a, Pt A, float b, Pt B, float c, Pt C) {
		return P(a * A.x + b * B.x + c * C.x, a * A.y + b * B.y + c * C.y);
	} // aA+bB+cC

	Pt P(float a, Pt A, float b, Pt B, float c, Pt C, float d, Pt D) {
		return P(a * A.x + b * B.x + c * C.x + d * D.x, a * A.y + b * B.y + c
				* C.y + d * D.y);
	} // aA+bB+cC+dD

	float dot(Vec U, Vec V) {
		return (U.x * V.x + U.y * V.y + U.z * V.z);
	};

	Vec R(Vec V) {
		return new Vec(-V.y, V.x);
	}; // V turned right 90 degrees (as seen on screen)

	Vec R(Vec V, float a) {
		float c = cos(a), s = sin(a);
		return (new Vec(V.x * c - V.y * s, V.x * s + V.y * c));
	}; // V rotated by a radians

	Vec S(float s, Vec V) {
		return new Vec(s * V.x, s * V.y);
	}; // sV

	Vec M(Vec V) {
		return V(-V.x, -V.y);
	}

	// ML: just like L1, but updates A
	void lerpTo(Pt A, Pt B, float t) {
		A.x += t * (B.x - A.x);
		A.y += t * (B.y - A.y);
	}

	void rotateAround(Pt Q, float angle, Pt center) {
		float dx = Q.x - center.x, dy = Q.y - center.y;
		float c = cos(angle), s = sin(angle);
		Q.x = center.x + c * dx - s * dy;
		Q.y = center.y + s * dx + c * dy;
	}

	/*
	 * ******************************************End of Math
	 * Utilities***************************************************
	 */
	/*******************************************************************************************************************/

	/********************************************* Drawing helper functions ************************************************/

	void show(Pt P, float r) {
		ellipse(P.x, P.y, 2 * r, 2 * r);
	}; // draws circle of center r around P

	void show(Pt P) {
		ellipse(P.x, P.y, 6, 6);
	}; // draws small circle around point

	void edge(Pt P, Pt Q) {
		line(P.x, P.y, Q.x, Q.y);
	}; // draws edge (P,Q)

	void drawFingerPaths() {
		drawArrayList(A);
		drawArrayList(B);
		drawArrayList(C);
		drawArrayList(D);
	}

	void drawArrayList(ArrayList<Pt> list) {
		for (Pt p : list) {
			fill(255, 0, 0);
			show(p);
		}
	}

	/********************************************* End of Drawing helper functions ************************************************/

	/********************************************* Smoothing ***************************/
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
			S[v] = P(list.get(v), s, average(list.get(v - 1), list.get(v + 1))); // S
																					// =
																					// G
																					// +
																					// s((P+N)/2-G)
		for (int v = 1; v < list.size() - 1; v++)
			list.set(v, S[v]); // copy back
	}

	/********************************************* End of Smoothing ***************************/
	void launchShareOption() {
		// TODO: Figure out why the sharing intent crashes
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("image/*");
		// For a file in shared storage. For data in private storage, use a
		// ContentProvider.
		File imageFile = new File("//sdcard//WARPIC/firstWarp.png");
		Uri uri = Uri.fromFile(imageFile);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		startActivity(Intent.createChooser(shareIntent,
				"How do you want to share"));

	}

	void launchShareMultiple() {

		Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("image/*");
		// For a file in shared storage. For data in private storage, use a
		// ContentProvider.
		File imageFile = new File(imageFilePath);
		Uri uri = Uri.fromFile(imageFile);
		ArrayList<Uri> uris = new ArrayList<Uri>();
		uris.add(uri); // Add your image URIs here
		// TODO: FIX THIS MOTIONTEXT FILE NOT SENDING
		File motionFile = new File("//sdcoard//WARPIC/motion.txt");
		Uri uri1 = Uri.fromFile(motionFile);
		uris.add(uri1);
		// END OF TODO
		shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		startActivity(Intent.createChooser(shareIntent,
				"How do you want to share"));
	}

	public void writePathsFile(File file, String data) {
		FileWriter fWriter;
		try {
			fWriter = new FileWriter(file, true);
			fWriter.write(data);
			fWriter.flush();
			fWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String makePtsString() {
		String ret = "";
		System.out.println("A.size()" + A.size());
		for (Pt p : A) {
			ret += p.x + "," + p.y + "\n";
		}
		ret += "-\n";
		System.out.println("B.size()" + B.size());
		for (Pt p : B) {
			ret += p.x + "," + p.y + "\n";
		}
		ret += "-\n";
		System.out.println("C.size()" + C.size());
		for (Pt p : C) {
			ret += p.x + "," + p.y + "\n";
		}
		ret += "-\n";
		System.out.println("D.size()" + D.size());
		for (Pt p : D) {
			ret += p.x + "," + p.y + "\n";
		}
		ret += "-\n";
		return ret;
	}

	// image naming and path to include sd card appending name you choose for
	// file
	public void timeToAnimate() {
		getReadyToAnimate();
		smooth(A);
		smooth(B);
		smooth(C);
		smooth(D);
		animate = true;
		showController = false;
		showSpirals = false;
		showMenu = false;
		realTimeWarp = false;
	}

	public int computeAnimationFrame(double currentT, double totalAnimationT,
			int numFrames) {
		return (int) ((currentT / totalAnimationT) * numFrames);
	}

	public void addAnimationString() {
		motionString += makePtsString();
		motionString += displayInfo;
		motionString += "\nellapsedTime\n" + mController.getElapsedTime();
	}

	public void sendAnimation() {
		System.out.println("motionString: " + motionString);
		testObject.put("brain", motionString);
		motionString = "";
		imageParseFile = new ParseFile("image_file", parseImageFile());
		imageParseFile.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (e == null) {
					System.out.println("save successful of image file!!");
					testObject.put("imageFile", imageParseFile);
					testObject.saveInBackground(new SaveCallback() {
						public void done(ParseException e) {
							if (e == null) {
								String urlID = testObject.getObjectId();
								String url = "http://ec2-54-237-34-141.compute-1.amazonaws.com/?"
										+ urlID;
								launchShareUrl(url);
								System.out.println("url: " + url);

							} else {
								System.out.println("Unable to save data");
							}
						}

						private void launchShareUrl(String url) {
							Intent shareIntent = new Intent(Intent.ACTION_SEND);
							shareIntent
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
							shareIntent.setType("text/*");
							shareIntent.putExtra(Intent.EXTRA_TEXT, url);
							shareIntent.setType("text/plain");
							startActivity(Intent.createChooser(shareIntent,
									"Share your Animation!!"));
						}
					});

				} else {
					System.out.println("Unable to save image file data");
				}
			}
		});

	}

	private byte[] parseImageFile() {
		Bitmap bMap;
		bMap = BitmapFactory.decodeFile(imageFilePath); // Get the jpeg image as
														// a Bitmap from the
														// filesystem
		System.out.println("bMap byte Count: " + bMap.getByteCount());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		System.out.println("byteArray length: " + byteArray.length);
		return byteArray;

	}

	public void launchMotionGallery() {
		Intent intent = new Intent(this, Gallery_Activity.class);
		startActivity(intent);
	}
}