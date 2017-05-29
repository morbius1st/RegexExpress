package pro.cyberstudio.displaylist;

import java.awt.*;

import pro.cyberstudio.regexexpress.Utility;

import static pro.cyberstudio.utilities.log.*;

import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemRect extends GraphElemShape {

//	Rectangle2D.Double	rect
	
	public GElemRect(Point inserPt, Dimension size, double rotation,
					 Color color, BasicStroke stroke) {
		super(inserPt, size, rotation, GraphicType.RECT, color, stroke);
	}
	
	GElemRect(Point inserPt, Dimension size, double rotation,
			  GraphicType graphicType,
			  Color color, BasicStroke stroke) {
		super(inserPt, size, rotation, graphicType, color, stroke);
	}

	@Override
	void setBoundary(Point insertPt, Dimension size) {
		boundary = new Rectangle(insertPt, size);
	}
	
	
	//	public void draw(Graphics2D g2) {
	@Override
	public void draw() {
		LogMsgFmtln("this is a rect|",
				GraphElem.listElemInfo(ID(), paint, getInsertPt()));
	}
	
}
