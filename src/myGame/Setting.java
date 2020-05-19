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

public class Setting extends JFrame {

  private JLabel FPSLabel = new JLabel("Frames Per Second", JLabel.CENTER);
  private JLabel PlayerLabel = new JLabel("Player Speed", JLabel.CENTER);
  private JLabel Worldlabel = new JLabel("Obstacle Speed", JLabel.CENTER);

  private JSlider FPS;
  private JSlider PlayerSpeed;
  private JSlider WorldSpeed;
  private JButton save = new JButton("Save");
  private Handler handler;

  Setting(Handler handler) {
    super("Settings");
    this.handler = handler;
    this.layoutFrame();
    this.registerControllers();
    this.pack();
    this.setVisible(true);
  }

  private void layoutFrame() {
    int frameps = handler.getGame().getGameView().getFPS();
    int pspeed = handler.getGame().getGameView().getPlayer().getSpeed();
    int wspeed = handler.getGame().getGameView().getWorld().getSpeed();

    JPanel settings = new JPanel();
    settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));
    settings.setBackground(Color.white);
    this.setSize(1000, 200);

    FPS = new JSlider(0, 100, frameps);
    FPS.setMajorTickSpacing(10);
    FPS.setMinorTickSpacing(1);
    FPS.setPaintTicks(true);
    FPS.setPaintLabels(true);
    FPS.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    Font font = new Font("Serif", Font.ITALIC, 15);
    FPS.setFont(font);

    PlayerSpeed = new JSlider(1, 5, pspeed);
    PlayerSpeed.setMajorTickSpacing(1);
    PlayerSpeed.setMinorTickSpacing(1);
    PlayerSpeed.setPaintTicks(true);
    PlayerSpeed.setPaintLabels(true);
    PlayerSpeed.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    PlayerSpeed.setFont(font);

    WorldSpeed = new JSlider(1, 5, wspeed);
    WorldSpeed.setMajorTickSpacing(1);
    WorldSpeed.setMinorTickSpacing(1);
    WorldSpeed.setPaintTicks(true);
    WorldSpeed.setPaintLabels(true);
    WorldSpeed.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    WorldSpeed.setFont(font);

    settings.add(FPSLabel);
    settings.add(FPS);

    settings.add(PlayerLabel);
    settings.add(PlayerSpeed);

    settings.add(Worldlabel);
    settings.add(WorldSpeed);

    settings.add(save);

    this.add(settings);
  }

  private void registerControllers() {

    FPS.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        JSlider s = (JSlider) e.getSource();
        handler.getGame().getGameView().setFPS(s.getValue());
      }
    });

    PlayerSpeed.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        JSlider s = (JSlider) e.getSource();
        handler.getGame().getGameView().getPlayer().setSpeed(s.getValue());
      }
    });

    WorldSpeed.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        JSlider s = (JSlider) e.getSource();
        handler.getGame().getGameView().getWorld().setSpeed(s.getValue());
      }
    });

    save.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        close();
      }
    });
  }

  private void close() {
    this.setVisible(false);
  }
}
