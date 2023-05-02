package ChessComponents;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import Resources.Tuple;

public class ChessBishop extends ChessPiece{
	
	private int x, y, xCoord, yCoord, numMoves;
	private char color;
	private String name;
	private List<ChessTile> possibleMoves;
	
	public ChessBishop(int x, int y, char color) {
		super(x,y,color);
		this.x = x;
		this.y = y;
		this.numMoves = 0;
		this.xCoord = (x-1)*90+375;
		this.yCoord = (8-y)*90+35;
		this.color = color;
		this.name = "Bishop";
		this.possibleMoves = new ArrayList<>();
	}
	
	public void draw(PaintEvent event, Shell shell, Canvas canvas) {
		if (color == 'w')
			event.gc.drawImage(new Image(shell.getDisplay(),"ChessBishopWhite.png"), 0, 0, 183, 206, xCoord, yCoord, 60, 60);
		else
			event.gc.drawImage(new Image(shell.getDisplay(),"ChessBishopBlack.png"), 0, 0, 180, 180, xCoord, yCoord, 60, 60);
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
		this.yCoord = (8-y)*90+35;
		this.y = y;
	}

	public List<Tuple> findPossibleMoves(ChessBoard board){
		List<Tuple> coordinates = new ArrayList<>();
		boolean player1 = (color == 'w');
		boolean rightFlag, leftFlag;;
		int up, down, left, right;
		if (player1) {
			up = y+1;
			right = x+1;
			left = x-1;
			down = y-1;
			rightFlag = false;
			leftFlag = false;
			
			while (up <= 8) {
				if (right <= 8 && rightFlag == false) {
					for (int i = 0; i < board.getBoardTiles().size(); i++)
						for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
						{
							ChessTile temp = board.getBoardTiles().get(i).get(j);
							if (temp.getPiece() != null)
								if (temp.getX() == right && temp.getY() == up)
								{
									rightFlag = true;
								}
						}
					coordinates.add(new Tuple(right,up));
					right++;
				}
				
				if (left >= 1 && leftFlag == false) {
					for (int i = 0; i < board.getBoardTiles().size(); i++)
						for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
						{
							ChessTile temp = board.getBoardTiles().get(i).get(j);
							if (temp.getPiece() != null)
								if (temp.getX() == left && temp.getY() == up)
								{
									leftFlag = true;
								}
						}
					coordinates.add(new Tuple(left,up));
					left--;
				}
				up++;
			}
			right = x+1;
			left = x-1;
			rightFlag = false;
			leftFlag = false;
			while (down >= 1) {
				if (right <= 8 && rightFlag == false) {
					for (int i = 0; i < board.getBoardTiles().size(); i++)
						for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
						{
							ChessTile temp = board.getBoardTiles().get(i).get(j);
							if (temp.getPiece() != null)
								if (temp.getX() == right && temp.getY() == down)
								{
									rightFlag = true;
								}
						}
							coordinates.add(new Tuple(right,down));
							right++;
				}
				if (left >= 1 && leftFlag == false) {
					for (int i = 0; i < board.getBoardTiles().size(); i++)
						for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
						{
							ChessTile temp = board.getBoardTiles().get(i).get(j);
							if (temp.getPiece() != null)
								if (temp.getX() == left && temp.getY() == down)
								{
									leftFlag = true;
								}
						}
						coordinates.add(new Tuple(left,down));
						left--;
				}
				down--;
			}
		}
		else {
			down = y+1;
			left = x+1;
			right = x-1;
			up = y-1;
			rightFlag = false;
			leftFlag = false;
			while (down <= 8) {
				if (left <= 8 && leftFlag == false) {
					for (int i = 0; i < board.getBoardTiles().size(); i++)
						for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
						{
							ChessTile temp = board.getBoardTiles().get(i).get(j);
							if (temp.getPiece() != null)
								if (temp.getX() == left && temp.getY() == down)
								{
									leftFlag = true;
								}
						}
					coordinates.add(new Tuple(left,down));
					left++;
				}
				if (right >= 1 && rightFlag == false) {
					for (int i = 0; i < board.getBoardTiles().size(); i++)
						for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
						{
							ChessTile temp = board.getBoardTiles().get(i).get(j);
							if (temp.getPiece() != null)
								if (temp.getX() == right && temp.getY() == down)
								{
									rightFlag = true;
								}
						}
					coordinates.add(new Tuple(right,down));
					right--;
				}
				down++;
			}
			left = x+1;
			right = x-1;
			rightFlag = false;
			leftFlag = false;
			while (up >= 1) {
				if (left <= 8 && leftFlag == false) {
					for (int i = 0; i < board.getBoardTiles().size(); i++)
						for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
						{
							ChessTile temp = board.getBoardTiles().get(i).get(j);
							if (temp.getPiece() != null)
								if (temp.getX() == left && temp.getY() == up)
								{
									leftFlag = true;
								}
						}
					coordinates.add(new Tuple(left,up));
					left++;
				}
				if (right >= 1 && rightFlag == false) {
					for (int i = 0; i < board.getBoardTiles().size(); i++)
						for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
						{
							ChessTile temp = board.getBoardTiles().get(i).get(j);
							if (temp.getPiece() != null)
								if (temp.getX() == right && temp.getY() == up)
								{
									rightFlag = true;
								}
						}
					coordinates.add(new Tuple(right,up));
					right--;
				}
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
