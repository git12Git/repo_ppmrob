package at.ppmrob.autopilot.state;

import java.io.IOException;

import at.ppmrob.autopilot.CircleInformation;
import at.ppmrob.examples.main.LastKnownCircleLinePosition;

public class CircleSearchState extends AutoPilotState {

	private int countMoves = 0;
	private final float CORRECTION_IMPULSE = 0.05f;

	@Override
	protected void internalHandling() {

		CircleInformation circlesFoundInformation = autoPilot.getFoundCirclesInformation();
	
		LastKnownCircleLinePosition lastKnownPosition = circlesFoundInformation.getLastKnownCirclePosition();
		
		try {
			if (lastKnownPosition.equals(LastKnownCircleLinePosition.CENTER_RECTANGLE_UPPER_HLAF)) {
				autoPilot.goForward(CORRECTION_IMPULSE);
//				Thread.sleep(200);
//				autoPilot.goForward(CORRECTION_IMPULSE);
//				Thread.sleep(200);
//				autoPilot.goForward(CORRECTION_IMPULSE);
				countMoves++;
			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.CENTER_RECTANGLE_BOTTOM_HLAF)) {
				autoPilot.goBack(CORRECTION_IMPULSE);
//				Thread.sleep(200);
//				autoPilot.goBack(CORRECTION_IMPULSE);
//				Thread.sleep(200);
//				autoPilot.goBack(CORRECTION_IMPULSE);
				countMoves++;
			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.LEFT_RECTANGLE_UPPER_HLAF)) {
				if(countMoves%2==0){
					autoPilot.goLeft(CORRECTION_IMPULSE);
				} else {
					autoPilot.goForward(CORRECTION_IMPULSE);
				}
//				Thread.sleep(200);
//				autoPilot.goLeft(CORRECTION_IMPULSE);
//				autoPilot.goForward(CORRECTION_IMPULSE);
//				Thread.sleep(200);
//				autoPilot.goLeft(CORRECTION_IMPULSE);
//				autoPilot.goForward(CORRECTION_IMPULSE);
				countMoves++;
			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.LEFT_RECTANGLE_BOTTOM_HLAF)) {			
				if(countMoves%2==0){
					autoPilot.goLeft(CORRECTION_IMPULSE);
				} else {
					autoPilot.goBack(CORRECTION_IMPULSE);
				}
//				Thread.sleep(200);
//				autoPilot.goLeft(CORRECTION_IMPULSE);
//				autoPilot.goBack(CORRECTION_IMPULSE);
//				Thread.sleep(200);
//				autoPilot.goLeft(CORRECTION_IMPULSE);
//				autoPilot.goBack(CORRECTION_IMPULSE);
				countMoves++;
				
			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.RIGHT_RECTANGLE_UPPER_HLAF)) {
				if(countMoves%2==0){
					autoPilot.goRight(CORRECTION_IMPULSE);
				} else {
					autoPilot.goForward(CORRECTION_IMPULSE);
				}
				countMoves++;
//				Thread.sleep(200);
//				autoPilot.goRight(CORRECTION_IMPULSE);
//				autoPilot.goForward(CORRECTION_IMPULSE);
//				Thread.sleep(200);
//				autoPilot.goRight(CORRECTION_IMPULSE);
//				autoPilot.goForward(CORRECTION_IMPULSE);
			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.RIGHT_RECTANGLE_BOTTOM_HLAF)) {
				if(countMoves%2==0){
					autoPilot.goRight(CORRECTION_IMPULSE);
				} else {
					autoPilot.goBack(CORRECTION_IMPULSE);
				}
				countMoves++;
//				Thread.sleep(200);
//				autoPilot.goRight(CORRECTION_IMPULSE);
//				autoPilot.goBack(CORRECTION_IMPULSE);
//				Thread.sleep(200);
//				autoPilot.goRight(CORRECTION_IMPULSE);
//				autoPilot.goBack(CORRECTION_IMPULSE);
			}
		
		}
		catch (IOException e) {
			e.printStackTrace();
		} 

	}

}