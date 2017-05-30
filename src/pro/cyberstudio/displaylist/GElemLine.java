package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

import static pro.cyberstudio.regexexpress.Utility.*;


/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemLine extends GraphElemLinear {
	
	public GElemLine(double rotation, Paint paint,
					 BasicStroke stroke, Line2D line) {
		
		super(rotation, GraphicType.LINE, paint, stroke, line);
	}

//	public void draw(Graphics2D g2) {
	public void draw() {
		LogMsgFmtln("this is a line|",
				GraphElement.listElemInfo(ID(), paint, ((Line2D) shape).getP1(), rotation));
	}
}
