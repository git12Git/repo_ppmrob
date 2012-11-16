package at.ppmrob.autopilot.state;

import at.ppmrob.autopilot.AutoPilotInformation;
import at.ppmrob.examples.main.AppWindows;

public class OnGroundPilotState extends AutoPilotState {

	private final Integer MAX_TAKEOFF_ATTEMPTS = 2;
	private Integer currentTakeOffAttempt = 0;
	private boolean takeOffCmdSent = false;
	private int milisecCounter; //timer @200ms
	
	@Override
	protected void internalHandling() {
		
	if (currentTakeOffAttempt > MAX_TAKEOFF_ATTEMPTS) {
			AppWindows.setDEBUGStateText("starting failed, shutting down");
			//TODO ChangeState ErrorTakeoff
			return;
		}
	
	if (!takeOffCmdSent) {
		//AppWindows.setDEBUGCurrentCommandToDroneText("drone starting");
		autoPilot.takeOff();
		currentTakeOffAttempt++;
		milisecCounter++;
		takeOffCmdSent=true;
	} else {
		milisecCounter++;
		if(milisecCounter%15==0){ //15*200ms
			if(autoPilot.isDroneFlying()){
				AppWindows.setDEBUGStateText("change to take OFF");
				changeState(new TakeOffState());
			} else {
				takeOffCmdSent=false;
				milisecCounter=0;
			}
		}
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
//			AppWindows.setDEBUGStateText("change to take OFF");
//			changeState(new TakeOffState());
//		}
	
	}
}
