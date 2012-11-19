package at.ppmrob.autopilot;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.codeminders.ardrone.Point;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_videostab.DeblurerBase;

import at.ppmrob.examples.main.LastKnownCircleLinePosition;
import at.ppmrob.featuredetection.MyLine;

public class LineInformation {
	
	private static final Long LAST_LINE_FOUND_TIMEOUT = 6000l;
	
	private long lineFoundTime;
	private long lineFoundTimeDifference;
	private Vector<MyLine> detectedLines;
	private Point2D.Double averageLinesCenter;
	
	private LastKnownCircleLinePosition lastKnownLinePosition;
	
	private int heightDroneCamera = 144; //240 144
	private int widthDroneCamera = 176; //320 176
	private Rectangle2D.Double greenZoneCenterRectangle = new Rectangle2D.Double(heightDroneCamera*0.33f, 1, heightDroneCamera*0.33f, widthDroneCamera-1);
	private Rectangle2D.Double redZoneRightSideRectangle = new Rectangle2D.Double(heightDroneCamera*0.66f, 1, heightDroneCamera*0.33f, widthDroneCamera-1);

	private Rectangle2D.Double upperHalfSideRectangle = new Rectangle2D.Double(1, 1, heightDroneCamera-2, widthDroneCamera*0.5);
	private Rectangle2D.Double bottomHalfSideRectangle = new Rectangle2D.Double(1, widthDroneCamera*0.5+2, heightDroneCamera-2, widthDroneCamera*0.5f-2);


	private Rectangle2D.Double redZoneLeftSideRectangle = new Rectangle2D.Double(1, 1, heightDroneCamera*0.33f, widthDroneCamera-1);
	
	private MyLine avgLine;
	private boolean isDroneInWrongPosition = true;
	
	public LineInformation() {
		detectedLines = new Vector<MyLine>();
		averageLinesCenter = new Point2D.Double();
	}
	
	public Vector<MyLine> getDetectedLines() {
		return detectedLines;
	}
	
	public void setDetectedLines(Vector<MyLine> detectedLines) {
		this.detectedLines = detectedLines;
		
		int lineCount = 0;
		int xCoordCount = 0;
		int yCoordCount = 0;
		
		synchronized (detectedLines) {
			if(detectedLines!=null){
				lineCount=detectedLines.size();
				if(lineCount>0){
				    int x1=0;
				    int y1=0;
				    int x2=0;
				    int y2=0;
					for(MyLine myLine:detectedLines){
						x1+=myLine.point1.x();
				    	y1+=myLine.point1.y();
				    	x2+=myLine.point2.x();
				    	y2+=myLine.point2.y();
				    	
					}
					
					x1=x1/lineCount;
				    y1=y1/lineCount;
				    x2=x2/lineCount;
				    y2=y2/lineCount;
				    avgLine =  new MyLine(new CvPoint().put(x1, y1), new CvPoint().put(x2, y2));
				    
					averageLinesCenter.setLocation((xCoordCount/lineCount), (yCoordCount/lineCount));

					Point2D point1 = new Point2D.Double(x1,y1);
					Point2D point2 = new Point2D.Double(x2,y2);
					
					if(greenZoneCenterRectangle.contains(point1) && greenZoneCenterRectangle.contains(point2)){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.CENTER_UP_AND_BOTTOM;
						this.isDroneInWrongPosition=false;
					}
					if(redZoneLeftSideRectangle.contains(point1) && redZoneLeftSideRectangle.contains(point2)){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.LEFT_UP_AND_BOTTOM;
						this.isDroneInWrongPosition=false;
					}
					if(redZoneRightSideRectangle.contains(point1) && redZoneRightSideRectangle.contains(point2)){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.RIGHT_UP_AND_BOTTOM;
						this.isDroneInWrongPosition=false;
					}
					
					if((upperHalfSideRectangle.contains(point1) && 
						redZoneRightSideRectangle.contains(point1) && 
						!redZoneRightSideRectangle.contains(point2)) ||
							(upperHalfSideRectangle.contains(point2) && 
							redZoneRightSideRectangle.contains(point2) && 
							!redZoneRightSideRectangle.contains(point1))){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.RIGHT_UP_AND_CENTER_OR_LEFT;
						this.isDroneInWrongPosition=true;
					}
					if((upperHalfSideRectangle.contains(point1) &&
						redZoneLeftSideRectangle.contains(point1) &&
						!redZoneLeftSideRectangle.contains(point2)) ||
							(upperHalfSideRectangle.contains(point2) &&
							redZoneLeftSideRectangle.contains(point2) &&
							!redZoneLeftSideRectangle.contains(point1))){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.LEFT_UP_AND_CENTER_OR_RIGHT;
						this.isDroneInWrongPosition=true;
					}
					if((upperHalfSideRectangle.contains(point1) &&
						greenZoneCenterRectangle.contains(point1) &&
						redZoneRightSideRectangle.contains(point2)) ||
							(upperHalfSideRectangle.contains(point2) &&
							greenZoneCenterRectangle.contains(point2) &&
							redZoneRightSideRectangle.contains(point1)) ){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.CENTER_UP_AND_RIGHT_BOTTOM;
						this.isDroneInWrongPosition=true;
					}
					if((upperHalfSideRectangle.contains(point1) && 
						greenZoneCenterRectangle.contains(point1) &&
						redZoneLeftSideRectangle.contains(point2)) ||
							(upperHalfSideRectangle.contains(point2) && 
							greenZoneCenterRectangle.contains(point2) &&
							redZoneLeftSideRectangle.contains(point1))){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.CENTER_UP_AND_LEFT_BOTTOM;
						this.isDroneInWrongPosition=true;
					}
						
					
					//				this.dronePleaseStayOver_Current_Bullseye(w, h);
					return;
				}
			} 

			//TODO no circles found, drone lost, drone go home
			averageLinesCenter.setLocation(0, 0);

		}
	}

	
	
	
	
	
	
	
	public void setDetectedLineAvg(Vector<MyLine> detectedLine) {
		this.detectedLines = detectedLine;
		
		int lineCount = 0;
		int xCoordCount = 0;
		int yCoordCount = 0;
		
		synchronized (detectedLines) {
			if(detectedLines!=null){
				lineCount=detectedLines.size();
				if(lineCount>0){
				   
				    this.avgLine =  detectedLine.firstElement();
				    
					averageLinesCenter.setLocation((xCoordCount/lineCount), (yCoordCount/lineCount));

					Point2D point1 = new Point2D.Double(this.avgLine.point1.x(), this.avgLine.point1.y());
					Point2D point2 = new Point2D.Double(this.avgLine.point2.x(), this.avgLine.point2.y());
					
					if(greenZoneCenterRectangle.contains(point1) && greenZoneCenterRectangle.contains(point2)){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.CENTER_UP_AND_BOTTOM;
						this.isDroneInWrongPosition=false;
					}
					if(redZoneLeftSideRectangle.contains(point1) && redZoneLeftSideRectangle.contains(point2)){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.LEFT_UP_AND_BOTTOM;
						this.isDroneInWrongPosition=false;
					}
					if(redZoneRightSideRectangle.contains(point1) && redZoneRightSideRectangle.contains(point2)){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.RIGHT_UP_AND_BOTTOM;
						this.isDroneInWrongPosition=false;
					}
					
					if((upperHalfSideRectangle.contains(point1) && 
						redZoneRightSideRectangle.contains(point1) && 
						!redZoneRightSideRectangle.contains(point2)) ||
							(upperHalfSideRectangle.contains(point2) && 
							redZoneRightSideRectangle.contains(point2) && 
							!redZoneRightSideRectangle.contains(point1))){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.RIGHT_UP_AND_CENTER_OR_LEFT;
						this.isDroneInWrongPosition=true;
					}
					if((upperHalfSideRectangle.contains(point1) &&
						redZoneLeftSideRectangle.contains(point1) &&
						!redZoneLeftSideRectangle.contains(point2)) ||
							(upperHalfSideRectangle.contains(point2) &&
							redZoneLeftSideRectangle.contains(point2) &&
							!redZoneLeftSideRectangle.contains(point1))){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.LEFT_UP_AND_CENTER_OR_RIGHT;
						this.isDroneInWrongPosition=true;
					}
					if((upperHalfSideRectangle.contains(point1) &&
						greenZoneCenterRectangle.contains(point1) &&
						redZoneRightSideRectangle.contains(point2)) ||
							(upperHalfSideRectangle.contains(point2) &&
							greenZoneCenterRectangle.contains(point2) &&
							redZoneRightSideRectangle.contains(point1)) ){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.CENTER_UP_AND_RIGHT_BOTTOM;
						this.isDroneInWrongPosition=true;
					}
					if((upperHalfSideRectangle.contains(point1) && 
						greenZoneCenterRectangle.contains(point1) &&
						redZoneLeftSideRectangle.contains(point2)) ||
							(upperHalfSideRectangle.contains(point2) && 
							greenZoneCenterRectangle.contains(point2) &&
							redZoneLeftSideRectangle.contains(point1))){
						this.lastKnownLinePosition=LastKnownCircleLinePosition.CENTER_UP_AND_LEFT_BOTTOM;
						this.isDroneInWrongPosition=true;
					}
						
					
					//				this.dronePleaseStayOver_Current_Bullseye(w, h);
					return;
				}
			} 

			//TODO no circles found, drone lost, drone go home
			averageLinesCenter.setLocation(0, 0);

		}
	}
	
	
	
	
	
	
	
	
	
	
	public long getLineFoundTime() {
		return lineFoundTime;
	}

	public void setLineFoundTime(long lineFoundTime) {
		this.lineFoundTime = lineFoundTime;
	}

	public long getLineFoundTimeDifference() {
		return lineFoundTimeDifference;
	}

	public void setLineFoundTimeDifference(long lineFoundTimeDifference) {
		this.lineFoundTimeDifference = lineFoundTimeDifference;
	}
	

	public boolean isDroneLost() {
		return (getLineFoundTimeDifference() >= LAST_LINE_FOUND_TIMEOUT);
	}

	public boolean isDroneInGreenRectangle() {
		return greenZoneCenterRectangle.contains(averageLinesCenter);
	}
	
	public boolean isDroneInLeftRedZoneRectangle() {
		return redZoneLeftSideRectangle.contains(averageLinesCenter);
	}
	
	public boolean isDroneInRightRedZoneRectangle() {
		return redZoneRightSideRectangle.contains(averageLinesCenter);
	}
	
	public boolean isDroneOutsideRectangles() {
		return !(isDroneInGreenRectangle() || isDroneInLeftRedZoneRectangle() || isDroneInRightRedZoneRectangle());
	}
	
	public boolean isDroneInWrongPosition(){
		return this.isDroneInWrongPosition;
	}

	public LastKnownCircleLinePosition getLastKnownLinePosition() {
		return lastKnownLinePosition;
	}
	
}
