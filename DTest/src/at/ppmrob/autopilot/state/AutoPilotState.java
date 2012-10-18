package at.ppmrob.autopilot.state;

import org.apache.log4j.Logger;

import at.ppmrob.autopilot.AutoPilot;
/*
public enum AutoPilotState {
	DRONE_ON_GROUND,
	DRONE_TAKEOFF,
	DRONE_IS_FLYING,
	DRONE_STAY_OVER_STARTING_CIRCLE,
	DRONE_STAY_OVER_LINE,
	DRONE_STAY_OVER_FINISH_CIRCLE,
	DRONE_TURN_360,
	DRONE_FOLLOW_LINE,
	DRONE_LAND,
	DRONE_LOST_CIRCLE,
	DRONE_LOST_LINE
}
*/

public abstract class AutoPilotState {
	
	protected AutoPilot autoPilot;
	protected Logger autoPilotLogger;
	
	public void changeState(AutoPilotState state) {
		autoPilot.changeState(state);
		autoPilotLogger.info("switching to state: " + state.getClass().getSimpleName());
	}

	
	public void handle() {
		autoPilotLogger.info("executing handler of state: " + getClass().getSimpleName());
		internalHandling();
	}
	protected abstract void internalHandling();

	public AutoPilotState() {
		super();
		autoPilotLogger = Logger.getLogger(getClass());
	}
	

	public void setAutoPilot(AutoPilot autoPilot) {
		this.autoPilot = autoPilot;
	}

	public AutoPilot getAutoPilot() {
		return autoPilot;
	}

}