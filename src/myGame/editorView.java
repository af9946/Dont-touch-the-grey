package myGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Observable;
import java.util.Observer;

/**
 * View for the direct manipulation demo. Created by Gustavo on 2017-06-18.
 */
public class editorView extends JPanel implements Observer {

  private int width;
  private int height = 320;

  private editorModel model = new editorModel(width);
  private int x = 50;
  private int y = 70;

  JScrollPane scrollPane;

  private JPopupMenu pmenu_white;
  private JPopupMenu pmenu_select;

  public editorView(editorModel model) {
    this.width = model.getWidth();
    scrollPane = new JScrollPane(this);
    scrollPane.setBounds(100, 100, 640, 320);

    this.model = model;
    this.model.addObserver(this);
    Controller controller = new Controller(model);
    this.addMouseListener(controller);
    this.addMouseMotionListener(controller);

    this.setPreferredSize(new Dimension(width, height));

    pmenu_white = new JPopupMenu();
    JMenuItem add = new JMenuItem("Add");
    JMenuItem paste = new JMenuItem("Paste");

    add.addActionListener((ActionEvent e) -> {
      double w = 2.0;
      double h = 2.0;
      AffineTransform a = AffineTransform.getScaleInstance(w, h);
      a.translate(x / w, y / h);
      Bar o = new Bar(a);
      model.addDrawable(o);
    });

    pmenu_white.add(add);

    paste.addActionListener((ActionEvent e) -> {
      double w = model.getClip().getX();
      double h = model.getClip().getY();

      AffineTransform a = AffineTransform.getScaleInstance(w, h);
      a.translate(x / w, y / h);
      Bar o = new Bar(a);
      model.addDrawable(o);
    });
    pmenu_white.add(paste);

    pmenu_select = new JPopupMenu();
    JMenuItem delete = new JMenuItem("Delete");
    JMenuItem copy = new JMenuItem("Copy");
    JMenuItem cut = new JMenuItem("Cut");

    delete.addActionListener((ActionEvent e) -> {
      model.removeDrawable(model.getCurrentSelection());
    });
    pmenu_select.add(delete);

    copy.addActionListener((ActionEvent e) -> {

      model.copy(model.getCurrentSelection());
    });
    pmenu_select.add(copy);

    cut.addActionListener((ActionEvent e) -> {
      model.cut(model.getCurrentSelection());
    });
    pmenu_select.add(cut);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(Color.WHITE);
    g2.fillRect(0, 0, this.getWidth(), this.getHeight());
    // Paint all the drawables in the list
    for (SelectableDrawable drawable : this.model.getDrawables()) {
      drawable.paint(g2);
    }
    g2.setColor(Color.BLACK);
    g2.drawString("( " + x + " , " + y + ")", 10, 20);
  }

  @Override
  public void update(Observable o, Object arg) {
    this.repaint();
  }

  /**
   * Controller for the direct manipulation demo. Created by Gustavo on
   * 2017-06-18.
   */
  class Controller extends MouseAdapter {
    private editorModel model;
    private boolean isMoving;
    private BoundingBoxBorder isResizing;
    private Point lastPoint;

    public Controller(editorModel model) {
      this.model = model;
      this.isMoving = false;
      this.isResizing = BoundingBoxBorder.NONE;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      // Select the clicked drawable (if any) and deselect all others
      try {
        this.model.setCurrentSelection(null);
        for (AbstractManipulableDrawable drawable : this.model.getDrawables()) {
          if (drawable.contains(e.getPoint())) {
            drawable.select();
            this.model.setCurrentSelection(drawable);
          } else {
            drawable.deselect();
          }
        }
      } catch (NoninvertibleTransformException e1) {
        e1.printStackTrace();
      }
    }

    @Override
    public void mousePressed(MouseEvent e) {
      // If the mouse is over a selected component, begin dragging (moving or
      // resizing)
      try {
        if (this.model.getCurrentSelection() != null) {
          if (e.getButton() == MouseEvent.BUTTON3) {
            pmenu_select.show(e.getComponent(), e.getX(), e.getY());
          } else {
            this.isResizing = this.model.getCurrentSelection().isInBoundingBox(e.getPoint());
            this.isMoving = (this.isResizing == BoundingBoxBorder.NONE) && this.model.getCurrentSelection().contains(e.getPoint());
            this.lastPoint = e.getPoint();
          }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
          pmenu_white.show(e.getComponent(), e.getX(), e.getY());
        }
      } catch (NoninvertibleTransformException e1) {
        e1.printStackTrace();
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      // Stop moving and resizing
      this.isResizing = BoundingBoxBorder.NONE;
      this.isMoving = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      try {
        if (this.model.getCurrentSelection() != null) {
          // Move (translate) drawable
          if (this.isMoving) {
            double tx = e.getPoint().getX() - this.lastPoint.getX();
            double ty = e.getPoint().getY() - this.lastPoint.getY();
            this.model.translateSelected(tx, ty);
          }
          // Resize (scale) drawable
          switch (this.isResizing) {
          case NORTH:
            this.model.scaleSelectedNorth(lastPoint, e.getPoint());
            break;
          case SOUTH:
            this.model.scaleSelectedSouth(lastPoint, e.getPoint());
            break;
          case WEST:
            this.model.scaleSelectedWest(lastPoint, e.getPoint());
            break;
          case EAST:
            this.model.scaleSelectedEast(lastPoint, e.getPoint());
            break;
          }
          // Save last point
          this.lastPoint = e.getPoint();
        }
      } catch (NoninvertibleTransformException e1) {
        e1.printStackTrace();
      }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      x = e.getX();
      y = e.getY();
      repaint();
      // If the mouse is over a selected component, change the mouse cursor
      // accordingly
      try {
        editorView.this.setCursor(Cursor.getDefaultCursor());
        if (this.model.getCurrentSelection() != null) {
          // Move cursor
          if (this.model.getCurrentSelection().contains(e.getPoint())) {
            editorView.this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
          }
          // Scale cursor
          switch (this.model.getCurrentSelection().isInBoundingBox(e.getPoint())) {
          case NORTH:
            editorView.this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
            break;
          case SOUTH:
            editorView.this.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
            break;
          case WEST:
            editorView.this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
            break;
          case EAST:
            editorView.this.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
            break;
          }
        }
      } catch (NoninvertibleTransformException e1) {
        e1.printStackTrace();
      }
    }
  }
}
