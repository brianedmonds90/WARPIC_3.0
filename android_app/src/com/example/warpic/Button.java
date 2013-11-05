package com.example.warpic;
import processing.core.PApplet;
class Button{
  String title;
  int x,y,bWidth,bHeight;
  boolean pressed;
  public Button(String myTitle,int myX,int myY,PApplet pa){
    title=myTitle;
    x=myX;
    y=myY;
    findWidth(pa);
    findHeight(pa);
    pressed=false;
  }  
  void draw(PApplet pa){

	if(!pressed)    pa.fill(255);
	else pa.fill(0,255,255);
    pa.strokeWeight(5);
    pa.stroke(255,0,0);   
    pa.rect(x, y, bWidth, bHeight);
    pa.fill(0);
    pa.textSize(20);
    pa.text(title, x+(bWidth/4), y+(bHeight/2)); 
    pa.noStroke();
  }
  void drawWithTextSize(PApplet pa,int textSize){    
    pa.fill(255);
    pa.strokeWeight(5);
    pa.stroke(255,0,0);   
    pa.rect(x, y, bWidth, bHeight);
    pa.fill(0);
    pa.textSize(20);
    pa.text(title, x+(bWidth/4), y+(bHeight/2)); 
    pa.noStroke();
  }
  boolean pressed(Pt me){
    if(me.x<x){
      pressed=false;
      return false;
    } 
    if(me.x>x+bWidth){
      pressed=false;
      return false; 
    }
    if(me.y<y){
      pressed=false;
      return false; 
    }
    if(me.y>y+bHeight){
      pressed=false;
      return false; 
    }
   
    pressed=true;
    return true;
  }
  void findWidth(PApplet pa){
    bWidth=pa.width/3;
  }
  void findHeight(PApplet pa){
    bHeight=pa.height/8;
  }
}