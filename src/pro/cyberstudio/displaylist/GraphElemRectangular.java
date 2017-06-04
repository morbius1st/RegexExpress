package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author jeffs
 *         File:    GELinear
 *         Created: 5/28/2017 @ 6:11 PM
 *         Project: RegexExpress
 */

// a graphic element with a length definition but no width
abstract class GraphElemRectangular extends GraphElemRotatable {
	
	public GraphElemRectangular(GraphElemType graphElemType,
								Paint paint, BasicStroke stroke, Shape shape,
								double rotation) {
		
		super(graphElemType, paint, stroke, shape, rotation);
	}
}
