package ChessComponents;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import Resources.Tuple;

public class ChessRook extends ChessPiece{

	private int x, y, xCoord, yCoord, numMoves;
	private char color;
	private String name;
	private List<ChessTile> possibleMoves;
	
	public ChessRook(int x, int y, char color) {
		super(x, y, color);
		this.x = x;
		this.y = y;
		this.numMoves = 0;
		this.xCoord = (x-1)*90+375;
		this.yCoord = (8-y)*90+35;
		this.color = color;
		this.name = "Rook";
		this.possibleMoves = new ArrayList<>();
	}
	
	public void draw(PaintEvent event, Shell shell, Canvas canvas) {
		if (color == 'w')
			event.gc.drawImage(new Image(shell.getDisplay(),"ChessRookWhite.png"), 0, 0, 187, 202, xCoord, yCoord, 60, 60);
		else
			event.gc.drawImage(new Image(shell.getDisplay(),"ChessRookBlack.png"), 0, 0, 199, 195, xCoord, yCoord, 60, 60);
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
		boolean rightFlag, leftFlag, upFlag, downFlag;
		int up, down, left, right;
		if (player1) {
			right = x+1;
			left = x-1;
			leftFlag = false;
			rightFlag = false;
			while (right <= 8 && rightFlag == false) {
				for (int i = 0; i < board.getBoardTiles().size(); i++)
					for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
					{
						ChessTile temp = board.getBoardTiles().get(i).get(j);
						if (temp.getPiece() != null)
							if (temp.getX() == right && temp.getY() == y)
								rightFlag = true;
					}
				coordinates.add(new Tuple(right,y));
				right++;
			}
			while (left >= 1 && leftFlag == false) {
				for (int i = 0; i < board.getBoardTiles().size(); i++)
					for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
					{
						ChessTile temp = board.getBoardTiles().get(i).get(j);
						if (temp.getPiece() != null)
							if (temp.getX() == left && temp.getY() == y)
								leftFlag = true;
					}
				coordinates.add(new Tuple(left,y));
				left--;
			}
			up = y+1;
			down = y-1;
			upFlag = false;
			downFlag = false;
			while (up <= 8 && upFlag == false) {
				for (int i = 0; i < board.getBoardTiles().size(); i++)
					for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
					{
						ChessTile temp = board.getBoardTiles().get(i).get(j);
						if (temp.getPiece() != null)
							if (temp.getX() == x && temp.getY() == up)
								upFlag = true;
					}
				coordinates.add(new Tuple(x,up));
				up++;
			}
			while (down >= 1 && downFlag == false) {
				for (int i = 0; i < board.getBoardTiles().size(); i++)
					for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
					{
						ChessTile temp = board.getBoardTiles().get(i).get(j);
						if (temp.getPiece() != null)
							if (temp.getX() == x && temp.getY() == down)
								downFlag = true;
					}
				coordinates.add(new Tuple(x,down));
				down--;
			}
		}
		else {
			left = x+1;
			right = x-1;
			rightFlag = false;
			leftFlag = false;
			while (left <= 8 && leftFlag == false) {
				for (int i = 0; i < board.getBoardTiles().size(); i++)
					for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
					{
						ChessTile temp = board.getBoardTiles().get(i).get(j);
						if (temp.getPiece() != null)
							if (temp.getX() == left && temp.getY() == y)
								leftFlag = true;
					}
				coordinates.add(new Tuple(left,y));
				left++;
			}
			while (right >= 1 && rightFlag == false) {
				for (int i = 0; i < board.getBoardTiles().size(); i++)
					for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
					{
						ChessTile temp = board.getBoardTiles().get(i).get(j);
						if (temp.getPiece() != null)
							if (temp.getX() == right && temp.getY() == y)
								rightFlag = true;
					}
				coordinates.add(new Tuple(right,y));
				right--;
			}
			down = y+1;
			up = y-1;
			upFlag = false;
			downFlag = false;
			while (down <= 8 && downFlag == false) {
				for (int i = 0; i < board.getBoardTiles().size(); i++)
					for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
					{
						ChessTile temp = board.getBoardTiles().get(i).get(j);
						if (temp.getPiece() != null)
							if (temp.getX() == x && temp.getY() == down)
								downFlag = true;
					}
				coordinates.add(new Tuple(x,down));
				down++;
			}
			while (up >= 1 && upFlag == false) {
				for (int i = 0; i < board.getBoardTiles().size(); i++)
					for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
					{
						ChessTile temp = board.getBoardTiles().get(i).get(j);
						if (temp.getPiece() != null)
							if (temp.getX() == x && temp.getY() == up)
								upFlag = true;
					}
				coordinates.add(new Tuple(x,up));
				up--;
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
	
	public void increaseNumMoves() {
		numMoves ++;
	}
	
}