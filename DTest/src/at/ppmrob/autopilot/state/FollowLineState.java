package at.ppmrob.autopilot.state;

import java.io.IOException;

import at.ppmrob.autopilot.CircleInformation;

public class FollowLineState extends AutoPilotState {

	private int lineFollowImpulses = 0;

	@Override
	protected void internalHandling() {
		try {
			CircleInformation circleInformation = autoPilot.getFoundCirclesInformation();
			if (lineFollowImpulses++ < 8) {
				autoPilot.goForward(0.05f);
			}

			// probably found the bullseye by now
			else if (circleInformation.getCircleFoundTimeDifference() <= 4000) {
				//TODO don't know yet how to do this
				//autoPilot.changeState(new DroneLandState());
			}

			if(lineFollowImpulses>20){
				// should disable autopilot
				changeState(new DroneLandState());
			}
		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
