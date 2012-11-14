package at.ppmrob.autopilot.state;

import java.io.IOException;
import java.util.Timer;

import at.ppmrob.examples.main.AppWindows;

public class TakeOffState extends AutoPilotState {

	private static final float ASCEND_STEP_SIZE = 0.05f;
	private static final double OPERATING_HEIGHT = 0.8;
	private static final int CHECK_CIRCLE_INTERVAL = 500;
	private boolean circlePositionCheckScheduled = false;

	@Override
	public void internalHandling() {
		// TODO Auto-generated method stub
		//drone takeoff to 1.3+ meters

		//this.autoPilotState=AutoPilotState.DRONE_IS_FLYING;
		
		if (!circlePositionCheckScheduled) {
			new Timer().schedule(autoPilot.getCheckCirclePosition(), 0, CHECK_CIRCLE_INTERVAL);
		}
			while(autoPilot.getDroneAltitude() <= OPERATING_HEIGHT){  // go to 1.3 meter height
			AppWindows.setDEBUGStateText("Take OFF");
			try {
				autoPilot.ascendDrone(ASCEND_STEP_SIZE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
		
		
		changeState(new TurnAroundState());
	//	autoPilot.timerCheckCircleOrLineLost.scheduleAtFixedRate(tt, 0, CHECK_CIRCLE_INTERVAL);

	}
}