package myGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PauseMenu extends JFrame {

  private JButton save = new JButton("Save");
  private JButton reStart = new JButton("Restart");
  private JButton main = new JButton("Main Menu");
  private JButton Resume = new JButton("Resume");

  private Handler handler;

  PauseMenu(Handler handler) {
    super("Pause");
    this.handler = handler;
    this.layoutFrame();
    this.registerControllers();
    this.pack();
    this.setVisible(true);
  }

  private void layoutFrame() {
    JPanel pausing = new JPanel();
    pausing.setLayout(new BoxLayout(pausing, BoxLayout.Y_AXIS));
    pausing.setBackground(Color.white);
    this.setSize(1000, 200);

    pausing.add(save);
    pausing.add(reStart);
    pausing.add(main);
    pausing.add(Resume);

    this.add(pausing);
  }

  private void registerControllers() {
    save.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handler.getWorld().setPreviousLevel(handler.getWorld().getCurrentLevel());
        handler.getGame().cl.show(Handler.getGame().panelCont, "Menu");
        handler.getGame().menuView.requestFocusInWindow();
        close();
      }
    });

    reStart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handler.getGame().getGameView().start();
        close();
      }
    });

    main.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handler.getGame().cl.show(Handler.getGame().panelCont, "Menu");
        handler.getGame().menuView.requestFocusInWindow();
        close();
      }
    });

    Resume.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handler.getGame().getGameView().start();
        close();
      }
    });
  }

  private void close() {
    this.setVisible(false);
  }
}
