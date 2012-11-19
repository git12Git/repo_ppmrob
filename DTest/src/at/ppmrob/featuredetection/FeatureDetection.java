package at.ppmrob.featuredetection;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.util.Vector;

import com.googlecode.javacpp.FloatPointer;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class FeatureDetection {

	private int height2 = 240; //240 144
	private int width2 = 320; //320 176
	private Vector<IFeatureDetectionListener> listeners;
//	CvMemStorage storage;
//	CvMemStorage storage2;
	private static FeatureDetection featureDetection;
	
	private MyLine avgLine;
	
	private FeatureDetection ()
	{
		listeners = new Vector<IFeatureDetectionListener>();
//		storage2 = CvMemStorage.create();
//		storage = CvMemStorage.create();
	}// FeatureDetection
	public static FeatureDetection getInstance(){
		if(featureDetection==null){
			featureDetection = new FeatureDetection();
		}
		return featureDetection;
	}
	
	public void initWidthAndHeight(int imgWidth, int imgHeight){
//		this.imgWidth=imgHeight;
//		this.imgHeight = imgWidth;
	}
	
	
	public void addFeatureDetectionListener(IFeatureDetectionListener ifdl){
		listeners.add(ifdl);
	}
	/**
	 * 
	 * @param im2
	 * @return
	 */
	public synchronized Vector<MyCircle> detectCircles(IplImage im, int width, int height)
	{
		
//		IplImage img = IplImage.createFrom(im);
		
		Vector<MyCircle> detectedCircles = new Vector<MyCircle>();
		
		IplImage grayImgCircles = IplImage.create(width, height, IPL_DEPTH_8U, 1);

		cvCvtColor(im, grayImgCircles, CV_BGR2GRAY);
		
		cvSmooth(grayImgCircles, grayImgCircles, CV_GAUSSIAN, 3);

//		cvCircle(im, cvPoint(153, 130), 5, cvScalar(0, 255, 0, 0), 
//    			-1,// line thickness 
//    			8,// line type
//    			0);// shift);		

//	    cvCvtColor(im, im, CV_BGR2GRAY);

	    CvMemStorage storage = CvMemStorage.create();

	    CvSeq circles = cvHoughCircles(grayImgCircles, storage, CV_HOUGH_GRADIENT, 1, 2, 100, 50, 0, 0);

//	    storage.release();
	    
	  
        for (int i = 0; i < circles.total(); i++) {

          FloatPointer fPoint = new FloatPointer(cvGetSeqElem(circles, i));
          
          int x= (int)fPoint.get(0);
          int y= (int)fPoint.get(1);
          int radius = (int)fPoint.get(2);
 
          detectedCircles.add(new MyCircle(new CvPoint(x,y), radius));
//            if (y > 100 && y<350 && x >150 && x <224) {
//                cvCircle(im, new CvPoint(x,y), 3, cvScalar(0, 255, 0, 0)/*CV_RGB(100, 0, 0)*/, -1, 8, 0);
//                cvCircle(im, new CvPoint(x,y), radius, cvScalar(0, 255, 0, 0)/*CV_RGB(100, 0, 0)*/, 1, 8, 0);
        }
        if(circles.total()>0){
        	for(IFeatureDetectionListener ifdl:listeners){
        		ifdl.foundCircles(detectedCircles);
        	}
        }
        else {
        	for(IFeatureDetectionListener ifdl:listeners){
        		ifdl.noCirclesFound();
        	}
        }
		return detectedCircles;
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param img
	 * @param detectedCircles
	 * @return
	 */
	public synchronized IplImage drawCircles(IplImage img, Vector<MyCircle> detectedCircles){
		
		for(int i=0; i<detectedCircles.size(); i++){
		  cvCircle(img, detectedCircles.get(i).center, 3, CvScalar.GREEN/*CV_RGB(100, 0, 0)*/, -1, 8, 0);
          cvCircle(img, detectedCircles.get(i).center,  detectedCircles.get(i).radius, CvScalar.GREEN/*CV_RGB(100, 0, 0)*/, 1, 8, 0);
		}
		return img;
	}

	
	
	
	
	
	/**
	 * 
	 * @param im
	 * @return
	 */
	public synchronized Vector<MyLine> detectLines(IplImage im, int width, int height)
	{
		Vector<MyLine> detectedLines = new Vector<MyLine>();
    
//	    cvSmooth(im, im, CV_BLUR, 3);//Smooth image (test)

		IplImage grayImgLines= IplImage.create(width, height, IPL_DEPTH_8U, 1);

		cvCvtColor(im, grayImgLines, CV_BGR2GRAY);

	    CvMemStorage storage2 = CvMemStorage.create();

	    IplImage imCanny = IplImage.create(width, height, IPL_DEPTH_8U, 1);
//	    IplImage grayImgCanny = IplImage.create(320, 240, IPL_DEPTH_8U, 1);
	
	    cvCanny(grayImgLines, imCanny, 50, 200, 3);
//	    cvCvtColor(imCanny, grayImgCanny, CV_BGR2GRAY);

	    CvSeq lines = cvHoughLines2( imCanny, storage2, CV_HOUGH_PROBABILISTIC, 1, 3.1415926/180, 50, 80, 50 );
	  
	    int x1=0;
	    int y1=0;
	    int x2=0;
	    int y2=0;
	    for(int i = 0; i < lines.total(); i++ )
	    {
	    	Pointer line = cvGetSeqElem(lines, i);
	    	detectedLines.add(new MyLine(new CvPoint(line).position(0), new CvPoint(line).position(1)));
	    	MyLine myLine = new MyLine(new CvPoint(line).position(0), new CvPoint(line).position(1));
//	    	cvLine(im, new CvPoint(line).position(0), 
//	        		new CvPoint(line).position(1), CvScalar.BLUE, 2, CV_AA, 0);
	    	x1+=myLine.point1.x();
	    	y1+=myLine.point1.y();
	    	x2+=myLine.point2.x();
	    	y2+=myLine.point2.y();
	    }
	    
	    
	    if(lines.total()>0){
	    	x1=x1/lines.total();
		    y1=y1/lines.total();
		    x2=x2/lines.total();
		    y2=y2/lines.total();
		    avgLine =  new MyLine(new CvPoint().put(x1, y1), new CvPoint().put(x2, y2));
		    
	    	for(IFeatureDetectionListener ifdl:listeners){
	    		ifdl.foundLines(detectedLines, avgLine);
	    	}
	}
	    //storage2.release();
	 return detectedLines;
	}
	
	
	
	/**
	 * 
	 * @param im
	 * @return
	 */
	public synchronized IplImage getCanny(IplImage im, int width, int height)
	{
		
    
//	    cvSmooth(im, im, CV_BLUR, 3);//Smooth image (test)

		IplImage grayImgLines= IplImage.create(width, height, IPL_DEPTH_8U, 1);

		cvCvtColor(im, grayImgLines, CV_BGR2GRAY);

	    IplImage imCanny = IplImage.create(width, height, IPL_DEPTH_8U, 1);
//	    IplImage grayImgCanny = IplImage.create(320, 240, IPL_DEPTH_8U, 1);
	
	    cvCanny(grayImgLines, imCanny, 50, 200, 3);
//	    cvCvtColor(imCanny, grayImgCanny, CV_BGR2GRAY);
	    return grayImgLines;
	}
	
	
	/**
	 * 
	 * @param img
	 * @param detectedlines
	 * @return
	 */
	public synchronized IplImage drawLines(IplImage img, Vector<MyLine> detectedlines){
//		 DEBUG_JFrame.setLblNewLabel_5("Lines det: "+detectedlines.size());
//		for(int i = 0; i < detectedlines.size(); i++ )
//		    {
//		    	cvLine(img, detectedlines.get(i).point1, 
//		        		detectedlines.get(i).point2, CvScalar.RED, 1, CV_AA, 0);
//		    }
		for(MyLine l:detectedlines)
	    {
	    	cvLine(img, l.point1, l.point2, CvScalar.RED, 1, CV_AA, 0);
	    	cvCircle(img, l.point1, 3, CvScalar.RED/*CV_RGB(100, 0, 0)*/, -1, 5, 0);
	    	cvCircle(img, l.point2, 3, CvScalar.YELLOW/*CV_RGB(100, 0, 0)*/, -1, 5, 0);
	    }
		return img;
	}
	public MyLine getAvgLine() {
		return avgLine;
	}
}
