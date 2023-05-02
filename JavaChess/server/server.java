package server;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;

import javax.xml.crypto.Data;

import ChessComponents.ChessPiece;
import game.ChessPlayer;


/**
 * A server for a multi-player chess game.
 */
public class server {
    public static void main(String[] args) throws Exception 
    {
		System.out.println("Chess hosted on: " + InetAddress.getLocalHost().getHostAddress());
        try (var listener = new ServerSocket(59896)) 
        {
			listener.setReuseAddress(true);
            System.out.println("Chess is Running...");
            var pool = Executors.newFixedThreadPool(2);
            while (true) 
            {
				Game game = new Game();
                pool.execute(new Play(listener.accept(), new ChessPlayer('w', "player1"), game));
				pool.execute(new Play(listener.accept(), new ChessPlayer('b', "player2"), game));  
            }
        }
    }
}

class Game 
{
    // do %8 for now on the board to get row/column
    private ChessPiece[] chessBoard = new ChessPiece[64];
    private ChessPlayer player1 = null;
    private ChessPlayer player2 = null;
    private ChessPlayer currentPlayer = null;
	private boolean ready = false;
	
	public boolean ready() {return ready;}
	public void setReady(boolean ready) {this.ready = ready;}
    public ChessPiece[] getBoard() {return chessBoard;}
    public ChessPlayer getCurrentPlayer() {return currentPlayer;}
    public void setCurrentPlayer(ChessPlayer player) {currentPlayer = player;}
    public ChessPlayer getPlayer1(){return player1;}
    public void setPlayer1(ChessPlayer player) {player1 = player;}
    public ChessPlayer getPlayer2(){return player2;}
    public void setPlayer2(ChessPlayer player) {player2= player;}
}

class Play implements Runnable 
{
	ChessPlayer you;
	ChessPlayer opponent=null;
	Game game;
	Socket socket;
	BufferedReader in;
    PrintWriter out;
	

	public Play(Socket socket, ChessPlayer player, Game game) throws IOException 
	{
		System.out.println(player.name() + " has connected!");
		this.socket = socket;
		this.you = player;
		this.game = game;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

		you.setInput(in);
		you.setOutput(out);

		game.setCurrentPlayer(you);

		if (game.getPlayer1() == null) {
			game.setPlayer1(you);
			// Send out name to p1
		}
		else {
			game.setPlayer2(you);
			// Send out name to p2
		}
		out.println(you.name());
		if (game.getPlayer1() != null && game.getPlayer2() != null) {
			game.getPlayer1().setOpp(game.getPlayer2());
			game.getPlayer2().setOpp(game.getPlayer1());
			game.setReady(true);
			opponent = you.getOpp();
			opponent.setOpp(you);
			System.out.println("Game is Ready");
			you.getOutput().println("Game is Ready");
			opponent.getOutput().println("Game is Ready");
		}
	}

	@Override
	public void run() 
	{
		try 
		{
			while (true) {
				String inp = in.readLine();
				System.out.println(inp);
				if (inp.startsWith("turn end")) {
					you.getOpp().getOutput().println(inp.substring(20));
					inp = in.readLine();
					you.getOpp().getOutput().println(inp.substring(20));
					you.getOpp().getOutput().println("your turn");
				}

				if (inp.startsWith("timer")) {
					you.getOpp().getOutput().println(inp);
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}
}