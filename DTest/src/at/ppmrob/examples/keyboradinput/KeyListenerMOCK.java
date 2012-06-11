package at.ppmrob.examples.keyboradinput;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListenerMOCK implements KeyListener {
	
	public KeyListenerMOCK(){
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

		System.out.println(arg0.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getKeyCode());
		System.out.println("22222");
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getKeyCode());
	}

}
