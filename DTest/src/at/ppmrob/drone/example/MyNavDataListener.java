package at.ppmrob.drone.example;

import org.apache.log4j.Logger;

import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;

public class MyNavDataListener implements NavDataListener{

    @Override
    public void navDataReceived(NavData nd)
    {
        Logger.getLogger(getClass().getName()).debug("navDataReceived()");
//        updateBatteryStatus(nd.getBattery());
//        this.flying.set(nd.isFlying());
       
    }

}
