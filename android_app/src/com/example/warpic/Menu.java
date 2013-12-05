package com.example.warpic;

import processing.core.PApplet;

class Menu {
	  Button playAnimation, sendAnimation, showSpirals,showEdges, loadMotionPath;
	  int menuH;
	  int menuW;
	  WarpicActivity myPa;
	  
	  Menu(WarpicActivity pa) {
		sendAnimation=new Button("Send Animation Files", 500,500, pa);
		playAnimation=new Button("Play Animation",500,500,pa);
		showSpirals = new Button("Show Spirals", 500,500,pa);
		showEdges = new Button("ShowEdges", 500,500,pa);
		loadMotionPath = new Button("Load Motion", 500,500,pa);
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
		  loadMotionPath.y= sendAnimation.y-showEdges.bHeight;;
	  }
	  
	  void draw(PApplet pa) {
		  //draw the buttons here
		  sendAnimation.draw(pa);
		  playAnimation.draw(pa);
		  showSpirals.draw(pa);
		  showEdges.draw(pa);
		  loadMotionPath.draw(pa);
		  
	  }
	 
	  void buttonPressed(Pt p) {
		  sendAnimation.pressed(p);
		  playAnimation.pressed(p);
		  showSpirals.pressed(p);
		  showEdges.pressed(p);
		  loadMotionPath.pressed(p);
		
	  }
	  
	  void buttonLifted(){
		  //set all buttons pressed to false
		  if(sendAnimation.pressed){
			 
			  myPa.getReadyToAnimate();
			  myPa.addAnimationString();
			  myPa.sendAnimation();
		  }
		  else if(playAnimation.pressed)
			 myPa.timeToAnimate();
		  else if(showSpirals.pressed){
			  //myPa.launchMotionGallery();
			  myPa.showSpirals=!myPa.showSpirals;
		  }
		  else if(showEdges.pressed){
			  myPa.showEdges=!myPa.showEdges;
		  }
		  else if(loadMotionPath.pressed){
			  myPa.launchMotionGallery();
		  }
		  
		  unPressAll();
		  
	  }
	  
	  private void unPressAll() {
		  sendAnimation.pressed=false;
		  playAnimation.pressed=false;
		  showSpirals.pressed=false;
		  showEdges.pressed=false;
		  loadMotionPath.pressed=false;
	  }

	  public void motion(MyMotionEvent me) {
		  Pt p=me.loc;
		  sendAnimation.pressed(p);
	  }
}

