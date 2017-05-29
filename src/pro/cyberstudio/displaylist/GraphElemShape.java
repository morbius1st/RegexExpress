package pro.cyberstudio.displaylist;

import java.awt.*;

/**
 * @author jeffs
 *         File:    GEShape
 *         Created: 5/28/2017 @ 6:24 PM
 *         Project: RegexExpress
 */

abstract class GraphElemShape extends GraphElement {
	
	Shape shape;
	
	public GraphElemShape(double rotation, GraphicType graphicType,
						  Paint paint, BasicStroke stroke, Shape shape) {

		super(rotation, graphicType, paint, stroke);
		
		this.shape = shape;
	}
}
