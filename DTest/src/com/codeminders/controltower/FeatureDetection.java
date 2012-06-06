package com.codeminders.controltower;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class FeatureDetection {

	public FeatureDetection ()
	{
		
	}// FeatureDetection
	
	public void detectCircles(IplImage im)
	{
        cvSmooth(im, im, CV_BLUR, 3);//Smooth image (test)
        cvCircle(im, cvPoint(20, 20), 20, cvScalar(120, 120, 255, 0), 4, 8, 0);//draw a test circle

		/*IplImage gray;
		
	    cvCvtColor(im, gray, CV_BGR2GRAY);
	    // smooth it, otherwise a lot of false circles may be detected
	    cvSmooth(gray, gray, CV_BLUR, 3);
	    vector<Vec3f> circles;
	    HoughCircles(gray, circles, CV_HOUGH_GRADIENT,
	                 2, gray->rows/4, 200, 100 );
	    for( size_t i = 0; i < circles.size(); i++ )
	    {
	         Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
	         int radius = cvRound(circles[i][2]);
	         // draw the circle center
	         circle( im, center, 3, cvScalar(0,255,0), -1, 8, 0 );
	         // draw the circle outline
	         circle( im, center, radius, cvScalar(0,0,255), 3, 8, 0 );
	    }
*/
		
        
	}//detectCircles
	
	public void detectLines(IplImage im)
	{//Doesn't work
		IplImage dest = new IplImage();
		cvCvtColor(im, dest, CV_RGB2GRAY );
	    cvCanny(dest, im, 50, 200, 3 );
	}
}
