package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author jeffs
 *         File:    GEString
 *         Created: 5/28/2017 @ 6:29 PM
 *         Project: RegexExpress
 */

abstract class GraphElemString extends GraphElemRotatable {
	
	Point 		insertPt;
	Font 		font;
	String 		string;
	Graphics2D	graphics;
	
	GraphElemString() {}
	
	GraphElemString(GraphElemType graphElemType) {
		super(graphElemType);
	}
	
	
	
	public Point getInsertPt() {
		return insertPt;
	}
	
	Rectangle2D getBounds() {
		Rectangle2D bounds = font.getStringBounds(string, graphics.getFontRenderContext());
		return bounds;
	}
}
