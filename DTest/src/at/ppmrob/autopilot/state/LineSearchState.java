package at.ppmrob.autopilot.state;

import at.ppmrob.autopilot.LineInformation;

public class LineSearchState extends AutoPilotState {

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		//LineInformation: load lineinformation from autopilot
		// store last known position
		
		// if drone in left redzone 
		// move left
		// inc. movecount
		
		// if drone in right rectangle
		// move right
		// inc. movecount
		
		LineInformation linesFoundInformation = autoPilot.getFoundLineInformation();
//		LastKnownCircleLinePosition lastKnownPosition = linesFoundInformation.get
		
		// TODO similar to circlesearchstate


	}

}
