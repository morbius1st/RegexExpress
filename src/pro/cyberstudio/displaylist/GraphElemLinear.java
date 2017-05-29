package pro.cyberstudio.displaylist;

import java.awt.*;

/**
 * @author jeffs
 *         File:    GELinear
 *         Created: 5/28/2017 @ 6:11 PM
 *         Project: RegexExpress
 */

// a graphic element with a length definition but no width
abstract class GraphElemLinear extends GraphElemShape {
	
	public GraphElemLinear(double rotation, GraphicType graphicType,
						   Paint paint, BasicStroke stroke, Shape shape) {
		
		super(rotation, graphicType, paint, stroke, shape);
	}
}
