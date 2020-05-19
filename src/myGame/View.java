package myGame;

import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class View extends JPanel {

  private static View currentView = null;
  protected Handler handler;

  public View(Handler handler) {
    this.handler = handler;
  }

  // getter and setter
  public static void setView(View s) {
    currentView = s;
    currentView.start();
  }

  public static View getView() {
    return currentView;
  }

  public abstract void start();

  public abstract void update();

  public abstract void render(Graphics g);

}
