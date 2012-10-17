
package at.ppmrob.examples.mock;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public interface IDroneVideoListenerMOCK
{
    void frameReceived(int startX, int startY, int w, int h, IplImage im , /*int[] rgbArray,*/ int offset, int scansize);
}
