package at.ppmrob.examples;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

public class MyCircle {

	CvPoint center;
	int radius;
	
	public MyCircle(CvPoint center, int r){
		this.center=center;
		this.radius=r;
	}
}