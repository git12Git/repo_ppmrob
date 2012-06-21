/*
 * VideoPanel.java
 * 
 * Created on 21.05.2011, 18:42:10
 */

package at.ppmrob.examples.main;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.DroneVideoListener;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.text.AttributeSet.FontAttribute;

/**
 * 
 * @author normenhansen
 */
@SuppressWarnings("serial")
public class VideoPanelCustom extends javax.swing.JPanel implements DroneVideoListener, NavDataListener
{

    private AtomicReference<BufferedImage> image          = new AtomicReference<BufferedImage>();
    private AtomicBoolean                  preserveAspect = new AtomicBoolean(true);
    private BufferedImage                  noConnection   = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);

    private NavData droneNavData;
    private float droneAltitude;
    private float droneBattery;
    private String droneControlState;
    private String droneFlyingState;
    private boolean droneIsFlying;
    private boolean droneIsBatteryTooHigh;
    private boolean droneIsBatteryTooLow;
    private boolean droneIsEmergency;
    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    
    /** Creates new form VideoPanel */
    public VideoPanelCustom()
    {
        initComponents();
        Graphics2D g2d = (Graphics2D) noConnection.getGraphics();
//        Font f = g2d.getFont().deriveFont(10.0f);
        g2d.setFont(font);
        g2d.setColor(Color.GREEN);
        g2d.drawString("No video connection", 40, 180); 
//        g2d.drawString("HEIGHT:     "+droneAltitude, 5, 15);
//        g2d.drawString("BATTERY:    "+droneBattery, 5, 30);
//        g2d.drawString("BTR HIGH:   "+droneIsBatteryTooHigh, 5, 45);
//        g2d.drawString("BTR LOW:    "+droneIsBatteryTooLow, 5, 60);
//        g2d.drawString("CTRL STATE: "+droneControlState, 5, 75);
//        g2d.drawString("FLY STATE:  "+droneFlyingState, 5, 90);
//        g2d.drawString("IS FLYING:  "+droneIsFlying, 5, 105);
//        g2d.drawString("IS EMERG:   "+droneIsEmergency, 5, 120);
        
        image.set(noConnection);
    }

    public void setDrone(ARDrone drone)
    {
        drone.addImageListener(this);
    }

    public void setPreserveAspect(boolean preserve)
    {
        preserveAspect.set(preserve);
    }

    @Override
    public void frameReceived(int startX, int startY, int w, int h, int[] rgbArray, int offset, int scansize)
    {
        BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        im.setRGB(startX, startY, w, h, rgbArray, offset, scansize);
        image.set(im);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        drawDroneImage(g2d, width, height);
    }

    private void drawDroneImage(Graphics2D g2d, int width, int height)
    {
        BufferedImage im = image.get();
        if(im == null)
        {
            return;
        }
        int xPos = 0;
        int yPos = 0;
        if(preserveAspect.get())
        {
            g2d.setColor(Color.BLACK);
            g2d.fill3DRect(0, 0, width, height, false);
            float widthUnit = ((float) width / 4.0f);
            float heightAspect = (float) height / widthUnit;
            float heightUnit = ((float) height / 3.0f);
            float widthAspect = (float) width / heightUnit;

            if(widthAspect > 4)
            {
                xPos = (int) (width - (heightUnit * 4)) / 2;
                width = (int) (heightUnit * 4);
            } else if(heightAspect > 3)
            {
                yPos = (int) (height - (widthUnit * 3)) / 2;
                height = (int) (widthUnit * 3);
            }
        }
        if(im != null)
        {
        	
            g2d.drawImage(im, xPos, yPos, width, height, null);

            g2d.setFont(font);
            g2d.setColor(Color.GREEN);
            g2d.drawString("No video connection", 40, 180); 
            g2d.drawString("HEIGHT:     "+droneAltitude, 5, 15);
            g2d.drawString("BATTERY:    "+droneBattery, 5, 30);
            g2d.drawString("BTR HIGH:   "+droneIsBatteryTooHigh, 5, 45);
            g2d.drawString("BTR LOW:    "+droneIsBatteryTooLow, 5, 60);
            g2d.drawString("CTRL STATE: "+droneControlState, 5, 75);
            g2d.drawString("FLY STATE:  "+droneFlyingState, 5, 90);
            g2d.drawString("IS FLYING:  "+droneIsFlying, 5, 105);
            g2d.drawString("IS EMERG:   "+droneIsEmergency, 5, 120);
            
            
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed"
    // desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        setLayout(new java.awt.GridLayout(4, 6));
    }// </editor-fold>//GEN-END:initComponents
     // Variables declaration - do not modify//GEN-BEGIN:variables
     // End of variables declaration//GEN-END:variables


	@Override
	public void navDataReceived(NavData nd) {
		// TODO Auto-generated method stub
		synchronized (this.droneNavData) {
			this.droneNavData=nd;
		}
		droneAltitude = this.droneNavData.getAltitude();
		droneBattery = this.droneNavData.getBattery();
		droneControlState = this.droneNavData.getControlState().name();
		droneFlyingState = this.droneNavData.getFlyingState().name();
		droneIsFlying = this.droneNavData.isFlying();
		droneIsBatteryTooHigh = this.droneNavData.isBatteryTooHigh();
		droneIsBatteryTooLow = this.droneNavData.isBatteryTooLow();
		droneIsEmergency = this.droneNavData.isEmergency();
	
	}
}
