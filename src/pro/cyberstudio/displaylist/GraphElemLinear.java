package pro.cyberstudio.displaylist;

import java.awt.*;

/**
 * @author jeffs
 *         File:    GELinear
 *         Created: 5/28/2017 @ 6:11 PM
 *         Project: RegexExpress
 */

// a graphic element with a length definition but no width
abstract class GraphElemLinear extends GraphElem {
	
	public GraphElemLinear(Point insertPt, int length, double rotation,
						   GraphicType graphicType, Color color, BasicStroke stroke) {
		super(rotation, graphicType, color, stroke);
		
		setBoundary(insertPt, new Dimension(length, 0));
	}
}
