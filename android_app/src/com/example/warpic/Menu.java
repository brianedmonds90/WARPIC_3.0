package com.example.warpic;

import processing.core.PApplet;

class Menu {
	  Button playAnimation, sendAnimation, showSpirals,showEdges, 
	  	loadMotionPath, showTexture, editWarp, saveWarpPath, showWarp,showHistory,
	  	reset;
	  int menuH;
	  int menuW;
	  WarpicActivity myPa;
	  
	  Menu(WarpicActivity pa) {
		sendAnimation=new Button("Send Animation Files", 500,500, pa);
		playAnimation=new Button("Play Animation",500,500,pa);
		showSpirals = new Button("Show Spirals", 500,500,pa);
		showEdges = new Button("ShowEdges", 500,500,pa);
		loadMotionPath = new Button("Load Motion", 500,500,pa);
		showTexture = new Button("Show Texture", 0,0 , pa);
		editWarp = new Button("Edit Warp", 0, 0, pa);
		saveWarpPath = new Button("Save Warp", 0,0,pa);
		showWarp = new Button("Show Warp",0,0,pa);
		showHistory= new Button("Show History", 0, 0, pa);
		reset= new Button("Reset",0,0,pa);
		myPa= pa;
		reArrange();
	  }
	  
	  void reArrange(){
		  sendAnimation.x=0;
		  sendAnimation.y= myPa.height-sendAnimation.bHeight;
		  playAnimation.x=sendAnimation.bWidth;
		  playAnimation.y= myPa.height-playAnimation.bHeight;
		  showSpirals.x=playAnimation.x+playAnimation.bWidth;
		  showSpirals.y=myPa.height-playAnimation.bHeight;
		  showEdges.x= 0;
		  showEdges.y= sendAnimation.y-showEdges.bHeight;
		  loadMotionPath.x=showEdges.bWidth;
		  loadMotionPath.y= sendAnimation.y-showEdges.bHeight;
		  showTexture.y= sendAnimation.y-showEdges.bHeight;
		  showTexture.x=loadMotionPath.x+loadMotionPath.bWidth;
		  editWarp.x=showTexture.x+editWarp.bWidth;
		  editWarp.y=showTexture.y;
		  saveWarpPath.x=editWarp.x;
		  saveWarpPath.y=playAnimation.y;
		  showWarp.x=saveWarpPath.x+showWarp.bWidth;
		  showWarp.y=saveWarpPath.y;
		  showHistory.x=showWarp.x;
		  showHistory.y=showWarp.y-showHistory.bHeight;
		  reset.x=myPa.width-reset.bWidth;
		  reset.y= showHistory.y-reset.bHeight;
		 }
	  
	  void draw(PApplet pa) {
		  //draw the buttons here
		  sendAnimation.draw(pa);
		  playAnimation.draw(pa);
		  showSpirals.draw(pa);
		  showEdges.draw(pa);
		  loadMotionPath.draw(pa);
		  showTexture.draw(pa);
		  editWarp.draw(pa);
		  saveWarpPath.draw(pa);
		  showWarp.draw(pa);
		  showHistory.draw(pa);
		  reset.draw(pa);
		 
	  }
	 
	  void buttonPressed(Pt p) {
		  sendAnimation.pressed(p);
		  playAnimation.pressed(p);
		  showSpirals.pressed(p);
		  showEdges.pressed(p);
		  loadMotionPath.pressed(p);
		  showTexture.pressed(p);
		  editWarp.pressed(p);
		  saveWarpPath.pressed(p);
		  showWarp.pressed(p);
		  showHistory.pressed(p);
		  reset.pressed(p);
		 
	  }
	  
	  void buttonLifted(){
		  //set all buttons pressed to false
		  if(sendAnimation.pressed){
			  myPa.addAnimationString();
			  myPa.sendAnimation();
		  }
		  else if(playAnimation.pressed)
			 myPa.timeToAnimate();
		  else if(showSpirals.pressed){
			  myPa.showSpirals=!myPa.showSpirals;
		  }
		  else if(showEdges.pressed){
			  myPa.showEdges=!myPa.showEdges;
		  }
		  else if(loadMotionPath.pressed){
			  myPa.launchMotionGallery();
		  }
		  else if(showTexture.pressed){
			  myPa.showTexture=!myPa.showTexture;
		  }
		  else if(editWarp.pressed){
			  try{
			//	  myPa.getReadyToAnimate_1();
			  }
			  catch(Exception e){
				  e.printStackTrace();
			  }
			  WarpicActivity.effects_path.getBaryCentricCoords(WarpicActivity.mController);
			  WarpicActivity.editWarp=!WarpicActivity.editWarp;
			  WarpicActivity.showWarp=true;
		  }
		  else if(saveWarpPath.pressed){
			  myPa.saveWarpPath();
		  }
		  else if(showWarp.pressed){
			  myPa.showWarp=!WarpicActivity.showWarp;
		  }
		  else if(showHistory.pressed){
			  myPa.draw_finger_paths=!myPa.draw_finger_paths;
		  }
		  else if(reset.pressed){
			  myPa.setup();
		  }
		 
		  unPressAll();
	  }
	  
	  private void unPressAll() {
		  sendAnimation.pressed=false;
		  playAnimation.pressed=false;
		  showSpirals.pressed=false;
		  showEdges.pressed=false;
		  loadMotionPath.pressed=false;
		  showTexture.pressed=false;
		  editWarp.pressed=false;
		  saveWarpPath.pressed=false;
		  showWarp.pressed=false;
		  showHistory.pressed=false;
		  reset.pressed=false;
		  	
	  }

	  public void motion(MyMotionEvent me) {
		  Pt p=me.loc;
		  sendAnimation.pressed(p);
	  }
}

