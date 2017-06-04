package pro.cyberstudio.displaylist;

/**
 * @author jeffs
 *         File:    GELinear
 *         Created: 5/28/2017 @ 6:11 PM
 *         Project: RegexExpress
 */

// a graphic element with a length definition but no width
abstract class GraphElemLineBased extends GraphElemShapeNonRotatable {
	
	GraphElemLineBased() {}
	
	GraphElemLineBased(GraphElemType graphElemType) {
		super(graphElemType);
	}

}
