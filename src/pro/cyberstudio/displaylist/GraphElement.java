package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.*;

import static pro.cyberstudio.regexexpress.Utility.*;


//import pro.cyberstudio.displaylist.DisplayListUtilities.GraphicType;

/**
 * @author jeffs
 *         File:    DisplayListItem
 *         Created: 5/25/2017 @ 10:42 PM
 *         Project: RegexExpress
 */

// this is the root graphic element

abstract public class GraphElement extends Element {

	GraphElemType graphElemType;
	Paint paint = null;
	BasicStroke stroke = null;
	
	GraphElemFeatures gf = new GraphElemFeatures();

	GraphElement(GraphElemType graphElemType,
						Paint paint,
						BasicStroke stroke) {
		
		super(ElementType.GraphicElement, graphElemType);
		
		this.graphElemType = graphElemType;
		this.paint = paint;
		this.stroke = stroke;
	}

	class GraphElemFeatures {
		boolean canPaint = true;
		boolean canStroke = true;
		boolean canRotate = false;
	}
	
	
	
	//	abstract public void draw(Graphics2D g2);
	abstract void draw(Graphics2D g2);
	
	abstract void drawChild(Graphics2D g2);
	
	void draw(Graphics2D g2, GraphElement ge) {
	
		Stroke stroke_save = null;
		Paint paint_save = null;

		if (ge.gf.canPaint && ge.paint != null) {
			paint_save = g2.getPaint();
			g2.setPaint(ge.paint);
		}

		if (ge.gf.canStroke && ge.stroke != null) {
			stroke_save = g2.getStroke();
			g2.setStroke(ge.stroke);
		}

		drawChild(g2);

		// restore prior settings
		if (stroke_save != null) {
			g2.setStroke(stroke_save);
		}

		if (paint_save != null) {
			g2.setPaint(paint_save);
		}
	
	}
	
	static String listElemInfo(String id, Paint paint, double x, double y,
							   GraphElemFeatures gf, double rotation) {
		return  listElemInfo(id, paint, new Point2D.Double(x, y), gf)
				+ " rotation| " + dispVal(rotation);
	}

	static String listElemInfo(String id, Paint paint, double x, double y,
							   GraphElemFeatures gf) {
		return  listElemInfo(id, paint, new Point2D.Double(x, y), gf);
	}
	
	static String listElemInfo(String id, Paint paint, Point2D insertPt,
							   GraphElemFeatures gf, double rotation) {
		return  listElemInfo(id, paint, insertPt, gf)
				+ " rotation| " + dispVal(rotation);
	}
	
	static String listElemInfo(String id, Paint paint, Point2D insertPt,
							   GraphElemFeatures gf) {
		String msg = "ID| " + dispVal(id, 25)
				+ " color| " + dispVal(paint)
				+ " insert pt| " + dispVal(insertPt)
				+ " canPaint?| " + gf.canPaint
				+ " canStroke?| " + gf.canStroke
				;
		
		return msg;
	}
	
	@Override
	public abstract String toString();


}
