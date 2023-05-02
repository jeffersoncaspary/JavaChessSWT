package ChessComponents;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import Resources.Tuple;

public class ChessPawn extends ChessPiece{

	private int x, y, xCoord, yCoord, numMoves;
	private char color;
	private boolean firstMove;
	private String name;
	private List<Tuple> possibleMoves;

	
	public ChessPawn(int x, int y, char color) {
		super(x, y, color);
		this.x = x;
		this.y = y;
		this.numMoves = 0;
		this.xCoord = (x-1)*90+375;
		this.yCoord = (8-y)*90+35;
		this.color = color;
		this.name = "Pawn";
		this.firstMove = true;
		this.possibleMoves = null;
	}
	
	public void draw(PaintEvent event, Shell shell, Canvas canvas) {
		if (color == 'w')
			event.gc.drawImage(new Image(shell.getDisplay(),"ChessPawnWhite.png"), 0, 0, 201, 197, xCoord, yCoord, 60, 60);
		else
			event.gc.drawImage(new Image(shell.getDisplay(),"ChessPawnBlack.png"), 0, 0, 203, 194, xCoord, yCoord, 60, 60);
	}
	
	public void move(int x, int y) {
		this.xCoord = x;
		this.yCoord = y;
		this.x = (x-375)/90 + 1;
		this.y = 8 - ((y-35)/90);
		System.out.println(this.x);
		System.out.println(this.y);
		if (this.firstMove)
			this.firstMove = false;
	}

	public boolean first() {
		return firstMove;
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
		int up, left, right;
		boolean straightUp = false;
		boolean twoAhead = false;
		if (color == 'w') {
			up = y+1;
			right = x+1;
			left = x-1;
		}
		else {
			left = x+1;
			right = x-1;
			up = y-1;
		}
		if (up <= 8 && up >= 1) {
			for (int i = 0; i < board.getBoardTiles().size(); i++)
				for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
				{
					ChessTile temp = board.getBoardTiles().get(i).get(j);
					if (temp.getPiece() != null) {
						if (temp.getX() == right && temp.getY() == up)
							coordinates.add(new Tuple(right, up));
						if (temp.getX() == left && temp.getY() == up)
							coordinates.add(new Tuple(left, up));
						if (temp.getX() == x && temp.getY() == up)
							straightUp = true;
						if (color == 'w') 
							up++;
						else
							up--;
						if (temp.getX() == x && temp.getY() == up)
							twoAhead = true;
						if (color == 'w') 
							up--;
						else
							up++;
					}
				}
		}
		if (!straightUp) {
			coordinates.add(new Tuple(x,up));
			if (firstMove && !twoAhead) {
				if (color == 'w') 
					up++;
				else
					up--;
				coordinates.add(new Tuple(x,up));
			}
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
		this.x = (xCoord-375)/90 + 1;
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
		this.y = 8 - ((yCoord-35)/90);
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
		this.name = name;
		
	}
	
	public int getNumMoves() {
		return numMoves;
	}

	@Override
	public void increaseNumMoves() {
		// TODO Auto-generated method stub
		numMoves++;
	}
	
}