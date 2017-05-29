package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import static pro.cyberstudio.utilities.log.*;

/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemRect extends GraphElemRectangular {
	
	public GElemRect(double rotation, Paint paint,
					 BasicStroke stroke, Rectangle2D.Double rect) {
		
		super(rotation, GraphicType.RECT, paint, stroke, rect);
	}
	
	//	public void draw(Graphics2D g2) {
	@Override
	public void draw() {
		LogMsgFmtln("this is a rect|",
				GraphElement.listElemInfo(ID(), paint,
						((Rectangle2D.Double) shape).getX(),
						((Rectangle2D.Double) shape).getY(),
						rotation));
	}
	
}
