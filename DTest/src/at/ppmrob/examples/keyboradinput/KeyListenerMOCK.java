package at.ppmrob.examples.keyboradinput;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListenerMOCK implements KeyListener {
	
	private boolean front = false, back = false, left = false,
			right = false, up = false, down = false;
	
	public KeyListenerMOCK(){}
	
	public int getfrontback()
	{
		if(front) return 1;
		else if(back) return -1;
		return 0;
	}
	
	public int getleftright()
	{
		if(left) return 1;
		else if(right) return -1;
		return 0;
	}
	
	public int getupdown()
	{
		if(up) return 1;
		else if(down) return -1;
		return 0;
	}
	
	@Override
	public void keyPressed(KeyEvent k) {
		// TODO Auto-generated method stub
		if(k.getKeyCode() == KeyEvent.VK_DOWN)
			back = true;
		else if(k.getKeyCode() == KeyEvent.VK_UP)
			front = true;
		else if(k.getKeyCode() == KeyEvent.VK_LEFT)
			left = true;
		else if(k.getKeyCode() == KeyEvent.VK_RIGHT)
			right = true;
		else if(k.getKeyCode() == KeyEvent.VK_PLUS)
			up = true;
		else if(k.getKeyCode() == KeyEvent.VK_MINUS)
			down = true;
	}

	@Override
	public void keyReleased(KeyEvent k) {
		// TODO Auto-generated method stub
		if(k.getKeyCode() == KeyEvent.VK_DOWN)
			back = false;
		else if(k.getKeyCode() == KeyEvent.VK_UP)
			front = false;
		else if(k.getKeyCode() == KeyEvent.VK_LEFT)
			left = false;
		else if(k.getKeyCode() == KeyEvent.VK_RIGHT)
			right = false;
		else if(k.getKeyCode() == KeyEvent.VK_PLUS)
			up = false;
		else if(k.getKeyCode() == KeyEvent.VK_MINUS)
			down = false;	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getKeyCode());
	}

}
