package at.ppmrob.examples;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

public class MyLine {
	
	public CvPoint point1;
	public CvPoint point2;
	
	public MyLine(CvPoint p1, CvPoint p2){
		this.point1=p1;
		this.point2=p2;
	}

}
