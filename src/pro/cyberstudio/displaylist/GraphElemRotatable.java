package pro.cyberstudio.displaylist;

import java.awt.*;

/**
 * @author jeffs
 *         File:    GEShape
 *         Created: 5/28/2017 @ 6:24 PM
 *         Project: RegexExpress
 */

abstract class GraphElemRotatable extends GraphElemShape {
	
	double rotation;
	
	public GraphElemRotatable(GraphElemType graphElemType,
							  Paint paint, BasicStroke stroke, Shape shape,
							  double rotation) {

		super(graphElemType, paint, stroke, shape);
		
		this.rotation = rotation;
		
		gf.canRotate = true;
	}
	
	
}
