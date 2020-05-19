package myGame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Gustavo on 2017-06-18.
 */

public class Bar extends AbstractManipulableDrawable {

  private Rectangle shape;

  public Bar() {
    super();
    this.initializeShape();
  }

  public Bar(AffineTransform transform) {
    super(transform);
    this.initializeShape();
  }

  private void initializeShape() {
    this.shape = new Rectangle(32, 32);

  }

  @Override
  public boolean contains(double px, double py) throws NoninvertibleTransformException {
    // Transform mouse coordinates to model coordinates before testing
    Point2D transformedPoint = this.transformMousePointToModel(new Point2D.Double(px, py));
    return this.shape.contains(transformedPoint);
  }

  @Override
  protected void paintItself(Graphics2D g2) {
    // Transform the basic shape using the current Affine Transform before
    // drawing
    Shape transformedShape = this.transform == null ? this.shape : this.transform.createTransformedShape(this.shape);
    // Draw outline
    g2.setColor(Color.DARK_GRAY);
    g2.setStroke(new BasicStroke(2));
    g2.draw(transformedShape);
    // Fill shape
    g2.setColor(Color.LIGHT_GRAY);
    g2.fill(transformedShape);
  }

  @Override
  protected Rectangle2D getBoundingBox() {
    return this.shape.getBounds2D();
  }
}
