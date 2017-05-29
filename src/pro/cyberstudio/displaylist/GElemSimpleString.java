package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.font.LineMetrics;

import static pro.cyberstudio.utilities.log.*;

/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemSimpleString extends GraphElemString {
	
	Point insertPt;
	
	public GElemSimpleString(double rotation, Paint paint, BasicStroke stroke,
							 Graphics2D graphics, String string, Font font, Point insertPt) {
		
		super(rotation, GraphicType.STRING, paint, stroke, graphics, string, font, insertPt);

		this.insertPt = insertPt;
		this.graphics = graphics;
	}

	Dimension determineSize(Font font, String string) {
		LineMetrics lm = font.getLineMetrics(string, graphics.getFontRenderContext());
		return new Dimension(30, 10);
	}
	
//	public void draw(Graphics2D g2) {
	public void draw() {
		LogMsgFmtln("this is a simple string|",
				GraphElement.listElemInfo(ID(), paint, insertPt, rotation));
	}
}
