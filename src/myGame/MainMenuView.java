package myGame;
// CS 349 Undo Demo

// Daniel Vogel (2013)

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

// a main menu view
public class MainMenuView extends JMenuBar implements Observer {

  // Undo menu items
  private JMenuItem undoMenuItem;
  private JMenuItem redoMenuItem;

  // the model that this view is showing
  private editorModel model;

  public MainMenuView(editorModel model_) {

    // set the model
    model = model_;
    model.addObserver(this);

    // create a menu UI with undo/redo
    JMenu fileMenu = new JMenu("File");
    JMenu editMenu = new JMenu("Redo/Undo");
    JMenu helpMenu = new JMenu("Info");
    this.add(fileMenu);
    this.add(editMenu);
    this.add(helpMenu);

    JMenuItem helpItem = new JMenuItem("Help");
    helpMenu.add(helpItem);
    helpItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        JOptionPane.showMessageDialog(null, ("Right click on white borad: Add Obstacles, Paste Obstacles, Clear\n" + "Right click on selected obstacle: Copy, Cut, Delete\n"
            + "Save: save the editable file(please save to rec/level)"));

      }
    });

    // Create a "quit" menu item and add it to the file menu
    JMenuItem quitMenuItem = new JMenuItem("Quit");
    fileMenu.add(quitMenuItem);

    // quit menu controller
    quitMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    });

    // save and load
    JMenuItem save = new JMenuItem("Save");

    save.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        JButton save1 = new JButton();
        JFileChooser jf1 = new JFileChooser();
        jf1.setCurrentDirectory(new java.io.File("."));
        jf1.setDialogTitle("Save Level");
        jf1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jf1.showSaveDialog(save1) == JFileChooser.APPROVE_OPTION) {
          model.SaveEditable(jf1.getSelectedFile().getAbsolutePath());
        }
      }
    });
    fileMenu.add(save);

    JMenuItem load = new JMenuItem("Load");
    load.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        JButton open1 = new JButton();
        JFileChooser jf2 = new JFileChooser();
        jf2.setCurrentDirectory(new java.io.File("."));
        jf2.setDialogTitle("Load Level");
        jf2.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (jf2.showOpenDialog(open1) == JFileChooser.APPROVE_OPTION) {
          model.LoadLevel(jf2.getSelectedFile().getAbsolutePath());
        }
      }
    });
    fileMenu.add(load);

    // create undo and redo menu items
    undoMenuItem = new JMenuItem("Undo");
    undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
    redoMenuItem = new JMenuItem("Redo");
    redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
    editMenu.add(undoMenuItem);
    editMenu.add(redoMenuItem);

    // controllers for undo menu item
    undoMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        model.undo();
      }
    });
    // controller for redo menu item
    redoMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        model.redo();
      }
    });
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    undoMenuItem.setEnabled(model.canUndo());
    redoMenuItem.setEnabled(model.canRedo());
  }

}
