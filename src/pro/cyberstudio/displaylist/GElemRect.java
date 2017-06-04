package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.*;

import pro.cyberstudio.utilities.*;

/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemRect extends GraphElemRectangular {
	
	double cx;
	double cy;
	
	public GElemRect(Paint paint,
					 BasicStroke stroke, Rectangle2D rect,
					 double rotation) {
		
		super(GraphElemType.RECT);
		
		this.paint = paint;
		this.stroke = stroke;
		this.shape = rect;
		setRotation(rotation);
		
		cx = rect.getCenterX();
		cy = rect.getCenterY();
	}
	
	public void draw(Graphics2D g2) {
		super.draw(g2, this);
	}
	
	
	//	public void draw(Graphics2D g2) {
	@Override
	public void drawChild(Graphics2D g2) {
		AffineTransform aft = null;
		
		if (getRotation() > 0) {
			aft = g2.getTransform();
			g2.rotate(getRotationRad(),
					((Rectangle2D) shape).getCenterX(),
					((Rectangle2D) shape).getCenterY());
		}
		
		g2.draw(shape);
		
		if (getRotation() > 0) {
			g2.setTransform(aft);
		}
	}
	
	@Override
	public String toString() {
		return log.LogMsgStr("this is a rect| ",
			GraphElement.listElemInfo(ID(), paint,
			((Rectangle2D) shape).getX(),
			((Rectangle2D) shape).getY(),
			gf, getRotation()))
				+ " size| " + log.dispVal(((Rectangle2D) shape).getWidth(), ((Rectangle2D) shape).getHeight())
				+ " center| " + log.dispVal(cx, cy);
	}
	
}
