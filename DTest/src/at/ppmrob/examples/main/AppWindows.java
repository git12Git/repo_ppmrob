package at.ppmrob.examples.main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import at.ppmrob.autopilot.AutoPilot;
import at.ppmrob.autopilot.AutoPilotInformation;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.DroneStatusChangeListener;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.ARDrone.VideoChannel;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;

public class AppWindows implements IAutoPilotDataInformationListener {

	private JFrame frame;

	private AutoPilot autoPilot;
	private Thread autoPilotStarter;
	private ARDrone arDrone;
	private final int CONNECT_TIMEOUT = 2000;
	private VideoPanelCustom videoPanelDrone;
	private NavData navDataFromDrone = new NavData();
	public static final Integer AUTOPILOT_TASK_FREQUENCY = 1000;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppWindows window = new AppWindows();
					window.frame.setVisible(true);
					window.frame.setFocusable(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AppWindows() {
		initialize();
		initDrone();
		initClosingListener();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		videoPanelDrone = new VideoPanelCustom();
		frame.getContentPane().add(videoPanelDrone, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		initActionlistener();
		
		JButton btnA = new JButton("A");
		
		JButton btnD = new JButton("D");
		
		JButton btnVv = new JButton("S");
		btnVv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JButton btnNewButton = new JButton("W");
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(18)
					.addComponent(btnA)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnVv)
							.addGap(18)
							.addComponent(btnD)))
					.addGap(387))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnA)
						.addComponent(btnVv)
						.addComponent(btnD)))
		);
		panel.setLayout(gl_panel);
	}
	
	
	
	public void initActionlistener(){
		this.frame.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("typed:"+e.getKeyCode());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				 float left_tilt = -0.1f;
				 float right_tilt = 0.1f;
				 //forward NEED TO BE MINUS
                 float front_tilt = -0.1f;
                 float back_tilt = 0.1f;
                 
                 float vertical_speed_up = 0.1f;
                 float vertical_speed_down = -0.1f;
                 
                 float angular_speed_right = 0.3f;
                 float angular_speed_left = -0.3f;
                 
				System.out.println("released:"+e.getKeyCode());
				int released = e.getKeyCode();
				
				try {
						switch(released){
						case KeyEvent.VK_ENTER:
								AppWindows.this.arDrone.takeOff();
							break;
						case KeyEvent.VK_SPACE:
								AppWindows.this.arDrone.land();
								AppWindows.this.arDrone.sendEmergencySignal();
							break;
		/*				case KeyEvent.VK_P:  // AutoPilot test
							autoPilot.getFeatures(videoPanelDrone.detectedLines,
									videoPanelDrone.detectedCircles);
							autoPilot.moveAlongLines();
							autoPilot.stayInMiddle();
							break;
		*/
						case KeyEvent.VK_W:   // go forward
							AppWindows.this.arDrone.move(0.0f, front_tilt, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_S:  // go backward
							AppWindows.this.arDrone.move(0.0f, back_tilt, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_A:  // go left
							AppWindows.this.arDrone.move(left_tilt, 0.0f, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_D:   // go right
							AppWindows.this.arDrone.move(right_tilt, 0.0f, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_UP:   // go foreward
							AppWindows.this.arDrone.move(0.0f, front_tilt, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_DOWN: // go backward
							AppWindows.this.arDrone.move(0.0f, back_tilt, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_LEFT:  // go left
							AppWindows.this.arDrone.move(left_tilt, 0.0f, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_RIGHT:	// go right
							AppWindows.this.arDrone.move(right_tilt, 0.0f, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_X:	// turn right
							AppWindows.this.arDrone.move(0.0f, 0.0f, 0.0f, angular_speed_right);
							break;
						case KeyEvent.VK_Y:	// turn left
							AppWindows.this.arDrone.move(0.0f, 0.0f, 0.0f, angular_speed_left);
							break;
						case KeyEvent.VK_H:	// go up
							AppWindows.this.arDrone.move(0.0f, 0.0f, vertical_speed_up, 0.0f);
							break;
						case KeyEvent.VK_L:	//go down
							AppWindows.this.arDrone.move(0.0f, 0.0f, vertical_speed_down, 0.0f);
							break;
						case KeyEvent.VK_5:
							AppWindows.this.arDrone.selectVideoChannel(VideoChannel.HORIZONTAL_ONLY);
							break;
						case KeyEvent.VK_6:
							AppWindows.this.arDrone.selectVideoChannel(VideoChannel.VERTICAL_ONLY);
							break;
						case KeyEvent.VK_7:
							AppWindows.this.arDrone.selectVideoChannel(VideoChannel.HORIZONTAL_IN_VERTICAL);
							break;
						case KeyEvent.VK_F1:
							new Timer().schedule(autoPilot, 1000);
							//AppWindows.this.autoPilotStarter.start();
							break;
						case KeyEvent.VK_F2:
							AppWindows.this.autoPilotStarter.interrupt();
							break;
						case KeyEvent.VK_8:
							break;
						case KeyEvent.VK_F12:
							for(int i=0; i<6; i++){
								AppWindows.this.arDrone.move(0.0f, 0.0f, 0.0f, angular_speed_left); //turn left
								try {
									Thread.sleep(200);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							break;
						}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				 float left_tilt = -0.1f;
				 float right_tilt = 0.1f;
				 
				 //forward NEED TO BE MINUS
                float front_tilt = -0.1f;
                float back_tilt = 0.1f;
                
                float vertical_speed_up = 0.1f;
                float vertical_speed_down = -0.1f;
                
                float angular_speed_right = 0.15f;
                float angular_speed_left = -0.15f;
                
				System.out.println("released:"+e.getKeyCode());
				int pressed = e.getKeyCode();
				
				try {
						switch(pressed){
						case KeyEvent.VK_ENTER:
								AppWindows.this.arDrone.takeOff();
							break;
						case KeyEvent.VK_SPACE:
								AppWindows.this.arDrone.land();
								AppWindows.this.arDrone.sendEmergencySignal();
							break;
							/*
						case KeyEvent.VK_P:  // AutoPilot test
							autoPilot.getFeatures(videoPanelDrone.detectedLines,
									videoPanelDrone.detectedCircles);
							autoPilot.moveAlongLines();
							autoPilot.stayInMiddle();
							break;
							*/
						case KeyEvent.VK_W:   // go forward
							AppWindows.this.arDrone.move(0.0f, front_tilt, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_S:  // go backward
							AppWindows.this.arDrone.move(0.0f, back_tilt, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_A:  // go left
							AppWindows.this.arDrone.move(left_tilt, 0.0f, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_D:   // go right
							AppWindows.this.arDrone.move(right_tilt, 0.0f, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_UP:   // go foreward
							AppWindows.this.arDrone.move(0.0f, front_tilt, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_DOWN: // go backward
							AppWindows.this.arDrone.move(0.0f, back_tilt, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_LEFT:  // go left
							AppWindows.this.arDrone.move(left_tilt, 0.0f, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_RIGHT:	// go right
							AppWindows.this.arDrone.move(right_tilt, 0.0f, 0.0f, 0.0f);
							break;
						case KeyEvent.VK_X:	// turn right
							AppWindows.this.arDrone.move(0.0f, 0.0f, 0.0f, angular_speed_right);
							break;
						case KeyEvent.VK_Y:	// turn left
							AppWindows.this.arDrone.move(0.0f, 0.0f, 0.0f, angular_speed_left);
							break;
						case KeyEvent.VK_H:	// go up
							AppWindows.this.arDrone.move(0.0f, 0.0f, vertical_speed_up, 0.0f);
							break;
						case KeyEvent.VK_L:	//go down
							AppWindows.this.arDrone.move(0.0f, 0.0f, vertical_speed_down, 0.0f);
							break;
						}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	
	
	public void initClosingListener(){

		frame.addWindowListener(new WindowListener() {
		
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				try {
					arDrone.disconnect();
					System.out.println("DISCONNECTED");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void initDrone(){
		try {
		this.arDrone = new ARDrone();
		this.arDrone.connect();
		this.arDrone.clearEmergencySignal();
        // Wait until drone is ready
		this.arDrone.waitForReady(CONNECT_TIMEOUT);
        // do TRIM operation
		this.arDrone.trim();
		this.arDrone.addImageListener(this.videoPanelDrone);
//        this.videoPanelDrone.setDrone(arDrone);
		this.arDrone.selectVideoChannel(VideoChannel.HORIZONTAL_ONLY);
		
		/*this.arDrone.addNavDataListener(new NavDataListener() {
			
			@Override
			public void navDataReceived(NavData nd) {
				synchronized(AppWindows.this.navDataFromDrone){
					
					//this can be use later with UAV operations
					AppWindows.this.navDataFromDrone = nd;
				}
			}
		});
		*/
		autoPilot = new AutoPilot(this.arDrone);
		arDrone.addNavDataListener(autoPilot);
		this.arDrone.addNavDataListener(videoPanelDrone);
//		arDrone.addImageListener(autoPilot);
		//this.autoPilotStarter = new Thread(autoPilot);
		
		
		this.arDrone.addStatusChangeListener(new DroneStatusChangeListener() {
			
			@Override
			public void ready() {
				// TODO Auto-generated method stub
				 /**
			     * This method is called whenever the drone changes from BOOTSTRAP or
			     * ERROR modes to DEMO mode. Could be used for user-supplied initialization
			     */
			}
		});
        
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void pushAutoPilotInformation(AutoPilotInformation data) {
		//TODO add textfield for status information
	}
}
