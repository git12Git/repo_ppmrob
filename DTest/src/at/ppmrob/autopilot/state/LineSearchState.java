package at.ppmrob.autopilot.state;

import java.io.IOException;

import at.ppmrob.autopilot.CircleInformation;
import at.ppmrob.autopilot.LineInformation;
import at.ppmrob.examples.main.LastKnownCircleLinePosition;

public class LineSearchState extends AutoPilotState {

	private final float CORRECTION_IMPULSE = 0.05f;
	int counter =0;
		
	@Override
	public void internalHandling() {
		// TODO Auto-generated method stub
		LineInformation lineInformation = autoPilot.getFoundLineInformation();
		LastKnownCircleLinePosition lastPosLine = lineInformation.getLastKnownLinePosition();
		
		CircleInformation circlesFoundInformation = autoPilot.getFoundCirclesInformation();
		LastKnownCircleLinePosition lastPosCircle = circlesFoundInformation.getLastKnownCirclePosition();
		
		counter++;
		try{
			if(lastPosLine.equals(LastKnownCircleLinePosition.CENTER_UP_AND_BOTTOM)){
				autoPilot.goForward(CORRECTION_IMPULSE);
			}
			if(lastPosLine.equals(LastKnownCircleLinePosition.RIGHT_UP_AND_BOTTOM)){
				autoPilot.goForward(CORRECTION_IMPULSE);
			}
			if(lastPosLine.equals(LastKnownCircleLinePosition.LEFT_UP_AND_BOTTOM)){
				autoPilot.goForward(CORRECTION_IMPULSE);
			}
			if(lastPosLine.equals(LastKnownCircleLinePosition.RIGHT_UP_AND_CENTER_OR_LEFT)){
				if(counter%4==0){
					autoPilot.goRight(CORRECTION_IMPULSE);
				} else {
					autoPilot.turnRight(CORRECTION_IMPULSE);
				}
			}
			if(lastPosLine.equals(LastKnownCircleLinePosition.LEFT_UP_AND_CENTER_OR_RIGHT)){
				if(counter%4==0){
					autoPilot.goLeft(CORRECTION_IMPULSE);
				} else {
					autoPilot.turnLeft(CORRECTION_IMPULSE);
				}
			}
			if(lastPosLine.equals(LastKnownCircleLinePosition.CENTER_UP_AND_RIGHT_BOTTOM)){
				autoPilot.turnLeft(CORRECTION_IMPULSE);
			}
			if(lastPosLine.equals(LastKnownCircleLinePosition.CENTER_UP_AND_LEFT_BOTTOM)){
				autoPilot.turnRight(CORRECTION_IMPULSE);
			}
//			if(counter%5==0){
//				autoPilot.goForward(CORRECTION_IMPULSE);
//			}
		}catch (IOException x){
			x.printStackTrace();
		}

	}

}
