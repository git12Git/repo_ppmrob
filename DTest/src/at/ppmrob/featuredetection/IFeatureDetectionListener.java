package at.ppmrob.featuredetection;

import java.util.Vector;

/**
 * 
 * im videopanelcustom wird ja die FeatureDetection schon aufgerufen und alle circles und lines berechnet
 * 
 *deshalb sollten die anderen klassen wie autopilot sich als listener registrieren so das wir nicht unötig mehrmals
 *circles und lines berechnen
 */
public interface IFeatureDetectionListener {
	
	public void foundCircles(Vector<MyCircle> circles);
	public void foundLines(Vector<MyLine> lines);
	public void noCirclesFound();
}
