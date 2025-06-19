package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import tetrimino.Block;
import tetrimino.Mino;
import tetrimino.Mino_I;
import tetrimino.Mino_L;
import tetrimino.Mino_O;
import tetrimino.Mino_S;
import tetrimino.Mino_T;
import tetrimino.Mino_Z;
import tetrimino.Mino_rL;

public class PlayManager {
	//Main play area
	final int WIDTH = 300; 
	final int HEIGHT = 600; //30 x 30 block -> 10 blocks horizontally, 20 vertically
	public static int left_x;
	public static int right_x;
	public static int top_y;
	public static int bottom_y; //play area bounds
	
	//Mino
	Mino currentMino;
	final int MINO_START_X;
	final int MINO_START_Y;
	
	//Next mino
	Mino nextMino;
	final int NEXTMINO_X;
	final int NEXTMINO_Y;
	
	public static ArrayList<Block> staticBlocks = new ArrayList<>(); //inactive minos
	
	public static int dropInterval = 60; //mino drops every 60 frames = 1s
	boolean gameOver;
	boolean extraLife = true;
	
	//Effects
	boolean effectCounterOn;
	int effectCounter;
	ArrayList<Integer> effectY = new ArrayList<>(); //can delete multiple lines
	
	//Score
	int level = 1;
	int lines = 0;
	int score = 0;
	
	
	public PlayManager() {
		//Main play area frame
		left_x = (GamePanel.WIDTH/2) - (WIDTH/2); //1280/2 - 300/2 = 490
		right_x = left_x + WIDTH;
		top_y = 50;
		bottom_y = top_y + HEIGHT;
		
		MINO_START_X = left_x + (WIDTH/2) - Block.SIZE; //start X = center of play area
		MINO_START_Y = top_y + Block.SIZE; //start Y = top of play area
		
		NEXTMINO_X = right_x + 200;
		NEXTMINO_Y = top_y + 520;
		
		//Set the starting Mino
		//currentMino = new Mino_S(); //for testing each mino
		currentMino = pickMino();
		currentMino.setXY(MINO_START_X, MINO_START_Y);
		
		//Set next mino in next box
		nextMino = pickMino();
		nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
		
		
	}
	
	private Mino pickMino() {
		//Pick a random mino
		Mino mino = null;
		int i = new Random().nextInt(7);
		
		switch(i) {
		case 0: mino = new Mino_L(); break;
		case 1: mino = new Mino_rL(); break;
		case 2: mino = new Mino_I(); break;
		case 3: mino = new Mino_O(); break;
		case 4: mino = new Mino_T(); break;
		case 5: mino = new Mino_S(); break;
		case 6: mino = new Mino_Z(); break;
		}
		
		return mino;
		
	}
	
	public void update() {
		//Check if current mino is active
		if (currentMino.active == false) {
			//if inactive, put its blocks into staticBlocks
			staticBlocks.add(currentMino.b[0]);
			staticBlocks.add(currentMino.b[1]);
			staticBlocks.add(currentMino.b[2]);
			staticBlocks.add(currentMino.b[3]);
			
			//if currentMino is inactive, check game over
			if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
				//currentMino immediately collided with a block when spawned in
				gameOver = true;
				GamePanel.music.stop();
				GamePanel.sfx.play(2, false); //stop bgm & play game over sfx
			}
			
			//Replace currentMino with nextMino, pick new nextMino
			currentMino = nextMino;
			currentMino.setXY(MINO_START_X, MINO_START_Y);
			nextMino = pickMino();
			nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
			
			//when a mino becomes inactive, check if lines can be deleted
			checkDelete();
			
		}
		else {
			cheatCode();
			currentMino.update();
		}
	}
	
	private void cheatCode() {
		if(KeyboardHandler.cheatPressed) {
			for(int i=staticBlocks.size()-1; i>-1; i--) { //removing blocks shifts the array -> missing blocks if iterate from bottom up
				//remove all blocks at the current row
				if(staticBlocks.get(i).y == bottom_y+Block.SIZE || staticBlocks.get(i).y == bottom_y) {
					staticBlocks.remove(i);
				}
			}
			for(int i = 0; i<staticBlocks.size(); i++) {
				if(staticBlocks.get(i).y < bottom_y+Block.SIZE) {//coordinate root at top left -> smaller y = higher position
					staticBlocks.get(i).y += 2*Block.SIZE;
				}
			}
			
			KeyboardHandler.cheatPressed = false;
		}
		for(int i=staticBlocks.size()-1; i>-1; i--) {
			if(staticBlocks.get(i).y >= bottom_y) {
				staticBlocks.remove(i);
			}
		}
	}
	
	private void checkDelete() {
		int x = left_x;
		int y = top_y;
		int BlockCount = 0;
		int LineCount = 0;
		
		//iterate through every square in play area grid
		while(x<right_x && y<bottom_y) {
			
			for(int i = 0; i<staticBlocks.size(); i++) {
				if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
					BlockCount++; //if a square is a staticBlock, increase counter
				}
			}
			
			x += Block.SIZE;
			
			if(x == right_x) {
				//if BlockCount == 10 -> full line -> delete
				if (BlockCount == 10) {
					effectCounterOn = true;
					effectY.add(y); //effect applies to the rows deleted
					
					for(int i=staticBlocks.size()-1; i>-1; i--) { //removing blocks shifts the array -> missing blocks if iterate from bottom up
						//remove all blocks at the current row
						if(staticBlocks.get(i).y == y) {
							staticBlocks.remove(i);
						}
					}
					
					LineCount++;
					lines++;
					
					//Drop speed increase
					if (lines % 10 == 0 && dropInterval > 1){
						level++;
						if(dropInterval > 10) {
							dropInterval -= 10;
						}
						else {
							dropInterval -= 1;
						}
					}
					
					//shift down the rows above
					for(int i = 0; i<staticBlocks.size(); i++) {
						if(staticBlocks.get(i).y < y) {//coordinate root at top left -> smaller y = higher position
							staticBlocks.get(i).y += Block.SIZE;
						}
					}
				}
				
				// go to next row
				BlockCount = 0;
				x = left_x;
				y += Block.SIZE;
			}
		}
		//Add score
		if (LineCount > 0) {
			GamePanel.sfx.play(1, false); //play delete line sfx
			switch(LineCount) {
			case 1: score += 40*level; break;
			case 2: score += 100*level; break;
			case 3: score += 300*level; break;
			case 4: score += 1200*level; break;
			}
			LineCount = 0;
		}
	}
	
	public void draw(Graphics2D g2) {
		//Draw play area frame
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(4f));
		g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8); //start drawing boundary before collision happens
		
		//Draw next piece frame
		int x = right_x + 100;
		int y = bottom_y - 200;
		g2.drawRect(x, y, 200, 200); //draw frame
		g2.setFont(new Font("Arial", Font.BOLD, 30));
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.drawString("NEXT", x+60, y+60); //draw text
		
		//Draw Score frame
		g2.drawRect(x, top_y, 250, 300);
		x += 40;
		y = top_y + 90;
		g2.drawString("LEVEL: " + level, x, y);
		y += 70;
		g2.drawString("SCORE: " + score, x, y);
		y += 70;
		g2.drawString("LINES: " + lines, x, y);
		
		//Draw TETRIS
		int titleX = left_x - 300;
		int titleY = top_y + 100;
		g2.setColor(Color.RED);
		g2.setFont(new Font("Arial", Font.BOLD, 60));
		g2.drawString("T", titleX, titleY);
		titleY += 90;
		g2.setColor(Color.ORANGE);
		g2.drawString("E", titleX, titleY);
		titleY += 90;
		g2.setColor(Color.YELLOW);
		g2.drawString("T", titleX, titleY);
		titleY += 90;
		g2.setColor(Color.GREEN);
		g2.drawString("R", titleX, titleY);
		titleX += 10;
		titleY += 90;
		g2.setColor(Color.CYAN);
		g2.drawString("I", titleX, titleY);
		titleX -= 10;
		titleY += 90;
		g2.setColor(Color.MAGENTA);
		g2.drawString("S", titleX, titleY);
		
		//Draw currentMino
		if(currentMino != null) {
			currentMino.draw(g2);
		}
		
		//Draw next mino
		nextMino.draw(g2);
		
		//Draw staticBlocks
		for(int i = 0; i< staticBlocks.size(); i++) {
			staticBlocks.get(i).draw(g2);
		}
		
		//Draw effect
		if(effectCounterOn) {
			effectCounter++;
			
			g2.setColor(Color.RED);
			for(int i = 0; i<effectY.size(); i++) {
				g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
			}
			
			if(effectCounter == 10) {
				effectCounterOn = false;
				effectCounter = 0;
				effectY.clear();
			}
		}
		
		//Draw PAUSED && GAME OVER
		g2.setColor(Color.YELLOW);
		g2.setFont(g2.getFont().deriveFont(50f));
		
		if(gameOver) {
			if(extraLife) {
				for(int i=staticBlocks.size()-1; i>-1; i--) { //removing blocks shifts the array -> missing blocks if iterate from bottom up
					//remove all blocks at the current row
					if(staticBlocks.get(i).x == MINO_START_X || staticBlocks.get(i).x == MINO_START_X + Block.SIZE || staticBlocks.get(i).x == MINO_START_X - Block.SIZE) {
						staticBlocks.remove(i);
					}
				}
				
				
				gameOver = false;
				extraLife = false;
			}
			else {
				x = left_x;
				y = top_y + 320;
				g2.drawString("GAME OVER", x, y);
			}
			
		}
		
		else if(KeyboardHandler.pausePressed) {
			x = left_x + 50;
			y = top_y + 320;
			g2.drawString("PAUSED", x, y);
		}
		
	}
}
