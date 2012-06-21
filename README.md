# Vision for Youtube
[Vision for Youtube](http://vision.metova.com) is a BlackBerry app developed in 2009 by [Martin Reed](http://martinmreed.com) and [Metova, Inc](http://metova.com).

For more details about Vision, please visit [http://vision.metova.com](http://vision.metova.com). To download the app you can visit [http://vision.metova.com/download](http://vision.metova.com/download) in your mobile browser, or find the included [vtube.zip](https://github.com/hardisonbrewing/vtube/raw/master/vtube.zip).

# BlackBerry DevCon finalist
[Watch the presentation on YouTube](http://www.youtube.com/watch?v=v1KqwA8RMSc).

# Building the project
Vision utilizes the [Metova Mobile SDK](http://metova.com/x/FIAJ) for a large amount of the base code. Without this SDK and Maven plugin you will not be able to build the project. The purpose of opening this project is to provide developers with access to code samples and show what can be achieved without having to write an app entirely from scratch. If you are interested in licensing the SDK please visit [metova.com](http://metova.com/x/FIAJ) for more details.

# Caveats
* Vision for YouTube is dependent not only on your phone's capabilities, but your currently available bandwidth.  Some carriers block access to stream via RTSP.  
* If you cannot stream with the BlackBerry browser from http://m.youtube.com, you will likewise not be able to use Vision for YouTube.  
* Vision for YouTube plays the only available YouTube stream that will work on BlackBerry and the application has no ability to impact the quality of the audio or video.
* If you receive an error code from the media player, there is unfortunately *nothing* we can do about it.  The application utilizes the provided media player component, therefore you will get to see both it's good and bad behaviors.  We have provided a list of [Player Error Codes] for you to help determine the specific nature of your issue.   Overwhelmingly, the majority of problems occur due to lack of bandwidth.

# Requirements
Vision for YouTube is restricted to devices running a 4.3.0 or newer operating system, but is only recommended for use with devices using 4.6.0 and higher. While Vision may install and run correctly, the *device and carrier must support RTSP video streaming* in order to play the YouTube media.

# Shortcuts
Shortcuts can be used through the keyboard for switching between pages and reloading a standard feed. The available shortcuts are:

## Video list and details
* N = next page
* P = previous page
* R = reload feed (not searches)
* D = view video details
* (space) = watch a video
* (enter) = watch a video

## Media player
* (touch) = enable/disable fullscreen
* F = enable/disable fullscreen
* R = restart video
* (space) pause/play video

# FAQ
## I have a problem, how do I get help?
Please use the "Submit Feedback" feature in the application after you have experienced a problem.  This will send us detailed diagnostics that we may use to try and solve your problem.  Unfortunately, the "Contact Support" feature in AppWorld does not send us any information other than an email and your message so we do not have a very good chance of solving your problem.

## What does "Submit Feedback" do?
Using the "Submit Feedback" option from the menu will allow you to submit feedback about the application. It does not allow you to add comments to a video.  This feature will also send us diagnostic information and will assist us in diagnosing any problems.

## Where is the search feature?
The search feature is available only on the main screen navigable through a bar positioned at the top of the display. You can access it by scrolling your trackball up, or by touching the bar on a Storm.

## How much does it cost?
It is free!  Data charges may apply with your carrier but the Vision application does not charge for any of the functionality provided.

## Why does the video cutoff while watching?
Network throughput, this is unfortunately out of our hands and is dependent on your carrier or WiFi connection.

## Can I stream videos over the BIS network?
Short answer is no. The BIS network is limited in bandwidth, and streaming content like videos through it would affect the BlackBerry experience for other users. Instead Vision will first try to stream over WiFi if it is available, otherwise it will use WAP/TCP. The correct WAP service books or carrier APN settings will be required.

## Why did the application ask to use the native media player?
If Vision encounters an error during video playback that it cannot recover from, it will provide the option of viewing the video in the native media player instead. This may happen if there is a server error or a problem streaming the specified format type.

If the application crashes or displays the error a lot during video playback, the first thing to do is reduce the video format through the preferences. Some older versions of a given operating system may contain defects that will affect some video formats over others. An example of this is the 9000 (Bold) 4.6.0.167 operating system where the more stable format is the lowest.

The lower quality format available is "H.263 Video and AMR Audio."

The preferred solution would be to upgrade your operating system to the newest publicly available version.

## Why did the application crash during video playback?
If you are running an older version of your operating system, there may be defects that will affect some video formats over others. These defects can cause the application to crash unexpectedly. While Vision cannot prevent these unexpected crashes, it will try to predict a crash and display the option of viewing through the native media player (as of version 1.0.24).

See the FAQ above for more details.

## Can I save the video to the device?
No, this feature is not supported or currently possible due to restrictions with the RTSP stream.

## How do I uninstall the application?
Vision uninstalls just like any other third party application.  If you installed the application via AppWorld, you may open AppWorld, go to "My World" and use the menu to uninstall.  If you installed directly from our provisioning server and have a newer device, you should be able to highlight the icon on the ribbon, press the menu key and "Delete".  If you are using an older device, you may go to {{Options | Advanced Options | Applications | Select the application | press the menu key | Delete}}.  A reboot is required by the BlackBerry operating system before the icon will be removed from the ribbon.