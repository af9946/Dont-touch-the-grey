package myGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyAdapter implements KeyListener {

  private boolean[] keys;
  public boolean up;
  public boolean down;
  public boolean left;
  public boolean right;

  public KeyAdapter() {
    keys = new boolean[256];
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    keys[e.getKeyCode()] = true;
    System.out.println("pressed:" + e.getKeyCode());
    update();
  }

  @Override
  public void keyReleased(KeyEvent e) {
    keys[e.getKeyCode()] = false;
    update();
  }

  public void update() {
    up = keys[KeyEvent.VK_W];
    down = keys[KeyEvent.VK_S];
    left = keys[KeyEvent.VK_A];
    right = keys[KeyEvent.VK_D];
  }

}
