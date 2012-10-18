package at.ppmrob.autopilot.state;

import java.io.IOException;

import at.ppmrob.autopilot.CircleInformation;
import at.ppmrob.examples.main.LastKnownCircleLinePosition;

public class CircleSearchState extends AutoPilotState {

	private int countMoves = 0;

	@Override
	protected void internalHandling() {

		CircleInformation circlesFoundInformation = autoPilot.getFoundCirclesInformation();
	
		LastKnownCircleLinePosition lastKnownPosition = circlesFoundInformation.getLastKnownCirclePosition();
		
		try {
			if (lastKnownPosition.equals(LastKnownCircleLinePosition.CENTER_RECTANGLE_UPPER_HLAF)) {
				autoPilot.goForward(0.05f);
				countMoves++;
			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.CENTER_RECTANGLE_BOTTOM_HLAF)) {
				autoPilot.goBack(0.05f);
				countMoves++;
			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.LEFT_RECTANGLE_UPPER_HLAF)) {
				autoPilot.goLeft(0.05f);
				autoPilot.goForward(0.05f);
				countMoves++;
			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.LEFT_RECTANGLE_BOTTOM_HLAF)) {
				autoPilot.goLeft(0.05f);
				autoPilot.goBack(0.05f);
				countMoves++;
			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.RIGHT_RECTANGLE_UPPER_HLAF)) {
				autoPilot.goRight(0.05f);
				autoPilot.goForward(0.05f);

			}
			else if (lastKnownPosition.equals(LastKnownCircleLinePosition.RIGHT_RECTANGLE_BOTTOM_HLAF)) {
				autoPilot.goRight(0.05f);
				autoPilot.goBack(0.05f);
			}
		
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		countMoves++;




	}

}