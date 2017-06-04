package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static pro.cyberstudio.regexexpress.Utility.dispVal;

/**
 * @author jeffs
 *         File:    dlItemFilledRect
 *         Created: 5/27/2017 @ 9:01 AM
 *         Project: RegexExpress
 */

public class GElemRectFilled extends GraphElemRectangular {

//	GraphicFeatures gf = new GraphicFeatures();
	
	private Paint colorFill;
	
	public GElemRectFilled(Paint paint, BasicStroke stroke,
						   Rectangle2D rect, double rotation,
						   Paint colorFill) {
		
		super(GraphElemType.RECTFILLED, paint, stroke, rect, rotation);
		
		this.colorFill = colorFill;
		
		gf.canPaint = false;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, this);
	}
	
	@Override
	public void drawChild(Graphics2D g2) {
		
		g2.setPaint(colorFill);
		g2.fill(shape);
		
		g2.setPaint(paint);
		g2.draw(shape);
	}
	
	@Override
	public String toString() {
		return "this is a rect filled| " +
				GraphElement.listElemInfo(ID(), paint,
						((Rectangle2D) shape).getX(),
						((Rectangle2D) shape).getY(),
						gf, rotation);
		
	}
}
