package at.ppmrob.autopilot.state;

import java.io.IOException;


public class DroneIsLostState extends AutoPilotState {

	@Override
	public void internalHandling() {
		try {
			autoPilot.landDrone();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("error circles completely lost");
	}

}
