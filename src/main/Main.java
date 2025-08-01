package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame("Tetris");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		//Add GamePanel to window
		GamePanel gp = new GamePanel();
		window.add(gp);
		window.pack(); //size of window fit to size of GamePanel
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gp.launchGame();
	}
}
