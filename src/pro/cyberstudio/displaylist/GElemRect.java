package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.*;

/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemRect extends GraphElemRectangular {
	
	public GElemRect(Paint paint,
					 BasicStroke stroke, Rectangle2D rect,
					 double rotation) {
		
		super(GraphElemType.RECT, paint, stroke, rect, rotation);
	
	}
	
	public void draw(Graphics2D g2) {
		super.draw(g2, this);
		
	}
	
	
	//	public void draw(Graphics2D g2) {
	@Override
	public void drawChild(Graphics2D g2) {
		g2.draw(shape);
	}
	
	@Override
	public String toString() {
		return "this is a rect| " +
			GraphElement.listElemInfo(ID(), paint,
			((Rectangle2D) shape).getX(),
			((Rectangle2D) shape).getY(),
			gf, rotation);
	}
	
}
