package tetrimino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Block extends Rectangle {
	private static final long serialVersionUID = 1L;
	public int x, y; //block coords
	public static final int SIZE = 30; //30 x 30 block
	public Color c; //each tetrimino has a different color
	
	public Block(Color c) {
		this.c = c;
	}
	
	public void draw(Graphics2D g2) {
		int margin = 2;
		g2.setColor(c);
		g2.fillRect(x+margin, y+margin, SIZE - (margin*2), SIZE - (margin*2)); //block shrunken by 2px margin -> block separation without changing x & y
	}
	
}
