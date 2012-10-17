package at.ppmrob.autopilot;

import java.util.TimerTask;

import at.ppmrob.autopilot.state.AutoPilotState;
import at.ppmrob.autopilot.state.DroneLandState;
import at.ppmrob.autopilot.state.IStateTransition;
import at.ppmrob.autopilot.state.LineSearchState;

public class CheckLinePosition extends TimerTask{

	private LineInformation linesFoundInformation;
	private IStateTransition stateUpdate;
	private AutoPilotState previousState;
	
	public CheckLinePosition(IStateTransition stateUpdate, LineInformation linesFoundInformation) {
		super();
		this.linesFoundInformation = linesFoundInformation;
		this.stateUpdate = stateUpdate;
	}
	
	@Override
	public void run() {
		long timeNow = System.currentTimeMillis();
		linesFoundInformation.setLineFoundTimeDifference(timeNow - linesFoundInformation.getLineFoundTime());
		if (linesFoundInformation.isDroneLost()) {
			stateUpdate.changeState(new DroneLandState());
		}
		if (linesFoundInformation.isDroneOutsideRectangles()) {
			previousState = stateUpdate.getCurrentState();
			stateUpdate.changeState(new LineSearchState());
		}
		else {
			stateUpdate.changeState(previousState);
		}
		
	}
	
}
