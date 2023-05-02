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
import java.util.List;

public class ChessPieceAlter {
	
	private List<ChessPiece> deadPieces;
	private int x, y;
	private boolean kingDead;
	
	public ChessPieceAlter(int x, int y) {
		deadPieces = new ArrayList<ChessPiece>();
		this.x = x;
		this.y = y;
		kingDead = false;
	}
	
	public void draw(PaintEvent event, Shell shell, Canvas canvas) {
		event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
		event.gc.fillRectangle(x,y,300,640);
		event.gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		event.gc.drawRectangle(x,y,300,640);
		int count = -50;
		for (int i = 0; i < deadPieces.size(); i++) {
			ChessPiece c = deadPieces.get(i);
			c.setxCoord(x + 30 + (i%3)*90);
			if (i % 3 == 0)
				count += 100;
			c.setyCoord(y + count);
			c.draw(event, shell, canvas);
		}
	}

	public List<ChessPiece> getDeadPieces() {
		return deadPieces;
	}

	public void setDeadPieces(List<ChessPiece> deadPieces) {
		this.deadPieces = deadPieces;
	}

	public boolean kingDead() {
		for (ChessPiece c: deadPieces) {
			if (c instanceof ChessKing) {
				kingDead = true;
			}
		}
		return kingDead;
	}

}
