package at.ppmrob.autopilot.state;

import java.io.IOException;

public class DroneLandState extends AutoPilotState {

	@Override
	public void internalHandling() {
		// TODO Auto-generated method stub
		try {
			autoPilot.getCheckLinePosition().cancel();
			autoPilot.landDrone();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
