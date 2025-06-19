package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements KeyListener {

	public static boolean upPressed, downPressed, leftPressed, rightPressed, pausePressed, zPressed, spacePressed, cheatPressed;
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_UP) {
			upPressed = true;
		}
		if(code == KeyEvent.VK_Z) {
			zPressed = true;
		}
		if (code == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if (code == KeyEvent.VK_SPACE) {
			spacePressed = true;
		}
		if (code == KeyEvent.VK_P) {
			if(pausePressed) {
				pausePressed = false;
				GamePanel.music.play(0, true); //start bgm when resumed
				GamePanel.music.loop();
			}
			else {
				pausePressed = true;
				GamePanel.music.stop(); //stop bgm when paused
			}
		}
		if(code == KeyEvent.VK_C) {
			cheatPressed = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
}
