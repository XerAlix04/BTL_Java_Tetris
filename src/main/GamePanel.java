package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	final int FPS = 60;
	
	Thread gameThread;
	PlayManager pm;
	
	//Sound
	public static Sound music = new Sound();
	public static Sound sfx = new Sound();
	
	public GamePanel() {
		//Panel settings
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.BLACK);
		this.setLayout(null); //no preset layout -> customizable
		
		//Implements KeyListener
		this.addKeyListener(new KeyboardHandler());
		this.setFocusable(true); //GamePanel can be focused to receive input
		
		pm = new PlayManager();
	}
	
	public void launchGame() {
		//Create game thread
		gameThread = new Thread(this);
		gameThread.start(); //gameThread begins execution, calls run()
		
		music.play(0, true); //start BGM at game start
		music.loop();
	}

	@Override
	public void run() {
		//Delta Game loop
		double drawInterval = 1000000000/FPS; //using time in nanoseconds -> 1.000.000.000 nano = 1s -> drawInterval = 16.666.666 nano = 1/60s
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		/* FPS counter
		long timer = 0;
		int drawCount = 0;
		*/
		
		while (gameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval; //currentTime - lastTime = how much time has past
			//timer += currentTime - lastTime; //FPS counter
			lastTime = currentTime;
			
			//Update & draw 60 times per second
			if (delta >= 1) { //if currentTime - lastTime >= 1 frame
				update();
				repaint(); //calls paintComponent()
				delta--;
				//drawCount++; //FPS counter
			}
			/* FPS counter
			if(timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
			*/
		}
	}
	
	private void update() {
		if(KeyboardHandler.pausePressed == false && pm.gameOver == false) {
			pm.update();
		}
	}
	
	public void paintComponent(Graphics g) {
		//Draw screen with updated game info
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g; //Graphics2D extends Graphics for 2D geometry
		pm.draw(g2);
		
	}
}
