package at.ppmrob.autopilot;

import java.util.TimerTask;

import at.ppmrob.autopilot.state.AutoPilotState;
import at.ppmrob.autopilot.state.CircleSearchState;
import at.ppmrob.autopilot.state.DroneIsLostState;
import at.ppmrob.autopilot.state.IStateTransition;
import at.ppmrob.examples.main.AppWindows;
public class CheckCirclePosition extends TimerTask {

	private CircleInformation circlesFoundInformation;
	private IStateTransition stateUpdate;
	private AutoPilotState previousState;
	
	public CheckCirclePosition(IStateTransition stateUpdate, CircleInformation circlesFoundInformation) {
		super();
		this.circlesFoundInformation = circlesFoundInformation;
		this.stateUpdate = stateUpdate;
		previousState = stateUpdate.getCurrentState();
	}
	@Override
	public void run() {
		
		long timeNow = System.currentTimeMillis();
		
		circlesFoundInformation.setCircleFoundTimeDifference(timeNow-circlesFoundInformation.getCircleFoundTime());
		AppWindows.setDEBUGCurrentCommandToDroneText(new Long(circlesFoundInformation.getCircleFoundTimeDifference()).toString());
		if (circlesFoundInformation.isDroneLost()) {
			if (previousState != stateUpdate.getCurrentState()) {
				stateUpdate.changeState(new DroneIsLostState());
				previousState = stateUpdate.getCurrentState();
			}
		} 
		else if(circlesFoundInformation.isDroneOutsideRectangles()) {
			if (previousState != stateUpdate.getCurrentState()) {
				AppWindows.setDEBUGCurrentCommandToDroneText("store previous state: " + previousState.getClass().getSimpleName());
				stateUpdate.changeState(new CircleSearchState());
				previousState = stateUpdate.getCurrentState();
			}
			
		}
		else {
			stateUpdate.changeState(previousState);
		}
		
				
	}

	
}
