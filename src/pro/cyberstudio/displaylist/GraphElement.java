package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.Point2D;

import static pro.cyberstudio.regexexpress.Utility.*;


//import pro.cyberstudio.displaylist.DisplayListUtilities.GraphicType;

/**
 * @author jeffs
 *         File:    DisplayListItem
 *         Created: 5/25/2017 @ 10:42 PM
 *         Project: RegexExpress
 */

// this is the root graphic element

abstract class GraphElement extends Element {

	GraphicType graphicType;
	Paint paint = null;
	BasicStroke stroke = null;
	double rotation;
	
	
	public GraphElement(double rotation, GraphicType graphicType,
						Paint paint,
						BasicStroke stroke) {
		
		super(ElementType.GraphicElement, graphicType);
		
		this.rotation = rotation;
		this.graphicType = graphicType;
		this.paint = paint;
		this.stroke = stroke;
	}
	
	//	abstract public void draw(Graphics2D g2);
	abstract void draw();

	static String listElemInfo(String id, Paint paint, double x, double y, double rotation) {
		return  listElemInfo(id, paint, new Point2D.Double(x, y), rotation);
	}
	
	static String listElemInfo(String id, Paint paint, Point2D insertPt, double rotation) {
		String msg = " ID| " + dispVal(id, 25)
				+ " color| " + dispVal(paint)
				+ " insert pt| " + dispVal(insertPt)
				+ " rotation| " + dispVal(rotation);
		
		return msg;
	}


}
