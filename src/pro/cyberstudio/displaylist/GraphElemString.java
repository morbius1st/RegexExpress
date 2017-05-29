package pro.cyberstudio.displaylist;

import java.awt.*;

/**
 * @author jeffs
 *         File:    GEString
 *         Created: 5/28/2017 @ 6:29 PM
 *         Project: RegexExpress
 */

abstract class GraphElemString extends GraphElem {
	
	Font font;
	String string;
	Graphics2D	graphics;
	
	public GraphElemString(Point insertPt, double rotation,
						   GraphicType graphicType, Color color, BasicStroke stroke,
						   Font font, String string, Graphics2D graphics) {
		super(rotation, graphicType, color, stroke);
		
		this.font = font;
		this.string = string;
		
	}
	
}
