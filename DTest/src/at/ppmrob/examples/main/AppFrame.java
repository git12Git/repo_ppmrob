package at.ppmrob.examples.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import at.ppmrob.examples.keyboradinput.KeyListenerMOCK;
import at.ppmrob.examples.mock.ARDroneMOCK;
import at.ppmrob.examples.mock.VideoPanelForMOCK;
import at.ppmrob.examples.mock.VideoReaderMOCK_2;

public class AppFrame extends JFrame {

	private JPanel contentPane;
	VideoPanelForMOCK videoPanel;
	ARDroneMOCK arDroneMOCK;
	VideoReaderMOCK_2 videoReaderMOCK_2;
	Thread readerThread;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppFrame frame = new AppFrame();
					frame.setVisible(true);
					frame.init();
					frame.addKeyListener(new KeyListenerMOCK());
					frame.setFocusable(true);
					System.out.println("-------------------");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 510);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		videoPanel = new VideoPanelForMOCK();
		contentPane.add(videoPanel, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnButton = new JButton("button1");
		panel_1.add(btnButton);
		
		JButton btnButton_1 = new JButton("button2");
		panel_1.add(btnButton_1);
		
	}
	
	
	public void init(){
		try {
			this.arDroneMOCK = new ARDroneMOCK();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.videoPanel.setDrone(this.arDroneMOCK);
		
		this.videoReaderMOCK_2= new VideoReaderMOCK_2(this.arDroneMOCK);
		this.readerThread = new Thread(this.videoReaderMOCK_2);
		this.readerThread.start();
		
		
	}
	
	
	/**
	 * 
	 * JUST FOR TEST  -  NO FUNCTION

Send a command with this bit set to 1 to make the drone take-off.  This command
should be repeated until the drone state in the navdata shows that drone actually
took off.  If no other command is supplied, the drone enters a hovering mode and
stays still at approximately 1 meter above ground.
Send a command with this bit set to 0 to make the drone land. This command should
be repeated until the drone state in the navdata shows that drone actually landed,
and should be sent as a safety whenever an abnormal situation is detected.
After the first start AT-Command, the drone is in the taking-Off state, but still accepts
other commands.  It means that while the drone is rising in the air to the "1-meter-
high-hovering state", the user can send orders to move or rotate it.
	 */
	public void controllDrone(){
		try {
			/**This command
			should be repeated until the drone state in the navdata shows that drone actually
			took off.  If no other command is supplied, the drone enters a hovering mode and
			stays still at approximately 1 meter above ground.*/
			this.arDroneMOCK.takeOff();
			/**
			 * move the drone few cm up
			 */
			this.arDroneMOCK.move(0, 0, 0.1f, 0);
			/**
			 * move forward
			 */
			this.arDroneMOCK.move(0, 0, 0, 0.15f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
