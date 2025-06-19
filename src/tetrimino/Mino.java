package tetrimino;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;
import main.KeyboardHandler;
import main.PlayManager;

public class Mino {
	public Block b[] = new Block[4]; //tetriminos have 4 blocks
	public Block tempB[] = new Block[4]; //for rotation, if collision happens, cancel rotation -> don't update to tempB
	int autoDropCounter = 0;
	public int direction = 1; //4 directions 1,2,3,4
	boolean leftCollision, rightCollision, bottomCollision;
	public boolean active = true;
	public boolean deactivating = false;
	int deactivateCounter = 0;
	
	public void create(Color c) {
		b[0] = new Block(c);
		b[1] = new Block(c);
		b[2] = new Block(c);
		b[3] = new Block(c);
		tempB[0] = new Block(c);
		tempB[1] = new Block(c);
		tempB[2] = new Block(c);
		tempB[3] = new Block(c);
	}
	
	public void setXY(int x, int y) {}
	public void updateXY(int direction) {
		//Update Mino according to rotation
		checkRotationCollision();
		
		if(leftCollision == false && rightCollision == false && bottomCollision == false) {
			this.direction = direction;
			b[0].x = tempB[0].x;
			b[0].y = tempB[0].y;
			
			b[1].x = tempB[1].x;
			b[1].y = tempB[1].y;
			
			b[2].x = tempB[2].x;
			b[2].y = tempB[2].y;
			
			b[3].x = tempB[3].x;
			b[3].y = tempB[3].y;
		}
		
		
	}
	public void getDirection1() {}
	public void getDirection2() {}
	public void getDirection3() {}
	public void getDirection4() {} //to be overridden by individual Mino classes
	
	//Collision detection
	public void checkMovementCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;
		
		//check staticBlocks
		checkStaticBlocksCollision();
		
		//Check frame collision
		//Left wall
		for (int i = 0; i< b.length; i++) {
			//Left wall
			if(b[i].x == PlayManager.left_x) {
				leftCollision = true;
			}
			//Right wall
			if(b[i].x + Block.SIZE == PlayManager.right_x) {
				rightCollision = true;
			}
			//Bottom wall
			if(b[i].y + Block.SIZE == PlayManager.bottom_y) {
				bottomCollision = true;
			}
		}
		
	}
	public void checkRotationCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;
		
		//check staticBlocks
		checkStaticBlocksCollision();
		
		//Check frame collision
		//Left wall
		for (int i = 0; i< b.length; i++) {
			//Left wall
			if(tempB[i].x < PlayManager.left_x) {
				leftCollision = true;
			}
			//Right wall
			if(tempB[i].x + Block.SIZE > PlayManager.right_x) {
				rightCollision = true;
			}
			//Bottom wall
			if(tempB[i].y + Block.SIZE > PlayManager.bottom_y) {
				bottomCollision = true;
			}
		}		
	} 
	
	public void checkStaticBlocksCollision() {
		for (int i = 0; i<PlayManager.staticBlocks.size(); i++) {
			//check against all of staticBlocks, note x & y at top left of block
			int targetX = PlayManager.staticBlocks.get(i).x;
			int targetY = PlayManager.staticBlocks.get(i).y;
			
			//check down
			for (int j = 0; j<b.length; j++) {
				if(b[j].y + Block.SIZE == targetY && b[j].x == targetX) {
					bottomCollision = true;
				}
			}
			
			//check left
			for (int j = 0; j<b.length; j++) {
				if(b[j].x - Block.SIZE == targetX && b[j].y == targetY) {
					leftCollision = true;
				}
			}
			
			//check right
			for (int j = 0; j<b.length; j++) {
				if(b[j].x + Block.SIZE == targetX && b[j].y == targetY) {
					rightCollision = true;
				}
			}
			
		}
	}
	
	public void update() {
		//check deactivating -> allow sliding
		if(deactivating) {
			deactivating();
		}
		
		//Control mino
		if(KeyboardHandler.upPressed) {
			//UP = rotate clockwise
			switch(direction) {
			case 1: getDirection2(); break;
			case 2: getDirection3(); break;
			case 3: getDirection4(); break;
			case 4: getDirection1(); break;
			}
			
			KeyboardHandler.upPressed = false;
			GamePanel.sfx.play(3, false); //play sfx
		}
		
		if(KeyboardHandler.zPressed) {
			//Z = rotate counterclockwise
			switch(direction) {
			case 1: getDirection4(); break;
			case 2: getDirection1(); break;
			case 3: getDirection2(); break;
			case 4: getDirection3(); break;
			}
			
			KeyboardHandler.zPressed = false;
			GamePanel.sfx.play(3, false); //play sfx
		}
		
		checkMovementCollision();
		
		if(KeyboardHandler.downPressed) {

			if(bottomCollision == false) {
				b[0].y += Block.SIZE;
				b[1].y += Block.SIZE;
				b[2].y += Block.SIZE;
				b[3].y += Block.SIZE;
			}
			
			autoDropCounter = 0;
			
			KeyboardHandler.downPressed = false;
			
		}
		if(KeyboardHandler.leftPressed) {

			if(leftCollision == false) {
				b[0].x -= Block.SIZE;
				b[1].x -= Block.SIZE;
				b[2].x -= Block.SIZE;
				b[3].x -= Block.SIZE;
			}

			KeyboardHandler.leftPressed = false;
			
		}
		if(KeyboardHandler.rightPressed) {

			if(rightCollision == false) {
				b[0].x += Block.SIZE;
				b[1].x += Block.SIZE;
				b[2].x += Block.SIZE;
				b[3].x += Block.SIZE;
			}

			KeyboardHandler.rightPressed = false;
		}
		if(KeyboardHandler.spacePressed) {
			//SPACE = hard drop
			for(int i = 0; i<20; i++) {
				checkMovementCollision();
				if(bottomCollision == false) {
					b[0].y += Block.SIZE;
					b[1].y += Block.SIZE;
					b[2].y += Block.SIZE;
					b[3].y += Block.SIZE;
				}
			}
			active = false;
			
			KeyboardHandler.spacePressed = false;
		}
		
		//AutoDrop
		if(bottomCollision) {
			if(deactivating == false) {
				GamePanel.sfx.play(4, false); //only play sfx once
			}
			deactivating = true; //active = false -> immediate deactivation -> no sliding
		}
		else {
			autoDropCounter++; //counter ++ every frame
			if(autoDropCounter==PlayManager.dropInterval) {
				b[0].y += Block.SIZE; //every second mino goes down 1 block height
				b[1].y += Block.SIZE;
				b[2].y += Block.SIZE;
				b[3].y += Block.SIZE;
				autoDropCounter = 0;
			}
		}
		
	}
	
	private void deactivating() {
		deactivateCounter++; //increments every frame
		
		//Wait 45 frames until deactivate
		if(deactivateCounter == 45) {
			deactivateCounter = 0;
			checkMovementCollision(); //check if Mino is still hitting the bottom
			
			//if bottomCollision true after 45 frames, deactivate 
			if(bottomCollision) {
				active = false;
			}
		}
		
		
	}

	public void draw(Graphics2D g2) {
		int margin = 2;
		g2.setColor(b[0].c);
		g2.fillRect(b[0].x+margin, b[0].y+margin, Block.SIZE - (margin*2), Block.SIZE - (margin*2));
		g2.fillRect(b[1].x+margin, b[1].y+margin, Block.SIZE - (margin*2), Block.SIZE - (margin*2));
		g2.fillRect(b[2].x+margin, b[2].y+margin, Block.SIZE - (margin*2), Block.SIZE - (margin*2));
		g2.fillRect(b[3].x+margin, b[3].y+margin, Block.SIZE - (margin*2), Block.SIZE - (margin*2)); //block shrunken by 2px margin -> block separation without changing x & y
		
		
	}
}
