package game;
import ChessComponents.ChessBishop;
import ChessComponents.ChessKing;
import ChessComponents.ChessKnight;
import ChessComponents.ChessPawn;
import ChessComponents.ChessPiece;
import ChessComponents.ChessQueen;
import ChessComponents.ChessRook;

import java.io.PrintWriter;
import java.util.*;
import java.io.BufferedReader;


public class ChessPlayer {
	
	private char color;
	private List<ChessPiece> boardPieces, deadPieces; // ---- lists of board and dead pieces
	private boolean turnIsUp; // ---- true if it is this player's turn
	BufferedReader input;
	PrintWriter output;
	private String name;
	private ChessPlayer opponent;
	private boolean checked;
	
	public ChessPlayer(char c, String name) {
		this.color = c;
		this.setBoardPieces();
		this.opponent = null;
		this.input = null;
		this.output = null;
		this.name = name;
		this.checked = false;
	}

	public boolean checked() {
		return this.checked;
	}

	public void isChecked() {
		this.checked = true;
	}

	public void notChecked() {
		this.checked = false;
	}
	
	public String name() {
		return name;
	}

	public char getColor() {
		return color;
	}

	public void setColor(char c) {
		this.color = c;
	}

	public List<ChessPiece> getBoardPieces() {
		return boardPieces;
	}
	
	public void setBoardPieces() {
		boardPieces = new ArrayList<>();
		if (color == 'w') {
			for (int i = 1; i <= 8; i++)
				boardPieces.add(new ChessPawn(i,2, color));
			boardPieces.add(new ChessRook(1,1, color));
			boardPieces.add(new ChessKnight(2,1, color));
			boardPieces.add(new ChessBishop(3,1, color));
			boardPieces.add(new ChessKing(5,1, color));
			boardPieces.add(new ChessQueen(4,1, color));
			boardPieces.add(new ChessBishop(6,1, color));
			boardPieces.add(new ChessKnight(7,1, color));
			boardPieces.add(new ChessRook(8,1, color));
		}
		else{
			for (int i = 1; i <= 8; i++)
				boardPieces.add(new ChessPawn(i,7, color));
			boardPieces.add(new ChessRook(1,8, color));
			boardPieces.add(new ChessKnight(2,8, color));
			boardPieces.add(new ChessBishop(3,8, color));
			boardPieces.add(new ChessKing(5,8, color));
			boardPieces.add(new ChessQueen(4,8, color));
			boardPieces.add(new ChessBishop(6,8, color));
			boardPieces.add(new ChessKnight(7,8, color));
			boardPieces.add(new ChessRook(8,8, color));
		}
	}

	public List<ChessPiece> getDeadPieces() {
		return deadPieces;
	}

	public void setDeadPieces(List<ChessPiece> deadPieces) {
		this.deadPieces = deadPieces;
	}

	public boolean isTurnIsUp() {
		return turnIsUp;
	}

	public void setTurnIsUp(boolean turnIsUp) {
		this.turnIsUp = turnIsUp;
	}
	
	public void setInput(BufferedReader input) { 
		this.input = input;
	}
	
	public void setOutput(PrintWriter output) {
		this.output = output;
		}
	
	public BufferedReader getInput() {
		return input;
		}
	
	public PrintWriter getOutput() {
		return output;
	}

	public void setOpp(ChessPlayer opp) {
		opponent = opp;
	}

	public ChessPlayer getOpp() {
		return opponent;
	}
	
}
