package ChessComponents;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import Resources.Tuple;

public class ChessKing extends ChessPiece{

	private int x, y, xCoord, yCoord, numMoves;
	private char color;
	private String name;
	private List<ChessTile> possibleMoves;
	
	public ChessKing(int x, int y, char color) {
		super(x, y, color);
		this.x = x;
		this.y = y;
		this.numMoves = 0;
		this.xCoord = (x-1)*90+375;
		this.yCoord = (8-y)*90+35;
		this.color = color;
		this.name = "King";
		this.possibleMoves = new ArrayList<>();
	}
	
	public void draw(PaintEvent event, Shell shell, Canvas canvas) {
		if (color == 'w')
			event.gc.drawImage(new Image(shell.getDisplay(),"ChessKingWhite.png"), 0, 0, 196, 203, xCoord, yCoord, 60, 60);
		else
			event.gc.drawImage(new Image(shell.getDisplay(),"ChessKingBlack.png"), 0, 0, 202, 194, xCoord, yCoord, 60, 60);
	}
	
	public void move(int x, int y) {
		this.xCoord = x;
		this.yCoord = y;
		this.x = (x-375)/90 + 1;
		this.y = 8 - ((y-35)/90);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		this.xCoord = (x-1)*90+375;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		this.yCoord = (8-y)*90+35;
	}

	public List<Tuple> findPossibleMoves(ChessBoard board){
		List<Tuple> coordinates = new ArrayList<>();
		boolean player1 = (color == 'w');
		int up, down, left, right;
		if (player1) {
				up = y+1;
				right = x+1;
				left = x-1;
				down = y-1;
			if (left >= 1) {			
				coordinates.add(new Tuple(left,y));
				if (up <= 8)
					coordinates.add(new Tuple(left,up));
				if (down >= 1)
					coordinates.add(new Tuple(left,down));
			}
			if (right <= 8) {			
				coordinates.add(new Tuple(right,y));
				if (up <= 8)
					coordinates.add(new Tuple(right,up));
				if (down >= 1)
					coordinates.add(new Tuple(right,down));
			}
			if (up <= 8)
				coordinates.add(new Tuple(x,up));
			if (down >= 1)
				coordinates.add(new Tuple(x,down));
		}
		else {
			down = y+1;
			left = x+1;
			right = x-1;
			up = y-1;
		if (right >= 1) {			
			coordinates.add(new Tuple(right,y));
			if (down <= 8)
				coordinates.add(new Tuple(right,down));
			if (up >= 1)
				coordinates.add(new Tuple(right,up));
		}
		if (left <= 8) {			
			coordinates.add(new Tuple(left,y));
			if (down <= 8)
				coordinates.add(new Tuple(left,down));
			if (up >= 1)
				coordinates.add(new Tuple(left,up));
		}
		if (down <= 8)
			coordinates.add(new Tuple(x,down));
		if (up >= 1)
			coordinates.add(new Tuple(x,up));
	}
		return coordinates;
	}

	@Override
	public int getxCoord() {
		// TODO Auto-generated method stub
		return xCoord;
	}

	@Override
	public void setxCoord(int xCoord) {
		// TODO Auto-generated method stub
		this.xCoord = xCoord;
		
	}

	@Override
	public int getyCoord() {
		// TODO Auto-generated method stub
		return yCoord;
	}

	@Override
	public void setyCoord(int yCoord) {
		// TODO Auto-generated method stub
		this.yCoord = yCoord;
	}

	@Override
	public char getColor() {
		// TODO Auto-generated method stub
		return color;
	}

	@Override
	public void setColor(char color) {
		// TODO Auto-generated method stub
		this.color = color;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}
	
	public int getNumMoves() {
		return numMoves;
	}
	
	public void increaseNumMoves() {
		numMoves ++;
	}
	
}