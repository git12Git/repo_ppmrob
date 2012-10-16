package at.ppmrob.autopilot;

import at.ppmrob.autopilot.state.AutoPilotState;
import at.ppmrob.autopilot.state.IStateTransition;
import at.ppmrob.autopilot.state.OnGroundPilotState;
import at.ppmrob.examples.main.LastKnownCircleLinePosition;
import at.ppmrob.featuredetection.FeatureDetection;
import at.ppmrob.featuredetection.IFeatureDetectionListener;
import at.ppmrob.featuredetection.MyCircle;
import at.ppmrob.featuredetection.MyLine;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

public class AutoPilot extends TimerTask implements DroneVideoListener, NavDataListener, IFeatureDetectionListener, IStateTransition {

	private boolean runAutoPilot = true;
	private boolean lostCircle = true;
	private boolean lostLine = true;


	private LastKnownCircleLinePosition lastKnownCircleLinePosition;
	//private long circleFoundTime;
	private long circleFoundTimeDifference;
	private long lineFoundTime;
	private long lineFoundTimeDifferece;

	private Timer timerCheckCircleOrLineLost;
	private TimerTask tt;
	private TimerTask checkCirclePosition;

	public TimerTask getCheckCirclePosition() {
		return checkCirclePosition;
	}

	public void setCheckCirclePosition(TimerTask checkCirclePosition) {
		this.checkCirclePosition = checkCirclePosition;
	}

	private static final Integer MOVE_WAIT_TIMEOUT = 500;

	private ARDrone drone;
	public static double PI = 3.14159265;
	private AtomicReference<BufferedImage> image = new AtomicReference<BufferedImage>();
	FeatureDetection featureDetection = new FeatureDetection();
	AutoPilotState autoPilotState;

	private int heightDroneCamera = 144; //240 144
	private int widthDroneCamera = 176; //320 176
	Rectangle2D.Double redZoneLeftSideRectangle = new Rectangle2D.Double(1, 1, heightDroneCamera*0.33f, widthDroneCamera-1);
	Rectangle2D.Double greenZoneCenterRectangle = new Rectangle2D.Double(heightDroneCamera*0.33f, 1, heightDroneCamera*0.33f, widthDroneCamera-1);
	Rectangle2D.Double redZoneRightSideRectangle = new Rectangle2D.Double(heightDroneCamera*0.66f, 1, heightDroneCamera*0.33f, widthDroneCamera-1);

	Rectangle2D.Double upperHalfSideRectangle = new Rectangle2D.Double(1, 1, heightDroneCamera-2, widthDroneCamera*0.5);
	Rectangle2D.Double bottomHalfSideRectangle = new Rectangle2D.Double(1, widthDroneCamera*0.5+2, heightDroneCamera-2, widthDroneCamera*0.5f-2);

	//	Rectangle2D.Double redZoneLeftSide = new Rectangle2D.Double(1, 1, widthDroneCamera*0.33f, heightDroneCamera-1);
	//	Rectangle2D.Double greenZoneCenter = new Rectangle2D.Double(widthDroneCamera*0.33f, 1, widthDroneCamera*0.33f, heightDroneCamera-1);
	//	Rectangle2D.Double redZoneRightSide = new Rectangle2D.Double(widthDroneCamera*0.66f, 1, widthDroneCamera*0.33f, heightDroneCamera-1);

	private Point2D.Double averageBullseyeCenter = new Point2D.Double();
	private Point2D.Double averageLinesCenter = new Point2D.Double();
	private CircleInformation foundCirclesInformation = new CircleInformation();

	IplImage iplImg_lines;
	IplImage iplImg_circles;
	Vector<MyLine> detectedLines = new Vector<MyLine>();
	Vector<MyCircle> detectedCircles = new Vector<MyCircle>();
	IplImage imgWithCircles;
	IplImage imgWithLines;

	float left_tilt = -0.1f;
	float right_tilt = 0.1f;
	// forward NEED TO BE MINUS
	float front_tilt = -0.1f;
	float back_tilt = 0.1f;


	private NavData droneNavData = new NavData();
	private float droneAltitude;
	public float getDroneAltitude() {
		return droneAltitude;
	}

	public void setDroneAltitude(float droneAltitude) {
		this.droneAltitude = droneAltitude;
	}

	private float droneBattery;
	private float droneYaw;
	private String droneControlState;
	private String droneFlyingState;
	private boolean droneIsFlying;
	private boolean droneIsBatteryTooHigh;
	private boolean droneIsBatteryTooLow;
	private boolean droneIsEmergency;


	@Override
	public void navDataReceived(NavData nd) {
		// the data here is just for drawing on screen. later we use another navdatalistener
		synchronized (this.droneNavData) {
			this.droneNavData=nd;
		}
		droneAltitude = this.droneNavData.getAltitude();
		droneBattery = this.droneNavData.getBattery();
		droneControlState = this.droneNavData.getControlState().name();
		droneFlyingState = this.droneNavData.getFlyingState().name();
		droneIsFlying = this.droneNavData.isFlying();
		droneIsBatteryTooHigh = this.droneNavData.isBatteryTooHigh();
		droneIsBatteryTooLow = this.droneNavData.isBatteryTooLow();
		droneIsEmergency = this.droneNavData.isEmergency();
		droneYaw = this.droneNavData.getYaw();
	}








	@Override
	public void frameReceived(int startX, int startY, int w, int h,
			int[] rgbArray, int offset, int scansize) {

		//Not sure whether this method is needed! Use getFeatures() instead
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
			// initialize autopilot
			drone = new ARDrone();
			AutoPilot sophisticatedpilot = new AutoPilot(new ARDrone());

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
			// stuff moved to constructor
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}










	/**
	 * 
	 * 
	 * 
	 * 
	 * @param drone
	 */
	public AutoPilot(ARDrone drone) throws IOException{
		super();
		this.drone = drone;
		this.drone.addNavDataListener(this);
		FeatureDetection.addFeatureDetectionListener(this);
		changeState(new OnGroundPilotState());
		//autoPilotState = AutoPilotState.DRONE_ON_GROUND;

		this.timerCheckCircleOrLineLost = new Timer();
		checkCirclePosition = new CheckCirclePosition(this, foundCirclesInformation);

		this.tt = new TimerTask() {

			@Override
			public void run() {

				long timeNow = System.currentTimeMillis();

		//		circleFoundTimeDifference = timeNow-AutoPilot.this.circleFoundTime;
				lineFoundTimeDifferece = timeNow-AutoPilot.this.lineFoundTime;

			
				if(AutoPilot.this.autoPilotState.equals(AutoPilotState.DRONE_FOLLOW_LINE)){

					if(lineFoundTimeDifferece>=6000){
						AutoPilot.this.autoPilotState=AutoPilotState.DRONE_LOST_LINE;
					}
				}

			}
		};

	}

	public void setCircleFoundTimeDifference(long circleFoundTimeDifference) {
		this.circleFoundTimeDifference = circleFoundTimeDifference;
	}

	public long getCircleFoundTimeDifference() {
		return circleFoundTimeDifference;
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * @param detectedLines
	 * @param detectedCircles
	 */
	public void getFeatures(Vector<MyLine> detectedLines,
			Vector<MyCircle> detectedCircles)
	{
		this.detectedLines = detectedLines;
		this.detectedCircles = detectedCircles;
	}//


	/**
	 * 
	 * 
	 * 
	 * 
	 */
	public void moveAlongLines() {
		try {

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

			// Get the average of the line angles and go that way

			// Land
			drone.land();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	/**
	 * 
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
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

	/**
	 * 
	 * 
	 * 
	 */
	//Get the average X coordinates of lines and try to converge to the middle
	public void stayInMiddle()
	{
		double averageX = 0;
		final int middle = 160;
		final int threshold = 50;

		for (int i = 0; i < detectedLines.size(); i++) {

			averageX +=
					(detectedLines.elementAt(i).point1.x() +
							detectedLines.elementAt(i).point2.x()) / 2;
		}// for
		averageX /= detectedLines.size();

		//Now decide whether to accelerate right or left or stay at place
		try{
			if(averageX > middle + threshold)
				drone.move(right_tilt, 0.0f, 0.0f, 0.0f);
			else if(averageX < middle - threshold)
				drone.move(left_tilt, 0.0f, 0.0f, 0.0f);
		} catch(IOException e) {e.printStackTrace();}

	}// stayInMiddle

	/**
	 * 
	 * @param foundCircles
	 * @return
	 */
	public synchronized Point2D.Double updateAverageCenterOfFoundCircles(Vector<MyCircle> foundCircles){//, int w, int h) {
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
				averageBullseyeCenter.setLocation((xCoordCount/circlesCount), (yCoordCount/circlesCount));

				if(greenZoneCenterRectangle.contains(averageBullseyeCenter)){
					if(upperHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.CENTER_RECTANGLE_UPPER_HLAF;
					}
					if(bottomHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.CENTER_RECTANGLE_BOTTOM_HLAF;
					}
				}
				if(redZoneLeftSideRectangle.contains(averageBullseyeCenter)){
					if(upperHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.LEFT_RECTANGLE_UPPER_HLAF;
					}
					if(bottomHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.LEFT_RECTANGLE_BOTTOM_HLAF;
					}
				}
				if(redZoneRightSideRectangle.contains(averageBullseyeCenter)){
					if(upperHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.RIGHT_RECTANGLE_UPPER_HLAF;
					}
					if(bottomHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.RIGHT_RECTANGLE_BOTTOM_HLAF;
					}
				}

				//				this.dronePleaseStayOver_Current_Bullseye(w, h);

				System.out.println("LAST KNOWN POSITION ########"+this.lastKnownCircleLinePosition);

				return averageBullseyeCenter;
			}
		} 

		//TODO no circles found, drone lost, drone go home
		averageBullseyeCenter.setLocation(0, 0);
		return averageBullseyeCenter;

		//go back to bullseye ?!?!
		//			averageCirclesCenter.setLocation(20, 80);
		//			
		//			this.dronePleaseStayOver_Current_Bullseye(w, h);
		//			
		//			return averageCirclesCenter;
	}











	public synchronized Point2D.Double updateAverageCenterOfFoundLines(Vector<MyLine> foundLines){//, int w, int h) {
		int lineCount = 0;
		int xCoordCount = 0;
		int yCoordCount = 0;

		if(foundLines!=null){
			lineCount=foundLines.size();
			if(lineCount>0){
				for(MyLine line_n:foundLines){
					xCoordCount+=line_n.point1.x();
					xCoordCount+=line_n.point2.x();
					yCoordCount+=line_n.point1.y();
					yCoordCount+=line_n.point2.y();

				}

				averageLinesCenter.setLocation((xCoordCount/lineCount), (yCoordCount/lineCount));

				if(greenZoneCenterRectangle.contains(averageLinesCenter)){
					if(upperHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.CENTER_RECTANGLE_UPPER_HLAF;
					}
					if(bottomHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.CENTER_RECTANGLE_BOTTOM_HLAF;
					}
				}
				if(redZoneLeftSideRectangle.contains(averageLinesCenter)){
					if(upperHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.LEFT_RECTANGLE_UPPER_HLAF;
					}
					if(bottomHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.LEFT_RECTANGLE_BOTTOM_HLAF;
					}
				}
				if(redZoneRightSideRectangle.contains(averageLinesCenter)){
					if(upperHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.RIGHT_RECTANGLE_UPPER_HLAF;
					}
					if(bottomHalfSideRectangle.contains(averageBullseyeCenter)){
						this.lastKnownCircleLinePosition=LastKnownCircleLinePosition.RIGHT_RECTANGLE_BOTTOM_HLAF;
					}
				}

				//				this.dronePleaseStayOver_Current_Bullseye(w, h);

				return averageLinesCenter;
			}
		} 

		//TODO no circles found, drone lost, drone go home
		averageBullseyeCenter.setLocation(0, 0);
		return averageBullseyeCenter;

		//go back to bullseye ?!?!
		//			averageCirclesCenter.setLocation(20, 80);
		//			
		//			this.dronePleaseStayOver_Current_Bullseye(w, h);
		//			
		//			return averageCirclesCenter;
	}













	/**
	 * 
	 * 
	 * 
	 * 
	 */
	private void droneTurn360(){
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
	 */

	
	/*private void dronePleaseStayOver_Current_Bullseye(){
		*//**
		 * ich bin nicht sicher aber ich glaube mich zu erinnern das bei der drone die width und height umkehrt
		 * waren ??? (drone width == standard height   &   drone height == standard width)
		 * w=176   33% = 58
		 * h=144   33% = 47
		 *//*	
		int countMoves=0;


		//###################################
		//drone don't see the bullseye - lost
		//###################################
		while((!greenZoneCenterRectangle.contains(averageBullseyeCenter) &&
				!redZoneLeftSideRectangle.contains(averageBullseyeCenter) &&
				!redZoneRightSideRectangle.contains(averageBullseyeCenter)) &&
				!autoPilotState.equals(AutoPilotState.DRONE_FOLLOW_LINE) &&
				(autoPilotState.equals(AutoPilotState.DRONE_STAY_OVER_STARlostTING_CIRCLE)) || autoPilotState.equals(AutoPilotState.DRONE_STAY_OVER_FINISH_CIRCLE)) {


			if(countMoves>30){
				try {
					this.drone.land();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.runAutoPilot=false;
			}



			try {

				if(lastKnownCircleLinePosition.equals(LastKnownCircleLinePosition.CENTER_RECTANGLE_UPPER_HLAF)){
					// drone go forward
					this.drone.move(0.0f, -0.05f, 0.0f, 0.0f);

					Thread.sleep(MOVE_WAIT_TIMEOUT);
					countMoves++;
					if(countMoves>30){
						this.drone.land();
						this.runAutoPilot=false;
					}
				} else if(lastKnownCircleLinePosition.equals(LastKnownCircleLinePosition.CENTER_RECTANGLE_BOTTOM_HLAF)){
					// drone go back
					this.drone.move(0.0f, 0.05f, 0.0f, 0.0f);
					Thread.sleep(MOVE_WAIT_TIMEOUT);
					countMoves++;
					if(countMoves>30){
						this.drone.land();
						this.runAutoPilot=false;
					}
				}

				if(lastKnownCircleLinePosition.equals(LastKnownCircleLinePosition.LEFT_RECTANGLE_UPPER_HLAF)){
					// drone go left
					this.drone.move(-0.05f, 0.0f, 0.0f, 0.0f);
					//							if(countMoves%2==0){
					// drone go forward every second move
					this.drone.move(0.0f, -0.05f, 0.0f, 0.0f);
					//							}
					Thread.sleep(MOVE_WAIT_TIMEOUT);
					countMoves++;
					if(countMoves>30){
						this.drone.land();
						this.runAutoPilot=false;
					}
				} else if(lastKnownCircleLinePosition.equals(LastKnownCircleLinePosition.LEFT_RECTANGLE_BOTTOM_HLAF)){
					// drone go left
					this.drone.move(-0.05f, 0.0f, 0.0f, 0.0f);
					//							if(countMoves%2==0){
					// drone go back every second move
					this.drone.move(0.0f, 0.05f, 0.0f, 0.0f);
					//							}
					Thread.sleep(MOVE_WAIT_TIMEOUT);
					countMoves++;
					if(countMoves>30){
						this.drone.land();
						this.runAutoPilot=false;
					}
				}

				if(lastKnownCircleLinePosition.equals(LastKnownCircleLinePosition.RIGHT_RECTANGLE_UPPER_HLAF)){
					// drone go right
					this.drone.move(0.05f, 0.0f, 0.0f, 0.0f);
					//							if(countMoves%2==0){
					// drone go forward every second move
					this.drone.move(0.0f, -0.05f, 0.0f, 0.0f);
					//							}
					Thread.sleep(MOVE_WAIT_TIMEOUT);
					countMoves++;
					if(countMoves>30){
						this.drone.land();
						this.runAutoPilot=false;
					}
				} else if(lastKnownCircleLinePosition.equals(LastKnownCircleLinePosition.RIGHT_RECTANGLE_BOTTOM_HLAF)){
					// drone go right
					this.drone.move(0.05f, 0.0f, 0.0f, 0.0f);
					//							if(countMoves%2==0){
					// drone go back every second move
					this.drone.move(0.0f, 0.05f, 0.0f, 0.0f);
					//							}
					Thread.sleep(MOVE_WAIT_TIMEOUT);
					countMoves++;
					if(countMoves>30){
						this.drone.land();
						this.runAutoPilot=false;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			countMoves++;
		}

	}
*/

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
	 */
	private void dronePleaseFollowTheLine(){
		/**
		 * ich bin nicht sicher aber ich glaube mich zu erinnern das bei der drone die width und height umkehrt
		 * waren ??? (drone width == standard height   &   drone height == standard width)
		 * w=176   33% = 58
		 * h=144   33% = 47
		 */
		//

		boolean follow = true;

		while(follow){

			//TODO CIRCLE OUT OF SIGHT
			//TODO drone go backward/forward

			int countMoves=0;



			while(redZoneLeftSideRectangle.contains(averageLinesCenter)){
				// drone go right
				try {
					this.drone.move(0.05f, 0.0f, 0.0f, 0.0f);
					Thread.sleep(1000);
					countMoves++;
					if(countMoves>30){
						this.drone.land();
						this.runAutoPilot=false;
						follow=false;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}




			while(redZoneRightSideRectangle.contains(averageLinesCenter)){
				// drone go left
				try {
					this.drone.move(-0.05f, 0.0f, 0.0f, 0.0f);
					Thread.sleep(1000);
					countMoves++;
					if(countMoves>30){
						this.drone.land();
						this.runAutoPilot=false;
						follow=false;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}





			while(greenZoneCenterRectangle.contains(averageLinesCenter)){
				try {
					this.drone.move(0.0f, 0.05f, 0.0f, 0.0f); // go forward
					Thread.sleep(1000);
					countMoves++;
					if(countMoves>=8){ // the start bullseye should be invisible know
						if(circleFoundTimeDifference<=4000){ 
							//finish bullseye is visible
							follow=false;
							break;
						}
					}
					if(countMoves>20){
						this.drone.land();
						this.runAutoPilot=false;
						follow=false;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}//while(follow) loop

	}
	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public void foundCircles(Vector<MyCircle> circles) {
		// TODO Auto-generated method stub
		foundCirclesInformation.setCircleFoundTime(System.currentTimeMillis());
		foundCirclesInformation.setDetectedCircles(circles);

	}	
	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public void foundLines(Vector<MyLine> lines) {
		// TODO Auto-generated method stub
		this.lineFoundTime = System.currentTimeMillis();
		synchronized (this.detectedLines) {
			this.detectedLines=lines;
			this.updateAverageCenterOfFoundLines(lines);
		}

	}

	/**
	 * 
	 * 
	 * 
	 */
	public void stopAutoPilot(){
		this.runAutoPilot=false;
		try {
			this.drone.land();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		autoPilotState=AutoPilotState.DRONE_ON_GROUND;
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public void run() {

		//while(runAutoPilot){

		autoPilotState.handle();
		try {


			if(autoPilotState.equals(AutoPilotState.DRONE_TURN_360)){
				autoPilotState=AutoPilotState.DRONE_FOLLOW_LINE;
				this.dronePleaseFollowTheLine();
			}
			if(autoPilotState.equals(AutoPilotState.DRONE_FOLLOW_LINE)){
				autoPilotState=AutoPilotState.DRONE_STAY_OVER_FINISH_CIRCLE;
				this.dronePleaseStayOver_Current_Bullseye();
			}
			if(autoPilotState.equals(AutoPilotState.DRONE_STAY_OVER_FINISH_CIRCLE)){
				autoPilotState=AutoPilotState.DRONE_LAND;
				this.drone.land();
				this.runAutoPilot=false;
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//	}



		try {
			this.drone.land();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void changeState(AutoPilotState state) {
		// TODO Auto-generated method stub
		state.setAutoPilot(this);
		this.autoPilotState = state;
	}

	public void takeOff() {
		try {
			drone.takeOff();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isDroneFlying() {
		return droneIsFlying;
	}

	public void ascendDrone(float value) throws IOException {
		drone.move(0.0f, 0.0f, value, 0.0f);
	}

	public void turnLeft(float value) throws IOException {
		drone.move(0.0f, 0.0f, 0.0f, -1 * value);//turn left ca.10degree ?!?!?!?!?
	}
	
	public void turnRight(float value) throws IOException {
		drone.move(0.0f, 0.0f, 0.0f, value);	
	}
	
	public void goLeft(float value) throws IOException {
		drone.move(-1 * value, 0.0f, 0.0f, 0.0f);
	}
	
	public void goRight(float value) throws IOException {
		drone.move(value, 0.0f, 0.0f, 0.0f);
	}
	
	public void goForward(float value) throws IOException {
		drone.move(0.0f, -1 * value, 0.0f, 0.0f);	
	}
	

	public void goBack(float value) throws IOException {
		drone.move(0.0f, value, 0.0f, 0.0f);	
	}
	
	public CircleInformation getFoundCirclesInformation() {
		return foundCirclesInformation;
	}

	public void setFoundCirclesInformation(CircleInformation foundCirclesInformation) {
		this.foundCirclesInformation = foundCirclesInformation;
	}

}
