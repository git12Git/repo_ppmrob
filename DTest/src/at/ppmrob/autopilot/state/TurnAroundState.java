package at.ppmrob.autopilot.state;

import java.io.IOException;
import java.util.Timer;

import at.ppmrob.examples.main.AppWindows;
public class TurnAroundState extends AutoPilotState {

	private static final int CHECK_LINE_INTERVAL = 500;
	private final float ROATION_IMPULSE = 0.009f; 
	private int rotationImpulses = 0;
	boolean timerStarted = false;
	@Override
	public void internalHandling() {
		System.err.println("WARGL WE ARE IN TURN AROUND");
		try {
			//hardcoded
			if (rotationImpulses++ < 120){//70 ){
				autoPilot.turnLeft(ROATION_IMPULSE);
				AppWindows.setDEBUGTurnValue(""+rotationImpulses);
			}
			// we will stop at this point
			else {
				if(!timerStarted) {
					autoPilot.getCheckCirclePosition().cancel();
//					new Timer().scheduleAtFixedRate(autoPilot.getCheckLinePosition(), 0, CHECK_LINE_INTERVAL);
//					new Timer().schedule(autoPilot.getCheckLinePosition(), 0, CHECK_LINE_INTERVAL);
					//changeState(new DroneLandState());
					changeState(new FollowLineState());
					timerStarted=true;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
