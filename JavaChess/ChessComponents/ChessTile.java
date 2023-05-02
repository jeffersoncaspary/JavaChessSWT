package ChessComponents;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

//class representing each tile on the board.
public class ChessTile {

	private int width, height, xCoord, yCoord, x, y;
	private boolean isSelected, isAvailable, isAtTheEndp1, isAtTheEndp2;
	private ChessPiece piece;
	
	public ChessTile(int xCoord, int yCoord, int width, int height) {
		this.width = width;
		this.height = height;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.x = 0;
		this.y = 0;
		isSelected = false;
		isAvailable = false;
		isAtTheEndp1 = false;
		isAtTheEndp2 = false;
		this.piece = null;
	}
	
	//draws the chess board (without pieces)
	public void draw(PaintEvent event, Shell shell, Canvas canvas, int color) {
		if (color == 0)
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		else
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if (isAvailable)
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_CYAN));
		if (isSelected)
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		event.gc.fillRectangle(xCoord,yCoord,width,height);
		event.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		event.gc.drawRectangle(xCoord,yCoord,width,height);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable() {
		this.isAvailable = true;
	}
	
	public void setUnavailable() {
		this.isAvailable = false;
	}
	
	public boolean isAtTheEndp1() {
		return isAtTheEndp1;
	}

	public void setAtTheEndp1() {
		this.isAtTheEndp1 = true;
	}
	
	
	public boolean isAtTheEndp2() {
		return isAtTheEndp2;
	}

	public void setAtTheEndp2() {
		this.isAtTheEndp2 = true;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void select() {
		this.isSelected = true;
	}
	
	public void unselect() {
		this.isSelected = false;
	}

	public ChessPiece getPiece() {
		return piece;
	}

	public void setPiece(ChessPiece piece) {
		this.piece = piece;
	}
		
}
