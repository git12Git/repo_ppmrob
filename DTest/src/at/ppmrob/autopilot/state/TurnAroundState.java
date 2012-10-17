package at.ppmrob.autopilot.state;

import java.io.IOException;
import java.util.Timer;
public class TurnAroundState extends AutoPilotState {

	private static final int CHECK_LINE_INTERVAL = 500;
	private int rotationImpulses = 0;
	@Override
	public void handle() {
		try {
			//hardcoded
			if (rotationImpulses++ < 300 ){
				autoPilot.turnLeft(0.05f);
			}
			// we will stop at this point
			else {
				autoPilot.getCheckCirclePosition().cancel();
				new Timer().scheduleAtFixedRate(autoPilot.getCheckLinePosition(), 0, CHECK_LINE_INTERVAL);
				autoPilot.changeState(new DroneLandState());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
