package at.ppmrob.autopilot.state;

import java.io.IOException;
import java.util.Timer;
public class TurnAroundState extends AutoPilotState {

	private static final int CHECK_LINE_INTERVAL = 500;
	private final float ROATION_IMPULSE = 0.05f; 
	private int rotationImpulses = 0;
	@Override
	public void internalHandling() {
		System.err.println("WARGL WE ARE IN TURN AROUND");
		try {
			//hardcoded
			if (rotationImpulses++ < 600 ){
				autoPilot.turnLeft(ROATION_IMPULSE);
			}
			// we will stop at this point
			else {
				autoPilot.getCheckCirclePosition().cancel();
				new Timer().scheduleAtFixedRate(autoPilot.getCheckLinePosition(), 0, CHECK_LINE_INTERVAL);
				//changeState(new DroneLandState());
				changeState(new DroneIsLostState());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
