package com.example.warpic;

import processing.core.PApplet;

class Menu {
	  Button playAnimation, sendAnimation;
	  int menuH;
	  int menuW;
	  WarpicActivity myPa;
	  
	  Menu(WarpicActivity pa) {
		sendAnimation=new Button("Send Animation Files", 500,500, pa);
		playAnimation=new Button("Play Animation",500,500,pa);
		myPa= pa;
		reArrange();
	    
	  }
	  
	  void reArrange(){
		  sendAnimation.x=0;
		  sendAnimation.y= myPa.height-sendAnimation.bHeight;
		  playAnimation.x=sendAnimation.bWidth;
		  playAnimation.y= myPa.height-playAnimation.bHeight;
		  
	  }
	  
	  void draw(PApplet pa) {
		  //draw the buttons here
		  sendAnimation.draw(pa);
		  playAnimation.draw(pa);
	  }
	 
	  void buttonPressed(Pt p) {
		  sendAnimation.pressed(p);
		  playAnimation.pressed(p);
		
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
		  unPressAll();
		  
	  }
	  
	  private void unPressAll() {
		  sendAnimation.pressed=false;
		  playAnimation.pressed=false;
	  }

	  public void motion(MyMotionEvent me) {
		  Pt p=me.loc;
		  sendAnimation.pressed(p);
	  }
}

