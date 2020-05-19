package myGame;

public class clipBoard {

  private double scaleX;
  private double scaleY;

  public clipBoard() {
    scaleX = 0;
    scaleY = 0;
  }

  public double getX() {
    return scaleX;
  }

  public void setX(double width) {
    this.scaleX = width;
  }

  public double getY() {
    return scaleY;
  }

  public void setY(double height) {
    this.scaleY = height;
  }

}
