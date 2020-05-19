package myGame;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * Model for the direct manipulation demo. Created by Gustavo on 2017-06-18.
 */
public class editorModel extends Observable {

  private UndoManager undoManager;

  /**
   * The list of drawables.
   */
  private ArrayList<AbstractManipulableDrawable> drawables;

  private ArrayList<AbstractManipulableDrawable> tempArray;

  /**
   * A convenient reference to the currently selected drawable.
   */
  private AbstractManipulableDrawable currentSelection;

  private clipBoard clip;

  private int width;

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public editorModel(int width) {
    this.width = width;
    this.clip = new clipBoard();
    this.undoManager = new UndoManager();
    this.drawables = new ArrayList<AbstractManipulableDrawable>();
  }

  /**
   * Returns the list of drawables.
   * 
   * @return
   */
  public void updateViews() {
    setChanged();
    notifyObservers();
  }

  public ArrayList<AbstractManipulableDrawable> getDrawables() {
    return drawables;
  }

  /**
   * Adds a drawable to the list and notify the Views.
   * 
   * @param drawable
   */
  public void addDrawable(AbstractManipulableDrawable drawable) {
    // tempArray = drawables;
    // tempArray.add(drawable);
    UndoableEdit undoableEdit = new AbstractUndoableEdit() {

      // final ArrayList<AbstractManipulableDrawable> oldArray = drawables;
      // final ArrayList<AbstractManipulableDrawable> newArray = tempArray;

      private static final long serialVersionUID = 1L;

      @Override
      public void redo() throws CannotRedoException {
        super.redo();
        drawables.add(drawable);
        setChanged();
        notifyObservers();
      }

      @Override
      public void undo() throws CannotUndoException {
        super.undo();
        drawables.remove(drawable);
        setChanged();
        notifyObservers();
      }
    };

    undoManager.addEdit(undoableEdit);
    drawables.add(drawable);
    updateViews();

  }

  /**
   * Removes a drawable from the list and notify the Views.
   * 
   * @param drawable
   */
  public void removeDrawable(AbstractManipulableDrawable drawable) {
    // tempArray = drawables;
    // tempArray.remove(drawable);
    UndoableEdit undoableEdit = new AbstractUndoableEdit() {

      // final ArrayList<AbstractManipulableDrawable> oldArray = drawables;
      // final ArrayList<AbstractManipulableDrawable> newArray = tempArray;

      private static final long serialVersionUID = 1L;

      @Override
      public void redo() throws CannotRedoException {
        super.redo();
        drawables.remove(drawable);
        setChanged();
        notifyObservers();
      }

      @Override
      public void undo() throws CannotUndoException {
        super.undo();
        drawables.add(drawable);
        setChanged();
        notifyObservers();
      }
    };
    undoManager.addEdit(undoableEdit);
    this.drawables.remove(drawable);
    updateViews();

  }

  /**
   * Removes all drawables from the list and notify the Views.
   */
  public void removeAll() {
    tempArray = drawables;
    tempArray.clear();
    UndoableEdit undoableEdit = new AbstractUndoableEdit() {

      final ArrayList<AbstractManipulableDrawable> oldArray = drawables;
      final ArrayList<AbstractManipulableDrawable> newArray = tempArray;

      private static final long serialVersionUID = 1L;

      @Override
      public void redo() throws CannotRedoException {
        super.redo();
        drawables = newArray;
        setChanged();
        notifyObservers();
      }

      @Override
      public void undo() throws CannotUndoException {
        super.undo();
        drawables = oldArray;
        setChanged();
        notifyObservers();
      }
    };
    undoManager.addEdit(undoableEdit);
    this.drawables.clear();
    updateViews();

  }

  /**
   * Returns the currently selected drawable.
   * 
   * @return
   */
  public AbstractManipulableDrawable getCurrentSelection() {
    return currentSelection;
  }

  /**
   * Sets the currently selected drawable and notify the Views.
   * 
   * @param currentSelection
   */
  public void setCurrentSelection(AbstractManipulableDrawable currentSelection) {
    if (currentSelection != null) {
      currentSelection.select();
    }
    this.currentSelection = currentSelection;
    updateViews();
  }

  /**
   * Translate the currently selected drawable by the specified screen amounts,
   * considering the current transformation of the selected drawable.
   * 
   * @param tx
   * @param ty
   */
  public void translateSelected(double tx, double ty) {
	  
    if (this.currentSelection != null) {
      if (this.currentSelection.getTransform() != null) {
        // We need to apply the inverse of the scale factors to the
        // translation amounts
        // to transform screen amounts to model amounts
        double stx = tx / this.currentSelection.getTransform().getScaleX();
        double sty = ty / this.currentSelection.getTransform().getScaleY();
        this.currentSelection.getTransform().translate(stx, sty);
      } else {
        this.currentSelection.setTransform(AffineTransform.getTranslateInstance(tx, ty));
      }
      updateViews();
    }
  }

  /**
   * Scale the currently selected drawable about the West border, according to
   * the specified screen points, considering the current transformation of the
   * selected drawable.
   * 
   * @param oldPoint
   * @param newPoint
   * @throws NoninvertibleTransformException
   */
  public void scaleSelectedWest(Point oldPoint, Point newPoint) throws NoninvertibleTransformException {
    if (this.currentSelection != null) {
      if (this.currentSelection.getTransform() != null) {
        // We need to transform screen coordinates to model coordinates
        Point2D oldPointTransformed = this.currentSelection.transformMousePointToModel(oldPoint);
        Point2D newPointTransformed = this.currentSelection.transformMousePointToModel(newPoint);
        double width = this.currentSelection.getBoundingBox().getWidth();
        double sx = (oldPointTransformed.getX() + width) / (newPointTransformed.getX() + width);
        this.currentSelection.getTransform().scale(sx, 1.0);
        this.currentSelection.getTransform().translate(newPointTransformed.getX() - oldPointTransformed.getX(), 0.0);
      } else {
        double width = this.currentSelection.getBoundingBox().getWidth();
        double sx = (oldPoint.getX() + width) / (newPoint.getX() + width);
        this.currentSelection.setTransform(AffineTransform.getScaleInstance(sx, 1.0));
      }
      updateViews();

    }
  }

  /**
   * Scale the currently selected drawable about the East border, according to
   * the specified screen points, considering the current transformation of the
   * selected drawable.
   * 
   * @param oldPoint
   * @param newPoint
   * @throws NoninvertibleTransformException
   */
  public void scaleSelectedEast(Point oldPoint, Point newPoint) throws NoninvertibleTransformException {
    if (this.currentSelection != null) {
      if (this.currentSelection.getTransform() != null) {
        // We need to transform screen coordinates to model coordinates
        Point2D oldPointTransformed = this.currentSelection.transformMousePointToModel(oldPoint);
        Point2D newPointTransformed = this.currentSelection.transformMousePointToModel(newPoint);
        this.currentSelection.getTransform().scale(newPointTransformed.getX() / oldPointTransformed.getX(), 1.0);
      } else {
        this.currentSelection.setTransform(AffineTransform.getScaleInstance(newPoint.getX() / oldPoint.getX(), 1.0));
      }
      updateViews();

    }
  }

  /**
   * Scale the currently selected drawable about the North border, according to
   * the specified screen points, considering the current transformation of the
   * selected drawable.
   * 
   * @param oldPoint
   * @param newPoint
   * @throws NoninvertibleTransformException
   */
  public void scaleSelectedNorth(Point oldPoint, Point newPoint) throws NoninvertibleTransformException {
    if (this.currentSelection != null) {
      if (this.currentSelection.getTransform() != null) {
        // We need to transform screen coordinates to model coordinates
        Point2D oldPointTransformed = this.currentSelection.transformMousePointToModel(oldPoint);
        Point2D newPointTransformed = this.currentSelection.transformMousePointToModel(newPoint);
        double height = this.currentSelection.getBoundingBox().getHeight();
        double sy = (oldPointTransformed.getY() + height) / (newPointTransformed.getY() + height);
        this.currentSelection.getTransform().scale(1.0, sy);
        this.currentSelection.getTransform().translate(0.0, newPointTransformed.getY() - oldPointTransformed.getY());
      } else {
        double height = this.currentSelection.getBoundingBox().getHeight();
        double sy = (oldPoint.getY() + height) / (newPoint.getY() + height);
        this.currentSelection.setTransform(AffineTransform.getScaleInstance(1.0, sy));
      }
      updateViews();

    }
  }

  /**
   * Scale the currently selected drawable about the South border, according to
   * the specified screen points, considering the current transformation of the
   * selected drawable.
   * 
   * @param oldPoint
   * @param newPoint
   * @throws NoninvertibleTransformException
   */
  public void scaleSelectedSouth(Point oldPoint, Point newPoint) throws NoninvertibleTransformException {
    if (this.currentSelection != null) {
      if (this.currentSelection.getTransform() != null) {
        // We need to transform screen coordinates to model coordinates
        Point2D oldPointTransformed = this.currentSelection.transformMousePointToModel(oldPoint);
        Point2D newPointTransformed = this.currentSelection.transformMousePointToModel(newPoint);
        this.currentSelection.getTransform().scale(1.0, newPointTransformed.getY() / oldPointTransformed.getY());
      } else {
        this.currentSelection.setTransform(AffineTransform.getScaleInstance(1.0, newPoint.getY() / oldPoint.getY()));
      }
      updateViews();

    }
  }

  // copy and cut
  public void copy(AbstractManipulableDrawable drawable) {
    this.clip.setX(drawable.getTransform().getScaleX());
    this.clip.setY(drawable.getTransform().getScaleY());
  }

  public void cut(AbstractManipulableDrawable drawable) {
    this.clip.setX(drawable.getTransform().getScaleX());
    this.clip.setY(drawable.getTransform().getScaleY());
    this.drawables.remove(drawable);
  }

  public clipBoard getClip() {
    return clip;
  }

  // redos and undos
  public void undo() {
    if (canUndo())
      undoManager.undo();
  }

  public void redo() {
    if (canRedo())
      undoManager.redo();
  }

  public boolean canUndo() {
    return undoManager.canUndo();
  }

  public boolean canRedo() {
    return undoManager.canRedo();
  }

  // output file

  public void SaveEditable(String path) {
    FileWriter writer;
    try {
      writer = new FileWriter(path);
      int num = drawables.size();
      writer.write(String.valueOf(num) + "\n");
      writer.write(String.valueOf(this.width) + "\n");

      for (AbstractManipulableDrawable o : drawables) {
        String str = (parse(o.getTransform().getTranslateX() / 32) + " " + parse(o.getTransform().getTranslateY() / 32) + " " + parse(o.getTransform().getScaleX()) + " "
            + parse(o.getTransform().getScaleY()));
        writer.write(str);
        writer.write("\n");
      }

      writer.flush();

      writer.close();

    } catch (IOException e) {

      e.printStackTrace();

    }
  }

  public static String parse(double d) { // parse string to int so that we can
                                         // access it
    try {
      int i;
      i = (int) d;
      return String.valueOf(i);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return "\n";
    }
  }

  // load file
  public void LoadLevel(String path) {
    drawables.clear();
    String file = FileLoader.loadFile(path);
    String[] tokens = file.split("\\s+");
    int num = FileLoader.parse(tokens[0]); // numbers of obstacles
    width = FileLoader.parse(tokens[1]); // goal score

    for (int i = 2; i < num * 4; i += 4) { // we start at token[3]
      double tx = Double.valueOf(tokens[i]) * 32;
      double ty = Double.valueOf(tokens[i + 1]) * 32;
      double sx = Double.valueOf(tokens[i + 2]);
      double sy = Double.valueOf(tokens[i + 3]);
      AffineTransform a = AffineTransform.getScaleInstance(sx, sy);
      a.translate(tx / sx, ty / sy);
      Bar o = new Bar(a);
      addDrawable(o);
    }
    updateViews();

  }
}
