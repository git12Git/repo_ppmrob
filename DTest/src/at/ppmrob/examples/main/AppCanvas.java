package at.ppmrob.examples.main;
import java.util.Vector;

import at.ppmrob.examples.FeatureDetection;
import at.ppmrob.examples.MyCircle;
import at.ppmrob.examples.MyLine;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.OpenCVFrameGrabber;

import java.awt.*;
import java.awt.image.*;

/**
 * example without videolistener
 * 
 *
 */
public class AppCanvas {
	
	
	
 public static void main(String[] args) {
     CanvasFrame canvas = new CanvasFrame("VideoCanvas");

     canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);  
      
     FrameGrabber grabber = new OpenCVFrameGrabber("video/320x240_at_15fps.mp4"); 

     FeatureDetection fd = new FeatureDetection();
     try {     
       
      grabber.start();     

      IplImage img;
       
      while (true) {

       img = grabber.grab();

       canvas.setCanvasSize(grabber.getImageWidth(), grabber.getImageHeight());
  
        Thread.sleep(100);
       
       if (img != null) {      
    	  
       	Vector<MyLine> detectedLines = fd.detectLines(img);
       	Vector<MyCircle> detectedCircles = fd.detectCircles(img);
       	IplImage imgWithLines = fd.drawLines(img, detectedLines);
       	IplImage imgWithLinesAndCircles = fd.drawCircles(imgWithLines, detectedCircles);
    	
  
        canvas.showImage(imgWithLinesAndCircles.getBufferedImage());              
        }
       }
      }
     catch (Exception e) {     
     }
    }

 public BufferedImage scaleImage(BufferedImage img, int width, int height,
	        Color background) {
	    int imgWidth = img.getWidth();
	    int imgHeight = img.getHeight();
	    if (imgWidth*height < imgHeight*width) {
	        width = imgWidth*height/imgHeight;
	    } else {
	        height = imgHeight*width/imgWidth;
	    }
	    BufferedImage newImage = new BufferedImage(width, height,
	            BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = newImage.createGraphics();
	    try {
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        g.setBackground(background);
	        g.clearRect(0, 0, width, height);
	        g.drawImage(img, 0, 0, width, height, null);
	    } finally {
	        g.dispose();
	    }
	    return newImage;
	}
}