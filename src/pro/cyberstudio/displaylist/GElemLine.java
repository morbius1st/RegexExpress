package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.Line2D;

import static pro.cyberstudio.regexexpress.Utility.*;
import static pro.cyberstudio.utilities.log.*;


/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemLine extends GraphElemLinear {
	
	Line2D.Double line;
	
	public GElemLine(Point inserPt, int length, double rotation,
					 Color color, BasicStroke stroke) {
		super(inserPt, length, rotation, GraphicType.LINE, color, stroke);
		
	}
	
	// for a sub-classes
	GElemLine(Point inserPt, int length, double rotation,
			  GraphicType graphicType,
			  Color color, BasicStroke stroke) {
		super(inserPt, length, rotation, graphicType, color, stroke);
	}
	
	@Override
	void setBoundary(Point insertPt, Dimension size) {
		boundary = new Rectangle(insertPt, size);
	}
	
	
//	public void draw(Graphics2D g2) {
	public void draw() {
		LogMsgFmtln("this is a line|",
				GraphElem.listElemInfo(ID(), paint, getInsertPt()));
	}
}
