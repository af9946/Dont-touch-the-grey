package myGame;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LevelView extends View {

  public LevelView(Handler handler) {
    super(handler);
    this.setFocusable(true);
    JPanel levelChooser = new JPanel(new GridLayout());
    JPanel option = new JPanel();
    option.setLayout(new BoxLayout(option, BoxLayout.Y_AXIS));

    this.setLayout(new BorderLayout());
    this.add(levelChooser, BorderLayout.CENTER);
    this.add(option, BorderLayout.PAGE_END);

    int num = Handler.getGame().getLevel();
    JButton[] levels = new JButton[num + 1];
    for (int i = 0; i < Handler.getGame().getLevel(); i++) {
      File f = Handler.getGame().getGameView().getWorld().level.get(i);
      int index = i;
      levels[i] = new JButton(f.getName());
      levelChooser.add(levels[i]);
      levels[i].addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          handler.getGame().gameView.getWorld();
          World.setCurrentLevel(index);
          handler.getGame().gameView.loadGame();
          handler.getGame().cl.show(Handler.getGame().panelCont, "Game");
          handler.getGame().gameView.requestFocusInWindow();
          View.setView(Handler.getGame().gameView);
          handler.getGame().gameView.reStart();
          // Handler.getGame().gameView.start();
        }
      });
    }
    levels[num] = new JButton("+");
    levels[num].addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JPanel width = new JPanel();

        JTextField getWidth = new JTextField(8);
        getWidth.setBounds(10, 10, 40, 20);
        JDialog jDialog = new JDialog();

        JLabel label = new JLabel("Please enter your terminal");

        JButton button = new JButton("OK");
        button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            jDialog.setVisible(false);
            JFrame f = new JFrame("Level Editor");
            f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            f.setSize(646, 384);
            f.setResizable(false);
            editorModel model = new editorModel(Integer.valueOf(getWidth.getText()));
            editorView view = new editorView(model);
            MainMenuView menuView = new MainMenuView(model);

            f.add(view.scrollPane);
            f.setJMenuBar(menuView);
            f.setVisible(true);
          }
        });

        width.add(label);
        width.add(getWidth);
        width.add(button);

        jDialog.setSize(200, 100);
        jDialog.add(width);
        jDialog.setVisible(true);

      }
    });
    levelChooser.add(levels[num]);

    JButton newGame = new JButton("New Game");
    newGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        World.setCurrentLevel(0);
        handler.getGame().gameView.loadGame();
        handler.getGame().cl.show(Handler.getGame().panelCont, "Game");
        handler.getGame().gameView.requestFocusInWindow();
        View.setView(Handler.getGame().gameView);
        handler.getGame().gameView.reStart();
      }
    });
    JButton load = new JButton("Load Game");
    load.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        handler.getWorld().setCurrentLevel(handler.getWorld().getPreviousLevel());
        handler.getGame().gameView.loadGame();
        handler.getGame().cl.show(Handler.getGame().panelCont, "Game");
        handler.getGame().gameView.requestFocusInWindow();
        View.setView(Handler.getGame().gameView);
        handler.getGame().gameView.reStart();

      }
    });

    JButton setting = new JButton("Setting");
    setting.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Setting set = new Setting(handler);
      }
    });
    JButton exit = new JButton("Exit");
    exit.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }

    });
    option.add(newGame);
    option.add(load);
    option.add(setting);
    option.add(exit);
  }

  @Override
  public void start() {

  }

  @Override
  public void update() {

  }

  @Override
  public void render(Graphics g) {

  }

}
