package ChessComponents;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import Resources.Tuple;
import game.ChessPlayer;

public class ChessBoard {
	
	private int height, width, x, y, tileWidth, tileHeight;
	private List<List<ChessTile>> boardTiles;
	private ChessTile prevTile;
	private boolean drawn, turn; //turn is true when it's player 1s turn
	private Tuple currPieceCoord;
	private ChessPieceAlter cpa, fcpa;
	private ChessPlayer player1, player2;
	private boolean valid;
	private boolean enPassant;
	private char checkmated = ' ';
	private boolean castledLeft;
	private boolean castledRight;


	public ChessBoard(ChessPlayer player1, ChessPlayer player2) {
		valid = false;
		this.height = 720;
		this.width = 720;
		x = 360;
		y = 20;
		tileWidth = width/8;
		tileHeight = height/8;
		boardTiles = this.setBoardTiles();
		prevTile = null;
		drawn = false;
		this.player1 = player1;
		this.player2 = player2;
		this.turn = true;
		enPassant = false;
	}

	public boolean valid() {
		return valid;
	}

	public void turnOver() {
		if (turn == false) {
			turn = true;
		}
		else {
			turn = false;
		}
	}

	public void setPrevTile(ChessTile prev) {
		this.prevTile = prev;
	}


	public int prevX() {
		return prevTile.getX();
	}

	public int prevY() {
		return prevTile.getY();
	}

	public void setTurn(String turn) {
		if (turn.equals("player1")) {
			this.turn = true;
		}
		else {
			this.turn = false;
		}
	}
	
	public void draw(PaintEvent event, Shell shell, Canvas canvas) {
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++) {
				int color = (i+j)%2;
				boardTiles.get(i).get(j).draw(event, shell, canvas, color);
			}
		}
		// if board has not been initialized already, draw the labels for the board
		// TODO: need to add checks for what player is on which side, as it is relevent to the 
		// labeling of the board
		if (!drawn) {
			for (int i = 0; i < 8; i++) {
				event.gc.drawText(Character.toString((char) i + 97), x + width/18 + ((i) * width/8), 740, SWT.COLOR_WHITE);
				event.gc.drawText(Integer.toString(8 - i), 340, y + height/18 + ((i) * height/8), SWT.COLOR_WHITE);
				
				event.gc.drawText(Character.toString((char) i + 97), x + width/18 + ((i) * width/8), 0, SWT.COLOR_WHITE);
				event.gc.drawText(Integer.toString(8 - i), width + x + 10, y + height/18 + ((i) * height/8), SWT.COLOR_WHITE);
			}
		}
	}
	
	// ---- initializing tiles to the board
	private List<List<ChessTile>> setBoardTiles(){
		List<List<ChessTile>> ans = new ArrayList<>();
			for (int i = 0; i < 8; i++)
			{
				List<ChessTile> temp = new ArrayList<ChessTile>();
				for (int j = 0; j < 8; j++) {
					ChessTile var = new ChessTile(x+j*tileWidth,y+i*tileHeight, tileWidth, tileHeight);
					var.setX(j+1);
					var.setY(8-i);
					if (var.getY() == 1)
						var.setAtTheEndp2();
					if (var.getY() == 8) 
						var.setAtTheEndp1();
					temp.add(var);
				}
				ans.add(temp);
			}
		return ans;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
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

	public List<List<ChessTile>> getBoardTiles() {
		return boardTiles;
	}

	public void setBoardTiles(List<List<ChessTile>> boardTiles) {
		this.boardTiles = boardTiles;
	}
	
	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	/*
	 * Set SelectedTile sets the actual board, takes in 2 coordinates
	 */
	public void setSelectedTile(int x, int y) {
		List<Tuple> temp_coordinates = new ArrayList<>();
		List<Tuple> invalid_coordinates = new ArrayList<>();

		ChessTile selected = null;
		int selectedx = 0;
		int selectedy = 0;
		int tileX = 0;
		int tileY = 0;
		for (int i = 0; i < boardTiles.size(); i++)
			for (int j = 0; j < 8; j++)
				{
					ChessTile temp = boardTiles.get(i).get(j);
					if (temp.isSelected()) {
						if (temp.getPiece() != null) {
							invalid_coordinates = temp.getPiece().findPossibleMoves(this); //gathers previous coordinates			
						}			
						temp.unselect();
						temp.setUnavailable();
						prevTile = temp;
						break;
					}
		}
		
		//iterates through list of tiles to get selected tile
		for (int i = 0; i < boardTiles.size(); i++)
			for (int j = 0; j < 8; j++)
				{
					ChessTile temp = boardTiles.get(i).get(j);
					// checks to see if tile at (i, j) is the one being selected
					if (x >= temp.getxCoord() && x <= temp.getxCoord() + tileWidth
							&& y >= temp.getyCoord() && y <= temp.getyCoord() + tileHeight)
					{
						temp.select(); //selecting the tile
						tileX = temp.getX();
						tileY = temp.getY();
						if (temp.getPiece() == null) {
							if (enPassant) {
								System.out.println("at : " + temp.getX() + ", " + temp.getY());
								if (prevTile.getPiece().getName().equals("Pawn")) {
									if (prevTile.getPiece().getColor() == 'w') {
										System.out.println("White through Black");

										ChessTile possible = find(temp.getX(), temp.getY() - 1);
										// en passant white through black
										if (possible.getPiece() != null && possible.getPiece().getColor() == 'b') {
											System.out.println("En Passant");
											cpa.getDeadPieces().add(possible.getPiece());
											player2.getBoardPieces().remove(possible.getPiece());
										}
									}
									else {
										System.out.println("Black through White");
										
										ChessTile possible = find(temp.getX(), temp.getY() + 1);
										// en passant black through white
										if (possible.getPiece() != null && possible.getPiece().getColor() == 'w') {
											System.out.println("En Passant");
											fcpa.getDeadPieces().add(possible.getPiece());
											player1.getBoardPieces().remove(possible.getPiece());
										}
									}
								}
							}
							// if castledLeft or castledRight, allow for the player to castle their king to
							// the respective direction
							if (castledLeft || castledRight) {
								ChessPiece leftRook = find(1, prevTile.getPiece().getY()).getPiece();
								ChessPiece rightRook = find(8, prevTile.getPiece().getY()).getPiece();
								System.out.println("Checking for Castle");
								System.out.println(temp.getX() + ", " + temp.getY());
								if (temp.getX() == 3 && castledLeft) {
									System.out.println("Castling Left!");
									find(1, prevTile.getPiece().getY()).setPiece(null);
									leftRook.setX(4);
									find(leftRook.getX(), leftRook.getY()).setPiece(leftRook);
								}
								if (temp.getX() == 7 && castledRight) {
									System.out.println("Castling Right!");
									find(8, prevTile.getPiece().getY()).setPiece(null);
									rightRook.setX(6);
									find(rightRook.getX(), rightRook.getY()).setPiece(rightRook);
								}
							}
							// reset everything
							enPassant = false;
							castledLeft = false;
							castledRight = false;
						}

						// if tile has a piece
						if (temp.getPiece() != null) {
							if ((turn == true && temp.getPiece().getColor() == 'w')||
									(turn == false && temp.getPiece().getColor() == 'b')) {

								selected = temp;
								selectedx = i;
								selectedy = j;
								
								// code for castling
								if (temp.getPiece().getName().equals("King") && temp.getPiece().getNumMoves() == 0) {
									System.out.println("King selected and has not moved");

									ChessPiece leftRook = find(1, temp.getPiece().getY()).getPiece();
									ChessPiece rightRook = find(8, temp.getPiece().getY()).getPiece();

									System.out.println("WHERE AM I");

									// If the Rooks exist and have not moved, check if there is space in between them
									if (leftRook != null && leftRook instanceof ChessRook && leftRook.getNumMoves() == 0) {
										boolean leftClear = true;

										ChessKing king = (ChessKing) temp.getPiece();

										// Check if the piece is able to castle to the right by checking if the space is null, then checking
										// if it is not null checks if the king moves there and is in check

										for (int p = 2; p < 5; p++) {
											if (find(p, temp.getPiece().getY()).getPiece() != null) {
												System.out.println("Cannot Castle Left, Other Pieces are blocking");
												leftClear = false;
												break;
											}
											else {
												System.out.println("Checking for check");
												int ogx = temp.getPiece().getX();
												temp.setPiece(null);
												king.setX(p);
												ChessTile fake = find(p, king.getY());
												fake.setPiece(king);
												if (threats(king).size() != 0) {
													leftClear = false;
													System.out.println("King cannot make it all the way through without being checked");
												}
												fake.setPiece(null);
												temp.setPiece(king);
												king.setX(ogx);
												if(!leftClear) {
													break;
												}
											}
										}

										// if left side is clear to castle, allows for castling
										if (leftClear) {
											System.out.println("King at " + king.getX() +", "  + king.getY());
											System.out.println("Able to Castle Left");
											System.out.println(threats(king));
											castledLeft = true;
											temp_coordinates.add(new Tuple(leftRook.getX() + 2, leftRook.getY()));
										}
									}

									if (rightRook != null && rightRook instanceof ChessRook && rightRook.getNumMoves() == 0) {
										boolean rightClear = true;

										ChessKing king = (ChessKing) temp.getPiece();
										
										// Check if the piece is able to castle to the right by checking if the space is null, then checking
										// if it is not null checks if the king moves there and is in check

										for (int p = 6; p < 8; p++) {
											if (find(p, temp.getPiece().getY()).getPiece() != null) {
												System.out.println("Cannot Castle right, Other Pieces are blocking");
												rightClear = false;
												break;
											}
											else {
												System.out.println("Checking for check");
												int ogx = temp.getPiece().getX();
												temp.setPiece(null);
												king.setX(p);
												ChessTile fake = find(king.getX(), king.getY());
												fake.setPiece(king);
												if (threats(king).size() != 0) {
													rightClear = false;
													System.out.println("King cannot make it all the way through without being checked");
												}
												fake.setPiece(null);
												temp.setPiece(king);
												king.setX(ogx);
												if (!rightClear) {
													break;
												}
											}
										}

										// if right side is clear to castle, allows for castling
										if (rightClear) {
											System.out.println("King at " + king.getX() +", "  + king.getY());
											System.out.println(threats(king));
											System.out.println("Able to Castle Right");
											castledRight = true;
											temp_coordinates.add(new Tuple(rightRook.getX() - 1, rightRook.getY()));
										}
									}
									
								}
									
								//code for enPassant

								if (prevTile != null) {
									if (prevTile.getPiece()!= null) {
										if (prevTile.getPiece().getName().equals("Pawn") && temp.getPiece().getName().equals("Pawn")) {
											if (temp.getPiece().getNumMoves() != 0 && prevTile.getPiece().getNumMoves() == 1) {
												System.out.println("En Passant Possible");
												// check if current pawn is white, in position and able to take a nearby pawn
												if (temp.getPiece().getColor() == 'w') {
													System.out.println("Current Pawn is White");	
													if (prevTile.getPiece().getColor() == 'b' && temp.getPiece().getY() == 5) {
														if (prevTile.getPiece().getY() == temp.getY()) {
															if (prevTile.getPiece().getX() == temp.getX() + 1 || prevTile.getPiece().getX() == temp.getX() - 1) {
																System.out.println("Target Pawn is Black");
																temp_coordinates.add(new Tuple(prevX(), prevY() + 1));
																enPassant = true;
															}
														}
													}
												}
												else {
													System.out.println("Current Pawn is Black");
													// check if current pawn is black, in position and able to take a nearby pawn
													if (prevTile.getPiece().getColor() == 'w' && temp.getPiece().getY() == 4) {
														if (prevTile.getPiece().getY() == temp.getY()) {
															if (prevTile.getPiece().getX() == temp.getX() - 1 || prevTile.getPiece().getX() == temp.getX() + 1) {
																System.out.println("Target Pawn is White");
																temp_coordinates.add(new Tuple(prevX(), prevY() - 1));
																enPassant = true;
															}
														}
													}
												}
											}
										}
									}
								}
										

							}		
						}
						if (temp.isAvailable()) {

							ChessPiece piece = find(prevTile.getX(), prevTile.getY()).getPiece();
							System.out.println("Board: " + temp.getX() + ", " + temp.getY());
							if(piece != null)
								if (turn == true && piece.getColor() == 'w') {
									ChessPiece pos = find(temp.getX(), temp.getY()).getPiece();
									if (pos != null && pos.getX() == temp.getX() && pos.getY() == temp.getY())
									{
										// player 2 had a piece taken
										cpa.getDeadPieces().add(pos);
										player2.getBoardPieces().remove(pos);
										System.out.println("Removing " + pos.getName());
									}
													
									piece.increaseNumMoves();
									piece.setX(temp.getX());
									piece.setY(temp.getY());

									//if pawn hits the end (setting to new queen automatically for now
									if (prevTile.getPiece().getName().equals("Pawn")) {
										if (temp.isAtTheEndp1()) {
											int pieceXcoord = piece.getxCoord();
											int pieceYcoord = piece.getyCoord();
											piece = new ChessQueen(piece.getX(), piece.getY(), 'w');
											piece.setxCoord(pieceXcoord);
											piece.setyCoord(pieceYcoord);
											player1.getBoardPieces().add(piece);
										}
									}
									turn = !turn;
									temp.setPiece(piece);
									prevTile.setPiece(null);
									// set pieces and check for check and checkmate
									if(this.inCheck('b')) {
										System.out.print("black in check from board\n");
										if(this.isCheckmated('b')) {
											System.out.print("checkmate");
										}
									}
									
								}
								else if (turn == false && piece.getColor() == 'b') {
									ChessPiece pos = find(temp.getX(), temp.getY()).getPiece();
									if (pos != null && pos.getX() == temp.getX() && pos.getY() == temp.getY())
									{
										// player 2 had a piece taken
										fcpa.getDeadPieces().add(pos);
										player1.getBoardPieces().remove(pos);
									}

									piece.increaseNumMoves();
									piece.setX(temp.getX());
									piece.setY(temp.getY());
									// if the previous tile is a pawn, spawn in a queen in its place
									if (prevTile.getPiece().getName().equals("Pawn")) 
										if (temp.isAtTheEndp2()) {
											int pieceXcoord = piece.getxCoord();
											int pieceYcoord = piece.getyCoord();
											piece = new ChessQueen(piece.getX(), piece.getY(), 'b');
											piece.setxCoord(pieceXcoord);
											piece.setyCoord(pieceYcoord);
											player2.getBoardPieces().add(piece);
									}
									turn = !turn;
									temp.setPiece(piece);
									prevTile.setPiece(null);
									// set piece and then check for checkmate/check
									if(this.inCheck('w')) {
										System.out.print("white in check from board\n");
										if(this.isCheckmated('w')) {
											System.out.print("checkmate");
										}
									}
								}
						}
					}
				}
	
		// ensures invalid_coordinates doesn't include coordinates that should be available
		this.setUnavailableTiles();
		if (selected != null)
			this.setAvailableTiles(selectedx, selectedy, selected.getPiece().findPossibleMoves(this), castledLeft, castledRight, tileX, tileY);
		this.setAvailableTiles(selectedx, selectedy, temp_coordinates, castledLeft, castledRight, tileX, tileY);
	}
	
	//determines what tiles a piece can move to, and determines if the king it in check.
	public void setAvailableTiles(int x, int y, List<Tuple> coordinates, boolean castledLeft, boolean castledRight, int tileX, int tileY) {
		List<Tuple> can = new ArrayList<>();
		ChessTile foo = boardTiles.get(x).get(y);
		List<ChessPiece> threatening = new ArrayList<>();
		boolean reduce = false;

		if (foo.getPiece() != null) {
			ChessPiece piece = foo.getPiece();
			if (inCheck(piece.getColor())) {
				reduce = true;
				ChessKing king = getKing(foo.getPiece().getColor());
				threatening = threats(king);
				ChessPiece m = piece;
				System.out.println(piece.getName() + " selected");

				if (piece.equals(king)) {
					for (Tuple t: moves(king)) {

						ChessTile currTile = find(king.getX(), king.getY());
						
						ChessTile fakeTile = find(t.getX(), t.getY());
						ChessPiece fake = fakeTile.getPiece();

						fakeTile.setPiece(king);
						king.setX(t.getX());
						king.setY(t.getY());

						currTile.setPiece(null);

						threatening = threats(king);

						fakeTile.setPiece(fake);
						king.setX(currTile.getX());
						king.setY(currTile.getY());

						currTile.setPiece(king);

						System.out.println(threatening);
						if (threatening.size() == 0) {
							can.add(t);
						}
					}
				}
				threatening = threats(king);
					for (ChessPiece c: threatening) {
						List<Tuple> threatMoves = new ArrayList<>();
						threatMoves = moves(c);
						List<Tuple> currMoves = new ArrayList<>();
						currMoves = moves(m);
	
						for (Tuple p: currMoves) {
							for (Tuple z: threatMoves) {
	
								if (threatening.contains(find(p.getX(),p.getY()).getPiece())) {
									// Check if piece is able to kill and also still work
									System.out.println(find(p.getX(),p.getY()).getPiece().getName() + " found");

									ChessTile temp = find(p.getX(), p.getY());
									ChessPiece taken = temp.getPiece();

									temp.setPiece(null);
	
									int ogx = m.getX();
									int ogy = m.getY();
	
									m.setX(p.getX());
									m.setY(p.getY());
									
									int check = 1;
									temp.setPiece(m);
									if (king.getColor() == 'w') {
										player2.getBoardPieces().remove(taken);	
										check = threats(king).size();	
										player2.getBoardPieces().add(taken);
									}
									else {
										player1.getBoardPieces().remove(taken);
										check = threats(king).size();	
										player1.getBoardPieces().add(taken);
									}

									System.out.println(threats(king));
									// Reset the tile and the piece
									temp.setPiece(null);
	
									m.setX(ogx);
									m.setY(ogy);
	
									temp.setPiece(taken);
	
									if (check == 0) {
										can.add(p);
									}
								}
								else if (z.getX() == p.getX() && z.getY() == p.getY()) {
									// Piece can move into line of sight of threatening piece
									System.out.println("My" + m.getName() + " can block at " + z.getX() + ", " + z.getY());
	
									// Now check if piece can actually defend the king
									// Temp move the piece into position, then move it back out to check
									int ogx = m.getX();
									int ogy = m.getY();
	
									m.setX(p.getX());
									m.setY(p.getY());
	
									ChessTile tile = find(p.getX(), p.getY());
									tile.setPiece(m);
									
									int check = threats(king).size();
	
									// Reset the tile and the piece
									tile.setPiece(null);
	
									m.setX(ogx);
									m.setY(ogy);
									
	
									if (check == 0) {
										can.add(new Tuple(p.getX(), p.getY()));
									}
								}
							}
						}
				}
			}
			System.out.println(can);
		}

		if (reduce) {
			for (Tuple t: can) {
				find(t.getX(), t.getY()).setAvailable();
			}
		}
		else {
			ChessPiece piece = foo.getPiece();
			System.out.println("Available Check: " + foo.getX() + ", " + foo.getY());
			

			for (int i = 0; i < boardTiles.size(); i++)
			for (int j = 0; j < 8; j++)
				{
					ChessTile temp = boardTiles.get(i).get(j);
					for (Tuple c: coordinates) {
						if (temp.getX() == c.getX() && temp.getY() == c.getY()) {	
							int ogx = piece.getX();
							int ogy = piece.getY();
							if (temp.getPiece() != null)
							{
								if (temp.getPiece().getColor() != piece.getColor()){
									foo.setPiece(null);
									piece.setX(c.getX());
									piece.setY(c.getY());

									ChessPiece taken = temp.getPiece();
									temp.setPiece(piece);

									if (taken.getColor() == 'w') {
										player1.getBoardPieces().remove(taken);
									}
									else {
										player2.getBoardPieces().remove(taken);
									}

									if (threats(getKing(piece.getColor())).size() == 0) {
										temp.setAvailable();
									}

									if (taken.getColor() == 'w') {
										player1.getBoardPieces().add(taken);
									}
									else {
										player1.getBoardPieces().add(taken);
									}

									temp.setPiece(taken);

									piece.setX(ogx);
									piece.setY(ogy);
									foo.setPiece(piece);
								}
							}
							else {
								foo.setPiece(null);
								piece.setX(temp.getX());
								piece.setY(temp.getY());
								if (threats(getKing(piece.getColor())).size() == 0) {
									temp.setAvailable();
								}
								piece.setX(ogx);
								piece.setY(ogy);
								foo.setPiece(piece);
							}
						}
					}
			}
		}
	}
	
	//resets all tiles to unavailable so availability can be set again
	public void setUnavailableTiles() {
		for (int i = 0; i < boardTiles.size(); i++)
			for (int j = 0; j < 8; j++)
				{
					ChessTile temp = boardTiles.get(i).get(j);
					temp.setUnavailable();
			}
	}

	public ChessTile find(int x, int y) {
		for (List<ChessTile> c: boardTiles) {
			for (ChessTile f: c) {
				if (f.getX() == x && f.getY() == y) {
					return f;
				}
			}
		}
		return null;
	}

	public void setCPA(ChessPieceAlter cpa) {
		this.cpa = cpa;
	}

	public void setFCPA(ChessPieceAlter fcpa) {
		this.fcpa = fcpa;
	}

	public boolean turn() {
		return this.turn;
	}

	public String turnString() {
		if (turn) {
			return "player1";
		}
		else {
			return "player2";
		}
	}
	
	public boolean inCheck(char player) {
		List<ChessPiece> whitePieces = player1.getBoardPieces();
		List<ChessPiece> blackPieces = player2.getBoardPieces();
		ChessKing king = null;
		ChessPlayer p = null;

		if (player == 'w') {
			// white king being threatened
			// find white king
			for (ChessPiece k: whitePieces) {
				if (k.getName().equals("King")) {
					king = (ChessKing) k;
					break;
				}
			}
			p = player1;
		}
		else {
			// black king being threatened
			// find black king
			for (ChessPiece k: blackPieces) {
				if (k.getName().equals("King")) {
					king = (ChessKing) k;
					break;
				}
			}
			p = player2;
		}

		if (threats(king).size() == 0) {
			p.notChecked();
			return false;
		}
		else {
			System.out.println("Check threats: " + threats(king).get(0).getColor());
			p.isChecked();
			return true;
		}
	}

	public char checkmated() {
		return checkmated;
	}
	
	public boolean isCheckmated(char player) {
		List<ChessPiece> whitePieces = player1.getBoardPieces();
		List<ChessPiece> blackPieces = player2.getBoardPieces();
		List<Tuple> kingMoves = new ArrayList<>();
		ChessKing king = null;
		List<ChessPiece> myPieces = null;

		List<ChessPiece> threatening = new ArrayList<>();

		king = getKing(player);
		
		// first check for pieces currently threatening the king
		if (player == 'w') {
			// set pieces
			myPieces = whitePieces;
		}
		else {
			// set pieces
			myPieces = blackPieces;
		}

		// if king doesn't exist return false, game should be over by now
		if (king == null) {
			return false;
		}

		// for all of the kings possible moves check if there are threats
		System.out.println("Checking all of king's possible moves...");
		System.out.println("Currently King at " + king.getX() + ", " + king.getY());

		// get available moves
		kingMoves = moves(king);

		if (threats(king).size() == 0) {
			return false;
		}

		System.out.println("King can move to " + kingMoves);
		
		// check king immediate moves
		for (Tuple t: kingMoves) {
			ChessKing fake = new ChessKing(t.getX(), t.getY(), king.getColor());

			ChessTile currTile = find(king.getX(), king.getY());


			currTile.setPiece(null);

			threatening = threats(fake);

			currTile.setPiece(king);
			if (threatening.contains(find(t.getX(),t.getY()).getPiece())) {
				// Check if piece is able to kill and also still work
				ChessTile temp = find(t.getX(), t.getY());
				ChessPiece taken = temp.getPiece();

				temp.setPiece(null);

				int ogx = king.getX();
				int ogy = king.getY();

				king.setX(t.getX());
				king.setY(t.getY());

				temp.setPiece(king);
				
				int check = threats(king).size();

				// Reset the tile and the piece
				temp.setPiece(null);

				king.setX(ogx);
				king.setY(ogy);

				temp.setPiece(taken);

				if (check == 0) {
					return false;
				}
			}

			System.out.println("Checking " + fake.getX() + ", " + fake.getY());
			if (threatening.size() == 0) {
				// if there is nothing threatening the king at this move then it is not checkmated
				return false;
			}
			// check if own pieces can block right now
			for (ChessPiece c: threatening) {
				List<Tuple> threatMoves = new ArrayList<>();
				threatMoves = moves(c);
				for (ChessPiece m: myPieces) {
					List<Tuple> currMoves = new ArrayList<>();
					currMoves = moves(m);
					for (Tuple p: currMoves) {
						for (Tuple z: threatMoves) {
							if (threatening.contains(find(p.getX(),p.getY()).getPiece())) {
								// Check if piece is able to kill and also still work
								ChessTile temp = find(p.getX(), p.getY());
								ChessPiece taken = temp.getPiece();

								temp.setPiece(null);

								int ogx = m.getX();
								int ogy = m.getY();

								m.setX(p.getX());
								m.setY(p.getY());

								temp.setPiece(m);
								
								int check = threats(king).size();

								// Reset the tile and the piece
								temp.setPiece(null);

								m.setX(ogx);
								m.setY(ogy);

								temp.setPiece(taken);

								if (check == 0) {
									return false;
								}
							}
							else if (z.getX() == p.getX() && z.getY() == p.getY()) {
								// Piece can move into line of sight of threatening piece

								// Now check if piece can actually defend the king
								// Temp move the piece into position, then move it back out to check
								int ogx = m.getX();
								int ogy = m.getY();

								m.setX(p.getX());
								m.setY(p.getY());

								ChessTile tile = find(p.getX(), p.getY());
								tile.setPiece(m);
								
								int check = threats(king).size();

								// Reset the tile and the piece
								tile.setPiece(null);

								m.setX(ogx);
								m.setY(ogy);
								
								// if king is still threatened, piece moving did not do anything!
								if (check == 0) {
									return false;
								}
							}
						}
					}
				}
			}
		}


		// checkmated!
		checkmated = king.getColor();
		return true;
	}

	private ChessKing getKing(char player) {
		List<ChessPiece> whitePieces = player1.getBoardPieces();
		List<ChessPiece> blackPieces = player2.getBoardPieces();
		ChessKing king = null;
		// first check for pieces currently threatening the king
		if (player == 'w') {
			// white king being threatened
			// find white king
			for (ChessPiece k: whitePieces) {
				if (k.getName().equals("King")) {
					king = (ChessKing) k;
					break;
				}
			}
		}
		else {
			// black king being threatened
			// find black king
			for (ChessPiece k: blackPieces) {
				if (k.getName().equals("King")) {
					king = (ChessKing) k;
					break;
				}
			}
		}

		return king;
	}



	private List<Tuple> moves(ChessPiece piece) {
		List<Tuple> pieceMoves = new ArrayList<>();;
		for (Tuple t: piece.findPossibleMoves(this)) {
			ChessTile curr = find(t.getX(), t.getY());
			if (curr.getPiece() != null) {
				System.out.println("piece is not null");
				System.out.println(curr.getPiece());
				if (curr.getPiece().getColor() != piece.getColor()) {
					pieceMoves.add(t);
				}
			}
			else {
				pieceMoves.add(t);
			}
		}
		return pieceMoves;
	}

	private List<ChessPiece> threats(ChessKing king) {
		List<ChessPiece> threatening = new ArrayList<>();
		List<ChessPiece> oppPieces = new ArrayList<>();

		if (king == null) {
			return threatening;
		}

		if (king.getColor() == 'w') {
			oppPieces = player2.getBoardPieces();
		}
		else {
			oppPieces = player1.getBoardPieces();
		}

		// first find what pieces immediately threaten the king	
		for (ChessPiece p: oppPieces) {
			// find moves that opponenets can move to
			if (p != null) {
				List<Tuple> bMoves = p.findPossibleMoves(this);
				// check if moves can touch the king
				for (Tuple t: bMoves) {
					if (t.getX() == king.getX() && t.getY() == king.getY() && p.getColor() != king.getColor()) {
						// king is found, add chesspiece that threatens to threatens
						if (!p.getName().equals("pawn")) {
							threatening.add(p);
						}
					}
				}
				if (p.getName().equals("Pawn") && p.getColor() != king.getColor()) {
					if (king.getColor() == 'w') {
						if (p.getY() == king.getY() + 1 && (p.getX() == king.getX() + 1|| p.getX() == king.getX() - 1)) {
							threatening.add(p);
						}
					}
					else {
						if (p.getY() == king.getY() - 1 && (p.getX() == king.getX() + 1|| p.getX() == king.getX() - 1)) {
							threatening.add(p);
						}
					}
				}
			}
		}

		return threatening;
	}
}

	