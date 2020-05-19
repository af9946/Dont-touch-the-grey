package myGame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseAdapter implements MouseListener, MouseMotionListener {

  private boolean left;
  private boolean right;
  private int x;
  private int y;

  public boolean isLeft() {
    return left;
  }

  public boolean isRight() {
    return right;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {
    x = e.getX();
    y = e.getY();
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      left = true;
    } else if (e.getButton() == MouseEvent.BUTTON3) {
      right = true;
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      left = false;
    } else if (e.getButton() == MouseEvent.BUTTON3) {
      right = false;
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

}
