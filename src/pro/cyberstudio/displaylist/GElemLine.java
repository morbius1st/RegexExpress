package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.Line2D;

import pro.cyberstudio.utilities.log;


/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */


// note that, for a line, the rotation does not apply
public class GElemLine extends GraphElemLineBased {

	public GElemLine(Paint paint,
					 BasicStroke stroke, Line2D line) {
		
		super(GraphElemType.LINE, paint, stroke, line);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, this);
	}
	
	public void drawChild(Graphics2D g2) {
		g2.draw(shape);
	}
	
	@Override
	public String toString() {
		return log.LogMsgStr( "this is a line| ",
				listElemInfo(ID(), paint, ((Line2D) shape).getP1(), gf));
		
	}
}
