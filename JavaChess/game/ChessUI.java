package game;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OUTLINETEXTMETRIC;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import ChessComponents.ChessBoard;
import ChessComponents.ChessPiece;
import ChessComponents.ChessPieceAlter;
import ChessComponents.ChessTile;


public class ChessUI {
	
	private ChessBoard board;
	private ChessPieceAlter cpa, fcpa;
	private Canvas canvas;
	private Display display;
	private ChessPlayer player1, player2;
	private static Font font;
	private boolean ai;
	private boolean multi;
	private Shell shellMain;
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private int p1Timer;
	private int p2Timer;
	private boolean ready;
	private String me;
	private int tempx;
	private int tempy;

	private boolean finished;
	private boolean won;
	private boolean stalemate;
	private boolean timeOut;

	// Constructor
	public ChessUI() {
		in = null;
		out = null;
		display = new Display();
		reset();
	}
	
	/*
	 * Resets the data needed for a fresh new game
	 */
	public void reset() {
		stalemate = false;
		finished = false;
		won = false;
		ready = false;
		multi = false;
		ai = true;
		player1 = new ChessPlayer('w', "player1");
		player2 = new ChessPlayer('b', "player2");
		board = new ChessBoard(player1, player2);
		cpa = new ChessPieceAlter(board.getX()+board.getWidth()+45,60);
		fcpa = new ChessPieceAlter(board.getX()-345,60);
		board.setCPA(cpa);
		board.setFCPA(fcpa);

		for (int i = 0; i < board.getBoardTiles().size(); i++)
			for (int j = 0; j < board.getBoardTiles().get(i).size(); j++)
				{
					ChessTile temp = board.getBoardTiles().get(i).get(j);
					for (ChessPiece p: player1.getBoardPieces()) {
						if (temp.getX() == p.getX() && temp.getY() == p.getY())
							temp.setPiece(p);
						
					}
					for (ChessPiece p: player2.getBoardPieces()) {
						if (temp.getX() == p.getX() && temp.getY() == p.getY())
							temp.setPiece(p);
					}
			}

		startup();
	}

	/*
	 * This function will connect our game to the given ip server, if it exists
	 */
	public void connect(String host, Shell shell) throws UnknownHostException, IOException {
		System.out.println("Attempting to Connect to: " + host);

		try (Socket s = new Socket(host, 59896)) 
        {
			shell.close();
			System.out.println(s);
			System.out.println("Connected to " + host + "!");
			multi = true;
			start(s);
        }
	}

	/*
	 * This function will set up all the multiplayer needed to start communicating back and forth between a server
	 */
	public void multiplayer(){
		System.out.println("\nYou have selected Multiplayer\n");
		
		// Create new Multiplayer shell
		Shell shell = new Shell(display);
		shell.setBounds(600, 600, 500, 500);
		shell.setText("Multiplayer Connect");
		Rectangle shellDim = shell.getBounds();

		// text input
		Text text = new Text(shell, SWT.BORDER);
		text.setBounds((shellDim.width/2) - 100, 200, 200, 30);
		Label adLabel = new Label(shell, 0);

		// title for text input
		adLabel.setText("Enter Server IP Address: ");
		adLabel.setBounds((shellDim.width/2) - 75, 170, 200, 30);
		
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Connect");
		button.setBounds((shellDim.width/2) - 50, 250, 100, 30);

		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Attempting to connect..."); 
				try {
					connect(text.getText(), shell);
				} catch (IOException e) {
					System.out.println("IP not valid, or Game does not exist.");
					Label error = new Label(shell, 0);
					error.setText("Error Connecting");
					error.setBounds((shellDim.width/2) - 60, 150, 150, 30);
					Color red = display.getSystemColor(SWT.COLOR_RED);
					error.setForeground(red);
				}
			}
			
		});

		Button back = new Button(shell, SWT.PUSH);
		back.setText("Back");

		// listener for back button just goes back to startup
		back.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Going back to Startup Menu..."); 
				shell.close();
				startup();
			}
			
		});

		back.setBounds((shellDim.width/2) - 38, 300, 75, 30);
		shell.open();
		


		while (!shell.isDisposed()) 
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();	

	}

	/*
	 * Startup Popup for pregame settings, allows for server implementation if selected
	 */
	public void startup() {
		System.out.println("StartUp Menu");

		Shell shell = new Shell(display);
		shell.setBounds(600, 600, 500, 500);
		shell.setText("Chess Startup");
		Rectangle shellDim = shell.getBounds();

		Group gameMode = new Group(shell, SWT.NONE);
		gameMode.setLayout(new RowLayout(SWT.VERTICAL));
		gameMode.setText("Select Game Mode");

		Button selection = new Button(gameMode, SWT.RADIO);
		selection.setText("Single Player");

		selection.addSelectionListener(new SelectionAdapter() {
			@Override
            public void widgetSelected(SelectionEvent e) {
                if (selection.getSelection()) {
					System.out.println("SinglePlayer Selected");
					ai = true;
				 }
            }
		});

		Button mselection = new Button(gameMode, SWT.RADIO);
		mselection.setText("Multiplayer");

		mselection.addSelectionListener(new SelectionAdapter() {
			@Override
            public void widgetSelected(SelectionEvent e) {
				 if (mselection.getSelection()) {
					System.out.println("MultiPlayer Selected");
					ai = false;
				 }
            }
		});

		gameMode.setBounds((shellDim.width/2) - 100, 0, 200, 100);

		Button start = new Button(shell, SWT.PUSH);
		start.setText("Start");

		start.setBounds((shellDim.width/2) - 60, (shellDim.height/2) + 100, 100, 30);
		start.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Start Button Pressed");
				System.out.println("SinglePlayer: " + ai);
				shell.close();
				if (ai) {
					try {
						start(null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
					multiplayer();
				}
			}
		});

		shell.open();
		while (!shell.isDisposed()) 
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();	
	}
	
	// ---- runs the UI
	public void start(Socket s) throws IOException
	{
		
		// Timers
		p1Timer = 10 * 60;
		p2Timer = 10 * 60;

		shellMain = new Shell(display);
		shellMain.setText("Chess");
		shellMain.setLayout(new FillLayout());
		shellMain.setSize(1440, 800);
		canvas = new Canvas(shellMain, SWT.DOUBLE_BUFFERED);
		font = new Font( display, "Helvetica", 16, SWT.BOLD);
		/*
		// ---- creating timer to refresh canvas every 100th of a second
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {gl 
                    public void run() {
                        canvas.redraw();
                    }
				});
			} 
		}; 
		timer.scheduleAtFixedRate(task, 10, 10);
		*/
		
		Color bColor = display.getSystemColor(SWT.COLOR_DARK_YELLOW);
		canvas.setBackground(bColor);

		canvas.addPaintListener(event -> {
			event.gc.setFont(font);
			event.gc.fillRectangle(canvas.getBounds());
			board.draw(event, shellMain, canvas);
			
			cpa.draw(event, shellMain, canvas);
			fcpa.draw(event, shellMain, canvas);
			for (ChessPiece p: player1.getBoardPieces()) {
				p.draw(event, shellMain, canvas);
			}
			for (ChessPiece p: player2.getBoardPieces()) {
				p.draw(event, shellMain, canvas);
			}
			
			event.gc.setBackground(bColor);
			event.gc.drawText(player1.name(), 125, 0);
			event.gc.drawText(player2.name(), 1250, 0);



			event.gc.drawText(time(p1Timer), 135, 25);
			event.gc.drawText(time(p2Timer), 1265, 25);
			
			event.gc.setBackground(bColor);
			event.gc.drawText(player1.name(), 125, 0);
			event.gc.drawText(player2.name(), 1250, 0);

			event.gc.drawText(time(p1Timer), 135, 25);
			event.gc.drawText(time(p2Timer), 1265, 25);
			
			boolean hasKing1 = false;
			for (ChessPiece p: player1.getBoardPieces()) 
				if (p.getName().equals("King")) {
					hasKing1 = true;
					break;
				}
			boolean hasKing2 = false;
			for (ChessPiece p: player2.getBoardPieces()) 
				if (p.getName().equals("King")) {
					hasKing2 = true;
					break;
				}
			
			//win, lose, and tie conditions
			if(!hasKing1 || !hasKing2 || stalemate || board.checkmated() != ' ') {
				event.gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
				event.gc.setFont(new Font( display, "Helvetica", 50, SWT.BOLD));
				event.gc.drawText("GAME OVER", 570, 300);
				event.gc.setFont(new Font( display, "Helvetica", 45, SWT.BOLD));
				if (stalemate) {
					event.gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
					event.gc.drawText("StaleMate!", 485, 400);
				}
				else if (!hasKing1 || board.checkmated() == 'w') {
					event.gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
					event.gc.drawText("Player 2 is the winner!", 485, 400);
					if (board.checkmated() == 'w') {
						event.gc.drawText("CheckMated!", 485, 500);
					}
				}
				else {
					event.gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
					event.gc.drawText("Player 1 is the winner!", 485, 400);
					if (board.checkmated() == 'b') {
						event.gc.drawText("CheckMated!", 485, 500);
					}
				}
			}
		});	

		
		// ---- creates mouse listener
		canvas.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e) {
				if (finished || timeOut) {
					canvas.removeMouseListener(this);
				}
				if (ai && ready) {
					board.setSelectedTile(e.x, e.y);
				}
				else if (ready && board.turnString().equals(me)) {
					board.setSelectedTile(e.x, e.y);
					String turn = board.turnString();
					if (multi) {
						if (!turn.equals(me)) {
							if (board.valid()) {
								System.out.println(e.x + ", " + e.y);

								if (me.equals("player1")) {
									out.println("timer player1-" + p1Timer);
								}
								else {
									out.println("timer player2-" + p2Timer);
								}

								out.println("turn end " + me + " at " + tempx +", " + tempy);
								out.println("turn end " + me + " at " + e.x +", " + e.y);
							}
						}
						else {
							tempx = e.x;
							tempy = e.y;
							System.out.println(e.x + ", " + e.y);
						}
					}
				}

				canvas.redraw();
			} 
			public void mouseUp(MouseEvent e) {
			}
			public void mouseDoubleClick(MouseEvent e) {} 
		});

		canvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
					canvas.redraw();
			}
			public void keyReleased(KeyEvent e) {}
		});

		if (multi) {
			socket = s;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	out = new PrintWriter(socket.getOutputStream(), true);
			Runnable runnable = new Runner();
			display.asyncExec(runnable);
		}
		
		/*
		 * Able to change player here so far
		 */
		if (ai) {
			ready = true;
			me = "player1";
			// Timer Executable
		}

		Runnable timer = new Timer();
		display.asyncExec(timer);

		shellMain.open();
		while (!shellMain.isDisposed()) 
			if (!display.readAndDispatch())
				display.sleep();
		
		display.dispose();		
	}


	class Timer implements Runnable 
	{
		@Override
		public void run() {
			if (ready) {
				if (board.turn()) {
					if (p1Timer <= 0) {
						p1Timer = 0;
						if (me.equals("player1")) {
							timeOut = true;
							if (multi) {
								out.println("timer player1-" + p1Timer);
							}
							board.turnOver();
						}
						else {
							board.setTurn("player2");
						}
					}
					else {
						p1Timer--;
					}
				}
				else {
					if (p2Timer <= 0) {
						p2Timer = 0;
						if (me.equals("player2")) {
							timeOut = true;
							if (multi) {
								out.println("timer player1-" + p2Timer);
							}
							board.turnOver();
						}
						else {
							board.setTurn("player1");
						}
					}
					else {
						p2Timer--;
					}
				}
				if (p1Timer == 0 && p2Timer == 0) {
					stalemate = true;
					finished = true;
				}
				else {
					stalemate = false;
				}
			}
			canvas.redraw();
			display.timerExec(1000, this);
		}
	}

	class Runner implements Runnable
	{
		public void run() 
		{	
			try {
				if (in.ready()) {
					String inp = in.readLine();
					if (inp.startsWith("player")) {
						me = inp;
						System.out.println("I am " + me);
					}
					if (inp.equals("Game is Ready")) {
						System.out.println(inp);
						ready = true;
					}
					if (inp.startsWith("timer")) {
						System.out.println(inp);
						if (me.equals("player1")) {
							p2Timer = Integer.valueOf(inp.substring(14));
						}
						else {
							p1Timer = Integer.valueOf(inp.substring(14));
						}
					}

					if (inp.startsWith("your turn")) {
						System.out.println("my turn");
					}

					if (inp.contains(",")) {
						System.out.println("Opp moved at " + inp);
						System.out.println(inp.split(",")[0]);
						System.out.println(inp.split(", ")[1]);
						
						int x = (Integer.valueOf(inp.split(", ")[0]));
						int y = Integer.valueOf(inp.split(", ")[1]);
						

						inp = in.readLine();

						System.out.println("Opp moved at " + inp);
						System.out.println(inp.split(",")[0]);
						System.out.println(inp.split(", ")[1]);

						int toX = Integer.valueOf(inp.split(", ")[0]);
						int toY = Integer.valueOf(inp.split(", ")[1]);

						board.setSelectedTile(x, y);
						board.setSelectedTile(toX, toY);

						canvas.redraw();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			display.timerExec(6, this);

		};
	}


	public String time(int seconds) {
		if (seconds%60 == 0) {
			return Integer.toString(seconds/60) + ":" + Integer.toString(seconds%60) + "0";
		}
		else {
			return Integer.toString(seconds/60) + ":" + Integer.toString(seconds%60);
		}
	}
}






