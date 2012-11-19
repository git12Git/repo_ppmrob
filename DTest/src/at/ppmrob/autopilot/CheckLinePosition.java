package at.ppmrob.autopilot;

import java.util.TimerTask;

import at.ppmrob.autopilot.state.AutoPilotState;
import at.ppmrob.autopilot.state.DroneLandState;
import at.ppmrob.autopilot.state.FollowLineState;
import at.ppmrob.autopilot.state.IStateTransition;
import at.ppmrob.autopilot.state.LineSearchState;

public class CheckLinePosition extends TimerTask{

	private LineInformation linesFoundInformation;
	private CircleInformation circleInformation;
	private boolean isStartBullseyeLost = false;
	private boolean searchForFinishBullseye = false;
	private IStateTransition stateUpdate;
	private AutoPilotState previousState;
	public CheckLinePosition(IStateTransition stateUpdate, LineInformation linesFoundInformation, CircleInformation circleInformation) {
		super();
		this.linesFoundInformation = linesFoundInformation;
		this.circleInformation = circleInformation;
		this.stateUpdate = stateUpdate;
	}
	
	@Override
	public void run() {
		long timeNow = System.currentTimeMillis();
		linesFoundInformation.setLineFoundTimeDifference(timeNow - linesFoundInformation.getLineFoundTime());
		
		if(!linesFoundInformation.isDroneInWrongPosition() && stateUpdate.getCurrentState() instanceof LineSearchState){
			stateUpdate.changeState(previousState);
		}
		if(linesFoundInformation.isDroneInWrongPosition() && stateUpdate.getCurrentState() instanceof FollowLineState){
			previousState = stateUpdate.getCurrentState();
			stateUpdate.changeState(new LineSearchState());
		}
		if (linesFoundInformation.isDroneLost()) {
			stateUpdate.changeState(new DroneLandState());
		}
		
		//start bullseye lost
		if(circleInformation.isDroneOutsideRectangles() && !isStartBullseyeLost){  //
			isStartBullseyeLost=true;
			searchForFinishBullseye=true;
		} // finish bullseye found
		else if(!circleInformation.isDroneOutsideRectangles() && isStartBullseyeLost && searchForFinishBullseye){
			stateUpdate.changeState(new DroneLandState());
		}
		
//		if (linesFoundInformation.isDroneLost()) {
//			stateUpdate.changeState(new DroneLandState());
//		}
//		if (linesFoundInformation.isDroneOutsideRectangles()) {
//			previousState = stateUpdate.getCurrentState();
//			stateUpdate.changeState(new LineSearchState());
//		}
//		else {
//			stateUpdate.changeState(previousState);
//		}
		
	}
	
}
