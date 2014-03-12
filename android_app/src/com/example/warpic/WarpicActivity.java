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
//import processing.event.KeyEvent;
//import processing.event.KeyEvent;

public class WarpicActivity extends PApplet { // PApplet in fact extends
												// android.app.Activity

	/****************************** INSTANCE VARIABLES ********************************/
	String motionString;
	
		
	boolean showVertices = false, showEdges = true, showTexture = false; // flags for rendering vertices and edges
	static boolean showWarp = false;

	static MultiTouchController mController;
	ArrayList<MyMotionEvent> l;
	String imageFilePath;
	boolean showSpirals, showFingerPaths;
	PImage myImage;// Image to be warped
	static Menu menu;
	float bigR;
	static boolean showController, fingersOnScreen, animate, firstPass,
			saveWarp, showPrimeSpirals, first_load_canned_warp;
	//Pair L, R; // Declare warping pairs
	float roi, t, f,littleR;
	
	public static boolean grabPoints, writePaths;
	SurfaceView mySurfaceView;
	File motionFile, gifFile;;
	int counter;
	String displayInfo;
	boolean firstAnimation = true;
	boolean showMenu;
	public static boolean createNewController;
	private boolean firstFrame;
	private double firstFrameT;
	public static boolean drawButton;
	public static boolean realTimeWarp;
	ParseObject testObject;
	ParseFile imageParseFile;
	StringBuilder motionStrBuilder;
	static double ellapsed_time;	
	static MotionPath effects_path;
	static boolean editWarp;
	public static boolean compute_bary;
	public static boolean regrab;
	public static boolean warp_selected=true;
	public ArrayList<Weight> weights_a, weights_b, weights_c, weights_d, weights_bounding_box;
	public static boolean regrab_touched;
	public boolean draw_finger_paths,edit_ratio;
	private Pt ctr_of_roi;
	static public boolean regrab_save;
	public static MotionPath reset_to_motion_path;
	public EditRatioSlider ratio_slider;
	Texture texture;
	
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
		imageFilePath = intent.getStringExtra("filePath");
		myImage = goGetImage(imageFilePath);
		imageMode(PORTRAIT);
		fingersOnScreen = false;
		firstPass = true;
		animate = false;
		saveWarp = false;
		grabPoints = false;
		editWarp = false;
		
		texture = new Texture(displayWidth, displayHeight, this);
		
		// *****************Weight lists for Barycentric coords
		weights_a = new ArrayList<Weight>();
		weights_b = new ArrayList<Weight>();
		weights_c = new ArrayList<Weight>();
		weights_d = new ArrayList<Weight>();
		weights_bounding_box = new ArrayList<Weight>();
		// *****************

		showMenu = false;
		
		counter = 0;
		showSpirals = true;
		
		
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
		//******Global Variables that could use some refactoring
		effects_path = new MotionPath("smile");
		ratio_slider = new EditRatioSlider(this, new Pt(displayWidth-40,40), displayHeight-80);
		mController = new MultiTouchController(menu, effects_path, ratio_slider, this);
		regrab = false;
		showPrimeSpirals = false;
		edit_ratio = false;
		regrab_touched= false;
		draw_finger_paths=false;
		compute_bary=false;
		reset_to_motion_path= new MotionPath("RESET");
		ctr_of_roi= new Pt();
	
	}

	public void draw() {

		Pair L = new Pair();
		Pair R = new Pair();
		background(255);
		synchronized (l) {// handle the previously queued motion events
			if (showMenu) {
				for (MyMotionEvent me : l) {
					// Register the button input
					mController.handleButton(me);
				}
			} else if (editWarp) {
				for (MyMotionEvent me : l) {
					mController.handle_triangle(me);
				}
			} 
			else {
				for (MyMotionEvent me : l) {
					mController.handle(me);
				}
			}
			l.clear(); // clear the array list
		}// end of Synchronized

		if (createNewController) { createNewController();}
		if(compute_bary){ computeBarys();}
		if(first_load_canned_warp){
			//TODO: Doesn't work
			L.A0.setTo(effects_path.A.get(0));
			L.B0.setTo(effects_path.B.get(0));
			R.A0.setTo(effects_path.C.get(0));
			R.B0.setTo(effects_path.D.get(0));
			first_load_canned_warp=false;
		}
		
		if (fingersOnScreen&&!showMenu){
			mController.updateHistory();// Record the position once per frame
			ratio_slider.updateHistory(); //update the current history list
			if(!editWarp){	
				
			}
			else{
				//TODO: the user must apply displacement for the canned warp to take effect
				editWarp(L,R,mController);
			}
		}
		
		if (grabPoints) { grabPoints(L,R); }

		if (!saveWarp) texture.resetVertices();
		
		if(!editWarp){
			if (fingersOnScreen || realTimeWarp) {// The user has put their fingers on the screen to begin warping
				noFill();
				//Set L and R variables
				int index = Math.min(mController.getHistoryOf(0).size(), 
						mController.getHistoryOf(1).size());
				index--;
				L =  L.setPair(mController.getHistoryOf(0),mController.getHistoryOf(1),index);
				index = Math.min(mController.getHistoryOf(2).size(), 
							mController.getHistoryOf(3).size());
				index--;
				R = R.setPair(mController.getHistoryOf(2),mController.getHistoryOf(3),index);
				setPairs(mController);
				realTimeWarp(texture,L,R);
				
			}
		}
		if (animate) { 
			//Find the current animation time
			if (firstFrame) {
				firstFrameT = System.nanoTime() / 1000000000.0;			
				firstFrame = false;
			}
			//If it's time to save the motionPath and get ready to send it to the website
			if (writePaths) {
				writePathsFile(motionFile, makePtsString());
				writePathsFile(motionFile, displayInfo);
				writePaths = false;
			}
			//Find the current frame to advance the pairs to
			effects_path.setTo(mController);
			double ellapsed_time = mController.getElapsedTime();
			int currFrame= findCurrentFrame(effects_path.A, effects_path.B, effects_path.C,
					effects_path.D, ellapsed_time);
			// Advance the current pairs along the user's path
			L = L.setPair(effects_path.A, effects_path.B, currFrame);
			R = R.setPair(effects_path.C, effects_path.D, currFrame);
			ctr_of_roi= MathUtility.findCtr(L,R);
			bigR = MathUtility.findFurthestFinger(L,R, ctr_of_roi);
			float ratio= ratio_slider.getHistory().get(currFrame);
			//Proxy the pairs based on the current raio of the frame
			MathUtility.proxy_pairs(L,R,ratio,ctr_of_roi);
			texture.calculate_warp(L,R);
		}// end of animate
			
		if (showEdges) texture.drawEdges();

		if (showTexture) texture.paintImage(myImage);

		if (showSpirals) {
			try{
				
			L.showAll(this);
			R.showAll(this);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if (saveWarp) { saveWarp(); }
		
		//Show the multiTouchController
		showMultiTouchController();

		if (showMenu) menu.draw(this);

		if (showWarp) { showWarp();	}
		
		if(draw_finger_paths){ draw_finger_paths();}
		
		if(!animate){ratio_slider.draw();}

	}// End of draw

	private void setPairs(MultiTouchController mCont) {
		
	}

	private void editWarp(Pair L, Pair R, MultiTouchController mController) {
		Pt v1 = mController.getDiskAt(0);
		Pt v2 = mController.getDiskAt(1);
		Pt v3 = mController.getDiskAt(2);
		displaceBoundingBox(effects_path, v1, v2, v3);
		editWarp(effects_path.A, effects_path.B, effects_path.C,
		effects_path.D, v1, v2, v3,L,R);
		effects_path.drawBoundingBox(this);
		mController.showTriangle(this);
	}

	private void computeBarys() {
		clearBarys();
		getBaryCentricCoords();
		compute_bary=false;
		
	}

	private void createNewController() {
		mController = new MultiTouchController(menu,effects_path, ratio_slider, this);
		createNewController = false;
	}

	private void draw_finger_paths() {
		try{
			showFingerHistory(mController);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void showMultiTouchController() {
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
	}

	private void showWarp() {
		this.noFill();
		this.strokeWeight(6);
		this.stroke(0, 255, 0);
		effects_path.showPaths(this);
	}

	private void grabPoints(Pair L, Pair R) {
		L.writePairsToFile(motionFile);
		R.writePairsToFile(motionFile);
		motionString += L.addToString();
		motionString += R.addToString();
		grabPoints = false;
	}

	private void clearBarys() {
		weights_a.clear();
		weights_b.clear();
		weights_c.clear();
		weights_d.clear();
		weights_bounding_box.clear();
	}

	private void showFingerHistory(MultiTouchController mController2) {
		ArrayList<Pt> temp;//= mController2.getHistoryOf(0);
		for(int i=0;i<4;i++){
		temp= mController2.getHistoryOf(i);	
			noFill();
			stroke(0,255,0);
			strokeWeight(8);
			beginShape();
			for(Pt p: temp){
				vertex(p.x,p.y);
			}
			endShape();	
		}
	}
	
	private void displaceBoundingBox(MotionPath mp, Pt v1, Pt v2, Pt v3){
		Weight w= weights_bounding_box.get(0);
		effects_path.upperLeftCorner=w.to_global_coords(v1, v2, v3);
		w=weights_bounding_box.get(1);
		effects_path.upperRightCorner=w.to_global_coords(v1, v2, v3);
		w=weights_bounding_box.get(2);
		effects_path.bottomRightCorner=w.to_global_coords(v1, v2, v3);
		w=weights_bounding_box.get(3);
		effects_path.bottomLeftCorner=w.to_global_coords(v1, v2, v3);
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private PImage goGetImage(String string) {
		Bitmap bMap = null;
		PImage img = null;
		try {
			bMap = BitmapFactory.decodeFile(string); // Get the jpeg image as a Bitmap from the filesystem
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

	public int findCurrentFrame(ArrayList<Pt> _A, ArrayList<Pt> _B,
			ArrayList<Pt> _C, ArrayList<Pt> _D, double _ellapsed_time){
		int numFrames = min(min(_A.size(), _B.size()),
				min(_C.size(), _D.size()));
		double currentT = (System.nanoTime() / 1000000000.0) - firstFrameT;
		int ret = computeAnimationFrame(currentT, _ellapsed_time, numFrames);
		if (ret >= numFrames) {
			ret = 0;
			firstFrame = true;
		}
		return ret;
	}
	
	private void realTimeWarp(Texture t, Pair l2, Pair r2) {
		
		
		ctr_of_roi= MathUtility.findCtr(l2,r2);
		bigR = MathUtility.findFurthestFinger(l2,r2, ctr_of_roi);
		//Visualize the region of influence
		visualizeROI(ctr_of_roi,bigR);
		MathUtility.proxy_pairs(l2,r2,ratio_slider.getRatio(),ctr_of_roi);
		t.calculate_warp(l2,r2);
	}

	private void visualizeROI(Pt ctr_of_roi2, float bigR2) {
		stroke(0,255,0);
		ellipse(ctr_of_roi.x,ctr_of_roi.y, bigR*2, bigR*2);
		float littleR = bigR*ratio_slider.getRatio();
		ellipse(ctr_of_roi.x,ctr_of_roi.y, littleR*2, littleR*2);
		ctr_of_roi.show(ctr_of_roi,this);
		
	}

	private void debugTextSetup() {
		textSize(32);
		stroke(255,0,0);
		strokeWeight(10);
		noFill();
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
				if (fingersOnScreen && firstPass && !editWarp) {
					createNewController = true;
				}

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
							pointerCount, System.nanoTime())); // queue all the data recieved from the motion events
				}
			}
		}
		return super.surfaceTouchEvent(me);
	}

	void saveWarp() {// Bakes the warped image as a PImage
		noLoop();
		myImage = get(0, 0, displayWidth, displayHeight);
		loop();
		saveWarp = false;
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
				// showController = false;
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

	void drawArrayList(ArrayList<Pt> list) {
		for (Pt p : list) {
			fill(255, 0, 0);
			show(p);
		}
	}

	/********************************************* End of Drawing helper functions ************************************************/

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

	public static String makePtsString() {
		String ret = "";
		for (Pt p : effects_path.A) {
			ret += p.x + "," + p.y + "\n";
		}
		ret += "-\n";
		for (Pt p : effects_path.B) {
			ret += p.x + "," + p.y + "\n";
		}
		ret += "-\n";
		for (Pt p : effects_path.C) {
			ret += p.x + "," + p.y + "\n";
		}
		ret += "-\n";
		for (Pt p : effects_path.D) {
			ret += p.x + "," + p.y + "\n";
		}
		ret += "-\n";
		return ret;
	}

	// image naming and path to include sd card appending name you choose for
	// file
	public void timeToAnimate() {
		if (!editWarp) {
			effects_path.smooth();
		}
		animate = true;
		showController = false;
		showSpirals = true;
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
		//motionString += "\nellapsedTime\n" + mController.getElapsedTime();
		motionString += "\nellapsedTime\n" + ellapsed_time;
	}

	public void sendAnimation() {
		
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
	
	public static void load_warp_points(MotionPath mp){
		parse_paths(mp);
		effects_path.setTo(mp);
		effects_path.getBoundingBoxCoords();
		first_load_canned_warp=true;
	
		mController = new MultiTouchController(menu);
		editWarp = true;
		ellapsed_time = 5;
		reset_to_motion_path=mp;
		//showWarp = true; 
	}
	
	private static void parse_paths(MotionPath mp) {
		seperatePts(split(mp.unparsed_string,"\n"),mp);
	}

	//Seperates a text file into seperate ArrayLists each one represents a different fingers motions
	public static void seperatePts(String [] ss,MotionPath mp) {  
	  int spaces=0;
	  ArrayList<String> myList=new ArrayList<String>();
	  for (int i=0;i<ss.length;i++) {
	    if (ss[i].equals("-")) {
	      if (spaces==0) {
	    	  System.out.println("myList: "+myList.get(0));
	    	  mp.A= addPtsToList(myList);
	      }  
	      else if (spaces==1) {
	    	  mp.B= addPtsToList(myList);
	      }
	      else if (spaces==2)
	    	  mp.C=addPtsToList(myList);
	      else if (spaces==3)
	    	  mp.D=addPtsToList(myList);
	      else {
	        break;
	      }
	      myList.clear();
	      spaces++;
	    }
	   
	    else {
	      System.out.println("string ss: "+ss[i]);
	      myList.add(ss[i]);
	    }
	  }
	}

	//Takes in the finger motion lists and adds them to the user motion paths
	static ArrayList<Pt> addPtsToList(ArrayList<String> ss) {
	  ArrayList<Pt> list = new ArrayList<Pt>();
	  
	  int comma;   
	  float x, y; 
	  Pt n;  
	  for (int k=0; k<ss.size(); k++) { 
	    comma=ss.get(k).indexOf(',');   
	    x= Float.parseFloat(ss.get(k).substring(0, comma)); 
	    y= Float.parseFloat(ss.get(k).substring(comma+1, ss.get(k).length()));
	    n= new Pt(x,y);
	    list.add(n); 
	  }
	  return list;
	}
	
	public void getBaryCentricCoords() {
		// compute triangle
	
		// Compute The original Barycentric coords for the loaded effects_path
		for (Pt p : effects_path.A) {
			System.out.println("point p: "+p);
			weights_a.add(p.barycentric(mController.getDiskAt(0),
					mController.getDiskAt(1), mController.getDiskAt(2)));
		}
		
		for (Pt p : effects_path.B) {
			weights_b.add(p.barycentric(mController.getDiskAt(0),
					mController.getDiskAt(1), mController.getDiskAt(2)));
		}

		for (Pt p : effects_path.C) {
			weights_c.add(p.barycentric(mController.getDiskAt(0),
					mController.getDiskAt(1), mController.getDiskAt(2)));
		}

		for (Pt p : effects_path.D) {
			weights_d.add(p.barycentric(mController.getDiskAt(0),
					mController.getDiskAt(1), mController.getDiskAt(2)));
		}
		getBaryCentricCoordsOfBox();
	}

	private void getBaryCentricCoordsOfBox() {
		weights_bounding_box.add(effects_path.upperLeftCorner.barycentric(mController.getDiskAt(0),
				mController.getDiskAt(1), mController.getDiskAt(2)));
		weights_bounding_box.add(effects_path.upperRightCorner.barycentric(mController.getDiskAt(0),
			mController.getDiskAt(1), mController.getDiskAt(2)));
		weights_bounding_box.add(effects_path.bottomRightCorner.barycentric(mController.getDiskAt(0),
			mController.getDiskAt(1), mController.getDiskAt(2)));
		weights_bounding_box.add(effects_path.bottomLeftCorner.barycentric(mController.getDiskAt(0),
			mController.getDiskAt(1), mController.getDiskAt(2)));	
	}

	public void saveWarpPath() {
		
		Intent intent = new Intent(this, SaveWarpActivity.class);
		startActivity(intent);
	
	}
}