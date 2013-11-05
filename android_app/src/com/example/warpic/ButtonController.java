package com.example.warpic;

import java.util.ArrayList;
import java.util.LinkedList;

public class ButtonController {
	ArrayList<Button> buttonList1;
	LinkedList<Button> buttonList;
	boolean active;
	ButtonController(){
		buttonList=new LinkedList<Button>();
		active = false;
	}
	void addButton(Button b){
		buttonList.add(b);
	}
//	boolean isActive(){
//		for(Button b: buttonList){
//			if(b.active)
//				return true;
//		}
//		return false;
//	}
	void handle(MyMotionEvent me){
		if (me.action==1) {//The user has touched the screen
			touch(me); //Register the touch event
		}
		else if (me.action==0) {//The user has lifted their fingers from the screen
			//Register the lift event
		//	lift(me);
		}
		else {
			//motion(me);//Register the motion event
		}
	}
	void touch(MyMotionEvent me){
		
	}
		
}
