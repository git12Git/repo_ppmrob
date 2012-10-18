package at.ppmrob.autopilot.state;

import java.io.IOException;
import java.util.Timer;

public class TakeOffState extends AutoPilotState {

	private static final float ASCEND_STEP_SIZE = 0.05f;
	private static final double OPERATING_HEIGHT = 1.5;
	private static final int CHECK_CIRCLE_INTERVAL = 500;

	@Override
	public void internalHandling() {
		// TODO Auto-generated method stub
		//drone takeoff to 1.3+ meters

		//this.autoPilotState=AutoPilotState.DRONE_IS_FLYING;
		while(autoPilot.getDroneAltitude() <= OPERATING_HEIGHT){  // go to 1.3 meter height
			try {
				autoPilot.ascendDrone(ASCEND_STEP_SIZE);
				Thread.sleep(500);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		new Timer().scheduleAtFixedRate(autoPilot.getCheckCirclePosition(), 0, CHECK_CIRCLE_INTERVAL);
		changeState(new TurnAroundState());
	//	autoPilot.timerCheckCircleOrLineLost.scheduleAtFixedRate(tt, 0, CHECK_CIRCLE_INTERVAL);

	}
}