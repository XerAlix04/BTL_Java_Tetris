package tetrimino;

import java.awt.Color;

public class Mino_Z extends Mino{
	public Mino_Z() {
		create(Color.RED);
	}
	
	public void setXY(int x, int y) {
		//   o   (1)
		// o o (2 0)
		// o   (3)
		b[0].x = x;
		b[0].y = y;
		
		b[1].x = b[0].x;
		b[1].y = b[0].y - Block.SIZE;
		
		b[2].x = b[0].x - Block.SIZE;
		b[2].y = b[0].y;
		
		b[3].x = b[0].x - Block.SIZE;
		b[3].y = b[0].y + Block.SIZE;
	}
	public void getDirection1() {
		//   o   (1)
		// o o (2 0)
		// o   (3)
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		
		tempB[1].x = b[0].x;
		tempB[1].y = b[0].y - Block.SIZE;
		
		tempB[2].x = b[0].x - Block.SIZE;
		tempB[2].y = b[0].y;
		
		tempB[3].x = b[0].x - Block.SIZE;
		tempB[3].y = b[0].y + Block.SIZE;
		
		updateXY(1); //Mino.updateXY(int direction)
	}
	public void getDirection2() {
		// o o   (2 3)
		//   o o   (0 1)
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		
		tempB[1].x = b[0].x + Block.SIZE;
		tempB[1].y = b[0].y;
		
		tempB[2].x = b[0].x - Block.SIZE;
		tempB[2].y = b[0].y - Block.SIZE;
		
		tempB[3].x = b[0].x;
		tempB[3].y = b[0].y - Block.SIZE;
		
		updateXY(2);
	}
	public void getDirection3() {
		getDirection1();
	}
	public void getDirection4() {
		getDirection2();
	}

}
