package myGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MenuView extends View {

  private JButton start;
  private JButton editting;
  private JButton setting;
  private JButton exit;
  private Image image;
  private Timer timer;
  private int FPS = 30;

  public int mx;
  public int my;

  public MenuView(Handler handler) {
    super(handler);
    start = new JButton("start");
    this.setFocusable(true);
    start.setMaximumSize(new Dimension(100, 50));
    start.setPreferredSize(new Dimension(100, 50));
    editting = new JButton("editting");
    editting.setMaximumSize(new Dimension(100, 50));
    editting.setPreferredSize(new Dimension(100, 50));
    setting = new JButton("setting");
    setting.setMaximumSize(new Dimension(100, 50));
    setting.setPreferredSize(new Dimension(100, 50));
    exit = new JButton("exit");
    exit.setMaximumSize(new Dimension(100, 50));
    exit.setPreferredSize(new Dimension(100, 50));

    try {
      image = ImageIO.read(new File("src/MAIN.png"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    this.addMouseListener(new MouseListener() {
      public void mouseClicked(MouseEvent e) {

      }

      public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          handler.getGame().cl.show(Handler.getGame().panelCont, "Level");
          handler.getGame().levelView.requestFocusInWindow();
          View.setView(Handler.getGame().levelView);
        }
      }

      public void mouseReleased(MouseEvent e) {
      }

      public void mouseEntered(MouseEvent e) {
      }

      public void mouseExited(MouseEvent e) {
      }
    });

    this.addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
      }

      public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();

      }
    });

    this.timer = new Timer(1000 / this.FPS, event -> {
      this.repaint(); // note that we call repaint, not paintComponent
    });
  }

  @Override
  public void update() { // if click left on mouse, then we go to game view(game
                         // begin)
  }

  @Override
  public void render(Graphics g) { // draw the position of the mouse
  }

  public void paintComponent(Graphics g) { // draw the position of the mouse
    super.paintComponent(g);
    g.drawImage(image, 0, 0, this);
    g.setColor(Color.BLACK);
    g.fillOval(mx, my, 10, 10);
  }

  @Override
  public void start() {
    this.timer.start();
  }
}
