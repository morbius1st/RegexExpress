package pro.cyberstudio.displaylist;

import java.awt.*;

/**
 * @author jeffs
 *         File:    GEShape
 *         Created: 5/28/2017 @ 6:24 PM
 *         Project: RegexExpress
 */

abstract class GraphElemShape extends GraphElem {
	
	public GraphElemShape(Point insertPt, Dimension size, double rotation,
						  GraphicType graphicType, Color color, BasicStroke stroke) {
		super(rotation, graphicType, color, stroke);
		
		setBoundary(insertPt, size);
	}

}
