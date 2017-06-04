package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.geom.AffineTransform;

import pro.cyberstudio.utilities.log;

/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemSimpleString extends GraphElemString {
	
	
	public GElemSimpleString(Paint paint, BasicStroke stroke,
							 Graphics2D graphics, String string,
							 Font font, Point insertPt, double rotation) {
		
		super(GraphElemType.STRING);
		
		this.paint = paint;
		this.stroke = stroke;
		this.graphics = graphics;
		this.string = string;
		this.font = font;
		this.insertPt = insertPt;
		setRotation(rotation);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, this);
	}
	
	
	public void drawChild(Graphics2D g2) {
		AffineTransform aft = null;
		
		if (font != null) {
			g2.setFont(font);
		}
		
		GEStdShapes.drawLocation(g2, Color.CYAN, insertPt);
		
		if (getRotation() > 0) {
			aft = g2.getTransform();
			g2.rotate(getRotationRad(), insertPt.x, insertPt.y);
		}
		
		g2.drawString(string, insertPt.x, insertPt.y);
		
		if (getRotation() > 0) {
			g2.setTransform(aft);
		}

	}
	
	public String toString() {
		return log.LogMsgStr("this is a simple string| ",
				listElemInfo(ID(), paint, insertPt, gf, getRotation()));
	}
	
}
