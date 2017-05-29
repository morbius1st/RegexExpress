package pro.cyberstudio.displaylist;

import java.awt.*;

/**
 * @author jeffs
 *         File:    GEString
 *         Created: 5/28/2017 @ 6:29 PM
 *         Project: RegexExpress
 */

abstract class GraphElemString extends GraphElement {
	
	Point insertPt;
	Font font;
	String string;
	Graphics2D	graphics;
	
	public GraphElemString(double rotation, GraphicType graphicType,
						   Paint paint, BasicStroke stroke, Graphics2D graphics,
						   String string, Font font, Point insertPt) {
		super(rotation, graphicType, paint, stroke);
		
		this.insertPt = insertPt;
		this.font = font;
		this.string = string;
		this.graphics = graphics;
		
	}
	
	public Point getInsertPt() {
		return insertPt;
	}
	
}
