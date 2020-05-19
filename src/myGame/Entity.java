package myGame;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class Entity extends JComponent {

  protected int x; // x coordinate
  protected int y; // y coordinate
  protected int width; // width of the entity
  protected int height; // height of the entity
  protected Rectangle bound; // use bound to check collision (Rectangle's
                             // build-in function)

  public Entity(int x, int y, int width, int height) { // constructor
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.bound = new Rectangle(x, y, width, height); // bound is the entity
                                                     // itself
  }

  // getters and setters
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

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public Rectangle getBound() {
    return bound;
  }

  public void updateBound() {
    this.bound.setBounds(x, y, width, height);
  }

}
