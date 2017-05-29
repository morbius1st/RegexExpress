package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import static pro.cyberstudio.regexexpress.Utility.dispVal;
import static pro.cyberstudio.utilities.log.*;

/**
 * @author jeffs
 *         File:    dlItemFilledRect
 *         Created: 5/27/2017 @ 9:01 AM
 *         Project: RegexExpress
 */

public class GElemRectFilled extends GraphElemRectangular {
	
	private Paint colorFill;
	
	public GElemRectFilled(double rotation, Paint paint, BasicStroke stroke, Paint paintFill, Rectangle2D.Double rect) {
		
		super(rotation, GraphicType.RECTFILLED, paint, stroke, rect);
		
		this.colorFill = paintFill;
	}
	
//	public void draw(Graphics2D g2) {
	@Override
	public void draw() {
		LogMsgFmtln("this is a rect filled|",
				GraphElement.listElemInfo(ID(), paint,
						((Rectangle2D.Double) shape).getX(),
						((Rectangle2D.Double) shape).getY(),
						rotation));
	}
}
