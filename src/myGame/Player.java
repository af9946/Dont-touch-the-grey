package myGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

public class Player extends Entity {
  Handler handler;
  private static int SPEED = 3;
  private int xMove = 0;
  private int yMove = 0;
  private KeyAdapter k;
  public boolean lose = false;

  public Player(Handler handler, int x, int y, int width, int height) {
    super(x, y, width, height);
    this.handler = handler;
    this.k = new KeyAdapter();
  }

  public KeyListener getKeyListener() {
    return k;
  }

  public void update() {
    // System.out.println("y = "+ y);
    // System.out.println("x = "+ x);

    x--; // graph move to right then player's position move to left
    if (k.isUp()) { // press W
      yMove -= SPEED;
    }
    if (k.isDown()) { // press S
      yMove += SPEED;
    }
    if (k.isLeft())
      xMove -= SPEED; // press A

    if (k.isRight())
      xMove += SPEED; // press D

    if (k.isSpace()) {
      k.setSpace(false);
      handler.getGame().gameView.pause();
      PauseMenu pausing = new PauseMenu(handler);
    }

    if (y + yMove > 320 - 32 || y + yMove < 0) { // player cannot get outside of
                                                 // the world, if so, don't move

    } else {
      y += yMove;
    }

    updateBound(); // update bounds so that the collision detection can work
    lose = lose();

    if (lose) { // collision or player falls behind the left boundary, game over
      Object[] options = { "Back to Main Menu", "Restart" };
      int n = JOptionPane.showOptionDialog(null, "You Lose", "So sad!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
      System.out.println("result:" + n);
      if (n == 0) {
        handler.getGame().cl.show(Handler.getGame().panelCont, "Menu");
        handler.getGame().menuView.requestFocusInWindow();
        handler.getGame().gameView.pause();
        // handler.getGame().GameOver(); // if win, show message and close the
      } else {
        // restart
        handler.getGame().gameView.reStart();
      }
    }

    x += xMove;
    xMove = 0;
    yMove = 0;
  }

  public boolean lose() {
    return (collision() || x < -32);
  }

  public boolean collision() { // collision detection
    Rectangle pb = this.getBound();// get player's bound
    for (Obstacle obstacle : World.obstacles) { // check for all the obstacles
                                                // to see if any collision exits
      Rectangle ob = obstacle.getBound();
      if (pb.intersects(ob)) {
        return true; // if so, return true
      }
    }
    return false;
  }

  public void paintComponent(Graphics g) { // draw player like a triangle
    Polygon p = new Polygon();
    p.addPoint(x, y);
    p.addPoint(x, y + 32);
    p.addPoint(x + 32, y + 16);
    g.setColor(Color.BLACK);
    g.drawPolygon(p);
    g.fillPolygon(p);
  }

  public class KeyAdapter implements KeyListener {

    private boolean[] keys;

    public synchronized boolean isUp() {
      return keys[KeyEvent.VK_W];
    }

    public synchronized boolean isDown() {
      return keys[KeyEvent.VK_S];
    }

    public synchronized boolean isLeft() {
      return keys[KeyEvent.VK_A];
    }

    public synchronized boolean isRight() {
      return keys[KeyEvent.VK_D];
    }

    public synchronized boolean isSpace() {
      return keys[KeyEvent.VK_SPACE];
    }

    public synchronized void setSpace(boolean val) {
      keys[KeyEvent.VK_SPACE] = val;
    }

    public KeyAdapter() {
      keys = new boolean[256];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
      keys[e.getKeyCode()] = true;
      System.out.println("pressed: " + e.getKeyCode());
      update();
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
      keys[e.getKeyCode()] = false;
      update();
    }

  }

  public static int getSpeed() {
    return SPEED;
  }

  public static void setSpeed(int speed) {
    SPEED = speed;
  }

}
