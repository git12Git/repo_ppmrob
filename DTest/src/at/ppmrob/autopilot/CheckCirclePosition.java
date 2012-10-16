package at.ppmrob.autopilot;

import java.io.IOException;
import java.util.TimerTask;

import at.ppmrob.autopilot.state.AutoPilotState;
import at.ppmrob.autopilot.state.CircleSearchState;
import at.ppmrob.autopilot.state.DroneIsLostState;
import at.ppmrob.autopilot.state.IStateTransition;
import at.ppmrob.examples.main.LastKnownCircleLinePosition;

public class CheckCirclePosition extends TimerTask {

	private CircleInformation circlesFoundInformation;
	private IStateTransition stateUpdate;
	private AutoPilotState previousState;
	
	public CheckCirclePosition(IStateTransition stateUpdate, CircleInformation circlesFoundInformation) {
		super();
		this.circlesFoundInformation = circlesFoundInformation;
		this.stateUpdate = stateUpdate;
	}
	@Override
	public void run() {
		
		long timeNow = System.currentTimeMillis();
		circlesFoundInformation.setCircleFoundTimeDifference(timeNow-circlesFoundInformation.getCircleFoundTime());
		if (circlesFoundInformation.isDroneLost()) {
			stateUpdate.changeState(new DroneIsLostState());
		} 
		if(circlesFoundInformation.isDroneOutsideRectangles()) {
			previousState = stateUpdate.getCurrentState();
			stateUpdate.changeState(new CircleSearchState());
		}
		else {
			stateUpdate.changeState(previousState);
		}
		
				
	}

	
}
