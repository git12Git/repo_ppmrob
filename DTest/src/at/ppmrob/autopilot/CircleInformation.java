package at.ppmrob.autopilot;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import at.ppmrob.examples.main.AppWindows;
import at.ppmrob.examples.main.LastKnownCircleLinePosition;
import at.ppmrob.featuredetection.MyCircle;

public class CircleInformation {

	private static final Long LAST_CIRCLE_LOST_TIMEOUT = 15000l;
	private static final long DRONE_OUTSIDE_CIRCLE_TIMEOUT = 500l;

	private long circleFoundTime;
	private long circleFoundTimeDifference;
	private Vector<MyCircle> detectedCircles;
	private Point2D.Double averageBullsEyeCenter;

	private LastKnownCircleLinePosition lastKnownCirclePosition;

	private int heightDroneCamera = 144;//<-drone original //240 144
	private int widthDroneCamera = 176;//<-drone original //320 176
	private Rectangle2D.Double greenZoneCenterRectangle = new Rectangle2D.Double(heightDroneCamera*0.33f, 1, heightDroneCamera*0.33f, widthDroneCamera-1);
	private Rectangle2D.Double redZoneRightSideRectangle = new Rectangle2D.Double(heightDroneCamera*0.66f, 1, heightDroneCamera*0.33f, widthDroneCamera-1);

	private Rectangle2D.Double upperHalfSideRectangle = new Rectangle2D.Double(1, 1, heightDroneCamera-2, widthDroneCamera*0.5);
	private Rectangle2D.Double bottomHalfSideRectangle = new Rectangle2D.Double(1, widthDroneCamera*0.5+2, heightDroneCamera-2, widthDroneCamera*0.5f-2);


	private Rectangle2D.Double redZoneLeftSideRectangle = new Rectangle2D.Double(1, 1, heightDroneCamera*0.33f, widthDroneCamera-1);

	public Rectangle2D.Double getRedZoneLeftSideRectangle() {
		
		return redZoneLeftSideRectangle;
	}

	public void setRedZoneLeftSideRectangle(
			Rectangle2D.Double redZoneLeftSideRectangle) {
		this.redZoneLeftSideRectangle = redZoneLeftSideRectangle;
	}

	public Rectangle2D.Double getGreenZoneCenterRectangle() {
		return greenZoneCenterRectangle;
	}

	public void setGreenZoneCenterRectangle(
			Rectangle2D.Double greenZoneCenterRectangle) {
		this.greenZoneCenterRectangle = greenZoneCenterRectangle;
	}

	public Rectangle2D.Double getRedZoneRightSideRectangle() {
		return redZoneRightSideRectangle;
	}

	public void setRedZoneRightSideRectangle(
			Rectangle2D.Double redZoneRightSideRectangle) {
		this.redZoneRightSideRectangle = redZoneRightSideRectangle;
	}

	public Rectangle2D.Double getUpperHalfSideRectangle() {
		return upperHalfSideRectangle;
	}

	public void setUpperHalfSideRectangle(Rectangle2D.Double upperHalfSideRectangle) {
		this.upperHalfSideRectangle = upperHalfSideRectangle;
	}

	public Rectangle2D.Double getBottomHalfSideRectangle() {
		return bottomHalfSideRectangle;
	}

	public void setBottomHalfSideRectangle(
			Rectangle2D.Double bottomHalfSideRectangle) {
		this.bottomHalfSideRectangle = bottomHalfSideRectangle;
	}


	public long getCircleFoundTime() {
		return circleFoundTime;
	}

	public void setCircleFoundTime(long circleFoundTime) {
		this.circleFoundTime = circleFoundTime;
	}

	public Vector<MyCircle> getDetectedCircles() {
		return detectedCircles;
	}

	public void setDetectedCircles(Vector<MyCircle> detectedCircles) {

		int circlesCount = 0;
		int xCoordCount = 0;
		int yCoordCount = 0;

		synchronized (detectedCircles) {
			this.detectedCircles = detectedCircles;
			if(detectedCircles!=null){
				circlesCount=detectedCircles.size();
				for(MyCircle circle_n:detectedCircles){
					xCoordCount+=circle_n.center.x();
					yCoordCount+=circle_n.center.y();
				}
				averageBullsEyeCenter.setLocation((xCoordCount/circlesCount), (yCoordCount/circlesCount));

				if(greenZoneCenterRectangle.contains(averageBullsEyeCenter)){
					if(upperHalfSideRectangle.contains(averageBullsEyeCenter)){
						this.lastKnownCirclePosition=LastKnownCircleLinePosition.CENTER_RECTANGLE_UPPER_HLAF;
					}
					if(bottomHalfSideRectangle.contains(averageBullsEyeCenter)){
						this.lastKnownCirclePosition=LastKnownCircleLinePosition.CENTER_RECTANGLE_BOTTOM_HLAF;
					}
				}
				if(redZoneLeftSideRectangle.contains(averageBullsEyeCenter)){
					if(upperHalfSideRectangle.contains(averageBullsEyeCenter)){
						this.lastKnownCirclePosition=LastKnownCircleLinePosition.LEFT_RECTANGLE_UPPER_HLAF;
					}
					if(bottomHalfSideRectangle.contains(averageBullsEyeCenter)){
						this.lastKnownCirclePosition=LastKnownCircleLinePosition.LEFT_RECTANGLE_BOTTOM_HLAF;
					}
				}
				if(redZoneRightSideRectangle.contains(averageBullsEyeCenter)){
					if(upperHalfSideRectangle.contains(averageBullsEyeCenter)){
						this.lastKnownCirclePosition=LastKnownCircleLinePosition.RIGHT_RECTANGLE_UPPER_HLAF;
					}
					if(bottomHalfSideRectangle.contains(averageBullsEyeCenter)){
						this.lastKnownCirclePosition=LastKnownCircleLinePosition.RIGHT_RECTANGLE_BOTTOM_HLAF;
					}
				}

				//				this.dronePleaseStayOver_Current_Bullseye(w, h);

				System.out.println("LAST KNOWN POSITION ########"+this.lastKnownCirclePosition);

			} 

			// not needed anymore, this method doesn't get executed if no circles are found
			//averageBullsEyeCenter.setLocation(0, 0);
		}
	}

	public LastKnownCircleLinePosition getLastKnownCirclePosition() {
		return lastKnownCirclePosition;
	}

	public void setLastKnownCirclePosition(
			LastKnownCircleLinePosition lastKnownCirclePosition) {
		this.lastKnownCirclePosition = lastKnownCirclePosition;
	}

	public CircleInformation() {
		super();
		detectedCircles = new Vector<MyCircle>();
		averageBullsEyeCenter = new Point2D.Double();
		circleFoundTime = System.currentTimeMillis();
	}

	public long getCircleFoundTimeDifference() {
		return circleFoundTimeDifference;
	}

	public void setCircleFoundTimeDifference(long circleFoundTimeDifference) {
		this.circleFoundTimeDifference = circleFoundTimeDifference;
	}

	public boolean isDroneLost() {
		return (getCircleFoundTimeDifference() >= LAST_CIRCLE_LOST_TIMEOUT);
	}
	
	public boolean droneOutsideTimeoutReached() {
		return (getCircleFoundTimeDifference() >= DRONE_OUTSIDE_CIRCLE_TIMEOUT);
	}

	public boolean isDroneOutsideRectangles() {
		return !(!droneOutsideTimeoutReached() || isDroneInGreenRectangle() || isDroneInLeftRedZoneRectangle() || isDroneInRightRedZoneRectangle());
	}

	public boolean isDroneInGreenRectangle() {
		return greenZoneCenterRectangle.contains(averageBullsEyeCenter);
	}

	public boolean isDroneInLeftRedZoneRectangle() {
		return redZoneLeftSideRectangle.contains(averageBullsEyeCenter);
	}

	public boolean isDroneInRightRedZoneRectangle() {
		return redZoneRightSideRectangle.contains(averageBullsEyeCenter);
	}

	public Point2D.Double getAverageBullsEyeCenter() {
		return averageBullsEyeCenter;
	}

	public void setAverageBullsEyeCenter(Double x, Double y) {
		this.averageBullsEyeCenter = new Point2D.Double(x,y);
	}
	
	

}
