package at.ppmrob.autopilot;

import at.ppmrob.autopilot.state.AutoPilotState;
import at.ppmrob.autopilot.state.IStateTransition;
import at.ppmrob.autopilot.state.OnGroundPilotState;
import at.ppmrob.featuredetection.FeatureDetection;
import at.ppmrob.featuredetection.IFeatureDetectionListener;
import at.ppmrob.featuredetection.MyCircle;
import at.ppmrob.featuredetection.MyLine;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Vector;

public class AutoPilot extends TimerTask implements NavDataListener, IFeatureDetectionListener, IStateTransition {

	private TimerTask checkCirclePosition;
	private TimerTask checkLinePosition;

	private ARDrone drone;
	public static double PI = 3.14159265;

	FeatureDetection featureDetection = new FeatureDetection();
	AutoPilotState autoPilotState;
	private CircleInformation foundCirclesInformation = new CircleInformation();
	private LineInformation foundLineInformation = new LineInformation();

	private NavData droneNavData = new NavData();
	private float droneAltitude;
	// probably not need but we leave it for now
	private float droneBattery;
	private float droneYaw;
	private String droneControlState;
	private String droneFlyingState;
	private boolean droneIsFlying;
	private boolean droneIsBatteryTooHigh;
	private boolean droneIsBatteryTooLow;
	private boolean droneIsEmergency;
	// end sensordata

	

	/**
	 * run()
	 * Main control loop of autopilot
	 * We make heavy use of the statepattern(GoF) here
	 * Each state of the drone is represented by its own class
	 * we always call the handle() method of our autoPilotState object
	 * state transision is handled by the handle() method itslef and thus, the autoPilotState member variable
	 * always represnts the current active state (in the AutoPilot class, we don't really care what state we are currently in)
	 */
	@Override
	public void run() {
		autoPilotState.handle();
		
	}

	
	public TimerTask getCheckCirclePosition() {
		return checkCirclePosition;
	}

	public void setCheckCirclePosition(TimerTask checkCirclePosition) {
		this.checkCirclePosition = checkCirclePosition;
	}
	

	public TimerTask getCheckLinePosition() {
		return checkLinePosition;
	}

	public void setCheckLinePosition(TimerTask checkLinePosition) {
		this.checkLinePosition = checkLinePosition;
	}
	
	public float getDroneAltitude() {
		return droneAltitude;
	}

	public void setDroneAltitude(float droneAltitude) {
		this.droneAltitude = droneAltitude;
	}

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

	
	public AutoPilot(ARDrone drone) throws IOException{
		super();
		this.drone = drone;
		this.drone.addNavDataListener(this);
		FeatureDetection.addFeatureDetectionListener(this);
		changeState(new OnGroundPilotState());
	}

	public void setCircleFoundTimeDifference(long circleFoundTimeDifference) {
		foundCirclesInformation.setCircleFoundTimeDifference(circleFoundTimeDifference);
	}

	public long getCircleFoundTimeDifference() {
		return foundCirclesInformation.getCircleFoundTimeDifference();
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
	 */
	
	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public void foundCircles(Vector<MyCircle> circles) {
		foundCirclesInformation.setCircleFoundTime(System.currentTimeMillis());
		foundCirclesInformation.setDetectedCircles(circles);
	}	

	@Override
	public void foundLines(Vector<MyLine> lines) {
		foundLineInformation.setLineFoundTime(System.currentTimeMillis());
		foundLineInformation.setDetectedLines(lines);
	}

	
	@Override
	public void changeState(AutoPilotState state) {
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
	
	

	public LineInformation getFoundLineInformation() {
		return foundLineInformation;
	}

	public void setFoundLineInformation(LineInformation foundLineInformation) {
		this.foundLineInformation = foundLineInformation;
	}

	@Override
	public AutoPilotState getCurrentState() {
		return autoPilotState;
	}
	
	/** @Deprecated
	 * 	 */
	/*
	public void getFeatures(Vector<MyLine> detectedLines,
			Vector<MyCircle> detectedCircles)
	{
		this.detectedLines = detectedLines;
		this.detectedCircles = detectedCircles;
	}
	*/

	/**@Deprecated
	 * probably not needed
	 */
	/*
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
	}*/
	
	/*
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


*/



	/**
	 * 
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	/*
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
	}*/

	/**
	 * 
	 * 
	 * 
	 */
	//Get the average X coordinates of lines and try to converge to the middle
	/*
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

	}
	*/// stayInMiddle

}
