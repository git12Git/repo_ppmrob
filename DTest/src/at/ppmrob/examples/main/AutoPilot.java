package at.ppmrob.examples.main;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.DroneVideoListener;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;

public class AutoPilot implements DroneVideoListener, NavDataListener{

	private ARDrone drone;
	
	@Override
	public void navDataReceived(NavData nd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameReceived(int startX, int startY, int w, int h,
			int[] rgbArray, int offset, int scansize) {
		// TODO Auto-generated method stub
		
	}

	public AutoPilot(ARDrone drone) {
		super();
		this.drone = drone;
	}
	
	
}
