package at.ppmrob.autopilot.state;

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
	
	public void ChangeState(AutoPilotState state) {
		autoPilot.changeState(state);
	}
	
	public abstract void handle();

	public AutoPilotState() {
		super();
	}
	

	public void setAutoPilot(AutoPilot autoPilot) {
		this.autoPilot = autoPilot;
	}

	public AutoPilot getAutoPilot() {
		return autoPilot;
	}

}