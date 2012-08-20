package at.ppmrob.examples.main;

import at.ppmrob.examples.FeatureDetection;
import at.ppmrob.examples.MyCircle;
import at.ppmrob.examples.MyLine;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.DroneVideoListener;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.Math;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

public class AutoPilot implements DroneVideoListener, NavDataListener {

	private ARDrone drone;
	public static double PI = 3.14159265;
	private AtomicReference<BufferedImage> image = new AtomicReference<BufferedImage>();
	FeatureDetection featureDetection = new FeatureDetection();

	private Point2D.Double averageCirclesCenter = new Point2D.Double();
	
	IplImage iplImg_lines;
	IplImage iplImg_circles;
	Vector<MyLine> detectedLines;
	Vector<MyCircle> detectedCircles;
	IplImage imgWithCircles;
	IplImage imgWithLines;

	float left_tilt = -0.1f;
	float right_tilt = 0.1f;
	// forward NEED TO BE MINUS
	float front_tilt = -0.1f;
	float back_tilt = 0.1f;

	@Override
	public void navDataReceived(NavData nd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void frameReceived(int startX, int startY, int w, int h,
			int[] rgbArray, int offset, int scansize) {

		BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		im.setRGB(startX, startY, w, h, rgbArray, offset, scansize);

		if (im != null) {

			int w1 = im.getWidth();
			int h1 = im.getHeight();

			iplImg_lines = IplImage.createFrom(im);
			detectedLines = featureDetection.detectLines(iplImg_lines, w, h);

			detectedCircles = featureDetection
					.detectCircles(imgWithLines, w, h);

		}
	}

	// This method is when its NOT called by another class
	public static void main(String[] args) {
		ARDrone drone;
		try {
			// Create ARDrone object,
			// connect to drone and initialize it.
			drone = new ARDrone();
			AutoPilot sophisticatedpilot = new AutoPilot(drone);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public AutoPilot(ARDrone drone) {
		super();
		this.drone = drone;
		try {

			drone.connect();
			drone.clearEmergencySignal();

			// Wait until drone is ready
			drone.waitForReady(3000);

			// do TRIM operation
			drone.trim();

			// Take off
			drone.takeOff();

			// Turn around 360 degrees

			// Follow the lines
			int times = 0;
			while (times < 100) {
				double averageangle = 0;
				for (int i = 0; i < detectedLines.size(); i++) {

					averageangle += lnangle(
							detectedLines.elementAt(i).point1.x(),
							detectedLines.elementAt(i).point1.y(),
							detectedLines.elementAt(i).point2.x(),
							detectedLines.elementAt(i).point2.y());
				}
				averageangle /= detectedLines.size();
				if (averageangle > 60 && averageangle < 120)
					drone.move(0.0f, front_tilt, 0.0f, 0.0f);
				else if (averageangle <= 60)
					drone.move(left_tilt, 0.0f, 0.0f, 0.0f);
				else if (averageangle >= 120)
					drone.move(right_tilt, 0.0f, 0.0f, 0.0f);
				else
					drone.land();
				/*
				 * this.addKeyListener(new KeyListener() { public void
				 * keyTyped(KeyEvent e) {} public void keyReleased(KeyEvent e)
				 * {} public void keyPressed(KeyEvent e) {}
				 * 
				 * });
				 */
				times++;
			}

			// Get the average of the line angles and go that way

			// Land
			drone.land();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public double lnangle(int x1, int y1, int x2, int y2) {
		double angle = 0;
		double div1 = (x2 - x1);
		if (div1 == 0)
			return 0;
		double m1 = Math.atan((y2 - y1) / div1);
		m1 *= 180 / PI;

		if (m1 < 0)
			m1 = 360 + m1;

		angle = m1;
		if (angle < 0)
			angle = 360 + angle;
		if (angle > 180)
			angle -= 180;

		return angle;
	}

	
	
	
	public synchronized Point2D.Double updateAverageCenterOfFoundCircles(Vector<MyCircle> foundCircles, int w, int h) {
		int circlesCount = 0;
		int xCoordCount = 0;
		int yCoordCount = 0;
		
		if(foundCircles!=null){
			circlesCount=foundCircles.size();
			if(circlesCount>0){
				for(MyCircle circle_n:foundCircles){
					xCoordCount+=circle_n.center.x();
					yCoordCount+=circle_n.center.y();
				}
				averageCirclesCenter.setLocation((xCoordCount/circlesCount), (yCoordCount/circlesCount));
				
				this.dronePleaseStayOver_Current_Bullseye(w, h);
				
				return averageCirclesCenter;
			}
		} 
			//go back to bullseye ?!?!
			averageCirclesCenter.setLocation(20, 80);
			
			this.dronePleaseStayOver_Current_Bullseye(w, h);
			
			return averageCirclesCenter;
		}
	
	/**
	 * 
	 *      height=144                height=144                height=144                   
	 *     33%  33%                    33%  33%                  33%  33%
	 *  +-------------+            +-------------+           +-------------+       
	 * w|   || | ||   |100%       w|   || | ||   |100%      w|   || x ||   |100%  end          
	 * i|   || | ||   |           i|   || | ||   |          i|   || | ||   |             
	 * d|   || | ||   |66%        d|   || | ||   |66%       d|   || | ||   |66%           
	 * t|   || | ||   |           t|   || | ||   |          t|   || | ||   |        
	 * h|   || | ||   |33%        h|   || | ||   |33%       h|   || | ||   |33%          
	 *  |   || x ||   | start      |   || | ||   |           |   || | ||   |      
	 *  +-------------+            +-------------+           +-------------+      
	 *   red green red              red green red            red green red     
	 * 
	 * @param averageCircCenter
	 * @param w
	 * @param h
	 */
	private void dronePleaseStayOver_Current_Bullseye(int w, int h){
		/**
		 * ich bin nicht sicher aber ich glaube mich zu erinnern das bei der drone die width und height umkehrt
		 * waren ??? (drone width == standard height   &   drone height == standard width)
		 * w=176   33% = 58
		 * h=144   33% = 47
		 */
		//
		
		Rectangle2D.Double greenZone_bullseye = new Rectangle2D.Double(h*0.33f, w, h*0.33f, w);
		
		Rectangle2D.Double redZone_bullseye_left_side = new Rectangle2D.Double(0, w, h*0.33f, w);
		Rectangle2D.Double redZone_bullseye_right_side = new Rectangle2D.Double(h*0.66f, w, h*0.33f, w);
		
		if(!greenZone_bullseye.contains(averageCirclesCenter)){
		    //TODO CIRCLE OUT OF SIGHT
			//TODO drone go backward/forward
			
			int countMoves=0;
			
			
			
			
			while(redZone_bullseye_left_side.contains(averageCirclesCenter)){
		        // drone go right
				try {
					this.drone.move(0.1f, 0.0f, 0.0f, 0.0f);
					Thread.sleep(1000);
					countMoves++;
					if(countMoves>15){
						this.drone.land();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			while(redZone_bullseye_right_side.contains(averageCirclesCenter)){
			   // drone go left
				try {
					this.drone.move(-0.1f, 0.0f, 0.0f, 0.0f);
					Thread.sleep(1000);
					countMoves++;
					if(countMoves>15){
						this.drone.land();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	/**
	 *      height=144                height=144                height=144                   
	 *     33%  33%                    33%  33%                  33%  33%
	 *  +-------------+            +-------------+           +-------------+       
	 * w|   || | ||   |100%       w|   || | ||   |100%      w|   || x ||   |100%  END          
	 * i|   || | ||   |           i|   || | ||   |          i|   || | ||   |             
	 * d|   || | ||   |66%        d|   || | ||   |66%       d|   || | ||   |66%           
	 * t|   || | ||   |           t|   || | ||   |          t|   || | ||   |        
	 * h|   || | ||   |33%        h|   || | ||   |33%       h|   || | ||   |33%          
	 *  |   || x ||   | START      |   || | ||   |           |   || | ||   |      
	 *  +-------------+            +-------------+           +-------------+      
	 *   red green red              red green red            red green red     
	 * 
	 * @param averageCircCenter
	 * @param w
	 * @param h
	 */
	private void dronePleaseFollowTheLine(Point2D.Double averageCircCenter, int w, int h){
		/**
		 * ich bin nicht sicher aber ich glaube mich zu erinnern das bei der drone die width und height umkehrt
		 * waren ??? (drone width == standard height   &   drone height == standard width)
		 * w=176   33% = 58
		 * h=144   33% = 47
		 */
		//
		
		Rectangle2D.Double greenZone_lines = new Rectangle2D.Double(h*0.33f, w, h*0.33f, w);
		
		Rectangle2D.Double redZone_lines_left_side = new Rectangle2D.Double(0, w, h*0.33f, w);
		Rectangle2D.Double redZone_lines_right_side = new Rectangle2D.Double(h*0.66f, w, h*0.33f, w);
	}
}
