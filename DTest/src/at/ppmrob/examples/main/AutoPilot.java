package at.ppmrob.examples.main;

import at.ppmrob.examples.FeatureDetection;
import at.ppmrob.examples.MyCircle;
import at.ppmrob.examples.MyLine;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.DroneVideoListener;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.Math;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

public class AutoPilot implements DroneVideoListener, NavDataListener{

	private ARDrone drone;
	public static double PI = 3.14159265;
	  private AtomicReference<BufferedImage> image          = new AtomicReference<BufferedImage>();
	    FeatureDetection featureDetection = new FeatureDetection();
	  
	  
	  IplImage iplImg_lines;
	    IplImage iplImg_circles;
	    Vector<MyLine> detectedLines;
		Vector<MyCircle> detectedCircles;
	    IplImage imgWithCircles;
		IplImage imgWithLines;

	@Override
	public void navDataReceived(NavData nd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameReceived(int startX, int startY, int w, int h,
			int[] rgbArray, int offset, int scansize) {
		
		BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        im.setRGB(startX, startY, w, h, rgbArray, offset, scansize);
      
	       
	        if(im != null)
	        {
	        	
	        	int w1 = im.getWidth();
	        	int h1 = im.getHeight();

	        	iplImg_lines = IplImage.createFrom(im);
	        	detectedLines = featureDetection.detectLines(iplImg_lines, w, h);

	        	detectedCircles = featureDetection.detectCircles(imgWithLines, w, h);

	        }
	}

	public AutoPilot(ARDrone drone) {
		super();
		this.drone = drone;
		
		try {
		//Take off
		drone.takeOff();
		
		//Turn around 360 degrees
		
		//Follow the lines
		double averageangle = 0;
		for(int i = 0; i < detectedLines.size(); i++)
		{

			averageangle += lnangle(detectedLines.elementAt(i).point1.x(),
					detectedLines.elementAt(i).point1.y(),
					detectedLines.elementAt(i).point2.x(),
					detectedLines.elementAt(i).point2.y());
		}
		averageangle /= detectedLines.size();

		//Get the average of the line angles and go that way
		
		//Land
		drone.land();
		}
	 catch (IOException e1) {e1.printStackTrace();}
	}
	
	public double lnangle(int x1, int y1, int x2, int y2)
	{
	    double angle = 0;
	    double div1 = (x2 - x1);
	    if(div1 == 0) return 0;
	    double m1 = Math.atan ((y2 - y1) / div1);
	    m1 *= 180 / PI;
	    
	    if(m1 < 0) m1 = 360 + m1;
	    
	    angle = m1;
	    if(angle < 0) angle = 360 + angle;
	    if(angle > 180) angle -= 180;
	        
	    return angle;
	}

}
