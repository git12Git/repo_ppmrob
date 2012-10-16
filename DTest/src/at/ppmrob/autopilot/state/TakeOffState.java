package at.ppmrob.autopilot.state;

import java.io.IOException;
import java.util.Timer;

import at.ppmrob.autopilot.AutoPilot;

public class TakeOffState extends AutoPilotState {

	private static final int CHECK_CIRCLE_INTERVAL = 500;

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		//drone takeoff to 1.3+ meters

		//this.autoPilotState=AutoPilotState.DRONE_IS_FLYING;
		while(autoPilot.getDroneAltitude() <= 1.3){  // go to 1.3 meter height
			try {
				autoPilot.ascendDrone(0.1f);
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
		autoPilot.changeState(new TurnAroundState());
	//	autoPilot.timerCheckCircleOrLineLost.scheduleAtFixedRate(tt, 0, CHECK_CIRCLE_INTERVAL);

	}
}