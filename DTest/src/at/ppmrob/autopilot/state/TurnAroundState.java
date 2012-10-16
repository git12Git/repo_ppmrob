package at.ppmrob.autopilot.state;

import java.io.IOException;

public class TurnAroundState extends AutoPilotState {

	private int rotationImpulses = 0;
	@Override
	public void handle() {
		try {
			//hardcoded
			if (rotationImpulses++ < 300 ){
				autoPilot.turnLeft(0.05f);
			}
			else {
				autoPilot.changeState(new DroneLandState());
			}
			
			// todo disable CheckCirclePosition timertask
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
