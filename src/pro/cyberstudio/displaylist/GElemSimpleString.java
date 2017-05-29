package pro.cyberstudio.displaylist;

import java.awt.*;
import java.awt.font.LineMetrics;

import static pro.cyberstudio.utilities.log.*;
import static pro.cyberstudio.utilities.point.*;
import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    ElemRect
 *         Created: 5/27/2017 @ 4:19 PM
 *         Project: RegexExpress
 */

public class GElemSimpleString extends GraphElemString {
	
	
	public GElemSimpleString(Point inserPt, double rotation,
							 Color color, BasicStroke stroke,
							 Font font, String string,
							 Graphics2D graphics) {
		
		super(inserPt, rotation, GraphicType.STRING, color, stroke, font, string, graphics);

		this.graphics = graphics;
		setBoundary(inserPt, null);
		
	}
	
	public GElemSimpleString(Point inserPt, double rotation,
							 GraphicType graphicType, Color color, BasicStroke stroke,
							 Font font, String string,
							 Graphics2D graphics) {
		
		super(inserPt, rotation, GraphicType.STRING, color, stroke, font, string, graphics);

		this.graphics = graphics;
		setBoundary(inserPt, null);
		
	}

	Dimension determineSize(Font font, String string) {
		LineMetrics lm = font.getLineMetrics(string, graphics.getFontRenderContext());
		return new Dimension(30, 10);
	}
	
	void setBoundary(Point insertPt, Dimension size) {
		size = determineSize(font, string);
		boundary = new Rectangle(addPoints(getInsertPt(), new Point(0, -size.height)), size);
	}
	
//	public void draw(Graphics2D g2) {
	public void draw() {
		LogMsgFmtln("this is a simple string|",
				GraphElem.listElemInfo(ID(), paint, getInsertPt()));
	}
}
