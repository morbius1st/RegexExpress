package pro.cyberstudio.displaylist;

/**
 * @author jeffs
 *         File:    GEShape
 *         Created: 5/28/2017 @ 6:24 PM
 *         Project: RegexExpress
 */

abstract class GraphElemRotatable extends GraphElement {
	
	private double rotation;	// degrees, + = ccw
	
	GraphElemRotatable() {gf.canRotate = true;}
	
	GraphElemRotatable(GraphElemType graphElemType) {
		
		super(graphElemType);
		
		gf.canRotate = true;
	}
	
	void setRotation(double rotation) {
		this.rotation = ((360 - rotation) % 360);
	}

	double getRotation() {
		return rotation;
	}
	
	double getRotationRad() {
		return Math.toRadians(rotation);
	}
}
