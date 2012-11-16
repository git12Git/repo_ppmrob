package at.ppmrob.autopilot;

import java.util.TimerTask;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import at.ppmrob.autopilot.state.AutoPilotState;
import at.ppmrob.autopilot.state.CircleSearchState;
import at.ppmrob.autopilot.state.DroneIsLostState;
import at.ppmrob.autopilot.state.IStateTransition;
import at.ppmrob.autopilot.state.OnGroundPilotState;
import at.ppmrob.examples.main.AppWindows;
public class CheckCirclePosition extends TimerTask {

	private CircleInformation circlesFoundInformation;
	private IStateTransition stateUpdate;
	private AutoPilotState restoreState;
	
	public CheckCirclePosition(IStateTransition stateUpdate, CircleInformation circlesFoundInformation) {
		super();
		this.circlesFoundInformation = circlesFoundInformation;
		this.stateUpdate = stateUpdate;
		restoreState = stateUpdate.getCurrentState();
	}
	@Override
	public void run() {
		
		long timeNow = System.currentTimeMillis();
		
		circlesFoundInformation.setCircleFoundTimeDifference(timeNow-circlesFoundInformation.getCircleFoundTime());
		
		if (circlesFoundInformation.isDroneOutsideRectangles() && !(stateUpdate.getCurrentState() instanceof CircleSearchState)) {
			//AppWindows.setDEBUGCurrentCommandToDroneText("store previous state: " + restoreState.getClass().getSimpleName());
			restoreState = stateUpdate.getCurrentState();
			stateUpdate.changeState(new CircleSearchState());
		}
		
		if (circlesFoundInformation.isDroneLost() && !(stateUpdate.getCurrentState() instanceof DroneIsLostState)) {
				restoreState = stateUpdate.getCurrentState();
				stateUpdate.changeState(new DroneIsLostState());
		} 
		if (circlesFoundInformation.isDroneLost()) {
			stateUpdate.changeState(new DroneIsLostState());
			return;
		}

		
		if (!(circlesFoundInformation.isDroneOutsideRectangles() || circlesFoundInformation.isDroneLost() || restoreState == stateUpdate.getCurrentState()))
		{
			if(!(restoreState instanceof OnGroundPilotState))
			stateUpdate.changeState(restoreState);
		}
		
	
				
	}

	
}
