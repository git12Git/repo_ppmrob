package at.ppmrob.autopilot.state;

import java.io.IOException;
import java.util.Timer;

import at.ppmrob.autopilot.CircleInformation;
import at.ppmrob.autopilot.LineInformation;
import at.ppmrob.examples.main.LastKnownCircleLinePosition;

public class FollowLineState extends AutoPilotState {

	private boolean isTimerStarted=false;
	
	@Override
	protected void internalHandling() {
		try {
			LineInformation lineInformation = autoPilot
					.getFoundLineInformation();
			CircleInformation circleInformation = autoPilot
					.getFoundCirclesInformation();

			autoPilot.goForward(0.05f);

			if (!isTimerStarted) {
				new Timer().scheduleAtFixedRate(
						autoPilot.getCheckLinePosition(), 0, 100);
				isTimerStarted = true;
			}

			if (lineInformation.getLastKnownLinePosition().equals(
					LastKnownCircleLinePosition.RIGHT_UP_AND_BOTTOM)) {
				autoPilot.goRight(0.1f);
			} else if (lineInformation.getLastKnownLinePosition().equals(
					LastKnownCircleLinePosition.LEFT_UP_AND_BOTTOM)) {
				autoPilot.goLeft(0.1f);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
