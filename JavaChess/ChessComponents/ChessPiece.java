package ChessComponents;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.PaintEvent;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import Resources.Tuple;

public abstract class ChessPiece {
	
	private int x, y, xCoord, yCoord, numMoves;
	private char color;
	private String name;
	private List<ChessTile> possibleMoves;
	
	public ChessPiece(int x, int y, char color) {
		this.x = x;
		this.y = y;
		this.numMoves = 0;
		this.xCoord = 360 + x*90;
		this.yCoord = 20 + y*90;
		this.color = color;
		this.name = null;
		this.possibleMoves = new ArrayList<>();
	}
	
	public abstract void draw(PaintEvent event, Shell shell, Canvas canvas);
	
	public abstract void move(int x, int y);

	public abstract int getX();

	public abstract void setX(int x);

	public abstract int getY();

	public abstract void setY(int y);
	
	public abstract int getxCoord();

	public abstract void setxCoord(int xCoord);

	public abstract int getyCoord();

	public abstract void setyCoord(int yCoord);

	public abstract char getColor();

	public abstract void setColor(char color);
	
	public abstract String getName();

	public abstract void setName(String name);
	
	public abstract List<Tuple> findPossibleMoves(ChessBoard board);
	
	public abstract int getNumMoves();
	
	public abstract void increaseNumMoves();
	
}
