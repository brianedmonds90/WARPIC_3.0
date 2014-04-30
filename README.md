WARPIC_3.0
==========

Warpic consists of an Android application and a Web app. The android app handles the user input and can save the animation to be viewed at a later date on a web page.

#####Android Code Components: 

WarpicActivity.java-> Handles the rendering of Warpic. Also intercepts motion events from the Android System and feeds them to the MultiTouchController Object to be handled. Note that the touch events are queued as they are recieved by the system and then handled once per frame.

MultiTouchController.java-> Handles a variable number of fingers of user input. Important methods are touch, lift, and motion. 

Texture.java-> stores and instantiates the grid. Implements the warping methods that operate on the grid

Menu.java -> Holds all of the buttons that are used for user interaction. 

MyMotionEvent.java -> Stores the important data of the android motion event. This object is passed to the MultiTouchController to be processed.

#####WebApp Code Components

Runs processingjs. Basically, the Android processing graphics code was run within the processing ide to generate a javascript version of Warpic. As the webpage is loaded by the browser, the url is parsed for an unique id that identifies the animation. We load the motion path and the image and display the warp.

All server communication is done using a service called Parse.com. When a user decides to save an animation within the android application, we use Parse's api to save the data to their servers. Parse makes requests from the web application for the correct data. 