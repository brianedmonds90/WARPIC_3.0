package com.example.warpic;
class MyMotionEvent{
	int action;
	int pointerId;
	int pointerCount;
	long nanoTime;
	Pt loc;
	MyMotionEvent(int k,int pId, Pt myLoc, int myPointerCount,long nanoTTime){
		action=k;
	    pointerId=pId;
	    loc= myLoc;
	    pointerCount=myPointerCount;
	    nanoTime=nanoTTime;
	}
}