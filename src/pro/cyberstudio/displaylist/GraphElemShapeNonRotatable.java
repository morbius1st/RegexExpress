package pro.cyberstudio.displaylist;

import java.awt.Shape;

/**
 * @author jeffs
 *         File:    GEShape
 *         Created: 5/28/2017 @ 6:24 PM
 *         Project: RegexExpress
 */

abstract class GraphElemShapeNonRotatable extends GraphElemNonRotatable {
	
	Shape shape;
	
	GraphElemShapeNonRotatable() {}
	
	GraphElemShapeNonRotatable(GraphElemType graphElemType) {
		super(graphElemType);
	}

}
