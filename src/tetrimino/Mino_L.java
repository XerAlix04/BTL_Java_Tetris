package tetrimino;

import java.awt.Color;

public class Mino_L extends Mino {
	public Mino_L() {
		create(Color.ORANGE);
	}
	
	public void setXY(int x, int y) {
		// o (b[1])
		// o (b[0]) -> rotation pivot
		// o o (b[2] & b[3])
		b[0].x = x;
		b[0].y = y;
		
		b[1].x = b[0].x;
		b[1].y = b[0].y - Block.SIZE;
		
		b[2].x = b[0].x;
		b[2].y = b[0].y + Block.SIZE;
		
		b[3].x = b[0].x + Block.SIZE;
		b[3].y = b[0].y + Block.SIZE;
	}
	public void getDirection1() {
		// o
		// o (tempB[0]) -> rotation pivot
		// o o
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		
		tempB[1].x = b[0].x;
		tempB[1].y = b[0].y - Block.SIZE;
		
		tempB[2].x = b[0].x;
		tempB[2].y = b[0].y + Block.SIZE;
		
		tempB[3].x = b[0].x + Block.SIZE;
		tempB[3].y = b[0].y + Block.SIZE;
		
		updateXY(1);//Mino.updateXY(int direction)
	}
	public void getDirection2() {
		//
		// o o o
		// o
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		
		tempB[1].x = b[0].x + Block.SIZE;
		tempB[1].y = b[0].y;
		
		tempB[2].x = b[0].x - Block.SIZE;
		tempB[2].y = b[0].y;
		
		tempB[3].x = b[0].x - Block.SIZE;
		tempB[3].y = b[0].y + Block.SIZE;
		
		updateXY(2);
	}
	public void getDirection3() {
		// o o
		//   o
		//   o
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		
		tempB[1].x = b[0].x;
		tempB[1].y = b[0].y + Block.SIZE;
		
		tempB[2].x = b[0].x;
		tempB[2].y = b[0].y - Block.SIZE;
		
		tempB[3].x = b[0].x - Block.SIZE;
		tempB[3].y = b[0].y - Block.SIZE;
		
		updateXY(3);
	}
	public void getDirection4() {
		//     o
		// o o o
		tempB[0].x = b[0].x;
		tempB[0].y = b[0].y;
		
		tempB[1].x = b[0].x - Block.SIZE;
		tempB[1].y = b[0].y;
		
		tempB[2].x = b[0].x + Block.SIZE;
		tempB[2].y = b[0].y;
		
		tempB[3].x = b[0].x + Block.SIZE;
		tempB[3].y = b[0].y - Block.SIZE;
		
		updateXY(4); 
	}

}
