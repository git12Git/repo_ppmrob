package at.ppmrob.examples.mock;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class VideoReaderMOCK_2 implements Runnable{

    private final String inputFilename = "video/320x240_at_15fps.mp4";//320x240_at_15fps.mp4";
    
    private final String outputFilePrefix = "C:/Users/slobo/Desktop/slik/";
    
	
//    public Vector<IplImage> framesVector = new Vector<IplImage>();
	ARDroneMOCK droneMock;
	
	
	public VideoReaderMOCK_2(ARDroneMOCK drone){
		this.droneMock=drone;
	}

	public void getAllFrames(){
		try {
			
			FrameGrabber frameGrabber = new OpenCVFrameGrabber(new File(inputFilename));
			
			IplImage img;
			
			frameGrabber.start();
			while((img= frameGrabber.grab())!=null){
//				framesVector.add(img);
				droneMock.videoFrameReceived(0, 0, 320, 240, img, 0, 0);
	            try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			frameGrabber.stop();
//			int frameCount = framesVector.size();
//	        int fps=0;
//	        for(int i=0;i<frameCount;i++){
//	        	droneMock.videoFrameReceived(0, 0, 720, 480, framesVector.get(i).getBufferedImage(), 0, 0);
//
//	        	
//	        	try {
//					Thread.sleep(55);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	        	if(i==frameCount-1){
//	        		for(int j=frameCount-1;j>0;j--){
//	        			droneMock.videoFrameReceived(0, 0, 720, 480, framesVector.get(j).getBufferedImage(), 0, 0);
//	    	        	
//	        			try {
//							Thread.sleep(55);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//	        		}
//	        		i=0;
//	        	}
//	        }
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	

        
        private String dumpImageToFile(BufferedImage image) {
            try {
                String outputFilename = outputFilePrefix + 
                     System.currentTimeMillis() + ".png";
                ImageIO.write(image, "png", new File(outputFilename));
                return outputFilename;
            } 
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		getAllFrames();
	}
	
	
	public static void test_changeVideoCoord(){
		
	}
}
