package pro.cyberstudio.displaylist;

/**
 * @author jeffs
 *         File:    GEShape
 *         Created: 5/28/2017 @ 6:24 PM
 *         Project: RegexExpress
 */

abstract class GraphElemNonRotatable extends GraphElement {
	
	double rotation;
	
	GraphElemNonRotatable() {gf.canRotate = true;}
	
	GraphElemNonRotatable(GraphElemType graphElemType) {
		
		super(graphElemType);
		
		gf.canRotate = true;
	}

}
