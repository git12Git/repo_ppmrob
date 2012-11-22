package at.ppmrob.autopilot;

import java.io.IOException;
import java.util.TimerTask;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import at.ppmrob.autopilot.state.AutoPilotState;
import at.ppmrob.autopilot.state.CircleSearchState;
import at.ppmrob.autopilot.state.DroneIsLostState;
import at.ppmrob.autopilot.state.IStateTransition;
import at.ppmrob.autopilot.state.OnGroundPilotState;
import at.ppmrob.examples.main.AppWindows;
import at.ppmrob.examples.main.LastKnownCircleLinePosition;
public class CheckCirclePosition extends TimerTask {

	
	private CircleInformation circlesFoundInformation;
	private IStateTransition stateUpdate;
	private AutoPilotState restoreState;
	
	private final float CORRECTION_IMPULSE = 0.009f;

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


		// when inside circles, optionally corect position
		if (!circlesFoundInformation.isDroneOutsideRectangles()) {
			try {
				correctPosition();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void correctPosition() throws IOException {
		AutoPilot pilot = stateUpdate.getCurrentState().getAutoPilot();
		switch(circlesFoundInformation.getLastKnownCirclePosition()) {
		case LEFT_RECTANGLE_UPPER_HLAF:
			pilot.goLeft(CORRECTION_IMPULSE);
			pilot.goForward(0.006f);
			break;
		case LEFT_RECTANGLE_BOTTOM_HLAF:
			pilot.goLeft(CORRECTION_IMPULSE);
			pilot.goBack(0.006f);
			break;
		case RIGHT_RECTANGLE_UPPER_HLAF:
			pilot.goRight(CORRECTION_IMPULSE);
			pilot.goForward(0.006f);
			break;
		case RIGHT_RECTANGLE_BOTTOM_HLAF:
			pilot.goRight(CORRECTION_IMPULSE);
			pilot.goBack(0.006f);
			break;
		}
	}


}
