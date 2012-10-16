package at.ppmrob.autopilot.state;

public interface IStateTransition {
	public void changeState(AutoPilotState state);
	public AutoPilotState getCurrentState();
}
