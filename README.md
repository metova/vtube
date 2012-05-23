Vision for Youtube
====
[Vision for Youtube](http://vision.metova.com) is a BlackBerry app developed in 2009 by [Martin Reed](http://martinmreed.com) and [Metova, Inc](http://metova.com).

For more details about Vision, please visit [http://vision.metova.com](http://vision.metova.com). To download the app you can visit [http://vision.metova.com/download](http://vision.metova.com/download) in your mobile browser, or find the included vtube.zip.

Building the project
====
Vision utilizes the [Metova Mobile SDK](http://metova.com/x/FIAJ) for a large amount of the base code. Without this SDK and Maven plugin you will not be able to build the project. The purpose of opening this project is to provide developers with access to code samples and show what can be achieved without having to write an app entirely from scratch. If you are interested in licensing the SDK please visit [metova.com](http://metova.com/x/FIAJ) for more details.

Caveats
====
* Vision for YouTube is dependent not only on your phone's capabilities, but your currently available bandwidth.  Some carriers block access to stream via RTSP.  
* If you cannot stream with the BlackBerry browser from http://m.youtube.com, you will likewise not be able to use Vision for YouTube.  
* Vision for YouTube plays the only available YouTube stream that will work on BlackBerry and the application has no ability to impact the quality of the audio or video.
* If you receive an error code from the media player, there is unfortunately *nothing* we can do about it.  The application utilizes the provided media player component, therefore you will get to see both it's good and bad behaviors.  We have provided a list of [Player Error Codes] for you to help determine the specific nature of your issue.   Overwhelmingly, the majority of problems occur due to lack of bandwidth.

Requirements
====
Vision for YouTube is restricted to devices running a 4.3.0 or newer operating system, but is only recommended for use with devices using 4.6.0 and higher. While Vision may install and run correctly, the *device and carrier must support RTSP video streaming* in order to play the YouTube media.

Shortcuts
====
Shortcuts can be used through the keyboard for switching between pages and reloading a standard feed. The available shortcuts are:

Video list and details
----
* N = next page
* P = previous page
* R = reload feed (not searches)
* D = view video details
* (space) = watch a video
* (enter) = watch a video

Media player
----
* (touch) = enable/disable fullscreen
* F = enable/disable fullscreen
* R = restart video
* (space) pause/play video