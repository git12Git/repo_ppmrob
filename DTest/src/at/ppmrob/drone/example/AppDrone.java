package at.ppmrob.drone.example;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.ARDrone.VideoChannel;
import com.codeminders.ardrone.controllers.KeyboardController;
import com.codeminders.ardrone.controllers.PS3Controller;
import com.codeminders.controltower.ControlTower;

import at.ppmrob.examples.keyboradinput.KeyListenerMOCK;
import javax.swing.JButton;

public class AppDrone extends JFrame {

	private JPanel contentPane;
	
	private ARDrone ardrone;
	private MyVideoPanel videoPanel;
	 private final AtomicReference<PS3Controller> dev = new AtomicReference<PS3Controller>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppDrone frame = new AppDrone();
					frame.addKeyListener(new KeyListenerMOCK());
					frame.setFocusable(true);
					
					frame.setVisible(true);
					
					frame.initController();
					frame.initDrone();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppDrone() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		MyVideoPanel panel = new MyVideoPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("New button");
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		panel_1.add(btnNewButton_1);
		
	}

	
	
    private void initDrone()
    {
        try
        {
            ardrone = new ARDrone();
            ardrone.connect();
            ardrone.clearEmergencySignal();
            ardrone.waitForReady(5000);
            ardrone.selectVideoChannel(VideoChannel.VERTICAL_ONLY);
        }
        catch(UnknownHostException ex)
        {
            Logger.getLogger(ControlTower.class.getName()).error("Error creating drone object!", ex);
            return;
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        droneConfigWindow.setDrone(drone);
//        gauges.setDrone(drone);
        videoPanel.setDrone(ardrone);
        ardrone.addStatusChangeListener(new MyStatusChangeListener());
        ardrone.addNavDataListener(new MyNavDataListener());
    }
    
    
    
    
    
    
    
    
    /**
     * 
     */
    private void initController()
    {
            System.err.println("No suitable controller found! Using keyboard");
            dev.set(new KeyboardController(this));
//            updateControllerStatus(false);
    }
}
