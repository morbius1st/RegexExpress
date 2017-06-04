package pro.cyberstudio.displaylist;

import java.awt.*;

/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemSimpleString extends GraphElemString {
	
	public GElemSimpleString(Paint paint, BasicStroke stroke,
							 Graphics2D graphics, String string,
							 Font font, Point insertPt) {
		
		super(GraphElemType.STRING, paint, stroke, graphics, string, font, insertPt);

	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2, this);
	}
	
	
	public void drawChild(Graphics2D g2) {
		if (font != null) {
			g2.setFont(font);
		}
		
		
		
		g2.drawString(string, insertPt.x, insertPt.y);
	}
	
	public String toString() {
		return "this is a simple string| " +
				listElemInfo(ID(), paint, insertPt, gf);
	}
	
}
