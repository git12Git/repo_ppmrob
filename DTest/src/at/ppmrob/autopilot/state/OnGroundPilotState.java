package at.ppmrob.autopilot.state;

import at.ppmrob.autopilot.AutoPilotInformation;
import at.ppmrob.examples.main.AppWindows;

public class OnGroundPilotState extends AutoPilotState {

	private final Integer MAX_TAKEOFF_ATTEMPTS = 2;
	private Integer currentTakeOffAttempt = 0;
	
	@Override
	protected void internalHandling() {
		
	if (currentTakeOffAttempt > MAX_TAKEOFF_ATTEMPTS) {
			AppWindows.setDEBUGStateText("starting failed, shutting down");
			//TODO ChangeState ErrorTakeoff
			return;
		}
//		if (!autoPilot.isDroneFlying()) {
//			//AppWindows.setDEBUGCurrentCommandToDroneText("drone starting");
//			autoPilot.takeOff();
//			currentTakeOffAttempt++;
//			try { //weil takeoff dauert 3-4 sekunden
//				Thread.sleep(3000); //+1000 vom timer
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		else {
			AppWindows.setDEBUGStateText("change to take OFF");
			changeState(new TakeOffState());
//		}
		
	//autoPilot.changeState(new TakeOffState());		
		
		 
		AppWindows.setDEBUGStateText("change to take OFF");
	
	}
}
