package pro.cyberstudio.displaylist;

import java.awt.*;

/**
 * @author jeffs
 *         File:    GEShape
 *         Created: 5/28/2017 @ 6:24 PM
 *         Project: RegexExpress
 */

abstract class GraphElemShapeRotatable extends GraphElemRotatable {
	
	Shape shape;
	
	GraphElemShapeRotatable() {}
	
	GraphElemShapeRotatable(GraphElemType graphElemType) {
		super(graphElemType);
	}

}
