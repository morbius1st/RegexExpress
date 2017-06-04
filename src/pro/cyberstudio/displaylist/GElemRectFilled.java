package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.*;

import pro.cyberstudio.utilities.log;

import static pro.cyberstudio.regexexpress.Utility.dispVal;

/**
 * @author jeffs
 *         File:    dlItemFilledRect
 *         Created: 5/27/2017 @ 9:01 AM
 *         Project: RegexExpress
 */

public class GElemRectFilled extends GraphElemRectangular {
	
	double cx;
	double cy;
	
	private Paint colorFill;
	
	public GElemRectFilled(Paint paint, BasicStroke stroke,
						   Rectangle2D rect, double rotation,
						   Paint colorFill) {
		
		super(GraphElemType.RECTFILLED);
		
		this.paint = paint;
		this.stroke = stroke;
		this.shape = rect;
		setRotation(rotation);
		this.colorFill = colorFill;
		
		cx = rect.getCenterX();
		cy = rect.getCenterY();
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, this);
	}
	
	@Override
	public void drawChild(Graphics2D g2) {
		AffineTransform aft = null;
		
		if (getRotation() > 0) {
			aft = g2.getTransform();
			g2.rotate(getRotationRad(),
					((Rectangle2D) shape).getCenterX(),
					((Rectangle2D) shape).getCenterY());
		}
		
		g2.setPaint(colorFill);
		g2.fill(shape);
		
		g2.setPaint(paint);
		g2.draw(shape);
		
		if (getRotation() > 0) {
			g2.setTransform(aft);
		}
	}
	
	@Override
	public String toString() {
		return log.LogMsgStr("this is a rect filled| ",
				GraphElement.listElemInfo(ID(), paint,
						((Rectangle2D) shape).getX(),
						((Rectangle2D) shape).getY(),
						gf, getRotation()))
				+ " size| " + log.dispVal(((Rectangle2D) shape).getWidth(), ((Rectangle2D) shape).getHeight())
				+ " center| " + log.dispVal(cx, cy);
		
	}
}
