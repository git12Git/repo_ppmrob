package at.ppmrob.examples.mock;

import java.util.List;

import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.VisionTag;

public class NavDataMOCK extends NavData {
	
	public NavDataMOCK(){
		this.ctrl_state = NavData.CtrlState.FLYING;
	}
	public void setDroneAltitude(float droneAltitude) {
		this.altitude=droneAltitude;
	}
	public void setDroneBattery(int droneBattery) {
		this.battery = droneBattery;
	}
	public void setDroneYaw(float droneYaw) {
		this.yaw = droneYaw;
	}
	public void setDroneIsFlying(boolean droneIsFlying) {
		this.flying = droneIsFlying;
	}
	public void setDroneIsBatteryTooHigh(boolean droneIsBatteryTooHigh) {
		this.batteryTooHigh = droneIsBatteryTooHigh;
	}
	public void setDroneIsBatteryTooLow(boolean droneIsBatteryTooLow) {
		this.batteryTooLow = droneIsBatteryTooLow;
	}
	public void setDroneIsEmergency(boolean droneIsEmergency) {
		this.emergency = droneIsEmergency;
	}

	
}
