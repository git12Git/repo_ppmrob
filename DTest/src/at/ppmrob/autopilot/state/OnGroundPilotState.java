package at.ppmrob.autopilot.state;

public class OnGroundPilotState extends AutoPilotState {

	private final Integer MAX_TAKEOFF_ATTEMPTS = 1;
	private Integer currentTakeOffAttempt = 0;
	
	@Override
	protected void internalHandling() {
		
		if (currentTakeOffAttempt > MAX_TAKEOFF_ATTEMPTS) {
			//TODO ChangeState ErrorTakeoff
			return;
		}
		if (!autoPilot.isDroneFlying()) {
			autoPilot.takeOff();
			currentTakeOffAttempt++;
		}
		else {
			changeState(new TakeOffState());
		}
		//pilot.ChangeState(new TakeOffState(pilot));		
	}
}
