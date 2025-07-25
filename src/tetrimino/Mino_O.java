package tetrimino;

import java.awt.Color;

public class Mino_O extends Mino{
	public Mino_O() {
		create(Color.YELLOW);
	}
	
	public void setXY(int x, int y) {
		// o o (0 2)
		// o o (1 3)
		b[0].x = x;
		b[0].y = y;
		
		b[1].x = b[0].x;
		b[1].y = b[0].y + Block.SIZE;
		
		b[2].x = b[0].x + Block.SIZE;
		b[2].y = b[0].y;
		
		b[3].x = b[0].x + Block.SIZE;
		b[3].y = b[0].y + Block.SIZE;
	}
	public void getDirection1() {}
	public void getDirection2() {}
	public void getDirection3() {}
	public void getDirection4() {} //can't rotate square

}
