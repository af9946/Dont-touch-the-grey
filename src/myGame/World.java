package myGame;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class World {

  private Handler handler;
  private int num; // number of obstacles
  private int score; // player's score
  private int terminal; // the goal score to win the game in particular level
  public static ArrayList<Obstacle> obstacles;
  public static ArrayList<File> level = new ArrayList<File>();
  public static int currentLevel = 0;
  public static int previousLevel = 0;
  private int speed = 1;

  public World(Handler handler) { // constructor
    this.handler = handler;
    this.score = 0;
    File dir = new File("rec/Level");
    File[] directoryListing = dir.listFiles();
    if (directoryListing != null) {
      for (File child : directoryListing) {
        level.add(child);
      }
    }
  }

  public static int getCurrentLevel() {
    return currentLevel;
  }

  public static void setCurrentLevel(int i) {
    World.currentLevel = i;
  }

  public static int getPreviousLevel() {
    return previousLevel;
  }

  public static void setPreviousLevel(int previousLevel) {
    World.previousLevel = previousLevel;
  }

  public void reStart() {
    this.score = 0;
    loadGame();
  }

  public void update() {
    score++; // score is increasing during the time
    if (win()) {
      handler.getGame().gameView.pause();
      if (currentLevel < handler.getGame().getLevel() - 1) {
        Object[] options = { "Next Level", "Restart" };
        int n = JOptionPane.showOptionDialog(null, "You Win!", "Congratulation!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        System.out.println("result:" + n);
        if (n == 0) {
          // next level
          currentLevel += 1;
        }
        handler.getGame().gameView.reStart();
      } else {
        Object[] options = { "Back to Main Menu", "Restart" };
        int n = JOptionPane.showOptionDialog(null, "You win them all!", "Congratulation!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        System.out.println("result:" + n);
        if (n == 0) {
          handler.getGame().cl.show(Handler.getGame().panelCont, "Menu");
          handler.getGame().menuView.requestFocusInWindow();
          handler.getGame().gameView.pause();
          score = 0;
          // handler.getGame().GameOver(); // if win, show message and close the
        } else {
          // restart
          handler.getGame().gameView.reStart();
        }
      }

    }
    for (Obstacle obstacle : obstacles) { // update all the obstacles position
      obstacle.update(speed);
    }
  }

  public boolean win() { // score reach the terminal, then game win
    return (score > terminal); // If I don't add 2, then the game will end
                               // earlier based on my test,
    // I don't know why, so I just add 2
    // I will try to figure it out
  }

  public void render(Graphics g) { // draw all the obstacles and update scores

  }

  public void paintComponent(Graphics g) { // draw all the obstacles and update
                                           // scores

    for (Obstacle obstacle : obstacles) {
      obstacle.paintComponent(g);
    }
    g.setColor(Color.BLACK);
    g.drawString("Goals: " + terminal + "   Scores: " + score, 10, 20);
    g.drawString("Level: " + (currentLevel + 1), 300, 20);

  }

  public int getTerminal() {
    return this.terminal;
  }

  public void loadGame() {
    score = 0;
    obstacles = new ArrayList<Obstacle>();
    String file = FileLoader.loadFile(level.get(currentLevel).getPath());
    String[] tokens = file.split("\\s+");
    num = FileLoader.parse(tokens[0]); // numbers of obstacles
    terminal = FileLoader.parse(tokens[1]); // goal score

    for (int i = 2; i < num * 4; i += 4) { // we start at token[3]
      // since 1 block is 32*32, so I times 32 for x,y,width,height for
      // obstacles
      Obstacle o = new Obstacle(FileLoader.parse(tokens[i]) * 32 + 160, // give
                                                                        // player
                                                                        // some
                                                                        // space
                                                                        // to
                                                                        // get
                                                                        // ready
          FileLoader.parse(tokens[i + 1]) * 32, FileLoader.parse(tokens[i + 2]) * 32, FileLoader.parse(tokens[i + 3]) * 32);
      obstacles.add(o); // add obstacles to the Arraylist of obstacle
    }
  }

  public int getSpeed() {
    return speed;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

}
