package myGame;

import java.awt.Color;
import java.awt.Graphics;

public class Obstacle extends Entity {
  private static int speed = 1;

  public Obstacle(int x, int y, int width, int height) {
    super(x, y, width, height);

  }

  public void update(int speed) {
    x -= speed; // all the obstacles move left on every updates
    updateBound(); // update the bound so that the collision can be detected
  }

  @Override
  public void paintComponent(Graphics g) { // draw obstacles
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(x, y, width, height);
  }

  public static int getSpeed() {
    return speed;
  }

  public static void setSpeed(int speed) {
    speed = speed;
  }

}
