package myGame;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game {
  private JFrame frame;
  public String title; // title of the game
  private int width, height; // game's width and height
  private Thread thread;
  private boolean running = false;

  private int level;

  CardLayout cl;
  JPanel panelCont;

  public GameView gameView; // gameplay view
  public MenuView menuView; // main menu view
  public LevelView levelView; // choose level

  private KeyAdapter keyAdapter;
  private MouseAdapter mouseAdapter;
  private Handler handler;

  private BufferStrategy bs;
  private Graphics g;

  public Game(String title, int width, int height) { // constructor
    this.title = title;
    this.width = width;
    this.height = height;
    this.level = new File("rec/Level").listFiles().length;
    System.out.println("level =" + level);

    this.keyAdapter = new KeyAdapter();
    this.mouseAdapter = new MouseAdapter();
    this.handler = new Handler(this);
  }

  private void init() {
    gameView = new GameView(handler);
    menuView = new MenuView(handler);
    levelView = new LevelView(handler);
    View.setView(menuView);

    frame = new JFrame(this.title);
    panelCont = new JPanel();

    cl = new CardLayout();
    panelCont.setLayout(cl);

    panelCont.add(menuView, "Menu");
    panelCont.add(gameView, "Game");
    panelCont.add(levelView, "Level");

    cl.show(panelCont, "Menu");

    // frame.addKeyListener(keyAdapter);
    frame.addMouseListener(mouseAdapter);
    frame.addMouseMotionListener(mouseAdapter);

    frame.setSize(width, height);
    frame.add(panelCont);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  // getters
  public KeyAdapter getKeyAdapter() {
    return keyAdapter;
  }

  public MouseAdapter getMouseAdapter() {
    return mouseAdapter;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public GameView getGameView() {
    return gameView;
  }

  public void run() {

  }

  public synchronized void start() { // make running = true
    init();
  }

  public void GameOver() { // exit the game
    System.exit(0);
  }

  public synchronized void stop() { // make running = false, then game will stop
                                    // (get out from while loop)
    if (running == false) {
      return;
    } else {
      running = false;
    }
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.exit(1);
  }
}
