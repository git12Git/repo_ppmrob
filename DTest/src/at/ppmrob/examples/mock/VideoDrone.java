package at.ppmrob.examples.mock;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.ARDrone.Animation;
import com.codeminders.ardrone.ARDrone.ConfigOption;
import com.codeminders.ardrone.ARDrone.LED;
import com.codeminders.ardrone.ARDrone.VideoChannel;
import com.codeminders.ardrone.DroneStatusChangeListener;
import com.codeminders.ardrone.DroneVideoListener;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.OpenCVFrameGrabber;

public class VideoDrone extends ARDrone{

	private Vector<DroneVideoListener> videoListeners = new Vector<DroneVideoListener>();
	private Vector<NavDataListener> navDataListeners = new Vector<NavDataListener>();
	private FrameGrabber grabber = new OpenCVFrameGrabber("video/v1.mp4"); 
	private IplImage img;
	private NavDataMOCK navData = new NavDataMOCK();
	
	public VideoDrone() throws UnknownHostException {
		
	}
	
	
	

	@Override
	public void addImageListener(DroneVideoListener l) {
		this.videoListeners.add(l);
	}

	@Override
	public void addNavDataListener(NavDataListener l) {
		this.navDataListeners.add(l);
	}
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public void removeImageListener(DroneVideoListener l) {}



	@Override
	public void clearImageListeners() {}



	@Override
	public void addStatusChangeListener(DroneStatusChangeListener l) {}



	@Override
	public void removeStatusChangeListener(DroneStatusChangeListener l) {}



	@Override
	public void clearStatusChangeListeners() {}



	@Override
	public void removeNavDataListener(NavDataListener l) {}



	@Override
	public void clearNavDataListeners() {}



	@Override
	public void changeToErrorState(java.lang.Exception ex) {}



	@Override
	public void clearEmergencySignal() throws IOException {}



	@Override
	public void connect() throws IOException {
		
		new Thread(){

			@Override
			public void run() {
				
				try {
					grabber.start();
					while(true){
						img = grabber.grab();
						for(DroneVideoListener d:videoListeners){
							d.frameReceived(0, 0, img.width(), img.height(), 
									img.getBufferedImage().getRGB(0, 0, img.width(), img.height(), null, 0, img.width()), 0, img.width());
						}
						for(NavDataListener n:navDataListeners){
							n.navDataReceived(navData);
						}
						Thread.sleep(70);
					}
//					grabber.stop();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}.start();
	}



	@Override
	public void disableAutomaticVideoBitrate() throws IOException {}



	@Override
	public void disconnect() throws IOException {}



	@Override
	public void enableAutomaticVideoBitrate() throws IOException {}



	@Override
	public void hover() throws IOException {}



	@Override
	public boolean isCombinedYawMode() {return false;}



	@Override
	public boolean isEmergencyMode() {return false;}



	@Override
	public void land() throws IOException {}



	@Override
	public void move(float left_right_tilt, float front_back_tilt,
			float vertical_speed, float angular_speed) throws IOException {}



	@Override
	public void navDataReceived(NavData nd) {}



	@Override
	public void playAnimation(int animation_no, int duration)
			throws IOException {}



	@Override
	public void playAnimation(Animation animation, int duration)
			throws IOException {}



	@Override
	public void playLED(int animation_no, float freq, int duration)
			throws IOException {}



	@Override
	public void playLED(LED animation, float freq, int duration)
			throws IOException {}



	@Override
	public void selectVideoChannel(VideoChannel c) throws IOException {}



	@Override
	public void sendAllNavigationData() throws IOException {
		// TODO Auto-generated method stub
		super.sendAllNavigationData();
	}



	@Override
	public void sendDemoNavigationData() throws IOException {}



	@Override
	public void sendEmergencySignal() throws IOException {}



	@Override
	public void setCombinedYawMode(boolean combinedYawMode) {}



	@Override
	public void setConfigOption(String name, String value) throws IOException {}



	@Override
	public void setConfigOption(ConfigOption option, String value)
			throws IOException {	}



	@Override
	public void takeOff() throws IOException {
		this.navData.setDroneIsFlying(true);
		this.navData.setDroneAltitude(0.56f);
	}



	@Override
	public void trim() throws IOException {}



	@Override
	public void videoFrameReceived(int startX, int startY, int w, int h,
			int[] rgbArray, int offset, int scansize) {}



	@Override
	public void waitForReady(long how_long) throws IOException {}
}
