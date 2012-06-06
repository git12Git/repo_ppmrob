
package com.codeminders.ardrone.examples;

import com.codeminders.ardrone.ARDrone;


import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class TakeOffAndLand
{

    private static final long CONNECT_TIMEOUT = 3000;

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        ARDrone drone;
        try
        {
            // Create ARDrone object,
            // connect to drone and initialize it.
            drone = new ARDrone();
            drone.connect();
            drone.clearEmergencySignal();

            // Wait until drone is ready
            drone.waitForReady(CONNECT_TIMEOUT);

            // do TRIM operation
            drone.trim();

            // Take off
            System.err.println("Taking off");
            drone.takeOff();

            //I modified the numbers here!!!
            // Fly a little :)
            Thread.sleep(2000);

            // Land
            System.err.println("Landing");
            drone.land();

            // Give it some time to land
            Thread.sleep(2000);
            
            // Disconnect from the done
            drone.disconnect();

        } catch(Throwable e)
        {
            e.printStackTrace();
        }
    }

    public class Smoother { 
    	public void smooth(String filename) {
    		IplImage image = cvLoadImage(filename);
    		if (image != null) {
    			cvSmooth(image, image, CV_GAUSSIAN, 3);
    			cvSaveImage(filename, image);
    			cvReleaseImage(image);}
    		
    		}//smooth
    	}//Smoother
}//TakeOffAndLand

