package pro.cyberstudio.displaylist;

import java.awt.*;
import static pro.cyberstudio.regexexpress.Utility.*;


//import pro.cyberstudio.displaylist.DisplayListUtilities.GraphicType;

/**
 * @author jeffs
 *         File:    DisplayListItem
 *         Created: 5/25/2017 @ 10:42 PM
 *         Project: RegexExpress
 */

// this is the root graphic element

abstract class GraphElem extends Element {

	private GraphicType graphicType;
	Paint paint = null;
	BasicStroke stroke = null;
	Rectangle boundary = new Rectangle(new Point(0, 0), new Dimension(0 ,0));
	double rotation;
	
	
	public GraphElem(double rotation, GraphicType graphicType,
					 Paint paint,
					 BasicStroke stroke) {
		
		super(ElementType.GraphicElement, graphicType);
		
		this.rotation = rotation;
		this.graphicType = graphicType;
		this.paint = paint;
		this.stroke = stroke;
	}
	
	GraphicType getGraphicType() {
		return graphicType;
	}
	
	//	abstract public void draw(Graphics2D g2);
	abstract void draw();
	
	abstract void setBoundary(Point insertPt, Dimension size);

	Rectangle getBoundary() {
		return boundary;
	}
	
	public Point getInsertPt() {
		return boundary.getLocation();
	}

	public void setInsertPt(Point insertPt) {
		this.boundary.setLocation(insertPt);
	}

	public Dimension getSize() {
		return boundary.getSize();
	}
	void setSize(Dimension size) {boundary.setSize(size);}

	int getInsertX() {
		return boundary.x;
	}

	int getInsertY() {
		return boundary.y;
	}

	int getWidth() {
		return boundary.width;
	}

	int getHeight() {
		return boundary.height;
	}
	
	public static String listElemInfo(String id, Paint paint, Point insertPt) {
		String msg = " ID| " + dispVal(id, 25)
				+ " color| " + dispVal(paint)
				+ " insert pt| " + dispVal(insertPt);

		return msg;
	}


}
